package com.metis.meishuquan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.List;

/**
 * Created by WJ on 2015/5/7.
 */
public class ImgTitleSubAdapter extends RecyclerView.Adapter<ImgTitleSubAdapter.ImgTitleSubHolder>{

    private Context mContext = null;
    private List<? extends ImgTitleSubImpl> mDataList = null;

    public ImgTitleSubAdapter (Context context, List<? extends ImgTitleSubImpl> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public ImgTitleSubHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_img_title_sub, null);
        return new ImgTitleSubHolder(view);
    }

    @Override
    public void onBindViewHolder(ImgTitleSubHolder holder, int position) {
        ImgTitleSubImpl t = mDataList.get(position);
        holder.titleTv.setText(t.getTitle());
        holder.subTitleTv.setText(t.getSubTitle());
        ImageLoaderUtils.getImageLoader(mContext).displayImage(
                t.getImageUrl(), holder.profileIv,
                ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher)
        );
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ImgTitleSubHolder extends RecyclerView.ViewHolder {

        public ImageView profileIv = null;
        public TextView titleTv = null;
        public TextView subTitleTv = null;

        public ImgTitleSubHolder(View itemView) {
            super(itemView);
            profileIv = (ImageView)itemView.findViewById(R.id.img_title_sub_profile);
            titleTv = (TextView)itemView.findViewById(R.id.img_title_sub_title);
            subTitleTv = (TextView)itemView.findViewById(R.id.img_title_sub_sub);
        }
    }

}