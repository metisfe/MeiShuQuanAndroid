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
import com.metis.meishuquan.model.assess.AssessListFilter;
import com.metis.meishuquan.model.assess.Channel;
import com.metis.meishuquan.model.assess.ChannelAndGradeData;
import com.metis.meishuquan.model.assess.Grade;
import com.metis.meishuquan.model.enums.AssessStateEnum;
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
    private List<Channel> lstChannel;
    private List<Grade> lstGrade;
    private ChannelGridViewAdapter channelAdapter;
    private GradeGridViewAdapter gradeAdapter;

    //条件
    private AssessStateEnum assessState = AssessStateEnum.ALL;
    private AssessListFilter assessListFilter = new AssessListFilter();
    private AssessFragment.OnFilterChanngedListner listner;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //从缓存中读取Grade和Channel数据
        getData();
        //获得当前用户已选择的Grade和Channel数据
        getCheckedData();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_assess_list_condition_filter, null, false);
        initView(rootView);
        setFilterData();
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

        this.gradeAdapter = new GradeGridViewAdapter(MainApplication.UIContext, this.lstGrade, this.assessListFilter.getLstSelectedGrade());
        this.channelAdapter = new ChannelGridViewAdapter(MainApplication.UIContext, this.lstChannel, this.assessListFilter.getLstSelectedChannel());

        this.gvGrade.setAdapter(gradeAdapter);
        this.gvChannel.setAdapter(channelAdapter);
    }

    //读取已缓存好的数据
    private void getData() {
        String json = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey(SharedPreferencesUtil.ASSESS_LIST_FILTER_DATA);
        Gson gson = new Gson();
        if (!json.equals("")) {
            ChannelAndGradeData data = gson.fromJson(json, new TypeToken<ChannelAndGradeData>() {
            }.getType());
            this.lstGrade = data.getData().getGradeList();
            this.lstChannel = data.getData().getChannelList();
        }
    }

    //读取用户上次选择的筛选条件
    private void getCheckedData() {
        String json = SharedPreferencesUtil.getInstanse(MainApplication.UIContext).getStringByKey(SharedPreferencesUtil.CHECKED_ASSESS_FILTER + MainApplication.userInfo.getUserId());
        Gson gson = new Gson();
        if (!json.equals("")) {
            AssessListFilter data = gson.fromJson(json, new TypeToken<AssessListFilter>() {
            }.getType());
            assessListFilter = data;
        }
    }

    private void initEvent() {
        //确定
        this.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //传递已选择的筛选条件
                AssessListFilter assessListFilterNew = new AssessListFilter();
                assessListFilterNew.setAssessState(assessState);//评价状态（已评价，未评语，全部）
                assessListFilterNew.setLstSelectedChannel(channelAdapter.getCheckedChannel());
                assessListFilterNew.setLstSelectedGrade(gradeAdapter.getCheckedGrade());
                listner.setFilter(assessListFilterNew);
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(FilterConditionForAssessListFragment.this);
                ft.commit();
            }
        });

        //已评价
        this.btnAssessStateTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assessState = AssessStateEnum.ASSESSED;
                setButtonChecked(btnAssessStateTrue);
                setButtonUnChecked(btnAssessStateFalse);
                setButtonUnChecked(btnAssessStateAll);
            }
        });

        //未评价
        this.btnAssessStateFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assessState = AssessStateEnum.UNASSESS;
                setButtonChecked(btnAssessStateFalse);
                setButtonUnChecked(btnAssessStateTrue);
                setButtonUnChecked(btnAssessStateAll);
            }
        });

        //评论状态 全部
        this.btnAssessStateAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assessState = AssessStateEnum.ALL;
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
                gradeAdapter.lstOldCheckedGrade.clear();
                gradeAdapter.lstCheckedGrade.clear();
                gradeAdapter.notifyDataSetChanged();

            }
        });

        //标签 全部
        this.btnChannelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnChannelAll);
                gvChannel.setAdapter(channelAdapter);
                channelAdapter.lstOldCheckedChannel.clear();
                channelAdapter.lstCheckedChannel.clear();
                channelAdapter.notifyDataSetChanged();

            }
        });

//        this.gvGrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                for (int i = 0; i < parent.getCount(); i++) {
//                    View v = parent.getChildAt(i);
//                    TextView textview = (TextView) v;
//                    if (position == i) {//当前选中的Item改变背景颜色
//                        setSelectedColorForTextView(textview);
//                        Grade selsectedGrade = gradeAdapter.getItem(i);
//                        assessListFilter.getLstSelectedGrade().add(selsectedGrade);
//                        setButtonUnChecked(btnGradeAll);
//                    }
//                }
//            }
//        });
//
//        this.gvChannel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
//                for (int i = 0; i < parent.getCount(); i++) {
//                    View v = parent.getChildAt(i);
//                    TextView textview = (TextView) v;
//                    if (position == i) {//当前选中的Item改变背景颜色
//                        setSelectedColorForTextView(textview);
//                        Channel selectedChannel = channelAdapter.getItem(i);
//                        assessListFilter.getLstSelectedChannel().add(selectedChannel);
//                        setButtonUnChecked(btnChannelAll);
//                    }
//                }
//            }
//        });
    }

    private void setFilterData() {
        //评价状态
        if (assessListFilter.getAssessState() == AssessStateEnum.ALL) {
            setButtonChecked(this.btnAssessStateAll);
        } else if (assessListFilter.getAssessState() == AssessStateEnum.ASSESSED) {
            setButtonChecked(this.btnAssessStateTrue);
            setButtonUnChecked(this.btnAssessStateAll);
        } else if (assessListFilter.getAssessState() == AssessStateEnum.UNASSESS) {
            setButtonChecked(this.btnAssessStateFalse);
            setButtonUnChecked(this.btnAssessStateAll);
        }
        //年级
        if (assessListFilter.getLstSelectedGrade().size() > 0) {
            setButtonUnChecked(this.btnGradeAll);
        }
        //频道
        if (assessListFilter.getLstSelectedChannel().size() > 0) {
            setButtonUnChecked(this.btnChannelAll);
        }
    }

    public void setFilterConditionListner(AssessFragment.OnFilterChanngedListner listner) {
        this.listner = listner;
    }

    private void setSelectedColorForTextView(TextView tv) {
        tv.setTextColor(Color.rgb(251, 109, 109));
    }

//    private void setUnselectedColorForTextView(TextView[] tvs) {
//        for (TextView textView : tvs) {
//            textView.setTextColor(Color.rgb(126, 126, 126));
//        }
//    }

    private void setButtonChecked(Button btn) {
        btn.setTextColor(Color.rgb(251, 109, 109));
    }

    private void setButtonUnChecked(Button btn) {
        btn.setTextColor(Color.rgb(126, 126, 126));
    }
}
