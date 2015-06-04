package com.metis.meishuquan.fragment.circle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.R;
import com.metis.meishuquan.adapter.circle.CircleAtMeAdapter;
import com.metis.meishuquan.model.BLL.CircleOperator;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CRelatedCircleModel;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.view.shared.DragListView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @我的 Created by wangjin on 6/3/2015.
 */
public class AtMeFragment extends Fragment {
    public static final String CLASS_NAME = AtMeFragment.class.getSimpleName();

    private Button btnBack;
    private TextView tvTitel;
    private DragListView listView;

    private List<CCircleDetailModel> list = new ArrayList<>();
    private CircleAtMeAdapter circleMomentAdapter;
    private FragmentManager fm = null;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_circle_atme_momentsfragment, container, false);
        initView(contextView);
        initEvent();

        getData(0, DragListView.REFRESH);

        return contextView;
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(CLASS_NAME); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(CLASS_NAME);
    }

    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.popBackStack();
            }
        });

        this.listView.setOnRefreshListener(new DragListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(0, DragListView.REFRESH);
            }
        });

        this.listView.setOnLoadListener(new DragListView.OnLoadListener() {
            @Override
            public void onLoad() {
                int momentLastId = list != null && list.size() > 0 ? list.get(list.size() - 1).id : 0;
                getData(momentLastId, DragListView.LOAD);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MomentDetailFragment momentDetailFragment = new MomentDetailFragment();
                Bundle args = new Bundle();
                args.putInt(momentDetailFragment.KEY_MOMENT_ID, list.get(position).id);
                momentDetailFragment.setArguments(args);

                // refresh load more listview has header
                if (list.size() == 0) {
                    return;
                }
                if (position == list.size() + 1 || position == 0) {
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putInt(MomentDetailFragment.KEY_MOMENT_ID, list.get(position - 1).id);
                momentDetailFragment.setArguments(bundle);
                GlobalData.moment = list.get(position - 1);

                fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, momentDetailFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    private void initView(View contextView) {
        this.btnBack = (Button) contextView.findViewById(R.id.id_btn_back);
        this.tvTitel = (TextView) contextView.findViewById(R.id.id_tv_title);
        this.listView = (DragListView) contextView.findViewById(R.id.fragment_circle_moments_list);

        this.tvTitel.setText("@我的");

        fm = getActivity().getSupportFragmentManager();
        circleMomentAdapter = new CircleAtMeAdapter(getActivity(), list, contextView);
        this.listView.setAdapter(circleMomentAdapter);
    }

    public void getData(final int lastId, final int mode) {
        CircleOperator.getInstance().getAtMeData(lastId, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if (progressDialog != null) {
                    progressDialog.cancel();
                    progressDialog = null;
                }

                Gson gson = new Gson();
                String json = gson.toJson(result);
                ReturnInfo<CRelatedCircleModel> cRelatedCircleModel = gson.fromJson(json, new TypeToken<ReturnInfo<CRelatedCircleModel>>() {
                }.getType());

                if (result != null && !result.isSuccess()) {
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

                List<CCircleDetailModel> result_list = cRelatedCircleModel.getData().aitelMeList;
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
