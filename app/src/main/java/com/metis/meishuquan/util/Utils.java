package com.metis.meishuquan.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.framework.util.TextureRender;
import com.metis.meishuquan.view.shared.BaseDialog;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;

/**
 * Created by wudi on 3/15/2015.
 */
public class Utils {
    public static final String IMAGE_DOWNLOAD_DIR = MainApplication.Resources.getString(R.string.app_name);
    public static final String CLIENT_SOURCE = "MobileApp";

    public static final String SOURCE_SINA_WEIBO_ID = "SinaWeibo";
    public static final String SOURCE_TECENT_WEIBO_ID = "TencentWeibo";
    public static final String SOURCE_BING_ID = "Bing";
    public static final String SOURCE_RENLIFANG_ID = "Renlifang";
    public static final String SOURCE_BAIDU_ID = "Baidu";
    public static final String SOURCE_MSN_ID = "Msn";

    public static final int CATEGORY_ITEM_USER_PAGE = 0;
    public static final int CATEGORY_ITEM_TIMELINE_PAGE = 1;
    public static final int CELEBRITY_ITEM_TIMELINE_PAGE = 0;
    public static final int CELEBRITY_ITEM_NAVIGATE_PAGE = 1;
    public static final int CELEBRITY_ITEM_IMAGES_PAGE = 2;
    public static final int CELEBRITY_ITEM_RESUME_PAGE = 3;
    public static final String CATEGORY_HOT_PEOPLE = "BingScore_HotPeople";
    public static final String CATEGORY_TYPE_ALL = "All";
    public static final String CATEGORY_TYPE_BUSINESS = "Business";
    public static final String CATEGORY_TYPE_ENTERTAINMENT = "Entertainment";
    public static final String CATEGORY_TYPE_SCIENCEANDTECH = "ScienceAndTech";
    public static final String CATEGORY_TYPE_SPORTS = "Sports";
    public static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'-00:00'";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DAY_FORMAT = "yyyy-MM-dd";

    public static final long MINUTE_IN_MILISECOND = 60 * 1000;
    public static final long HOUR_IN_MILISECOND = 60 * 60 * 1000;
    public static final long DAY_IN_MILISECOND = 24 * 60 * 60 * 1000;

    public static final int MAX_TOP_CELEBRITIES_PAGE_GRID_NUM = 16;
    public static final int MIN_SELECTED_FOLLOWEE = 3;

    public static SimpleDateFormat DateFormatter = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS", Locale.US);

    public static String getDisplayTime(String timeString) {
        return getDateFromNow(getDate(timeString, null));
    }

    public static Date getDate(String dateString, Date defaultValue) {
        Date result = defaultValue;
        try {
            if (dateString != null) {
                result = DateFormatter.parse(dateString.replace("T", "").replace("Z", ""));
            }
        } catch (ParseException ex) {
            result = defaultValue;
        }

        return result;
    }

    public static String getDateFromNow(Date date) {
        if (date == null) {
            return "";
        }

        Date nowDate = new Date();
        long nowTime = nowDate.getTime();
        long dateTime = date.getTime();
        long diffTime = nowTime - dateTime;
        if (diffTime <= MINUTE_IN_MILISECOND) {
            return "刚刚";
        } else if (diffTime < HOUR_IN_MILISECOND) {
            int minutes = (int) (diffTime / MINUTE_IN_MILISECOND);
            return String.format("%d分钟以前", minutes);
        } else if (diffTime < DAY_IN_MILISECOND) {
            int hours = (int) (diffTime / HOUR_IN_MILISECOND);
            if (hours == 1) {
                return "1小时之前";
            } else {
                return String.format("%d 小时之前", hours);
            }
        } else {
            SimpleDateFormat outputFmt = new SimpleDateFormat(DAY_FORMAT);
            return outputFmt.format(date);
        }
    }

    public static String getTimeFromDate(Date date) {
        SimpleDateFormat outputFmt = new SimpleDateFormat(DATE_FORMAT);
        return outputFmt.format(date);
    }

