package com.metis.meishuquan.activity.course;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.fragment.course.CourseWordAndPhontoInfoFragment;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.BLL.CourseOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.course.CourseInfo;
import com.metis.meishuquan.model.enums.BlockTypeEnum;
import com.metis.meishuquan.model.enums.PrivateResultEnum;
import com.metis.meishuquan.model.enums.PrivateTypeEnum;
import com.metis.meishuquan.model.enums.SupportStepTypeEnum;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.popup.SharePopupWindow;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

public class CourseInfoActivity extends FragmentActivity {
    private static final String KEY_COURSE_INFO = "COURSE_INFO";
    private TextView tvAuthor, tvTitle, tvCreateTime, tvReadCount, tvSupportAddOne, tvStepAddOne, tvSupportCount, tvStepCount;
    private RelativeLayout rlSupport, rlStep, rlWriteComment, rlCommentList, rlPrivate, rlShare, rlInputComment, rlSend;
    private LinearLayout ll_content, llRelation, ll_support_step;
    private Button btnBack;
    private ImageView imgAuthor, imgContent;
    private ImageView imgSupport, imgStep, imgPrivate;
    private EditText editText;
    private CourseInfo courseInfo;
    private Animation animation;

    private int courseId = -1;
    private boolean isPrivate = false;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_course_info);
        if (getIntent().getExtras() != null) {
            this.courseId = getIntent().getIntExtra("courseId", -1);
        }
        initView();
        getInfo();
        initEvent();
    }

    private void initView() {
        tvAuthor = (TextView) this.findViewById(R.id.id_tv_author);
        tvTitle = (TextView) this.findViewById(R.id.id_tv_title);
        tvCreateTime = (TextView) this.findViewById(R.id.id_tv_create_time);
        tvReadCount = (TextView) this.findViewById(R.id.id_tv_read_count);

        imgAuthor = (ImageView) this.findViewById(R.id.id_img_dynamic);
        imgSupport = (ImageView) this.findViewById(R.id.id_img_support);
        imgStep = (ImageView) this.findViewById(R.id.id_img_step);
        imgPrivate = (ImageView) this.findViewById(R.id.id_img_favorite);

        llRelation = (LinearLayout) this.findViewById(R.id.id_ll_relation);
        ll_content = (LinearLayout) this.findViewById(R.id.id_ll_class_content);//内容父布局
        ll_support_step = (LinearLayout) this.findViewById(R.id.id_ll_support_step);

        btnBack = (Button) this.findViewById(R.id.id_course_info_btn_back);

        rlSupport = (RelativeLayout) this.findViewById(R.id.id_rl_support);//赞
        tvSupportAddOne = (TextView) this.findViewById(R.id.id_tv_support_add_one);
        tvStepAddOne = (TextView) this.findViewById(R.id.id_tv_step_add_one);
        tvSupportCount = (TextView) this.findViewById(R.id.id_tv_support_count);
        tvStepCount = (TextView) this.findViewById(R.id.id_tv_step_count);

        rlStep = (RelativeLayout) this.findViewById(R.id.id_rl_step);//踩
        rlWriteComment = (RelativeLayout) this.findViewById(R.id.id_rl_writecomment);
        rlCommentList = (RelativeLayout) this.findViewById(R.id.id_rl_commentlist);
        rlPrivate = (RelativeLayout) this.findViewById(R.id.id_rl_private);
        rlShare = (RelativeLayout) this.findViewById(R.id.id_rl_share);
        rlInputComment = (RelativeLayout) this.findViewById(R.id.id_rl_input);
        rlSend = (RelativeLayout) this.findViewById(R.id.id_rl_send);

        editText = (EditText) this.findViewById(R.id.id_comment_edittext);

        animation = AnimationUtils.loadAnimation(this, R.anim.support_add_one);
        fm = this.getSupportFragmentManager();
    }

    private void initEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //赞
