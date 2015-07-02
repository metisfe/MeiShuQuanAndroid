package com.metis.meishuquan.fragment.circle;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.CircleOperator;
import com.metis.meishuquan.model.circle.CCircleCommentModel;
import com.metis.meishuquan.model.circle.CParamCircleComment;
import com.metis.meishuquan.model.circle.CUserModel;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.util.GlobalData;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.util.ViewUtils;
import com.metis.meishuquan.view.circle.moment.comment.EmotionEditText;
import com.metis.meishuquan.view.circle.moment.comment.EmotionSelectView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.umeng.analytics.MobclickAgent;

/**
 * comment
 * <p/>
 * Created by jx on 15/4/11.
 */
public class MomentCommentFragment extends Fragment {
    public static final String CLASS_NAME = MomentCommentFragment.class.getSimpleName();
    public static final String KEY_CIRCLE_ID = "KEY_CIRCLE_ID";//圈子评论，回复评论
    public static final String KEY_REPLY_NAME = "KEY_REPLY_NAME";//被回复人用户名
    public static final String KEY_RELAYUSER_ID = "KEY_RELAYUSER_ID";
    public static final String KEY_ISREPLY = "KEY_ISREPLY";

    private static final int KEYBOARD_DRAWABLE_RESOURCE_ID = R.drawable.circle_moment_emotion_icon_keyboard;
    private static final int EMOTION_DRAWABLE_RESOURCE_ID = R.drawable.circle_moment_icon_emotion;
    private static final int ScreenHeight = MainApplication.getDisplayMetrics().heightPixels;
    private static final int SoftInputMinHeight = ScreenHeight / 5;
    private static final int EmotionSelectViewHeight = MainApplication.Resources.getDimensionPixelSize(R.dimen.emotion_input_height);
    private static final int EmotionSwitchContainerHeight = MainApplication.Resources.getDimensionPixelSize(R.dimen.switch_emotion_container_height);
    private int MarginTopForEmotionSwitch = ScreenHeight - EmotionSelectViewHeight - EmotionSwitchContainerHeight;
    private int StatusBarHeight;
    private ViewGroup rootView;

    private FragmentManager fm;
    private TextView cancelButton, publishButton;

    private TextView title;
    private Button buttonPublish;
    private ProgressDialog progressDialog;
    private String originalMessage;
    private EmotionEditText editText;
    private EmotionSelectView emotionSelectView;
    private LinearLayout emotionKeyboardSwitchButtonContainer;
    private ImageButton emotionKeyboardSwitchButton;
    private boolean isEmotionSoftInputSwitchInMiddle;
    private boolean isEmotionInputShown;

    private boolean isReplay;

    private MomentDetailFragment.OnCommentSuccessListner onCommentSuccessListner;

    public void setOnCommentSuccessListner(MomentDetailFragment.OnCommentSuccessListner onCommentSuccessListner) {
        this.onCommentSuccessListner = onCommentSuccessListner;
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(CLASS_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Bundle args = this.getArguments();
//        if (args != null) {
//            newsId = args.getInt("newsId");
//            getInfoData(newsId);
//        }
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_moment_comment, null, false);
        fm = getActivity().getSupportFragmentManager();

        title = (TextView) rootView.findViewById(R.id.moment_detail_tv_nickname);
        isReplay = getArguments().getBoolean(KEY_ISREPLY);
        if (isReplay) {
            title.setText("回复评论");
        } else {
            title.setText("写评论");
        }

        cancelButton = (TextView) rootView.findViewById(R.id.moment_comment_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(MomentCommentFragment.this).commit();
                /*MomentDetailFragment momentDetailFragment = new MomentDetailFragment();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.add(R.id.content_container, momentDetailFragment);
                ft.remove(MomentCommentFragment.this);
                ft.commit();*/
                hideKeyBoard();
            }
        });

        publishButton = (TextView) rootView.findViewById(R.id.moment_comment_publish);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(getActivity(), "", "正在发送...");
