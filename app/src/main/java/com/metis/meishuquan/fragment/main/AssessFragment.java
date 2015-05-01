package com.metis.meishuquan.fragment.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.assess.AssessInfoActivity;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.fragment.assess.AssessPublishFragment;
import com.metis.meishuquan.fragment.assess.ChooseCityFragment;
import com.metis.meishuquan.fragment.assess.FilterConditionForAssessListFragment;
import com.metis.meishuquan.model.BLL.AssessOperator;
import com.metis.meishuquan.model.assess.AllAssess;
import com.metis.meishuquan.model.assess.Assess;
import com.metis.meishuquan.model.assess.AssessListFilter;
import com.metis.meishuquan.model.assess.Channel;
import com.metis.meishuquan.model.assess.Grade;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.AssessStateEnum;
import com.metis.meishuquan.model.enums.QueryTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.popup.ChoosePhotoPopupWindow;
import com.metis.meishuquan.view.shared.DragListView;
import com.metis.meishuquan.view.shared.TabBar;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 模块：点评
 * <p/>
 * Created by wj on 3/15/2015.
 */
public class AssessFragment extends Fragment {
    private static final String TAG = "getAssessComment";
    private static final int TAKE_PHOTO = 1;
    private static final int PICK_PICTURE = 2;
    private ViewGroup rootView;
    private TabBar tabBar;

    private DragListView listView;
    private Button btnRegion, btnFilter, btnPublishComment;
    private Button btnRecommend, btnNewPublish, btnHotComment;

    private List<Assess> lstAllAssess = new ArrayList<Assess>();
    private AssessListFilter assessListFilter = new AssessListFilter();
    private AssessAdapter adapter;
    private QueryTypeEnum type = QueryTypeEnum.RECOMMEND;
    private AssessStateEnum assessState = AssessStateEnum.ALL;

