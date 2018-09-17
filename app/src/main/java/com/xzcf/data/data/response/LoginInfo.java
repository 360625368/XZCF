package com.xzcf.data.data.response;

import com.google.gson.reflect.TypeToken;
import com.xzcf.utils.GsonUtils;

public class LoginInfo extends MyResponse {
    private static final long serialVersionUID = -1354707887232250765L;
    private String memberId;
    private String organId;
    private String balanceTake;
    private String balanceFreeze;
    private String assetsReference;
    private String assetsAll;
    private String accountId;
    private String accountNo;
    private String commissionScale;
    private String commissionLowest;
    private String username;
    private String realname;
    private String headimageUrl;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String state;
    private String lastLoginIp;
    private String lastLoginTime;

    public String getMemberId() {
        return memberId;
    }

    public String getOrganId() {
        return organId;
    }

    public String getBalanceTake() {
        return balanceTake;
    }

    public String getBalanceFreeze() {
        return balanceFreeze;
    }

    public String getAssetsReference() {
        return assetsReference;
    }

    public String getAssetsAll() {
        return assetsAll;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getCommissionScale() {
        return commissionScale;
    }

    public String getCommissionLowest() {
        return commissionLowest;
    }

    public String getUsername() {
        return username;
    }

    public String getRealname() {
        return realname;
    }

    public String getHeadimageUrl() {
        return headimageUrl;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public static LoginInfo fromJson(String json) {
        return GsonUtils.getGson().fromJson(json, new TypeToken<LoginInfo>() {}.getType());
    }

    @Override
    public String toString() {
        return GsonUtils.getGson().toJson(this, new TypeToken<LoginInfo>() {}.getType());
    }
}
