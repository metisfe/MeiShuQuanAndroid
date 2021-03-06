package com.metis.meishuquan.adapter.circle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.act.ActDetailActivity;
import com.metis.meishuquan.activity.act.SelectStudioActivity;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.fragment.Topline.ItemInfoFragment;
import com.metis.meishuquan.fragment.circle.MomentCommentFragment;
import com.metis.meishuquan.fragment.circle.MomentDetailFragment;
import com.metis.meishuquan.fragment.commons.ListDialogFragment;
import com.metis.meishuquan.model.BLL.CircleOperator;
import com.metis.meishuquan.model.circle.CCircleCommentModel;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CirclePushCommentResult;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.ActivityUtils;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.Helper;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.circle.moment.MomentActionBar;
import com.metis.meishuquan.view.circle.moment.comment.EmotionTextView;
import com.metis.meishuquan.view.common.NinePictruesView;
import com.metis.meishuquan.view.course.FlowLayout;
import com.metis.meishuquan.view.popup.SharePopupWindow;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

public class CircleAtMeAdapter extends BaseAdapter {
    private static String KEY_ISSUPPORT = "已赞过";

    private static final int CIRCLE_TYPE = 0;
    private static final int REPLEY_TYPE = 1;

    private FragmentManager fm = null;
    private Context mContext;
    private View parent;

    private List<CCircleDetailModel> momentList = new ArrayList<CCircleDetailModel>();

    public CircleAtMeAdapter(Context context, List<CCircleDetailModel> momentList, View parent) {
        this.momentList = momentList;
        this.mContext = context;
        this.parent = parent;
        fm = ((FragmentActivity) context).getSupportFragmentManager();
    }

    private class ViewHolder {
        //        CircleMomentViewItem llCircle;
//
        ImageView avatar, chooseHuashi;
        TextView name;
        TextView grade;
        TextView createTime;
        EmotionTextView content;
        EmotionTextView replyCircleContent;
        TextView device;
        NinePictruesView imgForCircle;
        ImageView imgTop, imgMore;
        MomentActionBar momentActionBar;

