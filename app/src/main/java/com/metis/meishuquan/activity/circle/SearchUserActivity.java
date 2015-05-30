package com.metis.meishuquan.activity.circle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.circle.UserSearch;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.umeng.analytics.MobclickAgent;

public class SearchUserActivity extends FragmentActivity {

    private SearchView searchView;
    private CUserModel user;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        this.searchView = (SearchView) this.findViewById(R.id.fragment_circle_onlinesearchfragment_input);

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchView, InputMethodManager.SHOW_FORCED);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String account) {
                progressDialog = new ProgressDialog(SearchUserActivity.this);
                progressDialog.show(SearchUserActivity.this, "", "正在查找...");

                CommonOperator.getInstance().searchUser(account, new ApiOperationCallback<UserSearch>() {
                    @Override
                    public void onCompleted(UserSearch result, Exception exception, ServiceFilterResponse response) {
                        progressDialog.dismiss();
                        if (result != null && result.data != null) {
                            progressDialog.dismiss();
                            user = result.data;

                            //显示好友信息
                            Intent intent = new Intent(SearchUserActivity.this, SearchUserInfoActivity.class);
                            intent.putExtra(SearchUserInfoActivity.KEY_USER_INFO, (java.io.Serializable) user);
                            startActivity(intent);
                            finish();
                        } else if (result.option.status.equals(String.valueOf(1))) {
                            CUserModel userNull = new CUserModel();
                            userNull.name = account;
                            userNull.relation = 2;

                            Intent intent = new Intent(SearchUserActivity.this, SearchUserInfoActivity.class);
                            intent.putExtra(SearchUserInfoActivity.KEY_USER_INFO, (java.io.Serializable) userNull);
                            startActivity(intent);
                            finish();
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
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
