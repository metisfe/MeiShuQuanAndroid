package com.metis.meishuquan.model.BLL;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/6.
 */
public class StudioBaseInfo implements Serializable {

    public static final String KEY_STUDIO_ID = "studio_id";

    private int studioId;
    private String studioDesc;
    private String backgroundImg;
    private String studioShowImg1;
    private String studioShowImg2;
    private String studioShowImg3;
    private String studioShowImg4;
    private String address;
    private String telephone;
    private String studioContact;
    private String microblog;
    private String weChat;
    private String webSite;
    private String addressPhoto;

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(int studioId) {
        this.studioId = studioId;
    }

    public String getStudioDesc() {
        return studioDesc;
    }

    public void setStudioDesc(String studioDesc) {
        this.studioDesc = studioDesc;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public String getStudioShowImg1() {
        return studioShowImg1;
    }

    public void setStudioShowImg1(String studioShowImg1) {
        this.studioShowImg1 = studioShowImg1;
    }

    public String getStudioShowImg2() {
        return studioShowImg2;
    }

    public void setStudioShowImg2(String studioShowImg2) {
        this.studioShowImg2 = studioShowImg2;
    }

    public String getStudioShowImg3() {
        return studioShowImg3;
    }

    public void setStudioShowImg3(String studioShowImg3) {
        this.studioShowImg3 = studioShowImg3;
    }

    public String getStudioShowImg4() {
        return studioShowImg4;
    }

    public void setStudioShowImg4(String studioShowImg4) {
        this.studioShowImg4 = studioShowImg4;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getStudioContact() {
        return studioContact;
    }

    public void setStudioContact(String studioContact) {
        this.studioContact = studioContact;
    }

    public String getMicroblog() {
        return microblog;
    }

    public void setMicroblog(String microblog) {
        this.microblog = microblog;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getAddressPhoto() {
        return addressPhoto;
    }

    public void setAddressPhoto(String addressPhoto) {
        this.addressPhoto = addressPhoto;
    }

    public List<String> getImageList () {
        List<String> list = new ArrayList<String>();
        if (!TextUtils.isEmpty(studioShowImg1)) {
            list.add(studioShowImg1);
        }
        if (!TextUtils.isEmpty(studioShowImg2)) {
            list.add(studioShowImg2);
        }
        if (!TextUtils.isEmpty(studioShowImg3)) {
            list.add(studioShowImg3);
        }
        if (!TextUtils.isEmpty(studioShowImg4)) {
            list.add(studioShowImg4);
        }
        return list;
    }
}
