package com.metis.meishuquan.fragment.course;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.course.CourseInfo;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.model.topline.ContentInfo;
import com.metis.meishuquan.model.topline.Urls;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.List;
import java.util.StringTokenizer;

/**
 * 课程详情-图文混排
 * Created by wangjin on 15/4/29.
 */
public class CourseWordAndPhontoInfoFragment extends Fragment {
    private static final String KEY_COURSE_INFO = "COURSE_INFO";

    private TextView tvTitle, tvCreateTime, tvReadCount, tvSupportAddOne, tvStepAddOne, tvSupportCount, tvStepCount;
    private RelativeLayout rlSupport, rlStep;
    private LinearLayout ll_content, llRelation, ll_support_step;
    private ListView lvRelationRead;
    private ImageView imgSupport, imgStep, imgPrivate;
    private EditText editText;
    private CourseInfo courseInfo;
    private Animation animation;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_word_photo_class_info_content, container, false);
        initView(rootView);
        initEvent();
        Bundle bundle = getArguments();
        if (bundle != null) {
            courseInfo = (CourseInfo) bundle.getSerializable(KEY_COURSE_INFO);
            if (courseInfo != null) {
                addViewByContent();
            }
        }
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        tvTitle = (TextView) rootView.findViewById(R.id.id_tv_title);
        tvCreateTime = (TextView) rootView.findViewById(R.id.id_tv_create_time);
        tvReadCount = (TextView) rootView.findViewById(R.id.id_tv_read_count);

        imgSupport = (ImageView) rootView.findViewById(R.id.id_img_support);
        imgStep = (ImageView) rootView.findViewById(R.id.id_img_step);
        imgPrivate = (ImageView) rootView.findViewById(R.id.id_img_favorite);
        lvRelationRead = (ListView) rootView.findViewById(R.id.id_lv_relation);

        llRelation = (LinearLayout) rootView.findViewById(R.id.id_ll_relation);
        ll_content = (LinearLayout) rootView.findViewById(R.id.id_ll_class_content);//内容父布局
        ll_support_step = (LinearLayout) rootView.findViewById(R.id.id_ll_support_step);

        rlSupport = (RelativeLayout) rootView.findViewById(R.id.id_rl_support);//赞
        rlStep = (RelativeLayout) rootView.findViewById(R.id.id_rl_step);//踩

        tvSupportAddOne = (TextView) rootView.findViewById(R.id.id_tv_support_add_one);
        tvStepAddOne = (TextView) rootView.findViewById(R.id.id_tv_step_add_one);
        tvSupportCount = (TextView) rootView.findViewById(R.id.id_tv_support_count);
        tvStepCount = (TextView) rootView.findViewById(R.id.id_tv_step_count);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.support_add_one);
    }

    private void initEvent() {
        //赞
        rlSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断登录状态
                if (!MainApplication.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }

                int count = courseInfo.getData().getSupportCount();
                Object supportCount = tvSupportCount.getTag();
                if (tvStepCount.getTag() != null) {
                    Toast.makeText(MainApplication.UIContext, "已踩", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (supportCount != null) {
                    int temp = (int) supportCount;
                    if (temp == count + 1) {
                        Toast.makeText(MainApplication.UIContext, "已赞", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //点赞加1效果
                supportOrStep(tvSupportCount, tvSupportAddOne, imgSupport, count, true);

                CommonOperator.getInstance().supportOrStep(MainApplication.userInfo.getUserId(), courseInfo.getData().getCourseId(), SupportTypeEnum.Course, 1, new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Log.i("supportOrStep", "赞成功");
                        }
                    }
                });
            }
        });

        //踩
        rlStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断登录状态
                if (!MainApplication.isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }

                //点踩加1效果
                int count = courseInfo.getData().getOppositionCount();
                Object stepCount = tvSupportCount.getTag();
                if (tvSupportCount.getTag() != null) {
                    Toast.makeText(MainApplication.UIContext, "已赞", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (stepCount != null) {
                    int temp = (int) stepCount;
                    if (temp == count + 1) {
                        Toast.makeText(MainApplication.UIContext, "已踩", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                supportOrStep(tvStepCount, tvStepAddOne, imgStep, count, false);

                CommonOperator.getInstance().supportOrStep(MainApplication.userInfo.getUserId(), courseInfo.getData().getCourseId(), SupportTypeEnum.Course, 1, new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Log.i("supportOrStep", "赞成功");
                        }
                    }
                });
            }
        });

        //相关阅读
        lvRelationRead.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void bindData() {
        this.tvTitle.setText(courseInfo.getData().getTitle());
        this.tvCreateTime.setText(courseInfo.getData().getCreateTime());
        this.tvReadCount.setText("阅读(" + courseInfo.getData().getViewCount() + ")");
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

    private void addViewByContent() {
        if (courseInfo.getData() != null) {
            String json = courseInfo.getData().getContent();
            Log.i("content", json);
            Gson gson = new Gson();
            ContentInfo[] lstContentInfo = gson.fromJson(json, ContentInfo[].class);
            if (lstContentInfo == null) {
                return;
            }
            for (int i = 0; i < lstContentInfo.length; i++) {
                ContentInfo contentInfo = lstContentInfo[i];
                if (contentInfo.getType().equals("TXT")) {
                    if (contentInfo.getData().getContentType().equals("p")) {
                        addTextView(contentInfo.getData().getContent());
                    } else if (contentInfo.getData().getContentType().equals("")) {
                        addTextView(contentInfo.getData().getContent());
                    }
                }
                if (contentInfo.getType().equals("IMG")) {
                    addImageView(contentInfo.getData().getUrl(), contentInfo.getData().getWidth(), contentInfo.getData().getHeight());
                }
                if (contentInfo.getType().equals("VOIDE")) {
                    //addImageView(contentInfo.getData().getUrl(), contentInfo.getData().getWidth(), contentInfo.getData().getHeight());
                }
            }
            ll_support_step.setVisibility(View.VISIBLE);
        }
    }

    //添加视图控件
    private void addImageView(String url, int width, int height) {
        ImageView imageView = new ImageView(getActivity());
        ImageLoaderUtils.getImageLoader(getActivity()).displayImage(url, imageView, ImageLoaderUtils.getNormalDisplayOptions(R.drawable.img_topline_default));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //lp.topMargin = 10;
        //imageView.setLayoutParams(lp);
        setImageViewMathParent(getActivity(), ll_content, imageView, width, height);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:大图浏览
            }
        });
    }

    public void setImageViewMathParent(Activity context, LinearLayout view,
                                       ImageView image, int width, int height) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        float scalew = (float) displayMetrics.widthPixels
                / (float) width;
        image.setScaleType(ImageView.ScaleType.MATRIX);
        Matrix matrix = new Matrix();
        image.setAdjustViewBounds(true);
        if (displayMetrics.widthPixels < width) {
            matrix.postScale(scalew, scalew);
        } else {
            matrix.postScale(1 / scalew, 1 / scalew);
        }
        image.setMaxWidth(displayMetrics.widthPixels);
        float ss = displayMetrics.heightPixels > height ? displayMetrics.heightPixels
                : height;
        image.setMaxWidth((int) ss);
        view.addView(image);
    }

    //添加文本控件
    private void addTextView(String words) {
        TextView textView = new TextView(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 10, 20, 10);
        textView.setLayoutParams(lp);
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(R.color.tv_channel_item));

        int k = 0;
        StringTokenizer tokenizer = new StringTokenizer(words, "$");

        String[] str = new String[tokenizer.countTokens()];

        while (tokenizer.hasMoreTokens()) {
            str[k] = new String();
            str[k] = tokenizer.nextToken().trim();
            k++;
        }

        for (int i = 0; i < str.length; i++) {
            Log.i(String.valueOf(i), str[i]);
        }

        //将link占位符替换成链接文字
        for (int j = 0; j < str.length; j++) {
            for (int i = 0; i < courseInfo.getData().getUrls().size(); i++) {
                Urls url = courseInfo.getData().getUrls().get(i);
                String newShowContent = url.getNewShowContent().trim();
                if (str[j].equals(newShowContent)) {
                    str[j] = url.getDescription();
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length; i++) {
            sb.append(str[i]);
        }

        //得到最终包含有链接文字的字符串
        String finalWord = sb.toString().trim();

        //创建一个 SpannableString对象
        SpannableString sp = new SpannableString(finalWord);
        if (!finalWord.equals("")) {
            List<Urls> lstUrl = courseInfo.getData().getUrls();
            for (int i = 0; i < lstUrl.size(); i++) {
                String key = lstUrl.get(i).getDescription();
                if (!key.equals("")) {
                    if (finalWord.contains(key)) {
                        //设置超链接
                        sp.setSpan(new URLSpan(lstUrl.get(i).getDir()), finalWord.indexOf(key), finalWord.indexOf(key) + key.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            textView.setText(sp);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        ll_content.addView(textView);
    }
}
