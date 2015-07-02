package com.metis.meishuquan.fragment.circle;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.act.ActDetailActivity;
import com.metis.meishuquan.activity.info.ImagePreviewActivity;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.fragment.Topline.ItemInfoFragment;
import com.metis.meishuquan.model.BLL.CircleOperator;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.circle.CCircleCommentModel;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CCircleReplyModel;
import com.metis.meishuquan.model.circle.CCircleTabModel;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.circle.CircleMomentDetail;
import com.metis.meishuquan.model.circle.CirclePushCommentResult;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.ActivityUtils;
import com.metis.meishuquan.util.AlertDialogUtils;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.circle.PopupAttentionWindow;
import com.metis.meishuquan.view.circle.moment.MomentActionBar;
import com.metis.meishuquan.view.circle.moment.MomentPageListView;
import com.metis.meishuquan.view.circle.moment.comment.EmotionTextView;
import com.metis.meishuquan.view.common.NinePictruesView;
import com.metis.meishuquan.view.course.FlowLayout;
import com.metis.meishuquan.view.popup.SharePopupWindow;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

/**
 * 圈子列表项详细信息界面
 * <p/>
 * Created by jx on 15/4/11.
 */
public class MomentDetailFragment extends Fragment {
    public static final String CLASS_NAME = MomentDetailFragment.class.getSimpleName();
    public static final String KEY_MOMENT_ID = "KEY_MOMENT_ID";

    private MomentPageListView listView;
    private Button btnBack;
    private ImageView btnShare, btnLike;
    private ViewGroup rootView;
    private View footerView;

    private TextView tv_nickname;
    private RelativeLayout rl_writeCommont;
    private ImageView mHeaderView = null;

    private ImageView avatar, chooseHuashi;
    private TextView name;
    private TextView grade;
    private TextView createTime;
    private EmotionTextView content;
    private EmotionTextView replyContent;
    private TextView device;
    private NinePictruesView imgForCircle;
    private ImageView imgAttention;
    private MomentActionBar momentActionBar;

    private FlowLayout fl_atUsers;//@用户集合
    private LinearLayout ll_circle;//非转发
    private LinearLayout ll_not_circle;//转发
    private ImageView imgForReply;
    private TextView tvTitle;
    private TextView tvInfo;

    private View mTitleView = null;

    private boolean isAttention;

    private FragmentManager fm;

    private int momentId;

    private CircleMomentDetailReplyAdpater circleMomentDetailReplyAdpater;
    private CircleMomentDetailCommentAdapter circleMomentCommentAdapter;
    private CircleMomentDetailLikeAdapter circleMomentLikeAdapter;

    private MomentActionBar actionBar;
    private CCircleDetailModel moment;
    private List<CCircleCommentModel> list = new ArrayList<CCircleCommentModel>();
    List<CCircleReplyModel> replyList = new ArrayList<CCircleReplyModel>();
    List<CCircleCommentModel> commentList = new ArrayList<CCircleCommentModel>();
    List<CUserModel> likeList = new ArrayList<CUserModel>();
    private boolean isCommentShown = true;
    private boolean isLikeShow = false;
    private boolean isShareShow = false;
    private PopupAttentionWindow popupAttentionWindow;

    private int mActionBarVisible = View.VISIBLE;

    public interface OnCommentSuccessListner {
        void onSuccess(CCircleCommentModel circleCommentModel);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(CLASS_NAME); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(CLASS_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_moment_detail, null, false);
//        if (GlobalData.moment != null) {
//            this.moment = GlobalData.moment;
//        }
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "正在加载...");

        Bundle bundle = getArguments();
        if (bundle != null) {
            momentId = bundle.getInt(KEY_MOMENT_ID);
        }

