package com.metis.meishuquan.adapter.circle;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.circle.MomentDetailFragment;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.circle.moment.MomentActionBar;
import com.metis.meishuquan.view.circle.moment.comment.EmotionTextView;
import com.metis.meishuquan.view.course.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class CircleMomentAdapter extends BaseAdapter {
    private FragmentManager fm = null;

    private List<CCircleDetailModel> momentList = new ArrayList<CCircleDetailModel>();

    public CircleMomentAdapter(Context context,List<CCircleDetailModel> momentList) {
        this.momentList = momentList;
        fm = ((MainActivity) context).getSupportFragmentManager();
    }

    private class ViewHolder {
        ImageView avatar, chooseHuashi;
        TextView name;
        TextView grade;
        TextView createTime;
        EmotionTextView content;
        TextView device;
        ImageView imgForCircle;
        MomentActionBar momentActionBar;

        FlowLayout fl_atUsers;//@用户集合
        LinearLayout ll_circle;//非转发
        LinearLayout ll_not_circle;//转发
        ImageView imgForReply;
        TextView tvTitle;
        TextView tvInfo;
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
        final CCircleDetailModel moment = momentList.get(i);
        if (convertView == null) {
            convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_list_item, null);

            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.id_img_portrait);
            viewHolder.name = (TextView) convertView.findViewById(R.id.id_username);
            viewHolder.grade = (TextView) convertView.findViewById(R.id.id_tv_grade);
            viewHolder.createTime = (TextView) convertView.findViewById(R.id.id_createtime);
            viewHolder.content = (EmotionTextView) convertView.findViewById(R.id.id_tv_content);
            viewHolder.device = (TextView) convertView.findViewById(R.id.tv_device);
            viewHolder.imgForCircle = (ImageView) convertView.findViewById(R.id.id_img_for_circle);
            viewHolder.momentActionBar = (MomentActionBar) convertView.findViewById(R.id.moment_action_bar);

            viewHolder.fl_atUsers = (FlowLayout) convertView.findViewById(R.id.id_flowlayout_at_users);
            viewHolder.ll_circle = (LinearLayout) convertView.findViewById(R.id.id_ll_circle);
            viewHolder.ll_not_circle = (LinearLayout) convertView.findViewById(R.id.id_ll_not_circle);
            viewHolder.chooseHuashi = (ImageView) convertView.findViewById(R.id.id_img_choose_huashi);
            viewHolder.imgForReply = (ImageView) convertView.findViewById(R.id.id_img_for_not_circle);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.id_tv_title);
            viewHolder.tvInfo = (TextView) convertView.findViewById(R.id.id_tv_info);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (moment.relayCircle == null) {
            viewHolder.ll_not_circle.setVisibility(View.GONE);
            viewHolder.ll_circle.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ll_not_circle.setVisibility(View.VISIBLE);
            viewHolder.ll_circle.setVisibility(View.GONE);

            if (moment.relayCircle.type == SupportTypeEnum.Activity.getVal()) {
                if (moment.user.identity == IdTypeEnum.STUDENT.getVal() && moment.user.userId == MainApplication.userInfo.getUserId()) {
                    viewHolder.chooseHuashi.setVisibility(View.VISIBLE);
                    viewHolder.chooseHuashi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //TODO:选画室
                            chooseHuaShi(moment);
                            Toast.makeText(MainApplication.UIContext, "选画室", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (moment.relayCircle.type == SupportTypeEnum.News.getVal()) {
                    viewHolder.ll_not_circle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //进入新闻详情
                            navigatToNewInfo(moment.relayCircle.id);
                        }
                    });
                }
            } else {
                viewHolder.chooseHuashi.setVisibility(View.GONE);
            }
        }
        //头像
        ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(moment.user.avatar, viewHolder.avatar, ImageLoaderUtils.getRoundDisplayOptions(MainApplication.UIContext.getResources().getDimensionPixelSize(R.dimen.user_portrait_height)));
        //昵称
        viewHolder.name.setText(moment.user.name);
        //角色
        viewHolder.grade.setText(moment.user.grade);
        //创建时间
        viewHolder.createTime = (TextView) convertView.findViewById(R.id.id_createtime);
        //分享心得
        viewHolder.content.setText(moment.content);
        if (moment.images.size() > 0) {
            viewHolder.imgForCircle.setVisibility(View.VISIBLE);
            ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(moment.images.get(0).Thumbnails, viewHolder.imgForCircle);
        } else {
            viewHolder.imgForCircle.setVisibility(View.GONE);
        }

        viewHolder.createTime.setText(moment.getTimeText());
        viewHolder.device.setText(moment.getDeviceText());
        viewHolder.momentActionBar.setData(moment.relayCount, moment.comentCount, moment.supportCount);

        //转发部分
        if (moment.relayCircle != null) {
            if (moment.relayCircle.images != null && moment.relayCircle.images.size() > 0 && !moment.relayCircle.images.get(0).Thumbnails.isEmpty()) {
                ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(moment.relayCircle.images.get(0).Thumbnails, viewHolder.imgForReply);
            }
            viewHolder.tvTitle.setText(moment.relayCircle.title);
            viewHolder.tvInfo.setText(moment.relayCircle.desc);

            //跳转至详情
            viewHolder.ll_not_circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (moment.relayCircle.type == SupportTypeEnum.Activity.getVal()) {
                        //TODO:跳转至活动详情
                        navigatToActivityInfo(moment);
                    }
                }


            });
        }

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MomentDetailFragment momentDetailFragment = new MomentDetailFragment();
//                GlobalData.moment = moment;
//
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
//                ft.add(R.id.content_container, momentDetailFragment);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });

        return convertView;
    }

    //进入新闻详情
    private void navigatToNewInfo(int newId) {

    }

    private void navigatToActivityInfo(CCircleDetailModel moment) {
        //TODO:进入活动详情
    }

    private void chooseHuaShi(CCircleDetailModel moment) {
        //TODO:选画室
    }


}