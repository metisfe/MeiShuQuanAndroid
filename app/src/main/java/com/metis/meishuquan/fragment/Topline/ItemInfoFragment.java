package com.metis.meishuquan.fragment.Topline;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.login.LoginFragment;
import com.metis.meishuquan.fragment.main.ToplineFragment;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.TopLineNewsInfo;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.popup.SharePopupWindow;
import com.metis.meishuquan.view.topline.CommentInputView;
import com.metis.meishuquan.view.topline.NewsShareView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.StringTokenizer;

/**
 * 头条列表项详细信息界面
 * <p/>
 * Created by wj on 15/3/23.
 */
public class ItemInfoFragment extends Fragment {
    private int newsId = 0;
    private Button btnBack, btnShare;
    private ViewGroup rootView;
    private LinearLayout ll_content;
    private TopLineNewsInfo newsInfo;
    private TextView tv_title, tv_createtime, tv_sourse, tv_comment_count;
    private Button btn_writeCommont, btn_commontList, btn_private, btn_share;
    private ScrollView contentScrollView;

    private CommentInputView commentInputView;
    private NewsShareView newsShareView;
    private boolean addCommentPoped;
    private boolean addSharePoped;
    private FragmentManager fm;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载新闻详细
        Bundle args = this.getArguments();
        if (args != null) {
            newsId = args.getInt("newsId");
            //根据新闻Id获取新闻内容
            getInfoData(newsId);
        }

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_topline_topbar_list_item_info, null, false);

        initView(rootView);
        initEvent();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //初始化视图
    private void initView(ViewGroup rootView) {
        btnBack = (Button) rootView.findViewById(R.id.id_btn_back);
        tv_title = (TextView) rootView.findViewById(R.id.id_title);
        tv_createtime = (TextView) rootView.findViewById(R.id.id_createtime);
        tv_sourse = (TextView) rootView.findViewById(R.id.id_tv_source);
        tv_comment_count = (TextView) rootView.findViewById(R.id.id_tv_topline_info_comment_count);//评论数
        btn_writeCommont = (Button) rootView.findViewById(R.id.id_btn_writecomment);
        btn_commontList = (Button) rootView.findViewById(R.id.id_btn_commentlist);
        btn_private = (Button) rootView.findViewById(R.id.id_btn_private);
        btn_share = (Button) rootView.findViewById(R.id.id_btn_share);
        contentScrollView = (ScrollView) rootView.findViewById(R.id.id_scrollview_info_content);

        commentInputView = new CommentInputView(getActivity(), null, 0);
        newsShareView = new NewsShareView(getActivity(), null, 0);

        fm = getActivity().getSupportFragmentManager();
    }

    private void addViewByContent() {
        if (newsInfo != null) {
            String content = newsInfo.getData().getContent();
            //解析内容
            int i = 0;
            StringTokenizer tokenizer = new StringTokenizer(content, "|$|");

            String[] str = new String[tokenizer.countTokens()];

            while (tokenizer.hasMoreTokens()) {
                str[i] = new String();
                str[i] = tokenizer.nextToken();
                i++;
            }

            for (int j = 0; j < str.length; j++) {
                if (str[j].contains("<p>") || str[j].contains("</p>")) {
                    String wordTemp = str[j].replace("<p>", "");
                    String word = wordTemp.replace("</p>", "\n");
                    addTextView(word);
                }
                if (str[j].contains("<!--img")) {
                    String name = str[j];
                    addImageView(name);
                }
            }
        }
    }

    //添加视图控件
    private void addImageView(String name) {
        String url = "";
        int width = 0;
        int height = 0;

        if (ll_content == null) {
            ll_content = (LinearLayout) rootView.findViewById(R.id.id_ll_news_content);//内容父布局
        }

        SmartImageView imageView = new SmartImageView(MainApplication.UIContext);
        for (int i = 0; i < newsInfo.getData().getUrlss().size(); i++) {
            if (newsInfo.getData().getUrlss().get(i).getNewShowContent().equals(name)) {
                url = newsInfo.getData().getUrlss().get(i).getDir();
            }
        }

        imageView.setImageUrl(url.trim());
        imageView.setScaleType(ImageView.ScaleType.MATRIX);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(lp);

        ll_content.addView(imageView);
    }

    //添加文本控件
    private void addTextView(String words) {
        if (ll_content == null) {
            ll_content = (LinearLayout) rootView.findViewById(R.id.id_ll_news_content);//内容父布局
        }

        TextView textView = new TextView(MainApplication.UIContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        textView.setLayoutParams(lp);
        textView.setText(words);
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(R.color.tv_channel_item));

        ll_content.addView(textView);
    }

    //初始化事件
    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//返回
                ToplineFragment toplineFragment = new ToplineFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content_container, toplineFragment);
                ft.commit();
            }
        });

        this.contentScrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐藏写评论
                if (!addCommentPoped) {
                    return;
                }
                addCommentPoped = false;
                showOrHideCommentInputView(false);
                Utils.hideInputMethod(getActivity(), commentInputView.editText);
            }
        });

        this.contentScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) commentInputView.editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    if (isOpen) {
                        //隐藏写评论
                        if (!addCommentPoped) {
                            return false;
                        }
                        addCommentPoped = false;
                        showOrHideCommentInputView(false);
                        Utils.hideInputMethod(getActivity(), commentInputView.editText);
                    }
                }
                return false;
            }
        });

        this.btn_writeCommont.setOnClickListener(new View.OnClickListener() {//写评论
            @Override
            public void onClick(View view) {//写评论
                SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                String loginState = spu.getStringByKey(SharedPreferencesUtil.LOGIN_STATE);
                if (loginState != null && loginState.equals("已登录")) {
                    showOrHideCommentInputView(true);
                    Utils.showInputMethod(getActivity(), commentInputView.editText);
                } else {
                    LoginFragment loginFragment = new LoginFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(R.id.content_container, loginFragment);
                    ft.commit();
                }
            }
        });

        this.commentInputView.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//写评论-发送
                String content = commentInputView.editText.getText().toString().trim();
                if (content.length() > 0) {
                    TopLineOperator topLineOperator = TopLineOperator.getInstance();
                    topLineOperator.publishComment(0, newsId, content, 0, 0, new ApiOperationCallback<ReturnInfo<String>>() {
                        @Override
                        public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                            if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    showOrHideCommentInputView(false);
                    Utils.hideInputMethod(getActivity(), commentInputView.editText);
                } else {
                    Toast.makeText(getActivity(), "请输入内容", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.btn_commontList.setOnClickListener(new View.OnClickListener() {//评论列表
            @Override
            public void onClick(View view) {//查看评论列表
                Bundle args = new Bundle();
                if (newsId != 0) {
                    args.putInt("newsId", newsId);
                    args.putInt("totalCommentCount", newsInfo.getData().getCommentCount());
                }
                //跳转至评论列表
                CommentListFragment commentListFragment = new CommentListFragment();
                commentListFragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.content_container, commentListFragment);
                fragmentTransaction.commit();
            }
        });

        this.btn_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//收藏
                Utils.alertMessageDialog("提示", "收藏成功！");//TODO:
            }
        });

        this.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SharePopupWindow(MainApplication.UIContext, rootView);
            }
        });

