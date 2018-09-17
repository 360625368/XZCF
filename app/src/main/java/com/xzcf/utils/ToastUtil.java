package com.xzcf.utils;

import android.widget.Toast;

import com.xzcf.App;


public class ToastUtil {

    private static Toast mToast;

    public static void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(App.context(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showToast(int resID) {
        if (mToast == null) {
            mToast = Toast.makeText(App.context(), resID, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resID);
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showToastLong(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(App.context(), msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void showToastLong(int resID) {
        if (mToast == null) {
            mToast = Toast.makeText(App.context(), resID, Toast.LENGTH_LONG);
        } else {
            mToast.setText(resID);
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }
}
