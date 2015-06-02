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
public class StudentJoinedFragment extends Fragment {

    private RecyclerView mRecyclerView = null;
    private LinearLayoutManager mLinearLayoutManager = null;

    private StudentListActivity.StudentAdapter mAdapter = null;
    private List<AbsDelegate> mDataList = new ArrayList<AbsDelegate>();

    private List<StudentListActivity.Student> mNewStudentList = null;

    private List<StudentListActivity.Student> mOldStudentList = null;

    List<AbsDelegate> mNewDelegates = new ArrayList<AbsDelegate>();
    List<AbsDelegate> mOldDelegates = new ArrayList<AbsDelegate>();

    private String mLastCreateTime = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        mAdapter = new StudentListActivity.StudentAdapter(getActivity(), mDataList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            DBManager.getInctance(getActivity()).createDB().createTableIfNotExist(StudentListActivity.Student.class);
            mOldStudentList = DBManager.db.findAll(StudentListActivity.Student.class);
            if (mOldStudentList != null && mOldStudentList.size() > 0) {
                mLastCreateTime = mOldStudentList.get(0).createDatetime;
                List<StudentListActivity.StudentDelegate> delegates = new ArrayList<StudentListActivity.StudentDelegate>();
                for (StudentListActivity.Student student : mOldStudentList) {
                    delegates.add(new StudentListActivity.StudentDelegate(student));
                }
                mDataList.add(0, new StudentListActivity.HeaderDelegate(getString(R.string.act_has_joined_before, delegates.size())));
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
                    /*long lastId = 0;
                    if (mDataList != null && mDataList.size() > 0) {
                        Object obj = mDataList.get(mDataList.size() - 1).getSource();
                        if (obj instanceof StudentListActivity.Student) {
                            lastId = ((StudentListActivity.Student) obj).userId;
                        }
                    }*/
                    loadData(activeInfo, 0);
                }
            }
        });
    }

    private void loadData (ActiveInfo activeInfo, final long lastUserId) {
        ActiveOperator.getInstance().getStudioStudent(activeInfo.getpId(), lastUserId, new UserInfoOperator.OnGetListener<List<StudentListActivity.Student>>() {
            @Override
            public void onGet(boolean succeed, List<StudentListActivity.Student> students) {
                if (succeed) {
                    if (lastUserId == 0) {
                        mNewStudentList = students;
                        mNewDelegates.clear();
                        mOldDelegates.clear();
                    }

                    for (StudentListActivity.Student student : students) {
                        StudentListActivity.StudentDelegate delegate = new StudentListActivity.StudentDelegate(student);
                        delegate.isNewOne = student.createDatetime.compareTo(mLastCreateTime) > 0;
                        delegate.isFriend = StudentListActivity.isFriend(getActivity(), student.userId);
                        if (delegate.isNewOne) {
                            mNewDelegates.add(delegate);
                        } else {
                            mOldDelegates.add(delegate);
                        }
                    }

                    if (!StudentListActivity.hasHeader(mNewDelegates) && !mNewDelegates.isEmpty()) {
                        StudentListActivity.HeaderDelegate headerDelegate = new StudentListActivity.HeaderDelegate(getString(R.string.act_lastest_joined, mNewDelegates.size()));
                        mNewDelegates.add(0, headerDelegate);
                    }
                    if (!StudentListActivity.hasHeader(mOldDelegates) && !mOldDelegates.isEmpty()) {
                        mOldDelegates.add(0, new StudentListActivity.HeaderDelegate(getString(R.string.act_has_joined_before, mOldDelegates.size())));
                    }
                    if (lastUserId == 0) {
                        mDataList.clear();
                    }
                    mDataList.addAll(mNewDelegates);
                    mDataList.addAll(mOldDelegates);
                    //mDataList.addAll(delegates);
                                /*if (delegates.size() > 0) {
                                    mDataList.addAll(0, delegates);
                                    StudentListActivity.HeaderDelegate headerDelegate = new StudentListActivity.HeaderDelegate(getString(R.string.act_lastest_joined, delegates.size()));
                                    mDataList.add(0, headerDelegate);
                                }*/
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mNewStudentList != null && mNewStudentList.size() > 0) {
                DBManager.getInctance(getActivity()).createDB().createTableIfNotExist(StudentListActivity.Student.class);
                DBManager.db.deleteAll(StudentListActivity.Student.class);
                DBManager.db.saveAll(mNewStudentList);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
