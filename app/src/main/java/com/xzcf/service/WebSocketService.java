package com.xzcf.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.blankj.utilcode.util.EncryptUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.xzcf.data.data.socket.BaseSocketRes;
import com.xzcf.utils.ToastUtil;

/**
 * Description:
 * -
 * Author：chasen
 * Date： 2018/9/17 17:48
 */
public class WebSocketService extends Service {


    private WebSocket socket;
    private Gson gson;

    private String token;

    public static final int SUB = 1;
    public static final int CANCEL = 2;

    public static final String COMMOND = "COMMOND";
    public static final String PATH = "PATH";
    public static final String QID = "QID";

    public static final String SOCKET_DATA = "SOCKET_DATA";

    public static final String SOCKET_RECEIVE_ACTION = "SOCKET_RECEIVE_ACTION";

    /**
     * 大智慧配置
     */
    private static final String APP_ID = "ac813f07955111e8886b0242ac11003c";
    private static final String APP_KEY = "C8lw2pZ07kjy";
    private static final String SHORT_ID = "000000c9";

    private static final String URL = "ws://gw.yundzh.com/ws?token=";
    private static final String PORT = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(gson == null){
            gson = new GsonBuilder().create();
        }

        int commond = intent.getIntExtra(COMMOND,SUB);

        String qid = intent.getStringExtra(QID);
        String path = intent.getStringExtra(PATH);

        if(socket == null){
            token = buildeToken();
            String uri = URL + token;
            AsyncHttpClient.getDefaultInstance().websocket(uri, PORT, new AsyncHttpClient.WebSocketConnectCallback() {
                @Override
                public void onCompleted(Exception ex, WebSocket webSocket) {
                    if (ex != null) {
                        ex.printStackTrace();
                        return;
                    }

                    socket = webSocket;

                    socket.setStringCallback(new WebSocket.StringCallback() {
                        public void onStringAvailable(String s) {
                            sendSocketDataBroadcast(s);
                        }
                    });

                    socket.setDataCallback(new DataCallback() {
                        @Override
                        public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                            String content = bb.peekString();
                            sendSocketDataBroadcast(content);
                        }
                    });
                }
            });
        }

        if(commond == SUB){
            if(TextUtils.isEmpty(path)){
                ToastUtil.showToast("path is empty");
            }else{
                socket.send(path);
            }
        }
        if(commond == CANCEL){
            if(TextUtils.isEmpty(qid)){
                ToastUtil.showToast("qid is empty");
            }else{
                socket.send("/cancel?qid=" + qid);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void sendSocketDataBroadcast(String s) {
        if(TextUtils.isEmpty(s)){
            return;
        }

        BaseSocketRes socketRes = gson.fromJson(s,BaseSocketRes.class);
        String qid = socketRes.getQid();

        Intent send = new Intent(SOCKET_RECEIVE_ACTION + qid);
        send.putExtra(QID,qid);
        send.putExtra(SOCKET_DATA,s);
        sendBroadcast(send);
    }

    private String buildeToken(){
        String expiredTime = String.valueOf(System.currentTimeMillis() / 1000 + 1 * 60 * 60 * 1000);
        String rawMask = APP_ID + "_" + expiredTime + "_" + APP_KEY;
        String hexMask = EncryptUtils.encryptHmacSHA1ToString(rawMask, APP_KEY).toLowerCase();
        return SHORT_ID + ":" + expiredTime + ":" + hexMask;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
