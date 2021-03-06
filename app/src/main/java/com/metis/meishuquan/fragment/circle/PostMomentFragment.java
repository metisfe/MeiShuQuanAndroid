package com.metis.meishuquan.fragment.circle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.FriendsChooseActivity;
import com.metis.meishuquan.activity.assess.TestPicActivity;
import com.metis.meishuquan.model.BLL.CircleOperator;
import com.metis.meishuquan.model.BLL.CommonOperator;
import com.metis.meishuquan.model.assess.Bimp;
import com.metis.meishuquan.model.circle.CCircleDetailModel;
import com.metis.meishuquan.model.circle.CircleImage;
import com.metis.meishuquan.model.circle.CirclePushBlogParm;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.FileUploadTypeEnum;
import com.metis.meishuquan.model.enums.SupportTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.circle.CircleTitleBar;
import com.metis.meishuquan.view.circle.moment.comment.EmotionEditText;
import com.metis.meishuquan.view.circle.moment.comment.EmotionSelectView;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wudi on 4/2/2015.
 */
public class PostMomentFragment extends Fragment {

    private static final String TAG = PostMomentFragment.class.getSimpleName();

    private CircleTitleBar titleBar;
    private RelativeLayout rl_choose_pic;
    private RelativeLayout rl_at;
    private RelativeLayout rl_emotion;
    private EmotionEditText editText;

    private GridView mImageGrid = null;

    private List<Integer> lstUserId = null;//@好友

    private FragmentManager fm = null;
    private boolean isSend;

    private static final int ScreenHeight = MainApplication.getDisplayMetrics().heightPixels;
    private static final int EmotionSelectViewHeight = MainApplication.Resources.getDimensionPixelSize(R.dimen.emotion_input_height);
    private static final int EmotionSwitchContainerHeight = MainApplication.Resources.getDimensionPixelSize(R.dimen.switch_emotion_container_height);
    private int MarginTopForEmotionSwitch = ScreenHeight - EmotionSelectViewHeight - EmotionSwitchContainerHeight;
    private EmotionSelectView emotionSelectView;
    private ProgressDialog progressDialog;

    private List<String> mImagePaths = new ArrayList<String>();

    private ImageAdapter mAdapter = new ImageAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_circle_postmoment, container, false);
        initView(rootView);
        initTitleBar();
        initEvent();
        return rootView;
    }

    @Override
    public void onResume() {
        if (Bimp.getInstance().drr.size() > 0) {
            mImagePaths.clear();
            mImagePaths.addAll(Bimp.getInstance().drr);
            mAdapter.notifyDataSetChanged();
        }
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
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

        this.mImageGrid = (GridView) rootView.findViewById(R.id.postmoment_grid_pics);
        this.mImageGrid.setAdapter(mAdapter);

        this.fm = getActivity().getSupportFragmentManager();

    }

    private void initTitleBar() {
        titleBar.setText("发日志");
        titleBar.setRightButton("发送", 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSend) {
                    progressDialog = ProgressDialog.show(getActivity(), "", "发送中...");
                    if (!mImagePaths.isEmpty()) {
                        try {
                            CommonOperator.getInstance().fileUpload(FileUploadTypeEnum.IMG, mImagePaths, new ServiceFilterResponseCallback() {
                                @Override
                                public void onResponse(ServiceFilterResponse response, Exception exception) {
                                    progressDialog.cancel();
                                    String feedbackContent = response.getContent();
                                    if (TextUtils.isEmpty(feedbackContent)) {
                                        Toast.makeText(getActivity(), "上传图片失败", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Gson gson = new Gson();
                                    Log.v(TAG, "content=" + feedbackContent);
                                    Result<List<CircleImage>> imageListResult = null;
                                    try {
                                        imageListResult = gson.fromJson(feedbackContent, new TypeToken<Result<List<CircleImage>>>() {
                                        }.getType());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        return;
                                    }
                                    String content = editText.getText().toString();

                                    CirclePushBlogParm parm = new CirclePushBlogParm();
                                    parm.setContent(content);
                                    parm.setDevice(Build.MODEL);
                                    parm.setType(SupportTypeEnum.Circle.getVal());
                                    parm.setImages(imageListResult.getData());

                                    send(parm);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String content = editText.getText().toString();

                        CirclePushBlogParm parm = new CirclePushBlogParm();
                        parm.setContent(content);
                        parm.setDevice(Build.MODEL);
                        parm.setType(SupportTypeEnum.Circle.getVal());

                        send(parm);
                    }

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
        if (parm.getContent().toString().trim().length() == 0 && parm.getImages().size() == 0) {
            Toast.makeText(MainApplication.UIContext, "请输入日志内容，或选择图片", Toast.LENGTH_SHORT).show();
            isSend = false;
            return;
        }
        CircleOperator.getInstance().pushBlog(parm, new ApiOperationCallback<ReturnInfo<CCircleDetailModel>>() {
            @Override
            public void onCompleted(ReturnInfo<CCircleDetailModel> result, Exception exception, ServiceFilterResponse response) {
                progressDialog.cancel();
                if (result != null && result.isSuccess()) {
                    String json = new Gson().toJson(result);
                    Log.i("pushBlog", json);
                    Toast.makeText(MainApplication.UIContext, "发送成功", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                } else if (exception != null) {
                    progressDialog.dismiss();
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
        Bimp.getInstance().drr.clear();
    }

    private void initEvent() {
        //选择图片
        this.rl_choose_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "onClick rl_choose_pic", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), TestPicActivity.class));
            }
        });

        //@
        this.rl_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), FriendsChooseActivity.class);
                startActivity(it);
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

    private class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mImagePaths.size();
        }

        @Override
        public String getItem(int i) {
            return mImagePaths.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_img_item, null);
            }
            String url = mImagePaths.get(i);
            ImageView iv = (ImageView) view.findViewById(R.id.item_img);
            ImageLoaderUtils.getImageLoader(getActivity()).displayImage(
                    ImageDownloader.Scheme.FILE.wrap(url),
                    iv,
                    ImageLoaderUtils.getNormalDisplayOptions(R.drawable.ic_launcher));
            return view;
        }
    }

}
