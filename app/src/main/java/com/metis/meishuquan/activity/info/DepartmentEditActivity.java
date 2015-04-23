package com.metis.meishuquan.activity.info;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.view.shared.TitleView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

public class DepartmentEditActivity extends BaseActivity implements View.OnClickListener {

    public static final String KEY_NAME = "name",
                                KEY_DURATION_START = "duration_start",
                                KEY_DURATION_END = "duration_end";

    private EditText mInputEt = null;
    private TextView mDurationStartTv, mDurationEndTv;
    private String mName = null;
    private int mStartYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mStartMonth = Calendar.getInstance().get(Calendar.MONTH);

    private int mEndYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mEndMonth = Calendar.getInstance().get(Calendar.MONTH);
    //private String mDurationStartStr = null, mDurationEndStr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_edit);

        mInputEt = (EditText)findViewById(R.id.department_edit_name);

        mDurationStartTv = (TextView)findViewById(R.id.department_edit_duration_start);
        mDurationEndTv = (TextView)findViewById(R.id.department_edit_duration_end);

        mDurationStartTv.setText(makeTimeStr(mStartYear, mStartMonth));
        mDurationEndTv.setText(makeTimeStr(mEndYear, mEndMonth));

        mDurationStartTv.setOnClickListener(this);
        mDurationEndTv.setOnClickListener(this);

    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.info_department);
    }

    @Override
    public String getTitleRight() {
        return getString(R.string.department_complete);
    }

    @Override
    public void onTitleRightPressed() {
        final int error = canInfoAccess();
        if (error != 0) {
            Toast.makeText(DepartmentEditActivity.this, "error happened " + error, Toast.LENGTH_SHORT).show();
            return;
        }
        String str = mInputEt.getText().toString();
        Intent it = new Intent();
        it.putExtra(KEY_NAME, str);
        it.putExtra(KEY_DURATION_START, makeTimeStr(mStartYear, mStartMonth));
        it.putExtra(KEY_DURATION_END, makeTimeStr(mEndYear,mEndMonth));
        setResult(RESULT_OK, it);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.department_edit_duration_start:
                createDialogWithoutDateField(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mStartYear = year;
                        mStartMonth = monthOfYear;
                        mDurationStartTv.setText(makeTimeStr(year, monthOfYear));
                    }
                }, mStartYear, mStartMonth).show();
                break;
            case R.id.department_edit_duration_end:
                createDialogWithoutDateField(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mEndYear = year;
                        mEndMonth = monthOfYear;
                        mDurationEndTv.setText(makeTimeStr(year, monthOfYear));
                    }
                }, mEndYear, mEndMonth).show();
                break;
        }
    }

    private DatePickerDialog createDialogWithoutDateField(DatePickerDialog.OnDateSetListener listener, int year, int month){

        DatePickerDialog dlg = new DatePickerDialog(this, listener,
                year,
                month,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        {
            @Override
            protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                int dayId = getContext().getResources()
                        .getIdentifier("android:id/day", null, null);
                if(dayId != 0){
                    View yearPicker = findViewById(dayId);
                    if(yearPicker != null){
                        yearPicker.setVisibility(View.GONE);
                    }
                }
            }
        };
        return dlg;
    }

    public int canInfoAccess () {
        mName = mInputEt.getText().toString();

        String startDurationStr = makeTimeStr(mStartYear, mStartMonth);
        String endDurationStr = makeTimeStr(mEndYear, mEndMonth);

        if (TextUtils.isEmpty(mName)) {
            return -1;
        }

        if (TextUtils.isEmpty(startDurationStr)) {
            return -2;
        }

        if (TextUtils.isEmpty(endDurationStr)) {
            return -3;
        }

        if (startDurationStr.compareTo(endDurationStr) > 0) {
            return -4;
        }

        return 0;
    }

    private DecimalFormat mDecimalFormat = new DecimalFormat("00");
    private String makeTimeStr (int year, int monthInYear) {
        return year + "-" + mDecimalFormat.format(1 + monthInYear);
    }
}
