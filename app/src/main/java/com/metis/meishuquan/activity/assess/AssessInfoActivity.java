package com.metis.meishuquan.activity.assess;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.loopj.android.image.SmartImageView;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.login.LoginActivity;
import com.metis.meishuquan.model.BLL.AssessOperator;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.assess.Assess;
import com.metis.meishuquan.model.assess.AssessComment;
import com.metis.meishuquan.model.assess.AssessSupportAndComment;
import com.metis.meishuquan.model.assess.PushCommentParam;
import com.metis.meishuquan.model.commons.SimpleUser;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.CommentTypeEnum;
import com.metis.meishuquan.model.enums.SupportStepTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.assess.CommentTypePicView;
import com.metis.meishuquan.view.assess.CommentTypeTextView;
import com.metis.meishuquan.view.assess.CommentTypeVoiceView;
import com.metis.meishuquan.view.course.FlowLayout;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.util.ArrayList;
import java.util.List;

public class AssessInfoActivity extends FragmentActivity {

    private String TAG = "getAssessSupportAndComment";
    private Button btnBack;

    private TextView tvName, tvGrade, tvType, tvPublishTime, tvAssessState, tvContent, tvSupportCount, tvCommentCount, tvAddOne;
    private SmartImageView imgPortrait, imgContent;
    private ImageView imgCommentMode, imgMore, imgTriangle;
    private Button btnRecord, btnSend;
    private EditText etInput;
    private LinearLayout llSupport, llComment;
    private FlowLayout flImgs;
    private ImageView imgSupport;
    private ListView listView;
    private View headerView;

