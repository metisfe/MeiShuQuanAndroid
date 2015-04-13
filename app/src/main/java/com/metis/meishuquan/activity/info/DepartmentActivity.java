package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.shared.TitleView;

import java.util.ArrayList;
import java.util.List;

public class DepartmentActivity extends BaseActivity {

    public static final int REQUEST_CODE_ = 102;

    private TitleView mTitleView = null;

    private ListView mDepartmentLv = null;

    private List<DepartmentDelegate> mDepartments = new ArrayList<DepartmentDelegate>();
    private DepartmentAdapter mAdapter = new DepartmentAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        mTitleView = (TitleView)findViewById(R.id.title);
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleView.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent (DepartmentActivity.this, DepartmentEditActivity.class);
                startActivityForResult(it, REQUEST_CODE_);
            }
        });

        mDepartmentLv = (ListView)findViewById(R.id.department_list);
        mDepartmentLv.setAdapter(mAdapter);
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
    }
}
