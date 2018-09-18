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
import com.xzcf.data.data.socket.SocketSub;
import com.xzcf.service.WebSocketService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * -
 * Author：chasen
 * Date： 2018/9/17 18:18
 */
public abstract class BaseSocketActivity<T extends BaseSocketRes> extends BaseActivity {

    private Gson gson;
    private BroadcastReceiver socketReceiver;

    private List<SocketSub> socketSubs = new ArrayList<>();

    private boolean isRegister;

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

                onSocketReceive(gson.fromJson(date, type), date);
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (socketReceiver != null && isRegister) {
            unregisterReceiver(socketReceiver);
        }
    }

    protected void notiftSubChange() {

        if(socketSubs.size() == 0){
            return;
        }

        if(isRegister){
            unregisterReceiver(socketReceiver);
        }

        IntentFilter filter = new IntentFilter();
        for (SocketSub sub : socketSubs) {
            filter.addAction(WebSocketService.SOCKET_RECEIVE_ACTION + sub.getQid());
        }
        registerReceiver(socketReceiver, filter);

        isRegister = true;
    }

    protected void addSocketSub(SocketSub socketSub) {
        socketSubs.add(socketSub);
    }

    protected void cancelAllSub() {
        for (SocketSub sub : socketSubs) {
            Intent intent = new Intent(WebSocketService.SOCKET_RECEIVE_ACTION);
            intent.putExtra(WebSocketService.COMMOND, WebSocketService.CANCEL);
            intent.putExtra(WebSocketService.QID, sub.getQid());
            sendBroadcast(intent);
        }
        socketSubs.clear();

        unregisterReceiver(socketReceiver);

        isRegister = false;
    }

    protected void cancelSub(SocketSub sub) {
        Intent intent = new Intent(WebSocketService.SOCKET_RECEIVE_ACTION);
        intent.putExtra(WebSocketService.COMMOND, WebSocketService.CANCEL);
        intent.putExtra(WebSocketService.QID, sub.getQid());
        sendBroadcast(intent);
        for (int i = 0; i < socketSubs.size(); i++) {
            if (socketSubs.get(i).getQid().equals(sub.getQid())) {
                socketSubs.remove(i);
                break;
            }
        }
    }

    protected abstract void onSocketReceive(T result, String rawJson);
}
