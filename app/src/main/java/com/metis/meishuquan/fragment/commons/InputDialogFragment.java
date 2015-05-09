package com.metis.meishuquan.fragment.commons;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.metis.meishuquan.R;

/**
 * Created by WJ on 2015/5/9.
 */
public class InputDialogFragment extends DialogFragment {

    private static InputDialogFragment sFragment = new InputDialogFragment();

    public static InputDialogFragment getInstance (String title, String text, String hint, OnOkListener listener) {
        sFragment.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
        sFragment.setTitle(title);
        sFragment.setText(text);
        sFragment.setHint(hint);
        sFragment.setOnOkListener(listener);
        return sFragment;
    }

    private String mTitle, mText, mHint;
    private OnOkListener mListener = null;

    private TextView mTitleTv = null;
    private EditText mInputEt = null;
    private Button mOkBtn = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_input, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleTv = (TextView)view.findViewById(R.id.input_title);
        mInputEt = (EditText)view.findViewById(R.id.input_edit);
        mInputEt.requestFocus();
        mOkBtn = (Button)view.findViewById(R.id.input_ok);

        setTitle(mTitle);
        setText(mText);
        setHint(mHint);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    dismiss();
                    mListener.onOkClick(view, mInputEt.getText());
                }
            }
        });
    }

    public void setTitle (String title) {
        mTitle = title;
        if (mTitleTv != null) {
            mTitleTv.setText(mTitle);
        }
    }

    public void setText (String text) {
        mText = text;
        if (mInputEt != null) {
            mInputEt.append(mText);
            mInputEt.selectAll();
        }
    }

    public void setHint (String hint) {
        mHint = hint;
        if (mInputEt != null) {
            mInputEt.setHint(mHint);
        }
    }

    public void setOnOkListener (OnOkListener listener) {
        mListener = listener;
    }

    /*public void setOnClickListener (View.OnClickListener listener) {
        mListener = listener;
        if (mOkBtn != null) {
            mOkBtn.setOnClickListener(mListener);
        }
    }*/

    public static interface OnOkListener {
        public void onOkClick (View view, CharSequence cs);
    }

}
