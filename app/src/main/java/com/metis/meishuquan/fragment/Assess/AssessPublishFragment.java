package com.metis.meishuquan.fragment.Assess;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.BaseFragment;

/**
 * Fragment:发布点评
 * <p/>
 * Created by wj on 15/4/1.
 */
public class AssessPublishFragment extends BaseFragment {
    private FragmentManager fm;
    private Button btnCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_assess_publish, null);
        initView(rootView);
        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        fm = getActivity().getSupportFragmentManager();
        this.btnCancel = (Button) rootView.findViewById(R.id.id_btn_cancel);
    }

    private void initEvent() {
        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(AssessPublishFragment.this);
                ft.commit();
            }
        });
    }
}
