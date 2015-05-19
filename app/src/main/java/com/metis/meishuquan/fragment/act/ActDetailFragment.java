package com.metis.meishuquan.fragment.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
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
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.model.enums.IdTypeEnum;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;

/**
 * Created by WJ on 2015/4/29.
 */
public class ActDetailFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = ActDetailFragment.class.getSimpleName();

    private static ActDetailFragment sFragment = new ActDetailFragment();

    public static ActDetailFragment getInstance() {
        return sFragment;
    }

    private ActiveInfo mInfo = null;
    private ActiveOperator.SimpleActiveInfo mSimpleActiveInfo = null;

    private ImageView mCoverIv = null;
    private TextView mTitleTv = null, mDescTv = null, mAwardTv = null, mDetailTv = null;
    private TextView mActDeals = null;
    private Button mJoinBtn = null, mCheckBtn = null;

    private User mUser = null;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mSimpleActiveInfo != null) {
                mSimpleActiveInfo.isJoin = true;
                fillBtn();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, new IntentFilter("join_succeed"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

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
        mActDeals = (TextView)view.findViewById(R.id.act_deals);
        mJoinBtn.setOnClickListener(this);
        mCheckBtn.setOnClickListener(this);

        mActDeals.setText(Html.fromHtml("<font color=\"black\">" + getString(R.string.act_deals_1) + "</font><font color=\"red\">" + getString(R.string.act_deals_2) + "</font>"));
        mActDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = Uri.parse("http://www.meishuquan.net/UserAgreement.html");
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.act_not_found_exception, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.v(TAG, TAG + " onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, TAG + " onActivityCreated");
        if (MainApplication.userInfo != null && MainApplication.isLogin()) {
            UserInfoOperator.getInstance().getUserInfo(MainApplication.userInfo.getUserId(), true, new UserInfoOperator.OnGetListener<User>() {
                @Override
                public void onGet(boolean succeed, User user) {
                    if (succeed) {
                        mUser = user;
                        if (mInfo == null) {
                            ActiveOperator.getInstance().getActiveDetail(new UserInfoOperator.OnGetListener<ActiveInfo>() {
                                @Override
                                public void onGet(boolean succeed, ActiveInfo activeInfo) {
                                    if (succeed) {
                                        mInfo = activeInfo;
                                        fillInfo(mInfo);
                                        //mJoinBtn.setVisibility(View.GONE);
                                        ActiveOperator.getInstance().getMyActiveInfo(mInfo.getpId(), new UserInfoOperator.OnGetListener<ActiveOperator.SimpleActiveInfo>() {
                                            @Override
                                            public void onGet(boolean succeed, ActiveOperator.SimpleActiveInfo simpleActiveInfo) {
                                                if (succeed) {
                                                    //mJoinBtn.setVisibility(View.VISIBLE);
                                                    mSimpleActiveInfo = simpleActiveInfo;
                                                    fillBtn();
                                                    //fillInfo(mInfo);
                                                }
                                            }
                                        });
                                        Log.v(TAG, TAG + " onActivityCreated " + activeInfo.getContent());

                                    }

                                }
                            });
                        } else {
                            fillInfo(mInfo);
                            ActiveOperator.getInstance().getMyActiveInfo(mInfo.getpId(), new UserInfoOperator.OnGetListener<ActiveOperator.SimpleActiveInfo>() {
                                @Override
                                public void onGet(boolean succeed, ActiveOperator.SimpleActiveInfo simpleActiveInfo) {
                                    if (succeed) {
                                        mSimpleActiveInfo = simpleActiveInfo;
                                        fillBtn();
                                        //fillInfo(mInfo);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    private void fillBtn () {
        if (!isResumed()) {
            return;
        }
        if (mSimpleActiveInfo != null && (mSimpleActiveInfo.pId > 0 || mSimpleActiveInfo.isJoin)) {
            mJoinBtn.setEnabled(false);
        }
        //Toast.makeText(getActivity(), "getUserRole " + mUser.getUserRole(), Toast.LENGTH_SHORT).show();
        if (mSimpleActiveInfo.pId > 0 && !mSimpleActiveInfo.isJoin) {
            mJoinBtn.setText(R.string.act_joined_already);
            //mJoinBtn.setText(R.string.act_title_check);
        } else if (mSimpleActiveInfo.isJoin) {
            mJoinBtn.setText(R.string.act_joined_already);
        }
    }

    private void fillInfo(ActiveInfo info) {
        if (!isResumed()) {
            return;
        }
        ImageLoaderUtils.getImageLoader(getActivity()).displayImage(
                info.getImage(), mCoverIv, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher)
        );

        mTitleTv.setText(info.getTitle());
        mDescTv.setText(info.getDesc());
        mAwardTv.setText(info.getActivityAward());
        mDetailTv.setText(info.getContent());
        mJoinBtn.setVisibility(View.VISIBLE);
        if (mUser != null && mUser.getUserRoleEnum() != IdTypeEnum.STUDENT) {
            mJoinBtn.setEnabled(false);
            mJoinBtn.setVisibility(View.GONE);
            mActDeals.setVisibility(View.GONE);
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

                if (mUser != null && mUser.getUserRoleEnum() != IdTypeEnum.STUDENT) {
                    Toast.makeText(getActivity(), R.string.act_join_only_student, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mInfo != null) {
                    if (mSimpleActiveInfo != null && mSimpleActiveInfo.isJoin) {
                        Toast.makeText(getActivity(), R.string.act_joined, Toast.LENGTH_SHORT).show();
                        if (mJoinedListener != null) {
                            mJoinedListener.onClick(view);
                        }
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
    private View.OnClickListener mJoinedListener = null;
    public void setOnClickListenerIfJoined (View.OnClickListener listenerIfJoined) {
        mJoinedListener = listenerIfJoined;
    }
}
