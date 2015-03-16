package com.metis.meishuquan.view.shared;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.util.ViewUtils;

import java.util.Date;

/**
 * Created by wudi on 3/15/2015.
 */

public class RefreshLoadMoreListView extends ListView implements AbsListView.OnScrollListener
{
    public RefreshLoadMoreListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.init(context);
    }

    public RefreshLoadMoreListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.init(context);
    }

    public RefreshLoadMoreListView(Context context)
    {
        super(context);
        this.init(context);
    }

    private static final int TAP_TO_REFRESH = 1;
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;

    protected static final String RefreshTimeUpdateFormat = "Last refresh time: %s";

    private static final String TAG = "RefreshLoadMoreListView";

    private OnRefreshLoadMoreListener onRefreshLoadMoreListener;

    // header
    private boolean isCanRefresh = true;
    private OnScrollListener onScrollListener;
    private LayoutInflater inflater;
    private RelativeLayout refreshView;
    private View refreshViewContainer;
    private TextView refreshViewText;
    private ImageView refreshViewImage;
    private ProgressBar refreshViewProgress;
    private TextView refreshViewLastUpdated;
    private int currentScrollState;
    private int refreshState;
    private RotateAnimation flipAnimation;
    private RotateAnimation reverseFlipAnimation;
    private int refreshViewHeight;
    private int refreshOriginalTopPadding;
    private boolean bounceHack;
    private Date lastUpdateDate;

    // footer
    private boolean isLoadingmore;
    private View footer;
    private View footerContainer;

    // backer
    private View backer;
    private final Rect backerRect = new Rect();
    private final PointF touchPoint = new PointF();
    private boolean canBackerShow;
    private View touchTarget;
    private int touchSlop = 0;
    private MotionEvent downEvent;
    private int backerWidth;
    private int backerHeight;
    private int backerX;
    private int backerY;
    private ImageView backerBackgroud;
    private static final int BackerInterval = 3 * 1000;

    // pop message
    private View message;
    private TextView messageText;
    private static final int messageInterval = 2 * 1000;
    private int messageHeight;
    private Animation messageShowAnim;
    private Animation messageDisppearAnim;
    private AnimationSet messageAnim;
    private int messageType = 0;// 0:Toast 1:head line

    protected Context context;

    private void init(Context context)
    {
        this.context = context;

        this.setScrollbarFadingEnabled(true);

        // Load all of the animations we need in code rather than through XML
        this.flipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        this.flipAnimation.setInterpolator(new LinearInterpolator());
        this.flipAnimation.setDuration(250);
        this.flipAnimation.setFillAfter(true);
        this.reverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        this.reverseFlipAnimation.setInterpolator(new LinearInterpolator());
        this.reverseFlipAnimation.setDuration(250);
        this.reverseFlipAnimation.setFillAfter(true);
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.refreshView = (RelativeLayout) this.inflater.inflate(R.layout.view_shared_refreshloadmorelistview_header, this, false);
        this.refreshViewContainer = this.refreshView.findViewById(R.id.view_shared_refreshloadmoreview_header);
        this.refreshViewText = (TextView) this.refreshView.findViewById(R.id.view_shared_refreshloadmoreview_header_text);
        this.refreshViewImage = (ImageView) this.refreshView.findViewById(R.id.view_shared_refreshloadmoreview_header_image);
        this.refreshViewProgress = (ProgressBar) this.refreshView.findViewById(R.id.view_shared_refreshloadmoreview_header_progress);
        this.refreshViewLastUpdated = (TextView) this.refreshView.findViewById(R.id.view_shared_refreshloadmoreview_header_updated_at);
        this.refreshViewImage.setMinimumHeight(50);
        this.refreshView.setOnClickListener(new OnClickRefreshListener());
        this.refreshOriginalTopPadding = this.refreshView.getPaddingTop();
        this.refreshState = TAP_TO_REFRESH;
        this.addHeaderView(refreshView, null, false);
        this.measureView(this.refreshView);
        this.refreshViewHeight = this.refreshView.getMeasuredHeight();

        // footer
        this.footer = this.inflater.inflate(R.layout.view_shared_refreshloadmorelistview_footer, null);
        this.footerContainer = this.footer.findViewById(R.id.view_shared_refreshloadmorelistview_footer);
        this.footerContainer.setVisibility(View.GONE);
        this.addFooterView(footer, null, false);
        this.isLoadingmore = false;

        // backer
        this.backer = (LinearLayout) this.inflater.inflate(R.layout.view_shared_refreshloadmorelistview_backer, this, false);
        this.touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.backerBackgroud = (ImageView) this.backer.findViewById(R.id.view_shared_refreshloadmorelist_btn_backtotop);

        // message
        this.message = this.inflater.inflate(R.layout.view_shared_refreshloadmorelistview_message, this, false);
        this.message.setVisibility(View.GONE);
        this.messageText = (TextView) this.message.findViewById(R.id.view_shared_refreshloadmorelistview_message_text);
        this.measureView(this.message);
        this.messageHeight = this.message.getMeasuredHeight();
        this.messageShowAnim = new TranslateAnimation(0, 0, 0 - this.messageHeight, 0);
        this.messageShowAnim.setDuration(500);
        this.messageShowAnim.setInterpolator(new AccelerateInterpolator());
        this.messageDisppearAnim = new TranslateAnimation(0, 0, 0, 0 - this.messageHeight);
        this.messageDisppearAnim.setDuration(300);
        this.messageDisppearAnim.setInterpolator(new AccelerateInterpolator());
        this.messageDisppearAnim.setStartOffset(messageInterval);
        this.messageDisppearAnim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                message.setVisibility(View.GONE);
            }
        });
        this.messageAnim = new AnimationSet(false);
        this.messageAnim.addAnimation(this.messageShowAnim);
        this.messageAnim.addAnimation(this.messageDisppearAnim);

        super.setOnScrollListener(this);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        this.setSelection(1);
    }

    public void disableRefresh()
    {
        this.isCanRefresh = false;
        this.refreshViewContainer.setVisibility(View.GONE);
    }

    @Override
    public void setAdapter(ListAdapter adapter)
    {
        super.setAdapter(adapter);
        this.setSelection(1);
    }

    private void messageShow(CharSequence text)
    {
        this.messageText.setText(text);
        this.message.setVisibility(View.VISIBLE);
        this.message.layout(0, 0, this.getRight(), this.messageHeight);
        requestLayout();
        this.messageAnim.cancel();
        this.message.startAnimation(this.messageAnim);
    }

    private void setLastUpdated(CharSequence lastUpdated)
    {
        if (lastUpdated != null)
        {
            this.refreshViewLastUpdated.setVisibility(View.VISIBLE);
            this.refreshViewLastUpdated.setText(lastUpdated);
        }

        else
        {
            this.refreshViewLastUpdated.setVisibility(View.GONE);
        }
    }

    private void setBackerBackground()
    {
        this.backerBackgroud.setImageResource(R.drawable.view_shared_refreshloadmorelist_btn_backtotop);
        requestLayout();
    }

    private void setBackerPressedBackground()
    {
        this.backerBackgroud.setImageResource(R.drawable.view_shared_refreshloadmorelist_btn_backtotop_pressed);
        requestLayout();
    }

    private boolean isTouchedBacker(View view, float x, float y)
    {
        view.getHitRect(this.backerRect);
        return backerRect.contains((int) x, (int) y);
    }

    private void backerShow()
    {
        if (isBackerShow())
        {
            return;
        }

        this.backer.setVisibility(View.VISIBLE);
        requestLayout();
        ViewUtils.delayExecute(new Runnable() {
            @Override
            public void run() {
                backerDisappear();
            }
        }, BackerInterval);
    }

    private void backerDisappear()
    {
        if (!isBackerShow())
        {
            return;
        }

        this.backer.setVisibility(View.GONE);
        requestLayout();
    }

    private boolean isBackerShow()
    {
        return (this.backer.getVisibility() != View.GONE);
    }

    private void backAction()
    {
        this.setSelection(1);
        // this.smoothScrollToPosition(1);
    }

    private void clearTouchTarget()
    {
        this.touchTarget = null;
        if (downEvent != null)
        {
            downEvent.recycle();
            downEvent = null;
        }
    }

    // 0:up 1:down
    private int getScrollDirection(float x1, float y1, float x2, float y2)
    {
        if ((y2 - y1) > 8)
        {
            return 1;
        }
        if (y1 - y2 > 8)
        {
            return 0;
        }
        return -1;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        final int action = ev.getAction();

        final float x = ev.getX();
        final float y = ev.getY();

        if (action == MotionEvent.ACTION_DOWN)
        {
            this.touchPoint.x = x;
            this.touchPoint.y = y;
        }

        if (action == MotionEvent.ACTION_DOWN && touchTarget == null && isTouchedBacker(this.backer, x, y))
        {
            touchTarget = this.backer;
            this.touchPoint.x = x;
            this.touchPoint.y = y;
            downEvent = MotionEvent.obtain(ev);
            this.setBackerPressedBackground();
        }

        if (action == MotionEvent.ACTION_MOVE)
        {
            if (this.isCanRefresh && this.refreshViewContainer.getVisibility() == View.GONE && getScrollDirection(touchPoint.x, touchPoint.y, x, y) == 1)
            {
                this.refreshViewContainer.setVisibility(View.VISIBLE);
                this.resetHeaderPadding();
                if ((this.refreshView.getBottom() >= this.refreshViewHeight || this.refreshView.getTop() >= 0) && this.refreshState != RELEASE_TO_REFRESH)
                {
                    this.refreshViewText.setText("release to refresh");
                    this.refreshViewImage.setVisibility(View.VISIBLE);
                    this.refreshViewImage.clearAnimation();
                    this.refreshViewImage.startAnimation(flipAnimation);
                    this.refreshState = RELEASE_TO_REFRESH;
                }
            }

            if (getScrollDirection(touchPoint.x, touchPoint.y, x, y) == 1 && this.canBackerShow)
            {
                this.backerShow();
            }
            else if (getScrollDirection(touchPoint.x, touchPoint.y, x, y) == 0)
            {
                this.backerDisappear();
            }
        }

        if (action == MotionEvent.ACTION_UP)
        {
            this.setBackerBackground();
        }

        if (touchTarget != null)
        {
            if (isTouchedBacker(touchTarget, x, y))
            {
                touchTarget.dispatchTouchEvent(ev);
                this.setBackerPressedBackground();
            }
            else
            {
                this.setBackerBackground();
            }

            if (action == MotionEvent.ACTION_UP)
            {
                super.dispatchTouchEvent(ev);
                if (this.isBackerShow())
                {
                    this.backAction();
                }

                this.clearTouchTarget();
            }
            else if (action == MotionEvent.ACTION_CANCEL)
            {
                this.clearTouchTarget();
            }
            else if (action == MotionEvent.ACTION_MOVE)
            {
                if (Math.abs(y - touchPoint.y) > touchSlop)
                {
                    MotionEvent event = MotionEvent.obtain(ev);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    touchTarget.dispatchTouchEvent(event);
                    event.recycle();
                    super.dispatchTouchEvent(downEvent);
                    super.dispatchTouchEvent(ev);
                    this.clearTouchTarget();
                }
            }

            return this.isBackerShow() ? true : super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        this.bounceHack = false;

        switch (event.getAction())
        {
            case MotionEvent.ACTION_UP:
                if (!isVerticalScrollBarEnabled())
                {
                    setVerticalScrollBarEnabled(true);
                }

                if (this.isCanRefresh && this.getFirstVisiblePosition() == 0 && this.refreshState != REFRESHING)
                {
                    if ((this.refreshView.getBottom() >= this.refreshViewHeight || this.refreshView.getTop() >= 0) && this.refreshState == RELEASE_TO_REFRESH)
                    {
                        // Initiate the refresh
                        this.refreshState = REFRESHING;
                        this.prepareForRefresh();
                        this.onRefresh();
                    }

                    else if (this.refreshView.getBottom() < this.refreshViewHeight || this.refreshView.getTop() <= 0)
                    {
                        // Abort refresh and scroll down below the refresh view
                        this.resetHeader();
                        this.setSelection(1);
                    }
                }

                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Sets the header padding back to original size.
     */
    private void resetHeaderPadding()
    {
        this.refreshView.setPadding(this.refreshView.getPaddingLeft(), this.refreshOriginalTopPadding, this.refreshView.getPaddingRight(),
                this.refreshView.getPaddingBottom());
    }

    /**
     * Resets the header to the original state.
     */
    private void resetHeader()
    {
        this.refreshViewContainer.setVisibility(View.GONE);
        if (this.refreshState == TAP_TO_REFRESH)
        {
            return;
        }

        this.refreshState = TAP_TO_REFRESH;

        this.resetHeaderPadding();
        // Set refresh view text to the pull label
        this.refreshViewText.setText("click to refresh");
        // Replace refresh drawable with arrow drawable
        this.refreshViewImage.setImageResource(R.drawable.view_shared_refreshloadmoreview_loading);
        // Clear the full rotation animation
        this.refreshViewImage.clearAnimation();
        // Hide progress bar and arrow.
        this.refreshViewImage.setVisibility(View.GONE);
        this.refreshViewProgress.setVisibility(View.GONE);
    }

    private void measureView(View child)
    {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null)
        {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0)
        {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        }

        else
        {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (this.backer == null)
        {
            return;
        }

        this.measureChild(this.backer, widthMeasureSpec, heightMeasureSpec);
        this.backerWidth = this.backer.getMeasuredWidth();
        this.backerHeight = this.backer.getMeasuredHeight();
        this.backerX = this.getMeasuredWidth() - this.backerWidth;
        this.backerY = (int) (this.getBottom() * 0.65);

        if (this.message == null)
        {
            return;
        }

        this.measureChild(this.message, widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);

        if (this.backer == null)
        {
            return;
        }

        this.backer.layout(this.backerX, this.backerY, this.backerX + this.backerWidth, this.backerY + this.backerHeight);

        if (this.message.getVisibility() == View.VISIBLE)
        {
            this.message.layout(0, 0, this.getRight(), this.messageHeight);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);
        if (this.backer.getVisibility() == View.VISIBLE)
        {
            drawChild(canvas, this.backer, getDrawingTime());
        }

        if (this.message.getVisibility() == View.VISIBLE)
        {
            drawChild(canvas, this.message, getDrawingTime());
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        // When the refresh view is completely visible, change the text to say
        // "Release to refresh..." and flip the arrow drawable.
        if (firstVisibleItem == 0 && this.lastUpdateDate != null)
        {
            this.setLastUpdated(String.format(RefreshTimeUpdateFormat, Utils.getDateFromNow(this.lastUpdateDate)));
        }

        if (this.isLoadingmore)
        {
            this.refreshViewText.setText("loading please wait.");
        }

        if (this.isCanRefresh && this.currentScrollState == SCROLL_STATE_TOUCH_SCROLL && this.refreshState != REFRESHING && this.isLoadingmore == false)
        {
            if (firstVisibleItem == 0)
            {
                this.refreshViewImage.setVisibility(View.VISIBLE);
                if ((this.refreshView.getBottom() >= this.refreshViewHeight + 20 || this.refreshView.getTop() >= 0) && this.refreshState != RELEASE_TO_REFRESH)
                {
                    this.refreshViewText.setText("release to refresh");
                    this.refreshViewImage.clearAnimation();
                    this.refreshViewImage.startAnimation(flipAnimation);
                    this.refreshState = RELEASE_TO_REFRESH;
                }
                else if (this.refreshView.getBottom() < this.refreshViewHeight + 20 && this.refreshState != PULL_TO_REFRESH)
                {
                    this.setScrollbarFadingEnabled(false);
                    this.refreshViewText.setText("pull to refresh");
                    if (this.refreshState != TAP_TO_REFRESH)
                    {
                        this.refreshViewImage.clearAnimation();
                        this.refreshViewImage.startAnimation(reverseFlipAnimation);
                    }
                    this.refreshState = PULL_TO_REFRESH;
                }
            }

            else
            {
                this.setScrollbarFadingEnabled(true);
                this.refreshViewImage.setVisibility(View.GONE);
                this.resetHeader();
            }
        }

        else if (currentScrollState == SCROLL_STATE_FLING && firstVisibleItem == 0 && refreshState != REFRESHING)
        {
            this.setSelection(1);
            this.bounceHack = true;
        }

        else if (this.bounceHack && this.currentScrollState == SCROLL_STATE_FLING)
        {
            this.setSelection(1);
        }

        if (this.onScrollListener != null)
        {
            this.onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        // backer
        if (firstVisibleItem > 1)
        {
            this.canBackerShow = true;
        }
        if (firstVisibleItem == 1)
        {
            this.canBackerShow = false;
            this.backerDisappear();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        this.currentScrollState = scrollState;

        if (this.currentScrollState == SCROLL_STATE_IDLE)
        {
            this.bounceHack = false;
        }

        if (this.onScrollListener != null)
        {
            this.onScrollListener.onScrollStateChanged(view, scrollState);
        }

        // loading more trigger
        if (this.getLastVisiblePosition() + 3 >= this.getAdapter().getCount() && scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && refreshState == TAP_TO_REFRESH)
        {
            if (this.isLoadingmore == false && this.onRefreshLoadMoreListener != null && this.onRefreshLoadMoreListener.hasMore())
            {
                this.footerContainer.setVisibility(View.VISIBLE);
                this.isLoadingmore = true;
                if (this.onRefreshLoadMoreListener != null)
                {
                    this.onRefreshLoadMoreListener.onLoadMore();
                }
            }
        }
    }

    private void prepareForRefresh()
    {
        this.resetHeaderPadding();

        this.refreshViewImage.setVisibility(View.GONE);
        // We need this hack, otherwise it will keep the previous drawable.
        this.refreshViewImage.setImageDrawable(null);
        this.refreshViewProgress.setVisibility(View.VISIBLE);
        // Set refresh view text to the refreshing label
        this.refreshViewText.setText("loading...");
        this.refreshState = REFRESHING;
    }

    public void onRefresh()
    {
        Log.d(TAG, "onRefresh");

        if (this.onRefreshLoadMoreListener != null)
        {
            this.onRefreshLoadMoreListener.onRefresh();
        }
    }

    /**
     * Resets the list to a normal state after a refresh.
     */
    private void onRefreshComplete()
    {
        Log.d(TAG, "onRefreshComplete");
        this.lastUpdateDate = new Date();
        this.resetHeader();

        // If refresh view is visible when loading completes, scroll down to
        // the next item.
        if (getFirstVisiblePosition() != 0)
        {
            return;
        }
        this.setSelection(1);

    }

    private void onLoadMoreCompleted()
    {
        this.isLoadingmore = false;
        this.footerContainer.setVisibility(View.GONE);
    }

    private void showMessageBox(CharSequence message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // API
    public void firstRefresh()
    {
        this.prepareForRefresh();
        this.onRefresh();
    }

    public void onLoadDataCompletedWithMessage(boolean isSuccess, CharSequence message)
    {
        if (this.isLoadingmore == true)
        {
            this.onLoadMoreCompleted();

            if (!isSuccess)
            {
                Toast.makeText(context, "network error, please check network configuration", Toast.LENGTH_LONG).show();
            }
        }

        if (this.refreshState == REFRESHING)
        {
            this.onRefreshComplete();
        }

        if (isSuccess && message != null)
        {
            if (this.messageType == 0)
            {
                this.showMessageBox(message);
            }
            else
            {
                this.messageShow(message);
            }
        }
    }

    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener l)
    {
        this.onScrollListener = l;
    }

    public void setOnRefreshListener(OnRefreshLoadMoreListener onRefreshLoadMoreListener)
    {
        this.onRefreshLoadMoreListener = onRefreshLoadMoreListener;
    }

    public void setMessageType(int type)
    {
        this.messageType = type;
    }

    // API end

    /**
     * Invoked when the refresh view is clicked on. This is mainly used when
     * there's only a few items in the list and it's not possible to drag the
     * list.
     */
    private class OnClickRefreshListener implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if (refreshState == REFRESHING)
            {
                return;
            }

            prepareForRefresh();
            onRefresh();
        }
    }

    public interface OnRefreshLoadMoreListener
    {
        public void onRefresh();

        public void onLoadMore();

        public boolean hasMore();
    }
}