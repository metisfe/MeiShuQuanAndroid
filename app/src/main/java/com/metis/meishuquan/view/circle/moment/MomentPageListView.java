
package com.metis.meishuquan.view.circle.moment;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;

public class MomentPageListView extends ListView implements OnScrollListener
{
    public interface OnLoadMoreListener
    {
        public void onLoadMore();
        
        public boolean hasMore();
    }

    public interface OnScrolledListener
    {
        public void OnScrolled(int scrollTop, String tag);
    }

    public interface OnSocialActionBarPositionChangedListener
    {
        public void OnSocialActionBarPositionChanged(Position type);
    }

    public enum Position
    {
        Bottom, Middle, Top
    }

    private OnSocialActionBarPositionChangedListener onSocialActionBarPositionChangedListener;
    private Position socialActionBarPosition = Position.Bottom;
    private boolean isScrollingUp, isLoadingMore;
    private OnLoadMoreListener onLoadMoreListener;
    private OnScrolledListener onScrolledListener;
    private static final int socialActionBarHeight = (int) MainApplication.Resources.getDimension(R.dimen.circle_moment_action_bar_height);
    private static final int PRELOAD_BUFFER = 3;

    public MomentPageListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.setOnScrollListener(this);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
    {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void loadComplete()
    {
        this.isLoadingMore = false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        if (scrollState != OnScrollListener.SCROLL_STATE_IDLE)
        {
            return;
        }

        if (this.onScrolledListener != null)
        {
            View contentView = getChildAt(0);
            if (this.getFirstVisiblePosition() == 0 && contentView != null)
            {
                this.onScrolledListener.OnScrolled(-contentView.getTop(), (String) this.getTag());
            }
        }

        View contentView = getChildAt(1);
        if (contentView == null)
        {
            return;
        }

        final int top = contentView.getTop();

        if (this.getLastVisiblePosition() + PRELOAD_BUFFER >= this.getAdapter().getCount())
        {
            if (this.isLoadingMore == false && this.onLoadMoreListener != null && this.onLoadMoreListener.hasMore())
            {
                this.isLoadingMore = true;
                this.onLoadMoreListener.onLoadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        if (this.onSocialActionBarPositionChangedListener != null)
        {
            View contentView = getChildAt(0);
            if (firstVisibleItem == 0 && contentView != null)
            {
                int bottom = contentView.getBottom();
                if (bottom >= this.getHeight() && socialActionBarPosition != Position.Bottom)
                {
                    onSocialActionBarPositionChangedListener.OnSocialActionBarPositionChanged(Position.Bottom);
                    socialActionBarPosition = Position.Bottom;
                }

                if (bottom > socialActionBarHeight && bottom < this.getHeight() && socialActionBarPosition != Position.Middle)
                {
                    onSocialActionBarPositionChangedListener.OnSocialActionBarPositionChanged(Position.Middle);
                    socialActionBarPosition = Position.Middle;
                }

                if (bottom <= socialActionBarHeight && socialActionBarPosition != Position.Top)
                {
                    onSocialActionBarPositionChangedListener.OnSocialActionBarPositionChanged(Position.Top);
                    socialActionBarPosition = Position.Top;
                }
            }
            else if (socialActionBarPosition != Position.Top)
            {
                onSocialActionBarPositionChangedListener.OnSocialActionBarPositionChanged(Position.Top);
                socialActionBarPosition = Position.Top;
            }
        }

        if (this.onScrolledListener != null)
        {
            View contentView = getChildAt(0);
            if (firstVisibleItem == 0 && contentView != null)
            {
                this.onScrolledListener.OnScrolled(-contentView.getTop(), (String) this.getTag());
            }
        }
    }

    public void setOnSocialActionBarPositionChangedListener(OnSocialActionBarPositionChangedListener l)
    {
        this.onSocialActionBarPositionChangedListener = l;
    }

    public void scrollToContent()
    {
//        ViewUtility.delayExecute(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                if (getFirstVisiblePosition() == 0)
//                {
//                    int bottom = getChildAt(0).getBottom();
//                    if (bottom > MAX_SCROLL_DISTANCE)
//                    {
//                        if (socialActionBarPosition != Position.Top)
//                        {
//                            onSocialActionBarPositionChangedListener.OnSocialActionBarPositionChanged(Position.Top);
//                            socialActionBarPosition = Position.Top;
//                        }
//
//                        setSelection(1);
//                        View contentView = getChildAt(0);
//                        onScrolledListener.OnScrolled(contentView.getMeasuredHeight(), (String) getTag());
//                    }
//                    else
//                    {
//                        smoothScrollBy(bottom, DURATION_SMOOTH_SCROLL);
//                    }
//                }
//            }
//        }, 50);
    }

    public void setOnScrolledListener(OnScrolledListener listener)
    {
        this.onScrolledListener = listener;
    }
}