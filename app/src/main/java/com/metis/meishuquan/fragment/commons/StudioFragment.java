package com.metis.meishuquan.fragment.commons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.model.commons.User;

/**
 * Created by WJ on 2015/4/23.
 */
public class StudioFragment extends Fragment{

    private ListView mListView = null;

    private View mHeaderView = null;

    private BaseAdapter mAdapter = null;

    private MenuItem[] mMenuItems = {
            new MenuItem(R.id.studio_menu_introduce, R.drawable.ic_launcher, R.string.studio_introduce),
            new MenuItem(R.id.studio_menu_album, R.drawable.ic_launcher, R.string.studio_album),
            new MenuItem(R.id.studio_menu_team, R.drawable.ic_launcher, R.string.studio_team),
            new MenuItem(R.id.studio_menu_course_arrangement, R.drawable.ic_launcher, R.string.studio_course_arrangement),
            new MenuItem(R.id.studio_menu_video, R.drawable.ic_launcher, R.string.studio_video),
            new MenuItem(R.id.studio_menu_charge, R.drawable.ic_launcher, R.string.studio_charge),
            new MenuItem(R.id.studio_menu_book_publish, R.drawable.ic_launcher, R.string.studio_book_publish),
            new MenuItem(R.id.studio_menu_contact_us, R.drawable.ic_launcher, R.string.studio_contact_us),
    };

    private OnMenuItemClickListener mMenuClick = null;
    private RadioGroup.OnCheckedChangeListener mCheckChangeListener = null;

    private String mTitle1, mTitle2, mTitle3;

    private RadioGroup mRadioGroup;
    private RadioButton mBtn1, mBtn2, mBtn3;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_studio, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView)view.findViewById(R.id.studio_list_view);
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_studio_list_header, null);
        setAdapter(mAdapter);

        mRadioGroup = (RadioGroup)view.findViewById(R.id.studio_list_header_tab_container);
        mBtn1 = (RadioButton)view.findViewById(R.id.studio_list_header_tab1);
        mBtn2 = (RadioButton)view.findViewById(R.id.studio_list_header_tab2);
        mBtn3 = (RadioButton)view.findViewById(R.id.studio_list_header_tab3);
        setTabTitle(mTitle1, mTitle2, mTitle3);
        setOnCheckedChangeListener(mCheckChangeListener);
        mRadioGroup.check(R.id.studio_list_header_tab1);
        //fillHeader();
        fillHeader(null);
    }

    private void fillHeader (User user) {
        GridLayout gridLayout = (GridLayout)mHeaderView.findViewById(R.id.studio_list_header_menu);
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
            gridLayout.addView(view);
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
        setTabTitle(getString(titleRes1), getString(titleRes2), getString(titleRes3));
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
            if (mAdapter == null) {
                mAdapter = new EmptyAdapter();
            }

            mListView.setAdapter(mAdapter);

            if (mListView.getHeaderViewsCount() <= 0) {
                mListView.addHeaderView(mHeaderView, null, false);
            }
        }
    }

    public void setOnMenuItemClickListener (OnMenuItemClickListener listener) {
        mMenuClick = listener;
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

    public class EmptyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public String getItem(int position) {
            return getString(R.string.studio_empty_data_set);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(getActivity());
            tv.setText(getItem(position));
            return tv;
        }
    }

}
