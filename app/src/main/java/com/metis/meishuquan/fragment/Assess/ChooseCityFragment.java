package com.metis.meishuquan.fragment.Assess;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.ExpandeAdapter;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.model.assess.AllCity;
import com.metis.meishuquan.model.assess.City;
import com.metis.meishuquan.util.SharedPreferencesUtil;

/**
 * Fragment:城市选择
 * <p/>
 * Created by wj on 15/4/1.
 */
public class ChooseCityFragment extends BaseFragment {
    private ExpandableListView listView;
    private Button btnBack;
    private AutoCompleteTextView autoCompleteTextView;
    private ExpandeAdapter adapter;
    private AllCity mAllCity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getData();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_assess_city_list, null);
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        this.listView = (ExpandableListView) rootView.findViewById(R.id.id_assess_city_listview);
        this.btnBack = (Button) rootView.findViewById(R.id.id_assess_city_btn_back);
        this.autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.id_assess_city_autocomplete_textview);

        this.listView.setGroupIndicator(null);
        this.listView.setBackgroundColor(Color.rgb(255,255,255));

        this.adapter=new ExpandeAdapter(MainApplication.UIContext,mAllCity);
        this.listView.setAdapter(adapter);
    }

    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm=getActivity().getSupportFragmentManager();
                FragmentTransaction ft= fm.beginTransaction();
                ft.remove(ChooseCityFragment.this);
                ft.commit();
            }
        });

        this.listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView listView, View view, int groupPosition, int childPosition, long id) {
                City city=adapter.getChild(groupPosition,childPosition);
                Toast.makeText(MainApplication.UIContext,"您选择的城市为:"+city.getCityName(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void getData(){
        SharedPreferencesUtil spu=SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
        String json=spu.getStringByKey(SharedPreferencesUtil.REGION);
        Gson gson= new Gson();
        mAllCity=gson.fromJson(json,new TypeToken<AllCity>(){}.getType());
    }
}