        FlowLayout fl_atUsers;//@用户集合
        RelativeLayout rl_topbar;
        RelativeLayout btnTop;
        RelativeLayout btnMore;
        LinearLayout ll_circle;//非转发
        LinearLayout ll_not_circle;//转发
        ImageView imgForReply;
        TextView tvTitle;
        TextView tvInfo;
    }

//    private class ReplyViewHolder {
//        CircleReplyMomentViewItem llReply;
//    }

//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        CCircleDetailModel moment = momentList.get(position);
//        if (moment.relayCircle == null) {
//            return CIRCLE_TYPE;
//        } else if (moment.relayCircle != null && moment.relayCircle.type == SupportTypeEnum.Circle.getVal()) {
//            return CIRCLE_TYPE;
//        } else if (moment.relayCircle != null&& moment.relayCircle.type != SupportTypeEnum.Circle.getVal()) {
//            return REPLEY_TYPE;
//        }
//        return 0;
//    }

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
        final CCircleDetailModel moment = momentList.get(i);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_list_item, null);

            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.id_img_portrait);
            viewHolder.name = (TextView) convertView.findViewById(R.id.id_username);
            viewHolder.grade = (TextView) convertView.findViewById(R.id.id_tv_grade);
            viewHolder.createTime = (TextView) convertView.findViewById(R.id.id_createtime);
            viewHolder.content = (EmotionTextView) convertView.findViewById(R.id.id_tv_content);
            viewHolder.replyCircleContent = (EmotionTextView) convertView.findViewById(R.id.id_emotion_tv_content);
            viewHolder.device = (TextView) convertView.findViewById(R.id.tv_device);
            viewHolder.imgForCircle = (NinePictruesView) convertView.findViewById(R.id.id_img_for_circle);
            viewHolder.imgTop = (ImageView) convertView.findViewById(R.id.id_img_top);
            viewHolder.imgMore = (ImageView) convertView.findViewById(R.id.id_img_more);
            viewHolder.momentActionBar = (MomentActionBar) convertView.findViewById(R.id.moment_action_bar);

            viewHolder.fl_atUsers = (FlowLayout) convertView.findViewById(R.id.id_flowlayout_at_users);
            viewHolder.rl_topbar = (RelativeLayout) convertView.findViewById(R.id.id_rl_topbar);
            viewHolder.btnTop = (RelativeLayout) convertView.findViewById(R.id.id_btn_top);
            viewHolder.btnMore = (RelativeLayout) convertView.findViewById(R.id.id_btn_more);
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

        viewHolder.chooseHuashi.setVisibility(View.GONE);

        final boolean isMyWeibo = MainApplication.userInfo != null && moment.user.userId == MainApplication.userInfo.getUserId();
        viewHolder.imgMore.setVisibility(isMyWeibo ? View.VISIBLE : View.GONE);
        viewHolder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMyWeibo) {
                    ListDialogFragment.getInstance().setAdapter(new ListDialogFragment.SimpleAdapter(mContext, new String[]{"删除"}));
                    ListDialogFragment.getInstance().show(fm, TAG);
                    ListDialogFragment.getInstance().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            CircleOperator.getInstance().deleteCircle(moment.id, new ApiOperationCallback<Result<String>>() {

                                @Override
                                public void onCompleted(Result<String> result, Exception exception, ServiceFilterResponse response) {
                                    Log.v(TAG, "deleteCircle=" + result.getData());
                                    if (result.getOption().getStatus() == 0) {
                                        momentList.remove(moment);
                                        notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(mContext, R.string.delete_failed, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            ListDialogFragment.getInstance().dismiss();
                            ListDialogFragment.getInstance().setOnItemClickListener(null);
                        }
                    });
                }
            }
        });

        //顶部条的显示
        if (moment.user.userId == MainApplication.userInfo.getUserId()) {
            viewHolder.rl_topbar.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rl_topbar.setVisibility(View.GONE);
        }

        //标识置顶状态
        if (GlobalData.momentsGroupId == 3 && moment.user.userId == MainApplication.userInfo.getUserId() && moment.relayCircle != null
                && (moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal() || moment.relayCircle.type == SupportTypeEnum.CircleActivity.getVal())
                && moment.userMark.isTop) {
            viewHolder.imgTop.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgTop.setVisibility(View.GONE);
        }

        //朋友圈类型
        if (moment.relayCircle == null) {
            viewHolder.chooseHuashi.setVisibility(View.GONE);//隐藏选画室
            viewHolder.ll_not_circle.setVisibility(View.GONE);
            viewHolder.ll_circle.setVisibility(View.VISIBLE);
            viewHolder.ll_circle.setBackgroundColor(MainApplication.UIContext.getResources().getColor(R.color.white));
            viewHolder.replyCircleContent.setVisibility(View.GONE);

            //纯文字
            if (moment.images == null || moment.images.size() == 0) {
                viewHolder.chooseHuashi.setVisibility(View.GONE);//隐藏选画室
                viewHolder.content.setVisibility(View.VISIBLE);
                if (moment.content.length() > 0) {
                    viewHolder.content.setText(moment.content);
                } else {
                    viewHolder.content.setVisibility(View.GONE);
                }

                viewHolder.ll_circle.setVisibility(View.GONE);//隐藏图片区域
            }

            //纯图片
            else if (moment.content.equals("") && moment.images != null
                    && moment.images.size() > 0) {
                viewHolder.content.setVisibility(View.GONE);//隐藏文字
                viewHolder.chooseHuashi.setVisibility(View.GONE);//隐藏选画室
                viewHolder.imgForCircle.removeAllViews();
                viewHolder.imgForCircle.setLstCircleImage(moment.images);
            }

            //有文字和图片
            else {
                viewHolder.chooseHuashi.setVisibility(View.GONE);//隐藏选画室
                viewHolder.content.setVisibility(View.VISIBLE);
                viewHolder.ll_circle.setVisibility(View.VISIBLE);
                if (moment.content.length() > 0) {
                    viewHolder.content.setText(moment.content);
                } else {
                    viewHolder.content.setVisibility(View.GONE);
                }
                viewHolder.imgForCircle.removeAllViews();
                viewHolder.imgForCircle.setLstCircleImage(moment.images);
            }
        }

        //转发类型
        else if (moment.relayCircle != null) {
            viewHolder.ll_not_circle.setVisibility(View.VISIBLE);
            viewHolder.ll_circle.setVisibility(View.GONE);

            //活动类型
            if (moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal()) {
                if (moment.content.length() > 0) {
                    viewHolder.content.setVisibility(View.VISIBLE);
                    viewHolder.content.setText(moment.content);
                } else {
                    viewHolder.content.setVisibility(View.GONE);
                }
                viewHolder.tvTitle.setText(moment.relayCircle.title);
                viewHolder.tvInfo.setText(moment.relayCircle.desc);
                //选画室
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
            }

            //朋友圈类型
            else if (moment.relayCircle.type == SupportTypeEnum.Circle.getVal()) {
                viewHolder.chooseHuashi.setVisibility(View.GONE);//隐藏选画室
                viewHolder.ll_not_circle.setVisibility(View.GONE);
                viewHolder.ll_circle.setVisibility(View.VISIBLE);
                viewHolder.ll_circle.setBackgroundColor(MainApplication.UIContext.getResources().getColor(R.color.common_color_f1f2f4));
                viewHolder.replyCircleContent.setVisibility(View.GONE);//隐藏转发内容

                //纯文字
                if (moment.relayCircle.images == null || moment.relayCircle.images.size() == 0) {
                    viewHolder.replyCircleContent.setVisibility(View.VISIBLE);
                    viewHolder.replyCircleContent.setText(moment.relayCircle.desc);
                    viewHolder.ll_circle.setVisibility(View.GONE);//隐藏图片区域
                }

                //纯图片
                else if (moment.relayCircle.desc.equals("") && moment.relayCircle.images != null
                        && moment.relayCircle.images.size() > 0) {
                    viewHolder.replyCircleContent.setVisibility(View.GONE);//隐藏转发内容
                    viewHolder.imgForCircle.removeAllViews();
                    viewHolder.imgForCircle.setLstCircleImage(moment.images);
                }

                //有文字和图片
                else {
                    viewHolder.replyCircleContent.setVisibility(View.VISIBLE);
                    viewHolder.replyCircleContent.setText("@" + moment.relayCircle.user.name + ":" + moment.relayCircle.desc);
                    viewHolder.imgForCircle.removeAllViews();
                    viewHolder.imgForCircle.setLstCircleImage(moment.relayCircle.images);
                }
            } else if (moment.relayCircle.type == SupportTypeEnum.News.getVal()) {//新闻类型
                viewHolder.chooseHuashi.setVisibility(View.GONE);//隐藏选画室
                viewHolder.ll_circle.setVisibility(View.GONE);
                viewHolder.ll_not_circle.setVisibility(View.VISIBLE);
                viewHolder.content.setText(moment.content);
                viewHolder.imgForReply.refreshDrawableState();
                ImageLoaderUtils.getImageLoader(mContext).displayImage(moment.relayCircle.activityImg, viewHolder.imgForReply);

                viewHolder.tvTitle.setText(moment.relayCircle.title);
                viewHolder.tvInfo.setText(moment.relayCircle.desc);
            }
        }

        //头像
        viewHolder.avatar.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_portrait_fang));
        ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(moment.user.avatar, viewHolder.avatar, ImageLoaderUtils.getRoundDisplayOptions(MainApplication.UIContext.getResources().getDimensionPixelSize(R.dimen.user_portrait_height)));
        //昵称
        viewHolder.name.setText(moment.user.name);
        //角色
        viewHolder.grade.setText(moment.user.grade);
        //创建时间
        viewHolder.createTime = (TextView) convertView.findViewById(R.id.id_createtime);
        //分享心得
        viewHolder.content.setText(moment.content);
