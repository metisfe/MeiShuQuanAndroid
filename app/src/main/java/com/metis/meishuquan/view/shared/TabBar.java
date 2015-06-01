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
public class TabBar extends LinearLayout implements View.OnClickListener {
    private static final int TABBAR_TYPE_FOLLOWEES = 0x00;
    private static final int TABBAR_TYPE_TOPSTORY = 0x01;
    private static final int TABBAR_TYPE_TAG = 0x02;
    private static final int TABBAR_TYPE_ACTIVITY = 0x03;
    private static final int TABBAR_TYPE_CIRCLE = 0x04;

    public interface TabSelectedListener {
        public void onTabSelected(SelectedTabType type);
    }

    //头条
    private int tabbarType;
    private View followeesTab;
    private ImageView followeesIcon;
    private TextView followeesTitle;

    //点评
    private static View topStoryTab;
    private ImageView topStoryIcon;
    private TextView topStoryTitle;

    //circle
    private View circleTab;
    private ImageView circleIcon;
//    private TextView circleTitle;

    //课程
    private static View discoverTab;
    private ImageView discoverIcon;
    private TextView discoverTitle;

    //我
    private View activityTab;
    private ImageView activityIcon;
    private TextView activityTip;
    private TextView activityTitle;

    private TabSelectedListener tabSelectedListener;

    public TabBar(Context context) {
        super(context);
    }

    public TabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TabSelectedListener getTabSelectedListener() {
        return this.tabSelectedListener;
    }

    public void setTabSelectedListener(TabSelectedListener tabSelectedListener) {
        this.tabSelectedListener = tabSelectedListener;
    }

    public void reset() {
        clearSelected();

        selectedFolloweesTab(true);
    }

    @Override
    public void onClick(View v) {
        if (this.tabSelectedListener != null) {
            SelectedTabType type = null;
            switch (v.getId()) {
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
                case R.id.view_shared_tabbar_circle:
                    type = SelectedTabType.Circle;
                    break;
            }
            this.tabSelectedListener.onTabSelected(type);
        }
    }

    private void init(Context context, AttributeSet attrs) {
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
        this.activityTip = (TextView) this.findViewById(R.id.view_shared_tabbar_activity_tip);
        this.circleTab = this.findViewById(R.id.view_shared_tabbar_circle);
        this.circleIcon = (ImageView) this.findViewById(R.id.view_shared_tabbar_circle_icon);
//        this.circleTitle = (TextView) this.findViewById(R.id.view_shared_tabbar_circle_title);
        this.followeesTab.setOnClickListener(this);
        this.topStoryTab.setOnClickListener(this);
        this.discoverTab.setOnClickListener(this);
        this.activityTab.setOnClickListener(this);
        this.circleTab.setOnClickListener(this);

        clearSelected();
        switch (this.tabbarType) {
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
                selectedFolloweesTab(true);
                break;
            case TABBAR_TYPE_CIRCLE:
            default:
                selectedCircleTab(true);
                break;
        }
    }

    public static void showOrHide(int index, boolean isShow) {
        if (index == 3) {
            if (isShow) {
                discoverTab.setVisibility(View.VISIBLE);
            } else {
                discoverTab.setVisibility(View.GONE);
            }
        } else if (index == 1) {
            if (isShow) {
                topStoryTab.setVisibility(View.VISIBLE);
            } else {
                topStoryTab.setVisibility(View.GONE);
            }
        }
    }

    public void setActivityTipVisible (int visible) {
        activityTip.setVisibility(visible);
    }

    public void setActivityTipText (CharSequence charSequence) {
        activityTip.setText(charSequence);
    }

    //清除选中状态
    private void clearSelected() {
        selectedFolloweesTab(false);
        selectedTopStoryTab(false);
        selectedDiscoverTab(false);
        selectedActivityTab(false);
        selectedCircleTab(false);
    }

    /**
     * 底部Tab_头条
     *
     * @param isSelected 是否选中
     */
    private void selectedFolloweesTab(boolean isSelected) {
        this.followeesTitle.setText(R.string.tab_topLine);
        if (isSelected)//选中
        {
            this.followeesIcon.setImageResource(R.drawable.icon_tabbar_top_selected);
            this.followeesTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_selected_title_color));
        } else//未选中
        {
            this.followeesIcon.setImageResource(R.drawable.icon_tabbar_top_unselected);
            this.followeesTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_unselected_title_color));
        }
    }

    /**
     * 底部Tab_点评
     *
     * @param isSelected 选中状态
     */
    private void selectedTopStoryTab(boolean isSelected) {
        this.topStoryTitle.setText(R.string.tab_comment);
        if (isSelected) {
            this.topStoryIcon.setImageResource(R.drawable.icon_tabbar_assess_selected);
            this.topStoryTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_selected_title_color));
        } else {
            this.topStoryIcon.setImageResource(R.drawable.icon_tabbar_assess_unselected);
            this.topStoryTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_unselected_title_color));
        }
    }

    /**
     * 底部Tab_课程
     *
     * @param isSelected 选中状态
     */
    private void selectedDiscoverTab(boolean isSelected) {
        this.discoverTitle.setText(R.string.tab_course);
        if (isSelected) {
            this.discoverIcon.setImageResource(R.drawable.icon_tabbar_course_selected);
            this.discoverTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_selected_title_color));
        } else {
            this.discoverIcon.setImageResource(R.drawable.icon_tabbar_course_unselected);
            this.discoverTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_unselected_title_color));
        }
    }

    /**
     * 底部Tab_我
     *
     * @param isSelected 选中状态
     */
    private void selectedActivityTab(boolean isSelected) {
        this.activityTitle.setText(R.string.tab_me);
        if (isSelected) {
            this.activityIcon.setImageResource(R.drawable.icon_tabbar_me_selected);
            this.activityTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_selected_title_color));
        } else {
            this.activityIcon.setImageResource(R.drawable.icon_tabbar_me_unselected);
            this.activityTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_unselected_title_color));
        }
    }

    /**
     * 底部Tab_circle
     *
     * @param isSelected 选中状态
     */
    private void selectedCircleTab(boolean isSelected) {
//        this.circleTitle.setText(R.string.tab_circle);
        if (isSelected) {
            this.circleIcon.setImageResource(R.drawable.icon_tabbar_circle_selected2);
//            this.circleTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_selected_title_color));
        } else {
            this.circleIcon.setImageResource(R.drawable.icon_tabbar_circle_selected2);
//            this.circleTitle.setTextColor(this.getResources().getColor(R.color.view_shared_tab_bar_unselected_title_color));
        }
    }
}
