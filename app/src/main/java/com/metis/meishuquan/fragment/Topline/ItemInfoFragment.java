package com.metis.meishuquan.fragment.Topline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.jit.video.ControlVideoView;
import com.metis.meishuquan.MainActivity;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.TestActivity;
import com.metis.meishuquan.activity.info.ImagePreviewActivity;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.adapter.topline.CommonAdapter;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.BlockTypeEnum;
import com.metis.meishuquan.model.enums.PrivateResultEnum;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.model.topline.ContentInfo;
import com.metis.meishuquan.model.topline.RelatedRead;
import com.metis.meishuquan.model.topline.TopLineNewsInfo;
import com.metis.meishuquan.model.topline.Urls;
import com.metis.meishuquan.model.topline.UserMark;
import com.metis.meishuquan.util.ActivityUtils;
import com.metis.meishuquan.util.Helper;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.popup.SharePopupWindow;
import com.metis.meishuquan.view.topline.MeasureableListView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 头条列表项详细信息界面
 * <p/>
 * Created by wj on 15/3/23.
 */
public class ItemInfoFragment extends Fragment {

    private static final String CLASS_NAME = ItemInfoFragment.class.getSimpleName();
    public static final String KEY_NAVAGT = "navigate";
    public static final String KEY_IMAGE_URL_ARRAY = "image_url_array",
            KEY_THUMB_URL_ARRAY = "thumb_url_array";
    public static final String KEY_TITLE_VISIBLE = "title_visible";
    public static final String LOG_EXCEPTION = "exception";
    public static final String KEY_NEWSID = "newsId";
    public static final String KEY_SHARE_IMG_URL = "share_url";

    private final int LOGINREQUESTCODE = 1001;
    private String shareContent = "来自美术圈";

    private Button btnBack, btnShare;
    private ViewGroup rootView;
    private LinearLayout ll_content, ll_relatedReadAndSupportContainer;
    private TopLineNewsInfo newsInfo;
    private View titleBar = null;
    private TextView tv_title, tv_createtime, tv_sourse, tv_comment_count, tvSupportAddOne, tvStepAddOne, tvSupportCount, tvStepCount;
    private RelativeLayout rl_writeCommont, rl_commontList, rl_private, rl_share, rl_main, rlSupport, rlStep;
    private RelativeLayout rl_Input;
    private ImageView imgSupport, imgStep, imgPrivate;
    private ScrollView contentScrollView;
    private LinearLayout llRelatedRead;//相关阅读父容器
    private MeasureableListView lvRelatedRead;
    private EditText editText;
    private RelativeLayout rlSend;

    private ImageView mProfileIv;

    private CommonAdapter commonAdapter;

    private boolean isPrivate = false;
    private ArrayList<String> lstImgUrls = new ArrayList<String>();

    private int newsId = 0;
    private String shareImageUrl = "";
    private boolean titleVisible = true;
    private FragmentManager fm;
    private int flag = 0;//用于标识是否为NewDetailActivity调用

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        int animId = R.anim.right_out;
        if (enter) {
            animId = R.anim.right_in;
        }
        return AnimationUtils.loadAnimation(getActivity(), animId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_topline_topbar_list_item_info, null, false);
        initView(rootView);
        initEvent();

        //加载新闻详细
        Bundle args = this.getArguments();
        if (args != null) {
            newsId = args.getInt(KEY_NEWSID);
            flag = args.getInt(KEY_NAVAGT);
//            shareImageUrl = args.getString(KEY_SHARE_IMG_URL);
            //根据新闻Id获取新闻内容
            getInfoData(newsId);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(CLASS_NAME); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(CLASS_NAME);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleBar = view.findViewById(R.id.id_topbar);
        setTitleBarVisible(titleVisible);
    }

