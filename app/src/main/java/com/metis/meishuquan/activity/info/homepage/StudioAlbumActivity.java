package com.metis.meishuquan.activity.info.homepage;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.model.BLL.WorkInfo;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StudioAlbumActivity extends BaseActivity {

    private StickyGridHeadersGridView mGridView = null;

    private AlbumAdapter mAdapter = null;
    private List<WorkInfo> mDatalist = new ArrayList<WorkInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio_album);

        mGridView = (StickyGridHeadersGridView)findViewById(R.id.album_grid);
        for (int i = 0; i < 300; i++) {
            WorkInfo info = new WorkInfo();
            info.setCreateTime(('A' + i % 26) + "T");
            info.setPhotoThumbnail(('A' + i % 26) + "T " + i);
            mDatalist.add(info);
        }
        mAdapter = new AlbumAdapter(mDatalist);
        mGridView.setAdapter(mAdapter);
        mGridView.setAreHeadersSticky(false);

    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.studio_album);
    }

    private class AlbumAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

        private List<WorkInfo> mWorkList = null;
        private Map<String, List<WorkInfo>> mGroupMap = new HashMap<String, List<WorkInfo>>();


        public AlbumAdapter (List<WorkInfo> WorkList) {
            mWorkList = WorkList;
            parseWorkList(mWorkList);
        }

        public void append (List<WorkInfo> list) {
            mWorkList.addAll(list);
            parseWorkList(list);
        }

        @Override
        public long getHeaderId(int position) {
            return getTimeTitle(getItem(position).getCreateTime()).hashCode();
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(StudioAlbumActivity.this);
            tv.setText("header:" + getTimeTitle(getItem(position).getCreateTime()));
            return tv;
        }

        @Override
        public int getCount() {
            return mWorkList.size();
        }

        @Override
        public WorkInfo getItem(int position) {
            return mWorkList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(StudioAlbumActivity.this);
            tv.setText(mWorkList.get(position).getPhotoThumbnail());
            return tv;
        }

        public void parseWorkList (List<WorkInfo> infoList) {
            final int length = infoList.size();
            for (int i = 0; i < length; i++) {
                WorkInfo info = infoList.get(i);
                String key = getTimeTitle(info.getCreateTime());
                List<WorkInfo> list = null;
                if (mGroupMap.containsKey(key)) {
                    list = mGroupMap.get(key);
                } else {
                    list = new ArrayList<WorkInfo>();
                    mGroupMap.put(key, list);
                }
                list.add(info);
            }

            mWorkList.clear();
            Set<String> keySet = mGroupMap.keySet();
            for (String key : keySet) {
                List<WorkInfo> list = mGroupMap.get(key);
                mWorkList.addAll(list);
            }

        }

        private String getTimeTitle (String time) {
            final int index = time.indexOf("T");
            if (index > 0) {
                return time.substring(0, index);
            }
            return "#";
        }
    }

}
