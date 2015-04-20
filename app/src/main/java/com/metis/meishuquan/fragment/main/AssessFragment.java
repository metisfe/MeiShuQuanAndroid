package com.metis.meishuquan.fragment.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.assess.AssessInfoFragment;
import com.metis.meishuquan.fragment.assess.AssessPublishFragment;
import com.metis.meishuquan.fragment.assess.ChooseCityFragment;
import com.metis.meishuquan.fragment.assess.FilterConditionForAssessListFragment;
import com.metis.meishuquan.model.BLL.AssessOperator;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.assess.AllAssess;
import com.metis.meishuquan.model.assess.Assess;
import com.metis.meishuquan.model.assess.Bimp;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.AssessStateEnum;
import com.metis.meishuquan.model.enums.QueryTypeEnum;
import com.metis.meishuquan.view.popup.ChoosePhotoPopupWindow;
import com.metis.meishuquan.view.shared.DragListView;
import com.metis.meishuquan.view.shared.TabBar;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    private final String HOT = "热门点评";
    private final String NEW = "最新点评";
    private ViewGroup rootView;
    private TabBar tabBar;
    private FragmentManager fm;

    private DragListView listView;
    private Button btnRegion, btnFilter, btnPublishComment;
    private List<Assess> lstAllAssess = new ArrayList<Assess>();
    private AssessAdapter adapter;
    private Bimp bimp;
    private static final int TAKE_PHOTO = 1;
    private static final int PICK_PICTURE = 2;
    private int index = 1;
    private String photoPath = "";


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
        //TODO:定位
        AssessOperator assessOperator = AssessOperator.getInstance();
        assessOperator.AddRegionToCache();
        //加载过滤条件之标签数据
        assessOperator = AssessOperator.getInstance();
        assessOperator.addAssessChannelListToCache();//将过滤条件（年级、标签）加载至缓存
        //加载列表数据
        getData(DragListView.REFRESH, true, AssessStateEnum.ALL, null, null, index, QueryTypeEnum.ALL);

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

        this.adapter = new AssessAdapter(this.lstAllAssess);
        this.listView.setAdapter(adapter);

        this.bimp = new Bimp();
    }

    private void initEvent() {
        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {//列表刷新
            @Override
            public void onRefresh() {
                getData(DragListView.REFRESH, true, AssessStateEnum.ALL, null, null, index, QueryTypeEnum.ALL);
            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {//列表加载更多
            @Override
            public void onLoad() {
                index++;
                loadNewAssessList(DragListView.LOAD, true, AssessStateEnum.ALL, null, null, index, QueryTypeEnum.NEW);
            }
        });

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//列表项点击
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AssessInfoFragment assessInfoFragment = new AssessInfoFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.content_container, assessInfoFragment);
                ft.addToBackStack(null);
                ft.commit();
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

                ChoosePhotoPopupWindow choosePhotoPopupWindow = new ChoosePhotoPopupWindow(MainApplication.UIContext, AssessFragment.this, rootView);
                choosePhotoPopupWindow.getPath(new OnPathChannedListner() {
                    @Override
                    public void setPath(String path) {
                        photoPath = path;
                    }
                });
//                AssessPublishFragment assessPublishFragment = new AssessPublishFragment();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.add(R.id.content_container, assessPublishFragment);
//                ft.commit();
            }
        });
    }

    private void getData(final int type, boolean isAll, AssessStateEnum mType, List<Integer> grades, List<Integer> channelIds, int index, QueryTypeEnum queryTypeEnum) {
        AssessOperator assessOperator = AssessOperator.getInstance();
        assessOperator.getAssessList(isAll, mType, grades, channelIds, index, queryTypeEnum, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                AllAssess allAssess = new AllAssess();
                if (result != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    allAssess = gson.fromJson(json, new TypeToken<AllAssess>() {
                    }.getType());
                    List<Assess> data = new ArrayList<Assess>();
                    if (allAssess != null && allAssess.getData() != null) {
                        List<Assess> lastAssessLists = allAssess.getData().getLastAssessLists();//最新点评
                        List<Assess> hotAssessLists = allAssess.getData().getHotAssessLists();//热门点评
                        if (hotAssessLists != null && hotAssessLists.size() > 0) {
                            Assess assess = new Assess();
                            assess.setGroup(HOT);
                            data.add(assess);
                            data.addAll(hotAssessLists);
                        }
                        if (lastAssessLists != null && lastAssessLists.size() > 0) {
                            Assess assess = new Assess();
                            assess.setGroup(NEW);
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

    //加载最新点评
    private void loadNewAssessList(final int type, boolean isAll, AssessStateEnum mType, List<Integer> grades, List<Integer> channelIds, int index, QueryTypeEnum queryTypeEnum) {
        AssessOperator assessOperator = AssessOperator.getInstance();
        assessOperator.getAssessList(isAll, mType, grades, channelIds, index, queryTypeEnum, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                AllAssess allAssess = new AllAssess();
                if (result != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    allAssess = gson.fromJson(json, new TypeToken<AllAssess>() {
                    }.getType());
                    List<Assess> data = new ArrayList<Assess>();
                    if (allAssess != null && allAssess.getData() != null) {
                        List<Assess> lastAssessLists = allAssess.getData().getLastAssessLists();//最新点评
                        if (lastAssessLists != null && lastAssessLists.size() > 0) {
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

    public interface OnPathChannedListner {
        void setPath(String path);
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
            SmartImageView portrait, img_content;
            TextView userName, grade, createTime, content, tag;
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
            if (lstAssess.get(position).getGroup().equals(HOT) || lstAssess.get(position).getGroup().equals((NEW))) {
                return false;
            }
            return true;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup view) {
            ViewGroup viewGroup = null;
            if (viewGroup == null) {
                holder = new ViewHolder();
                Assess assess = lstAssess.get(i);
                if (assess.getGroup().equals(HOT) || assess.getGroup().equals((NEW))) {
                    viewGroup = (ViewGroup) LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_assess_list_item_group_tag, null);
                    holder.tag = (TextView) viewGroup.findViewById(R.id.id_tv_listview_tag);
                    holder.tag.setText(assess.getGroup());
                } else {
                    viewGroup = (ViewGroup) LayoutInflater.from(MainApplication.UIContext).inflate(R.layout.fragment_assess_list_item, null);
                    //holder.portrait= (SmartImageView) view.findViewById(R.id.id_img_portrait);
                    holder.userName = (TextView) viewGroup.findViewById(R.id.id_username);
                    holder.grade = (TextView) viewGroup.findViewById(R.id.id_tv_grade);
                    holder.createTime = (TextView) viewGroup.findViewById(R.id.id_createtime);
                    holder.content = (TextView) viewGroup.findViewById(R.id.id_tv_content);
                    holder.img_content = (SmartImageView) viewGroup.findViewById(R.id.id_img_content);
                    holder.tvSupportCount = (TextView) viewGroup.findViewById(R.id.id_tv_support_count);
                    holder.tvCommentCount = (TextView) viewGroup.findViewById(R.id.id_tv_comment_count);
                    holder.tvContentType = (TextView) viewGroup.findViewById(R.id.id_tv_content_type);
                    holder.tvSupportCount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TopLineOperator operator = TopLineOperator.getInstance();
                            //operator.commentSurpot(0, newsId, lstAllComments.get(i).getId(), 0, 1);
                        }
                    });

                    holder.userName.setText(assess.getUser().getName());//用户名
                    //holder.grade.setText(assess());
                    SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = sFormat.parse(assess.getCreateTime());
                        String dataStr = sFormat.format(date);
                        holder.createTime.setText(dataStr);//创建时间
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (assess.getDesc().length() > 0) {
                        holder.content.setText(assess.getDesc());//内容描述
                    } else {
                        holder.content.setHeight(0);
                    }

                    holder.img_content.setImageUrl(assess.getThumbnails().getUrl());//内容图片
                    holder.tvSupportCount.setText("赞(" + assess.getSupportCount() + ")");//赞数量
                    holder.tvCommentCount.setText("评论(" + assess.getCommentCount() + ")");//评论数量
                    holder.tvContentType.setText(assess.getAssessChannel().getChannelName());//内容类型
                    //TODO:点评状态
                }
                holder = (ViewHolder) viewGroup.getTag();
            }
            return viewGroup;
        }
    }
}
