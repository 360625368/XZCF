package com.xzcf.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xzcf.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebActivity extends AppCompatActivity {

    private static final String ARG_TITLE = "title";
    private static final String ARG_URL = "url";

    @BindView(R.id.iBtnBack)
    ImageButton iBtnBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.webView)
    WebView webView;
    private String mTitle;
    private String mUrl;

    public static Intent startAction(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(ARG_TITLE, title);
        intent.putExtra(ARG_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        iBtnBack.setVisibility(View.VISIBLE);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        if (getIntent().hasExtra(ARG_URL)) {
            mTitle = getIntent().getStringExtra(ARG_TITLE);
            mUrl = getIntent().getStringExtra(ARG_URL);
            tvTitle.setText(mTitle);
            webView.loadUrl(mUrl);
        }

        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient());


    }

    @OnClick(R.id.iBtnBack)
    public void onIBtnBackClicked() {
        finish();
    }
}
