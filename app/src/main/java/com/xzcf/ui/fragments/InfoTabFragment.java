package com.xzcf.ui.fragments;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.ui.WebActivity;
import com.xzcf.ui.adapters.InfoRssAdapter;
import com.xzcf.utils.DialogUtils;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InfoTabFragment extends BaseFragment {

    private static final String ARG_INFO_TYPE = "info_type";
    @BindView(R.id.rvRsses)
    RecyclerView rvRsses;
    Unbinder unbinder;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    private Dialog mProgressDialog;

    private InfoRssAdapter mAdapter;
    private InfoType mInfoType;
    private Disposable newsDisposable;
    boolean isCreate = true;

    public static InfoTabFragment newInstance(InfoType infoType) {
        InfoTabFragment fragment = new InfoTabFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_INFO_TYPE, infoType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInfoType = (InfoType) getArguments().getSerializable(ARG_INFO_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_tab, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        isCreate = true;
        if (newsDisposable != null) {
            newsDisposable.dispose();
            newsDisposable = null;
        }
    }

    private void initView() {
        try{

            loadData();
            smartRefreshLayout.setEnableLoadMore(false);
            smartRefreshLayout.setOnRefreshListener(refreshLayout -> loadData());

            mAdapter = new InfoRssAdapter();
            mAdapter.setOnItemClickListener(rss -> startActivity(WebActivity.startAction(getContext(), rss.getTitle(), rss.getLink())));
            rvRsses.setAdapter(mAdapter);
            rvRsses.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.info_rss_divider);
            itemDecoration.setDrawable(drawable);
            rvRsses.addItemDecoration(itemDecoration);

            //smartRefreshLayout.autoRefresh();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void loadData() {
        if (newsDisposable != null) {
            newsDisposable.dispose();
            newsDisposable = null;
        }
        try{
            newsDisposable = DataManager.getInstance().getNews()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(newsResponse -> {
                        if (smartRefreshLayout != null && smartRefreshLayout.isRefreshing()) {
                            smartRefreshLayout.finishRefresh();
                        }
                        if (mProgressDialog != null){
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }
                        switch (mInfoType) {
                            case ZHIBO:
                                mAdapter.setRsses(newsResponse.getRssZhiBo());
                                break;
                            case YIDONG:
                                mAdapter.setRsses(newsResponse.getRssYiDong());
                                break;
                            case YAOWEN:
                                mAdapter.setRsses(newsResponse.getRssYaoWen());
                                break;
                            case TUIJIAN:
                                mAdapter.setRsses(newsResponse.getRssTuiJian());
                                break;
                            case SHIKUANG:
                                mAdapter.setRsses(newsResponse.getRssShiKuang());
                                break;
                        }
                    }, throwable -> {
                        if (mProgressDialog != null){
                            mProgressDialog.dismiss();
                            mProgressDialog = null;
                        }
//                        if (smartRefreshLayout != null && smartRefreshLayout.isRefreshing()) {
//                            smartRefreshLayout.finishRefresh();
//                        }
                        //Logger.e(throwable, throwable.getMessage());
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public enum InfoType implements Serializable {
        ZHIBO,
        YIDONG,
        YAOWEN,
        TUIJIAN,
        SHIKUANG;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (isCreate) {
            mProgressDialog = DialogUtils.buildProgressDialogsDialog(getContext(), "加载中", "请稍候 " );
            mProgressDialog.show();
            initView();
            isCreate = false;
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
