package com.xzcf;

import com.xzcf.data.data.XZCFService;
import com.xzcf.data.data.response.LoginInfo;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void service() {
        XZCFService xzcfService = XZCFService.Creator.newXZCFService();
        Map<String, Object> params = new HashMap();
        params.put("TRANSID", "MEM_LOGIN_DO");
        params.put("userName", "aa");
        params.put("userPwd", "147258");
        params.put("token", "kasjdflasjflasdjflaskdfjaslfy");
        Observable<LoginInfo> memLoginDo = xzcfService.login(params);
        memLoginDo.subscribe(new Consumer<LoginInfo>() {
            @Override
            public void accept(LoginInfo response) throws Exception {
                response.toString();
            }
        });

    }
}