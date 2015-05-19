package com.metis.meishuquan.fragment.commons;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.metis.meishuquan.R;

/**
 * Created by WJ on 2015/4/20.
 */
public class ListDialogFragment extends DialogFragment {

    private static ListDialogFragment sFragment = new ListDialogFragment();

    private ListView mListView = null;

    private BaseAdapter mAdapter = null;

    private AdapterView.OnItemClickListener mItemClickListener = null;

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
        setOnItemClickListener(mItemClickListener);
    }

    public void setAdapter (BaseAdapter adapter) {
        mAdapter = adapter;
        if (mListView != null) {
            mListView.setAdapter(adapter);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListView.setAdapter(null);
        mListView.setOnItemClickListener(null);
    }

    public void setOnItemClickListener (AdapterView.OnItemClickListener listener) {
        mItemClickListener = listener;
        if (mListView != null) {
            mListView.setOnItemClickListener(mItemClickListener);
        }
    }

    public static class SimpleAdapter extends BaseAdapter {

        private String[] mArray = null;
        private Context mContext = null;

        public SimpleAdapter (Context context, String[] array) {
            mArray = array;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mArray.length;
        }

        @Override
        public String getItem(int i) {
            return mArray[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_list_dialog_item, null);
            }

            TextView tv = (TextView)view.findViewById(R.id.list_dialog_item);
            tv.setText(getItem(i));
            return view;
        }

    }
}
