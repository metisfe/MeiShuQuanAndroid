package com.metis.meishuquan.fragment.ToplineFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.TopLineNewsInfo;
import com.metis.meishuquan.util.Utils;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.StringTokenizer;

/**
 * 头条列表项详细信息界面
 * <p/>
 * Created by wj on 15/3/23.
 */
public class ItemInfoFragment extends BaseFragment {
    private Button btnBack;
    private ViewGroup rootView;
    private LinearLayout ll_content;
    private TopLineNewsInfo newsInfo;
    private TextView tv_title, tv_createtime, tv_sourse;
    private Button btn_writeCommont,btn_commontList,btn_private,btn_share;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载新闻详细
        Bundle args = this.getArguments();
        if (args != null) {
            int newsId = args.getInt("newsId");
            //根据新闻Id获取新闻内容
            getInfoData(newsId);
        }

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_topline_topbar_list_item_info, null, false);

        initView(rootView);
        //addViewByContent();
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
        tv_sourse = (TextView) rootView.findViewById(R.id.id_source);
        btn_writeCommont= (Button) rootView.findViewById(R.id.id_btn_writecomment);
        btn_commontList= (Button) rootView.findViewById(R.id.id_btn_commentlist);
        btn_private= (Button) rootView.findViewById(R.id.id_btn_private);
        btn_share= (Button) rootView.findViewById(R.id.id_btn_share);
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
                    String wordTemp = str[j].replace("<p>", "  ");
                    String word=wordTemp.replace("</p>","");
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
        String url="";
        int width = 0;
        int height = 0;

        if (ll_content == null) {
            ll_content = (LinearLayout) rootView.findViewById(R.id.id_ll_news_content);//内容父布局
        }

        SmartImageView imageView = new SmartImageView(MainApplication.UIContext);
        for (int i = 0; i < newsInfo.getData().getUrlss().size(); i++) {
            if (newsInfo.getData().getUrlss().get(i).getNewShowContent().equals(name)) {
                url = newsInfo.getData().getUrlss().get(i).getDir();
                //width = newsInfo.getData().getUrlss().get(i).getWidth();
                //height = newsInfo.getData().getUrlss().get(i).getHeight();
            }
        }

        imageView.setImageUrl(url.trim(),R.drawable.icon);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(lp);

        ll_content.addView(imageView);
        int yStart = -this.getResources().getDisplayMetrics().heightPixels;
        int yEnd = 0;
        TranslateAnimation tranlateAnimation = new TranslateAnimation(0, 0, yStart, yEnd);
        tranlateAnimation.setFillAfter(true);
        tranlateAnimation.setFillEnabled(true);
        tranlateAnimation.setDuration(300);
        tranlateAnimation.setInterpolator(new DecelerateInterpolator());

        imageView.startAnimation(tranlateAnimation);
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
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);

        ll_content.addView(textView);
        int yStart = -this.getResources().getDisplayMetrics().heightPixels;
        int yEnd = 0;
        TranslateAnimation tranlateAnimation = new TranslateAnimation(0, 0, yStart, yEnd);
        tranlateAnimation.setFillAfter(true);
        tranlateAnimation.setFillEnabled(true);
        tranlateAnimation.setDuration(300);
        tranlateAnimation.setInterpolator(new DecelerateInterpolator());

        textView.startAnimation(tranlateAnimation);
    }

    //初始化事件
    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(ItemInfoFragment.this);
                ft.commit();
            }
        });

        this.btn_writeCommont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        this.btn_commontList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        this.btn_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.alertMessageDialog("提示","收藏成功！");
            }
        });

        this.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //为控件绑定数据
    private void bindData() {
        if (newsInfo != null) {
            this.tv_title.setText(newsInfo.getData().getTitle());
            this.tv_createtime.setText(newsInfo.getData().getModifyTime());
            this.tv_sourse.setText(newsInfo.getData().getSource().getTitle());
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
                        if (!json.equals("")) {
                            newsInfo = gson.fromJson(json, new TypeToken<TopLineNewsInfo>() {
                            }.getType());
                        }
                    }
                    addViewByContent();
                    bindData();
                } else {
                    getInfoData(newsId);
                }
            }
        });
    }
}
