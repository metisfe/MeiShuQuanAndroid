package com.metis.meishuquan.activity.info;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.info.homepage.StudioActivity;
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.StudioOperator;
import com.metis.meishuquan.model.BLL.TopListItem;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.College;
import com.metis.meishuquan.model.commons.School;
import com.metis.meishuquan.model.commons.Studio;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.view.shared.TitleView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class DepartmentEditActivity extends BaseActivity implements View.OnClickListener {

    public static final String KEY_NAME = "name",
                                KEY_DURATION_START = "duration_start",
                                KEY_DURATION_END = "duration_end";

    private EditText mInputEt = null;
    private ListView mListView = null;
    //private String mDurationStartStr = null, mDurationEndStr = null;

    private int mRequestCode = 0;

    private List<NameShowable> mDataList = new ArrayList<NameShowable>();
    private NameAdapter mAdapter = null;

    private boolean loadingData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_edit);

        mInputEt = (EditText)findViewById(R.id.department_edit_name);
        mListView = (ListView)findViewById(R.id.department_edit_search_content_list);
        mAdapter = new NameAdapter(this, mDataList);
        mListView.setAdapter(mAdapter);

        mRequestCode = getIntent().getIntExtra(DepartmentActivity.KEY_REQUEST_CODE, StudioActivity.REQUEST_CODE_SCHOOL);
        if (mRequestCode != StudioActivity.REQUEST_CODE_SCHOOL) {
            getTitleView().setTitleRight("");
        }
        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!loadingData) {
                    searchResult(s.toString());
                }
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        String title = getString(R.string.info_department);
        switch (mRequestCode) {
            case StudioActivity.REQUEST_CODE_SCHOOL:
                title = getString(R.string.info_school);
                break;
            case StudioActivity.REQUEST_CODE_DEPARTMENT:
                if (MainApplication.userInfo != null && MainApplication.userInfo.getUserRoleEnum() == IdTypeEnum.TEACHER) {
                    title = getString(R.string.info_department);
                } else {
                    title = getString(R.string.info_studio);
                }
                break;
        }
        setTitleCenter(title);
        searchResult("");
    }

    private void searchResult (String str) {
        loadingData = true;
        switch (mRequestCode) {
            case StudioActivity.REQUEST_CODE_SCHOOL:
                /*UserInfoOperator.getInstance().getCollegeList(str, new UserInfoOperator.OnGetListener<List<College>>() {
                    @Override
                    public void onGet(boolean succeed, List<College> colleges) {
                        loadingData = false;
                        if (succeed) {
                            List<CollegeDelegate> delegates = new ArrayList<CollegeDelegate>();
                            for (College item : colleges) {
                                delegates.add(new CollegeDelegate(item));
                            }
                            mDataList.clear();
                            mDataList.addAll(delegates);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });*/
                UserInfoOperator.getInstance().searchSchool(str, new UserInfoOperator.OnGetListener<List<School>>() {
                    @Override
                    public void onGet(boolean succeed, List<School> schools) {
                        loadingData = false;
                        if (succeed) {
                            List<SchoolDelegate> delegates = new ArrayList<SchoolDelegate>();
                            for (School school : schools) {
                                delegates.add(new SchoolDelegate(school));
                            }
                            mDataList.clear();
                            mDataList.addAll(delegates);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
            case StudioActivity.REQUEST_CODE_DEPARTMENT:
                UserInfoOperator.getInstance().searchStudio(str, new UserInfoOperator.OnGetListener<List<Studio>>() {
                    @Override
                    public void onGet(boolean succeed, List<Studio> studios) {
                        loadingData = false;
                        List<StudioDelegate> delegates = new ArrayList<StudioDelegate>();
                        for (Studio studio : studios) {
                            delegates.add(new StudioDelegate(studio));
                        }
                        mDataList.clear();
                        mDataList.addAll(delegates);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                /*ActiveOperator.getInstance().getStudioList(0, 0, 0, 0, 0, 0, 0, 0, str, new UserInfoOperator.OnGetListener<List<TopListItem>>() {
                    @Override
                    public void onGet(boolean succeed, List<TopListItem> topListItems) {
                        loadingData = false;
                        if (succeed) {
                            List<TopListDelegate> delegates = new ArrayList<TopListDelegate>();
                            for (TopListItem item : topListItems) {
                                delegates.add(new TopListDelegate(item));
                            }
                            mDataList.clear();
                            mDataList.addAll(delegates);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });*/
                break;
        }
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.info_department);
    }

    @Override
    public String getTitleRight() {
        return getString(R.string.department_complete);
    }

    public void selectName (String name) {
        Intent it = new Intent();
        it.putExtra(KEY_NAME, name);
        setResult(RESULT_OK, it);
        finish();
    }

    public void selectId (int id, String name) {
        Intent it = new Intent();
        it.putExtra(User.KEY_USER_ID, id);
        it.putExtra(User.KEY_NICK_NAME, name);
        setResult(RESULT_OK, it);
        finish();
    }

    @Override
    public void onTitleRightPressed() {
        String content = mInputEt.getText().toString();
        if (TextUtils.isEmpty(content)) {
            finish();
            return;
        }
        if (mRequestCode == StudioActivity.REQUEST_CODE_SCHOOL) {
            selectName(content);
        }
        /*final int error = canInfoAccess();
        if (error != 0) {
            Toast.makeText(DepartmentEditActivity.this, "error happened " + error, Toast.LENGTH_SHORT).show();
            return;
        }
        String str = mInputEt.getText().toString();
        Intent it = new Intent();
        it.putExtra(KEY_NAME, str);
        it.putExtra(KEY_DURATION_START, makeTimeStr(mStartYear, mStartMonth));
        it.putExtra(KEY_DURATION_END, makeTimeStr(mEndYear, mEndMonth));
        setResult(RESULT_OK, it);
        finish();*/
    }

    @Override
    public void onClick(View v) {

    }

    public class NameAdapter extends BaseAdapter {

        private Context mContext = null;
        private List<? extends NameShowable> mDataList = null;

        public NameAdapter (Context context, List<? extends NameShowable> list) {
            mContext = context;
            mDataList = list;
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public NameShowable getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_list_dialog_item, null);
            TextView tv = (TextView)convertView.findViewById(R.id.list_dialog_item);
            final NameShowable nameShowable = getItem(position);
            tv.setText(nameShowable.getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nameShowable instanceof StudioDelegate) {
                        selectId(((StudioDelegate) nameShowable).mItem.getUserId(), nameShowable.getName());
                    } else {
                        selectName(getItem(position).getName());
                    }
                }
            });
            return convertView;
        }
    }

    public static interface NameShowable {
        public String getName ();
    }

    private class StudioDelegate implements NameShowable {

        public Studio mItem = null;

        public StudioDelegate(Studio TopListItem) {
            mItem = TopListItem;
        }

        @Override
        public String getName() {
            return mItem.getName();
        }
    }

    private class SchoolDelegate implements NameShowable {

        private School mCollege = null;

        public SchoolDelegate(School college) {
            mCollege = college;
        }

        @Override
        public String getName() {
            return mCollege.getSchoolName();
        }
    }

}
