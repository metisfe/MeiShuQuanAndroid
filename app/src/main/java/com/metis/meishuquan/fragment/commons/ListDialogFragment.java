package com.metis.meishuquan.fragment.commons;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.metis.meishuquan.R;

/**
 * Created by WJ on 2015/4/20.
 */
public class ListDialogFragment extends DialogFragment {

    private static ListDialogFragment sFragment = new ListDialogFragment();

    private ListView mListView = null;

    private BaseAdapter mAdapter = null;

    public static ListDialogFragment getInstance () {
        sFragment.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
        return sFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_dialog, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView)view.findViewById(R.id.listview);
        setAdapter(mAdapter);
    }

    public void setAdapter (BaseAdapter adapter) {
        mAdapter = adapter;
        if (mListView != null) {
            mListView.setAdapter(adapter);
        }
    }
}
