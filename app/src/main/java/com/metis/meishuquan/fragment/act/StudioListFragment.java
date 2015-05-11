package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.TopListItem;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/6.
 */
public class StudioListFragment extends ActiveListFragment {

    private int mIndex = 1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        reloadDataList();
    }

    private void reloadDataList () {
        mIndex = 1;
        loadDataList(mIndex, new UserInfoOperator.OnGetListener<List<TopListItem>>() {
            @Override
            public void onGet(boolean succeed, List<TopListItem> topListItems) {
                if (succeed) {
                    final int length = topListItems.size();
                    List<TopListDelegate> delegates = new ArrayList<TopListDelegate>();
                    for (int i = 0; i < length; i++) {
                        TopListDelegate delegate = new TopListDelegate(topListItems.get(i));
                        delegates.add(delegate);
                    }
                    onReloadFinished(delegates);
                }
            }
        });
    }

    private void loadDataListMore () {
        loadDataList(mIndex + 1, new UserInfoOperator.OnGetListener<List<TopListItem>>() {
            @Override
            public void onGet(boolean succeed, List<TopListItem> topListItems) {
                if (succeed) {
                    mIndex++;
                    final int length = topListItems.size();
                    List<TopListDelegate> delegates = new ArrayList<TopListDelegate>();
                    for (int i = 0; i < length; i++) {
                        TopListDelegate delegate = new TopListDelegate(topListItems.get(i));
                        delegates.add(delegate);
                    }
                }
            }
        });
    }

    private void loadDataList (int index, UserInfoOperator.OnGetListener<List<TopListItem>> listener) {
        int filter1 = (int)getFilterSpinner1().getSelectedItemId();
        int filter2 = (int)getFilterSpinner2().getSelectedItemId();
        int filter3 = (int)getFilterSpinner3().getSelectedItemId();

        ActiveOperator.getInstance().getStudioList(11, filter2, filter3, index, listener);
    }


    @Override
    public void needReloadData(int selectedIndex1, int selectedIndex2, int selectedIndex3) {
        reloadDataList();
    }
}
