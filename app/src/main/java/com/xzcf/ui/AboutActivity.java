package com.xzcf.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xzcf.R;
import com.xzcf.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {


    @BindView(R.id.iBtnBack)
    ImageButton iBtnBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tv_about_version)
    TextView tvAboutVersion;
    @BindView(R.id.tv_date)
    TextView tvDate;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about);
        ButterKnife.bind(this);
        iBtnBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("关于我们");

        tvAboutVersion.setText(AppUtils.getVersion(getContext()));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String format = simpleDateFormat.format(new Date());
        tvDate.setText(format);
    }


    @OnClick(R.id.iBtnBack)
    public void onBackOnClick() {
        finish();
    }

}
