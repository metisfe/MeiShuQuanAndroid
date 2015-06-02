package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.act.StudentListActivity;
import com.metis.meishuquan.view.common.delegate.AbsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/29.
 */
public class StudentJoinedFragment extends Fragment {

    private RecyclerView mRecyclerView = null;
    private LinearLayoutManager mLinearLayoutManager = null;

    private StudentListActivity.StudentAdapter mAdapter = null;
    private List<AbsViewHolder> mDataList = new ArrayList<AbsViewHolder>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_joined, null, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.joinec_list);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

}
