package com.metis.meishuquan.fragment.commons;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.metis.meishuquan.R;

import java.util.Collection;
import java.util.List;

/**
 * Created by WJ on 2015/5/12.
 */
public class MultiListViewFragment extends Fragment {

    private LinearLayout mContainer = null;

    private List<BaseAdapter> mAdapterCollection = null;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = null;
        if (enter) {
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.top_in);
        } else {
            animation = AnimationUtils.loadAnimation(getActivity(), R.anim.top_out);
        }
        return animation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multi_list_view, null, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContainer = (LinearLayout)view.findViewById(R.id.multi_list_view_container);
    }

    public void setAdapterList (List<BaseAdapter> adapters) {
        mContainer.removeAllViews();
        final int length = adapters.size();
        for (int i = 0; i < length; i++) {
            ListView lv = (ListView)LayoutInflater.from(getActivity()).inflate(R.layout.layout_single_list_view, null);
            BaseAdapter adapter = adapters.get(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            lv.setAdapter(adapter);
            lv.setLayoutParams(params);
            mContainer.addView(lv);
        }
    }

    public ListView getListView (int index) {
        return (ListView)mContainer.getChildAt(index);
    }

    public void setAdapterFor (int index, BaseAdapter adapter) {
        View child = mContainer.getChildAt(index);
        if (child instanceof ListView) {
            mAdapterCollection.set(index, adapter);
            ((ListView)child).setAdapter(adapter);
        }
    }

    public void notifyDateSetChanged (int index) {
        if (mAdapterCollection == null || mAdapterCollection.isEmpty()) {
            return;
        }
        if (index >= 0 && index < mAdapterCollection.size()) {
            mAdapterCollection.get(index).notifyDataSetChanged();
        }
    }

    public void reset () {
        if (mAdapterCollection != null) {
            mAdapterCollection.clear();
        }
        mAdapterCollection = null;
    }
}
