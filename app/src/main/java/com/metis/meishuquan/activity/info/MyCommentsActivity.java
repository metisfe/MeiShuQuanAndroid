package com.metis.meishuquan.activity.info;

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
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.Comment;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MyCommentsActivity extends DataListActivity {

    private List<Comment> mData = new ArrayList<Comment>();
    private int mIndex = 1;

    @Override
    public String getTitleText() {
        return getString(R.string.my_info_comments);
    }

    @Override
    public void loadData(int index) {
        /*UserInfoOperator.getInstance().getQuestionList(MainApplication.userInfo.getUserId(), index, 0, new UserInfoOperator.OnGetListener<List<Comment>>() {
            @Override
            public void onGet(boolean succeed, List<Comment> data) {
                if (succeed) {
                    mData.addAll(data);
                    setAdapter(new CommentAdapter());
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(MyCommentsActivity.this, R.string.common_load_faild, Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        UserInfoOperator.getInstance().getCommentsList(MainApplication.userInfo.getUserId() + "", index, new UserInfoOperator.OnGetListener(){

            @Override
            public void onGet(boolean succeed, Object o) {

            }
        });
    }

    @Override
    public void onLoadMore() {
        loadData(++mIndex);
    }

    @Override
    public void onRefresh() {
        mIndex = 1;
        loadData(1);
    }

    private class CommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Comment getItem(int position) {
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
                        .inflate(R.layout.fragment_comment_list_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.profileIv = (ImageView)convertView.findViewById(R.id.id_img_portrait);
                holder.nameTv = (TextView)convertView.findViewById(R.id.id_username);
                holder.locationTv = (TextView)convertView.findViewById(R.id.textView2);
                holder.contentTv = (TextView)convertView.findViewById(R.id.id_textview_comment_content);
                holder.notifyTimeTv = (TextView)convertView.findViewById(R.id.id_notifytime);
                holder.supportBtn = (Button)convertView.findViewById(R.id.id_btn_support);
                holder.supportCountTv = (TextView)convertView.findViewById(R.id.id_tv_support_count);
                holder.commentBtn = (Button)convertView.findViewById(R.id.id_btn_comment);
                holder.commentCountTv = (TextView)convertView.findViewById(R.id.id_tv_add_one);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Comment comment = getItem(position);
            User user = comment.getUser();
            ImageLoader loader = ImageLoaderUtils.getImageLoader(MyCommentsActivity.this);
            loader.displayImage(user.getAvatar(), holder.profileIv,
                    ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.my_info_profile_size)));
            holder.nameTv.setText(user.getName());
            holder.contentTv.setText(comment.getDesc());
            return convertView;
        }
    }

    private class ViewHolder {
        public ImageView profileIv;
        public TextView nameTv;
        public TextView locationTv;
        public TextView notifyTimeTv;
        public TextView contentTv;
        public Button supportBtn;
        public TextView supportCountTv;
        public Button commentBtn;
        public TextView commentCountTv;
    }
}
