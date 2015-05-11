package com.metis.meishuquan.adapter.circle;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.assess.Assess;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.view.circle.moment.MomentActionBar;
import com.metis.meishuquan.view.circle.moment.comment.EmotionTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CircleMomentAdapter extends BaseAdapter {
    public enum MomentType
    {
        Moment,
        RepostMoment,
        Activity
    }

    @Override
    public int getViewTypeCount() {
        //TODO 1
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        //TODO 2
        return super.getItemViewType(position);
    }

    private List<CCircleDetailModel> momentList = new ArrayList<CCircleDetailModel>();
    private ViewHolder holder;

    public CircleMomentAdapter(List<CCircleDetailModel> momentList) {
        this.momentList = momentList;
    }

    private class ViewHolder {
        SmartImageView avatar;
        TextView name;
        TextView grade;
        TextView createTime;
        EmotionTextView content;
        TextView device;
        SmartImageView imageView;
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
        ViewHolder viewHolder = new ViewHolder();
        CCircleDetailModel moment =  momentList.get(i);

        // TODO 3 get real moment type from moment above
        MomentType type = MomentType.Moment;
        switch (type)
        {
            case Moment:
                if (convertView == null)
                {
                    convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_list_item, null);

                    viewHolder.avatar = (SmartImageView) convertView.findViewById(R.id.id_img_portrait);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.id_username);
                    viewHolder.grade = (TextView) convertView.findViewById(R.id.id_tv_grade);
                    viewHolder.createTime = (TextView) convertView.findViewById(R.id.id_createtime);
                    viewHolder.content = (EmotionTextView) convertView.findViewById(R.id.id_tv_content);
                    viewHolder.device = (TextView) convertView.findViewById(R.id.tv_device);
                    viewHolder.imageView = (SmartImageView) convertView.findViewById(R.id.id_img_content);
                    viewHolder.momentActionBar = (MomentActionBar) convertView.findViewById(R.id.moment_action_bar);
                    convertView.setTag(viewHolder);
                }
                else
                {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.avatar.setImageUrl(moment.user.avatar);
                viewHolder.name.setText(moment.user.name);
                viewHolder.grade.setText(moment.user.grade);
                viewHolder.createTime = (TextView) convertView.findViewById(R.id.id_createtime);
                viewHolder.content.setText(moment.content);
                if (moment.images.size() > 0)
                {
                    viewHolder.imageView.setVisibility(View.VISIBLE);
                    viewHolder.imageView.setImageUrl(moment.images.get(0).Thumbnails);
                }
                else{
                    viewHolder.imageView.setVisibility(View.GONE);
                }

                viewHolder.createTime.setText(moment.getTimeText());
                viewHolder.device.setText(moment.getDeviceText());
                viewHolder.momentActionBar.setData(moment.relayCount, moment.comentCount, moment.supportCount);
                break;

            case RepostMoment:
                //TODO 4
                break;

            case Activity:
                //TODO 5
                break;
        }

        return convertView;
    }
}