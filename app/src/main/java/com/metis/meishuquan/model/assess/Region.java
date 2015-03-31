package com.metis.meishuquan.model.assess;

import java.util.List;

/**
 * Created by WJ on 2015/3/31.
 */
public class Region {
    private int provinceId;

    private List<City> cityLists ;

    private String name;

    private boolean hotCity;

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public List<City> getCityLists() {
        return cityLists;
    }

    public void setCityLists(List<City> cityLists) {
        this.cityLists = cityLists;
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
}
