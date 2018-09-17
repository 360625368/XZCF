package com.xzcf.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xzcf.R;

import java.util.List;

public class DialogUtils {

    public static MaterialDialog buildProgressDialogsDialog(Context context, String title, String content) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .progress(true, 0)
                .build();
    }

    public static MaterialDialog buildXianJiaChoiceDialog(Context context, String title, String cancel, List<String> characters, MaterialDialog.ListCallback listCallback){
        return new MaterialDialog.Builder(context)
                .title(title)
                .items(characters)
                .negativeText(cancel)
                .itemsCallback(listCallback)
                .build();
    }

    public static MaterialDialog buildBuyDialog(Context context,
                                                String title,
                                                String zqmc,
                                                String zqdm,
                                                String bjfs,
                                                String wtjg,
                                                String wtsl,
                                                String cancel,
                                                String confirm,
                                                MaterialDialog.SingleButtonCallback singleButtonCallback){
        View view = LayoutInflater.from(context).inflate(R.layout.view_buy_dialog, null);
        TextView viewZqmc = view.findViewById(R.id.tv_buy_dialog_zqmc);
        TextView viewZqdm = view.findViewById(R.id.tv_buy_dialog_zqdm);
        TextView viewBjfs = view.findViewById(R.id.tv_buy_dialog_bjfs);
        TextView viewWtjg = view.findViewById(R.id.tv_buy_dialog_wtjg);
        TextView viewWtsl = view.findViewById(R.id.tv_buy_dialog_wtsl);
        viewZqmc.setText(zqmc);
        viewZqdm.setText(zqdm);
        viewBjfs.setText(bjfs);
        viewWtjg.setText(wtjg);
        viewWtsl.setText(wtsl);
        return new MaterialDialog.Builder(context)
                .title(title)
                .customView(view,false)
                .negativeText(cancel)
                .positiveText(confirm)
                .onPositive(singleButtonCallback)
                .build();
    }



    public static Dialog buildDialog(Context context, String title, String content, MaterialDialog.SingleButtonCallback confirmCallback, MaterialDialog.SingleButtonCallback cancelCallback) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(confirmCallback)
                .onNegative(cancelCallback)
                .build();
    }


    public static Dialog buildBuyInfoDialog(Context context, String title, String content,MaterialDialog.SingleButtonCallback confirmCallback) {
        return new MaterialDialog.Builder(context)
                .cancelable(false)
                .title(title)
                .content(content)
                .positiveText("确认")
                .onPositive(confirmCallback)
                .build();
    }

}
