package com.metis.meishuquan.activity.topline;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.Topline.ItemInfoFragment;
import com.metis.meishuquan.fragment.login.LoginFragment;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.TopLineNewsInfo;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

/**
 * 新闻详情
 */
public class NewsInfoActity extends FragmentActivity {

    public static final String KEY_NEWSID = "NEWS_ID";
    public static final String KEY_SHARE_IMG_URL = "share_image_url";

    //头部导航条
    private Button btnBack;
    private ImageView imgUserHead;
    private TextView tvTitle;

    //内容
    private ListView listView;

    //HeaderView
    private View headerView;
    private TextView tvNewsTitle, tvPublishTime;

    //FooterView


    //底部操作条
    private RelativeLayout rl_writeCommont, rl_commontList, rl_private, rl_share;
    private ImageView imgPrivate;//收藏

    private int newsId;
    private String shareImageUrl;
    private TopLineNewsInfo newsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info_actity);

        //接收参数
        if (getIntent().getExtras() != null) {
            newsId = getIntent().getExtras().getInt(KEY_NEWSID);
            getInfoData(newsId, null);
        }

        initView();
        initHeaderView();
        initEvent();
    }

    //初始化控件
    private void initView() {
        this.btnBack = (Button) this.findViewById(R.id.id_btn_back);
        this.imgUserHead = (ImageView) this.findViewById(R.id.id_img_dynamic);
        this.tvTitle = (TextView) this.findViewById(R.id.id_tv_source);
        this.listView = (ListView) this.findViewById(R.id.id_listview_news_info);
    }

    //初始化HeaderView并添加至listView
    private void initHeaderView() {
        this.headerView = LayoutInflater.from(this).inflate(R.layout.layout_news_info_activity_headerview, null, false);
        this.tvNewsTitle = (TextView) headerView.findViewById(R.id.id_title);
        this.tvPublishTime = (TextView) headerView.findViewById(R.id.id_tv_create_time);

        this.listView.addHeaderView(headerView);
        //绑定数据
        bindHeaderViewData();
    }

    //绑定headerView数据
    private void bindHeaderViewData() {
        if (newsInfo != null) {
            this.tvNewsTitle.setText(newsInfo.getData().getTitle());
            this.tvPublishTime.setText(newsInfo.getData().getModifyTime());
        }
    }

    //初始化事件
    private void initEvent() {

    }

    //获取新闻详情
    public void getInfoData(final int newsId, final UserInfoOperator.OnGetListener<TopLineNewsInfo> listener) {
        TopLineOperator.getInstance().getNewsInfoById(newsId, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result != null) {
                    if (result.getInfo().equals(String.valueOf(0))) {
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        newsInfo = gson.fromJson(json, new TypeToken<TopLineNewsInfo>() {
                        }.getType());

                        if (newsInfo.getData() == null) {
                            Toast.makeText(NewsInfoActity.this, "相关新闻不存在!", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        if (!TextUtils.isEmpty(json)) {
                            newsInfo = gson.fromJson(json, new TypeToken<TopLineNewsInfo>() {
                            }.getType());
                            //Log.v(TAG, "getNewsInfoById result=" + newsInfo.getData().getUserMark().isFavorite());
                            if (listener != null) {
                                listener.onGet(true, newsInfo);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 新闻详情列表适配器
     */
    class NewsInfoAdapter extends BaseAdapter {

        private TopLineNewsInfo newsInfo;

        public NewsInfoAdapter(TopLineNewsInfo newsInfo) {
            if (newsInfo == null) {
                newsInfo = new TopLineNewsInfo();
            }
            this.newsInfo = newsInfo;
        }

        public void setNewsInfo(TopLineNewsInfo newsInfo) {
            this.newsInfo = newsInfo;
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
