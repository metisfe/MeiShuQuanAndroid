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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.assess.ExpandeAdapter;
import com.metis.meishuquan.model.assess.AllCity;
import com.metis.meishuquan.model.assess.City;
import com.metis.meishuquan.model.assess.Province;
import com.metis.meishuquan.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment:城市选择
 * <p/>
 * Created by wj on 15/4/1.
 */
public class ChooseCityFragment extends Fragment {
    private ExpandableListView listView;
    private Button btnBack;
    private SearchView searchView;
    private ExpandeAdapter adapter;
    private AllCity mAllCity;
    private OnCityChooseListener mListener = null;

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
        getData();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_assess_city_list, null);
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        this.listView = (ExpandableListView) rootView.findViewById(R.id.id_assess_city_listview);
        this.listView.setGroupIndicator(null);
        this.listView.setBackgroundColor(Color.rgb(255, 255, 255));
        this.listView.setTextFilterEnabled(true);

        this.btnBack = (Button) rootView.findViewById(R.id.id_assess_city_btn_back);

        this.searchView = (SearchView) rootView.findViewById(R.id.id_assess_city_autocomplete_searchView);
        this.searchView.setSubmitButtonEnabled(false);


        this.adapter = new ExpandeAdapter(MainApplication.UIContext, mAllCity);
        this.listView.setAdapter(adapter);
    }

    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(ChooseCityFragment.this);
                fm.popBackStack();
                ft.commit();
            }
        });

        this.listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView listView, View view, int groupPosition, int childPosition, long id) {
                City city = adapter.getChild(groupPosition, childPosition);
                if (mListener != null) {
                    mListener.onChoose(city);
                }
                Toast.makeText(MainApplication.UIContext, "您选择的城市为:" + city.getCityName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                AllCity allCity = searchItem(s);
                if (allCity.getData().size() > 0) {
                    adapter.setData(allCity);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    public AllCity searchItem(String val) {
        AllCity allCity = new AllCity();
        List<Province> provinces = new ArrayList<Province>();
        for (int i = 0; i < mAllCity.getData().size(); i++) {
            List<City> citys = new ArrayList<City>();
            Province province = mAllCity.getData().get(i);
            List<City> citysTemp = province.getCityList();
            for (int j = 0; j < citysTemp.size(); j++) {
                City city = citysTemp.get(j);
                city.setGroupName(province.getGroupName());
                if (city.getCityName().indexOf(val) != -1) {
                    citys.add(city);
                }
            }
            if (citys.size() > 0) {
                province.setCityList(citys);
                provinces.add(province);
            }
        }
        allCity.setData(provinces);
        return allCity;
    }

    private void getData() {
        SharedPreferencesUtil spu = SharedPreferencesUtil.getInstanse(MainApplication.UIContext);
        String json = spu.getStringByKey(SharedPreferencesUtil.REGION);
        Gson gson = new Gson();
        mAllCity = gson.fromJson(json, new TypeToken<AllCity>() {
        }.getType());
    }

    public void setOnCityChooseListener (OnCityChooseListener listener) {
        mListener = listener;
    }

    public static interface OnCityChooseListener {
        public void onChoose (City city);
    }
}