        CircleOperator.getInstance().getMomentDetail(momentId, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                progressDialog.cancel();
                if (result != null && result.isSuccess()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    ReturnInfo<CCircleDetailModel> returnInfo = gson.fromJson(json, new TypeToken<ReturnInfo<CCircleDetailModel>>() {
                    }.getType());
                    moment = returnInfo.getData();
                    initView(rootView);
                    getData(0, 0); // type = 0 : get comment and like
                    circleMomentCommentAdapter = new CircleMomentDetailCommentAdapter(list);
                    listView.setAdapter(circleMomentCommentAdapter);
                    initEvent();
                } else if (result != null && !result.isSuccess()) {
                    if (!result.getMessage().isEmpty()) {
                        Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(final ViewGroup rootView) {
        mTitleView = rootView.findViewById(R.id.circle_moment_detail_header);
        btnBack = (Button) rootView.findViewById(R.id.btn_back);
        tv_nickname = (TextView) rootView.findViewById(R.id.moment_detail_tv_nickname);

        rl_writeCommont = (RelativeLayout) rootView.findViewById(R.id.id_rl_writecomment);
        btnLike = (ImageView) rootView.findViewById(R.id.circle_moment_detail_footer_like);
        btnShare = (ImageView) rootView.findViewById(R.id.circle_moment_detail_footer_share);

        listView = (MomentPageListView) rootView.findViewById(R.id.moment_detail_listview);

        //tab切换事件
        MomentActionBar.OnActionButtonClickListener OnActionButtonClickListener = new MomentActionBar.OnActionButtonClickListener() {
            @Override
            public void onReply() {
                momentActionBar.showTab(0);
                actionBar.showTab(0);
                if (GlobalData.moment.relayCount > 0) {
                    footerView.setVisibility(View.GONE);
                } else {
                    footerView.setVisibility(View.VISIBLE);
                }
                if (!isShareShow) {
                    circleMomentDetailReplyAdpater = new CircleMomentDetailReplyAdpater(replyList);
                    listView.setAdapter(circleMomentDetailReplyAdpater);
                    circleMomentDetailReplyAdpater.notifyDataSetChanged();
                    listView.setSelection(1);
                    isShareShow = true;
                    isCommentShown = false;
                    isLikeShow = false;
                }
            }

            @Override
            public void onComment() {
                momentActionBar.showTab(1);
                actionBar.showTab(1);
                if (GlobalData.moment.comentCount > 0) {
                    footerView.setVisibility(View.GONE);
                } else {
                    footerView.setVisibility(View.VISIBLE);
                }
                if (!isCommentShown) {
                    circleMomentCommentAdapter = new CircleMomentDetailCommentAdapter(commentList);
                    listView.setAdapter(circleMomentCommentAdapter);
                    circleMomentCommentAdapter.notifyDataSetChanged();
                    isCommentShown = true;
                    isShareShow = false;
                    isLikeShow = false;
                }
            }

            @Override
            public void onLike() {
                momentActionBar.showTab(2);
                actionBar.showTab(2);
                if (GlobalData.moment.supportCount > 0) {
                    footerView.setVisibility(View.GONE);
                } else {
                    footerView.setVisibility(View.VISIBLE);
                }
                if (!isLikeShow) {
                    circleMomentLikeAdapter = new CircleMomentDetailLikeAdapter(likeList);
                    listView.setAdapter(circleMomentLikeAdapter);
                    circleMomentLikeAdapter.notifyDataSetChanged();
                    listView.setSelection(1);
                    isCommentShown = false;
                    isShareShow = false;
                    isLikeShow = true;
                }
            }
        };

        View headerView = initHeaderView(moment, OnActionButtonClickListener);
        footerView = initFooterView(moment);
        actionBar = (MomentActionBar) rootView.findViewById(R.id.moment_action_bar);
        listView.addHeaderView(headerView);
        listView.addFooterView(footerView);
        if (moment.comentCount == 0) {
            footerView.setVisibility(View.VISIBLE);
        } else {
            footerView.setVisibility(View.GONE);
        }

        actionBar.setData(moment.relayCount, moment.comentCount, moment.supportCount);
        actionBar.setOnActionButtonClickListener(OnActionButtonClickListener);

        listView.setOnSocialActionBarPositionChangedListener(new MomentPageListView.OnSocialActionBarPositionChangedListener() {
            @Override
            public void OnSocialActionBarPositionChanged(MomentPageListView.Position type) {
                if (type == MomentPageListView.Position.Top) {
                    actionBar.setVisibility(View.VISIBLE);
                } else {
                    actionBar.setVisibility(View.GONE);
                }
            }
        });

        fm = getActivity().getSupportFragmentManager();
    }

    private View initHeaderView(final CCircleDetailModel moment, MomentActionBar.OnActionButtonClickListener OnActionButtonClickListener) {
        View headerView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_list_item, null);

        mHeaderView = (ImageView) headerView.findViewById(R.id.id_img_portrait);
        name = (TextView) headerView.findViewById(R.id.id_username);
        grade = (TextView) headerView.findViewById(R.id.id_tv_grade);
        createTime = (TextView) headerView.findViewById(R.id.id_createtime);
        content = (EmotionTextView) headerView.findViewById(R.id.id_tv_content);
        content.setTextIsSelectable(true);
        replyContent = (EmotionTextView) headerView.findViewById(R.id.id_emotion_tv_content);
        replyContent.setTextIsSelectable(true);
        device = (TextView) headerView.findViewById(R.id.tv_device);
        imgForCircle = (NinePictruesView) headerView.findViewById(R.id.id_img_for_circle);
        momentActionBar = (MomentActionBar) headerView.findViewById(R.id.moment_action_bar);

        fl_atUsers = (FlowLayout) headerView.findViewById(R.id.id_flowlayout_at_users);
        ll_circle = (LinearLayout) headerView.findViewById(R.id.id_ll_circle);
        ll_not_circle = (LinearLayout) headerView.findViewById(R.id.id_ll_not_circle);

        //关注
        imgAttention = (ImageView) headerView.findViewById(R.id.id_img_attention);
        if (moment.user.userId != MainApplication.userInfo.getUserId()) {
            imgAttention.setVisibility(View.VISIBLE);
        }

        if (moment.userMark.isAttention) {
            imgAttention.setImageDrawable(getResources().getDrawable(R.drawable.bg_btn_attention));
            isAttention = true;
        } else {
            imgAttention.setImageDrawable(getResources().getDrawable(R.drawable.bg_btn_unattention));
            isAttention = false;
        }

        if (moment.userMark.isSupport) {
            btnLike.setImageDrawable(getResources().getDrawable(R.drawable.icon_support));
            btnLike.setTag(true);
        } else {
            btnLike.setImageDrawable(getResources().getDrawable(R.drawable.icon_unsupport));
            btnLike.setTag(false);
        }

//        if (moment.relayCircle == null) {
//            replyContent.setVisibility(View.GONE);
//        } else if (moment.relayCircle != null && moment.relayCircle.desc != null && moment.relayCircle.desc.isEmpty()) {
//            replyContent.setVisibility(View.VISIBLE);
//            replyContent.setText(moment.relayCircle.desc);
//        }

        imgForReply = (ImageView) headerView.findViewById(R.id.id_img_for_not_circle);
        tvTitle = (TextView) headerView.findViewById(R.id.id_tv_title);
        tvInfo = (TextView) headerView.findViewById(R.id.id_tv_info);

        actionBar = (MomentActionBar) headerView.findViewById(R.id.moment_action_bar);

        //判断朋友圈类型，并控制显示隐藏相应区域
        if (moment.relayCircle == null) {
            ll_not_circle.setVisibility(View.GONE);
            ll_circle.setVisibility(View.VISIBLE);
        } else {
            if (moment.relayCircle.type == SupportTypeEnum.Circle.getVal()) {
                ll_not_circle.setVisibility(View.GONE);
                ll_circle.setVisibility(View.VISIBLE);
            } else if (moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal()) {
                ll_not_circle.setVisibility(View.VISIBLE);
                ll_circle.setVisibility(View.GONE);
            } else if (moment.relayCircle.type == SupportTypeEnum.News.getVal()) {
                ll_not_circle.setVisibility(View.VISIBLE);
                ll_circle.setVisibility(View.GONE);
            } else if (moment.relayCircle.type == SupportTypeEnum.CircleActivity.getVal()) {
                ll_not_circle.setVisibility(View.VISIBLE);
                ll_circle.setVisibility(View.GONE);
            }
        }

        actionBar.setOnActionButtonClickListener(OnActionButtonClickListener);
        setTitleViewVisible(mActionBarVisible);
        bindHeaderViewData(moment);
        initHeaderViewEvent(moment);
        return headerView;
    }

    private View initFooterView(final CCircleDetailModel moment) {
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.circle_moent_detail_footer, null, false);
        return footerView;
    }

    public void setTitleViewVisible(int visible) {
        mActionBarVisible = visible;
        if (mTitleView != null) {
            mTitleView.setVisibility(visible);
        }
    }

    private void bindHeaderViewData(CCircleDetailModel moment) {
        //头像
        ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(moment.user.avatar, mHeaderView, ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.user_portrait_height)));
        name.setText(moment.user.name);
        tv_nickname.setText(moment.user.name);
        grade.setText(moment.user.grade);
        createTime.setText(moment.getTimeText());
        content.setText(moment.content);
        device.setText(moment.getDeviceText());
        actionBar.setData(moment.relayCount, moment.comentCount, moment.supportCount);

