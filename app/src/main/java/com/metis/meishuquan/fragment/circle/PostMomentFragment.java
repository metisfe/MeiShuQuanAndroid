package com.metis.meishuquan.fragment.circle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.CircleOperator;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CirclePushBlogParm;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.metis.meishuquan.view.circle.moment.comment.EmotionEditText;
import com.metis.meishuquan.view.circle.moment.comment.EmotionSelectView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.List;

/**
 * Created by wudi on 4/2/2015.
 */
public class PostMomentFragment extends Fragment {
    private CircleTitleBar titleBar;
    private RelativeLayout rl_choose_pic;
    private RelativeLayout rl_at;
    private RelativeLayout rl_emotion;
    private EmotionEditText editText;

    private List<Integer> lstUserId = null;//@好友

    private FragmentManager fm = null;
    private boolean isSend;

    private static final int ScreenHeight = MainApplication.getDisplayMetrics().heightPixels;
    private static final int EmotionSelectViewHeight = MainApplication.Resources.getDimensionPixelSize(R.dimen.emotion_input_height);
    private static final int EmotionSwitchContainerHeight = MainApplication.Resources.getDimensionPixelSize(R.dimen.switch_emotion_container_height);
    private int MarginTopForEmotionSwitch = ScreenHeight - EmotionSelectViewHeight - EmotionSwitchContainerHeight;
    private EmotionSelectView emotionSelectView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_postmoment, container, false);
        initView(rootView);
        initTitleBar();
        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        this.titleBar = (CircleTitleBar) rootView.findViewById(R.id.fragment_circle_postmoment_title_bar);
        this.editText = (EmotionEditText) rootView.findViewById(R.id.id_et_input_cirle_post_moment);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emotionSelectView.isShown()) {
                    emotionSelectView.setVisibility(View.GONE);
//                    emotionKeyboardSwitchButton.setImageResource(EMOTION_DRAWABLE_RESOURCE_ID);
                }
            }
        });
        editText.requestFocus();
        emotionSelectView = (EmotionSelectView) rootView.findViewById(R.id.postmoment_emotion_input);
        emotionSelectView.setEditText(editText);

        this.rl_choose_pic = (RelativeLayout) rootView.findViewById(R.id.id_rl_post_moment_pic);
        this.rl_at = (RelativeLayout) rootView.findViewById(R.id.id_rl_post_moment_at);
        this.rl_emotion = (RelativeLayout) rootView.findViewById(R.id.id_rl_post_moment_emotion);

        this.fm = getActivity().getSupportFragmentManager();
    }

    private void initTitleBar() {
        titleBar.setText("发日志");
        titleBar.setRightButton("发送", 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSend) {
                    String content = editText.getText().toString();

                    CirclePushBlogParm parm = new CirclePushBlogParm();
                    parm.setContent(content);
                    parm.setDevice("美术圈");
                    parm.setType(SupportTypeEnum.Circle.getVal());

                    send(parm);
                    isSend = true;
                }
            }
        });
        titleBar.setLeftButton("取消", 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void send(CirclePushBlogParm parm) {
        CircleOperator.getInstance().pushBlog(parm, new ApiOperationCallback<ReturnInfo<CCircleDetailModel>>() {
            @Override
            public void onCompleted(ReturnInfo<CCircleDetailModel> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && result.isSuccess()) {
                    String json = new Gson().toJson(result);
                    Log.i("pushBlog", json);

                    Toast.makeText(MainApplication.UIContext, "发送成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (exception != null) {
                    Log.i("pushBlog", "exception:cause:" + exception.getCause() + "  message:" + exception.getMessage());
                }
            }
        });
        Utils.hideInputMethod(MainApplication.UIContext, editText);
    }

    private void finish() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(this);
        ft.commit();
        Utils.hideInputMethod(MainApplication.UIContext, editText);
    }

    private void initEvent() {
        //选择图片
        this.rl_choose_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //@
        this.rl_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //表情
        this.rl_emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emotionSelectView.setVisibility(View.VISIBLE);
                Utils.hideInputMethod(MainApplication.UIContext, editText);
            }
        });
    }

}
