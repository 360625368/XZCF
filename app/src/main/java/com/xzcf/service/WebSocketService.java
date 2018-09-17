package com.xzcf.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.xzcf.config.UrlConfig;
import com.xzcf.utils.ToastUtil;

/**
 * Description:
 * -
 * Author：chasen
 * Date： 2018/9/17 17:48
 */
public class WebSocketService extends Service {


    private WebSocket socket;

    public static final int SUB = 1;
    public static final int CANCEL = 2;

    public static final String COMMOND = "COMMOND";
    public static final String PATH = "PATH";
    public static final String QID = "QID";

    public static final String SOCKET_DATA = "SOCKET_DATA";

    public static final String SOCKET_RECEIVE_ACTION = "SOCKET_RECEIVE_ACTION";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int commond = intent.getIntExtra(COMMOND,SUB);

        String qid = intent.getStringExtra(QID);
        String path = intent.getStringExtra(PATH);

        if(socket == null){
            AsyncHttpClient.getDefaultInstance().websocket(UrlConfig.SOCKET_BASE_URL, UrlConfig.SOCKET_PORT, new AsyncHttpClient.WebSocketConnectCallback() {
                @Override
                public void onCompleted(Exception ex, WebSocket webSocket) {
                    if (ex != null) {
                        ex.printStackTrace();
                        return;
                    }

                    socket = webSocket;

                    socket.setStringCallback(new WebSocket.StringCallback() {
                        public void onStringAvailable(String s) {
                            Intent send = new Intent(SOCKET_RECEIVE_ACTION);
                            send.putExtra(SOCKET_DATA,s);
                        }
                    });
                }
            });
        }

        if(commond == SUB){
            if(TextUtils.isEmpty(path)){
                ToastUtil.showToast("path is empty");
            }
            socket.send(path);
        }
        if(commond == CANCEL){
            if(TextUtils.isEmpty(qid)){
                ToastUtil.showToast("qid is empty");
            }
            socket.send("/cancel?qid=" + qid);
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
