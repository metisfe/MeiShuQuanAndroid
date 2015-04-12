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
import com.metis.meishuquan.model.circle.CircleMoment;
import com.metis.meishuquan.model.circle.CircleMomentComment;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.TopLineNewsInfo;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.circle.moment.MomentActionBar;
import com.metis.meishuquan.view.circle.moment.MomentPageListView;
import com.metis.meishuquan.view.popup.SharePopupWindow;
import com.metis.meishuquan.view.topline.CommentInputView;
import com.metis.meishuquan.view.topline.NewsShareView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

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

    private TextView tv_createtime, tv_nickname;
    private RelativeLayout rl_writeCommont;

    private FragmentManager fm;

    private List<CircleMomentComment> list = new ArrayList<CircleMomentComment>();
    private CircleMomentDetailCommentAdapter circleMomentCommentAdapter;
    private MomentActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Bundle args = this.getArguments();
//        if (args != null) {
//            newsId = args.getInt("newsId");
//            getInfoData(newsId);
//        }
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_moment_detail, null, false);

        initView(rootView);
        initEvent();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //初始化视图
    private void initView(final ViewGroup rootView) {
        btnBack = (Button) rootView.findViewById(R.id.btn_back);
        tv_nickname = (TextView) rootView.findViewById(R.id.moment_detail_tv_nickname);

        tv_createtime = (TextView) rootView.findViewById(R.id.id_createtime);

        rl_writeCommont = (RelativeLayout) rootView.findViewById(R.id.id_rl_writecomment);
        btnLike = (ImageView) rootView.findViewById(R.id.circle_moment_detail_footer_like);
        btnShare = (ImageView) rootView.findViewById(R.id.circle_moment_detail_footer_share);

        listView = (MomentPageListView) rootView.findViewById(R.id.moment_detail_listview);

        View view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_list_item, null);

        actionBar = (MomentActionBar) rootView.findViewById(R.id.moment_action_bar);

        listView.addHeaderView(view);
        listView.setOnSocialActionBarPositionChangedListener(new MomentPageListView.OnSocialActionBarPositionChangedListener() {
            @Override
            public void OnSocialActionBarPositionChanged(MomentPageListView.Position type) {
                if (type == MomentPageListView.Position.Top)
                {
                        actionBar.setVisibility(View.VISIBLE);
                }
                else
                {
                        actionBar.setVisibility(View.GONE);
                }
            }
        });
        circleMomentCommentAdapter = new CircleMomentDetailCommentAdapter(list);
        this.listView.setAdapter(circleMomentCommentAdapter);
        fm = getActivity().getSupportFragmentManager();
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
                SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                String loginState = spu.getStringByKey(SharedPreferencesUtil.LOGIN_STATE);
                if (loginState != null && loginState.equals("已登录")) {

                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        this.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//收藏
                Utils.alertMessageDialog("提示", "点赞成功！");//TODO:
            }
        });

        this.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SharePopupWindow(MainApplication.UIContext, rootView);
            }
        });
    }

    /**
     * 分享界面
     */
    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.choose_img_source_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            super.update();

            Button btnCamera = (Button) view.findViewById(R.id.item_popupwindows_camera);
            Button btnPhoto = (Button) view.findViewById(R.id.item_popupwindows_Photo);
            Button btnCancel = (Button) view.findViewById(R.id.item_popupwindows_cancel);

            btnCamera.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
            btnPhoto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    class CircleMomentDetailCommentAdapter extends BaseAdapter {
        private List<CircleMomentComment> commentList = new ArrayList<CircleMomentComment>();
        private ViewHolder holder;

        public CircleMomentDetailCommentAdapter(List<CircleMomentComment> momentList) {
            this.commentList = momentList;
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public CircleMomentComment getItem(int i) {
            return commentList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup view) {
            convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_circle_moment_detail_comment_list_item, null);
            return convertView;
        }

        private class ViewHolder {
            //TODO
        }
    }
}
