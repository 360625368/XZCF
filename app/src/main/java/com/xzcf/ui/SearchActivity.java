package com.xzcf.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xzcf.R;
import com.xzcf.data.data.StockDataManager;
import com.xzcf.data.data.response.StockListResponse;
import com.xzcf.ui.adapters.SearchResultAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends BaseActivity implements SearchResultAdapter.OnItemClickListener {

    @BindView(R.id.etInputBox)
    EditText etInputBox;
    @BindView(R.id.llSearchBar)
    LinearLayout llSearchBar;
    @BindView(R.id.tvDataLabel)
    TextView tvDataLabel;
    @BindView(R.id.rvData)
    RecyclerView rvData;
    @BindView(R.id.iBtnBack)
    ImageButton iBtnBack;
    private SearchResultAdapter resultAdapter;
    private List<StockListResponse.StockBean> stockBeanList;
    private Disposable disposable;
    private Disposable searchDisposable;

    public static Intent startIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    private static final String IS_STOCK_BUY_ENTRY = "IS_STOCK_BUY_ENTRY";
    private boolean isBuyEntry;

    public static Intent newIntent(Context context, boolean isBuyEntry) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(IS_STOCK_BUY_ENTRY, isBuyEntry);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        isBuyEntry = getIntent().getBooleanExtra(IS_STOCK_BUY_ENTRY, false);
        resultAdapter = new SearchResultAdapter();
        resultAdapter.setOnItemClickListener(this);
        rvData.setAdapter(resultAdapter);
        rvData.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.divider_shape_padding);
        dividerItemDecoration.setDrawable(drawable);
        rvData.addItemDecoration(dividerItemDecoration);
        etInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                if (TextUtils.isEmpty(s1)) {
                    stopSearch();
                    resultAdapter.clearStock();
                } else {
                    searchKey(s1);
                }
            }
        });
        etInputBox.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
                onBtnCompleteClick();
                return true;
            }
            return false;
        });
        loadStockList();
    }

    @OnClick(R.id.iBtnBack)
    public void onBtnBack(){
        finish();
    }

    @OnClick(R.id.btnComplete)
    public void onBtnCompleteClick() {
        if (resultAdapter == null) {
            return;
        }
        int itemCount = resultAdapter.getItemCount();
        if (itemCount==1) {
            onClick(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        stopSearch();
    }

    private void loadStockList() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        disposable = StockDataManager.getInstance().getMemHomeList().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stockListResponse -> {
                            stockBeanList = stockListResponse.getData();
                            String trim = etInputBox.getText().toString().trim();
                            if (!TextUtils.isEmpty(trim)) {
                                searchKey(trim);
                            }
                        },
                        throwable -> {});
    }

    private void searchKey(String key) {
        if (stockBeanList == null || stockBeanList.size() == 0) {
            return;
        }
        stopSearch();
        resultAdapter.clearStock();
        searchDisposable = Observable.fromIterable(stockBeanList)
                .filter(stockBean -> {
                    String name = stockBean.getStockName();
                    String code = stockBean.getStockCode();
                    String pinyin = stockBean.getStockPinyin();
                    return (!TextUtils.isEmpty(name) && name.contains(key)) ||
                            (!TextUtils.isEmpty(code) && code.contains(key)) ||
                            (!TextUtils.isEmpty(pinyin) && pinyin.indexOf(key) == 0) ||
                            (!TextUtils.isEmpty(pinyin) && pinyin.indexOf(key.toUpperCase()) == 0);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stockBean -> {
                    if (resultAdapter != null) {
                        resultAdapter.addStock(stockBean);
                    }
                }, throwable -> {});
    }

    private void stopSearch() {
        if (searchDisposable != null) {
            searchDisposable.dispose();
            searchDisposable = null;
        }
    }

    @Override
    public void onClick(int position) {
        StockListResponse.StockBean stockBean = resultAdapter.getItem(position);
        if (isBuyEntry) {
            Intent intent = new Intent();
            intent.putExtra(StockBuyActivity.STOCK_CODE, stockBean.getStockCode());
            intent.putExtra(StockBuyActivity.STOCK_NAME, stockBean.getStockName());
            setResult(0, intent);
        } else {
            startActivity(StockMarketActivity.newIntent(this, stockBean.getStockCode(), stockBean.getStockName()));
        }
        finish();
    }
}
