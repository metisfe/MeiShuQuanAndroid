package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.assess.AssessInfoActivity;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.assess.Assess;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MyCommentsActivity extends DataListActivity {

    public static final String KEY_TYPE = "type";

    private List<Assess> mData = new ArrayList<Assess>();
    private int mIndex = 1;

    private int mType = 0;

    @Override
    public String getTitleText() {
        return getString(R.string.my_info_comments);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mType = getIntent().getIntExtra(KEY_TYPE, mType);
        super.onCreate(savedInstanceState);
        if (mType == 1) {
            setTitleCenter(getString(R.string.my_info_asks));
        }
    }

    @Override
    public void loadData(int index) {
        UserInfoOperator.getInstance().getQuestionList(MainApplication.userInfo.getUserId(), index, mType, new UserInfoOperator.OnGetListener<List<Assess>>() {
            @Override
            public void onGet(boolean succeed, List<Assess> data) {
                if (succeed) {
                    mIndex = mIndex + 1;
                    mData.addAll(data);
                    setAdapter(new CommentAdapter());
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(MyCommentsActivity.this, R.string.common_load_faild, Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*UserInfoOperator.getInstance().getCommentsList(MainApplication.userInfo.getUserId() + "", index, new UserInfoOperator.OnGetListener(){

            @Override
            public void onGet(boolean succeed, Object o) {

            }
        });*/
    }

    @Override
    public void onLoadMore() {
        loadData(mIndex + 1);
    }

    @Override
    public void onRefresh() {
        mIndex = 1;
        loadData(1);
    }

    public class CommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Assess getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(MyCommentsActivity.this)
                        .inflate(R.layout.fragment_assess_list_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.profileIv = (ImageView)convertView.findViewById(R.id.id_img_portrait);
                holder.nameTv = (TextView)convertView.findViewById(R.id.id_username);
                holder.gradeTv = (TextView)convertView.findViewById(R.id.id_tv_grade);
                holder.createTimeTv = (TextView)convertView.findViewById(R.id.id_createtime);
                holder.contentTv = (TextView)convertView.findViewById(R.id.id_tv_content);
                holder.imageContentIv = (ImageView)convertView.findViewById(R.id.id_img_content);
                holder.commentCountTv = (TextView)convertView.findViewById(R.id.id_tv_comment_count);
                holder.supportCountTv = (TextView)convertView.findViewById(R.id.id_tv_support_count);
                holder.contentTypeTv = (TextView)convertView.findViewById(R.id.id_tv_content_type);
                holder.commentStateTv = (TextView)convertView.findViewById(R.id.id_tv_comment_state);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            final Assess comment = getItem(position);
            User user = comment.getUser();
            ImageLoader loader = ImageLoaderUtils.getImageLoader(MyCommentsActivity.this);
            loader.displayImage(user.getAvatar(), holder.profileIv,
                    ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.my_info_profile_size)));
            holder.nameTv.setText(user.getName());
            holder.contentTv.setText(comment.getDesc());
            ImageLoaderUtils.getImageLoader(MainApplication.UIContext)
                    .displayImage(
                            comment.getThumbnails().getUrl(),
                            holder.imageContentIv,
                            ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(MyCommentsActivity.this, AssessInfoActivity.class);
                    it.putExtra("assess", comment);
                    startActivity(it);
                }
            });
            holder.imageContentIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent (MyCommentsActivity.this, ImagePreviewActivity.class);
                    String[] array = {comment.getOriginalImage().getUrl()};
                    it.putExtra(ImagePreviewActivity.KEY_IMAGE_URL_ARRAY, array);
                    startActivity(it);
                }
            });
            return convertView;
        }
    }

    private class ViewHolder {
        public ImageView profileIv, imageContentIv;
        public TextView nameTv, gradeTv, createTimeTv,
                        contentTv, commentCountTv,
                        supportCountTv, contentTypeTv, commentStateTv;
    }
}
