package com.metis.meishuquan.model.login;

import java.util.List;

/**
 * 用户身份（角色）
 * <p/>
 * Created by wj on 15/4/8.
 */
public class UserRole {

    private List<IdentityType> data;

    public List<IdentityType> getData() {
        return data;
    }

    public void setData(List<IdentityType> data) {
        this.data = data;
    }
}
