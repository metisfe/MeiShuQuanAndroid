package com.metis.meishuquan.fragment.Topline;

import android.content.Context;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.metis.meishuquan.fragment.main.ToplineFragment;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.Content;
import com.metis.meishuquan.model.topline.ContentInfo;
import com.metis.meishuquan.model.topline.TopLineNewsInfo;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.popup.SharePopupWindow;
import com.metis.meishuquan.view.topline.CommentInputView;
import com.metis.meishuquan.view.topline.NewsShareView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;
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
    private RelativeLayout rl_writeCommont, rl_commontList, rl_private, rl_share, rl_main;
    private RelativeLayout rl_Input;
    private ImageView imgFavorite;
    private ScrollView contentScrollView;
    private EditText editText;
    private RelativeLayout rlSend;

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
        rl_writeCommont = (RelativeLayout) rootView.findViewById(R.id.id_rl_writecomment);
        rl_commontList = (RelativeLayout) rootView.findViewById(R.id.id_rl_commentlist);
        rl_private = (RelativeLayout) rootView.findViewById(R.id.id_rl_private);
        rl_share = (RelativeLayout) rootView.findViewById(R.id.id_rl_share);
        rl_Input = (RelativeLayout) rootView.findViewById(R.id.id_rl_input);
        rl_main = (RelativeLayout) rootView.findViewById(R.id.ll_parent);
        rlSend = (RelativeLayout) rootView.findViewById(R.id.id_rl_send);
        contentScrollView = (ScrollView) rootView.findViewById(R.id.id_scrollview_info_content);

        editText = (EditText) rootView.findViewById(R.id.id_comment_edittext);
        imgFavorite = (ImageView) rootView.findViewById(R.id.id_img_favorite);

        fm = getActivity().getSupportFragmentManager();
    }

    private void addViewByContent() {
        if (newsInfo != null) {
            String json = newsInfo.getData().getContent();
            Log.i("content", json);
            Gson gson = new Gson();
            ContentInfo[] lstContentInfo = gson.fromJson(json, ContentInfo[].class);
            if (lstContentInfo == null) {
                return;
            }
            for (int i = 0; i < lstContentInfo.length; i++) {
                ContentInfo contentInfo = lstContentInfo[i];
                if (contentInfo.getType().equals("TXT")) {
                    if (contentInfo.getData().getContentType().equals("p")) {
                        addTextView("   " + contentInfo.getData().getContent());
                    } else if (contentInfo.getData().getContentType().equals("#document")) {
                        addTextView("   " + contentInfo.getData().getContent());
                    }
                }
                if (contentInfo.getType().equals("IMG")) {
                    addImageView(contentInfo.getData().getUrl(), contentInfo.getData().getWidth(), contentInfo.getData().getHeight());
                }
                if (contentInfo.getType().equals("LINK")) {
                    //addImageView(contentInfo.getData().getUrl(), contentInfo.getData().getWidth(), contentInfo.getData().getHeight());
                }
                if (contentInfo.getType().equals("VOIDE")) {
                    //addImageView(contentInfo.getData().getUrl(), contentInfo.getData().getWidth(), contentInfo.getData().getHeight());
                }

            }
        }
    }

    //添加视图控件
    private void addImageView(String url, int width, int height) {
        if (ll_content == null) {
            ll_content = (LinearLayout) rootView.findViewById(R.id.id_ll_news_content);//内容父布局
        }

        SmartImageView imageView = new SmartImageView(MainApplication.UIContext);
        imageView.setImageUrl(url.trim());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
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
                hideInputView();
                ToplineFragment toplineFragment = new ToplineFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content_container, toplineFragment);
                ft.commit();
            }
        });

        this.contentScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    hideInputView();

                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    hideInputView();
                }
                return false;
            }
        });

        this.rl_writeCommont.setOnClickListener(new View.OnClickListener() {//写评论
            @Override
            public void onClick(View view) {//写评论
                SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
                String loginState = spu.getStringByKey(SharedPreferencesUtil.LOGIN_STATE);
                if (loginState != null && loginState.equals("已登录")) {
                    showInputView();
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        this.rl_commontList.setOnClickListener(new View.OnClickListener() {//评论列表
            @Override
            public void onClick(View view) {//查看评论列表
                if (newsInfo == null) {
                    return;
                }
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

        this.rl_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//收藏
                TopLineOperator topLineOperator = TopLineOperator.getInstance();
                topLineOperator.newsPrivate(1, newsId, 0, new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Toast.makeText(MainApplication.UIContext, "收藏成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        this.rl_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SharePopupWindow(MainApplication.UIContext, rootView);
            }
        });

        rlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                if (!content.isEmpty()) {
                    TopLineOperator topLineOperator = TopLineOperator.getInstance();
                    topLineOperator.publishComment(0, newsId, content, 0, 0, new ApiOperationCallback<ReturnInfo<String>>() {
                        @Override
                        public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                            if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
                                hideInputView();
                            }
                        }
                    });
                }
            }
        });
    }

    private void hideInputView() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        rl_Input.setVisibility(View.GONE);
    }

    private void showInputView() {
        //显示输入框
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        rl_Input.setVisibility(View.VISIBLE);
        editText.setText("");
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    //为控件绑定数据
    private void bindData() {
        if (newsInfo != null) {
            this.tv_title.setText(newsInfo.getData().getTitle());
            this.tv_createtime.setText(newsInfo.getData().getModifyTime());
            String sourse = newsInfo.getData().getSource().getTitle().trim();
            this.tv_sourse.setText(sourse);

            //评论数
            int commentCount = newsInfo.getData().getCommentCount();
            if (commentCount > 0) {
                this.tv_comment_count.setVisibility(View.VISIBLE);
                this.tv_comment_count.setText(String.valueOf(commentCount));
            } else {
                this.tv_comment_count.setVisibility(View.GONE);
            }
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
}
