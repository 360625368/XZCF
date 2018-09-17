package com.xzcf.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xzcf.App;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.response.LoginInfo;
import com.xzcf.utils.SPUtils;
import com.xzcf.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PersonalCenterActivity extends BaseActivity {

    private Disposable disposable;

    public static Intent startIntent(Context context) {
        return new Intent(context, PersonalCenterActivity.class);
    }

    @BindView(R.id.iBtnBack)
    ImageButton iBtnBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        ButterKnife.bind(this);
        iBtnBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.personal_center_title);
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

    @OnClick(R.id.llModifyPwd)
    public void onLlModifyPwdClicked() {
        startActivity(ModifyPwdActivity.startAction(getContext()));
    }

    @OnClick(R.id.btnExit)
    public void onBtnExitClicked() {
        LoginInfo loginInfo = App.context().getLoginInfo();
        String memberId = loginInfo.getMemberId();
        String token = "";
        disposable = DataManager.getInstance().logout(memberId, token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(myResponse -> {
                    ToastUtil.showToast(myResponse.getMsg());
                    if (myResponse.getCode().equals("0")) {
                        stopTimer();
                        SPUtils.getInstance(getContext()).clear();
                        App.clearPreferences();
                        App.context().loadLoginInfo();
                        finish();
                    }
                }, throwable -> {});
    }
}
