package com.metis.meishuquan.adapter.studio;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.circle.moment.MomentActionBar;

import java.util.ArrayList;
import java.util.List;

public class CircleListAdapter extends BaseAdapter {
    private List<CCircleDetailModel> momentList = new ArrayList<CCircleDetailModel>();

    private OnCircleClickListener mListener = null;

    public CircleListAdapter(List<CCircleDetailModel> momentList) {
        this.momentList = momentList;
    }

    private class ViewHolder {
        ImageView avatar;
        TextView name;
        TextView grade;
        TextView createTime;
        TextView content;
        TextView device;
        ImageView imageView;
        MomentActionBar momentActionBar;
    }

    @Override
    public int getCount() {
        return momentList.size();
    }

    @Override
    public CCircleDetailModel getItem(int i) {
        return momentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup view) {
        ViewHolder viewHolder = null;
        final CCircleDetailModel moment =  momentList.get(i);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.id_img_portrait);
            viewHolder.name = (TextView) convertView.findViewById(R.id.id_username);
            viewHolder.grade = (TextView) convertView.findViewById(R.id.id_tv_grade);
            viewHolder.createTime = (TextView) convertView.findViewById(R.id.id_createtime);
            viewHolder.content = (TextView) convertView.findViewById(R.id.id_tv_content);
            viewHolder.device = (TextView) convertView.findViewById(R.id.tv_device);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.id_img_for_circle);
            viewHolder.momentActionBar = (MomentActionBar) convertView.findViewById(R.id.moment_action_bar);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageLoaderUtils.getImageLoader(convertView.getContext()).displayImage(
                moment.user.avatar, viewHolder.avatar,
                ImageLoaderUtils.getRoundDisplayOptions(convertView.getContext().getResources().getDimensionPixelSize(R.dimen.user_portrait_height)));
        viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnClick(v, i, moment);
                }
            }
        });

        viewHolder.name.setText(moment.user.name);
        viewHolder.grade.setText(moment.user.grade);
        viewHolder.createTime = (TextView) convertView.findViewById(R.id.id_createtime);
        viewHolder.content.setText(moment.content);
        if (moment.images.size() > 0)
        {
            viewHolder.imageView.setVisibility(View.VISIBLE);
            ImageLoaderUtils.getImageLoader(convertView.getContext()).displayImage(
                    moment.images.get(0).Thumbnails, viewHolder.imageView,
                    ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher));
        }
        else{
            viewHolder.imageView.setVisibility(View.GONE);
        }

        viewHolder.createTime.setText(moment.getTimeText());
        viewHolder.device.setText(moment.getDeviceText());
        viewHolder.momentActionBar.setData(moment.relayCount, moment.comentCount, moment.supportCount);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnClick(v, i, moment);
                }
            }
        });
        return convertView;
    }

    public void setOnCircleClickListener (OnCircleClickListener listener) {
        mListener = listener;
    }

    public static interface OnCircleClickListener {
        public void OnClick (View view, int index, CCircleDetailModel model);
    }
}