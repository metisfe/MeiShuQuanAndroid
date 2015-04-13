package com.metis.meishuquan.fragment.commons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.R;

/**
 * Created by WJ on 2015/4/8.
 */
public class InputFragment extends Fragment {

    private EditText mInputEt = null;
    private TextView mCountTipTv = null;

    private boolean isSingleLine = false;
    private int mMaxCount = 500;
    private CharSequence mHint = null;
    private CharSequence mText = null;
    private int mInputType = InputType.TYPE_NULL;

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            final int length = s.length();
            if (length > mMaxCount) {
                s.delete(mMaxCount, length);
            }
            updateCountTip(s.length());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multi_input, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInputEt = (EditText)view.findViewById(R.id.input_edit_text);
        mCountTipTv = (TextView)view.findViewById(R.id.input_count_tip);

        //setInputType(mInputType);

        mInputEt.addTextChangedListener(watcher);

        updateCountTip(0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSingleLine(isSingleLine);
        setMaxCount(mMaxCount);
        setHint(mHint);
        setText(mText);
        setInputType(mInputType);
    }

    public void setSingleLine (boolean singleLine) {
        isSingleLine = singleLine;
        if (mInputEt != null) {
            ViewGroup.LayoutParams params = mInputEt.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            if (singleLine) {
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mCountTipTv.setVisibility(View.GONE);
                final int padding = (int)(10 * getResources().getDisplayMetrics().density);
                mInputEt.setPadding(padding, padding, padding, padding);
            } else {
                params.height = (int)(130 * getResources().getDisplayMetrics().density);
                mCountTipTv.setVisibility(View.VISIBLE);
                final int padding = (int)(10 * getResources().getDisplayMetrics().density);
                final int paddingBottom = (int)(18 * getResources().getDisplayMetrics().density);
                mInputEt.setPadding(padding, padding, padding, paddingBottom);
            }
            mInputEt.setLayoutParams(params);
            mInputEt.setSingleLine(singleLine);

        }

    }

    public void setText (CharSequence cs) {
        mText = cs;
        if (mInputEt != null) {
            mInputEt.setText(mText);
            mInputEt.selectAll();
        }
    }

    public void setHint (CharSequence cs) {
        mHint = cs;
        if (mInputEt != null) {
            mInputEt.setHint(mHint);
        }
    }

    public void setMaxCount (int count) {
        mMaxCount = count;
        if (mInputEt != null) {
            mInputEt.setMaxEms(mMaxCount);
        }
    }

    private void updateCountTip (int count) {
        mCountTipTv.setText(count + "/" + mMaxCount);
    }

    public CharSequence getText () {
        return mInputEt.getText();
    }

    public void setInputType(int type) {
        mInputType = type;
        if (mInputEt != null) {
            mInputEt.setInputType(type);
        }
    }
}
