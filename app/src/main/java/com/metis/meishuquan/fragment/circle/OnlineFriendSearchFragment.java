package com.metis.meishuquan.fragment.circle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.circle.UserSearch;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

/**
 * Created by wudi on 4/12/2015.
 */
public class OnlineFriendSearchFragment extends Fragment {
    private ViewGroup rootView;
    private SearchView searchView;
    private CUserModel user;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_onlinesearchfragment, container, false);
        this.searchView = (SearchView) rootView.findViewById(R.id.fragment_circle_onlinesearchfragment_input);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String account) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.show(getActivity(), "", "正在查找...");
                progressDialog.setCancelable(false);

                CommonOperator.getInstance().searchUser(account, new ApiOperationCallback<UserSearch>() {
                    @Override
                    public void onCompleted(UserSearch result, Exception exception, ServiceFilterResponse response) {
                        progressDialog.cancel();
                        if (result != null && result.data != null) {
                            user = result.data;

                            //TODO:显示好友信息
                            RequestMessageFragment requestMessageFragment = new RequestMessageFragment();
                            Bundle args = new Bundle();
                            args.putString("targetid", String.valueOf(user.userId));
                            requestMessageFragment.setArguments(args);

                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                            ft.add(R.id.content_container, requestMessageFragment);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

//        confirmView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (user != null) {
//                    RequestMessageFragment requestMessageFragment = new RequestMessageFragment();
//
//                    Bundle args = new Bundle();
//                    args.putString("targetid", String.valueOf(user.userId));
//                    requestMessageFragment.setArguments(args);
//
//                    FragmentManager fm = getActivity().getSupportFragmentManager();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
//                    ft.add(R.id.content_container, requestMessageFragment);
//                    ft.addToBackStack(null);
//                    ft.commit();
//                }
//            }
//        });

        return rootView;
    }
}
