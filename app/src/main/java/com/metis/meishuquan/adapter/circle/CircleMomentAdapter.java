package com.metis.meishuquan.adapter.circle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.act.ActDetailActivity;
import com.metis.meishuquan.activity.act.SelectStudioActivity;
import com.metis.meishuquan.activity.circle.ReplyActivity;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.fragment.circle.MomentCommentFragment;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CirclePushBlogParm;
import com.metis.meishuquan.model.circle.CirclePushCommentResult;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.circle.moment.MomentActionBar;
import com.metis.meishuquan.view.circle.moment.comment.EmotionTextView;
import com.metis.meishuquan.view.course.FlowLayout;
import com.metis.meishuquan.view.popup.SharePopupWindow;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

public class CircleMomentAdapter extends BaseAdapter {
    private FragmentManager fm = null;
    private Context mContext;
    private View parent;

    private List<CCircleDetailModel> momentList = new ArrayList<CCircleDetailModel>();

    public CircleMomentAdapter(Context context, List<CCircleDetailModel> momentList, View parent) {
        this.momentList = momentList;
        this.mContext = context;
        this.parent = parent;
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

            if (moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal()) {
                if (moment.user.identity == IdTypeEnum.STUDENT.getVal() && moment.user.userId == MainApplication.userInfo.getUserId()) {
                    viewHolder.chooseHuashi = (ImageView) convertView.findViewById(R.id.id_img_choose_huashi);
                    viewHolder.chooseHuashi.setVisibility(View.VISIBLE);
                    viewHolder.chooseHuashi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            chooseHuaShi(view, moment);
                            //Toast.makeText(MainApplication.UIContext, "选画室", Toast.LENGTH_SHORT).show();
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
        viewHolder.momentActionBar.setOnActionButtonClickListener(new MomentActionBar.OnActionButtonClickListener() {
            @Override
            public void onReply() {
                reply(moment);
            }

            @Override
            public void onComment() {
                comment(moment);
            }

            @Override
            public void onLike() {
                support(moment);
            }
        });

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
                    if (moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal()
                            || moment.relayCircle.type == SupportTypeEnum.ActivityStudio.getVal()) {
                        //跳转至活动详情
                        navigatToActivityInfo(view, moment);
                    } else if (moment.relayCircle.type == SupportTypeEnum.News.getVal()) {
                        navigatToNewsInfo(moment.relayCircle.id);
                    }
                }
            });
        }

        return convertView;
    }

    private void support(CCircleDetailModel moment) {

        String url = String.format("v1.1/Comment/Support?userid=%s&id=%s&type=7&result=1&session=%s", MainApplication.userInfo.getUserId(), moment.id, MainApplication.userInfo.getCookie());

        ApiDataProvider.getmClient().invokeApi(url, null,
                HttpGet.METHOD_NAME, null, CirclePushCommentResult.class,
                new ApiOperationCallback<CirclePushCommentResult>() {
                    @Override
                    public void onCompleted(CirclePushCommentResult result, Exception exception, ServiceFilterResponse response) {
                        if (result == null || !result.isSuccess()) {
                            return;
                        }
                        Toast.makeText(MainApplication.UIContext, "点赞成功！", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void comment(CCircleDetailModel moment) {
        GlobalData.getInstance().moment = moment;
        if (!MainApplication.isLogin()) {
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
            return;
        }
        MomentCommentFragment momentCommentFragment = new MomentCommentFragment();

        FragmentManager fm = ((MainActivity) mContext).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
        ft.add(R.id.content_container, momentCommentFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void reply(CCircleDetailModel moment) {
        Intent it = new Intent(mContext, ReplyActivity.class);
        CirclePushBlogParm parm = new CirclePushBlogParm();
        parm.setType(SupportTypeEnum.Activity.getVal());
        parm.setRelayId(moment.relayCircle.id);
        it.putExtra(ReplyActivity.PARM, parm);
        it.putExtra(ReplyActivity.TITLE, moment.relayCircle.title);
        it.putExtra(ReplyActivity.CONTENT, moment.relayCircle.desc);
//        it.putExtra(ReplyActivity.IMAGEURL, moment.relayCircle.images.get(0));
        mContext.startActivity(it);
    }

    //进入新闻详情
    private void navigatToNewsInfo(int newId) {

    }

    private static final String TAG = CircleMomentAdapter.class.getSimpleName();

    //进入活动详情
    private void navigatToActivityInfo(View view, CCircleDetailModel moment) {
//        Toast.makeText(view.getContext(), "navigatToActivityInfo", Toast.LENGTH_SHORT).show();
        Intent it = new Intent(view.getContext(), ActDetailActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        view.getContext().startActivity(it);

    }

    //选画室
    private void chooseHuaShi(View view, CCircleDetailModel moment) {
//        if (moment.relayCircle.upCount < 10) {
//            new AlertDialog.Builder(mContext)
//                    .setTitle("提示")
//                    .setMessage("您还没有集齐10个赞，还不能选画室，快拉小伙伴来给您点赞吧！")
//                    .setPositiveButton("确定", null)
//                    .show();
//            return;
//        }
        Log.v(TAG, "chooseHuaShi view.getContext=" + view.getContext());
        Toast.makeText(view.getContext(), "chooseHuaShi", Toast.LENGTH_SHORT).show();
        Intent it = new Intent(view.getContext(), SelectStudioActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        view.getContext().startActivity(it);

    }


}