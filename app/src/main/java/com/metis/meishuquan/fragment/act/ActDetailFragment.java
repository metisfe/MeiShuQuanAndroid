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

import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.circle.ReplyActivity;
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.circle.CirclePushBlogParm;
import com.metis.meishuquan.model.commons.ActiveInfo;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;

/**
 * Created by WJ on 2015/4/29.
 */
public class ActDetailFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = ActDetailFragment.class.getSimpleName();

    private static ActDetailFragment sFragment = new ActDetailFragment ();

    public static ActDetailFragment getInstance () {
        return sFragment;
    }

    private ActiveInfo mInfo = null;

    private ImageView mCoverIv = null;
    private TextView mTitleTv = null, mDescTv = null, mAwardTv = null, mDetailTv = null;
    private Button mJoinBtn = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_act_detail, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCoverIv = (ImageView)view.findViewById(R.id.act_detail_cover);
        mTitleTv = (TextView)view.findViewById(R.id.act_detail_title);
        mDescTv = (TextView)view.findViewById(R.id.act_detail_desc);
        mAwardTv = (TextView)view.findViewById(R.id.act_detail_award);
        mDetailTv = (TextView)view.findViewById(R.id.act_detail_text);
        mJoinBtn = (Button)view.findViewById(R.id.act_detail_join);
        mJoinBtn.setOnClickListener(this);
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
                        Log.v(TAG, TAG + " onActivityCreated " + activeInfo.getContent());
                        fillInfo(mInfo);
                    }

                }
            });
        } else {
            fillInfo(mInfo);
        }
    }

    private void fillInfo (ActiveInfo info) {
        ImageLoaderUtils.getImageLoader(getActivity()).displayImage("https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=91b19610064f78f0805ec9b31f0c3e67/cdbf6c81800a19d8d6628c4136fa828ba61e4614.jpg"
                /*info.getImage()*/, mCoverIv, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher)
        );
        mTitleTv.setText(info.getTitle());
        mDescTv.setText(info.getDesc());
        mAwardTv.setText(info.getActivityAward());
        mDetailTv.setText(info.getContent());
        /*mJoinBtn.setEnabled(!info.isUseState());
        mJoinBtn.setText(info.isUseState() ? );*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_detail_join:
                if (mInfo != null) {
                    Intent it = new Intent(getActivity(), ReplyActivity.class);
                    CirclePushBlogParm parm = new CirclePushBlogParm();
                    parm.setType(SupportTypeEnum.Activity.getVal());
                    parm.setRelayId(mInfo.getpId());
                    it.putExtra(ReplyActivity.PARM, parm);
                    it.putExtra(ReplyActivity.TITLE, mInfo.getTitle());
                    it.putExtra(ReplyActivity.CONTENT, mInfo.getContent());
                    it.putExtra(ReplyActivity.IMAGEURL, mInfo.getImage());
                    startActivity(it);
                    Toast.makeText(getActivity(), "onClick " + mInfo.getpId(), Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
}
