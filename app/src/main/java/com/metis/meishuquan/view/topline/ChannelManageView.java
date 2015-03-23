package com.metis.meishuquan.view.topline;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.topline.DragAdapter;
import com.metis.meishuquan.adapter.topline.OtherAdapter;
import com.metis.meishuquan.model.topline.ChannelItem;
import com.metis.meishuquan.model.topline.ChannelManage;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity：频道管理主界面
 *
 * @Author wj on 3/18/2015
 */
public class ChannelManageView extends RelativeLayout implements OnItemClickListener, View.OnClickListener {
    private DragGrid userGridView;
    private OtherGridView otherGridView;
    private DragAdapter userAdapter;
    private OtherAdapter otherAdapter;
    private Button btnBack;

    private List<ChannelItem> otherChannelList = new ArrayList<>();
    private List<ChannelItem> userChannelList = new ArrayList<>();
    boolean isMove = false;

    private Context context;
    ChannelManageViewListener channelManageViewListener;

    public ChannelManageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.activity_channel_manage_subscribe, this);
        initView(this);
        initData();
    }

    public void setOtherChannelList(List<ChannelItem> otherChannelList) {
        this.otherChannelList = otherChannelList;
    }

    public void setUserChannelList(List<ChannelItem> userChannelList) {
        this.userChannelList = userChannelList;
    }

    //初始化数据
    public void initData() {
        userAdapter = new DragAdapter(this.context, userChannelList);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new OtherAdapter(this.context, otherChannelList);
        otherGridView.setAdapter(this.otherAdapter);
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void initView(ViewGroup rootView) {
        this.userGridView = (DragGrid) rootView.findViewById(R.id.userGridView);
        this.otherGridView = (OtherGridView) rootView.findViewById(R.id.otherGridView);
        this.btnBack = (Button) rootView.findViewById(R.id.activity_channel_btn_back);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view,
                            final int position, long id) {
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                if (position != 0 && position != 1) {
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view
                                .findViewById(R.id.text_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final ChannelItem channel = ((DragAdapter) parent
                                .getAdapter()).getItem(position);
                        otherAdapter.setVisible(false);
                        otherAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    otherGridView.getChildAt(
                                            otherGridView.getLastVisiblePosition())
                                            .getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation,
                                            endLocation, channel, userGridView);
                                    userAdapter.setRemove(position);
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            case R.id.otherGridView:
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view
                            .findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ChannelItem channel = ((OtherAdapter) parent.getAdapter())
                            .getItem(position);
                    userAdapter.setVisible(false);
                    userAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                userGridView.getChildAt(
                                        userGridView.getLastVisiblePosition())
                                        .getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation,
                                        channel, otherGridView);
                                otherAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    /**
     * ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation,
                          int[] endLocation, final ChannelItem moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        // 获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        // 得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView,
                initLocation);
        // 创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);// 动画时间
        // 动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) ((Activity) this.context).getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this.context);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this.context);
        iv.setImageBitmap(cache);
        return iv;
    }

    private void saveChannel() {
        ChannelManage.getManage(this.context).deleteAllChannel();
        ChannelManage.getManage(this.context).saveUserChannel(
                userAdapter.getChannnelLst());
        ChannelManage.getManage(this.context).saveOtherChannel(
                otherAdapter.getChannnelLst());
    }

//	@Override
//	public void onBackPressed() {
//		saveChannel();
//		super.onBackPressed();
//	}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_channel_btn_back:
                //saveChannel();
                hide();
                break;
        }
    }

    private void hide() {
        channelManageViewListener.hide(this,this.userChannelList,this.otherChannelList);
    }

    public void setChannelManageViewListener(ChannelManageViewListener channelManageViewListener) {
        this.channelManageViewListener = channelManageViewListener;
    }

    public interface ChannelManageViewListener {
        void hide(ViewGroup viewGroup,List<ChannelItem> lstUserChannel,List<ChannelItem> lstOtherChannel);
    }
}
