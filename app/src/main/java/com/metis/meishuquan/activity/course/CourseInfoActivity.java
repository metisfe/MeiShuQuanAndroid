package com.metis.meishuquan.activity.course;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.CourseOperator;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.course.CourseInfo;
import com.metis.meishuquan.model.topline.ContentInfo;
import com.metis.meishuquan.model.topline.Urls;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.List;
import java.util.StringTokenizer;

public class CourseInfoActivity extends FragmentActivity {

    private TextView tvAuthor, tvTitle, tvCreateTime, tvReadCount, tvContent;
    private SmartImageView imgAuthor, imgContent;
    private RelativeLayout rlSupport, rlStep, rlWriteComment, rlCommentList, rlPrivate, rlShare;
    private LinearLayout ll_content, llRelation;
    private CourseInfo courseInfo;

    private int courseId = -1;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        mContext = this;
        if (getIntent().getExtras() != null) {
            this.courseId = getIntent().getIntExtra("courseId", -1);
        }
        initView();
        getInfo();
    }

    private void initView() {
        tvAuthor = (TextView) this.findViewById(R.id.id_tv_author);
        tvTitle = (TextView) this.findViewById(R.id.id_tv_title);
        tvCreateTime = (TextView) this.findViewById(R.id.id_tv_create_time);
        tvReadCount = (TextView) this.findViewById(R.id.id_tv_read_count);
        tvContent = (TextView) this.findViewById(R.id.id_tv_content);

        imgAuthor = (SmartImageView) this.findViewById(R.id.id_img_dynamic);

        llRelation = (LinearLayout) this.findViewById(R.id.id_ll_relation);
        ll_content = (LinearLayout) this.findViewById(R.id.id_ll_class_content);//内容父布局
//        rlSupport = (RelativeLayout) this.findViewById(R.id.id_rl_relation);
//        rlStep = (RelativeLayout) this.findViewById(R.id.id_rl_relation);
        rlWriteComment = (RelativeLayout) this.findViewById(R.id.id_rl_writecomment);
        rlCommentList = (RelativeLayout) this.findViewById(R.id.id_rl_commentlist);
        rlPrivate = (RelativeLayout) this.findViewById(R.id.id_rl_private);
        rlShare = (RelativeLayout) this.findViewById(R.id.id_rl_share);
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
                        bindData();
                        addViewByContent();
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
        }
    }

    //添加视图控件
    private void addImageView(String url, int width, int height) {
        SmartImageView imageView = new SmartImageView(this);
        imageView.setImageUrl(url.trim());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width * 2, height * 2);
        lp.topMargin = 10;
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(lp);

        ll_content.addView(imageView);
    }

    //添加文本控件
    private void addTextView(String words) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
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
            for (int i = 0; i < courseInfo.getData().getUrlss().size(); i++) {
                Urls url = courseInfo.getData().getUrlss().get(i);
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
            List<Urls> lstUrl = courseInfo.getData().getUrlss();
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
