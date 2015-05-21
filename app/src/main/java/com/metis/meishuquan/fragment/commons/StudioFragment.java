package com.metis.meishuquan.fragment.commons;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.model.BLL.StudioBaseInfo;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

/**
 * Created by WJ on 2015/4/23.
 */
public class StudioFragment extends Fragment{

    private static final String TAG = StudioFragment.class.getSimpleName();

    private ListView mListView = null;

    private View mHeaderView = null;
    private ImageView mHeadCoverIv = null;
    private TextView mHeaderExtraTv = null;
    private Button mHeaderMsgBtn = null;

    private BaseAdapter mAdapter = null;

    private MenuItem[] mMenuItems = {
            new MenuItem(R.id.studio_menu_introduce, R.drawable.studio_introduce, R.string.studio_introduce),
            new MenuItem(R.id.studio_menu_album, R.drawable.studio_album, R.string.studio_album),
            new MenuItem(R.id.studio_menu_team, R.drawable.studio_teacher_team, R.string.studio_team),
            new MenuItem(R.id.studio_menu_course_arrangement, R.drawable.studio_course_arrangement, R.string.studio_course_arrangement),
            new MenuItem(R.id.studio_menu_video, R.drawable.studio_video, R.string.studio_video),
            new MenuItem(R.id.studio_menu_charge, R.drawable.studio_charge, R.string.studio_charge),
            new MenuItem(R.id.studio_menu_book_publish, R.drawable.studio_book_publish, R.string.studio_book_publish),
            new MenuItem(R.id.studio_menu_contact_us, R.drawable.studio_contact_us, R.string.studio_contact_us),
    };

    private OnMenuItemClickListener mMenuClick = null;
    private RadioGroup.OnCheckedChangeListener mCheckChangeListener = null;
    private OnNextPageListener mNextListener = null;

    private String mTitle1, mTitle2, mTitle3;

