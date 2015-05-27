package com.metis.meishuquan.fragment.act;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
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

    private List<CityArea> mProvinceDataList = new ArrayList<CityArea>();
    private List<CityArea> mCityDataList = new ArrayList<CityArea>();
    private List<CityArea> mTownDataList = new ArrayList<CityArea>();
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
    public void reset() {
        mProvinceDataList.clear();
        mCityDataList.clear();
        mTownDataList.clear();
        mAdapters.clear();
        if (mProvAdapter != null) {
            mProvAdapter.notifyDataSetInvalidated();
        }
        if (mCityAdapter != null) {
            mCityAdapter.notifyDataSetChanged();
        }
        if (mTownAdapter != null) {
            mTownAdapter.notifyDataSetChanged();
        }
        super.reset();
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
            UserInfoOperator.getInstance().getProvinceStudio(0, new UserInfoOperator.OnGetListener<List<City>>() {
                @Override
                public void onGet(boolean succeed, List<City> cities) {
                    if (isDetached()) {
                        return;
                    }
                    if (succeed) {
                        mProvinceDataList.clear();
                        List<CityArea> provienceAreas = new ArrayList<CityArea>();
                        int countAll = 0;
                        for (City city : cities) {
                            countAll += city.getStudioCount();
                            provienceAreas.add(new CityArea(city));
                        }

                        mProvinceDataList.addAll(provienceAreas);
                        if (mProvinceDataList.size() > 0) {
                            City city = new City();
                            city.setCityName(getString(R.string.all));
                            CityArea all = new CityArea(city);
                            all.getmCity().setStudioCount(countAll);
                            mProvinceDataList.add(0, all);
                        }
                        mProvAdapter.notifyDataSetInvalidated();
                    }
                }
            });
            /*UserInfoOperator.getInstance().getAreaList(0, new UserInfoOperator.OnGetListener<List<City>>() {
                @Override
                public void onGet(boolean succeed, List<City> cities) {
                    if (succeed) {
                        mProvinceDataList.clear();
                        List<CityArea> provienceAreas = new ArrayList<CityArea>();
                        int countAll = 0;
                        for (City city : cities) {
                            countAll += city.getStudioCount();
                            provienceAreas.add(new CityArea(city));
                        }

                        mProvinceDataList.addAll(provienceAreas);
                        if (mProvinceDataList.size() > 0) {
                            City city = new City();
                            city.setCityName(getString(R.string.all));
                            CityArea all = new CityArea(city);
                            all.getmCity().setStudioCount(countAll);
                            mProvinceDataList.add(0, all);
                        }
                        mProvAdapter.notifyDataSetInvalidated();
                    }
                }
            });*/
            /*UserInfoOperator.getInstance().getProvinceList(new UserInfoOperator.OnGetListener<List<UserInfoOperator.SimpleProvince>>() {
                @Override
                public void onGet(boolean succeed, List<UserInfoOperator.SimpleProvince> simpleProvinces) {
                    if (succeed) {
                        mProvinceDataList.clear();
                        List<ProvinceArea> provinceAreas = new ArrayList<ProvinceArea>();
                        provinceAreas.add(new ProvinceArea(UserInfoOperator.SimpleProvince.getDefaultOne(getActivity())));
                        for (UserInfoOperator.SimpleProvince p : simpleProvinces) {
                            provinceAreas.add(new ProvinceArea(p));
                        }
                        mProvinceDataList.addAll(provinceAreas);
                        mProvAdapter.notifyDataSetChanged();
                    }
                }
            });*/
        }

        setAdapterList(mAdapters);
        mProvAdapter.setOnAreaChooseListener(new OnAreaChooseListener() {
            @Override
            public void onChoose(CityArea area) {
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
                UserInfoOperator.getInstance().getProvinceStudio(area.getId(), new UserInfoOperator.OnGetListener<List<City>>() {
                    @Override
                    public void onGet(boolean succeed, List<City> cities) {
                        if (isDetached()) {
                            return;
                        }
                        if (succeed) {
                            mCityDataList.clear();
                            List<CityArea> cityAreas = new ArrayList<CityArea>();
                            int countAll = 0;
                            for (City c : cities) {
                                countAll += c.getStudioCount();
                                cityAreas.add(new CityArea(c));
                            }
                            CityArea all = mProvAdapter.getSelectedAreable().cloneUnselectedOne(getActivity());
                            //all.getmCity().setStudioCount(countAll);
                            cityAreas.add(0, all);

                            mCityDataList.addAll(cityAreas);
                            mCityAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        mCityAdapter.setOnAreaChooseListener(new OnAreaChooseListener() {
            @Override
            public void onChoose(CityArea area) {
                mCityId = area.getId();
                mTownId = 0;
                if ((mProvAdapter.getSelectedAreable() != null && area.getId() == mProvAdapter.getSelectedAreable().getId()) || area.getId() == 0) {
                    mTownDataList.clear();
                    mTownAdapter.notifyDataSetChanged();
                    onAreaSelected(area);
                    return;
                }
                mTownDataList.clear();
                mTownAdapter.notifyDataSetChanged();
                UserInfoOperator.getInstance().getProvinceStudio(area.getId(), new UserInfoOperator.OnGetListener<List<City>>() {
                    @Override
                    public void onGet(boolean succeed, List<City> cities) {
                        if (isDetached()) {
                            return;
                        }
                        if (succeed) {
                            mTownDataList.clear();
                            List<CityArea> townAreas = new ArrayList<CityArea>();
                            int countAll = 0;
                            for (City c : cities) {
                                countAll += c.getStudioCount();
                                CityArea a = new CityArea(c);
                                a.canExtend = false;
                                townAreas.add(a);
                            }
                            CityArea all = mCityAdapter.getSelectedAreable().cloneUnselectedOne(getActivity());
                            //all.getmCity().setStudioCount(countAll);
                            townAreas.add(0, all);

                            mTownDataList.addAll(townAreas);
                            mTownAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });

        mTownAdapter.setOnAreaChooseListener(new OnAreaChooseListener() {
            @Override
            public void onChoose(CityArea area) {
                mTownId = area.getId();
                onAreaSelected(area);
            }
        });

    }

    public void setOnPlaceChooseListener (OnPlaceChooseListener listener) {
        mFragmentAreaListener = listener;
    }

    public static interface OnPlaceChooseListener{
        public void onChoose (CityArea areable, int provinceId, int cityId, int townId);
    }

    public void onAreaSelected (CityArea areable) {
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

        private List<CityArea> mDataList = null;
        private OnAreaChooseListener mAreaListener = null;

        private CityArea mSelectedAreable = null;

        public AreaAdapter (List<CityArea> list) {
            mDataList = list;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public CityArea getItem(int i) {
            return mDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_area_item, null);
            TextView tv = (TextView)view.findViewById(R.id.area_item_name);
            TextView countTv = (TextView)view.findViewById(R.id.area_item_count);

            CityArea areable = getItem(i);
            if (!TextUtils.isEmpty(areable.getShowName())) {
                tv.setText(areable.getShowName());
            } else {
                tv.setText(areable.getTitle());
            }

            City city = areable.mCity;
            final int count = city.getStudioCount();
            if (count > 0) {
                countTv.setText(city.getStudioCount() + "");

                if (areable.canExtend) {
                    countTv.setBackgroundResource(R.drawable.number_bg_arrow);
                } else {
                    countTv.setBackgroundResource(R.drawable.number_bg);
                }
            } else {
                countTv.setText("");
                countTv.setBackgroundDrawable(null);
            }

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

        public CityArea getSelectedAreable () {
            return mSelectedAreable;
        }

        public void setOnAreaChooseListener (OnAreaChooseListener listener) {
            mAreaListener = listener;
        }

    }

    public static interface OnAreaChooseListener {
        public void onChoose (CityArea areable);
    }

    /*private class ProvinceArea implements Areable {

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
    }*/

    public class CityArea implements Areable {

        private City mCity = null;

        private boolean isSelected = false;

        private String showName = null;

        private boolean canExtend = true;

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

        public CityArea cloneUnselectedOne (Context context) {
            City city = new City();
            city.setStudioCount(mCity.getStudioCount());
            city.setCodeid(0);
            city.setCityName(mCity.getCityName());
            CityArea area = new CityArea(city);
            area.setShowName(context.getString(R.string.all));
            area.canExtend = false;
            return area;
        }

        /*public CityArea makeParentOne (Context context) {
            City city = new City();
            city.setCodeid(mCity.getParentid());
            city.setCityName(context.getString(R.string.all));
            CityArea area = new CityArea(city);
            area.setShowName(mCity.getCityName());
            return area;
        }*/

        public City getCity () {
            return mCity;
        }

        public City getmCity() {
            return mCity;
        }

        public void setmCity(City mCity) {
            this.mCity = mCity;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }
    }

    public static interface Areable {
        public String getTitle ();
        public int getId ();
        public boolean isSelected ();
        public void setSelected (boolean selected);
        public Areable cloneUnselectedOne (Context context);
    }
}
