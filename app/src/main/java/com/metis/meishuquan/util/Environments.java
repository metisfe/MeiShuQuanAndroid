package com.metis.meishuquan.util;

import com.metis.meishuquan.model.provider.ApiDataProvider;

import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by wudi on 3/15/2015.
 */
public class Environments {
    // sina BingScore App
    public static String SINA_APP_KEY = "163300531";
    public static String SINA_APP_SECRET = "1a7ffe166de0382c69fe021434419548";
    public static String SINA_REDIRECT_URL = "http://cn.bing.com/yxl";

    public static String SINA_API_SERVER = "https://api.weibo.com/2";
    public static String SINA_OAUTH_SERVER = "https://api.weibo.com/oauth2";
    public static String SINA_OAUTH = SINA_OAUTH_SERVER + "/authorize?client_id=" + SINA_APP_KEY + "&response_type=code&redirect_uri=" + SINA_REDIRECT_URL
            + "&display=mobile";
    public static String SINA_ACCESS_TOKEN = SINA_OAUTH_SERVER + "/access_token?client_id=" + SINA_APP_KEY + "&client_secret=" + SINA_APP_SECRET
            + "&grant_type=authorization_code&redirect_uri=" + SINA_REDIRECT_URL + "&code=";

    // tencent BingScore App
    public static String TENCENT_APP_KEY = "801397398";
    public static String TENCENT_APP_SECRET = "3585b541a5624bd5b77c7841676fc5f1";
    public static String TENCENT_REDIRECT_URL = "http://cn.bing.com/yxl";

    public static String TENCENT_API_SERVER = "https://open.t.qq.com/api";
    public static String TENCENT_OAUTH_SERVER = "https://open.t.qq.com/cgi-bin/oauth2";
    public static String TENCENT_OAUTH = TENCENT_OAUTH_SERVER + "/authorize?client_id=" + TENCENT_APP_KEY + "&response_type=code&redirect_uri="
            + TENCENT_REDIRECT_URL;
    public static String TENCENT_ACCESS_TOKEN = TENCENT_OAUTH_SERVER + "/access_token?client_id=" + TENCENT_APP_KEY + "&client_secret=" + TENCENT_APP_SECRET
            + "&grant_type=authorization_code&redirect_uri=" + TENCENT_REDIRECT_URL + "&code=";

    public static String RENREN_APP_KEY = "TEST";

    public static String QZONE_APP_KEY = "TEST";

    public static String WECHAT_APP_ID = "wx2671a6fe4e4a83ba";

    // CDN URL
    public static final String CDNURL = "http://scp-social.s-msn.com/s/mobileapp/bingscore/";

    public static String AppDownloadLink = "http://www.bing.com/yingxiangli/app?mkt=zh-CN";

    private static HashMap<String, URL> snsSourceIcons = new HashMap<String, URL>();

    // recommend Apps filename
    public static final String RecommendAppsName = "recommendApps.xml";
    public static final String myRecommendAppsName = "myRecommendApps.xml";

    // version filename
    public static final String VersionsName = "versions.xml";

    // version filename
    public static final String ConfigurationName = "configurations.xml";
    public static final String myConfigurationName = "config.xml";
    public static final String Config = "config.properties";

    // pic no show mode choice
    public static boolean pic_noshow_mode = false;
    public static boolean is_remind = false;

    private static Environments instance = new Environments();

    private Environments()
    {
        putSNSSourceIcon("SNS.SourceIcon.SinaWeibo", "http://scp-social.s-msn.com/s/mobileapp/bingscore/sina_logo.png");
        putSNSSourceIcon("SNS.SourceIcon.TencentWeibo", "http://mat1.gtimg.com/app/opent/images/wiki/resource/weiboicon32.png");
        putSNSSourceIcon("SNS.SourceIcon.SohuVideo", "http://open.sohu.com/images/sohu_48.png");
        putSNSSourceIcon("SNS.SourceIcon.BingNews", "http://scp-social.s-msn.com/s/mobileapp/bingscore/bing_logo.png");
        putSNSSourceIcon("SNS.SourceIcon.BingImage", "http://scp-social.s-msn.com/s/mobileapp/bingscore/bing_logo.png");
    }

