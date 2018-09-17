package com.xzcf;

import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.StockDataManager;
import com.xzcf.data.data.response.LoginInfo;
import com.xzcf.data.data.response.NewsResponse;
import com.xzcf.data.data.response.StockIndexResponse;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class DataManagerTest {

    @Test
    public void testLogin() {
        Observable<LoginInfo> flowable = DataManager.getInstance().login(
                "Yoyun",
                "88888888",
                "123123123123123123123123"
        );

        flowable.subscribe(response -> System.out.println(response.toString()));
    }

    @Test
    public void testGetNews() {
        Observable<NewsResponse> flowable = DataManager.getInstance().getNews();
        flowable.subscribe(newsResponse -> System.out.println(newsResponse.toString()));
    }

    @Test
    public void testGetStockIndex() {
        Flowable<StockIndexResponse> flowable = StockDataManager.getInstance().getStockIndex();
        flowable.subscribe(stockIndexResponse -> System.out.println(stockIndexResponse.getStockIndexs()));

    }


    @Test
    public void testDate(){
        try{

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat formatDateFormat = new SimpleDateFormat("yyyy-MM");
            Date parse = simpleDateFormat.parse("20180730");
            String format = formatDateFormat.format(parse);
            System.out.println(format+","+parse.getTime()+","+System.currentTimeMillis());
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

}