    private boolean isVoiceMode;
    private Assess assess;
    private AssessComment selectedAssessComment;
    private AssessSupportAndComment assessSupportAndComment;
    private AssessInfoAdapter adapter;
    private List<ImageView> lstImageViews;
    private List<AssessComment> lstAssessComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_info);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.assess = (Assess) bundle.getSerializable("assess");
            this.assessSupportAndComment = (AssessSupportAndComment) bundle.getSerializable("assessSupportAndComment");
            this.lstAssessComment = assessSupportAndComment.getAssessCommentList();
        }
        initView();
        addHeaderView();
        initHeaderView();
        bindData(assess);
        initHeaderEvent();
        initEvent();

        adapter = new AssessInfoAdapter(AssessInfoActivity.this, assessSupportAndComment);
        listView.setAdapter(adapter);
    }

    private void addHeaderView() {
        headerView = View.inflate(this, R.layout.view_assess_info_header, null);
        listView.addHeaderView(headerView, null, false);
    }

    private void initHeaderView() {
        this.tvName = (TextView) headerView.findViewById(R.id.id_username);
        this.tvGrade = (TextView) headerView.findViewById(R.id.id_tv_grade);
        this.tvType = (TextView) headerView.findViewById(R.id.id_tv_content_type);
        this.tvPublishTime = (TextView) headerView.findViewById(R.id.id_createtime);
        this.tvAssessState = (TextView) headerView.findViewById(R.id.id_tv_comment_state);
        this.tvContent = (TextView) headerView.findViewById(R.id.id_tv_content);
        this.tvSupportCount = (TextView) headerView.findViewById(R.id.id_tv_support_count);
        this.tvCommentCount = (TextView) headerView.findViewById(R.id.id_tv_comment_count);
        this.imgPortrait = (SmartImageView) headerView.findViewById(R.id.id_img_portrait);
        this.imgContent = (SmartImageView) headerView.findViewById(R.id.id_img_content);
        this.imgTriangle = (ImageView) headerView.findViewById(R.id.id_img_triangle);
        this.tvAddOne = (TextView) headerView.findViewById(R.id.id_tv_add_one);

        this.llSupport = (LinearLayout) headerView.findViewById(R.id.id_ll_support);
        this.llComment = (LinearLayout) headerView.findViewById(R.id.id_ll_comment_count);
        this.flImgs = (FlowLayout) headerView.findViewById(R.id.id_flow_user_portrait);
        if (assessSupportAndComment.getSupportUserList().size() == 0 && assessSupportAndComment.getAssessCommentList().size() == 0) {
            this.imgTriangle.setVisibility(View.GONE);
        } else {
            this.imgTriangle.setVisibility(View.VISIBLE);
        }
        this.imgSupport = (ImageView) headerView.findViewById(R.id.id_img_assess_support);
    }

    private void initHeaderEvent() {
        //赞
        this.llSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                support();
            }
        });

        //评论
        this.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initView() {
        this.btnBack = (Button) this.findViewById(R.id.id_btn_assess_info_back);
        this.listView = (ListView) this.findViewById(R.id.id_list_assess_info_comment);
        //评论
        this.imgCommentMode = (ImageView) this.findViewById(R.id.id_img_comment_mode);//语音或文字切换
        this.imgMore = (ImageView) this.findViewById(R.id.id_btn_more);//更多（图片）
        this.btnRecord = (Button) this.findViewById(R.id.id_btn_voice_record);//录音
        this.etInput = (EditText) this.findViewById(R.id.id_et_input_comment);//输入框
        this.btnSend = (Button) this.findViewById(R.id.id_btn_send);//发送文字
    }

    private void initEvent() {
        //返回
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (lstAssessComment.size() == 0) {
                    return;
                }
                selectedAssessComment = lstAssessComment.get(i - 1);
                if (selectedAssessComment == null) {
                    return;
                }
                if (selectedAssessComment.getUser().getUserId() != -1) {
                    etInput.requestFocus();
                    etInput.setFocusableInTouchMode(true);
                    Utils.showInputMethod(AssessInfoActivity.this, etInput);
                    etInput.setHint("回复" + selectedAssessComment.getUser().getName() + ":");
                }
            }
        });

        this.listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        //隐藏输入法，清空回复对象
                        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED) {
                            Utils.hideInputMethod(AssessInfoActivity.this, etInput);
                        }
                        selectedAssessComment = null;
                        clearEditText();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });

        this.imgCommentMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isVoiceMode) {//语音模式 显示语音按钮 隐藏输入框
                    btnRecord.setVisibility(View.VISIBLE);
                    etInput.setVisibility(View.GONE);
                    etInput.clearFocus();
                    etInput.setFocusableInTouchMode(false);
                    isVoiceMode = true;
                } else {//文字模式 隐藏语音按钮，显示输入框
                    btnRecord.setVisibility(View.GONE);
                    etInput.setVisibility(View.VISIBLE);
                    etInput.requestFocus();
                    etInput.setFocusableInTouchMode(true);
                    isVoiceMode = false;
                }
            }
        });

        this.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        this.btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        this.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideInputMethod(AssessInfoActivity.this, etInput);//隐藏输入框
                PushCommentParam param = new PushCommentParam();
                param.setUserId(MainApplication.userInfo.getUserId());
                param.setAssessId(assess.getId());
                param.setCommentType(CommentTypeEnum.Text.getVal());
                param.setContent(etInput.getText().toString());
                if (selectedAssessComment != null) {
                    param.setReplyUserId(selectedAssessComment.getUser().getUserId());
                }

                AssessOperator.getInstance().pushComment(param, new ApiOperationCallback<ReturnInfo<AssessComment>>() {
                    @Override
                    public void onCompleted(ReturnInfo<AssessComment> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Gson gson = new Gson();
                            String json = gson.toJson(result);
                            //刷新列表
                            ReturnInfo returnInfo = gson.fromJson(json, new TypeToken<ReturnInfo<AssessComment>>() {
                            }.getType());
                            AssessComment assessComment = (AssessComment) returnInfo.getData();
                            if (assessComment != null) {
                                assessSupportAndComment.getAssessCommentList().add(assessComment);
                                adapter.notifyDataSetChanged();
                                btnSend.setVisibility(View.GONE);
                                clearEditText();
                            }
                            Toast.makeText(AssessInfoActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        this.etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String content = etInput.getText().toString();
                if (content.length() > 0) {
                    imgMore.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                } else {
                    imgMore.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.GONE);
                }
            }
        });
    }

    private void clearEditText() {
        etInput.setText("");
        etInput.setHint("我来说两句");
    }

    private void support() {
        if (!MainApplication.isLogin()) {
            startActivity(new Intent(AssessInfoActivity.this, LoginActivity.class));
            return;
        }
        //赞+1动画
        int count = assess.getSupportCount();
        Object supportCount = tvSupportCount.getTag();
        if (supportCount != null) {
            int temp = (int) supportCount;
            if (temp == count + 1) {
                Toast.makeText(MainApplication.UIContext, "已赞", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Animation animation = AnimationUtils.loadAnimation(AssessInfoActivity.this, R.anim.support_add_one);
        tvAddOne.setVisibility(View.VISIBLE);
        tvAddOne.startAnimation(animation);
        int addCount = count + 1;
        tvSupportCount.setText("赞(" + addCount + ")");
        tvSupportCount.setTag(count + 1);
        tvSupportCount.setTextColor(Color.RED);
        imgSupport.setImageDrawable(getResources().getDrawable(R.drawable.icon_support));
        new Handler().postDelayed(new Runnable() {
            public void run() {
                tvAddOne.setVisibility(View.GONE);
            }
        }, 1000);

        //后台执行赞操作
        CommonOperator.getInstance().supportOrStep(MainApplication.userInfo.getUserId(), assess.getId(), SupportStepTypeEnum.Assess, 1, new ApiOperationCallback<ReturnInfo<String>>() {
            @Override
            public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                if (result != null && result.getInfo().equals(String.valueOf(0))) {
                    Log.i("supportOrStep", "赞成功");
                } else if (result != null) {
                    Log.i("Error:", result.getMessage());
                } else {
                    exception.printStackTrace();
                }
            }
        });
    }

    private void bindData(Assess assess) {
        if (assess == null) {
            Toast.makeText(this, "加载失败,请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        //姓名
        this.tvName.setText(assess.getUser().getName().isEmpty() ? "" : assess.getUser().getName().trim());
        //头像
        this.imgPortrait.setImageUrl(assess.getUser().getAvatar(), R.drawable.default_user_dynamic);
        //年级
        this.tvGrade.setText(assess.getUser().getGrade().isEmpty() ? "" : assess.getUser().getGrade());
        //点评状态
        if (assess.getCommentCount() > 0) {
            this.tvAssessState.setText("已点评");
        } else {
            this.tvAssessState.setText("未点评");
        }
        //类型
        this.tvType.setText(assess.getAssessChannel().getChannelName().isEmpty() ? "" : assess.getAssessChannel().getChannelName());
        //创建时间
        this.tvPublishTime.setText(assess.getCreateTime());
        //正文内容
        this.tvContent.setText(assess.getDesc());
        //图片
        this.imgContent.setMinimumHeight(assess.getThumbnails().getHeigth() * 2);
        this.imgContent.setMinimumWidth(assess.getThumbnails().getWidth() * 2);
        this.imgContent.setImageUrl(assess.getThumbnails().getUrl());
        //赞数量和评论数量
        this.tvSupportCount.setText("赞(" + assess.getSupportCount() + ")");
        this.tvCommentCount.setText("评论(" + assess.getCommentCount() + ")");

        //添加赞头像
        lstImageViews = new ArrayList<ImageView>();
        for (int i = 0; i < assessSupportAndComment.getSupportUserList().size(); i++) {
            final SimpleUser supportUser = assessSupportAndComment.getSupportUserList().get(i);
            ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.layout_imageview_user_portrait, null).findViewById(R.id.id_img_user_portrait);
            imageView.setMaxWidth(40);
            imageView.setMaxHeight(40);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoaderUtils.getImageLoader(this).displayImage(assessSupportAndComment.getSupportUserList().get(i).getAvatar(), imageView, ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.user_portrait_height), R.drawable.default_user_dynamic));

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AssessInfoActivity.this, "进入" + supportUser.getName() + "的个人主页", Toast.LENGTH_SHORT).show();
                }
            });

            this.flImgs.addView(imageView);
            this.lstImageViews.add(imageView);
        }

    }

    private class AssessInfoAdapter extends BaseAdapter {

        private static final int TEXT = 0;
        private static final int VOICE = 1;
        private static final int PIC = 2;
        private Context mContext;
        private List<AssessComment> lstAssessComment;

        public AssessInfoAdapter(Context mContext, AssessSupportAndComment assessSupportAndComment) {
            this.mContext = mContext;
            if (assessSupportAndComment != null) {
                lstAssessComment = assessSupportAndComment.getAssessCommentList();
            }
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            if (lstAssessComment.get(position).getCommentType() == CommentTypeEnum.Text.getVal()) {
                return TEXT;
            } else if (lstAssessComment.get(position).getCommentType() == CommentTypeEnum.Image.getVal()) {
                return PIC;
            } else if (lstAssessComment.get(position).getCommentType() == CommentTypeEnum.Voice.getVal()) {
                return VOICE;
            }
            return 0;
        }

        @Override
        public int getCount() {
            return lstAssessComment.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        class TextViewHolder {
            CommentTypeTextView textView;
        }

        class VoiceViewHolder {
            CommentTypeVoiceView voiceView;
        }

        class PicViewHolder {
            CommentTypePicView picView;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            int current_type = getItemViewType(i);

            TextViewHolder textViewHolder = null;
            VoiceViewHolder voiceViewHolder = null;
            PicViewHolder picViewHolder = null;
            if (convertView == null) {
                if (current_type == TEXT) {
                    textViewHolder = new TextViewHolder();
                    textViewHolder.textView = new CommentTypeTextView(mContext);
                    textViewHolder.textView.setAssessComment(lstAssessComment.get(i));
                    convertView = textViewHolder.textView;
                    convertView.setTag(textViewHolder);
                } else if (current_type == PIC) {
                    picViewHolder = new PicViewHolder();
                    picViewHolder.picView = new CommentTypePicView(mContext);
                    picViewHolder.picView.setAssessComment(lstAssessComment.get(i));
                    convertView = picViewHolder.picView;
                    convertView.setTag(picViewHolder);
                } else if (current_type == VOICE) {
                    voiceViewHolder = new VoiceViewHolder();
                    voiceViewHolder.voiceView = new CommentTypeVoiceView(mContext);
                    voiceViewHolder.voiceView.setAssessComment(lstAssessComment.get(i));
                    convertView = voiceViewHolder.voiceView;
                    convertView.setTag(voiceViewHolder);
                }
            } else {
                if (current_type == TEXT) {
                    textViewHolder = (TextViewHolder) convertView.getTag();
                    textViewHolder.textView.setAssessComment(lstAssessComment.get(i));
                } else if (current_type == PIC) {
                    picViewHolder = (PicViewHolder) convertView.getTag();
                    picViewHolder.picView.setAssessComment(lstAssessComment.get(i));
                } else if (current_type == VOICE) {
                    voiceViewHolder = (VoiceViewHolder) convertView.getTag();
                    voiceViewHolder.voiceView.setAssessComment(lstAssessComment.get(i));
                }
            }
            return convertView;

        }
    }
}
