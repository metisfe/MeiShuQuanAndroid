package com.metis.meishuquan.view.shared;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.ui.SelectedTabType;

/**
 * Created by wudi on 3/15/2015.
 */
public class TabBar extends LinearLayout implements View.OnClickListener
{
    private static final int TABBAR_TYPE_FOLLOWEES = 0x00;
    private static final int TABBAR_TYPE_TOPSTORY = 0x01;
    private static final int TABBAR_TYPE_TAG = 0x02;
    private static final int TABBAR_TYPE_ACTIVITY = 0x03;

    public interface TabSelectedListener
    {
        public void onTabSelected(SelectedTabType type);
    }

    private int tabbarType;
    private View followeesTab;
    private ImageView followeesIcon;
    private TextView followeesTitle;

    private View topStoryTab;
    private ImageView topStoryIcon;
    private TextView topStoryTitle;

    private View discoverTab;
    private ImageView discoverIcon;
    private TextView discoverTitle;

    private View activityTab;
    private ImageView activityIcon;
    private TextView activityTitle;

    private TabSelectedListener tabSelectedListener;

    public TabBar(Context context)
    {
        super(context);
    }

    public TabBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public TabSelectedListener getTabSelectedListener()
    {
        return this.tabSelectedListener;
    }

    public void setTabSelectedListener(TabSelectedListener tabSelectedListener)
    {
        this.tabSelectedListener = tabSelectedListener;
    }

    public void reset()
    {
        clearSelected();

        selectedFolloweesTab(true);
    }

    public void jump(SelectedTabType type)
    {
        clearSelected();

        switch (type)
        {
            case TopLine:
                selectedFolloweesTab(true);
                break;
            case Comment:
                selectedTopStoryTab(true);
                break;
            case Class:
                selectedDiscoverTab(true);
                break;
            case MyInfo:
                selectedActivityTab(true);
                break;
            default:
                type = SelectedTabType.TopLine;
                selectedFolloweesTab(true);
        }

        if (this.tabSelectedListener != null)
        {
            this.tabSelectedListener.onTabSelected(type);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (this.tabSelectedListener != null)
        {
            SelectedTabType type = null;
            switch (v.getId())
            {
                case R.id.view_shared_tabbar_followees:
                    type = SelectedTabType.TopLine;
                    break;
                case R.id.view_shared_tabbar_topstory:
                    type = SelectedTabType.Comment;
                    break;
                case R.id.view_shared_tabbar_discover:
                    type = SelectedTabType.Class;
                    break;
                case R.id.view_shared_tabbar_activity:
                    type = SelectedTabType.MyInfo;
                    break;
            }

            this.tabSelectedListener.onTabSelected(type);
        }
    }

    private void init(Context context, AttributeSet attrs)
    {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabBar);
        this.tabbarType = a.getInt(R.styleable.TabBar_tabbarType, TABBAR_TYPE_FOLLOWEES);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_shared_tabbar, this);

        this.followeesTab = this.findViewById(R.id.view_shared_tabbar_followees);
        this.followeesIcon = (ImageView) this.findViewById(R.id.view_shared_tabbar_followees_icon);
        this.followeesTitle = (TextView) this.findViewById(R.id.view_shared_tabbar_followees_title);
        this.topStoryTab = this.findViewById(R.id.view_shared_tabbar_topstory);
        this.topStoryIcon = (ImageView) this.findViewById(R.id.view_shared_tabbar_topstory_icon);
        this.topStoryTitle = (TextView) this.findViewById(R.id.view_shared_tabbar_topstory_title);
        this.discoverTab = this.findViewById(R.id.view_shared_tabbar_discover);
        this.discoverIcon = (ImageView) this.findViewById(R.id.view_shared_tabbar_discover_icon);
        this.discoverTitle = (TextView) this.findViewById(R.id.view_shared_tabbar_discover_title);
        this.activityTab = this.findViewById(R.id.view_shared_tabbar_activity);
        this.activityIcon = (ImageView) this.findViewById(R.id.view_shared_tabbar_activity_icon);
        this.activityTitle = (TextView) this.findViewById(R.id.view_shared_tabbar_activity_title);
        this.followeesTab.setOnClickListener(this);
        this.topStoryTab.setOnClickListener(this);
        this.discoverTab.setOnClickListener(this);
        this.activityTab.setOnClickListener(this);

        clearSelected();
        switch (this.tabbarType)
        {
            case TABBAR_TYPE_TOPSTORY:
                selectedTopStoryTab(true);
                break;
            case TABBAR_TYPE_TAG:
                selectedDiscoverTab(true);
                break;
            case TABBAR_TYPE_ACTIVITY:
                selectedActivityTab(true);
                break;
            case TABBAR_TYPE_FOLLOWEES:
            default:
                selectedFolloweesTab(true);
                break;
        }
    }

    private void clearSelected()
    {
        selectedFolloweesTab(false);
        selectedTopStoryTab(false);
        selectedDiscoverTab(false);
        selectedActivityTab(false);
    }

    private void selectedFolloweesTab(boolean isSelected)
    {
        if (isSelected)
        {
            this.followeesIcon.setImageResource(R.drawable.icon_moments_following_selected);
            this.followeesTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_selected_title_color));
        }
        else
        {
            this.followeesIcon.setImageResource(R.drawable.icon_moments_following_unselected);
            this.followeesTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_unselected_title_color));
        }
    }

    private void selectedTopStoryTab(boolean isSelected)
    {
        if (isSelected)
        {
            this.topStoryIcon.setImageResource(R.drawable.icon_moments_all_selected);
            this.topStoryTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_selected_title_color));
        }
        else
        {
            this.topStoryIcon.setImageResource(R.drawable.icon_moments_all_unselected);
            this.topStoryTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_unselected_title_color));
        }
    }

    private void selectedDiscoverTab(boolean isSelected)
    {
        if (isSelected)
        {
            this.discoverIcon.setImageResource(R.drawable.icon_discover_selected);
            this.discoverTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_selected_title_color));
        }
        else
        {
            this.discoverIcon.setImageResource(R.drawable.icon_discover_unselected);
            this.discoverTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_unselected_title_color));
        }
    }

    private void selectedActivityTab(boolean isSelected)
    {
        if (isSelected)
        {
            this.activityIcon.setImageResource(R.drawable.icon_activity_selected);
            this.activityTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_selected_title_color));
        }
        else
        {
            this.activityIcon.setImageResource(R.drawable.icon_activity_unselected);
            this.activityTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_unselected_title_color));
        }
    }
}