        //朋友圈类型
        if (ll_circle.getVisibility() == View.VISIBLE) {
            if (moment.relayCircle == null) {
                replyContent.setVisibility(View.GONE);
                if (moment.images != null && moment.images.size() > 0 && !moment.images.get(0).equals("")) {
                    imgForCircle.setLstCircleImage(moment.images);
//                    ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(moment.images.get(0).Thumbnails, imgForCircle);
                } else {
                    imgForCircle.setVisibility(View.GONE);
                }
            } else {//转发类型
                if (moment.relayCircle.desc.equals("")) {
                    replyContent.setVisibility(View.VISIBLE);
                    replyContent.setText("@" + moment.relayCircle.user.name);
                } else {
                    replyContent.setVisibility(View.VISIBLE);
                    replyContent.setText("@" + moment.relayCircle.user.name + ":" + moment.relayCircle.desc);
                }
                if (moment.relayCircle.images != null && moment.relayCircle.images.size() > 0) {
                    imgForCircle.setVisibility(View.VISIBLE);
                    imgForCircle.setLstCircleImage(moment.relayCircle.images);
//                    ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(moment.relayCircle.images.get(0).Thumbnails, imgForCircle);
                } else {
                    imgForCircle.setVisibility(View.GONE);
                }
            }
        } else if (ll_not_circle.getVisibility() == View.VISIBLE) {
            //活动类型或新闻类型或其他类型
            if (moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal() || moment.relayCircle.type == SupportTypeEnum.News.getVal() || moment.relayCircle.type == SupportTypeEnum.CircleActivity.getVal()) {
                ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(moment.relayCircle.activityImg, imgForReply);
                tvTitle.setText(moment.relayCircle.title.trim());
                tvInfo.setText(moment.relayCircle.desc.trim());
            }
        }
    }

    private void initHeaderViewEvent(final CCircleDetailModel moment) {
        //头像点击事件
        mHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.startNameCardActivity(getActivity(), moment.user.userId);
            }
        });

        imgForCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moment.images != null && moment.images.size() > 0) {
                    Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
                    intent.putStringArrayListExtra(ImagePreviewActivity.KEY_IMAGE_URL_ARRAY, moment.getImagesUrl());
                    intent.putExtra(ImagePreviewActivity.KEY_START_INDEX, 0);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_zoomin, 0);
                } else if (moment.relayCircle != null && moment.relayCircle.images != null && moment.relayCircle.images.size() > 0) {
                    Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
                    intent.putStringArrayListExtra(ImagePreviewActivity.KEY_IMAGE_URL_ARRAY, moment.relayCircle.getImagesUrl());
                    intent.putExtra(ImagePreviewActivity.KEY_START_INDEX, 0);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_zoomin, 0);
                }
            }
        });

        ll_not_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moment.relayCircle.type == SupportTypeEnum.CircleActivity.getVal()
                        || moment.relayCircle.type == SupportTypeEnum.ActivityStudent.getVal()) {
                    //进入活动详情页
                    Intent it = new Intent(getActivity(), ActDetailActivity.class);
                    startActivity(it);
                } else if (moment.relayCircle.type == SupportTypeEnum.News.getVal()) {
                    //进入新闻详情页
                    navigatToNewsInfo(moment);
                }
            }
        });

        imgAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final String key = "attention" + moment.user.userId;
