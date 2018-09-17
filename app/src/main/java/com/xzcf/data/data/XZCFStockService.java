package com.xzcf.data.data;

import com.xzcf.data.data.convert.GsonConverterFactoryEx;
import com.xzcf.data.data.response.KDayChartRes;
import com.xzcf.data.data.response.KMinuteChartRes;
import com.xzcf.data.data.response.StockIndexResponse;
import com.xzcf.data.data.response.StockListResponse;
import com.xzcf.data.data.response.StockMarketDetailRes;
import com.xzcf.data.data.response.StockQuotesResponse;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface XZCFStockService {

    String ENDPOINT = "http://120.79.136.150:8001/";

    @POST("hq/api/data")
    Flowable<StockIndexResponse> getStockIndex(@Body Map<String, Object> params);

    @POST("hq/api/data")
    Flowable<StockQuotesResponse> getStockQuotes(@Body Map<String, Object> params);

    @POST("hq/api/data")
    Flowable<StockMarketDetailRes> getStockTransaction(@Body Map<String, Object> params);

    @POST("hq/api/data")
    Flowable<KMinuteChartRes> getStockMinuteChart(@Body Map<String, Object> params);

    @POST("hq/api/data")
    Flowable<StockListResponse> getMemHomeList(@Body Map<String, Object> params);

    @POST("hq/api/data")
    Flowable<KDayChartRes> getStockKDayList(@Body Map<String, Object> params);

    class Creator {
        private static XZCFStockService INSTANCE;

        public static XZCFStockService newXZCFStockService() {
            if (INSTANCE != null) {
                return INSTANCE;
            }

            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logInterceptor)
                    .addInterceptor(chain -> {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Access-Control-Allow-Headers", "X-Requested-With")
                                .addHeader("Access-Control-Allow-Methods", "POST,GET,DELETE,PUT")
                                .addHeader("Access-Control-Allow-Origin", "3628800")
                                .addHeader("Access-Control-Max-Age", "*")
                                .addHeader("Content-Type", "text/plain;charset=ISO-8859-1")
                                .build();
                        return chain.proceed(request);

                    })
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(XZCFStockService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactoryEx.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            INSTANCE = retrofit.create(XZCFStockService.class);
            return INSTANCE;
        }
    }
}
