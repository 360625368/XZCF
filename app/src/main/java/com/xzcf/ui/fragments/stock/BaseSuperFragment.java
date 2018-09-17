package com.xzcf.ui.fragments.stock;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xzcf.ui.adapters.StockRankAdapter;
import com.xzcf.ui.view.StockIndexView;
import com.xzcf.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.SupportFragment;


public abstract class BaseSuperFragment extends SupportFragment {

    private static final String TAG = "BaseFragment";

    protected View rootView = null;
    Unbinder unbinder;
    StockIndexView mStockIndexView;
    Disposable disposable;
    Disposable mChartDisposable;
    Disposable mIntervalDisposable;


    StockRankAdapter mStockRankAdapter;

    // 是否是详情进入
    boolean isDetailEntry;
    public static final int INTERVAL_TIME  = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutRes(), container, false);
            unbinder = ButterKnife.bind(this, rootView);
        }
        initView(rootView);
        return rootView;
    }

    /**
     * 返回布局 resId
     *
     * @return layoutId
     */
    protected abstract int getLayoutRes();

    /**
     * 初始化view
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);

    public void showToast(String msg) {
        ToastUtil.showToast(msg);
    }

    public void showToast(int id) {
        ToastUtil.showToast(id);
    }

    protected void getKChartData(){}

    public void bindOnClickLister(View rootView, View.OnClickListener listener, @IdRes int... ids) {
        for (int id : ids) {
            View view = rootView.findViewById(id);
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
    }

    public void bindOnClickLister(View.OnClickListener listener, View... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mStockIndexView != null) {
            mStockIndexView.onDestroy();
        }

        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }

        if (mChartDisposable != null) {
            mChartDisposable.dispose();
            mChartDisposable = null;
        }



    }


}
