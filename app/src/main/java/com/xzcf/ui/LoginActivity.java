package com.xzcf.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.xzcf.App;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.ui.view.VerificationCodeView;
import com.xzcf.utils.DialogUtils;
import com.xzcf.utils.SPUtils;
import com.xzcf.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.xzcf.Constants.PREF_S_LAST_TOUCH_TIME_INFO;
import static com.xzcf.Constants.PREF_S_LOGIN_INFO;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.iBtnBack)
    ImageButton iBtnBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.etVerificationCode)
    EditText etVerificationCode;
    @BindView(R.id.vcvCode)
    VerificationCodeView vcvCode;

    private String userName;
    private String password;
    private boolean verificationPassed = false;
    private MaterialDialog progressDialog;
    private Disposable loginDisposable;

    public static Intent startAction(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        vcvCode.refreshCode();
        iBtnBack.setVisibility(View.VISIBLE);
        tvTitle.setText("登录");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginDisposable != null) {
            loginDisposable.dispose();
            loginDisposable = null;
        }
    }

    private void verification() {
        verificationPassed = false;
        userName = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showToast("请输入用户名");
            return;
        }

        password = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast("请输入密码");
            return;
        }

        String vCode = etVerificationCode.getText().toString().trim();
        if (TextUtils.isEmpty(vCode)) {
            ToastUtil.showToast("请输入验证码");
            return;
        }
        if (!vCode.toLowerCase().equals(vcvCode.getvCode().toLowerCase())) {
            ToastUtil.showToast("验证码错误");
            return;
        }
        verificationPassed = true;
    }

    @OnClick(R.id.iBtnBack)
    public void onIBtnBackClicked() {
        finish();
    }

    @OnClick(R.id.vcvCode)
    public void onVcvCodeClicked() {
        vcvCode.refreshCode();
    }

    @OnClick(R.id.btnLogin)
    public void onBtnLoginClicked() {
        verification();
        if (!verificationPassed) return;
        progressDialog = DialogUtils.buildProgressDialogsDialog(getContext(), "请稍后", "登录中...");
        progressDialog.show();
        loginDisposable = DataManager.getInstance()
                .login(userName, password, "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(loginInfo -> {
                    progressDialog.dismiss();
                    if ("0".equalsIgnoreCase(loginInfo.getCode())) {
                        ToastUtil.showToast("登录成功！");
                        App.getPreferences().edit().putString(PREF_S_LOGIN_INFO, loginInfo.toString()).apply();
                        // 初始化上次操作时间为登录成功的时间
                        long mLastActionTime = System.currentTimeMillis();
                        // 更新统计时间
                        SPUtils.getInstance(getContext()).put(PREF_S_LAST_TOUCH_TIME_INFO,mLastActionTime);
                        App.context().loadLoginInfo();
                        // 启动计时
                        startTimer();
                        finish();
                    } else {
                        ToastUtil.showToast(loginInfo.getMsg());
                    }
                }, throwable -> {
                    progressDialog.dismiss();
                });
    }
}
