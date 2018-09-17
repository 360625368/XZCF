package com.xzcf;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.xzcf.data.data.response.LoginInfo;
import com.xzcf.utils.CrashHandler;
import com.xzcf.utils.SPUtils;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

import static com.xzcf.Constants.PREF_S_LAST_TOUCH_TIME_INFO;

public class App extends Application {
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    private static App mContext = null;
    private static SharedPreferences preferences;
    private boolean mLogged = false;
    private LoginInfo mLoginInfo = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mContext = this;
    }

    public static App context() {
        return mContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        initLogger();
        initFragmentManager();
        initSharedPreferences();
        initData();
        //======== 日志收集初始化
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    private void initData() {
        loadLoginInfo();
    }

    private void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }


    private void initFragmentManager() {
        // https://github.com/YoKeyword/Fragmentation
        // Fragmentation is recommended to initialize in the Application
        Fragmentation.builder()
                // show stack view. Mode: BUBBLE, SHAKE, NONE
                .stackViewMode(Fragmentation.NONE)
                .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)
                /**
                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                        // Bugtags.sendException(e);
                    }
                })
                .install();
    }


    private void initSharedPreferences() {
        preferences = this.getSharedPreferences(Constants.PREFS_FILE_NAME, MODE_PRIVATE);
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    public static void clearPreferences() {
        preferences.edit().clear().apply();
    }

    public void loadLoginInfo() {
        try {
            String loginInfoJson = App.getPreferences().getString(Constants.PREF_S_LOGIN_INFO, "");
            long currentTime = SPUtils.getInstance(mContext).getLong(PREF_S_LAST_TOUCH_TIME_INFO, 0L);
            if (TextUtils.isEmpty(loginInfoJson)) {
                mLogged = false;
            } else {
                if (System.currentTimeMillis() - currentTime > Constants.LOGIN_OUT_TIME) {
                    // 超过15分钟
                    mLogged = false;
                    SPUtils.getInstance(mContext).put(PREF_S_LAST_TOUCH_TIME_INFO, 0L);
                    App.clearPreferences();
                    return;
                }
                mLogged = true;
                mLoginInfo = LoginInfo.fromJson(loginInfoJson);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isLogged() {
        return mLogged;
    }

    public LoginInfo getLoginInfo() {
        if (mLoginInfo == null) {
            loadLoginInfo();
        }
        return mLoginInfo;
    }
}
