package com.metis.meishuquan.fragment.circle;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.adapter.circle.CircleMomentAdapter;
import com.metis.meishuquan.fragment.Topline.CommentListFragment;
import com.metis.meishuquan.fragment.main.CircleFragment;
import com.metis.meishuquan.fragment.main.ToplineFragment;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.circle.CCircleCommentModel;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CCircleTabModel;
import com.metis.meishuquan.model.circle.CircleMomentDetail;
import com.metis.meishuquan.model.circle.CircleMoments;
import com.metis.meishuquan.model.circle.CirclePushCommentResult;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.model.topline.TopLineNewsInfo;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.circle.moment.MomentActionBar;
import com.metis.meishuquan.view.circle.moment.MomentPageListView;
import com.metis.meishuquan.view.circle.moment.comment.EmotionTextView;
import com.metis.meishuquan.view.popup.SharePopupWindow;
import com.metis.meishuquan.view.shared.DragListView;
import com.metis.meishuquan.view.topline.CommentInputView;
import com.metis.meishuquan.view.topline.NewsShareView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 圈子列表项详细信息界面
 * <p/>
 * Created by jx on 15/4/11.
 */
public class MomentDetailFragment extends Fragment {
    private int newsId = 0;

    private MomentPageListView listView;
    private Button btnBack;
    private ImageView btnShare, btnLike;
    private ViewGroup rootView;

    private TextView tv_nickname;
    private RelativeLayout rl_writeCommont;

    private FragmentManager fm;

