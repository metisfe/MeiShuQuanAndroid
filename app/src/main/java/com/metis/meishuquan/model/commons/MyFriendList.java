package com.metis.meishuquan.model.commons;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WJ on 2015/5/18.
 */
public class MyFriendList implements Serializable {

    private List<User> myFirends = null;

    public List<User> getMyFirends() {
        return myFirends;
    }

    public void setMyFirends(List<User> myFirends) {
        this.myFirends = myFirends;
    }
}