//                Toast.makeText(MainApplication.UIContext, "正在发送，请稍候", Toast.LENGTH_SHORT).show();
//                String encodeContent = "";
//                try {
//                    encodeContent = URLEncoder.encode(editText.getText().toString(), "utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

                int id = getArguments().getInt(KEY_CIRCLE_ID, 0);
                int relayUserId = getArguments().getInt(KEY_RELAYUSER_ID);
                final String userName = getArguments().getString(KEY_REPLY_NAME, "");
                CParamCircleComment param = new CParamCircleComment();
                param.setId(id);
                if (isReplay) {
                    param.setRelyUserId(relayUserId);
                }
                param.setContent(editText.getText().toString());
                CircleOperator.getInstance().pushCommentByPost(param, new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception e, ServiceFilterResponse serviceFilterResponse) {
                        progressDialog.dismiss();
                        if (result != null && result.isSuccess()) {
                            if (result == null || !result.isSuccess()) {
                                Toast.makeText(MainApplication.UIContext, "发送失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Toast.makeText(MainApplication.UIContext, "发送成功！", Toast.LENGTH_SHORT).show();
                            GlobalData.momentsCommentCount += 1;

                            progressDialog.dismiss();
                            MomentDetailFragment momentDetailFragment = new MomentDetailFragment();
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                            ft.remove(MomentCommentFragment.this);
                            if (GlobalData.fromMomentsFragment == 1) {
                                ft.add(R.id.content_container, momentDetailFragment);
                            }
                            ft.commit();
                            hideKeyBoard();
                            CCircleCommentModel circleCommentModel = new CCircleCommentModel();
                            circleCommentModel.circleId = GlobalData.moment.id;
                            if (isReplay) {
                                circleCommentModel.content = "回复 " + userName + ":" + editText.getText().toString();
                            } else {
                                circleCommentModel.content = editText.getText().toString();
                            }
                            circleCommentModel.createTime = Utils.getCurrentTime();
                            circleCommentModel.user = new CUserModel();
                            circleCommentModel.user.userId = MainApplication.userInfo.getUserId();
                            circleCommentModel.user.avatar = MainApplication.userInfo.getUserAvatar();
                            circleCommentModel.user.name = MainApplication.userInfo.getName();
                            onCommentSuccessListner.onSuccess(circleCommentModel);
                        } else if (result != null && !result.isSuccess()) {
                            Log.i("pushCommentByPost", "errorcode:" + result.getErrorCode() + "message:" + result.getMessage());
                        }
                    }
                });


