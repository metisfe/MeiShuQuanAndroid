package com.metis.meishuquan.activity.info;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.metis.meishuquan.activity.info.homepage.StudioActivity;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.School;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.view.shared.TitleView;

import java.util.ArrayList;
import java.util.List;

public class DepartmentActivity extends BaseActivity {

    public static final String
            KEY_REQUEST_CODE = "request_code",
            KEY_CONTENT = "content";

    private ListView mDepartmentLv = null;

    /*private List<DepartmentDelegate> mDepartments = new ArrayList<DepartmentDelegate>();*/
    private StringAdapter mAdapter = null;
    //private List<DepartmentDelegate> mDataList = new ArrayList<DepartmentDelegate>();

    private int mRequestCode = StudioActivity.REQUEST_CODE_DEPARTMENT;

    private List<String> mDataList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        mDepartmentLv = (ListView)findViewById(R.id.department_list);

        String content = getIntent().getStringExtra(KEY_CONTENT);
        if (content == null) {
            content = "";
        }
        String[] array = content.split(" ");
        for (String str : array) {
            if (TextUtils.isEmpty(str.trim())) {
                continue;
            }
            mDataList.add(str);
        }
        mAdapter = new StringAdapter(mDataList);
        mDepartmentLv.setAdapter(mAdapter);

        mRequestCode = getIntent().getIntExtra(KEY_REQUEST_CODE, mRequestCode);
        if (mRequestCode == StudioActivity.REQUEST_CODE_DEPARTMENT) {
            setTitleCenter(getString(R.string.info_studio));
        } else if (mRequestCode == StudioActivity.REQUEST_CODE_SCHOOL) {
            setTitleCenter(getString(R.string.info_school));
        }

    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.info_department);
    }

    @Override
    public String getTitleRight() {
        return getString(R.string.department_add);
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        it.putExtra(User.KEY_NICK_NAME, getNames());
        setResult(RESULT_OK, it);
        super.onBackPressed();
    }

    @Override
    public void onTitleRightPressed() {
        Intent it = new Intent (DepartmentActivity.this, DepartmentEditActivity.class);
        it.putExtra(KEY_REQUEST_CODE, mRequestCode);
        startActivityForResult(it, mRequestCode);
    }

    public String getNames () {
        StringBuilder sb = new StringBuilder();
        for (String str : mDataList) {
            sb.append(str + " ");
        }
        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case StudioActivity.REQUEST_CODE_DEPARTMENT:
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra(DepartmentEditActivity.KEY_NAME);
                    if (!mDataList.contains(name)) {
                        mDataList.add(name);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case StudioActivity.REQUEST_CODE_SCHOOL:
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra(DepartmentEditActivity.KEY_NAME);
                    if (!mDataList.contains(name)) {
                        mDataList.add(name);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    private class StringAdapter extends BaseAdapter {

        private List<String> mDataList = null;

        public StringAdapter (List<String> strings) {
            mDataList = strings;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public String getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(DepartmentActivity.this).inflate(R.layout.layout_list_dialog_item, null);
            TextView tv = (TextView)convertView.findViewById(R.id.list_dialog_item);
            tv.setText(getItem(position));
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DepartmentActivity.this);
                    builder.setMessage(R.string.info_delete_this_item);
                    builder.setPositiveButton(R.string.gender_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDataList.remove(getItem(position));
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton(R.string.alter_dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                    return false;
                }
            });
            return convertView;
        }
    }

}