//        this.btn_share.setOnClickListener(new View.OnClickListener() {//分享
//            @Override
//            public void onClick(View view) {
//                if (addSharePoped) {
//                    return;
//                }
//                addSharePoped = true;
//                showOrHideNewsShareView(true);
//            }
//        });
//
//        this.newsShareView.btnFriends.setOnClickListener(new View.OnClickListener() {//分享至朋友圈
//            @Override
//            public void onClick(View view) {
//                if (!addSharePoped) {
//                    return;
//                }
//                addSharePoped = false;
//                Toast.makeText(getActivity(), "分享至微信朋友圈", Toast.LENGTH_SHORT).show();
//                showOrHideNewsShareView(false);
//            }
//        });
//
//        this.newsShareView.btnWeixin.setOnClickListener(new View.OnClickListener() {//分享至好友
//            @Override
//            public void onClick(View view) {
//                if (!addSharePoped) {
//                    return;
//                }
//                addSharePoped = false;
//                Toast.makeText(getActivity(), "分享至微信", Toast.LENGTH_SHORT).show();
//                showOrHideNewsShareView(false);
//            }
//        });
//
//        this.newsShareView.btnCancel.setOnClickListener(new View.OnClickListener() {//分享-取消
//            @Override
//            public void onClick(View view) {
//                if (!addSharePoped) {
//                    return;
//                }
//                addSharePoped = false;
//                showOrHideNewsShareView(false);
//            }
//        });
    }

    //显示或隐藏评论视图
    private void showOrHideCommentInputView(boolean isShow) {
        ViewGroup parent = (ViewGroup) getActivity().findViewById(R.id.ll_parent);
        TranslateAnimation translateAnimation = null;
        int yStart = -getActivity().getResources().getDisplayMetrics().heightPixels;
        int yEnd = 0;
        if (isShow) {
            if (commentInputView != null) {
                parent.addView(commentInputView);
            }
            translateAnimation = new TranslateAnimation(0, 0, yStart, yEnd);
        } else {
            if (commentInputView != null) {
                parent.removeView(commentInputView);
            }
            translateAnimation = new TranslateAnimation(0, 0, yEnd, yStart);
        }
        translateAnimation.setFillBefore(true);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setDuration(0);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        commentInputView.startAnimation(translateAnimation);
    }

    //显示或隐藏分享视图
    private void showOrHideNewsShareView(boolean isShow) {
        ViewGroup parent = (ViewGroup) getActivity().findViewById(R.id.ll_parent);
        TranslateAnimation translateAnimation = null;
        int yStart = -getActivity().getResources().getDisplayMetrics().heightPixels;
        int yEnd = 0;
        if (isShow) {
            if (newsShareView != null) {
                parent.addView(newsShareView);
            }
            translateAnimation = new TranslateAnimation(0, 0, yStart, yEnd);
        } else {
            if (newsShareView != null) {
                parent.removeView(newsShareView);
            }
            translateAnimation = new TranslateAnimation(0, 0, yEnd, yStart);
        }
        translateAnimation.setFillAfter(true);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setDuration(100);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        newsShareView.startAnimation(translateAnimation);
    }

    //为控件绑定数据
    private void bindData() {
        if (newsInfo != null) {
            this.tv_title.setText(newsInfo.getData().getTitle());
            this.tv_createtime.setText(newsInfo.getData().getModifyTime());
            this.tv_sourse.setText(newsInfo.getData().getSource().getTitle());
            this.tv_comment_count.setText(String.valueOf(newsInfo.getData().getCommentCount()));
        }
    }

    //根据新闻Id获取新闻内容
    private void getInfoData(final int newsId) {
        TopLineOperator topLineOperator = TopLineOperator.getInstance();
        topLineOperator.getNewsInfoById(newsId, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result != null) {
                    if (result.getInfo().equals(String.valueOf(0))) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        Log.i("newsInfo", json);
                        if (!json.equals("")) {
                            newsInfo = gson.fromJson(json, new TypeToken<TopLineNewsInfo>() {
                            }.getType());
                        }
                    }
                    bindData();
                    addViewByContent();

                } else {
                    getInfoData(newsId);
                }
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
}
