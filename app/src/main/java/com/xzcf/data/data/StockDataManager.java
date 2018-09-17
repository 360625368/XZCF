package com.xzcf.data.data;

import com.xzcf.Constants;
import com.xzcf.data.data.req.StockDayChartReq;
import com.xzcf.data.data.req.StockMarketDetailReq;
import com.xzcf.data.data.req.StockMinuteChartReq;
import com.xzcf.data.data.req.StockQuotesReq;
import com.xzcf.data.data.response.KDayChartRes;
import com.xzcf.data.data.response.KMinuteChartRes;
import com.xzcf.data.data.response.StockIndexResponse;
import com.xzcf.data.data.response.StockMarketDetailRes;
import com.xzcf.data.data.response.StockListResponse;
import com.xzcf.data.data.response.StockQuotesResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

public class StockDataManager {
    private static StockDataManager instance;

    public XZCFStockService  mXZCFStockService;

    public static StockDataManager getInstance() {
        if (instance == null) {
            instance = new StockDataManager();
        }
        return instance;
    }

    private StockDataManager(){
        mXZCFStockService = XZCFStockService.Creator.newXZCFStockService();
    }


    public Flowable<StockIndexResponse> getStockIndex(){
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID",Constants.TRANSID_GET_STOCK_INDEX);
        return mXZCFStockService.getStockIndex(params);
    }

    public Flowable<StockQuotesResponse> getStockQuotes(List<StockQuotesReq> stockQuotesReqs){
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID",Constants.TRANSID_GET_SECURITY_QUOTES);
        params.put("securitys",stockQuotesReqs);
        return mXZCFStockService.getStockQuotes(params);
    }

    public Flowable<StockMarketDetailRes> getStockTransaction(StockMarketDetailReq stockQuotesReqs){
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID",Constants.TRANSID_GET_TRANSACTION_DATA);
        params.put("stockCode",stockQuotesReqs.getStockCode());
        params.put("start",stockQuotesReqs.getStart());
        params.put("market",stockQuotesReqs.getMarket());
        params.put("count",stockQuotesReqs.getCount());
        return mXZCFStockService.getStockTransaction(params);
    }

    public Flowable<KMinuteChartRes> getStockMinuteChart(StockMinuteChartReq stockMinuteChartReq) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_GET_MINUTE_TIME_DATA);
        params.put("stockCode", stockMinuteChartReq.getStockCode());
        params.put("market", stockMinuteChartReq.getMarket());
        return mXZCFStockService.getStockMinuteChart(params);
    }
    public Flowable<StockListResponse> getMemHomeList() {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID",Constants.TRANSID_GET_MEM_HOME_LIST);
        return mXZCFStockService.getMemHomeList(params);
    }

    public Flowable<KDayChartRes> getStockKDayChart(StockDayChartReq stockDayChartReq) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_GET_SECURITY_BARS);
        params.put("category", stockDayChartReq.getCategory());
        params.put("stockCode", stockDayChartReq.getStockCode());
        params.put("market", stockDayChartReq.getMarket());
        params.put("start", stockDayChartReq.getStart());
        params.put("count", stockDayChartReq.getCount());
        params.put("isFirst", stockDayChartReq.getIsFirst());
        return mXZCFStockService.getStockKDayList(params);
    }

}
