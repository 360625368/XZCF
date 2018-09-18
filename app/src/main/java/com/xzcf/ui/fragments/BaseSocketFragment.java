package com.xzcf.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xzcf.data.data.socket.BaseSocketRes;
import com.xzcf.data.data.socket.SocketSub;
import com.xzcf.service.WebSocketService;
import com.xzcf.ui.fragments.BaseFragment;

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
public abstract class BaseSocketFragment<T extends BaseSocketRes> extends BaseFragment {

    private Gson gson;
    private BroadcastReceiver socketReceiver;

    private List<SocketSub> socketSubs = new ArrayList<>();

    private boolean isRegister;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
    public void onStop() {
        super.onStop();
        if (socketReceiver != null && isRegister && getContext() != null) {
            getContext().unregisterReceiver(socketReceiver);
            isRegister = false;
        }
    }

    protected void notiftSubChange() {

        if(socketSubs.size() == 0){
            return;
        }

        if(isRegister && getContext() != null){
            getContext().unregisterReceiver(socketReceiver);
            isRegister = false;
        }

        IntentFilter filter = new IntentFilter();
        for (SocketSub sub : socketSubs) {
            filter.addAction(WebSocketService.SOCKET_RECEIVE_ACTION + sub.getQid());
        }
        getContext().registerReceiver(socketReceiver, filter);

        for (SocketSub sub : socketSubs) {
            Intent intent = new Intent(getContext(),WebSocketService.class);
            intent.putExtra(WebSocketService.COMMOND,WebSocketService.SUB);
            intent.putExtra(WebSocketService.PATH,sub.getPath());
            intent.putExtra(WebSocketService.QID,sub.getQid());
            getContext().startService(intent);
        }

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
            getContext().sendBroadcast(intent);
        }
        socketSubs.clear();

        getContext().unregisterReceiver(socketReceiver);

        isRegister = false;
    }

    protected void cancelSub(SocketSub sub) {
        Intent intent = new Intent(WebSocketService.SOCKET_RECEIVE_ACTION);
        intent.putExtra(WebSocketService.COMMOND, WebSocketService.CANCEL);
        intent.putExtra(WebSocketService.QID, sub.getQid());
        getContext().sendBroadcast(intent);
        for (int i = 0; i < socketSubs.size(); i++) {
            if (socketSubs.get(i).getQid().equals(sub.getQid())) {
                socketSubs.remove(i);
                break;
            }
        }
    }

    protected abstract void onSocketReceive(T result, String rawJson);
}
