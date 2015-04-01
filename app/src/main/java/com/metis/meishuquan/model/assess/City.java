package com.metis.meishuquan.model.assess;

/**
 * POJO:City
 * <p/>
 * Created by WJ on 2015/3/31.
 */
public class City {
    private int codeid;

    private int parentid;

    private String cityName;

    private boolean hotCity;

    private String groupName;

    public int getCodeid() {
        return codeid;
    }

    public void setCodeid(int codeid) {
        this.codeid = codeid;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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
