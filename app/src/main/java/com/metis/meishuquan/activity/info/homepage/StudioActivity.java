package com.metis.meishuquan.activity.info.homepage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.InputActivity;
import com.metis.meishuquan.activity.info.BaseActivity;
import com.metis.meishuquan.activity.info.TextActivity;
import com.metis.meishuquan.adapter.commons.ConstellationAdapter;
import com.metis.meishuquan.adapter.commons.SimplePrvsAdapter;
import com.metis.meishuquan.adapter.studio.InfoAdapter;
import com.metis.meishuquan.fragment.commons.ListDialogFragment;
import com.metis.meishuquan.fragment.commons.StudioFragment;
import com.metis.meishuquan.manager.common.UserManager;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.PatternUtils;

import java.util.Calendar;

public class StudioActivity extends BaseActivity implements
        StudioFragment.OnMenuItemClickListener,
        RadioGroup.OnCheckedChangeListener,
        InfoAdapter.OnInfoItemClickListener,
        ConstellationAdapter.OnItemClickListener{

    private static final String TAG = StudioActivity.class.getSimpleName();

    public static final String KEY_USER_ID = User.KEY_USER_ID,
                                KEY_USER_ROLE = User.KEY_USER_ROLE;

    private View mTitleView = null;
    private ImageView mTitleProfile = null;
    private TextView mTitleName = null, mSubTitleName = null;

    private StudioFragment mStudioFragment = null;

    private BaseAdapter mAdapter = null;

    private long mUserId = MainApplication.userInfo.getUserId();

    private User mUser = null;

    private SimplePrvsAdapter.OnPrvsItemClickListener mPrvsListener = new SimplePrvsAdapter.OnPrvsItemClickListener() {

        @Override
        public void onItemClick(View view, String name) {
            ListDialogFragment.getInstance().dismiss();
            UserManager.updateMyInfo(User.KEY_REGION, name);
            if (mAdapter != null && mAdapter instanceof InfoAdapter) {
                InfoAdapter adapter = (InfoAdapter)mAdapter;
                adapter.setProvince(name);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio);

        mTitleView = LayoutInflater.from(this).inflate(R.layout.layout_studio_title, null);
        getTitleView().addCenterView(mTitleView);
        mTitleProfile = (ImageView)mTitleView.findViewById(R.id.studio_title_profile);
        mTitleName = (TextView)mTitleView.findViewById(R.id.studio_title_name);
        mSubTitleName = (TextView)mTitleView.findViewById(R.id.studio_title_meishuquan_id);
        ImageLoaderUtils.getImageLoader(this).displayImage("http://images.apple.com/cn/live/2015-mar-event/images/751591e0653867230e700d3a99157780826cce88_xlarge.jpg",
                mTitleProfile,
                ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.studio_profile_size)));

        mStudioFragment = (StudioFragment)getSupportFragmentManager().findFragmentById(R.id.studio_fragment);
        mStudioFragment.setOnMenuItemClickListener(this);

        String userRoleStr = getIntent().getStringExtra(KEY_USER_ROLE);
        if ("studio".equals(userRoleStr)) {
            mStudioFragment.setTabTitle(
                    R.string.studio_tab_top_line,
                    R.string.studio_tab_glory,
                    R.string.studio_tab_works);
        } else {
            mStudioFragment.setTabTitle(
                    R.string.studio_tab_daily,
                    R.string.studio_tab_album,
                    R.string.studio_tab_info_details
            );
        }
        mStudioFragment.setOnCheckedChangeListener(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadUser(mUserId, new UserInfoOperator.OnGetListener<User>() {
            @Override
            public void onGet(boolean succeed, User user) {
                if (succeed) {
                    mUser = user;
                    fillUser(user);
                }
            }
        });
    }

    private void fillUser (User user) {
        mStudioFragment.setSelfIntroduce(user.getSelfIntroduce());
        ImageLoaderUtils.getImageLoader(this)
                .displayImage(user.getUserAvatar(),
                        mTitleProfile,
                        ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.studio_profile_size)));
        mTitleName.setText(user.getName());
        //mSubTitleName.setText(user.get);
    }

    @Override
    public void onMenuItemClick(StudioFragment.MenuItem item, int position) {
        switch (item.id) {
            case R.id.studio_menu_introduce:
                startActivity(new Intent(this, StudioDetailActivity.class));
                break;
            case R.id.studio_menu_album:
                startActivity(new Intent(this, StudioAlbumActivity.class));
                break;
            case R.id.studio_menu_team:
                startActivity(new Intent(this, TeacherTeamActivity.class));
                break;
            case R.id.studio_menu_course_arrangement:
                startActivity(new Intent(this, CourseArrangementActivity.class));
                break;
            case R.id.studio_menu_video:
                startActivity(new Intent(this, VideoListActivity.class));
                break;
            case R.id.studio_menu_charge:
                startActivity(new Intent(this, ChargeActivity.class));
                break;
            case R.id.studio_menu_book_publish:
                startActivity(new Intent(this, BookListActivity.class));
                break;
            case R.id.studio_menu_contact_us:
                //TODO
                startActivity(new Intent(this, TextActivity.class));
                break;
        }
    }

    private void loadUser (long userId, UserInfoOperator.OnGetListener<User> listener) {
        UserInfoOperator.getInstance().getUserInfo(mUserId, listener);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.studio_list_header_tab1:
                mAdapter = new NewAdapter();
                break;
            case R.id.studio_list_header_tab2:
                mAdapter = new MyAdapter();
                break;
            case R.id.studio_list_header_tab3:
                if (mUser == null) {
                    mAdapter = StudioFragment.EmptyAdapter.getInstance(this);
                    loadUser(mUserId, new UserInfoOperator.OnGetListener<User>() {
                        @Override
                        public void onGet(boolean succeed, User user) {
                            if (succeed) {
                                mUser = user;
                                if (mStudioFragment.getCheckTabId() == R.id.studio_list_header_tab3) {
                                    InfoAdapter adapter = new InfoAdapter(StudioActivity.this, user);
                                    adapter.setOnInfoItemClickListener(StudioActivity.this);
                                    mAdapter = adapter;
                                    mStudioFragment.setAdapter(mAdapter);
                                }
                            }
                        }
                    });
                } else {
                    InfoAdapter adapter = new InfoAdapter(StudioActivity.this, mUser);
                    adapter.setOnInfoItemClickListener(StudioActivity.this);
                    mAdapter = adapter;
                }
                break;
        }
        mStudioFragment.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mAdapter != null && mAdapter instanceof InfoAdapter) {
            InfoAdapter adapter = (InfoAdapter)mAdapter;
            switch (requestCode) {
                case InputActivity.REQUEST_CODE_NICK:
                    if (resultCode == RESULT_OK) {
                        CharSequence nick = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR);
                        adapter.setNickName(nick.toString());
                        UserManager.updateMyInfo(User.KEY_NICK_NAME, nick.toString());
                    }
                    break;
                case InputActivity.REQUEST_CODE_RECENTS:
                    if (resultCode == RESULT_OK) {
                        String recents = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR).toString();
                        UserManager.updateMyInfo(User.KEY_NICK_NAME, recents);
                        adapter.setSelfSignature(recents);
                    }
                    break;
                case InputActivity.REQUEST_CODE_MEISHUQUAN_ID:
                    if (resultCode == RESULT_OK) {
                        String id = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR).toString();
                        //Pattern pattern = new Pattern("");
                        if (!PatternUtils.PATTERN_MEISHUQUAN_ID.matcher(id.toString()).matches()) {
                            Toast.makeText(this, R.string.info_meishuquan_id_illegal, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        UserManager.updateMyInfo(User.KEY_ACCOUNT, id);
                        adapter.setMeishuquanId(id);
                    }
                    break;
                case InputActivity.REQUEST_CODE_DEPARTMENT_ADDRESS:
                    if (resultCode == RESULT_OK) {
                        String address = data.getCharSequenceExtra(InputActivity.KEY_DEFAULT_STR).toString();
                        UserManager.updateMyInfo(User.KEY_LOCATIONADDRESS, address);
                        adapter.setDepartmentAddress(address);
                    }
                    break;
            }
        }
    }

    @Override
    public void onItemClick(View view, String name) {
        ListDialogFragment.getInstance().dismiss();
        if (mAdapter != null && mAdapter instanceof InfoAdapter) {
            InfoAdapter adapter = (InfoAdapter) mAdapter;
            adapter.setConstellation(name);
            UserManager.updateMyInfo(User.KEY_HOROSCOPE, name);
        }
    }

    @Override
    public void onClick(View view, InfoAdapter.Item item, User user) {
        if (item.title.equals(getString(R.string.info_nick))) {
            startInputActivityForResult(getString(R.string.info_modify_nick), item.content, true, InputActivity.REQUEST_CODE_NICK);
        } else if (item.title.equals(getString(R.string.info_recents))) {
            startInputActivityForResult(getString(R.string.info_recents), item.content, false, InputActivity.REQUEST_CODE_RECENTS);
        } else if (item.title.equals(getString(R.string.info_gender))) {
            showDialog();
        } else if (item.title.equals(getString(R.string.info_ages))) {
            showBirthdayDialog();
        } else if (item.title.equals(getString(R.string.info_constellation))) {
            ListDialogFragment fragment = ListDialogFragment.getInstance();
            ConstellationAdapter adapter = ConstellationAdapter.getInstance(this, item.content);
            adapter.setOnItemClickListener(this);
            fragment.setAdapter(adapter);
            fragment.show(getSupportFragmentManager(), TAG);
        } else if (item.title.equals(getString(R.string.info_provience))) {
            ListDialogFragment fragment = ListDialogFragment.getInstance();
            SimplePrvsAdapter adapter = SimplePrvsAdapter.getInstance(this, item.content);
            adapter.setOnItemClickListener(mPrvsListener);
            fragment.setAdapter(adapter);
            fragment.show(getSupportFragmentManager(), TAG);
        } else if (item.title.equals(getString(R.string.info_meishuquan_id))) {
            startInputActivityForResult(item.title, item.content, true, InputActivity.REQUEST_CODE_MEISHUQUAN_ID);
        } else if (item.title.equals(getString(R.string.info_department_address))) {
            startInputActivityForResult(getString(R.string.info_department_address), item.content, false, InputActivity.REQUEST_CODE_DEPARTMENT_ADDRESS);
        }
    }

    private Dialog mDialog = null;
    private String mGender = null;
    public void showDialog () {
        if (MainApplication.userInfo == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.info_gender);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_gender_chooser, null);
        builder.setView(contentView);
        builder.setPositiveButton(R.string.gender_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.dismiss();
            }
        });
        RadioGroup group = (RadioGroup)contentView.findViewById(R.id.gender_group);
        RadioButton female = (RadioButton)contentView.findViewById(R.id.gender_famale);
        RadioButton male = (RadioButton)contentView.findViewById(R.id.gender_male);
        final String femaleStr = getString(R.string.gender_famale);
        final String maleStr = getString(R.string.gender_male);
        if (mGender == null) {
            mGender = MainApplication.userInfo.getGender();
        }
        if (mGender.equals(femaleStr)) {
            female.setChecked(true);
        } else {
            male.setChecked(true);
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.gender_famale) {
                    mGender = femaleStr;
                } else {
                    mGender = maleStr;
                }
                if (mAdapter != null && mAdapter instanceof InfoAdapter) {
                    InfoAdapter adapter = (InfoAdapter) mAdapter;
                    adapter.setGender(mGender);
                }
                UserManager.updateMyInfo(User.KEY_GENDER, mGender);
                //updateInfo(User.KEY_GENDER, mGenderView.getSecondaryText().toString());
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    private DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            if (mAdapter != null && mAdapter instanceof InfoAdapter) {
                InfoAdapter adapter = (InfoAdapter) mAdapter;
                adapter.setBirthday(i, i1 + 1, i2);
                UserManager.updateMyInfo(User.KEY_BIRTHDAY, PatternUtils.formatToDateStr(i, i1 + 1, i2));
            }
            Log.v(TAG, "onDateSet " + i + " " + i1 + " " + i2);
        }
    };

    public void showBirthdayDialog () {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar,
                mDateListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 200;
        }

        @Override
        public String getItem(int position) {
            return position + "";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(StudioActivity.this);
            tv.setText(getItem(position));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return tv;
        }
    }

    class NewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 200;
        }

        @Override
        public String getItem(int position) {
            return position + " new";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(StudioActivity.this);
            tv.setText(getItem(position));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return tv;
        }
    }
}
