package com.metis.meishuquan.fragment.act;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.commons.MultiListViewFragment;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.assess.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/12.
 */
public class AreaSelectFragment extends MultiListViewFragment {

    private AreaAdapter mProvAdapter = null;
    private AreaAdapter mCityAdapter = null, mTownAdapter;

    private List<Areable> mProvinceDataList = new ArrayList<Areable>();
    private List<Areable> mCityDataList = new ArrayList<Areable>();
    private List<Areable> mTownDataList = new ArrayList<Areable>();
    private List<BaseAdapter> mAdapters = new ArrayList<BaseAdapter>();

    private int mProvinceId, mCityId, mTownId;

    private OnPlaceChooseListener mFragmentAreaListener = null;

    private static AreaSelectFragment sFragment = new AreaSelectFragment();

    public static AreaSelectFragment getInstance () {
        return sFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mAdapters.isEmpty()) {
            mProvAdapter = new AreaAdapter(mProvinceDataList);
            mCityAdapter = new AreaAdapter(mCityDataList);
            mTownAdapter = new AreaAdapter(mTownDataList);
            mAdapters.add(mProvAdapter);
            mAdapters.add(mCityAdapter);
            mAdapters.add(mTownAdapter);
        }

        if (mProvinceDataList.isEmpty()) {
            UserInfoOperator.getInstance().getProvinceList(new UserInfoOperator.OnGetListener<List<UserInfoOperator.SimpleProvince>>() {
                @Override
                public void onGet(boolean succeed, List<UserInfoOperator.SimpleProvince> simpleProvinces) {
                    if (succeed) {
                        mProvinceDataList.clear();
                        List<ProvinceArea> provinceAreas = new ArrayList<ProvinceArea>();
                        provinceAreas.add(new ProvinceArea(UserInfoOperator.SimpleProvince.getDefaultOne()));
                        for (UserInfoOperator.SimpleProvince p : simpleProvinces) {
                            provinceAreas.add(new ProvinceArea(p));
                        }
                        mProvinceDataList.addAll(provinceAreas);
                        mProvAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

        setAdapterList(mAdapters);
        mProvAdapter.setOnAreaChooseListener(new OnAreaChooseListener() {
            @Override
            public void onChoose(Areable area) {
                mCityDataList.clear();
                mCityAdapter.notifyDataSetChanged();
                mTownDataList.clear();
                mTownAdapter.notifyDataSetChanged();
                mProvinceId = area.getId();
                mCityId = 0;
                mTownId = 0;
                if (area.getId() == 0) {
                    onAreaSelected(area);
                    return;
                }
                UserInfoOperator.getInstance().getAreaList(area.getId(), new UserInfoOperator.OnGetListener<List<City>>() {
                    @Override
                    public void onGet(boolean succeed, List<City> cities) {
                        if (succeed) {
                            mCityDataList.clear();
                            List<Areable> cityAreas = new ArrayList<Areable>();
                            for (City c : cities) {
                                cityAreas.add(new CityArea(c));
                            }
                            cityAreas.add(0, mProvAdapter.getSelectedAreable().cloneUnselectedOne());
                            mCityDataList.addAll(cityAreas);
                            mCityAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        mCityAdapter.setOnAreaChooseListener(new OnAreaChooseListener() {
            @Override
            public void onChoose(Areable area) {
                mCityId = area.getId();
                mTownId = 0;
                if (mProvAdapter.getSelectedAreable() != null && area.getId() == mProvAdapter.getSelectedAreable().getId()) {
                    mTownDataList.clear();
                    mTownAdapter.notifyDataSetChanged();
                    onAreaSelected(area);
                    return;
                }
                mTownDataList.clear();
                mTownAdapter.notifyDataSetChanged();
                UserInfoOperator.getInstance().getAreaList(area.getId(), new UserInfoOperator.OnGetListener<List<City>>() {
                    @Override
                    public void onGet(boolean succeed, List<City> cities) {
                        if (succeed) {
                            mTownDataList.clear();
                            List<Areable> townAreas = new ArrayList<Areable>();
                            for (City c : cities) {
                                townAreas.add(new CityArea(c));
                            }
                            townAreas.add(0, mCityAdapter.getSelectedAreable().cloneUnselectedOne());
                            mTownDataList.addAll(townAreas);
                            mTownAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        mTownAdapter.setOnAreaChooseListener(new OnAreaChooseListener() {
            @Override
            public void onChoose(Areable area) {
                mTownId = area.getId();
                onAreaSelected(area);
            }
        });

    }

    public void setOnPlaceChooseListener (OnPlaceChooseListener listener) {
        mFragmentAreaListener = listener;
    }

    public static interface OnPlaceChooseListener{
        public void onChoose (Areable areable, int provinceId, int cityId, int townId);
    }

    public void onAreaSelected (Areable areable) {
        if (mFragmentAreaListener != null) {
            mFragmentAreaListener.onChoose(areable, mProvinceId, mCityId, mTownId);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setOnPlaceChooseListener(null);
    }

    private class AreaAdapter extends BaseAdapter {

        private List<Areable> mDataList = null;
        private OnAreaChooseListener mAreaListener = null;

        private Areable mSelectedAreable = null;

        public AreaAdapter (List<Areable> list) {
            mDataList = list;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Areable getItem(int i) {
            return mDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_list_dialog_item, null);
            TextView tv = (TextView)view.findViewById(R.id.list_dialog_item);
            tv.setText(getItem(i).getTitle());
            view.setBackgroundColor(getResources().getColor(getItem(i).isSelected() ? R.color.ltgray : android.R.color.white));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSelectedAreable != null) {
                        mSelectedAreable.setSelected(false);
                    }
                    mSelectedAreable = getItem(i);
                    mSelectedAreable.setSelected(true);
                    notifyDataSetChanged();
                    if (mAreaListener != null) {
                        mAreaListener.onChoose(getItem(i));
                    }
                }
            });
            return view;
        }

        public Areable getSelectedAreable () {
            return mSelectedAreable;
        }

        public void setOnAreaChooseListener (OnAreaChooseListener listener) {
            mAreaListener = listener;
        }

    }

    public static interface OnAreaChooseListener {
        public void onChoose (Areable areable);
    }

    private class ProvinceArea implements Areable {

        private UserInfoOperator.SimpleProvince mProvince = null;

        public ProvinceArea (UserInfoOperator.SimpleProvince province) {
            mProvince = province;
        }

        private boolean isSelected = false;

        @Override
        public String getTitle() {
            return mProvince.getName();
        }

        @Override
        public int getId() {
            return mProvince.getProvinceId();
        }

        @Override
        public boolean isSelected() {
            return isSelected;
        }

        @Override
        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public ProvinceArea cloneUnselectedOne () {
            return new ProvinceArea(mProvince);
        }
    }

    private class CityArea implements Areable {

        private City mCity = null;

        private boolean isSelected = false;

        public CityArea (City city) {
            mCity = city;
        }

        @Override
        public String getTitle() {
            return mCity.getCityName();
        }

        @Override
        public int getId() {
            return mCity.getCodeid();
        }

        @Override
        public boolean isSelected() {
            return isSelected;
        }

        @Override
        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public CityArea cloneUnselectedOne () {
            return new CityArea(mCity);
        }
    }

    public static interface Areable {
        public String getTitle ();
        public int getId ();
        public boolean isSelected ();
        public void setSelected (boolean selected);
        public Areable cloneUnselectedOne ();
    }
}