    private int regionId;
    private int index = 1;
    private String photoPath = "";
    private FragmentManager fm;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO:定位
        AssessOperator assessOperator = AssessOperator.getInstance();
        assessOperator.AddRegionToCache();
        //加载过滤条件之标签数据
        assessOperator = AssessOperator.getInstance();
        assessOperator.addAssessChannelListToCache();//将过滤条件（年级、标签）加载至缓存
        //加载列表数据
        getData(DragListView.REFRESH, regionId, true, assessState, assessListFilter.getLstSelectedGradeIds(), assessListFilter.getLstSelectedChannelIds(), QueryTypeEnum.RECOMMEND, index);

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_commentfragment, container, false);
        initView(rootView);
        initEvent();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FragmentActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO://拍照
                    openAssessPublishFragment(photoPath);
                    break;
                case PICK_PICTURE://相册
                    Uri uri = data.getData();
                    try {
                        //根据URL得到Bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(MainApplication.UIContext.getContentResolver(), uri);
                        //将返回的图片存入指定路径，以便上传至服务器
                        File dir = new File(Environment.getExternalStorageDirectory()
                                + "/myimage/");
                        if (!dir.exists()) {
                            dir.mkdir();
                        } else {
                            File[] files = dir.listFiles();
                            for (int i = 0; i < files.length; i++) {
                                if (files[i].isFile()) {
                                    files[i].delete();
                                }
                            }
                        }
                        File file = new File(dir, String.valueOf(System.currentTimeMillis()) + ".jpg");
                        photoPath = file.getPath();

                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();

                        openAssessPublishFragment(photoPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void openAssessPublishFragment(String photoPath) {
        AssessPublishFragment assessPublishFragment = new AssessPublishFragment();
        Bundle bundle = new Bundle();
        bundle.putString("photoPath", photoPath);
        assessPublishFragment.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_container, assessPublishFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void initView(ViewGroup rootView) {
        this.fm = getActivity().getSupportFragmentManager();
        this.tabBar = (TabBar) rootView.findViewById(R.id.fragment_shared_commentfragment_tab_bar);
        this.tabBar.setTabSelectedListener(MainApplication.MainActivity);
        this.listView = (DragListView) rootView.findViewById(R.id.id_fragment_comment_listview);
        this.btnRegion = (Button) rootView.findViewById(R.id.id_btn_region);
        this.btnPublishComment = (Button) rootView.findViewById(R.id.id_btn_assess_comment);
        this.btnFilter = (Button) rootView.findViewById(R.id.id_btn_commentlist_filter);
        this.btnRecommend = (Button) rootView.findViewById(R.id.id_btn_recommend);//推荐
        this.btnNewPublish = (Button) rootView.findViewById(R.id.id_btn_new_publish);//最新
        this.btnHotComment = (Button) rootView.findViewById(R.id.id_btn_hot_course);//最多评论

        this.adapter = new AssessAdapter(this.lstAllAssess);
        this.listView.setAdapter(adapter);

    }

    private void initEvent() {
        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {//列表刷新
            @Override
            public void onRefresh() {
                getData(DragListView.REFRESH, regionId, true, assessState, assessListFilter.getLstSelectedGradeIds(), assessListFilter.getLstSelectedChannelIds(), type, index);
            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {//列表加载更多
            @Override
            public void onLoad() {
                getData(DragListView.LOAD, regionId, true, assessState, assessListFilter.getLstSelectedGradeIds(), assessListFilter.getLstSelectedChannelIds(), type, index);
                index++;
            }
        });

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//列表项点击
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position >= lstAllAssess.size() + 1) return;
                final Assess assess = lstAllAssess.get(position - 1);
                Intent intent = new Intent(getActivity(), AssessInfoActivity.class);
                intent.putExtra("assess", (Serializable) assess);
                startActivity(intent);
            }
        });

        //全国
        this.btnRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseCityFragment chooseCityFragment = new ChooseCityFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.content_container, chooseCityFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //过滤条件
        this.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterConditionForAssessListFragment filterConditionForAssessListFragment = new FilterConditionForAssessListFragment();
                filterConditionForAssessListFragment.setFilterConditionListner(new OnFilterCannedListner() {
                    @Override
                    public void setFilter(AssessListFilter assessListFilter) {
                        //添加至本地保存
                        Gson gson = new Gson();
                        String json = gson.toJson(assessListFilter);
                        Log.i("点评列表筛选条件", json);
                        SharedPreferencesUtil.getInstanse(MainApplication.UIContext).update(SharedPreferencesUtil.CHECKED_ASSESS_FILTER, json);
                        //TODO:根据筛选条件刷新列表
                    }
                });
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.content_container, filterConditionForAssessListFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        //提问
        this.btnPublishComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainApplication.isLogin()) {
                    ChoosePhotoPopupWindow choosePhotoPopupWindow = new ChoosePhotoPopupWindow(MainApplication.UIContext, AssessFragment.this, rootView);
                    choosePhotoPopupWindow.getPath(new OnPathChannedListner() {
                        @Override
                        public void setPath(String path) {
                            photoPath = path;
                        }
                    });
                } else {
                    startActivity(new Intent(MainApplication.UIContext, LoginActivity.class));
                }
            }
        });

        this.btnRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnRecommend);
                setButtonUnChecked(new Button[]{btnNewPublish, btnHotComment});
                type = QueryTypeEnum.RECOMMEND;
                if (regionId == 0) {
                    getData(DragListView.REFRESH, regionId, true, assessState, assessListFilter.getLstSelectedGradeIds(), assessListFilter.getLstSelectedChannelIds(), type, index);
                } else {
                    getData(DragListView.REFRESH, regionId, false, assessState, assessListFilter.getLstSelectedGradeIds(), assessListFilter.getLstSelectedChannelIds(), type, index);
                }
            }
        });

        this.btnNewPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnNewPublish);
                setButtonUnChecked(new Button[]{btnRecommend, btnHotComment});
                type = QueryTypeEnum.NEW;
                if (regionId == 0) {
                    getData(DragListView.REFRESH, regionId, true, assessState, assessListFilter.getLstSelectedGradeIds(), assessListFilter.getLstSelectedChannelIds(), type, index);
                } else {
                    getData(DragListView.REFRESH, regionId, false, assessState, assessListFilter.getLstSelectedGradeIds(), assessListFilter.getLstSelectedChannelIds(), type, index);
                }
            }
        });

        this.btnHotComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonChecked(btnHotComment);
                setButtonUnChecked(new Button[]{btnNewPublish, btnRecommend});
                type = QueryTypeEnum.HOT;
                if (regionId == 0) {
                    getData(DragListView.REFRESH, regionId, true, assessState, assessListFilter.getLstSelectedGradeIds(), assessListFilter.getLstSelectedChannelIds(), type, index);
                } else {
                    getData(DragListView.REFRESH, regionId, false, assessState, assessListFilter.getLstSelectedGradeIds(), assessListFilter.getLstSelectedChannelIds(), type, index);
                }
            }
        });
    }

    private void setButtonChecked(Button btn) {
        btn.setTextColor(Color.rgb(251, 109, 109));
    }

    private void setButtonUnChecked(Button[] btns) {
        for (Button button : btns) {
            button.setTextColor(Color.rgb(126, 126, 126));
        }
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

    public interface OnPathChannedListner {
        void setPath(String path);
    }

    public interface OnFilterCannedListner {
        void setFilter(AssessListFilter assessListFilter);
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
            Assess assess = lstAssess.get(i);
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
            holder.img_content.setMinimumWidth(assess.getThumbnails().getWidth() * 2);
            holder.img_content.setMinimumHeight(assess.getThumbnails().getHeigth() * 2);
            ImageLoaderUtils.getImageLoader(MainApplication.UIContext).displayImage(assess.getThumbnails().getUrl(), holder.img_content, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.img_topline_default));
            //预览大图
            holder.img_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO:
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
