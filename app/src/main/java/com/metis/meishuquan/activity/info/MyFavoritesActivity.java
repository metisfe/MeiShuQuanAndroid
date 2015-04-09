package com.metis.meishuquan.activity.info;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.Item;

import java.util.List;

public class MyFavoritesActivity extends DataListActivity {

    @Override
    public void loadData(int index) {
        UserInfoOperator.getInstance().getFavoriteList("100001", new UserInfoOperator.OnGetListener<List<Item>>() {
            @Override
            public void onGet(boolean succeed, List<Item> items) {
                if (succeed) {

                } else {

                }
            }
        });
    }

}