    private List<CCircleCommentModel> list = new ArrayList<CCircleCommentModel>();
    private CircleMomentDetailCommentAdapter circleMomentCommentAdapter;
    private MomentActionBar actionBar;
    private CCircleDetailModel moment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_moment_detail, null, false);
        if (GlobalData.moment != null) {
            this.moment = GlobalData.moment;
        }

        initView(rootView);
        getData(0, 0); // type = 0 : get comment and like

        circleMomentCommentAdapter = new CircleMomentDetailCommentAdapter(list);
        listView.setAdapter(circleMomentCommentAdapter);

        initEvent();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView(final ViewGroup rootView) {
        btnBack = (Button) rootView.findViewById(R.id.btn_back);
        tv_nickname = (TextView) rootView.findViewById(R.id.moment_detail_tv_nickname);

        rl_writeCommont = (RelativeLayout) rootView.findViewById(R.id.id_rl_writecomment);
        btnLike = (ImageView) rootView.findViewById(R.id.circle_moment_detail_footer_like);
        btnShare = (ImageView) rootView.findViewById(R.id.circle_moment_detail_footer_share);

        listView = (MomentPageListView) rootView.findViewById(R.id.moment_detail_listview);

        View headerView = getHeaderView(moment);

        actionBar = (MomentActionBar) rootView.findViewById(R.id.moment_action_bar);
        listView.addHeaderView(headerView);
        actionBar.setData(moment.relayCount, moment.comentCount, moment.supportCount);

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

    private View getHeaderView(CCircleDetailModel moment) {
        View headerView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_list_item, null);

        ((SmartImageView) headerView.findViewById(R.id.id_img_portrait)).setImageUrl(moment.user.avatar);
        ((TextView) headerView.findViewById(R.id.id_username)).setText(moment.user.name);
        ((TextView) headerView.findViewById(R.id.id_tv_grade)).setText(moment.user.grade);
        ((TextView) headerView.findViewById(R.id.id_createtime)).setText(moment.getTimeText());
        ((TextView) headerView.findViewById(R.id.id_tv_content)).setText(moment.content);
        ((TextView) headerView.findViewById(R.id.tv_device)).setText(moment.getDeviceText());
        if (moment.images.size() > 0) {
            ((SmartImageView) headerView.findViewById(R.id.id_img_content)).setImageUrl(moment.images.get(0).thumbnails);
        } else {
            ((SmartImageView) headerView.findViewById(R.id.id_img_content)).setVisibility(View.GONE);
        }

        ((MomentActionBar) headerView.findViewById(R.id.moment_action_bar)).setData(moment.relayCount, moment.comentCount, moment.supportCount);

        return headerView;
    }


    //初始化事件
    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//返回
                CircleFragment momentsFragment = new CircleFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content_container, momentsFragment);
                ft.commit();
            }
        });

        this.rl_writeCommont.setOnClickListener(new View.OnClickListener() {//写评论
            @Override
            public void onClick(View view) {//写评论
                if (!MainApplication.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                MomentCommentFragment momentCommentFragment = new MomentCommentFragment();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, momentCommentFragment);
                ft.addToBackStack(null);
                ft.commit();

//                if (loginState != null && loginState.equals("已登录")) {

//                } else {
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    getActivity().startActivity(intent);
//                }
            }
        });

        this.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = String.format("v1.1/Comment/Support?userid=%s&id=%s&type=7&result=1&session=%s", MainApplication.userInfo.getUserId(), moment.id, MainApplication.userInfo.getCookie());

                ApiDataProvider.getmClient().invokeApi(url, null,
                        HttpGet.METHOD_NAME, null, CirclePushCommentResult.class,
                        new ApiOperationCallback<CirclePushCommentResult>() {
                            @Override
                            public void onCompleted(CirclePushCommentResult result, Exception exception, ServiceFilterResponse response) {
                                if (result == null || !result.isSuccess()) {
                                    return;
                                }

                                btnLike.setClickable(false);
                                Toast.makeText(MainApplication.UIContext, "点赞成功！", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        this.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SharePopupWindow(MainApplication.UIContext, rootView);
            }
        });
    }

    public void getData(int type, final int lastId) {
        String url = String.format("v1.1/Circle/CircleTabList?type=%s&id=%s&lastId=%s&session=%s", type, moment.id, lastId, MainApplication.userInfo.getCookie());

        ApiDataProvider.getmClient().invokeApi(url, null,
                HttpGet.METHOD_NAME, null, CircleMomentDetail.class,
                new ApiOperationCallback<CircleMomentDetail>() {
                    @Override
                    public void onCompleted(CircleMomentDetail result, Exception exception, ServiceFilterResponse response) {

                        if (result == null || !result.isSuccess()) {
                            return;
                        }

                        CCircleTabModel data = result.data;
                        List<CCircleCommentModel> commentList = data.commentList;

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
        private ViewHolder holder;

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
        public View getView(final int i, View convertView, ViewGroup view) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_detail_comment_list_item, null);

                viewHolder.avatar = (SmartImageView) convertView.findViewById(R.id.comment_list_item_avatar);
                viewHolder.name = (TextView) convertView.findViewById(R.id.comment_list_item_username);
                viewHolder.grade = (TextView) convertView.findViewById(R.id.comment_list_item_grade);
                viewHolder.time = (TextView) convertView.findViewById(R.id.comment_list_item_time);
                viewHolder.content = (EmotionTextView) convertView.findViewById(R.id.comment_list_item_content);
                viewHolder.likeCount = (TextView) convertView.findViewById(R.id.comment_list_item_like_count);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            CCircleCommentModel comment = commentList.get(i);
            viewHolder.avatar.setImageUrl(comment.user.avatar);
            viewHolder.name.setText(comment.user.name);
            viewHolder.grade.setText(comment.user.grade);
            viewHolder.time.setText(Utils.getDisplayTime(comment.createTime));
            viewHolder.content.setText(comment.content);
            viewHolder.likeCount.setText(comment.supportCount > 0 ? "" + comment.supportCount : "");
            return convertView;
        }

        private class ViewHolder {
            SmartImageView avatar;
            TextView name;
            TextView grade;
            TextView time;
            EmotionTextView content;
            TextView likeCount;
        }
    }
}
