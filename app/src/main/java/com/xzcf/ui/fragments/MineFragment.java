package com.xzcf.ui.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xzcf.App;
import com.xzcf.Constants;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.response.LoginInfo;
import com.xzcf.ui.AboutActivity;
import com.xzcf.ui.BargainRecordActivity;
import com.xzcf.ui.CancelEntrustActivity;
import com.xzcf.ui.EntrustRecordActivity;
import com.xzcf.ui.FundStockActivity;
import com.xzcf.ui.FundsFlowActivity;
import com.xzcf.ui.LoginActivity;
import com.xzcf.ui.PersonalCenterActivity;
import com.xzcf.ui.StockBuyActivity;
import com.xzcf.ui.StockSellActivity;
import com.xzcf.utils.AppUtils;
import com.xzcf.utils.CleanUtils;
import com.xzcf.utils.StockRuleUtils;
import com.xzcf.utils.ToastUtil;
import com.xzcf.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.llLogged)
    LinearLayout llLogged;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.tvAccountName)
    TextView tvAccountName;
    @BindView(R.id.clUserInfo)
    ConstraintLayout clUserInfo;
    private boolean mLogged = false;
    private LoginInfo mLoginInfo = null;
    private Disposable getVersionDisposable;
    private Listener mListener;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        int statusBarHeight = UiUtils.getStatusBarHeight(getContext());
        clUserInfo.getLayoutParams().height += statusBarHeight;
        clUserInfo.setPadding(0, statusBarHeight, 0, 0);
        return view;
    }

    private void updateView() {
        try{
            mLogged = App.context().isLogged();
            mLoginInfo = App.context().getLoginInfo();
            if (mLogged) {
                tvAccountName.setVisibility(View.VISIBLE);
                if (mLoginInfo != null) {
                    tvAccountName.setText(mLoginInfo.getRealname());
                }
                if (StockRuleUtils.isTestAccount()) {
                    llLogged.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.GONE);
                    return;
                }
                llLogged.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.GONE);
            } else {
                llLogged.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                tvAccountName.setVisibility(View.GONE);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ivIcon, R.id.iBtnSetting})
    public void onIvIconClicked() {
        if (mLogged)
            startActivity(PersonalCenterActivity.startIntent(getContext()));
        else
            startActivity(LoginActivity.startAction(getContext()));
    }

    @OnClick(R.id.llAssetsManager)
    public void onLlAssetsManagerClicked() {
        startActivity(new Intent(getContext(), FundStockActivity.class));
    }

    @OnClick(R.id.btnBuy)
    public void onBtnBuyClicked() {
        startActivity(StockBuyActivity.newIntent(getContext(), "", ""));
    }

    @OnClick(R.id.btnSell)
    public void onBtnSellClicked() {
        startActivity(StockSellActivity.newIntent(getContext(), "", ""));
    }

    @OnClick(R.id.btnCancelOrder)
    public void onBtnCancelOrderClicked() {
        startActivity(CancelEntrustActivity.startAction(getContext()));
    }

    @OnClick(R.id.btnEntrust)
    public void onBtnEntrustClicked() {
        startActivity(EntrustRecordActivity.startAction(getContext()));
    }

    @OnClick(R.id.btnClinchDeal)
    public void onBtnClinchDealClicked() {
        startActivity(BargainRecordActivity.startAction(getContext()));
    }

    @OnClick(R.id.btnCapitalFlow)
    public void onBtnCapitalFlowClicked() {
        startActivity(FundsFlowActivity.startAction(getContext()));
    }

    @OnClick(R.id.llHelp)
    public void onLlHelpClicked() {
        ToastUtil.showToast("操作指引");
    }

    @OnClick(R.id.llVersionUpdate)
    public void onLlVersionUpdateClicked() {
        if (getVersionDisposable != null) {
            getVersionDisposable.dispose();
            getVersionDisposable = null;
        }
        String version = AppUtils.getVersion(getContext());
        getVersionDisposable = DataManager.getInstance().getAppVersion(version)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(appVersionResponse -> {
                    if (appVersionResponse.getCode().equals("0")) {
                        if (TextUtils.isEmpty(appVersionResponse.getFileUrl())) {
                            ToastUtil.showToast("没有新版本");
                        } else {
                            if (mListener != null) {
                                String apkUrl = Constants.APP_URI_HOST+appVersionResponse.getFileUrl();
                                mListener.downloadApk(apkUrl);
                            }
                        }
                    } else {
                        ToastUtil.showToast(appVersionResponse.getMsg());
                    }
                }, throwable -> {
                    ToastUtil.showToast("获取版本失败");
                });
    }

    @OnClick(R.id.llClearCache)
    public void onLlClearCacheClicked() {
        ToastUtil.showToast("缓存已清理");
        clearCache();
    }

    @OnClick(R.id.llContext)
    public void onLlContextClicked() {
    }

    @OnClick(R.id.btnLogin)
    public void onBtnLoginClicked() {
        startActivity(LoginActivity.startAction(getContext()));
    }

    @OnClick(R.id.llAbout)
    public void onAboutClicked(){
        startActivity(AboutActivity.newIntent(getContext()));
    }

    public interface Listener {
        void downloadApk(String apkUrl);
    }

    private void clearCache(){
        try{
            String path = "/sdcard/AXZCF/Crash/";
            CleanUtils.cleanCustomDir(path);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void requestPermission(){
        //检查权限
        if (getActivity() != null) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    ) {
                //进入到这里代表没有权限.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO}, 0);
            }
        }

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        updateView();
    }
}