    public static Environments getInstance()
    {
        return instance;
    }

    public static void loadConfig(Properties prop)
    {
        SINA_APP_KEY = prop.getProperty("SNS.Sina.AppKey", SINA_APP_KEY);
        SINA_APP_SECRET = prop.getProperty("SNS.Sina.Secret", SINA_APP_SECRET);
        SINA_REDIRECT_URL = prop.getProperty("SNS.Sina.Callback", SINA_REDIRECT_URL);
        SINA_API_SERVER = prop.getProperty("SNS.Sina.ApiServer", SINA_API_SERVER);
        SINA_OAUTH_SERVER = prop.getProperty("SNS.Sina.OauthServer", SINA_OAUTH_SERVER);
        SINA_OAUTH = SINA_OAUTH_SERVER + "/authorize?client_id=" + SINA_APP_KEY + "&response_type=code&redirect_uri=" + SINA_REDIRECT_URL + "&display=mobile";
        SINA_ACCESS_TOKEN = SINA_OAUTH_SERVER + "/access_token?client_id=" + SINA_APP_KEY + "&client_secret=" + SINA_APP_SECRET
                + "&grant_type=authorization_code&redirect_uri=" + SINA_REDIRECT_URL + "&code=";

        TENCENT_APP_KEY = prop.getProperty("SNS.Tencent.AppKey", TENCENT_APP_KEY);
        TENCENT_APP_SECRET = prop.getProperty("SNS.Tencent.Secret", TENCENT_APP_SECRET);
        TENCENT_REDIRECT_URL = prop.getProperty("SNS.Tencent.Callback", TENCENT_REDIRECT_URL);
        TENCENT_API_SERVER = prop.getProperty("SNS.Tencent.ApiServer", TENCENT_API_SERVER);
        TENCENT_OAUTH_SERVER = prop.getProperty("SNS.Tencent.OauthServer", TENCENT_OAUTH_SERVER);
        TENCENT_OAUTH = TENCENT_OAUTH_SERVER + "/authorize?client_id=" + TENCENT_APP_KEY + "&response_type=code&redirect_uri=" + TENCENT_REDIRECT_URL;
        TENCENT_ACCESS_TOKEN = TENCENT_OAUTH_SERVER + "/access_token?client_id=" + TENCENT_APP_KEY + "&client_secret=" + TENCENT_APP_SECRET
                + "&grant_type=authorization_code&redirect_uri=" + TENCENT_REDIRECT_URL + "&code=";

        String apiFromCDN = prop.getProperty("BingScore.ApiServer", null);
        if (apiFromCDN != null)
        {
            ApiDataProvider.API_ROOT = apiFromCDN.replace("${IsSecure}", "");
        }

        for (Object source : prop.keySet())
        {
            String sourceString = source.toString();
            if (sourceString.startsWith("SNS.SourceIcon."))
            {
                putSNSSourceIcon(sourceString, prop.getProperty(sourceString, null));
            }
        }
    }

    public static URL getSNSSourceIcon(String key)
    {
        return snsSourceIcons.get("SNS.SourceIcon." + key);
    }

    private static void putSNSSourceIcon(String key, String value)
    {
        if (key == null || key.length() == 0 || value == null || value.length() == 0)
        {
            return;
        }

        URL url = ContractUtility.stringToUrl(value);
        if (url == null)
        {
            return;
        }

        snsSourceIcons.put(key, url);
    }

    private boolean isUseStub = false;

    public boolean isUseStub()
    {
        return this.isUseStub;
    }

    public void setIsUseStub(boolean isUseStub)
    {
        this.isUseStub = isUseStub;
    }

}
