package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.view.shared.TitleView;

import java.util.ArrayList;
import java.util.List;

public class DepartmentActivity extends BaseActivity implements View.OnClickListener{

    public static final int REQUEST_CODE_ = 102;

    private EditText mSearchInput = null;
    private ImageView mSearchBtn = null;
    private ListView mDepartmentLv = null;

    /*private List<DepartmentDelegate> mDepartments = new ArrayList<DepartmentDelegate>();*/
    private DepartmentAdapter mAdapter = null;
    private List<User> mDataList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        mSearchInput = (EditText)findViewById(R.id.department_search_input);
        mSearchBtn = (ImageView)findViewById(R.id.department_search_btn);
        mDepartmentLv = (ListView)findViewById(R.id.department_list);

        mSearchBtn.setOnClickListener(this);

        mAdapter = new DepartmentAdapter(mDataList);
        mDepartmentLv.setAdapter(mAdapter);
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.info_department);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.department_search_btn:
                String key = mSearchInput.getText().toString();
                if (TextUtils.isEmpty(key)) {
                    Toast.makeText(this, R.string.input_not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                UserInfoOperator.getInstance().searchDepartment(key, new UserInfoOperator.OnGetListener<List<User>>() {
                    @Override
                    public void onGet(boolean succeed, List<User> users) {
                        if (succeed) {
                            mDataList.clear();
                            mDataList.addAll(users);
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                });
                break;
        }
    }

    private class DepartmentAdapter extends BaseAdapter {

        private List<User> mUserList = null;

        public DepartmentAdapter (List<User> users) {
            mUserList = users;
        }

        @Override
        public int getCount() {
            return mUserList.size();
        }

        @Override
        public User getItem(int i) {
            return mUserList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(DepartmentActivity.this).inflate(R.layout.layout_department_item, null);
                holder = new ViewHolder();
                holder.titleTv = (TextView)view.findViewById(R.id.department_item_title);
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }
            final User user = mUserList.get(i);
            holder.titleTv.setText(user.getName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent ();
                    it.putExtra(User.KEY_USER_ID, user.getUserId());
                    it.putExtra(User.KEY_NICK_NAME, user.getName());
                    setResult(RESULT_OK, it);
                    finish();
                }
            });
            return view;
        }
    }

    private class ViewHolder {
        public TextView titleTv;
    }

    /*@Override
    public String getTitleRight() {
        return getString(R.string.department_add);
    }

    @Override
    public void onTitleRightPressed() {
        Intent it = new Intent (DepartmentActivity.this, DepartmentEditActivity.class);
        startActivityForResult(it, REQUEST_CODE_);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_:
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra(DepartmentEditActivity.KEY_NAME);
                    String start = data.getStringExtra(DepartmentEditActivity.KEY_DURATION_START);
                    String end = data.getStringExtra(DepartmentEditActivity.KEY_DURATION_END);
                    DepartmentDelegate delegate = new DepartmentDelegate(name, start, end);
                    mDepartments.add(delegate);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private class DepartmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDepartments.size();
        }

        @Override
        public Object getItem(int position) {
            return mDepartments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(DepartmentActivity.this).inflate(R.layout.layout_department_item, null);
                holder = new ViewHolder();
                holder.detailsTv = (TextView)convertView.findViewById(R.id.department_item_details);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            DepartmentDelegate delegate = mDepartments.get(position);
            holder.detailsTv.setText(delegate.name + "\n" + delegate.durationStart + "-" + delegate.durationEnd);
            return convertView;
        }
    }

    private class ViewHolder {
        public TextView detailsTv;
    }

    private class DepartmentDelegate {
        public String name;
        public String durationStart;
        public String durationEnd;

        public DepartmentDelegate (String name, String durationStart, String durationEnd) {
            this.name = name;
            this.durationStart = durationStart;
            this.durationEnd = durationEnd;
        }
    }*/
}