    public void setTitleBarVisible(boolean titleVisible) {
        this.titleVisible = titleVisible;
        if (titleBar != null) {
            titleBar.setVisibility(titleVisible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                //登录成功后，更新登录状态
                Toast.makeText(getActivity(), "已登录", Toast.LENGTH_SHORT).show();
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //初始化视图
    private void initView(ViewGroup rootView) {
        btnBack = (Button) rootView.findViewById(R.id.id_btn_back);
        tv_title = (TextView) rootView.findViewById(R.id.id_title);
        tv_createtime = (TextView) rootView.findViewById(R.id.id_tv_create_time);
        tv_sourse = (TextView) rootView.findViewById(R.id.id_tv_source);
        tv_comment_count = (TextView) rootView.findViewById(R.id.id_tv_topline_info_comment_count);//评论数
        rl_writeCommont = (RelativeLayout) rootView.findViewById(R.id.id_rl_writecomment);
        rl_commontList = (RelativeLayout) rootView.findViewById(R.id.id_rl_commentlist);
        rl_private = (RelativeLayout) rootView.findViewById(R.id.id_rl_private);//收藏
        rl_share = (RelativeLayout) rootView.findViewById(R.id.id_rl_share);//分享
        rl_Input = (RelativeLayout) rootView.findViewById(R.id.id_rl_input);//输入框
        rl_main = (RelativeLayout) rootView.findViewById(R.id.ll_parent);
        rlSend = (RelativeLayout) rootView.findViewById(R.id.id_rl_send);//发送评论
        contentScrollView = (ScrollView) rootView.findViewById(R.id.id_scrollview_info_content);
        mProfileIv = (ImageView) rootView.findViewById(R.id.id_img_dynamic);
        mProfileIv.setVisibility(View.GONE);

        rl_Input.setEnabled(false);
        rl_writeCommont.setEnabled(false);
        rl_private.setEnabled(false);
        rl_share.setEnabled(false);
        rlSend.setEnabled(false);

        ll_relatedReadAndSupportContainer = (LinearLayout) rootView.findViewById(R.id.id_ll_related_read_and_support_container);
        llRelatedRead = (LinearLayout) rootView.findViewById(R.id.id_ll_related_read);//相关阅读父容器
        lvRelatedRead = (MeasureableListView) rootView.findViewById(R.id.id_lv_relation_read);//相关阅读列表

        rlSupport = (RelativeLayout) rootView.findViewById(R.id.id_rl_support);//赞
        tvSupportAddOne = (TextView) rootView.findViewById(R.id.id_tv_support_add_one);
        tvStepAddOne = (TextView) rootView.findViewById(R.id.id_tv_step_add_one);
        tvSupportCount = (TextView) rootView.findViewById(R.id.id_tv_support_count);
        tvStepCount = (TextView) rootView.findViewById(R.id.id_tv_step_count);

        rlStep = (RelativeLayout) rootView.findViewById(R.id.id_rl_step);//踩

        editText = (EditText) rootView.findViewById(R.id.id_comment_edittext);
        imgSupport = (ImageView) rootView.findViewById(R.id.id_img_support);
        imgStep = (ImageView) rootView.findViewById(R.id.id_img_step);
        imgPrivate = (ImageView) rootView.findViewById(R.id.id_img_favorite);//收藏图标

        fm = getActivity().getSupportFragmentManager();
    }

    private void addViewByContent() {
        if (newsInfo != null && newsInfo.getData() != null) {
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
                    if (contentInfo.getData().getContentType() != null && contentInfo.getData().getContentType().equals("p")) {
                        addTextView(contentInfo.getData().getContent());
                    } else {
                        addTextView(contentInfo.getData().getContent());
                    }
                }
                if (contentInfo.getType() != null && contentInfo.getType().equals("IMG")) {
                    lstImgUrls.add(contentInfo.getData().getUrl());
                    addImageView(contentInfo.getData().getUrl(), contentInfo.getData().getWidth(), contentInfo.getData().getHeight());
                }
                if (contentInfo.getType() != null && contentInfo.getType().equals("VOIDE")) {
                    //addImageView(contentInfo.getData().getUrl(), contentInfo.getData().getWidth(), contentInfo.getData().getHeight());
                    addVideoView(contentInfo.getData().getUrl(), contentInfo.getData().getWidth(), contentInfo.getData().getHeight());
                }
            }

            //相关阅读
            if (newsInfo.getData().getRelatedNewsList().size() > 0) {
                ll_relatedReadAndSupportContainer.setVisibility(View.VISIBLE);
                //绑定相关阅读数据
                List<RelatedRead> lstRelatedRead = newsInfo.getData().getRelatedNewsList();
                commonAdapter = new CommonAdapter(MainApplication.UIContext, lstRelatedRead);
                lvRelatedRead.setAdapter(commonAdapter);
                setListViewHeightBasedOnChildren(lvRelatedRead);
            } else {
                ll_relatedReadAndSupportContainer.setVisibility(View.VISIBLE);
                llRelatedRead.setVisibility(View.GONE);
            }
        }
    }

    //添加视图控件
    private void addImageView(final String url, int width, int height) {
        if (!isResumed()) {
            return;
        }
        if (ll_content == null) {
            ll_content = (LinearLayout) rootView.findViewById(R.id.id_ll_news_content);//内容父布局
        }
        ImageView imageView = new ImageView(getActivity(), null);

        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(url.trim(), imageView);

//        ImageUtil.setImageViewMathParent(getActivity(), ll_content, imageView, width, height);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        lp.width = MainApplication.Resources.getDisplayMetrics().widthPixels;
        lp.height = (MainApplication.Resources.getDisplayMetrics().widthPixels * height) / width;
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(lp);
        ll_content.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
                intent.putStringArrayListExtra(ImagePreviewActivity.KEY_IMAGE_URL_ARRAY, lstImgUrls);
                intent.putExtra(ImagePreviewActivity.KEY_START_INDEX, lstImgUrls.indexOf(url));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_zoomin, 0);
            }
        });
    }

    //添加文本控件
    private void addTextView(String words) {
        if (!isResumed()) {
            return;
        }
        if (ll_content == null) {
            ll_content = (LinearLayout) rootView.findViewById(R.id.id_ll_news_content);//内容父布局
        }
        TextView textView = new TextView(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 10, 20, 10);
        textView.setLayoutParams(lp);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        textView.setLineSpacing(1.2f, 1.2f);
        textView.setTextColor(getResources().getColor(R.color.tv_channel_item));

        int k = 0;
        StringTokenizer tokenizer = new StringTokenizer(words, "$");

        String[] str = new String[tokenizer.countTokens()];

        while (tokenizer.hasMoreTokens()) {
            str[k] = new String();
            str[k] = tokenizer.nextToken().trim();
            k++;
        }

        for (int i = 0; i < str.length; i++) {
            Log.i(String.valueOf(i), str[i]);
        }

        //将link占位符替换成链接文字
        for (int j = 0; j < str.length; j++) {
            for (int i = 0; i < newsInfo.getData().getUrlss().size(); i++) {
                Urls url = newsInfo.getData().getUrlss().get(i);
                String newShowContent = url.getNewShowContent().trim();
                if (str[j].equals(newShowContent)) {
                    str[j] = url.getDescription();
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length; i++) {
            sb.append(str[i]);
        }

        //得到最终包含有链接文字的字符串
        String finalWord = sb.toString();
        shareContent = finalWord;

        //创建一个 SpannableString对象
        SpannableString sp = new SpannableString(finalWord);
        if (!finalWord.equals("")) {
            List<Urls> lstUrl = newsInfo.getData().getUrlss();
            for (int i = 0; i < lstUrl.size(); i++) {
                String key = lstUrl.get(i).getDescription();
                if (!key.equals("")) {
                    if (finalWord.contains(key)) {
                        //设置超链接
                        sp.setSpan(new URLSpan(lstUrl.get(i).getDir()), finalWord.indexOf(key), finalWord.indexOf(key) + key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            textView.setText(sp);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        ll_content.addView(textView);
    }

    private void addVideoView(final String url, int width, int height) {
        if (ll_content == null) {
            ll_content = (LinearLayout) rootView.findViewById(R.id.id_ll_news_content);//内容父布局
        }
        final ControlVideoView videoView = new ControlVideoView(getActivity(), null);

        videoView.setVideoPath(url);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(320, 200);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        lp.width = MainApplication.Resources.getDisplayMetrics().widthPixels;
        lp.height = (MainApplication.Resources.getDisplayMetrics().widthPixels * height) / width;
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        videoView.setLayoutParams(lp);
        ll_content.addView(videoView);
        videoView.setFullScreenClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!videoView.isFullScreen()) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    videoView.setFullScreen(true);
                } else {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    videoView.setFullScreen(false);
                }
            }
        });
    }

    //初始化事件
    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//返回
                if (flag == 1001) {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                hideInputView();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(ItemInfoFragment.this);
                ft.commit();
            }
        });

        //相关阅读列表项点击事件
        this.lvRelatedRead.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int newsId = commonAdapter.getLstRelatedRead().get(i).getNewsId();
                if (newsId == 0) {
                    Toast.makeText(getActivity(), "相关新闻已被删除", Toast.LENGTH_SHORT);
                    return;
                }
                ItemInfoFragment itemInfoFragment = new ItemInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(KEY_NEWSID, newsId);
                bundle.putString(KEY_SHARE_IMG_URL, "xxxx");
                itemInfoFragment.setArguments(bundle);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.content_container, itemInfoFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //赞
        rlSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserMark userMark = newsInfo.getData().getUserMark();
                if (userMark != null && userMark.isSupport()) {
                    Toast.makeText(MainApplication.UIContext, "您已赞", Toast.LENGTH_SHORT).show();
                    return;
                }
                int count = newsInfo.getData().getSupportCount();
                Object supportCount = tvSupportCount.getTag();
                if (tvStepCount.getTag() != null || (userMark != null && userMark.isOpposition())) {
                    Toast.makeText(MainApplication.UIContext, "您已踩", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (supportCount != null) {
                    int temp = (int) supportCount;
                    if (temp == count + 1) {
                        Toast.makeText(MainApplication.UIContext, "您已赞", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //点赞加1效果
                Helper.getInstance(MainApplication.UIContext).supportOrStep(tvSupportCount, tvSupportAddOne, imgSupport, count, true);

                if (newsInfo.getData().getUserMark() != null && newsInfo.getData().getUserMark().isSupport()) {
                    Toast.makeText(MainApplication.UIContext, "您已赞", Toast.LENGTH_SHORT).show();
                }

                //判断登录状态
                if (MainApplication.isLogin()) {
                    CommonOperator.getInstance().supportOrStep(MainApplication.userInfo.getUserId(), newsId, SupportTypeEnum.News, 1, new ApiOperationCallback<ReturnInfo<String>>() {
                        @Override
                        public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                            Log.v(TAG, "supportOrStep zan callback=" + response.getContent());
                            if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                Log.i("supportOrStep", "赞成功");
                            } else if (result != null && !result.isSuccess()) {
                                Log.i("support", result.getErrorCode() + ":" + result.getMessage());
                            }
                        }
                    });
                }
            }
        });

        //踩
        rlStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserMark userMark = newsInfo.getData().getUserMark();
                if (userMark != null && userMark.isOpposition()) {
                    Toast.makeText(MainApplication.UIContext, "您已踩", Toast.LENGTH_SHORT).show();
                    return;
                }
                //点踩加1效果
                int count = newsInfo.getData().getOppositionCount();
                Object stepCount = tvSupportCount.getTag();
                if (tvSupportCount.getTag() != null || (userMark != null && userMark.isSupport())) {
                    Toast.makeText(MainApplication.UIContext, "您已赞", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (stepCount != null) {
                    int temp = (int) stepCount;
                    if (temp == count + 1) {
                        Toast.makeText(MainApplication.UIContext, "您已踩", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Helper.getInstance(MainApplication.UIContext).supportOrStep(tvStepCount, tvStepAddOne, imgStep, count, false);


                //判断登录状态
                if (MainApplication.isLogin()) {
                    CommonOperator.getInstance().supportOrStep(MainApplication.userInfo.getUserId(), newsId, SupportTypeEnum.News, 2, new ApiOperationCallback<ReturnInfo<String>>() {
                        @Override
                        public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                            Log.v(TAG, "supportOrStep cai callback=" + response.getContent());
                            if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                Log.i("supportOrStep", "踩成功");
                            }
                        }
                    });
                }

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
                if (MainApplication.isLogin()) {
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
                navigitToCommentListFragment();
            }
        });

        //收藏
        this.rl_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.isLogin()) {
                    //获取本地的收藏状态
                    SharedPreferencesUtil.KEY_PRIVATE_NEWS = "新闻收藏" + MainApplication.userInfo.getUserId() + "_" + newsInfo.getData().getNewsId();
                    String private_state = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey(SharedPreferencesUtil.KEY_PRIVATE_NEWS);
                    if (private_state != null && !private_state.isEmpty() && private_state.equals("已收藏")) {

                    }
                    if (!isPrivate) {
                        //收藏(本地化)
                        Toast.makeText(MainApplication.UIContext, "收藏成功", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update(SharedPreferencesUtil.KEY_PRIVATE_NEWS, "已收藏");
                        isPrivate = true;
                        imgPrivate.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_topline_private));
                        CommonOperator.getInstance().favorite(MainApplication.userInfo.getUserId(), newsId, SupportTypeEnum.News, PrivateResultEnum.PRIVATE, new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                Log.v(TAG, "favorite callback=" + response.getContent());
                                if (result != null && result.isSuccess()) {
                                    Log.i("private_state", "收藏成功");
                                }
//                                else if (result != null && result.getInfo().equals(String.valueOf(1)) && result.getErrorCode().equals(4)) {
//                                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                                }
                            }
                        });
                    } else {
                        Toast.makeText(MainApplication.UIContext, "取消收藏", Toast.LENGTH_SHORT).show();
                        SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update(SharedPreferencesUtil.KEY_PRIVATE_NEWS, "未收藏");
//                        Toast.makeText(MainApplication.UIContext, "已收藏", Toast.LENGTH_SHORT).show();
                        //取消收藏
                        CommonOperator.getInstance().favorite(MainApplication.userInfo.getUserId(), newsId, SupportTypeEnum.News, PrivateResultEnum.CANCEL, new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                Log.v(TAG, "favorite callback=" + response.getContent());
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    //Toast.makeText(MainApplication.UIContext, "取消收藏", Toast.LENGTH_SHORT).show();
                                    isPrivate = false;
                                    imgPrivate.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_topline_unprivate));
                                }
                            }
                        });
                    }
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(intent, LOGINREQUESTCODE);
                }
            }
        });

        //分享
        this.rl_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainApplication.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                if (newsInfo != null && newsInfo.getData() != null) {
                    SharePopupWindow sharePopupWindow = new SharePopupWindow(getActivity(), rootView);
                    sharePopupWindow.setShareInfo(newsInfo.getData().getTitle(),
                            newsInfo.getData().getDescription().equals("") ? newsInfo.getData().getTitle() : newsInfo.getData().getDescription(),
                            newsInfo.getData().getShareUrl(), shareImageUrl, SupportTypeEnum.News.getVal(), newsInfo.getData().getNewsId());
                }
            }
        });

        //发送
        rlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                if (!content.isEmpty()) {
                    CommonOperator.getInstance().publishComment(MainApplication.userInfo.getUserId(), newsId, content, 0, BlockTypeEnum.TOPLINE, new ApiOperationCallback<ReturnInfo<String>>() {
                        @Override
                        public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                            if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
                                int count = Integer.parseInt(tv_comment_count.getText().toString());
                                tv_comment_count.setText(String.valueOf(count + 1));
                                navigitToCommentListFragment();
                                hideInputView();
                            } else if (result != null && !result.getInfo().equals(String.valueOf(0))) {
                                Log.e(LOG_EXCEPTION, "errorcode:" + result.getErrorCode() + "message:" + result.getMessage());
                                if (result.getErrorCode().equals(String.valueOf(4))) {
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                }
                            } else if (exception != null) {
                                Log.e(LOG_EXCEPTION, exception.getCause().toString());
                            }
                        }
                    });
                }
            }
        });
    }

    private void navigitToCommentListFragment() {
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
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.content_container, commentListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
        fragmentTransaction.commit();
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
        if (newsInfo != null && newsInfo.getData() != null) {

            User user = newsInfo.getData().getUser();
            if (user != null) {
                mProfileIv.setVisibility(View.VISIBLE);
                ImageLoaderUtils.getImageLoader(getActivity())
                        .displayImage(user.getUserAvatar(), mProfileIv,
                                ImageLoaderUtils.getRoundDisplayOptionsStill(getResources().getDimensionPixelSize(R.dimen.item_info_title_profile_size)));
                tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            } else {
                tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            }

            this.tv_title.setText(newsInfo.getData().getTitle());
            this.tv_createtime.setText(newsInfo.getData().getModifyTime());
            String sourse = newsInfo.getData().getSource().getTitle().trim();
            this.tv_sourse.setText(sourse);

            titleBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (newsInfo != null && newsInfo.getData().getUser() != null) {
                        ActivityUtils.startNameCardActivity(getActivity(), newsInfo.getData().getUser().getUserId());
                    }
                }
            });

            //评论数
            int commentCount = newsInfo.getData().getCommentCount();
            if (commentCount > 0) {
                this.tv_comment_count.setVisibility(View.VISIBLE);
                this.tv_comment_count.setText(String.valueOf(commentCount));
            } else {
                this.tv_comment_count.setVisibility(View.GONE);
            }

            //赞和踩数量及用户赞状态
            this.tvSupportCount.setText("(" + newsInfo.getData().getSupportCount() + ")");
            this.tvStepCount.setText("(" + newsInfo.getData().getOppositionCount() + ")");

            if (newsInfo.getData().getUserMark() != null) {
                //收藏状态,根据用户当前登录状态，设置收藏状态
                if (newsInfo.getData().getUserMark().isFavorite()) {
                    imgPrivate.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_topline_private));
                    isPrivate = true;
                }
                //根据用户是否赞
                if (newsInfo.getData().getUserMark().isSupport()) {
                    imgSupport.setBackgroundResource(R.drawable.icon_support);
                    tvSupportCount.setTextColor(Color.RED);
                }
                //根据用户是否踩
                if (newsInfo.getData().getUserMark().isOpposition()) {
                    imgStep.setBackgroundResource(R.drawable.icon_step);
                    tvStepCount.setTextColor(Color.RED);
                }
            }
        }
    }

    private static final String TAG = ItemInfoFragment.class.getSimpleName();

    public void getInfoData(final int newsId, final UserInfoOperator.OnGetListener<TopLineNewsInfo> listener) {
        //final Dialog progressDialog = new ProgressDialog(getActivity());
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.more));
        progressDialog.show();
        TopLineOperator.getInstance().getNewsInfoById(newsId, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (isDetached()) {
                    return;
                }
                progressDialog.cancel();
                if (result != null) {
                    if (result.getInfo().equals(String.valueOf(0))) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        if (!TextUtils.isEmpty(json)) {
                            newsInfo = gson.fromJson(json, new TypeToken<TopLineNewsInfo>() {
                            }.getType());
                            if (newsInfo.getData() == null) {
                                Toast.makeText(getActivity(), "相关新闻不存在!", Toast.LENGTH_SHORT).show();
                                fm.popBackStack();
                                return;
                            }
                            shareImageUrl = newsInfo.getData().getThumbnail();
                            rl_Input.setEnabled(true);
                            rl_writeCommont.setEnabled(true);
                            rl_private.setEnabled(true);
                            rl_share.setEnabled(true);
                            rlSend.setEnabled(true);

                            if (newsInfo.getData().getUserMark() != null) {
                                Log.v(TAG, "getNewsInfoById result=" + newsInfo.getData().getUserMark().isOpposition() + " " + newsInfo.getData().getUserMark().isSupport());
                            } else {
                                Log.e(TAG, "getNewsInfoById NOT FOUND USER_MARK");
                            }
                            if (listener != null) {
                                listener.onGet(true, newsInfo);
                            }
                            bindData();
                            addViewByContent();
                        }
                    }
                }
            }
        });
    }

    //根据新闻Id获取新闻内容
    public void getInfoData(final int newsId) {
        this.getInfoData(newsId, null);
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View的宽高
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
