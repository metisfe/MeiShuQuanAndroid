package com.metis.meishuquan.fragment.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.BaseFragment;
import com.metis.meishuquan.model.BLL.AssessOperator;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.assess.Assess;
import com.metis.meishuquan.model.assess.AssessData;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.topline.Comment;
import com.metis.meishuquan.view.shared.DragListView;
import com.metis.meishuquan.view.shared.TabBar;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块：点评
 * <p/>
 * Created by wudi on 3/15/2015.
 */
public class AssessFragment extends BaseFragment {

    private TabBar tabBar;

    private DragListView listView;
    private Button btnRegion, btnFilter, btnPublishComment;
    private List<Assess> lstAllAssess = new ArrayList<>();
    private CommentsAdapter adapter;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Assess> result = (List<Assess>) msg.obj;
            switch (msg.what) {
                case DragListView.REFRESH:
                    listView.onRefreshComplete();
                    lstAllAssess.clear();
                    lstAllAssess.addAll(result);
                    break;
                case DragListView.LOAD:
                    listView.onLoadComplete();
                    lstAllAssess.addAll(result);
                    break;
            }
            listView.setResultSize(result.size());
            adapter.notifyDataSetChanged();
        }

        ;
    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载列表数据
        //getData();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_commentfragment, container, false);
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_commentfragment_tab_bar);
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);
        this.listView = (DragListView) rootView.findViewById(R.id.id_fragment_comment_listview);
        this.btnRegion = (Button) rootView.findViewById(R.id.id_btn_region);
        this.btnPublishComment = (Button) rootView.findViewById(R.id.id_btn_assess_comment);
        this.btnFilter = (Button) rootView.findViewById(R.id.id_btn_commentlist_filter);
    }

    private void initEvent() {
        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {//刷新
            @Override
            public void onRefresh() {

            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {//加载更多
            @Override
            public void onLoad() {

            }
        });

        this.btnRegion.setOnClickListener(new View.OnClickListener() {//区域
            @Override
            public void onClick(View v) {

            }
        });

        this.btnFilter.setOnClickListener(new View.OnClickListener() {//过滤条件
            @Override
            public void onClick(View v) {

            }
        });

        this.btnPublishComment.setOnClickListener(new View.OnClickListener() {//发表评论
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getData(boolean isAll, final int type, List<Integer> grades, List<Integer> channelIds, int index) {
        AssessOperator assessOperator = AssessOperator.getInstance();
        assessOperator.getAssessList(isAll, type, grades, channelIds, index, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                AssessData assessData = new AssessData();
                if (result != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    assessData = gson.fromJson(json, new TypeToken<AssessData>() {}.getType());
                    if (assessData != null) {

                    }
                    Message msg = handler.obtainMessage();
                    msg.what = type;
                    msg.obj = assessData;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 点评列表适配器
     */
    private class CommentsAdapter extends BaseAdapter {
        private List<Comment> lstAllComments = new ArrayList<>();
        private ViewHolder holder;

        public CommentsAdapter(List<Comment> lstAllComments) {
            this.lstAllComments = lstAllComments;
        }

        private class ViewHolder {
            SmartImageView portrait;
            TextView userName, source, notifyTime, content, tag;
            Button btn_support, btn_comment;

        }

        @Override
        public int getCount() {
            return lstAllComments.size();
        }

        @Override
        public Object getItem(int i) {
            return lstAllComments.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean isEnabled(int position) {
            if (lstAllComments.get(position).getGroup().equals("热门评论") || lstAllComments.get(position).getGroup().equals(("最新评论"))) {
                return false;
            }
            return super.isEnabled(position);
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            if (convertView == null) {
                holder = new ViewHolder();
                if (lstAllComments.get(i).getGroup().equals("热门评论") || lstAllComments.get(i).getGroup().equals(("最新评论"))) {
                    view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_topline_comment_list_item_tag, null);
                    holder.tag = (TextView) view.findViewById(R.id.id_tv_listview_tag);
                    holder.tag.setText(lstAllComments.get(i).getGroup());
                } else {
                    view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_assess_list_item, null);
                    //holder.portrait= (SmartImageView) view.findViewById(R.id.id_img_portrait);
                    holder.userName = (TextView) view.findViewById(R.id.id_username);
                    //holder.source= (TextView) view.findViewById(R.id.id_username);
                    holder.notifyTime = (TextView) view.findViewById(R.id.id_notifytime);
                    holder.content = (TextView) view.findViewById(R.id.id_textview_comment_content);
                    holder.btn_support = (Button) view.findViewById(R.id.id_btn_support);
                    holder.btn_comment = (Button) view.findViewById(R.id.id_btn_comment);
                    holder.btn_support.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TopLineOperator operator = TopLineOperator.getInstance();
                            //operator.commentSurpot(0, newsId, lstAllComments.get(i).getId(), 0, 1);
                        }
                    });

                    holder.userName.setText(this.lstAllComments.get(i).getUser().getName());
                    //holder.source.setText(this.lstAllComments.get(i).getCommentDateTime());
                    holder.notifyTime.setText(this.lstAllComments.get(i).getCommentDateTime());
                    holder.content.setText(this.lstAllComments.get(i).getContent());
                }
                holder = (ViewHolder) view.getTag();
            }
            return view;
        }
    }
}
