package com.xzcf.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xzcf.data.data.socket.BaseSocketRes;
import com.xzcf.service.WebSocketService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Description:
 * -
 * Author：chasen
 * Date： 2018/9/17 18:18
 */
public abstract class BaseSocketActivity<T extends BaseSocketRes> extends BaseActivity {

    private Gson gson;
    private BroadcastReceiver socketReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        gson = new GsonBuilder().create();
        socketReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String date = intent.getStringExtra(WebSocketService.SOCKET_DATA);
                ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
                Type type = parameterizedType.getActualTypeArguments()[0];

                onSocketReceive(gson.fromJson(date, type));
            }
        };
        IntentFilter filter = new IntentFilter(WebSocketService.SOCKET_RECEIVE_ACTION + getQid());
        registerReceiver(socketReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(socketReceiver != null){
            unregisterReceiver(socketReceiver);
        }
    }

    protected void subSocket(String path){
        Intent intent = new Intent(this,WebSocketService.class);
        intent.putExtra(WebSocketService.COMMOND,WebSocketService.SUB);
        intent.putExtra(WebSocketService.PATH,path);
        intent.putExtra(WebSocketService.QID,getQid());
    }

    protected abstract void onSocketReceive(T result);

    protected abstract String getQid();
}