//        if (moment.images.size() > 0) {
//            viewHolder.imgForCircle.setVisibility(View.VISIBLE);
//            ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(moment.images.get(0).Thumbnails, viewHolder.imgForCircle);
//        } else {
//            viewHolder.imgForCircle.setVisibility(View.GONE);
//        }

        viewHolder.createTime.setText(moment.getTimeText());
        viewHolder.device.setText(moment.getDeviceText());
        viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.startNameCardActivity(mContext, (int) moment.user.userId);
            }
        });

        //朋友圈类型，查看大图
//        viewHolder.imgForCircle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (moment.images != null && moment.images.size() > 0) {
//                    Intent intent = new Intent(mContext, ImagePreviewActivity.class);
//                    intent.putStringArrayListExtra(ImagePreviewActivity.KEY_IMAGE_URL_ARRAY, moment.getImagesUrl());
//                    intent.putExtra(ImagePreviewActivity.KEY_START_INDEX, 0);
//                    mContext.startActivity(intent);
//                    ((FragmentActivity) mContext).overridePendingTransition(R.anim.activity_zoomin, 0);
//                } else if (moment.relayCircle != null && moment.relayCircle.images != null && moment.relayCircle.images.size() > 0) {
//                    Intent intent = new Intent(mContext, ImagePreviewActivity.class);
//                    intent.putStringArrayListExtra(ImagePreviewActivity.KEY_IMAGE_URL_ARRAY, moment.relayCircle.getImagesUrl());
//                    intent.putExtra(ImagePreviewActivity.KEY_START_INDEX, 0);
//                    mContext.startActivity(intent);
//                    ((FragmentActivity) mContext).overridePendingTransition(R.anim.activity_zoomin, 0);
//                }
//            }
//        });

        //重新初始化
        final ViewHolder finalViewHolder = viewHolder;

        //判断是否已赞过
        String isSupportStr = SharedPreferencesUtil.getInstanse(mContext).getStringByKey(MainApplication.userInfo.getUserId() + moment.id + "");
        if (moment.relayCircle != null && moment.relayCircle.type != SupportTypeEnum.ActivityStudent.getVal() && moment.userMark.isSupport) {
            finalViewHolder.momentActionBar.setCheck();
        } else if (moment.relayCircle != null && moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal() && moment.relayCircle.userMark.isSupport) {
            finalViewHolder.momentActionBar.setCheck();
        } else if (isSupportStr.equals(KEY_ISSUPPORT)) {
            finalViewHolder.momentActionBar.setCheck();
        } else {
            finalViewHolder.momentActionBar.setUncheck();
        }

        int supportCount = 0;
        if (moment.relayCircle != null && moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal()) {
            supportCount = moment.relayCircle.upCount;
        } else {
            supportCount = moment.supportCount;
        }
        finalViewHolder.momentActionBar.setData(moment.relayCount, moment.comentCount, supportCount);
        final int finalSupportCount = supportCount;
        final View finalConvertView = convertView;
        finalViewHolder.momentActionBar.setOnActionButtonClickListener(new MomentActionBar.OnActionButtonClickListener() {
            @Override
            public void onReply() {
                reply(moment, finalConvertView);
            }

            @Override
            public void onComment() {
                comment(moment);
            }

            @Override
            public void onLike() {
                if (moment.relayCircle != null && moment.relayCircle.type != SupportTypeEnum.ActivityStudent.getVal() && moment.userMark.isSupport) {
                    Toast.makeText(mContext, "您已顶过", Toast.LENGTH_SHORT).show();
                    return;
                } else if (moment.relayCircle != null && moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal() && moment.relayCircle.userMark.isSupport) {
                    Toast.makeText(mContext, "您已顶过", Toast.LENGTH_SHORT).show();
                    return;
                }
                String isSupportStr = SharedPreferencesUtil.getInstanse(mContext).getStringByKey(MainApplication.userInfo.getUserId() + moment.id + "");
                if (isSupportStr.equals(KEY_ISSUPPORT)) {
                    Toast.makeText(mContext, "您已顶过", Toast.LENGTH_SHORT).show();
                    return;
                }

                //+1动画
                Helper.getInstance(mContext).supportOrStep(finalViewHolder.momentActionBar.tvLikeCount, finalViewHolder.momentActionBar.tvAddOne, finalViewHolder.momentActionBar.imgSupport,
                        finalSupportCount, true);
                support(moment);
            }
        });

        //转发部分
        if (moment.relayCircle != null) {
            ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(moment.relayCircle.activityImg, viewHolder.imgForReply);
            viewHolder.tvTitle.setText(moment.relayCircle.title);
            viewHolder.tvInfo.setText(moment.relayCircle.desc);

            //跳转至详情
            viewHolder.ll_not_circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (moment.relayCircle.type == SupportTypeEnum.CircleActivity.getVal()
                            || moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal()) {
                        //跳转至活动详情
                        navigatToActivityInfo(view, moment);
                    } else if (moment.relayCircle != null && moment.relayCircle.type == SupportTypeEnum.News.getVal()) {
                        navigatToNewsInfo(moment);
                    }
                }
            });
        }
        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MomentDetailFragment momentDetailFragment = new MomentDetailFragment();
