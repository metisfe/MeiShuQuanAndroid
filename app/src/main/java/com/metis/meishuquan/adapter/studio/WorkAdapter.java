package com.metis.meishuquan.adapter.studio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.WorkInfo;
import com.metis.meishuquan.util.ActivityUtils;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/7.
 */
public class WorkAdapter extends BaseAdapter {

    private static final String TAG = WorkAdapter.class.getSimpleName();

    public static final int MAX_COLUMN = 2;

    private List<WorkInfoGroup> mDataList = new ArrayList<WorkInfoGroup>();

    private Context mContext = null;

    public WorkAdapter (Context context, List<WorkInfo> works) {
        mContext = context;
        addAllWorkInfo(works);
        //WorkInfoGroup group = new WorkInfoGroup();
        /*group.mInfo1 = new WorkInfo();
        group.mInfo2 = new WorkInfo();
        group.mInfo3 = new WorkInfo();
        group.mInfo1.setPhotoThumbnail("http://sfile.baidu.com/r/image/2015-03-06/232c50a98dd7c264233bad6937d403de.png");
        group.mInfo2.setPhotoThumbnail("http://ubmcmm.baidustatic.com/media/v1/0f000aJOWLDVWfyYtXV6Z6.jpg");
        group.mInfo3.setPhotoThumbnail("http://ww1.sinaimg.cn/bmiddle/6694d955jw1erwu2dne44j20bp0lbmyc.jpg");
        mDataList.add(group);*/
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public WorkInfoGroup getItem(int i) {
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
            for (int j = 0; j < MAX_COLUMN; j++) {
                try {
                    holder.imageViews[j] = (ImageView)view.findViewById(R.id.class.getField("work_" + j).getInt(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        WorkInfoGroup workInfo = getItem(i);
        for (int k = 0; k < MAX_COLUMN && k < workInfo.mInfoList.size(); k++) {
            //Log.v(TAG, "getView " + k + " " + holder.imageViews + " workInfo.mInfoList.get(k)=" + workInfo.mInfoList.get(k));
            final WorkInfo info = workInfo.mInfoList.get(k);
            ImageLoaderUtils.getImageLoader(mContext).displayImage(
                    info.getPhotoThumbnail(), holder.imageViews[k],
                    ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher)
            );

            holder.imageViews[k].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityUtils.startImagePreviewActivity(mContext, info.getPhotoUrl());
                }
            });
        }

        return view;
    }

    private void addAllWorkInfo (List<WorkInfo> workInfos) {
        if (workInfos == null || workInfos.isEmpty()) {
            return;
        }
        /*if (!mDataList.isEmpty()) {
            WorkInfoGroup lastGroup = mDataList.get(mDataList.size() - 1);
            List<WorkInfo> infoList = lastGroup.mInfoList;
            for (int i = infoList.size(); i < MAX_COLUMN && i < workInfos.size(); i++) {
                infoList.add(workInfos.remove(0));
            }
        }*/
        for (int i = 0; i < workInfos.size(); i++) {
            WorkInfo info = workInfos.get(i);
            if (!mDataList.isEmpty()) {
                WorkInfoGroup lastGroup = mDataList.get(mDataList.size() - 1);
                List<WorkInfo> infoList = lastGroup.mInfoList;
                if (infoList.size() < MAX_COLUMN) {
                    infoList.add(info);
                } else {
                    WorkInfoGroup group = new WorkInfoGroup();
                    group.mInfoList.add(info);
                    mDataList.add(group);
                }
            } else {
                WorkInfoGroup group = new WorkInfoGroup();
                group.mInfoList.add(info);
                mDataList.add(group);
            }
        }
        /*if (!mDataList.isEmpty()) {
            WorkInfoGroup lastGroup = mDataList.get(mDataList.size() - 1);

            for (int i = 0; i < MAX_COLUMN && !workInfos.isEmpty(); i++) {
                lastGroup.mInfoList.add(workInfos.remove(0));
            }

        }
        mDataList.addAll(makeWorkInfoGroupList(workInfos));*/
    }

    /*private List<WorkInfoGroup> makeWorkInfoGroupList (List<WorkInfo> workInfos) {
        List<WorkInfoGroup> groupList = new ArrayList<WorkInfoGroup>();
        //int index = 0;
        final int length = workInfos.size();
        for (int i = 0; i < length; i++) {
            WorkInfoGroup group = null;
            WorkInfo info = workInfos.get(i);
            if (groupList.isEmpty()) {
                group = new WorkInfoGroup();
                group.mInfo1 = info;
                groupList.add(group);
            } else {
                group = groupList.get(groupList.size() - 1);
                *//*if (group.mInfo1 == null) {
                    group.mInfo1 = info;
                } else *//*if (group.mInfo2 == null) {
                    group.mInfo2 = info;
                } else if (group.mInfo3 == null) {
                    group.mInfo3 = info;
                } else {
                    group = new WorkInfoGroup();
                    group.mInfo1 = info;
                    groupList.add(group);
                }
            }
        }

        return groupList;
    }*/

    private class ViewHolder {
        public ImageView[] imageViews = new ImageView[MAX_COLUMN];
    }

    private class WorkInfoGroup {
        public List<WorkInfo> mInfoList = new ArrayList<>();
//        public WorkInfo mInfo1, mInfo2, mInfo3;
    }
}