//                String isAttentionStr = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey(key);
//                if (!isAttentionStr.isEmpty() && isAttentionStr.equals("已关注")) {
//                    imgAttention.setImageDrawable(getResources().getDrawable(R.drawable.bg_btn_attention));
//                    isAttention = true;
//                } else {
//                    imgAttention.setImageDrawable(getResources().getDrawable(R.drawable.bg_btn_unattention));
//                    isAttention = false;
//                }
                if (!isAttention) {//关注
                    isAttention = true;
                    popupAttentionWindow = new PopupAttentionWindow(getActivity(),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((MainActivity) getActivity()).removeAllAttachedView();
                                    isAttention = false;
                                }
                            }, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //本地保存关注状态
//                            SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update(key, "已关注");
//                            Toast.makeText(getActivity(), "关注成功", Toast.LENGTH_SHORT).show();

                            //后台更新数据库
                            final int groupId = popupAttentionWindow.getGroupId(i);
                            CircleOperator.getInstance().attention(moment.user.userId, groupId, new ApiOperationCallback<ReturnInfo<String>>() {
                                @Override
                                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                    if (result != null && result.isSuccess()) {
                                        //切换至已关注状态
                                        imgAttention.setImageDrawable(getResources().getDrawable(R.drawable.bg_btn_attention));

                                        //保存关注的状态
//                                        SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update(key, "已关注");
                                        Toast.makeText(getActivity(), "关注成功", Toast.LENGTH_SHORT).show();
                                        isAttention = true;
                                    } else if (result != null && !result.isSuccess()) {
                                        Log.e("attention", result.getMessage());
                                        isAttention = true;
                                    } else if (result == null) {
                                        isAttention = true;
                                        Log.e("attention", exception.toString());
                                    }
                                }
                            });
                        }
                    });
                    ((MainActivity) getActivity()).addAttachView(popupAttentionWindow);
                } else {//取消关注
                    isAttention = false;
                    //本地保存关注状态
//                    SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update(key, "未关注");
//                    Toast.makeText(getActivity(), "取消关注成功", Toast.LENGTH_SHORT).show();

                    //后台更新数据
                    CircleOperator.getInstance().cancelAttention(moment.user.userId, new ApiOperationCallback<ReturnInfo<String>>() {
                        @Override
                        public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                            if (result != null && result.isSuccess()) {
                                isAttention = false;
                                //切换至已关注状态
                                imgAttention.setImageDrawable(getResources().getDrawable(R.drawable.bg_btn_unattention));

                                //保存关注的状态
//                                SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update(key, "未关注");
                                Toast.makeText(getActivity(), "取消关注成功", Toast.LENGTH_SHORT).show();
                                imgAttention.setClickable(true);
                            } else if (result != null && !result.isSuccess()) {
                                //isAttention = false;
                                Log.e("attention", result.getMessage());
                            } else if (result == null) {
                                //isAttention = false;
                                Log.e("attention", exception.toString());
                            }
                        }
                    });
                }
            }
        });
    }

    //进入新闻详情
    private void navigatToNewsInfo(CCircleDetailModel moment) {
        ItemInfoFragment itemInfoFragment = new ItemInfoFragment();
        Bundle args = new Bundle();
        args.putInt("newsId", moment.relayCircle.id);
        args.putString(ItemInfoFragment.KEY_SHARE_IMG_URL, moment.relayCircle.activityImg);
        itemInfoFragment.setArguments(args);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_container, itemInfoFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void commentCountAddOne(CCircleCommentModel circleCommentModel) {
        if (GlobalData.moment != null) {
            GlobalData.moment.comentCount += 1;
            momentActionBar.setData(replyList != null ? replyList.size() : 0, GlobalData.moment.comentCount, likeList != null ? likeList.size() : 0);
            if (commentList == null) {
                commentList = new ArrayList<CCircleCommentModel>();
            }
            commentList.add(0, circleCommentModel);
            circleMomentCommentAdapter = new CircleMomentDetailCommentAdapter(commentList);
            listView.setAdapter(circleMomentCommentAdapter);
            circleMomentCommentAdapter.notifyDataSetChanged();
            listView.setSelection(1);
        }
    }

    private void writeComment(boolean isReplay, int replyUserId, String userName) {
        if (!MainApplication.isLogin()) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        MomentCommentFragment momentCommentFragment = new MomentCommentFragment();

        //传递参数
        Bundle bundle = new Bundle();
        bundle.putInt(MomentCommentFragment.KEY_CIRCLE_ID, momentId);
        if (isReplay) {
            bundle.putInt(MomentCommentFragment.KEY_RELAYUSER_ID, replyUserId);
            bundle.putString(MomentCommentFragment.KEY_REPLY_NAME, userName);
        }
        bundle.putBoolean(MomentCommentFragment.KEY_ISREPLY, isReplay);
        momentCommentFragment.setArguments(bundle);

        momentCommentFragment.setOnCommentSuccessListner(new OnCommentSuccessListner() {
            @Override
            public void onSuccess(CCircleCommentModel circleCommentModel) {
                commentCountAddOne(circleCommentModel);
            }
        });
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
        ft.add(R.id.content_container, momentCommentFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    //初始化事件
    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//返回
                fm.popBackStack();
            }
        });

        this.rl_writeCommont.setOnClickListener(new View.OnClickListener() {//写评论
            @Override
            public void onClick(View view) {//写评论
                writeComment(false, 0, "");
            }
        });

        this.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnLike.getTag() != null && (boolean) btnLike.getTag()) {
                    Toast.makeText(MainApplication.UIContext, "您已赞！", Toast.LENGTH_LONG).show();
                    return;
                }

                String url = String.format("v1.1/Comment/Support?userid=%s&id=%s&type=7&result=1&session=%s", MainApplication.userInfo.getUserId(), moment.id, MainApplication.userInfo.getCookie());

                ApiDataProvider.getmClient().invokeApi(url, null,
                        HttpGet.METHOD_NAME, null, CirclePushCommentResult.class,
                        new ApiOperationCallback<CirclePushCommentResult>() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onCompleted(CirclePushCommentResult result, Exception exception, ServiceFilterResponse response) {
                                if (result == null || !result.isSuccess()) {
                                    return;
                                }

//                                btnLike.setClickable(false);
                                btnLike.setTag(true);
                                btnLike.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_support));
                                Toast.makeText(MainApplication.UIContext, "点赞成功！", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        this.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePopupWindow sharePopupWindow = new SharePopupWindow(getActivity(), rootView);
                String title = "xxx";
                String content = "xxx";
                if (moment.relayCircle == null) {
                    content = moment.content.isEmpty() ? "分享图片" : moment.content;
                } else if (moment.relayCircle != null) {
                    content = moment.relayCircle.desc.isEmpty() ? "分享图片" : moment.relayCircle.desc;
                }

                String shareUrl = moment.getShareUrl() + moment.id;
                String imgUrl = moment.relayImgUrl;

                sharePopupWindow.setShareInfo(title, content, shareUrl, imgUrl, moment);
//                SharePopupWindow sharePopupWindow = new SharePopupWindow(getActivity(), rootView);
//
//                String title = "分享 " + moment.user.name + " 的微博";
//                String content = moment.content.isEmpty() ? "分享图片" : moment.content;
//                String shareUrl = moment.getShareUrl() + moment.id;
//                String imgUrl = moment.relayImgUrl;
//                int type = 0;
//                if (moment.relayCircle == null) {
//                    type = SupportTypeEnum.Circle.getVal();
//                } else {
//                    type = moment.relayCircle.type;
//                }
//
//                sharePopupWindow.setShareInfo(title, content, shareUrl, imgUrl, type, moment.id);
            }
        });
    }

    public void getData(int type, final int lastId) {
        String url = String.format("v1.1/Circle/CircleTabList?type=%s&id=%s&lastId=%s&session=%s", type, moment.id, lastId, MainApplication.getSession());
        Log.i("detail_moment", url);
        ApiDataProvider.getmClient().invokeApi(url, null,
                HttpGet.METHOD_NAME, null, CircleMomentDetail.class,
                new ApiOperationCallback<CircleMomentDetail>() {
                    @Override
                    public void onCompleted(CircleMomentDetail result, Exception exception, ServiceFilterResponse response) {

                        if (result == null || !result.isSuccess()) {
                            return;
                        }

                        CCircleTabModel data = result.data;
                        commentList = data.commentList;
                        likeList = data.supportList;
                        replyList = data.relayList;
                        actionBar.showTab(1);
                        momentActionBar.showTab(1);
                        momentActionBar.setData(replyList != null ? replyList.size() : 0, commentList != null ? commentList.size() : 0, likeList != null ? likeList.size() : 0);
                        //用于更新圈子列表
                        GlobalData.moment.relayCount = replyList != null ? replyList.size() : 0;
                        GlobalData.moment.comentCount = commentList != null ? commentList.size() : 0;
                        GlobalData.moment.supportCount = likeList != null ? likeList.size() : 0;

                        if (commentList != null) {
                            for (int i = commentList.size() - 1; i >= 0; i--) {
                                if (!commentList.get(i).isValid()) {
                                    commentList.remove(i);
                                }
                            }

                            circleMomentCommentAdapter = new CircleMomentDetailCommentAdapter(commentList);
                            listView.setAdapter(circleMomentCommentAdapter);
                            circleMomentCommentAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    class CircleMomentDetailCommentAdapter extends BaseAdapter {
        private List<CCircleCommentModel> commentList = new ArrayList<CCircleCommentModel>();
        private boolean isSupport;

        public CircleMomentDetailCommentAdapter(List<CCircleCommentModel> momentList) {
            this.commentList = momentList;
        }

        @Override
        public int getCount() {
            return this.commentList == null ? 0 : this.commentList.size();
        }

        @Override
        public CCircleCommentModel getItem(int i) {
            return commentList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int p, View convertView, ViewGroup view) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_detail_comment_list_item, null);

                viewHolder.avatar = (ImageView) convertView.findViewById(R.id.comment_list_item_avatar);
                viewHolder.name = (TextView) convertView.findViewById(R.id.comment_list_item_username);
                viewHolder.grade = (TextView) convertView.findViewById(R.id.comment_list_item_grade);
                viewHolder.time = (TextView) convertView.findViewById(R.id.comment_list_item_time);
                viewHolder.content = (EmotionTextView) convertView.findViewById(R.id.comment_list_item_content);
                viewHolder.content.setTextIsSelectable(true);
                viewHolder.support = (ImageView) convertView.findViewById(R.id.id_img_support);
                viewHolder.likeCount = (TextView) convertView.findViewById(R.id.comment_list_item_like_count);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //判断点击当前的评论是否为自己的评论
            boolean isMySelf = false;
            if (commentList.get(p).user.userId == MainApplication.userInfo.getUserId()) {
                isMySelf = true;
            } else {
                isMySelf = false;
            }

            //弹出选项：复制、删除
            final boolean finalIsMySelf = isMySelf;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialogUtils.showMenuDialog(getActivity(), finalIsMySelf, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0://回复
                                    writeComment(true, commentList.get(p).user.userId, commentList.get(p).user.getUserName());
                                    break;
                                case 1://复制
                                    ClipboardManager clip = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                    clip.setText(commentList.get(p).content);
                                    Toast.makeText(getActivity(), "已复制到剪切板", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2://删除
                                    CCircleCommentModel comment = commentList.get(p);
                                    commentList.remove(comment);
                                    notifyDataSetChanged();
                                    CircleOperator.getInstance().deleteComment(comment.circleId, comment.id, new ApiOperationCallback<ReturnInfo<String>>() {
                                        @Override
                                        public void onCompleted(ReturnInfo<String> result, Exception e, ServiceFilterResponse serviceFilterResponse) {
                                            if (result != null && result.isSuccess()) {
                                                Log.i("deleteComment_result", new Gson().toJson(result));
                                            }
                                        }
                                    });
                                    break;
                            }
                        }
                    });
//                    Toast.makeText(getActivity(), "评论回复" + commentList.get(i).content, Toast.LENGTH_SHORT).show();
                }
            });

            final CCircleCommentModel comment = commentList.get(p);
            viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityUtils.startNameCardActivity(getActivity(), comment.user.userId);
                }
            });
            ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(comment.user.avatar, viewHolder.avatar, ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.user_portrait_height)));
            viewHolder.name.setText(comment.user.name);
            viewHolder.grade.setText(comment.user.grade);
            viewHolder.time.setText(Utils.getDisplayTime(comment.createTime));
            if (comment.isRelyComment == 1) {
                viewHolder.content.setText("回复 @" + comment.relyUser + ":" + comment.content);
            }
            viewHolder.content.setText(comment.content);
            viewHolder.likeCount.setText(comment.supportCount > 0 ? "" + comment.supportCount : "");

            if (comment.userMark != null && comment.userMark.isSupport()) {
                viewHolder.likeCount.setTextColor(getResources().getColor(R.color.red));
                viewHolder.support.setImageDrawable(getResources().getDrawable(R.drawable.icon_support));
            } else {
                viewHolder.support.setImageDrawable(getResources().getDrawable(R.drawable.icon_unsupport));
            }

            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.support.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSupport) {
                        Toast.makeText(MainApplication.UIContext, "您已赞", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String isSupportStr = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey("circle_comment_" + comment.id + "_" + MainApplication.userInfo.getUserId());
                    if (isSupportStr.equals("已赞")) {
                        Toast.makeText(MainApplication.UIContext, "您已赞", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    finalViewHolder.support.setImageDrawable(getResources().getDrawable(R.drawable.icon_support));
                    finalViewHolder.likeCount.setTextColor(getResources().getColor(R.color.red));
                    finalViewHolder.likeCount.setText(comment.supportCount + 1 + "");
                    CommonOperator.getInstance().supportOrStep(MainApplication.userInfo.getUserId(), comment.id, SupportTypeEnum.CircleComment, 1, new ApiOperationCallback<ReturnInfo<String>>() {
                        @Override
                        public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                            if (result != null && result.isSuccess()) {
                                SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update("circle_comment_" + comment.id + "_" + MainApplication.userInfo.getUserId(), "已赞");
                                Toast.makeText(MainApplication.UIContext, "点赞成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView avatar, support;
            TextView name;
            TextView grade;
            TextView time;
            EmotionTextView content;
            TextView likeCount;
        }
    }

    class CircleMomentDetailLikeAdapter extends BaseAdapter {
        private List<CUserModel> likeList = new ArrayList<CUserModel>();
        private ViewHolder holder;
        private float density = MainApplication.Resources.getDisplayMetrics().density;
        private float screenWidth = MainApplication.Resources.getDisplayMetrics().widthPixels;
        private int columnCount = 4;

        public CircleMomentDetailLikeAdapter(List<CUserModel> momentList) {
            this.likeList = momentList;
        }

        @Override
        public int getCount() {
            return this.likeList == null ? 0 : (int) Math.ceil((float) this.likeList.size() / columnCount);
        }

        @Override
        public CUserModel getItem(int i) {
            return likeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup view) {
            ViewHolder viewHolder = new ViewHolder();
            columnCount = (int) (screenWidth / (density * 90));
            Log.i("columnCount", "" + columnCount);
            if (convertView == null) {
                convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_detail_like_list_item, null);

                LinearLayout container = (LinearLayout) convertView.findViewById(R.id.container);

                viewHolder.container = container;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.container.removeAllViews();
            for (int j = columnCount * i; j < columnCount * (i + 1) && j < likeList.size(); j++) {
                final CUserModel user = likeList.get(j);
                LinearLayout childContainer = new LinearLayout(MainApplication.UIContext, null);
                childContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                childContainer.setOrientation(LinearLayout.VERTICAL);

                ImageView image = new ImageView(MainApplication.UIContext);
                float density = MainApplication.Resources.getDisplayMetrics().density;
                LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams((int) (density * 40), (int) (density * 40));
                int padding = (int) ((MainApplication.Resources.getDisplayMetrics().widthPixels - density * 40 * columnCount) / (columnCount * 2));
                padding = Math.min(padding, 20);
                imgParams.setMargins(padding, 0, padding, 0);
                image.setLayoutParams(imgParams);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(user.avatar, image, ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.user_portrait_height)));


                LinearLayout.LayoutParams tvParam = new LinearLayout.LayoutParams((int) (density * 40), LinearLayout.LayoutParams.WRAP_CONTENT);
                tvParam.setMargins(padding, 0, padding, 0);
                TextView textView = new TextView(MainApplication.UIContext);
                textView.setText(user.name);
                textView.setTextSize(10);
                textView.setSingleLine();
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextColor(getResources().getColor(R.color.common_color_424242));
                textView.setLayoutParams(tvParam);

                childContainer.addView(image);
                childContainer.addView(textView);

                viewHolder.container.addView(childContainer);

                childContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityUtils.startNameCardActivity(getActivity(), (int) (user.userId));
                    }
                });
            }

            return convertView;
        }

        private class ViewHolder {
            LinearLayout container;
            TextView tvUserName;
        }
    }

    class CircleMomentDetailReplyAdpater extends BaseAdapter {

        private List<CCircleReplyModel> replyList = new ArrayList<CCircleReplyModel>();

        CircleMomentDetailReplyAdpater(List<CCircleReplyModel> replyList) {
            this.replyList = replyList;
        }

        @Override
        public int getCount() {
            return replyList.size();
        }

        @Override
        public CCircleReplyModel getItem(int i) {
            return replyList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return replyList.get(i).id;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_detail_comment_list_item, null);

                viewHolder.avatar = (ImageView) convertView.findViewById(R.id.comment_list_item_avatar);
                viewHolder.name = (TextView) convertView.findViewById(R.id.comment_list_item_username);
                viewHolder.grade = (TextView) convertView.findViewById(R.id.comment_list_item_grade);
                viewHolder.time = (TextView) convertView.findViewById(R.id.comment_list_item_time);
                viewHolder.content = (EmotionTextView) convertView.findViewById(R.id.comment_list_item_content);
                viewHolder.likeCount = (TextView) convertView.findViewById(R.id.comment_list_item_like_count);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final CCircleReplyModel reply = replyList.get(i);
            viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityUtils.startNameCardActivity(getActivity(), (int) (moment.user.userId));
                }
            });
            ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(reply.user.avatar, viewHolder.avatar, ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.user_portrait_height)));
            viewHolder.name.setText(reply.user.name);
            viewHolder.grade.setText(reply.user.grade);
            viewHolder.time.setText(Utils.getDisplayTime(reply.createTime));
            viewHolder.content.setText(reply.content);
            viewHolder.likeCount.setText(reply.supportCount > 0 ? "" + reply.supportCount : "");
            return convertView;
        }

        private class ViewHolder {
            ImageView avatar;
            TextView name;
            TextView grade;
            TextView time;
            EmotionTextView content;
            TextView likeCount;
        }
    }
}
