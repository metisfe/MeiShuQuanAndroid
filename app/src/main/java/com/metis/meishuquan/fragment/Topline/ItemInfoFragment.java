package com.metis.meishuquan.fragment.Topline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
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
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.BlockTypeEnum;
import com.metis.meishuquan.model.enums.LoginStateEnum;
import com.metis.meishuquan.model.enums.PrivateResultEnum;
import com.metis.meishuquan.model.enums.PrivateTypeEnum;
import com.metis.meishuquan.model.topline.ContentInfo;
import com.metis.meishuquan.model.topline.TopLineNewsInfo;
import com.metis.meishuquan.model.topline.Urls;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.popup.SharePopupWindow;
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
    private final int LOGINREQUESTCODE = 1001;

    private Button btnBack, btnShare;
    private ViewGroup rootView;
    private LinearLayout ll_content;
    private TopLineNewsInfo newsInfo;
    private TextView tv_title, tv_createtime, tv_sourse, tv_comment_count;
    private RelativeLayout rl_writeCommont, rl_commontList, rl_private, rl_share, rl_main;
    private RelativeLayout rl_Input;
    private ImageView imgPrivate;
    private ScrollView contentScrollView;
    private EditText editText;
    private RelativeLayout rlSend;
    private boolean isPrivate = false;
    private List<ImageView> imageGroup = new ArrayList<ImageView>();
    private boolean windowAttached;

    private int newsId = 0;
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

        editText = (EditText) rootView.findViewById(R.id.id_comment_edittext);
        imgPrivate = (ImageView) rootView.findViewById(R.id.id_img_favorite);//收藏图标

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
                        addTextView(contentInfo.getData().getContent());
                    } else {
                        addTextView(contentInfo.getData().getContent());
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

        ImageView imageView = new ImageView(getActivity());
        ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(url.trim(), imageView, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.img_topline_default));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width * 2, height * 2);
        lp.topMargin = 10;
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(lp);

        ll_content.addView(imageView);
        imageGroup.add(imageView);
    }

    //添加文本控件
    private void addTextView(String words) {
        if (ll_content == null) {
            ll_content = (LinearLayout) rootView.findViewById(R.id.id_ll_news_content);//内容父布局
        }
        TextView textView = new TextView(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        textView.setLayoutParams(lp);
        textView.setTextSize(16);
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
        String finalWord = sb.toString().trim();

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

    //初始化事件
    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//返回
                hideInputView();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(ItemInfoFragment.this);
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
                if (MainApplication.userInfo.getAppLoginState() == LoginStateEnum.YES) {
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
                fragmentTransaction.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                fragmentTransaction.commit();
            }
        });

        //收藏
        this.rl_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.isLogin()) {
                    if (!isPrivate) {
                        //收藏
                        CommonOperator.getInstance().favorite(MainApplication.userInfo.getUserId(), newsId, PrivateTypeEnum.NEWS, PrivateResultEnum.PRIVATE, new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Toast.makeText(MainApplication.UIContext, "收藏成功", Toast.LENGTH_SHORT).show();
                                    isPrivate = true;
                                    imgPrivate.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_topline_private));
                                }
                            }
                        });
                    } else {
                        //取消收藏
                        CommonOperator.getInstance().favorite(MainApplication.userInfo.getUserId(), newsId, PrivateTypeEnum.NEWS, PrivateResultEnum.CANCEL, new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Toast.makeText(MainApplication.UIContext, "取消收藏", Toast.LENGTH_SHORT).show();
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
                new SharePopupWindow(MainApplication.UIContext, rootView);
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

            //TODO:收藏状态,根据用户当前登录状态，设置收藏状态


        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        windowAttached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        windowAttached = false;
        for (ImageView imageView : imageGroup) {
            if (imageView != null && imageView.getDrawable() != null) {
                ((BitmapDrawable) imageView.getDrawable()).getBitmap().recycle();
                imageView.setImageDrawable(null);
            }
        }

        imageGroup = null;
    }

    //根据新闻Id获取新闻内容
    private void getInfoData(final int newsId) {
        TopLineOperator topLineOperator = TopLineOperator.getInstance();
        topLineOperator.getNewsInfoById(newsId, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (!windowAttached) return;
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
