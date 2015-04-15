package com.metis.meishuquan.fragment.circle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.fragment.main.CircleFragment;
import com.metis.meishuquan.model.circle.CircleMomentComment;
import com.metis.meishuquan.util.SharedPreferencesUtil;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.util.ViewUtils;
import com.metis.meishuquan.view.circle.moment.MomentActionBar;
import com.metis.meishuquan.view.circle.moment.MomentPageListView;
import com.metis.meishuquan.view.circle.moment.comment.EmotionEditText;
import com.metis.meishuquan.view.circle.moment.comment.EmotionSelectView;
import com.metis.meishuquan.view.popup.SharePopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * comment
 * <p/>
 * Created by jx on 15/4/11.
 */
public class MomentCommentFragment extends Fragment {
    private static final int KEYBOARD_DRAWABLE_RESOURCE_ID = R.drawable.circle_moment_emotion_icon_keyboard;
    private static final int EMOTION_DRAWABLE_RESOURCE_ID = R.drawable.circle_moment_icon_emotion;
    private static final int ScreenHeight = MainApplication.getDisplayMetrics().heightPixels;
    private static final int SoftInputMinHeight = ScreenHeight / 5;
    private static final int EmotionSelectViewHeight = MainApplication.Resources.getDimensionPixelSize(R.dimen.emotion_input_height);
    private static final int EmotionSwitchContainerHeight = MainApplication.Resources.getDimensionPixelSize(R.dimen.switch_emotion_container_height);
    private static final int MarginTopForEmotionSwitch = ScreenHeight - EmotionSelectViewHeight - EmotionSwitchContainerHeight;

    private ViewGroup rootView;

    private FragmentManager fm;
    private TextView cancelButton;

    private Button buttonPublish;
    private ProgressDialog progressDialog;
    private String originalMessage;
    private EmotionEditText editText;
    private EmotionSelectView emotionSelectView;
    private LinearLayout emotionKeyboardSwitchButtonContainer;
    private ImageButton emotionKeyboardSwitchButton;
    private boolean isEmotionSoftInputSwitchInMiddle;
    private boolean isEmotionInputShown;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Bundle args = this.getArguments();
//        if (args != null) {
//            newsId = args.getInt("newsId");
//            getInfoData(newsId);
//        }
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_moment_comment, null, false);
        fm = getActivity().getSupportFragmentManager();

        cancelButton = (TextView) rootView.findViewById(R.id.moment_comment_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MomentDetailFragment momentDetailFragment = new MomentDetailFragment();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
                ft.replace(R.id.content_container, momentDetailFragment);
                ft.commit();
            }
        });

        this.emotionKeyboardSwitchButtonContainer = (LinearLayout) rootView.findViewById(R.id.switch_emotion_container);
        this.emotionKeyboardSwitchButton = (ImageButton) rootView.findViewById(R.id.switch_emotion);
        this.emotionKeyboardSwitchButton.setImageResource(EMOTION_DRAWABLE_RESOURCE_ID);
        this.emotionKeyboardSwitchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (emotionSelectView.getVisibility() == View.GONE)
                {
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
                }
                else
                {
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
        editText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (emotionSelectView.isShown())
                {
                    emotionSelectView.setVisibility(View.GONE);
                    emotionKeyboardSwitchButton.setImageResource(EMOTION_DRAWABLE_RESOURCE_ID);
                }
            }
        });

        emotionSelectView = (EmotionSelectView) rootView.findViewById(R.id.emotion_input);
        emotionSelectView.setEditText(editText);

        showKeyBoard();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showKeyBoard()
    {
        InputMethodManager imm = (InputMethodManager) MainApplication.MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void hideKeyBoard()
    {
        ((InputMethodManager) MainApplication.MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MainApplication.MainActivity
                .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        final View fragmentRootView = rootView.findViewById(R.id.ll_parent);
        final ViewTreeObserver observer = fragmentRootView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                Rect r = new Rect();
                fragmentRootView.getWindowVisibleDisplayFrame(r);
                int visible_height = r.bottom;
                // soft input pop out
                if (ScreenHeight - visible_height > SoftInputMinHeight && !isEmotionSoftInputSwitchInMiddle)
                {
                    isEmotionSoftInputSwitchInMiddle = true;
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    params.setMargins(0, visible_height - EmotionSwitchContainerHeight, 0, 0);
                    emotionKeyboardSwitchButtonContainer.setLayoutParams(params);
                }

                // soft input hide
                if (visible_height == ScreenHeight && isEmotionSoftInputSwitchInMiddle)
                {
                    isEmotionSoftInputSwitchInMiddle = false;
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    if (isEmotionInputShown)
                    {
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        params.setMargins(0, MarginTopForEmotionSwitch, 0, 0);
                    }
                    else
                    {
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    }

                    emotionKeyboardSwitchButtonContainer.setLayoutParams(params);
                }
            }
        });
    }

}