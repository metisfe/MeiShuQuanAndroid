package com.metis.meishuquan.activity.info;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.Topline.ItemInfoFragment;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.topline.NewsInfo;
import com.metis.meishuquan.model.topline.TopLineNewsInfo;
import com.metis.meishuquan.util.ImageLoaderUtils;

public class MyFavoriteDetailActivity extends BaseActivity {

    private static final String TAG = MyFavoriteDetailActivity.class.getSimpleName();

    public static final String KEY_ITEM_ID = "item_id";

    private ItemInfoFragment mItemInfoFragment = null;

    private int mItemId = -1;

    private View mHeaderView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite_detail);

        mItemInfoFragment =
                (ItemInfoFragment)getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.layout_studio_title, null);
        getTitleView().setCenterView(mHeaderView);

        mItemId = getIntent().getIntExtra(KEY_ITEM_ID, mItemId);
        Log.v(TAG, "mItemId " + mItemId);
        mItemInfoFragment.setTitleBarVisible(false);
        mItemInfoFragment.getInfoData(mItemId, new UserInfoOperator.OnGetListener<TopLineNewsInfo>() {
            @Override
            public void onGet(boolean succeed, TopLineNewsInfo topLineNewsInfo) {
                if (!succeed) {
                    return;
                }
                ImageView iv = (ImageView)mHeaderView.findViewById(R.id.studio_title_profile);
                TextView titleTv = (TextView)mHeaderView.findViewById(R.id.studio_title_name);
                TextView idTv = (TextView)mHeaderView.findViewById(R.id.studio_title_meishuquan_id);
                NewsInfo info = topLineNewsInfo.getData();
                /*ImageLoaderUtils.getImageLoader(MyFavoriteDetailActivity.this)
                        .displayImage(info.get);*/
                titleTv.setText(info.getAuthor());
                idTv.setText(info.getModifyTime());
            }
        });

    }

}
