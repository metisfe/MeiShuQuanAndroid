package com.metis.meishuquan.fragment.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.act.SelectStudioActivity;
import com.metis.meishuquan.activity.circle.ReplyActivity;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.circle.CirclePushBlogParm;
import com.metis.meishuquan.model.commons.ActiveInfo;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;

/**
 * Created by WJ on 2015/4/29.
 */
public class ActDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ActDetailFragment.class.getSimpleName();

    private static ActDetailFragment sFragment = new ActDetailFragment();

    public static ActDetailFragment getInstance() {
        return sFragment;
    }

    private ActiveInfo mInfo = null;
    private ActiveOperator.SimpleActiveInfo mSimpleActiveInfo = null;

    private ImageView mCoverIv = null;
    private TextView mTitleTv = null, mDescTv = null, mAwardTv = null, mDetailTv = null;
    private Button mJoinBtn = null, mCheckBtn = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_act_detail, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCoverIv = (ImageView) view.findViewById(R.id.act_detail_cover);
        mTitleTv = (TextView) view.findViewById(R.id.act_detail_title);
        mDescTv = (TextView) view.findViewById(R.id.act_detail_desc);
        mAwardTv = (TextView) view.findViewById(R.id.act_detail_award);
        mDetailTv = (TextView) view.findViewById(R.id.act_detail_text);
        mJoinBtn = (Button) view.findViewById(R.id.act_detail_join);
        mCheckBtn = (Button) view.findViewById(R.id.act_check_studio);
        mJoinBtn.setOnClickListener(this);
        mCheckBtn.setOnClickListener(this);

        Log.v(TAG, TAG + " onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, TAG + " onActivityCreated");
        if (mInfo == null) {
            ActiveOperator.getInstance().getActiveDetail(new UserInfoOperator.OnGetListener<ActiveInfo>() {
                @Override
                public void onGet(boolean succeed, ActiveInfo activeInfo) {
                    if (succeed) {
                        mInfo = activeInfo;
                        ActiveOperator.getInstance().getMyActiveInfo(mInfo.getpId(), new UserInfoOperator.OnGetListener<ActiveOperator.SimpleActiveInfo>() {
                            @Override
                            public void onGet(boolean succeed, ActiveOperator.SimpleActiveInfo simpleActiveInfo) {
                                if (succeed) {
                                    mSimpleActiveInfo = simpleActiveInfo;
                                    fillInfo(mInfo);
                                }
                            }
                        });
                        Log.v(TAG, TAG + " onActivityCreated " + activeInfo.getContent());

                    }

                }
            });
        } else {
            fillInfo(mInfo);
        }
    }

    private void fillInfo(ActiveInfo info) {
        ImageLoaderUtils.getImageLoader(getActivity()).displayImage(
                info.getImage(), mCoverIv, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher)
        );
        mTitleTv.setText(info.getTitle());
        mDescTv.setText(info.getDesc());
        mAwardTv.setText(info.getActivityAward());
        mDetailTv.setText(info.getContent());
        mJoinBtn.setVisibility(View.VISIBLE);
        if (mSimpleActiveInfo != null && mSimpleActiveInfo.isJoin) {
            mJoinBtn.setText(R.string.act_has_joined);
        }
        //mCheckBtn.setVisibility(View.VISIBLE);
        /*mJoinBtn.setEnabled(!info.isUseState());
        mJoinBtn.setText(info.isUseState() ? );*/
    }

    @Override
    public void onClick(View view) {
        Intent it = null;
        switch (view.getId()) {
            case R.id.act_detail_join:
                if (!MainApplication.isLogin()) {
                    Toast.makeText(getActivity(), R.string.my_info_toast_not_login, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                Log.i("user_role",MainApplication.userInfo.getUserRole()+"&&&&&&&&&&");
                if (MainApplication.userInfo.getUserRole() == IdTypeEnum.TEACHER.getVal()) {
                    Toast.makeText(getActivity(), R.string.act_join_only_student, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mInfo != null) {
                    if (mSimpleActiveInfo != null && mSimpleActiveInfo.isJoin) {
                        Toast.makeText(getActivity(), R.string.act_joined, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    it = new Intent(getActivity(), ReplyActivity.class);
                    CirclePushBlogParm parm = new CirclePushBlogParm();
                    parm.setType(SupportTypeEnum.Activity.getVal());
                    parm.setRelayId(mInfo.getpId());
                    it.putExtra(ReplyActivity.PARM, parm);
                    it.putExtra(ReplyActivity.TITLE, mInfo.getTitle());
                    it.putExtra(ReplyActivity.CONTENT, mInfo.getContent());
                    it.putExtra(ReplyActivity.IMAGEURL, mInfo.getImage());
                }
                break;
            case R.id.act_check_studio:
                if (mInfo != null) {
                    it = new Intent(getActivity(), SelectStudioActivity.class);
                }
                break;
        }
        startActivity(it);
    }
}
