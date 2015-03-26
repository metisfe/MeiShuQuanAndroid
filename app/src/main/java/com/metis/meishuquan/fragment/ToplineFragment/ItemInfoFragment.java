package com.metis.meishuquan.fragment.ToplineFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.fragment.main.ToplineFragment;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.TopLineNewsInfo;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

/**
 * 头条列表项详细信息界面
 * <p/>
 * Created by wj on 15/3/23.
 */
public class ItemInfoFragment extends BaseFragment {
    private Button btnBack;
    private LinearLayout ll_content;
    private TopLineNewsInfo newsInfo;
    private TextView tv_title, tv_createtime, tv_sourse;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载新闻详细
        Bundle args = this.getArguments();
        if (args != null) {
            int newsId = args.getInt("newsId");
            //根据新闻Id获取新闻内容
            getInfoData(newsId);
        }

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_topline_topbar_list_item_info, null, false);

        initView(rootView);
        addViewByContent();
        bindData();
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
        ll_content = (LinearLayout) rootView.findViewById(R.id.id_ll_news_content);//内容父布局
        tv_title = (TextView) rootView.findViewById(R.id.id_title);
        tv_createtime = (TextView) rootView.findViewById(R.id.id_createtime);
        tv_sourse = (TextView) rootView.findViewById(R.id.id_source);
    }

    private void addViewByContent() {
        if (newsInfo != null) {
            String content = newsInfo.getData().getContent();
            //解析内容
            int p_start = content.indexOf("<p>", 0);
            int p_end = content.indexOf("</p>", p_start);
            String word = content.substring(p_start, p_end);
            if (!word.equals("")) {
                TextView tv_word1 = new TextView(getActivity());
                tv_word1.setLayoutParams(new TextSwitcher.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv_word1.setText(word);
                tv_word1.setTextSize(15);
            }
        }
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
    }

    //为控件绑定数据
    private void bindData() {
        if (newsInfo != null) {
            this.tv_title.setText(newsInfo.getData().getTitle());
            this.tv_createtime.setText(newsInfo.getData().getModifyTime());
            this.tv_sourse.setText(newsInfo.getData().getSource());
        }
    }

    //根据新闻Id获取新闻内容
    private void getInfoData(int newsId) {
        TopLineOperator topLineOperator = TopLineOperator.getInstance();
        topLineOperator.getNewsInfoById(newsId, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result.getInfo().equals(String.valueOf(0))) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    if (!json.equals("")) {
                        newsInfo = gson.fromJson(json, new TypeToken<TopLineNewsInfo>() {
                        }.getType());
                    }
                }
            }
        });
    }
}
