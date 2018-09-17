package com.xzcf.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;

import com.xzcf.App;
import com.xzcf.Constants;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.response.LoginInfo;
import com.xzcf.utils.SPUtils;
import com.xzcf.utils.UtilHelpers;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.SupportActivity;

import static com.xzcf.Constants.PREF_S_LAST_TOUCH_TIME_INFO;


public class BaseActivity extends SupportActivity {

    private volatile long mLastActionTime; // 上一次操作时间
    private Disposable loginOutDisposable;


    public FragmentManager getFM() {
        return getSupportFragmentManager();
    }

    public void addFragment(Fragment fragment) {
        getFM().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

    public void replaceFragment(Fragment fragment) {
        getFM().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    //
    private synchronized void setTime() {
        mLastActionTime = System.currentTimeMillis();
    }

    private synchronized long getTime() {
        return mLastActionTime;
    }


    public Context getContext() {
        return this;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.context().loadLoginInfo();
    }

    // 登录成功，开始计时
    public void startTimer() {
        try {
            setTime();
            runInterval();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 每当用户接触了屏幕，都会执行此方法
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    View view = getCurrentFocus();
                    UtilHelpers.hideKeyboard(event, view, BaseActivity.this);//调用方法判断是否需要隐藏键盘
                    break;
                case MotionEvent.ACTION_UP:
                    setTime();
                    // 更新统计时间
                    SPUtils.getInstance(getContext()).put(PREF_S_LAST_TOUCH_TIME_INFO, getTime());
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return super.dispatchTouchEvent(event);
    }


    private void runInterval() {
        loginOutDisposable = Observable.interval(5, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    long currentTime = SPUtils.getInstance(getContext()).getLong(PREF_S_LAST_TOUCH_TIME_INFO, System.currentTimeMillis());
                    // 15分钟未操作停止计时并退出登录
                    if (System.currentTimeMillis() - currentTime > Constants.LOGIN_OUT_TIME) {
                        stopTimer();// 停止计时任务
                        resetLoginInfo();//退出登录状态
                    }
                });
    }


    public void resetLoginInfo() {
        try {
            if (App.context().isLogged()) {
                LoginInfo loginInfo = App.context().getLoginInfo();
                String memberId = loginInfo.getMemberId();
                String token = "";
                Disposable subscribe = DataManager.getInstance().logout(memberId, token)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(myResponse -> {
                            if (myResponse.getCode().equals("0")) {
                                App.clearPreferences();
                                SPUtils.getInstance(getContext()).clear();
                                App.context().loadLoginInfo();
                            }
                        }, throwable -> {
                        });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 停止计时任务
    public void stopTimer() {
        try {
            System.out.println("*************** 取消计时");
            if (loginOutDisposable != null && !loginOutDisposable.isDisposed()) {
                loginOutDisposable.dispose();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
