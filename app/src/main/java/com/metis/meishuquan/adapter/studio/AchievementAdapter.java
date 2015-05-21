package com.metis.meishuquan.adapter.studio;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.homepage.AchievementDetailActivity;
import com.metis.meishuquan.model.commons.Achievement;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.shared.MyInfoBtn;

import java.util.List;

/**
 * Created by WJ on 2015/5/7.
 */
public class AchievementAdapter extends BaseAdapter {

    private static final String TAG = AchievementAdapter.class.getSimpleName();

    private Context mContext = null;
    private List<AchievementDelegate> mDataList = null;

    public AchievementAdapter (Context context, List<AchievementDelegate> list) {
        mContext = context;
        mDataList = list;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public AchievementDelegate getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_achievement_item, null);
            holder = new ViewHolder();
            holder.myInfoBtn = (MyInfoBtn)view.findViewById(R.id.achievement_item_title);
            holder.layout = (LinearLayout)view.findViewById(R.id.achievement_item_img_container);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        final AchievementDelegate delegate = getItem(i);
        final Achievement achievement = delegate.achievement;
        holder.myInfoBtn.setText(achievement.getAchievementTitle());
        holder.layout.setVisibility(delegate.isShow ? View.VISIBLE : View.GONE);
        //final LinearLayout layout = holder.layout;

        holder.myInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.isShow = !delegate.isShow;
                final List<Achievement.ImageItem> imageItems = achievement.getImglist();
                final int size = imageItems.size();
                final int childCount = holder.layout.getChildCount();
                final int length = Math.max(size, childCount);
                Log.v(TAG, "size=" + size + " length=" + length + " childCount=" + childCount);
                for (int i = 0; i < length; i++) {
                    if (i < childCount && i < size) {
                        View child = holder.layout.getChildAt(i);
                        if (child instanceof ImageView) {
                            ImageView img = (ImageView)child;
                            img.setAdjustViewBounds(true);
                            img.setVisibility(View.VISIBLE);
                            ImageLoaderUtils.getImageLoader(mContext).displayImage(
                                    imageItems.get(i).getImgThumbnailUrl(), img
                            );
                        }
                    } else if (i < childCount && i >= size) {
                        holder.layout.getChildAt(i).setVisibility(View.GONE);
                    } else if (i >= childCount && i < size) {
                        ImageView img = new ImageView(mContext);
                        img.setAdjustViewBounds(true);
                        ImageLoaderUtils.getImageLoader(mContext).displayImage(
                                imageItems.get(i).getImgThumbnailUrl(), img
                        );
                        /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)img.getLayoutParams();
                        if (params == null) {
                            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 400);
                        }*/
                        holder.layout.addView(img);
                        Log.v(TAG, "AAAA i >= childCount && i < size    childCount=" + holder.layout.getChildCount());
                    }
                }
                /*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1000);
                layout.setLayoutParams(params);*/
                holder.layout.requestLayout();
                notifyDataSetChanged();
                if (viewGroup instanceof ListView) {
                    ((ListView) viewGroup).smoothScrollToPosition(getCount());
                }
                /*Intent it = new Intent(mContext, AchievementDetailActivity.class);
                it.putExtra(AchievementDetailActivity.KEY_ACHIEVEMENT_ID, achievement.getAchievementId());
                mContext.startActivity(it);*/
            }
        });
        return view;
    }

    public static class AchievementDelegate {
        private boolean isShow = false;
        public Achievement achievement;

        public AchievementDelegate (Achievement achievement) {
            this.achievement = achievement;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o instanceof AchievementDelegate) {
                return ((AchievementDelegate) o).achievement.equals(achievement);
            }
            return false;
        }
    }

    private class ViewHolder {
        public MyInfoBtn myInfoBtn;
        public LinearLayout layout;
    }
}
