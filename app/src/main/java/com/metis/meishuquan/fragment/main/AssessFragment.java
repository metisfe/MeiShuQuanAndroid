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
import com.metis.meishuquan.model.assess.AllAssess;
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
    private AssessAdapter adapter;

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
        //getData(DragListView.REFRESH, true, 1, null, null, 1, 0);

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

        this.adapter = new AssessAdapter(this.lstAllAssess);
    }

    private void initEvent() {
        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {//刷新
            @Override
            public void onRefresh() {
                //getData(DragListView.REFRESH, true, 1, null, null, 1, 0);
            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {//加载更多
            @Override
            public void onLoad() {
                //getData(DragListView.LOAD, true, 1, null, null, 1, 0);
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

    private void getData(final int type, boolean isAll, int mType, List<Integer> grades, List<Integer> channelIds, int index, int queryType) {
        AssessOperator assessOperator = AssessOperator.getInstance();
        assessOperator.getAssessList(isAll, mType, grades, channelIds, index, queryType, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                AllAssess allAssess = new AllAssess();
                if (result != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    allAssess = gson.fromJson(json, new TypeToken<AllAssess>() {
                    }.getType());
                    List<Assess> data = new ArrayList<>();
                    if (allAssess != null) {
                        List<Assess> lastAssessLists = allAssess.getData().getLastAssessLists();//最新点评
                        List<Assess> hotAssessLists = allAssess.getData().getHotAssessLists();//热门点评
                        if (hotAssessLists != null && hotAssessLists.size() > 0) {
                            Assess assess = new Assess();
                            assess.setGroup("热门点评");
                            data.add(assess);
                            data.addAll(hotAssessLists);
                        }
                        if (lastAssessLists != null && lastAssessLists.size() > 0) {
                            Assess assess = new Assess();
                            assess.setGroup("最新点评");
                            data.add(assess);
                            data.addAll(lastAssessLists);
                        }
                    }
                    Message msg = handler.obtainMessage();
                    msg.what = type;
                    msg.obj = data;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 点评列表适配器
     */
    private class AssessAdapter extends BaseAdapter {
        private List<Assess> lstAllAssess = new ArrayList<>();
        private ViewHolder holder;

        public AssessAdapter(List<Assess> lstAllComments) {
            this.lstAllAssess = lstAllComments;
        }

        private class ViewHolder {
            SmartImageView portrait;
            TextView userName, source, notifyTime, content, tag;
            Button btn_support, btn_comment;

        }

        @Override
        public int getCount() {
            return lstAllAssess.size();
        }

        @Override
        public Object getItem(int i) {
            return lstAllAssess.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean isEnabled(int position) {
            if (lstAllAssess.get(position).getGroup().equals("热门评论") || lstAllAssess.get(position).getGroup().equals(("最新评论"))) {
                return false;
            }
            return super.isEnabled(position);
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            if (convertView == null) {
                holder = new ViewHolder();
                if (lstAllAssess.get(i).getGroup().equals("热门评论") || lstAllAssess.get(i).getGroup().equals(("最新评论"))) {
                    view = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_topline_comment_list_item_tag, null);
                    holder.tag = (TextView) view.findViewById(R.id.id_tv_listview_tag);
                    holder.tag.setText(lstAllAssess.get(i).getGroup());
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

                    holder.userName.setText(this.lstAllAssess.get(i).getUser().getName());
                    //holder.source.setText(this.lstAllComments.get(i).getCommentDateTime());
                    holder.notifyTime.setText(this.lstAllAssess.get(i).getCreateTime());
                    holder.content.setText(this.lstAllAssess.get(i).getDesc());
                }
                holder = (ViewHolder) view.getTag();
            }
            return view;
        }
    }
}
