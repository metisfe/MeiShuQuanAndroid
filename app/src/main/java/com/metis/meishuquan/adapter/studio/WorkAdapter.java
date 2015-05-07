package com.metis.meishuquan.adapter.studio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.WorkInfo;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/7.
 */
public class WorkAdapter extends BaseAdapter {

    private List<WorkInfoGroup> mDataList = new ArrayList<WorkInfoGroup>();

    private Context mContext = null;

    public WorkAdapter (Context context, List<WorkInfo> works) {
        mContext = context;
        mDataList = works;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public WorkInfo getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_work_item, null);
            holder = new ViewHolder();
            holder.mIv1 = (ImageView)view.findViewById(R.id.work_1);
            holder.mIv2 = (ImageView)view.findViewById(R.id.work_2);
            holder.mIv3 = (ImageView)view.findViewById(R.id.work_3);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        WorkInfo workInfo = getItem(i);
        ImageLoaderUtils.getImageLoader(mContext).displayImage(
                workInfo.getPhotoThumbnail(), holder.mIv1,
                ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher)
        );
        return view;
    }

    private void addAllWorkInfo (List<WorkInfo> workInfos) {
        if (mDataList.isEmpty()) {
            int index = 0;
            final int length = workInfos.size();
            for (; index < length; index += 3) {
                WorkInfoGroup group = new WorkInfoGroup();
                group.mInfo1 = workInfos.get(index);
                group.mInfo2 = workInfos.get(index + 1);
                group.mInfo3 = workInfos.get(index + 2);
                mDataList.add(group);
            }
            index -= 3;
            if (index < length) {
                WorkInfoGroup group = new WorkInfoGroup();
                if (index + 1 < length) {
                    group.mInfo1 = workInfos.get(index + 1);
                }
                if (index + 2 < length) {
                    group.mInfo2 = workInfos.get(index + 2);
                }
            }
        } else {

        }
    }

    private class ViewHolder {
        public ImageView mIv1, mIv2, mIv3;
    }

    private class WorkInfoGroup {
        public WorkInfo mInfo1, mInfo2, mInfo3;
    }
}