//        rlSupport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //判断登录状态
//                if (!MainApplication.isLogin()) {
//                    startActivity(new Intent(CourseInfoActivity.this, LoginActivity.class));
//                    return;
//                }
//
//                int count = courseInfo.getData().getSupportCount();
//                Object supportCount = tvSupportCount.getTag();
//                if (tvStepCount.getTag() != null) {
//                    Toast.makeText(MainApplication.UIContext, "已踩", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (supportCount != null) {
//                    int temp = (int) supportCount;
//                    if (temp == count + 1) {
//                        Toast.makeText(MainApplication.UIContext, "已赞", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//                //点赞加1效果
//                supportOrStep(tvSupportCount, tvSupportAddOne, imgSupport, count, true);
//
//                CommonOperator.getInstance().supportOrStep(MainApplication.userInfo.getUserId(), courseInfo.getData().getCourseId(), SupportStepTypeEnum.Course, 1, new ApiOperationCallback<ReturnInfo<String>>() {
//                    @Override
//                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
//                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
//                            Log.i("supportOrStep", "赞成功");
//                        }
//                    }
//                });
//            }
//        });
//
//        //踩
//        rlStep.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //判断登录状态
//                if (!MainApplication.isLogin()) {
//                    startActivity(new Intent(CourseInfoActivity.this, LoginActivity.class));
//                    return;
//                }
//
//                //点踩加1效果
//                int count = courseInfo.getData().getOppositionCount();
//                Object stepCount = tvSupportCount.getTag();
//                if (tvSupportCount.getTag() != null) {
//                    Toast.makeText(MainApplication.UIContext, "已赞", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (stepCount != null) {
//                    int temp = (int) stepCount;
//                    if (temp == count + 1) {
//                        Toast.makeText(MainApplication.UIContext, "已踩", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//                supportOrStep(tvStepCount, tvStepAddOne, imgStep, count, false);
//
//                CommonOperator.getInstance().supportOrStep(MainApplication.userInfo.getUserId(), courseInfo.getData().getCourseId(), SupportStepTypeEnum.Course, 1, new ApiOperationCallback<ReturnInfo<String>>() {
//                    @Override
//                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
//                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
//                            Log.i("supportOrStep", "赞成功");
//                        }
//                    }
//                });
//            }
//        });

        //写评论
        rlWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.isLogin()) {
                    showInputView();
                } else {
                    Intent intent = new Intent(CourseInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        rlSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                if (!content.isEmpty()) {
                    CommonOperator.getInstance().publishComment(MainApplication.userInfo.getUserId(), courseId, content, 0, BlockTypeEnum.COURSE, new ApiOperationCallback<ReturnInfo<String>>() {
                        @Override
                        public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                            if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                Toast.makeText(CourseInfoActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                hideInputView();
                            } else {
                                Log.e("publishComment", "发送失败");
                            }
                        }
                    });
                } else {
                    Toast.makeText(CourseInfoActivity.this, "请输入评论内容", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //评论列表
        rlCommentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //收藏
        rlPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainApplication.isLogin()) {
                    if (!isPrivate) {
                        //收藏
                        CommonOperator.getInstance().favorite(MainApplication.userInfo.getUserId(), courseId, PrivateTypeEnum.COURSE, PrivateResultEnum.PRIVATE, new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Toast.makeText(MainApplication.UIContext, "收藏成功", Toast.LENGTH_SHORT).show();
                                    isPrivate = true;
                                    imgPrivate.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_topline_private));
                                }
                            }
                        });
                    } else {
                        //取消收藏
                        CommonOperator.getInstance().favorite(MainApplication.userInfo.getUserId(), courseId, PrivateTypeEnum.COURSE, PrivateResultEnum.CANCEL, new ApiOperationCallback<ReturnInfo<String>>() {
                            @Override
                            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                    Toast.makeText(MainApplication.UIContext, "取消收藏", Toast.LENGTH_SHORT).show();
                                    isPrivate = false;
                                    imgPrivate.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_topline_unprivate));
                                }
                            }
                        });
                    }
                } else {
                    Intent intent = new Intent(CourseInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        //分享
        rlShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SharePopupWindow(CourseInfoActivity.this, getRootView(CourseInfoActivity.this));
            }
        });
    }

    private static ViewGroup getRootView(Activity context) {
        return (ViewGroup) ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    private void supportOrStep(TextView tvCount, final TextView tvAddOne, ImageView img, int count, boolean isSupport) {
        tvAddOne.setVisibility(View.VISIBLE);
        tvAddOne.startAnimation(animation);
        int addCount = count + 1;
        tvCount.setText("(" + addCount + ")");
        tvCount.setTag(count + 1);
        tvCount.setTextColor(Color.RED);
        if (isSupport) {
            img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_support));
        } else {
            img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_step));
        }

        new Handler().postDelayed(new Runnable() {
            public void run() {
                tvAddOne.setVisibility(View.GONE);
            }
        }, 500);
    }

    private void hideInputView() {
        Utils.hideInputMethod(this, editText);
        rlInputComment.setVisibility(View.GONE);
    }

    private void showInputView() {
        //显示输入框
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        rlInputComment.setVisibility(View.VISIBLE);
        editText.setText("");
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    private void getInfo() {
        if (courseId == -1) {
            Log.e("courseId", "未初始化");
            return;
        }
        CourseOperator.getInstance().getCourseDetial(courseId, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && Integer.parseInt(result.getInfo()) == 0) {
                    Gson gson = new Gson();
                    String json = gson.toJson(result);
                    Log.i("courseInfo", json);
                    courseInfo = gson.fromJson(json, new TypeToken<CourseInfo>() {
                    }.getType());
                    if (courseInfo != null) {
                        //根据内容类型显示图片混排模式或大图
                        if (courseInfo.getData().getCourseType() == 0) {//图片混排模式
                            CourseWordAndPhontoInfoFragment courseWordAndPhontoInfoFragment = new CourseWordAndPhontoInfoFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(KEY_COURSE_INFO, (java.io.Serializable) courseInfo);
                            courseWordAndPhontoInfoFragment.setArguments(bundle);
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.add(R.id.id_ll_content, courseWordAndPhontoInfoFragment);
                            ft.commit();
                        } else {//大图
                            //bindData();
//                            addViewByContent();
                        }
                    }
                } else if (result != null && Integer.parseInt(result.getInfo()) == 1) {
                    Toast.makeText(CourseInfoActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bindData() {
        this.tvAuthor.setText(courseInfo.getData().getAuthor() != null ? courseInfo.getData().getAuthor().getName() : "");
        this.tvTitle.setText(courseInfo.getData().getTitle());
        this.tvCreateTime.setText(courseInfo.getData().getCreateTime());
        this.tvReadCount.setText("阅读(" + courseInfo.getData().getViewCount() + ")");
    }

//    private void addViewByContent() {
//        if (courseInfo.getData() != null) {
//            String json = courseInfo.getData().getContent();
//            Log.i("content", json);
//            Gson gson = new Gson();
//            ContentInfo[] lstContentInfo = gson.fromJson(json, ContentInfo[].class);
//            if (lstContentInfo == null) {
//                return;
//            }
//            for (int i = 0; i < lstContentInfo.length; i++) {
//                ContentInfo contentInfo = lstContentInfo[i];
//                if (contentInfo.getType().equals("TXT")) {
//                    if (contentInfo.getData().getContentType().equals("p")) {
//                        addTextView(contentInfo.getData().getContent());
//                    } else if (contentInfo.getData().getContentType().equals("")) {
//                        addTextView(contentInfo.getData().getContent());
//                    }
//                }
//                if (contentInfo.getType().equals("IMG")) {
//                    addImageView(contentInfo.getData().getUrl(), contentInfo.getData().getWidth(), contentInfo.getData().getHeight());
//                }
//                if (contentInfo.getType().equals("VOIDE")) {
//                    //addImageView(contentInfo.getData().getUrl(), contentInfo.getData().getWidth(), contentInfo.getData().getHeight());
//                }
//            }
//            ll_support_step.setVisibility(View.VISIBLE);
//        }
//    }
//
//    //添加视图控件
//    private void addImageView(String url, int width, int height) {
//        SmartImageView imageView = new SmartImageView(this);
//        imageView.setImageUrl(url.trim());
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width * 2, height * 2);
//        lp.topMargin = 10;
//        lp.gravity = Gravity.CENTER_HORIZONTAL;
//        imageView.setLayoutParams(lp);
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //TODO:大图浏览
//            }
//        });
//
//        ll_content.addView(imageView);
//    }
//
//    //添加文本控件
//    private void addTextView(String words) {
//        TextView textView = new TextView(this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.topMargin = 10;
//        textView.setLayoutParams(lp);
//        textView.setTextSize(16);
//        textView.setTextColor(getResources().getColor(R.color.tv_channel_item));
//
//        int k = 0;
//        StringTokenizer tokenizer = new StringTokenizer(words, "$");
//
//        String[] str = new String[tokenizer.countTokens()];
//
//        while (tokenizer.hasMoreTokens()) {
//            str[k] = new String();
//            str[k] = tokenizer.nextToken().trim();
//            k++;
//        }
//
//        for (int i = 0; i < str.length; i++) {
//            Log.i(String.valueOf(i), str[i]);
//        }
//
//        //将link占位符替换成链接文字
//        for (int j = 0; j < str.length; j++) {
//            for (int i = 0; i < courseInfo.getData().getUrls().size(); i++) {
//                Urls url = courseInfo.getData().getUrls().get(i);
//                String newShowContent = url.getNewShowContent().trim();
//                if (str[j].equals(newShowContent)) {
//                    str[j] = url.getDescription();
//                }
//            }
//        }
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < str.length; i++) {
//            sb.append(str[i]);
//        }
//
//        //得到最终包含有链接文字的字符串
//        String finalWord = sb.toString().trim();
//
//        //创建一个 SpannableString对象
//        SpannableString sp = new SpannableString(finalWord);
//        if (!finalWord.equals("")) {
//            List<Urls> lstUrl = courseInfo.getData().getUrls();
//            for (int i = 0; i < lstUrl.size(); i++) {
//                String key = lstUrl.get(i).getDescription();
//                if (!key.equals("")) {
//                    if (finalWord.contains(key)) {
//                        //设置超链接
//                        sp.setSpan(new URLSpan(lstUrl.get(i).getDir()), finalWord.indexOf(key), finalWord.indexOf(key) + key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    }
//                }
//            }
//            textView.setText(sp);
//            textView.setMovementMethod(LinkMovementMethod.getInstance());
//        }
//
//        ll_content.addView(textView);
//    }
}
