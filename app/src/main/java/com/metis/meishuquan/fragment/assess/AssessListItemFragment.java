package com.metis.meishuquan.fragment.assess;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.assess.AssessInfoActivity;
import com.metis.meishuquan.activity.info.ImagePreviewActivity;
import com.metis.meishuquan.model.BLL.AssessOperator;
import com.metis.meishuquan.model.assess.AllAssess;
import com.metis.meishuquan.model.assess.Assess;
import com.metis.meishuquan.model.assess.AssessListFilter;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.AssessStateEnum;
import com.metis.meishuquan.model.enums.QueryTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.shared.DragListView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Fragment:点评列表切换项（精选、最新、最热）
 * <p/>
 * Created by wangjin on 15/5/5.
 */
public class AssessListItemFragment extends Fragment {

    public static final String KEY_IMAGE_URL_ARRAY = "image_url_array";
    private static final String KEY_LIST_FILTER = "assessListFilter";

    private DragListView listView;

    private AssessAdapter adapter;
    private boolean isAll = true;
    private int regionId = 0;//省份或城市Id
    private int index = 1;
    private AssessListFilter assessListFilter = null;
    private AssessStateEnum assessState;
    private QueryTypeEnum queryType;

    private List<Assess> lstAllAssess = new ArrayList<Assess>();
    private ArrayList<String> lstImgUrl = new ArrayList<String>();//图片预览Url集合

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化视图
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_assess_list, null, false);
        initView(rootView);
        initEvent();
        return rootView;
    }

    public void setAssessState(AssessStateEnum assessState) {
        this.assessState = assessState;
    }

    public void setQueryType(QueryTypeEnum queryType) {
        this.queryType = queryType;
    }

    public void setAssessListFilter(AssessListFilter assessListFilter) {
        this.assessListFilter = assessListFilter;
        //加载数据
        if (assessListFilter.getLstSelectedGrade().size() > 0 || assessListFilter.getLstSelectedChannel().size() > 0 || assessListFilter.getAssessState() != AssessStateEnum.ALL) {
            isAll = false;
        }
        getData(DragListView.REFRESH, regionId, isAll, assessListFilter.getAssessState(), assessListFilter.getLstSelectedGradeIds(),
                assessListFilter.getLstSelectedChannelIds(), queryType, index);
    }

    private void initView(ViewGroup rootView) {
        this.listView = (DragListView) rootView.findViewById(R.id.id_listview_assess_fragment);
        this.adapter = new AssessAdapter(this.lstAllAssess);
        this.listView.setAdapter(adapter);
    }

    private void initEvent() {
        //列表刷新
        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(DragListView.REFRESH, regionId, isAll, assessListFilter.getAssessState(), assessListFilter.getLstSelectedGradeIds(),
                        assessListFilter.getLstSelectedChannelIds(), queryType, index);
            }
        });

        //列表加载更多
        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {
            @Override
            public void onLoad() {
                getData(DragListView.LOAD, regionId, isAll, assessListFilter.getAssessState(), assessListFilter.getLstSelectedGradeIds(),
                        assessListFilter.getLstSelectedChannelIds(), queryType, index);
                index++;
            }
        });

        //列表项点击
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position >= lstAllAssess.size() + 1) return;
                final Assess assess = lstAllAssess.get(position - 1);
                Intent intent = new Intent(getActivity(), AssessInfoActivity.class);
                intent.putExtra("assess", (Serializable) assess);
                startActivity(intent);
            }
        });
    }

    private void getData(final int loadingType, int region, boolean isAll, AssessStateEnum mType, List<Integer> grades, List<Integer> channelIds, final QueryTypeEnum queryTypeEnum, final int index) {
        AssessOperator assessOperator = AssessOperator.getInstance();
        assessOperator.getAssessList(region, isAll, mType, grades, channelIds, index, queryTypeEnum, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                AllAssess allAssess = new AllAssess();
                if (result != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    Log.i("assesslist", json);
                    allAssess = gson.fromJson(json, new TypeToken<AllAssess>() {
                    }.getType());
                    List<Assess> assessList = null;
                    if (loadingType == listView.REFRESH) {
                        if (queryTypeEnum == QueryTypeEnum.RECOMMEND) {
                            assessList = allAssess.getData().getSelectAssessList();//推荐点评
                        } else if (queryTypeEnum == QueryTypeEnum.HOT) {
                            assessList = allAssess.getData().getHotAssessList();//热门点评
                        } else if (queryTypeEnum == QueryTypeEnum.NEW) {
                            assessList = allAssess.getData().getLastAssessList();//最新点评
                        }
                        listView.onRefreshComplete();
                        lstAllAssess.clear();
                        lstAllAssess.addAll(assessList);
                    } else if (loadingType == listView.LOAD) {
                        if (queryTypeEnum == QueryTypeEnum.RECOMMEND) {
                            assessList = allAssess.getData().getSelectAssessList();//推荐点评
                        } else if (queryTypeEnum == QueryTypeEnum.HOT) {
                            assessList = allAssess.getData().getHotAssessList();//热门点评
                        } else if (queryTypeEnum == QueryTypeEnum.NEW) {
                            assessList = allAssess.getData().getLastAssessList();//最新点评
                        }
                        listView.onLoadComplete();
                        lstAllAssess.addAll(assessList);
                    }
                    listView.setResultSize(assessList.size());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 点评列表适配器
     */
    private class AssessAdapter extends BaseAdapter {
        private List<Assess> lstAssess = new ArrayList<Assess>();
        private ViewHolder holder;

        public AssessAdapter(List<Assess> lstAllComments) {
            this.lstAssess = lstAllComments;
        }

        private class ViewHolder {
            ImageView portrait, img_content;
            TextView userName, grade, createTime, content;
            TextView tvSupportCount, tvCommentCount, tvContentType, tvCommentState;

        }

        @Override
        public int getCount() {
            return lstAssess.size();
        }

        @Override
        public Assess getItem(int i) {
            return lstAssess.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup view) {
            final Assess assess = lstAssess.get(i);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_assess_list_item, null);
                holder.portrait = (ImageView) convertView.findViewById(R.id.id_img_portrait);
                holder.userName = (TextView) convertView.findViewById(R.id.id_username);
                holder.grade = (TextView) convertView.findViewById(R.id.id_tv_grade);
                holder.createTime = (TextView) convertView.findViewById(R.id.id_createtime);
                holder.content = (TextView) convertView.findViewById(R.id.id_tv_content);
                holder.img_content = (ImageView) convertView.findViewById(R.id.id_img_content);
                holder.tvSupportCount = (TextView) convertView.findViewById(R.id.id_tv_support_count);
                holder.tvCommentCount = (TextView) convertView.findViewById(R.id.id_tv_comment_count);
                holder.tvContentType = (TextView) convertView.findViewById(R.id.id_tv_content_type);
                holder.tvCommentState = (TextView) convertView.findViewById(R.id.id_tv_comment_state);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //用户名
            holder.userName.setText(assess.getUser().getName());

            //用户头像
            if (!assess.getUser().getAvatar().isEmpty()) {
                ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(assess.getUser().getAvatar(), holder.portrait, ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.user_portrait_height), R.drawable.default_user_dynamic));
            }

            //年级
            //holder.grade.setText(assess());

            //创建时间
            SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date = sFormat.parse(assess.getCreateTime());
                String dataStr = sFormat.format(date);
                holder.createTime.setText(dataStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //内容描述
            if (assess.getDesc().length() > 0) {
                holder.content.setText(assess.getDesc());
            } else {
                holder.content.setHeight(0);
            }

            //内容图片
            if (assess.getThumbnails().getUrl().equals("")){
                holder.img_content.setVisibility(View.GONE);
            }else{
                holder.img_content.setVisibility(View.VISIBLE);
            }
            holder.img_content.setMinimumWidth(assess.getThumbnails().getWidth() * 2);
            holder.img_content.setMinimumHeight(assess.getThumbnails().getHeigth() * 2);
            ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(assess.getThumbnails().getUrl(), holder.img_content);
            //预览大图
            holder.img_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lstImgUrl.clear();
                    lstImgUrl.add(assess.getOriginalImage().getUrl());
                    Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
                    intent.putStringArrayListExtra(KEY_IMAGE_URL_ARRAY, lstImgUrl);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_zoomin, 0);
                }
            });

            holder.tvSupportCount.setText("赞(" + assess.getSupportCount() + ")");//赞数量
            holder.tvCommentCount.setText("评论(" + assess.getCommentCount() + ")");//评论数量
            holder.tvContentType.setText(assess.getAssessChannel().getChannelName());//内容类型
            //设置点评状态
            if (assess.getCommentCount() > 0) {
                holder.tvCommentState.setText("已点评");
            }
            return convertView;
        }
    }
}
