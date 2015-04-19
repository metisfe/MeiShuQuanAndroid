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
import android.widget.EditText;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.circle.MyFriendList;
import com.metis.meishuquan.model.circle.UserSearch;
import com.metis.meishuquan.model.provider.ApiDataProvider;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import org.apache.http.client.methods.HttpGet;

/**
 * Created by wudi on 4/12/2015.
 */
public class OnlineFriendSearchFragment extends Fragment {
    private ViewGroup rootView;
    private EditText editText;
    private Button searchView, confirmView;
    private TextView tv;
    private CUserModel user;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_onlinesearchfragment, container, false);
        this.editText = (EditText) rootView.findViewById(R.id.fragment_circle_onlinesearchfragment_input);
        this.searchView = (Button) rootView.findViewById(R.id.fragment_circle_onlinesearchfragment_search);
        this.confirmView = (Button) rootView.findViewById(R.id.fragment_circle_onlinesearchfragment_add);
        this.tv = (TextView) rootView.findViewById(R.id.fragment_circle_onlinesearchfragment_info);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = editText.getText().toString();
                if (TextUtils.isEmpty(account)) return;

                StringBuilder PATH = new StringBuilder("v1.1/UserCenter/SearchUser");
                PATH.append("?session=");
                PATH.append(MainApplication.userInfo.getCookie());
                PATH.append("&account=");
                PATH.append(account);

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.show();

                ApiDataProvider.getmClient().invokeApi(PATH.toString(), null,
                        HttpGet.METHOD_NAME, null, UserSearch.class,
                        new ApiOperationCallback<UserSearch>() {
                            @Override
                            public void onCompleted(UserSearch result, Exception exception, ServiceFilterResponse response) {
                                progressDialog.cancel();
                                if (result != null && result.data != null) {
                                    user = result.data;
                                    confirmView.setVisibility(View.VISIBLE);
                                    tv.setText("name:" + user.name + "rid:" + user.rongCloud + "id:" + user.userId);
                                }
                            }
                        });

            }
        });

        confirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
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

        return rootView;
    }
}
