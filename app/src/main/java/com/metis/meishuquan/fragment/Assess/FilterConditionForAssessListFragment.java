package com.metis.meishuquan.fragment.assess;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.assess.ChannelGridViewAdapter;
import com.metis.meishuquan.adapter.assess.GradeGridViewAdapter;
import com.metis.meishuquan.fragment.main.AssessFragment;
import com.metis.meishuquan.model.assess.Channel;
import com.metis.meishuquan.model.assess.ChannelAndGradeData;
import com.metis.meishuquan.model.assess.Grade;
import com.metis.meishuquan.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment:点评列表过滤条件
 * <p/>
 * Created by wj on 15/4/1.
 */
public class FilterConditionForAssessListFragment extends Fragment {
    private FragmentManager fm;
    private Button btnConfirm, btnAssessStateTrue, btnAssessStateFalse, btnAssessStateAll, btnGradeAll, btnChannelAll;
    private GridView gvGrade, gvChannel;
    private ChannelGridViewAdapter channelAdapter;
    private GradeGridViewAdapter gradeAdapter;
    private List<Grade> lstGrade;
    private List<Channel> lstChannel;

    //条件
    private int type;//1最新 2已评价 3未评价
    private Grade selsectedGrade;
    private Channel selectedChannel;

    private AssessFragment.OnFilterCannedListner listner;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //从缓存中读取Grade和Channel数据
        getData();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_assess_list_condition_filter, null);
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        fm = getActivity().getSupportFragmentManager();
        this.btnConfirm = (Button) rootView.findViewById(R.id.id_btn_confirm);
        this.btnAssessStateTrue = (Button) rootView.findViewById(R.id.id_btn_state_true);
        this.btnAssessStateFalse = (Button) rootView.findViewById(R.id.id_btn_state_false);
        this.gvGrade = (GridView) rootView.findViewById(R.id.id_gridview_grade);
        this.gvChannel = (GridView) rootView.findViewById(R.id.id_gridview_channels);
        this.btnAssessStateAll = (Button) rootView.findViewById(R.id.id_btn_state_all);
        this.btnGradeAll = (Button) rootView.findViewById(R.id.id_btn_grade_all);
        this.btnChannelAll = (Button) rootView.findViewById(R.id.id_btn_channel_all);

        this.gradeAdapter = new GradeGridViewAdapter(MainApplication.UIContext, lstGrade);
        this.channelAdapter = new ChannelGridViewAdapter(MainApplication.UIContext, lstChannel);

        this.gvGrade.setAdapter(gradeAdapter);
        this.gvChannel.setAdapter(channelAdapter);
    }

    private void getData() {
        SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
        String json = spu.getStringByKey(SharedPreferencesUtil.ASSESS_CHANNEL_LIST);
        Gson gson = new Gson();
        if (!json.equals("")) {
            ChannelAndGradeData data = gson.fromJson(json, new TypeToken<ChannelAndGradeData>() {
            }.getType());
            this.lstGrade = data.getData().getGradeList();
            this.lstChannel = data.getData().getChannelList();
        }
    }

    private void initEvent() {
        //确定
        this.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.setFilter(selsectedGrade, selectedChannel, type);
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(FilterConditionForAssessListFragment.this);
                ft.commit();
            }
        });

        //已评价
        this.btnAssessStateTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 2;
                setButtonChecked(btnAssessStateTrue);
                setButtonUnChecked(btnAssessStateFalse);
                setButtonUnChecked(btnAssessStateAll);
            }
        });

        //未评价
        this.btnAssessStateFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 3;
                setButtonChecked(btnAssessStateFalse);
                setButtonUnChecked(btnAssessStateTrue);
                setButtonUnChecked(btnAssessStateAll);
            }
        });

        //评论状态 全部
        this.btnAssessStateAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 1;
                setButtonChecked(btnAssessStateAll);
                setButtonUnChecked(btnAssessStateFalse);
                setButtonUnChecked(btnAssessStateTrue);
            }
        });

        //年级 全部
        this.btnGradeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnGradeAll);
                gvGrade.setAdapter(gradeAdapter);
                gradeAdapter.notifyDataSetChanged();
            }
        });

        //标签 全部
        this.btnChannelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnChannelAll);
                gvChannel.setAdapter(channelAdapter);
                channelAdapter.notifyDataSetChanged();
            }
        });

        this.gvGrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < parent.getCount(); i++) {
                    View v = parent.getChildAt(i);
                    TextView textview = (TextView) v;
                    if (position == i) {//当前选中的Item改变背景颜色
                        setSelectedColorForTextView(textview);
                        selsectedGrade = gradeAdapter.getItem(i);
                        setButtonUnChecked(btnGradeAll);
                    } else {
                        setUnselectedColorForTextView(new TextView[]{textview});
                    }
                }
            }
        });

        this.gvChannel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                for (int i = 0; i < parent.getCount(); i++) {
                    View v = parent.getChildAt(i);
                    TextView textview = (TextView) v;
                    if (position == i) {//当前选中的Item改变背景颜色
                        setSelectedColorForTextView(textview);
                        selectedChannel = channelAdapter.getItem(i);
                        setButtonUnChecked(btnChannelAll);
                    } else {
                        setUnselectedColorForTextView(new TextView[]{textview});
                    }
                }
            }
        });
    }

    private void getFilterCondition(AssessFragment.OnFilterCannedListner listner) {
        this.listner = listner;
    }

    private void setSelectedColorForTextView(TextView tv) {
        tv.setTextColor(Color.rgb(251, 109, 109));
    }

    private void setUnselectedColorForTextView(TextView[] tvs) {
        for (TextView textView : tvs) {
            textView.setTextColor(Color.rgb(126, 126, 126));
        }
    }

    private void setButtonChecked(Button btn) {
        btn.setTextColor(Color.rgb(251, 109, 109));
    }

    private void setButtonUnChecked(Button btn) {
        btn.setTextColor(Color.rgb(126, 126, 126));
    }
}
