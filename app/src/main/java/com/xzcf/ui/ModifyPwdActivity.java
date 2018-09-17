package com.xzcf.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xzcf.App;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.response.LoginInfo;
import com.xzcf.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ModifyPwdActivity extends AppCompatActivity {

    @BindView(R.id.iBtnBack)
    ImageButton iBtnBack;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.etOldPwd)
    EditText etOldPwd;
    @BindView(R.id.etNewPwd)
    EditText etNewPwd;
    @BindView(R.id.etNewPwd2)
    EditText etNewPwd2;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    private Disposable disposable;

    public static Intent startAction(Context context) {
        return new Intent(context, ModifyPwdActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        ButterKnife.bind(this);
        iBtnBack.setVisibility(View.VISIBLE);
        tvConfirm.setVisibility(View.VISIBLE);
        tvTitle.setText("修改密码");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @OnClick(R.id.iBtnBack)
    public void onIBtnBackClicked() {
        finish();
    }

    @OnClick(R.id.tvConfirm)
    public void onTvConfirmClicked() {
        String userPwd = etOldPwd.getText().toString().trim();
        String newPwd = etNewPwd.getText().toString().trim();
        String new2Pwd = etNewPwd2.getText().toString().trim();
        if (!newPwd.equals(new2Pwd)) {
            ToastUtil.showToast("密码不匹配");
            return;
        }
        LoginInfo loginInfo = App.context().getLoginInfo();
        String memberId = loginInfo.getMemberId();
        String token = "";

        disposable = DataManager.getInstance().modifyPwd(memberId, userPwd, newPwd, token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(myResponse -> {
                    ToastUtil.showToast(myResponse.getMsg());
                    if (myResponse.getCode().equals("0")) {
                        finish();
                    }
                }, throwable -> {});
    }

    @OnClick(R.id.iBtnClear)
    public void onIBtnClearClicked() {
        etOldPwd.setText("");
    }
}
