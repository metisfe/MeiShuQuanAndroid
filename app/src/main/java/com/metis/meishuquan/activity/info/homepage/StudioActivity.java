package com.metis.meishuquan.activity.info.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.activity.info.TextActivity;
import com.metis.meishuquan.fragment.commons.StudioFragment;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.util.ImageLoaderUtils;

public class StudioActivity extends BaseActivity implements
        StudioFragment.OnMenuItemClickListener,
        RadioGroup.OnCheckedChangeListener{

    private static final String TAG = StudioActivity.class.getSimpleName();

    public static final String KEY_USER_ID = User.KEY_USER_ID,
                                KEY_USER_ROLE = User.KEY_USER_ROLE;

    private View mTitleView = null;
    private ImageView mTitleProfile = null;

    private StudioFragment mStudioFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio);

        mTitleView = LayoutInflater.from(this).inflate(R.layout.layout_studio_title, null);
        getTitleView().setCenterView(mTitleView);
        mTitleProfile = (ImageView)mTitleView.findViewById(R.id.studio_title_profile);
        ImageLoaderUtils.getImageLoader(this).displayImage("http://images.apple.com/cn/live/2015-mar-event/images/751591e0653867230e700d3a99157780826cce88_xlarge.jpg",
                mTitleProfile,
                ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.studio_profile_size)));

        mStudioFragment = (StudioFragment)getSupportFragmentManager().findFragmentById(R.id.studio_fragment);
        mStudioFragment.setOnMenuItemClickListener(this);

        String userRoleStr = getIntent().getStringExtra(KEY_USER_ROLE);
        if ("studio".equals(userRoleStr)) {
            mStudioFragment.setTabTitle(
                    R.string.studio_tab_top_line,
                    R.string.studio_tab_glory,
                    R.string.studio_tab_works);
        } else {
            mStudioFragment.setTabTitle(
                    R.string.studio_tab_daily,
                    R.string.studio_tab_album,
                    R.string.studio_tab_info_details
            );
        }
        mStudioFragment.setOnCheckedChangeListener(this);

    }

    @Override
    public void onMenuItemClick(StudioFragment.MenuItem item, int position) {
        switch (item.id) {
            case R.id.studio_menu_introduce:
                break;
            case R.id.studio_menu_album:
                startActivity(new Intent(this, StudioAlbumActivity.class));
                break;
            case R.id.studio_menu_team:
                startActivity(new Intent(this, TeacherTeamActivity.class));
                break;
            case R.id.studio_menu_course_arrangement:
                startActivity(new Intent(this, CourseArrangementActivity.class));
                break;
            case R.id.studio_menu_video:
                startActivity(new Intent(this, VideoListActivity.class));
                break;
            case R.id.studio_menu_charge:
                startActivity(new Intent(this, ChargeActivity.class));
                break;
            case R.id.studio_menu_book_publish:
                startActivity(new Intent(this, BookListActivity.class));
                break;
            case R.id.studio_menu_contact_us:
                //TODO
                startActivity(new Intent(this, TextActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.studio_list_header_tab1:
                mStudioFragment.setAdapter(new NewAdapter());
                break;
            case R.id.studio_list_header_tab2:
                mStudioFragment.setAdapter(new MyAdapter());
                break;
            case R.id.studio_list_header_tab3:
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 200;
        }

        @Override
        public String getItem(int position) {
            return position + "";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(StudioActivity.this);
            tv.setText(getItem(position));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return tv;
        }
    }

    class NewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 200;
        }

        @Override
        public String getItem(int position) {
            return position + " new";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(StudioActivity.this);
            tv.setText(getItem(position));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return tv;
        }
    }
}
