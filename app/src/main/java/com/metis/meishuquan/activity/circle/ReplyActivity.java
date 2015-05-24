package com.metis.meishuquan.activity.circle;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.FriendsChooseActivity;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.model.BLL.ActiveOperator;
import com.metis.meishuquan.model.BLL.CircleOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CirclePushBlogParm;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.Utils;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

/**
 * 转发：包含头条，课程，活动
 * created by wangjin on 4/2/2015.
 */
public class ReplyActivity extends FragmentActivity {

    public static final String PARM = "parm";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String IMAGEURL = "imgUrl";
    public static final String INPUT_CONTENT = "input_content";

    private Button btnBack;
    private Button btnSend;
    private EditText etInput;
    private ImageView imgReplyPic;
    private ImageView imgCirclePic;
    private TextView tvReplyTitle;
    private TextView tvReplyContent;
    private TextView tvCircleTitle;
    private TextView tvCircleContent;

    private RelativeLayout rl_at;
    private RelativeLayout rl_emotion;
    private RelativeLayout rl_reply;
    private RelativeLayout rl_circle;

    private boolean isPressed;
    private CirclePushBlogParm parm;

    private String title = "";
    private String content = "";
    private String imgUrl = "";
    private String inputContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            /*   parm 中 RelayId和type必须赋值  */
            this.parm = (CirclePushBlogParm) bundle.getSerializable(PARM);
            this.title = bundle.getString(TITLE, "");
            this.content = bundle.getString(CONTENT, "");
            this.imgUrl = bundle.getString(IMAGEURL, "");
            this.inputContent = bundle.getString(INPUT_CONTENT, "");
        }

        initView();
        bindData();
        initEvent();

    }

    public void bindData() {
        if (parm.getType() == SupportTypeEnum.Circle.getVal()) {
            this.rl_circle.setVisibility(View.VISIBLE);
            this.rl_reply.setVisibility(View.GONE);
            this.tvCircleTitle.setText(title);
            this.tvCircleContent.setText(content);
            this.etInput.setText(inputContent);
            if (imgUrl != null && !imgUrl.isEmpty()) {
                ImageLoaderUtils.getImageLoader(this).displayImage(imgUrl, imgCirclePic);
            }
        } else {
            this.rl_circle.setVisibility(View.GONE);
            this.rl_reply.setVisibility(View.VISIBLE);
            this.tvReplyTitle.setText(title);
            this.tvReplyContent.setText(content);
            this.etInput.setText(inputContent);
            if (imgUrl != null && !imgUrl.isEmpty()) {
                ImageLoaderUtils.getImageLoader(this).displayImage(imgUrl, imgReplyPic);
            }
        }
    }

    private void initView() {
        this.btnBack = (Button) this.findViewById(R.id.id_btn_back);
        this.btnSend = (Button) this.findViewById(R.id.id_btn_send);
        this.etInput = (EditText) this.findViewById(R.id.id_et_input_reply);
        this.imgReplyPic = (ImageView) this.findViewById(R.id.id_img_pic);
        this.imgCirclePic = (ImageView) this.findViewById(R.id.id_img_circle_pic);
        this.tvReplyTitle = (TextView) this.findViewById(R.id.id_tv_title);
        this.tvReplyContent = (TextView) this.findViewById(R.id.id_tv_content);
        this.tvCircleTitle = (TextView) this.findViewById(R.id.id_tv_circle_title);
        this.tvCircleContent = (TextView) this.findViewById(R.id.id_tv_circle_content);

        this.rl_at = (RelativeLayout) this.findViewById(R.id.id_rl_emotion);
        this.rl_emotion = (RelativeLayout) this.findViewById(R.id.id_rl_emotion);

        this.rl_reply = (RelativeLayout) this.findViewById(R.id.id_rl_reply);
        this.rl_circle = (RelativeLayout) this.findViewById(R.id.id_rl_circle);

        if (this.parm.getType() == SupportTypeEnum.Activity.getVal()) {
            this.etInput.setText("我正在参加#超级美术生#海选，一定要帮我到美术圈APP集齐10个赞哦！这样我就有机会免费进全国最好画室中最贵的VIP班学习啦！");
        }
    }

    private void initEvent() {
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPressed) {
                    send(parm);
                    isPressed = true;
                }
            }
        });

        this.rl_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Intent it = new Intent(ReplyActivity.this, FriendsChooseActivity.class);
                startActivity(it);
            }
        });

        this.rl_emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void send(final CirclePushBlogParm parm) {
        if (!MainApplication.isLogin()) {
            startActivity(new Intent(ReplyActivity.this, LoginActivity.class));
        }
        parm.setContent(etInput.getText().toString());
        CircleOperator.getInstance().pushBlog(parm, new ApiOperationCallback<ReturnInfo<CCircleDetailModel>>() {
            @Override
            public void onCompleted(ReturnInfo<CCircleDetailModel> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && result.isSuccess()) {
                    if (parm.getType() == SupportTypeEnum.Activity.getVal()) {
                        ActiveOperator.getInstance().joinActivity(parm.getRelayId(), new UserInfoOperator.OnGetListener() {
                            @Override
                            public void onGet(boolean succeed, Object o) {
                                if (succeed) {
                                    Intent it = new Intent("join_succeed");
                                    LocalBroadcastManager.getInstance(ReplyActivity.this).sendBroadcast(it);
                                }
                            }
                        });
                    }
                    String json = new Gson().toJson(result);
                    Log.i("pushBlog", json);

                    Toast.makeText(MainApplication.UIContext, "转发成功", Toast.LENGTH_SHORT).show();

                    finish();
                } else if (exception != null) {
                    Log.i("pushBlog", "exception:cause:" + exception.getCause() + "  message:" + exception.getMessage());
                }
            }
        });
        Utils.hideInputMethod(MainApplication.UIContext, etInput);
    }
}
