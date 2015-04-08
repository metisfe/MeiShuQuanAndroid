package com.metis.meishuquan.model.assess;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO:省
 * Created by wj on 15/4/1.
 */
public class Province {
    private int provinceId;

    private List<City> cityList =null;

    private String name;

    private boolean hotCity;

    private String groupName="";

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public List<City> getCityList() {
        if (cityList==null){
            cityList=new ArrayList<City>();
        }
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHotCity() {
        return hotCity;
    }

    public void setHotCity(boolean hotCity) {
        this.hotCity = hotCity;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