//                String url = String.format("v1.1/Circle/PushComment?id=%s&content=%s&session=%s", GlobalData.moment.id, encodeContent, MainApplication.userInfo.getCookie());
//                publishButton.setClickable(false);
//                ApiDataProvider.getmClient().invokeApi(url, null,
//                        HttpGet.METHOD_NAME, null, CirclePushCommentResult.class,
//                        new ApiOperationCallback<CirclePushCommentResult>() {
//                            @Override
//                            public void onCompleted(CirclePushCommentResult result, Exception exception, ServiceFilterResponse response) {
//                                progressDialog.cancel();
//                                publishButton.setClickable(true);
//                                if (result == null || !result.isSuccess()) {
//                                    Toast.makeText(MainApplication.UIContext, "发送失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//
//                                Toast.makeText(MainApplication.UIContext, "发送成功！", Toast.LENGTH_SHORT).show();
//                                GlobalData.momentsCommentCount += 1;
//
//                                progressDialog.dismiss();
//                                MomentDetailFragment momentDetailFragment = new MomentDetailFragment();
//                                FragmentManager fm = getActivity().getSupportFragmentManager();
//                                FragmentTransaction ft = fm.beginTransaction();
//                                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
//                                ft.remove(MomentCommentFragment.this);
//                                if (GlobalData.fromMomentsFragment == 1) {
//                                    ft.add(R.id.content_container, momentDetailFragment);
//                                }
//                                ft.commit();
//                                hideKeyBoard();
//                                CCircleCommentModel circleCommentModel = new CCircleCommentModel();
//                                circleCommentModel.circleId = GlobalData.moment.id;
//                                circleCommentModel.content = editText.getText().toString();
//                                circleCommentModel.createTime = Utils.getCurrentTime();
//                                circleCommentModel.user = new CUserModel();
//                                circleCommentModel.user.avatar = MainApplication.userInfo.getUserAvatar();
//                                circleCommentModel.user.name = MainApplication.userInfo.getName();
//                                onCommentSuccessListner.onSuccess(circleCommentModel);
//                            }
//                        });
            }
        });

        this.emotionKeyboardSwitchButtonContainer = (LinearLayout) rootView.findViewById(R.id.switch_emotion_container);
        this.emotionKeyboardSwitchButton = (ImageButton) rootView.findViewById(R.id.switch_emotion);
        this.emotionKeyboardSwitchButton.setImageResource(EMOTION_DRAWABLE_RESOURCE_ID);
        this.emotionKeyboardSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emotionSelectView.getVisibility() == View.GONE) {
                    isEmotionInputShown = true;
                    hideKeyBoard();
                    ViewUtils.delayExecute(new Runnable() {
                        @Override
                        public void run() {
                            Animation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
                            alphaAnimation.setDuration(200);
                            emotionSelectView.startAnimation(alphaAnimation);
                            emotionSelectView.setVisibility(View.VISIBLE);
                            emotionKeyboardSwitchButton.setImageResource(KEYBOARD_DRAWABLE_RESOURCE_ID);

                            //reposition switch button to the top of emotionSelectView
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            params.setMargins(0, MarginTopForEmotionSwitch, 0, 0);
                            emotionKeyboardSwitchButtonContainer.setLayoutParams(params);
                        }
                    }, 200);
                } else {
                    isEmotionInputShown = false;
                    showKeyBoard();
                    emotionSelectView.setVisibility(View.GONE);
                    emotionKeyboardSwitchButton.setImageResource(EMOTION_DRAWABLE_RESOURCE_ID);
                }
            }
        });

        editText = (EmotionEditText) rootView.findViewById(R.id.comment_edittext);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emotionSelectView.isShown()) {
                    emotionSelectView.setVisibility(View.GONE);
                    emotionKeyboardSwitchButton.setImageResource(EMOTION_DRAWABLE_RESOURCE_ID);
                }
            }
        });

        emotionSelectView = (EmotionSelectView) rootView.findViewById(R.id.emotion_input);
        emotionSelectView.setEditText(editText);

        StatusBarHeight = this.getStatusBarHeight();
        MarginTopForEmotionSwitch = ScreenHeight - EmotionSelectViewHeight - EmotionSwitchContainerHeight - StatusBarHeight;

        showKeyBoard();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) MainApplication.MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void hideKeyBoard() {
        ((InputMethodManager) MainApplication.MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity()
                .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onResume() {
        super.onResume();

        final View fragmentRootView = rootView.findViewById(R.id.ll_parent);
        final ViewTreeObserver observer = fragmentRootView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                fragmentRootView.getWindowVisibleDisplayFrame(r);
                int visible_height = r.bottom;
                // soft input pop out
                if (ScreenHeight - visible_height > SoftInputMinHeight && !isEmotionSoftInputSwitchInMiddle) {
                    isEmotionSoftInputSwitchInMiddle = true;
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    params.setMargins(0, visible_height - EmotionSwitchContainerHeight - StatusBarHeight, 0, 0);
                    emotionKeyboardSwitchButtonContainer.setLayoutParams(params);
                }

                // soft input hide
                if (visible_height == ScreenHeight && isEmotionSoftInputSwitchInMiddle) {
                    isEmotionSoftInputSwitchInMiddle = false;
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    if (isEmotionInputShown) {
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        params.setMargins(0, MarginTopForEmotionSwitch, 0, 0);
                    } else {
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    }

                    emotionKeyboardSwitchButtonContainer.setLayoutParams(params);
                }
            }
        });
        MobclickAgent.onPageStart(CLASS_NAME); //统计页面
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = MainApplication.Resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = MainApplication.Resources.getDimensionPixelSize(resourceId);
        } else {
            return (int) (MainApplication.getDisplayMetrics().density * 25);
        }

        return result;
    }
}
