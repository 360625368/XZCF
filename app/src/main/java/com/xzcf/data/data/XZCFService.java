package com.xzcf.data.data;

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

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface XZCFService {
    String ENDPOINT = "http://120.79.136.150:8096/";

    @POST("yw/api/service/app")
    Observable<LoginInfo> login(@Body Map<String, Object> params);

    @POST("yw/api/service/app")
    Observable<MyResponse> modifyPwd(@Body Map<String, Object> params);

    @POST("yw/api/service/app")
    Observable<MyResponse> logout(@Body Map<String, Object> params);

    @POST("yw/api/service/app")
    Observable<MemMoneyResponse> getMoney(@Body Map<String, Object> params);

    @POST("yw/appapi/service/app")
    Observable<NewsResponse> getNews(@Body Map<String, Object> params);

    @POST("yw/appapi/service/app")
    Observable<AppVersionResponse> getAppVersion(@Body Map<String, Object> params);

    @POST("yw/appapi/service/app")
    Observable<GetAdvertiseResponse> getAdvertise(@Body Map<String, Object> params);

    @POST("yw/appapi/service/app")
    Flowable<StockRankItemResponse> getStockRankList(@Body Map<String, Object> params);

    @POST("yw/appapi/service/app")
    Observable<StockSelfResponse> getStockSelfList(@Body Map<String, Object> params);

    @POST("yw/api/service/app")
    Observable<EntrustRecordResponse> getEntrustRecord(@Body Map<String, Object> params);

    @POST("yw/api/service/app")
    Observable<BargainRecordResponse> getBargainRecord(@Body Map<String, Object> params);

    @POST("yw/api/service/app")
    Observable<HavingLogResponse> getHavingLog(@Body Map<String, Object> params);

    @POST("yw/api/service/app")
    Observable<BaseRes> putBuyDo(@Body Map<String, Object> params);

    @POST("yw/api/service/app")
    Observable<BaseRes> cancelEntrust(@Body Map<String, Object> params);


    class Creator {
        private static XZCFService INSTANCE;

        public static XZCFService newXZCFService() {
            if (INSTANCE != null) {
                return INSTANCE;
            }

            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(XZCFService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            INSTANCE = retrofit.create(XZCFService.class);
            return INSTANCE;
        }
    }
}
