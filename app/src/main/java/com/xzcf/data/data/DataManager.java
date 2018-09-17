package com.xzcf.data.data;

import com.xzcf.Constants;
import com.xzcf.data.data.req.StockBuyDoReq;
import com.xzcf.data.data.req.StockSelfReq;
import com.xzcf.data.data.response.AppVersionResponse;
import com.xzcf.data.data.response.BargainRecordResponse;
import com.xzcf.data.data.response.BaseRes;
import com.xzcf.data.data.response.EntrustRecordResponse;
import com.xzcf.data.data.response.GetAdvertiseResponse;
import com.xzcf.data.data.response.HavingLogResponse;
import com.xzcf.data.data.response.LoginInfo;
import com.xzcf.data.data.response.MemMoneyResponse;
import com.xzcf.data.data.response.MyResponse;
import com.xzcf.data.data.response.NewsResponse;
import com.xzcf.data.data.response.StockRankItemResponse;
import com.xzcf.data.data.response.StockSelfResponse;
import com.xzcf.utils.DateTimeUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class DataManager {
    private static DataManager instance;

    public XZCFService xzcfService;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private DataManager() {
        xzcfService = XZCFService.Creator.newXZCFService();
    }


    public Observable<LoginInfo> login(String userName, String userPwd, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_MEM_LOGIN_DO);
        params.put("userName", userName);
        params.put("userPwd", userPwd);
        params.put("token", token);
        return xzcfService.login(params);
    }

    public Observable<MyResponse> modifyPwd(String memberId, String userPwd, String newPwd, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_MEM_UPDATE_DO);
        params.put("memberId", memberId);
        params.put("userPwd", userPwd);
        params.put("newPwd", newPwd);
        params.put("token", token);
        return xzcfService.modifyPwd(params);
    }

    public Observable<MyResponse> logout(String memberId, String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_MEM_EXIT_DO);
        params.put("memberId", memberId);
        params.put("token", token);
        return xzcfService.logout(params);
    }

    public Observable<MemMoneyResponse> getMoney(String memberId) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_MEM_MONEY);
        params.put("memberId", memberId);
//        params.put("token", token);
        return xzcfService.getMoney(params);
    }

    public Observable<NewsResponse> getNews() {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_APP_GET_NEWS);
        return xzcfService.getNews(params);
    }

    public Observable<AppVersionResponse> getAppVersion(String version) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_APP_VERSION);
        params.put("curVersion", version);
        return xzcfService.getAppVersion(params);
    }

    public Flowable<StockRankItemResponse> getStockRankList(String stockType) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_APP_GET_RANKING);
        params.put("type", stockType);
        return xzcfService.getStockRankList(params);
    }

    public Observable<StockSelfResponse> getStockSelfList(StockSelfReq stockSelfReq) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_APP_SELF_SELECTION);
        params.put("operateType", stockSelfReq.getOperateType());
        params.put("memberId", stockSelfReq.getMemberId());
        if (stockSelfReq.isAddCommand()) {
            params.put("stockCode", stockSelfReq.getStockCode());
            params.put("stockName", stockSelfReq.getStockName());
        } else if (stockSelfReq.isDelCommand()) {
            params.put("selfSelectionId", stockSelfReq.getSelfSelectionId());
        }
        return xzcfService.getStockSelfList(params);
    }

    public Observable<EntrustRecordResponse> getEntrustRecord(String memberId, String pageNo, Date createdStart, Date createdEnd,String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_TRADE_ENTRUST_LOG);
        params.put("memberId", memberId);
        params.put("pageNo", pageNo);
        params.put("type", type);
        if (createdStart != null) {
            params.put("createdStart", DateTimeUtils.toDateString(createdStart));
        }
        if (createdEnd != null) {
            params.put("createdEnd", DateTimeUtils.toDateString(createdEnd));
        }
        return xzcfService.getEntrustRecord(params);
    }

    public Observable<BargainRecordResponse> getBargainRecord(String memberId, String pageNo, Date createdStart, Date createdEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_TRADE_BARGAIN_LOG);
        params.put("memberId", memberId);
        params.put("pageNo", pageNo);
        if (createdStart != null) {
            params.put("createdStart", DateTimeUtils.toDateString(createdStart));
        }
        if (createdEnd != null) {
            params.put("createdEnd", DateTimeUtils.toDateString(createdEnd));
        }
        return xzcfService.getBargainRecord(params);
    }

    public Observable<HavingLogResponse> getHavingLog(String memberId, String pageNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_TRADE_HAVING_LOG);
        params.put("memberId", memberId);
        params.put("pageNo", pageNo);
        return xzcfService.getHavingLog(params);
    }

    public Observable<BaseRes> putBuyDo(StockBuyDoReq stockBuyDoReq) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_MEM_BUY_DO);
        params.put("memberId", stockBuyDoReq.getMemberId());
        params.put("stockCode", stockBuyDoReq.getStockCode());
        params.put("stockName", stockBuyDoReq.getStockName());
        params.put("allPrice", stockBuyDoReq.getAllPrice());
        params.put("allCount", stockBuyDoReq.getAllCount());
        params.put("type", stockBuyDoReq.getType());
        return xzcfService.putBuyDo(params);
    }

    public Observable<BaseRes> cancelEntrust(String memberId, String entrustIds) {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_MEM_CANCEL_DO);
        params.put("memberId", memberId);
        params.put("entrustIds", entrustIds);
        return xzcfService.cancelEntrust(params);
    }

    public Observable<GetAdvertiseResponse> getAdvertise() {
        Map<String, Object> params = new HashMap<>();
        params.put("TRANSID", Constants.TRANSID_APP_HOME_ADVERTISE);
        params.put("type", "1");
        return xzcfService.getAdvertise(params);
    }

}
