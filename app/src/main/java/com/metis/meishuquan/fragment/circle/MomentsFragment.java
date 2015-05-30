package com.metis.meishuquan.fragment.circle;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.circle.CircleMomentAdapter;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CircleMoments;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.view.shared.DragListView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jx on 4/7/2015.
 */
public class MomentsFragment extends CircleBaseFragment {
    public static final String CLASS_NAME=MomentsFragment.class.getSimpleName();

    @Override
    public void timeToSetTitleBar() {

        getTitleBar().setText(MainApplication.userInfo.getName().equals("") ? "朋友圈" : MainApplication.userInfo.getName());
        getTitleBar().setRightButton("", R.drawable.icon_pic, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostMomentFragment postMomentFragment = new PostMomentFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.content_container, postMomentFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    private DragListView listView;
    private List<CCircleDetailModel> list = new ArrayList<>();
    private CircleMomentAdapter circleMomentAdapter;
    private FragmentManager fm = null;

    private ProgressDialog progressDialog;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressDialog = ProgressDialog.show(getActivity(), "", "正在加载...", true, true);
            getData(GlobalData.momentsGroupId, 0, DragListView.REFRESH);
        }
    };

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(CLASS_NAME); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(CLASS_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_circle_momentsfragment, container, false);
        fm = getActivity().getSupportFragmentManager();
        this.listView = (DragListView) contextView.findViewById(R.id.fragment_circle_moments_list);
        circleMomentAdapter = new CircleMomentAdapter(getActivity(), list, contextView);
        this.listView.setAdapter(circleMomentAdapter);

        getData(GlobalData.momentsGroupId, 0, DragListView.REFRESH);

        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(GlobalData.momentsGroupId, 0, DragListView.REFRESH);
            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {
            @Override
            public void onLoad() {
                int momentLastId = list != null && list.size() > 0 ? list.get(list.size() - 1).id : 0;
                getData(GlobalData.momentsGroupId, momentLastId, DragListView.LOAD);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MomentDetailFragment momentDetailFragment = new MomentDetailFragment();
//                Bundle args = new Bundle();
//                args.putInt("newsId", newsId);
//                itemInfoFragment.setArguments(args);

                // refresh load more listview has header
                if (list.size()==0){
                    return;
                }
                if (position == list.size() + 1 || position == 0) {
                    return;
                }
                GlobalData.moment = list.get(position - 1);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, momentDetailFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

//        //TODO: remove
//        {
//            MomentCommentFragment momentCommentFragment = new MomentCommentFragment();
//
//            FragmentManager fm = getActivity().getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
//            ft.add(R.id.content_container, momentCommentFragment);
//            ft.addToBackStack(null);
//            ft.commit();
//        }

        LocalBroadcastManager.getInstance(MainApplication.UIContext).registerReceiver(receiver, new IntentFilter("update_moments_list"));

        return contextView;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(MainApplication.UIContext).unregisterReceiver(receiver);
        super.onDestroy();
    }

//    public void getMyMoments() {
//        StudioOperator.getInstance().getMyCircleList(MainApplication.userInfo.getUserId(), 0, new UserInfoOperator.OnGetListener<List<CCircleDetailModel>>() {
//            @Override
//            public void onGet(boolean succeed, List<CCircleDetailModel> cCircleDetailModels) {
//                if (succeed) {
//                    circleMomentAdapter = new CircleMomentAdapter(getActivity(), cCircleDetailModels, null);
//                    listView.onRefreshComplete();
//                    list.clear();
//                    list.addAll(cCircleDetailModels);
//                    listView.setResultSize(cCircleDetailModels.size());
//                    listView.setAdapter(circleMomentAdapter);
//                }
//            }
//        });
//    }

    public void getData(final int groupId, final int lastId, final int mode) {
        String url = String.format("v1.1/Circle/CircleList?groupId=%s&lastId=%s&session=%s", groupId, lastId, MainApplication.userInfo.getCookie());
        Log.i("CircleList", url);
        ApiDataProvider.getmClient().invokeApi(url, null,
                HttpGet.METHOD_NAME, null, CircleMoments.class,
                new ApiOperationCallback<CircleMoments>() {
                    @Override
                    public void onCompleted(CircleMoments result, Exception exception, ServiceFilterResponse response) {
                        //Log.d("CircleResult", result.toString());
                        if (progressDialog != null) {
                            progressDialog.cancel();
                            progressDialog = null;
                        }
                        if (!result.isSuccess()) {
                            switch (mode) {
                                case DragListView.REFRESH:
                                    listView.onRefreshComplete();
                                    break;
                                case DragListView.LOAD:
                                    listView.onLoadComplete();
                                    break;
                            }
                            listView.setResultSize(list.size());
                            circleMomentAdapter.notifyDataSetChanged();
                            return;
                        }

                        List<CCircleDetailModel> result_list = result.data;
                        for (int i = result_list.size() - 1; i >= 0; i--) {
                            if (!result_list.get(i).isValid()) {
                                result_list.remove(i);
                            }
                        }

                        switch (mode) {
                            case DragListView.REFRESH:
                                listView.onRefreshComplete();
                                list.clear();
                                list.addAll(result_list);
                                break;
                            case DragListView.LOAD:
                                listView.onLoadComplete();
                                list.addAll(result_list);
                                break;
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        listView.setResultSize(result_list.size());
                        circleMomentAdapter.notifyDataSetChanged();
                    }
                });
    }
}