    public static String decodeImageUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }

        // we should not encode url as :
        // http://www.bing.com/imagenewsfetcher.aspx?q=http%3a%2f%2fgx.people.com.cn%2fNMediaFile%2f2013%2f0724%2fLOCAL201307240808000165861116094.jpg&id=CBCA74FC0B7AA0F5C66E1CC96153CB86
        if (url.indexOf("http", 4) == -1 && url.indexOf("%", 4) == -1) {
            try {
                URL u = new URL(url);
                URI i = new URI(u.getProtocol(), u.getUserInfo(), u.getHost(), u.getPort(), u.getPath(), u.getQuery(), u.getRef());
                u = i.toURL();
                url = i.toString();
                Log.d("TextureRender", "after encode url==" + url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    public static String getImageSuffix(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return "";
        }

        int suffixIndex = imageUrl.lastIndexOf(".");
        if (suffixIndex != -1 && imageUrl.length() - suffixIndex < 6) {
            return imageUrl.substring(suffixIndex);
        } else {
            return ".png";
        }
    }

    public static boolean isPathExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }

        file.mkdirs();
        if (file.exists()) {
            return true;
        }

        return false;
    }

    public static String getDownloadImageDir() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + IMAGE_DOWNLOAD_DIR;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return path;
    }

    public static int getTopCelebritiesPages(int size) {
        int pages = size / MAX_TOP_CELEBRITIES_PAGE_GRID_NUM;
        int mod = size % MAX_TOP_CELEBRITIES_PAGE_GRID_NUM;
        if (mod > 0) {
            pages++;
        }
        return pages;
    }

    public static void alertMessageDialog(String title, String desc) {
        BaseDialog.Builder builder = new BaseDialog.Builder(MainApplication.MainActivity);
        builder.setTitle(title);
        builder.setMessage(desc);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });

        builder.create().show();
    }

    public static String getVersion(Context icontext) {
        PackageManager pm = icontext.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = pm.getPackageInfo(icontext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    public static boolean showConfigureNetwork(final Context icontext) {
        if (!SystemUtil.isNetworkAvailable(icontext)) {
            BaseDialog.Builder builder = new BaseDialog.Builder(icontext);
            builder.setTitle("Network Tip");
            builder.setMessage("Network Unavailable, go to setting?");
            builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    icontext.startActivity(intent);
                    arg0.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    arg0.dismiss();
                }
            });
            builder.create().show();

            return false;
        } else
            return true;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static int[] getLayoutParamsForHeroImage() {
        DisplayMetrics dm = MainApplication.getDisplayMetrics();
        return new int[]{dm.widthPixels, (int) (dm.widthPixels / 2.2)};
    }

    public static void setSnsLogo(ImageView imageView, URL sourceUrl) {
        if (sourceUrl != null) {
            imageView.setVisibility(View.VISIBLE);
            TextureRender.getInstance().setBitmap(sourceUrl, imageView, R.color.transparent);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getStringFromPosition(int position) {
        int minutes = position / 1000 / 60;
        int seconds = position / 1000 % 60;
        return String.format(Locale.US, "%d:%02d", minutes, seconds);
    }

    public static String getImageSavePath(String imageUrl) {
        String suffix = Utils.getImageSuffix(imageUrl);
        imageUrl = Utils.decodeImageUrl(imageUrl);
        return Utils.getDownloadImageDir() + File.separator + SystemUtil.hashKeyForDisk(imageUrl) + suffix;
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd HH:mm:ss");
    }

    public static void showInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideInputMethod(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static String toPinYinStringWithPrefix(String word) {
        if (TextUtils.isEmpty(word)) {
            return "~";
        }

        String ret = "";
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        String[] pinyinArray = null;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            try {
                if (c >= 0x4e00 && c <= 0x9fa5) {
                    pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (pinyinArray != null && pinyinArray.length > 0) {
                        ret += pinyinArray[0];
                    }
                } else if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9') {
                    ret += c;
                }
            } catch (Exception e) {
            }
        }

        ret = ret.toLowerCase();
        if (ret.length() == 0)
            return "~";
        if (ret.charAt(0) >= 'a' && ret.charAt(0) <= 'z')
            return ret;
        return "~" + ret;
    }

    public static String toPinYinString(String word) {
        if (TextUtils.isEmpty(word)) {
            return "";
        }

        String ret = "";
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        String[] pinyinArray = null;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            try {
                if (c >= 0x4e00 && c <= 0x9fa5) {
                    pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (pinyinArray != null && pinyinArray.length > 0) {
                        ret += pinyinArray[0];
                    }
                } else if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9') {
                    ret += c;
                }
            } catch (Exception e) {
            }
        }

        return ret.toLowerCase();
    }

    public static String getIpAddress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }
}