    private GridLayout mMenuLayout = null;
    private TextView mIntroduceTv = null;
    private RadioGroup mRadioGroup;
    private RadioButton mBtn1, mBtn2, mBtn3;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_studio, null, true);
    }

    public RadioGroup getRadioGroup () {
        return mRadioGroup;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView)view.findViewById(R.id.studio_list_view);
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_studio_list_header, null);
        mHeadCoverIv = (ImageView)mHeaderView.findViewById(R.id.studio_list_header_cover);
        mHeaderExtraTv = (TextView)mHeaderView.findViewById(R.id.studio_list_header_extras);
        mHeaderMsgBtn = (Button)mHeaderView.findViewById(R.id.studio_list_header_msg);
        setAdapter(mAdapter);

        mIntroduceTv = (TextView)mHeaderView.findViewById(R.id.studio_list_header_self_introduce);
        mMenuLayout = (GridLayout)mHeaderView.findViewById(R.id.studio_list_header_menu);

        mRadioGroup = (RadioGroup)view.findViewById(R.id.studio_list_header_tab_container);
        mBtn1 = (RadioButton)view.findViewById(R.id.studio_list_header_tab1);
        mBtn2 = (RadioButton)view.findViewById(R.id.studio_list_header_tab2);
        mBtn3 = (RadioButton)view.findViewById(R.id.studio_list_header_tab3);
        setTabTitle(mTitle1, mTitle2, mTitle3);
        setOnCheckedChangeListener(mCheckChangeListener);
        mRadioGroup.check(R.id.studio_list_header_tab1);
        //fillHeader();
        //fillHeader(null);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        Log.v(TAG, "onScrollStateChanged SCROLL_STATE_IDLE");
                        if (mListView.getLastVisiblePosition() >= absListView.getChildCount() - 1) {
                            if (mNextListener != null) {
                                mNextListener.onLoadNextPage();
                            }
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        Log.v(TAG, "onScrollStateChanged SCROLL_STATE_FLING");
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        Log.v(TAG, "onScrollStateChanged SCROLL_STATE_TOUCH_SCROLL");
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    public void setOnCoverLongClickListener (View.OnLongClickListener listener) {
        mHeadCoverIv.setOnLongClickListener(listener);
    }

    public void setCover (String path) {
        ImageLoaderUtils.getImageLoader(getActivity()).displayImage(ImageDownloader.Scheme.FILE.wrap(path), mHeadCoverIv, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher));
    }

    public void setUser (User user) {
        fillHeader(user);
    }

    public void setStudioBaseInfo (StudioBaseInfo info) {
        fillStudioInfo(info);
    }

    private void fillStudioInfo (StudioBaseInfo info) {
        ImageLoaderUtils.getImageLoader(getActivity()).displayImage(
                info.getBackgroundImg(), mHeadCoverIv, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher)
        );
    }

    private void fillHeader (User user) {
        if (isDetached()) {
            return;
        }
        /*if (user == null) {
            mHeaderView.setVisibility(View.GONE);
            return;
        }*/
        //TODO

        if (user.getUserRoleEnum() == IdTypeEnum.STUDIO) {
            mMenuLayout.setVisibility(View.VISIBLE);
            mIntroduceTv.setVisibility(View.GONE);
        } else {
            mMenuLayout.setVisibility(View.GONE);
            mIntroduceTv.setVisibility(View.VISIBLE);
        }
        if(user.getUserId() == MainApplication.userInfo.getUserId()) {
            mHeaderMsgBtn.setVisibility(View.GONE);
        }
        String coverUrl = user.getBackgroundImg();
        if (coverUrl == null) {
            coverUrl = user.getBackGroundImg();
        }
        ImageLoaderUtils.getImageLoader(getActivity()).displayImage(
                coverUrl, mHeadCoverIv,
                ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher)
        );
        mHeaderExtraTv.setText(MainApplication.UIContext.getString(R.string.studio_fans_and_focus, user.getFansNum(), user.getFocusNum()));
        for (int i = 0; i < mMenuItems.length; i++) {
            final int index = i;
            final MenuItem item = mMenuItems[i];
            View view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.layout_studio_menu_item, null);
            TextView title = (TextView)view.findViewById(R.id.menu_item_text);
            ImageView icon = (ImageView)view.findViewById(R.id.menu_item_icon);
            title.setText(item.textRes);
            icon.setImageResource(item.iconRes);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMenuClick != null) {
                        mMenuClick.onMenuItemClick(item, index);
                    }
                }
            });
            mMenuLayout.addView(view);
        }

    }

    public void setTabTitle (String title1, String title2, String title3) {
        mTitle1 = title1;
        mTitle2 = title2;
        mTitle3 = title3;
        if (mBtn1 != null) {
            mBtn1.setText(title1);
        }
        if (mBtn2 != null) {
            mBtn2.setText(title2);
        }
        if (mBtn3 != null) {
            mBtn3.setText(title3);
        }
    }

    public void setTabTitle (int titleRes1, int titleRes2, int titleRes3) {
        setTabTitle(
                MainApplication.UIContext.getString(titleRes1),
                MainApplication.UIContext.getString(titleRes2),
                MainApplication.UIContext.getString(titleRes3));
    }

    public void setSelfIntroduce (String introduce) {
        if (mIntroduceTv != null) {
            mIntroduceTv.setText(introduce);
        }
    }

    public int getCheckTabId () {
        return mRadioGroup.getCheckedRadioButtonId();
    }

    public void setOnCheckedChangeListener (RadioGroup.OnCheckedChangeListener listener) {
        mCheckChangeListener = listener;
        if (mRadioGroup != null) {
            mRadioGroup.setOnCheckedChangeListener(mCheckChangeListener);
        }
    }

    public void setAdapter (BaseAdapter adapter) {
        mAdapter = adapter;
        if (mListView != null) {
            if (mListView.getHeaderViewsCount() <= 0) {
                mListView.addHeaderView(mHeaderView, null, false);
            }
            if (mAdapter == null) {
                mAdapter = EmptyAdapter.getInstance(getActivity());
            }

            mListView.setAdapter(mAdapter);

        }
    }

    public void setOnMenuItemClickListener (OnMenuItemClickListener listener) {
        mMenuClick = listener;
    }

    public ListView getListView () {
        return mListView;
    }

    public static interface OnMenuItemClickListener {
        public void onMenuItemClick (MenuItem item, int position);
    }

    public static class MenuItem {
        public int id;
        public int iconRes;
        public int textRes;

        public MenuItem (int id, int icon, int textRes) {
            this.id = id;
            this.iconRes = icon;
            this.textRes = textRes;
        }

    }

    public static class EmptyAdapter extends BaseAdapter {

        private Context mContext = null;
        private static EmptyAdapter sAdapter = null;

        public static EmptyAdapter getInstance (Context context) {
            if (sAdapter == null) {
                sAdapter = new EmptyAdapter(context.getApplicationContext());
            }
            return sAdapter;
        }

        public EmptyAdapter (Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public String getItem(int position) {
            return mContext.getString(R.string.studio_empty_data_set);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(mContext);
            tv.setText(getItem(position));
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(mContext.getResources().getColor(android.R.color.black));
            return tv;
        }
    }

    public void setOnNextPageListener (OnNextPageListener listener) {
        mNextListener = listener;
    }

    public static interface OnNextPageListener {
        public void onLoadNextPage ();
    }

}