//                Bundle args = new Bundle();
//                args.putInt("newsId", newsId);
//                itemInfoFragment.setArguments(args);

                // refresh load more listview has header
                GlobalData.moment = momentList.get(i);

                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, momentDetailFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });*/
        return convertView;
    }

    private void delete(final CCircleDetailModel moment) {
        ListDialogFragment.getInstance().setAdapter(new ListDialogFragment.SimpleAdapter(mContext, new String[]{"删除"}));
        ListDialogFragment.getInstance().show(fm, TAG);
        ListDialogFragment.getInstance().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CircleOperator.getInstance().deleteCircle(moment.id, new ApiOperationCallback<Result<String>>() {

                    @Override
                    public void onCompleted(Result<String> result, Exception exception, ServiceFilterResponse response) {
                        Log.v(TAG, "deleteCircle=" + result.getData());
                        if (result.getOption().getStatus() == 0) {
                            momentList.remove(moment);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, R.string.delete_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ListDialogFragment.getInstance().dismiss();
                ListDialogFragment.getInstance().setOnItemClickListener(null);
            }
        });
    }

    private void support(final CCircleDetailModel moment) {
        int supportId = 0;
        int typeId = 0;
        if (moment.relayCircle != null && moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal()) {
            supportId = moment.relayCircle.joinActivityId;
            typeId = moment.relayCircle.type;
        } else {
            supportId = moment.id;
            typeId = SupportTypeEnum.Circle.getVal();
        }

        String url = String.format("v1.1/Comment/Support?userid=%s&id=%s&type=%s&result=1&session=%s", MainApplication.userInfo.getUserId(), supportId, typeId, MainApplication.getSession());

        ApiDataProvider.getmClient().invokeApi(url, null,
                HttpGet.METHOD_NAME, null, CirclePushCommentResult.class,
                new ApiOperationCallback<CirclePushCommentResult>() {
                    @Override
                    public void onCompleted(CirclePushCommentResult result, Exception exception, ServiceFilterResponse response) {
                        if (result == null || !result.isSuccess()) {
                            return;
                        }
                        SharedPreferencesUtil.getInstanse(mContext).add(MainApplication.userInfo.getUserId() + moment.id + "", KEY_ISSUPPORT);
                        //Toast.makeText(MainApplication.UIContext, "点赞成功！", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void comment(CCircleDetailModel moment) {
        GlobalData.momentsReplyCount = moment.comentCount;
        GlobalData.getInstance().moment = moment;
        if (!MainApplication.isLogin()) {
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
            return;
        }
        MomentCommentFragment momentCommentFragment = new MomentCommentFragment();

        //传递参数
        Bundle bundle = new Bundle();
        bundle.putInt(MomentCommentFragment.KEY_CIRCLE_ID, moment.id);
        momentCommentFragment.setArguments(bundle);

        //评论成功回调
        momentCommentFragment.setOnCommentSuccessListner(new MomentDetailFragment.OnCommentSuccessListner() {
            @Override
            public void onSuccess(CCircleCommentModel circleCommentModel) {
                MomentDetailFragment momentDetailFragment = new MomentDetailFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, momentDetailFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
        ft.add(R.id.content_container, momentCommentFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    //转发
    private void reply(CCircleDetailModel moment, View convertView) {
        SharePopupWindow sharePopupWindow = new SharePopupWindow((FragmentActivity) mContext, convertView);
        String title = "xxx";
        String content = "xxx";
        if (moment.relayCircle == null) {
            content = moment.content.isEmpty() ? "分享图片" : moment.content;
        } else if (moment.relayCircle != null) {
            content = moment.relayCircle.desc.isEmpty() ? "分享图片" : moment.relayCircle.desc;
        }

        String shareUrl = moment.getShareUrl() + moment.id;
        String imgUrl = moment.relayImgUrl;
        Log.i("reply_args", "title:" + title + "content:" + content + "shareUrl:" + shareUrl + "imgUrl:" + imgUrl + "");
        sharePopupWindow.setShareInfo(title, content, shareUrl, imgUrl, moment);

//        Intent it = new Intent(mContext, ReplyActivity.class);
//        CirclePushBlogParm parm = new CirclePushBlogParm();
//        if (moment.relayCircle != null && moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal()) {
//            parm.setType(SupportTypeEnum.CircleActivity.getVal());
//            parm.setRelayId(moment.relayCircle.id);
//        } else if (moment.relayCircle != null && moment.relayCircle.type == SupportTypeEnum.News.getVal()) {
//            parm.setType(SupportTypeEnum.News.getVal());
//            parm.setRelayId(moment.relayCircle.id);
//        } else {
//            parm.setType(SupportTypeEnum.Circle.getVal());
//            parm.setRelayId(moment.id);
//        }
//        it.putExtra(ReplyActivity.PARM, parm);
//        if (moment.relayCircle != null && (moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal() || moment.relayCircle.type == SupportTypeEnum.News.getVal())) {
//            it.putExtra(ReplyActivity.TITLE, moment.relayCircle.title);
//            it.putExtra(ReplyActivity.CONTENT, moment.relayCircle.desc);
//            it.putExtra(ReplyActivity.IMAGEURL, moment.relayCircle.activityImg);
//        } else {
//            it.putExtra(ReplyActivity.TITLE, moment.user.name);
//            it.putExtra(ReplyActivity.CONTENT, moment.content);
//            it.putExtra(ReplyActivity.IMAGEURL, moment.user.avatar);
//        }

//        mContext.startActivity(it);
    }

    //进入新闻详情
    private void navigatToNewsInfo(CCircleDetailModel moment) {
        ItemInfoFragment itemInfoFragment = new ItemInfoFragment();
        Bundle args = new Bundle();
        args.putInt("newsId", moment.relayCircle.id);
        args.putString(ItemInfoFragment.KEY_SHARE_IMG_URL, moment.relayCircle.activityImg);
        itemInfoFragment.setArguments(args);
        FragmentTransaction ft = ((MainActivity) mContext).getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_container, itemInfoFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private static final String TAG = CircleAtMeAdapter.class.getSimpleName();

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
        Intent it = new Intent(view.getContext(), SelectStudioActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        view.getContext().startActivity(it);

    }


}