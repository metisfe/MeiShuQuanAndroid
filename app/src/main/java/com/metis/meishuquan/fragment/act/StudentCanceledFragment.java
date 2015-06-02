package com.metis.meishuquan.fragment.act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.exception.DbException;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.act.StudentListActivity;
import com.metis.meishuquan.manager.common.DBManager;
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.commons.ActiveInfo;
import com.metis.meishuquan.view.common.delegate.AbsDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WJ on 2015/5/29.
 */
public class StudentCanceledFragment extends Fragment {

    private RecyclerView mRecyclerView = null;
    private LinearLayoutManager mLinearLayoutManager = null;
    private List<AbsDelegate> mDataList = new ArrayList<AbsDelegate>();
    private StudentListActivity.StudentAdapter mAdapter = null;
    private List<CanceledStudent> mStudentList = null;

    private String mLastCreateTime = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_canceled, null, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.cancel_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new StudentListActivity.StudentAdapter(getActivity(), mDataList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            DBManager.getInctance(getActivity()).createDB().createTableIfNotExist(StudentListActivity.Student.class);
            mStudentList = DBManager.db.findAll(CanceledStudent.class);
            if (mStudentList != null && mStudentList.size() > 0) {
                mLastCreateTime = mStudentList.get(0).createDatetime;
                List<StudentListActivity.StudentDelegate> delegates = new ArrayList<StudentListActivity.StudentDelegate>();
                for (CanceledStudent student : mStudentList) {
                    delegates.add(new StudentListActivity.StudentDelegate(student));
                }
                mDataList.add(0, new StudentListActivity.HeaderDelegate(getString(R.string.act_has_canceled_before, delegates.size())));
                mDataList.addAll(delegates);
                mAdapter.notifyDataSetChanged();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        ActiveOperator.getInstance().getActiveDetail(new UserInfoOperator.OnGetListener<ActiveInfo>() {
            @Override
            public void onGet(boolean succeed, ActiveInfo activeInfo) {
                if (succeed) {
                    ActiveOperator.getInstance().getCancelStudioStudent(activeInfo.getpId(), new UserInfoOperator.OnGetListener<List<CanceledStudent>>() {
                        @Override
                        public void onGet(boolean succeed, List<CanceledStudent> canceledStudents) {
                            if (!succeed) {
                                return;
                            }
                            mStudentList = canceledStudents;
                            List<AbsDelegate> mNewOnes = new ArrayList<AbsDelegate>();
                            List<AbsDelegate> mOldOnes = new ArrayList<AbsDelegate>();
                            for (CanceledStudent student : canceledStudents) {
                                StudentListActivity.StudentDelegate delegate = new StudentListActivity.StudentDelegate(student);
                                delegate.isNewOne = student.createDatetime.compareTo(mLastCreateTime) > 1;
                                if (delegate.isNewOne) {
                                    mNewOnes.add(delegate);
                                } else {
                                    mOldOnes.add(delegate);
                                }
                                delegate.isFriend = StudentListActivity.isFriend(getActivity(), student.userId);
                            }
                            if (!StudentListActivity.hasHeader(mNewOnes) && mNewOnes.size() > 0) {
                                StudentListActivity.HeaderDelegate headerDelegate = new StudentListActivity.HeaderDelegate(getString(R.string.act_lastest_canceled, mNewOnes.size()));
                                mNewOnes.add(0, headerDelegate);
                            }
                            if (!StudentListActivity.hasHeader(mOldOnes) && !mOldOnes.isEmpty()) {
                                mOldOnes.add(0, new StudentListActivity.HeaderDelegate(getString(R.string.act_has_canceled_before, mOldOnes.size())));
                            }
                            mDataList.clear();
                            mDataList.addAll(mNewOnes);
                            mDataList.addAll(mOldOnes);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public static class CanceledStudent extends StudentListActivity.Student{

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mStudentList != null && mStudentList.size() > 0) {
                DBManager.getInctance(getActivity()).createDB().createTableIfNotExist(CanceledStudent.class);
                DBManager.db.deleteAll(CanceledStudent.class);
                DBManager.db.saveAll(mStudentList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
