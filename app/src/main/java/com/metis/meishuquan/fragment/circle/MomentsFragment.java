package com.metis.meishuquan.fragment.circle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;

import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.circle.CircleMomentAdapter;
import com.metis.meishuquan.model.BLL.TopLineOperator;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.metis.meishuquan.model.topline.News;
import com.metis.meishuquan.model.topline.ToplineNewsList;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.view.shared.DragListView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jx on 4/7/2015.
 */
public class MomentsFragment extends CircleBaseFragment {
    @Override
    public void timeToSetTitleBar() {
        getTitleBar().setText("首页");
        getTitleBar().setRightButton("", 0, null);
    }

    private DragListView listView;
    private List<CCircleDetailModel> list = new ArrayList<>();
    private int momentLastId = 0;
    private CircleMomentAdapter circleMomentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_circle_momentsfragment, container, false);
        this.listView = (DragListView) contextView.findViewById(R.id.fragment_circle_moments_list);
        circleMomentAdapter = new CircleMomentAdapter(list);
        this.listView.setAdapter(circleMomentAdapter);
//        getData(0, momentLastId, DragListView.LOAD);

        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(0, momentLastId, DragListView.REFRESH);
            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {
            @Override
            public void onLoad() {
                getData(0, momentLastId, DragListView.LOAD);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MomentDetailFragment momentDetailFragment = new MomentDetailFragment();
//                Bundle args = new Bundle();
//                args.putInt("newsId", newsId);
//                itemInfoFragment.setArguments(args);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, momentDetailFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return contextView;
    }

    public void getData(final int groupId, final int lastId, final int mode) {
        String url = String.format("v1.1/Circle/CircleList?groupId=%s&lastId=%s&session=%s", groupId, lastId, MainApplication.userInfo.getCookie());

        ApiDataProvider.getmClient().invokeApi(url, null,
                HttpGet.METHOD_NAME, null, (Class<ReturnInfo<List<CCircleDetailModel>>>) new ReturnInfo<List<CCircleDetailModel>>().getClass(),
                new ApiOperationCallback<ReturnInfo<List<CCircleDetailModel>>>() {
                    @Override
                    public void onCompleted(ReturnInfo<List<CCircleDetailModel>> result, Exception exception, ServiceFilterResponse response) {

                        List<CCircleDetailModel> result_list = result.data;
                        if (result_list.size() > 0)
                        {
                            momentLastId = result_list.get(0).id;
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

                        listView.setResultSize(result_list.size());
                        circleMomentAdapter.notifyDataSetChanged();
                    }
                });
    }
}
