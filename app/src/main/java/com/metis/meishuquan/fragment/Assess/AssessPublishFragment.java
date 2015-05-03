package com.metis.meishuquan.fragment.assess;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.main.AssessFragment;
import com.metis.meishuquan.model.BLL.AssessOperator;
import com.metis.meishuquan.model.assess.Bimp;
import com.metis.meishuquan.model.assess.Channel;
import com.metis.meishuquan.model.commons.Profile;
import com.metis.meishuquan.model.commons.Result;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.model.enums.FileUploadTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.util.Utils;
import com.metis.meishuquan.view.popup.ChoosePhotoPopupWindow;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Fragment:发布点评
 * <p/>
 * Created by wj on 15/4/1.
 */
public class AssessPublishFragment extends Fragment {
    private final int MAX_LENGTH = 100;
    private FragmentManager fm;
    private Button btnCancel, btnChooseChannel, btnInviteTeacher, btnAssessButton;
    private EditText etDesc;
    private TextView tvWordCount;
    private ImageView addImg;
    private AssessOperator assessOperator;
    private ViewGroup rootView;

    private Channel selectedChannel;//类型

    private static final int TAKE_PHOTO = 1;
    private static final int PICK_PICTURE = 2;
    private String photoPath = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_assess_publish, null);
        initView(rootView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            //设置父级返回的图片
            this.photoPath = bundle.getString("photoPath");
            addImg.setImageBitmap(getBitmapFormPath(photoPath));
        }

        initEvent();
        return rootView;
    }

    private void initView(ViewGroup rootView) {
        fm = getActivity().getSupportFragmentManager();
        this.btnCancel = (Button) rootView.findViewById(R.id.id_btn_cancel);
        this.btnChooseChannel = (Button) rootView.findViewById(R.id.id_btn_choose_type);
        this.btnInviteTeacher = (Button) rootView.findViewById(R.id.id_btn_request_teacher_assess);
        this.btnAssessButton = (Button) rootView.findViewById(R.id.id_btn_assess_comment);
        this.etDesc = (EditText) rootView.findViewById(R.id.id_edt_desc);
        this.tvWordCount = (TextView) rootView.findViewById(R.id.id_tv_word_count);
        this.addImg = (ImageView) rootView.findViewById(R.id.id_add_image);
    }

    private void initEvent() {

        this.etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String content = etDesc.getText().toString();
                if (content.length() < MAX_LENGTH) {
                    tvWordCount.setText(content.length() + "/"
                            + MAX_LENGTH);
                } else {
                    Toast.makeText(MainApplication.UIContext, "您输入的字符数过多!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //发布
        this.btnAssessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideInputMethod(MainApplication.UIContext, etDesc);
                if (selectedChannel == null || selectedChannel.getChannelId() == 0) {
                    Toast.makeText(MainApplication.UIContext, "请选择类型", Toast.LENGTH_SHORT).show();
                    return;
                }

                //获取作品描述信息
                final String desc = etDesc.getText().toString();

                //得到图片的字节数组
                byte[] imgByte = ImageLoaderUtils.BitmapToByteArray(getBitmapFormPath(photoPath));

                //文件描述信息
                String define = imgByte.length + "," + 1 + "," + imgByte.length;
                Log.i("define", define);

                //发布提问
                assessOperator = AssessOperator.getInstance();
                assessOperator.fileUpload(FileUploadTypeEnum.IMG, define, imgByte, new ServiceFilterResponseCallback() {
                    @Override
                    public void onResponse(ServiceFilterResponse response, Exception exception) {
                        //上传文件
                        if (!response.getContent().isEmpty()) {
                            String json = response.getContent();
                            Log.v("fileUpload", json);
                            String fileStr = "";
                            try {
                                JSONObject object = new JSONObject(json);
                                JSONArray array = object.getJSONArray("data");
                                fileStr = array.getString(0);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            assessOperator.uploadAssess(MainApplication.userInfo.getUserId(), desc, selectedChannel.getChannelId(), 0, fileStr, new ApiOperationCallback<ReturnInfo<String>>() {
                                @Override
                                public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                                    if (result != null && result.getInfo().equals(String.valueOf(0))) {
                                        Toast.makeText(MainApplication.UIContext, "发布成功", Toast.LENGTH_SHORT).show();
                                        FragmentTransaction ft = fm.beginTransaction();
                                        ft.replace(R.id.content_container, new AssessFragment());
                                        ft.commit();
                                    }
                                }
                            });
                        }

                    }
                });
            }
        });

        //选择图片
        this.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoosePhotoPopupWindow choosePhotoPopupWindow = new ChoosePhotoPopupWindow(MainApplication.UIContext, AssessPublishFragment.this, rootView);
                choosePhotoPopupWindow.getPath(new AssessFragment.OnPathChannedListner() {
                    @Override
                    public void setPath(String path) {
                        photoPath = path;
                    }
                });
            }
        });

        //取消
        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(AssessPublishFragment.this);
                ft.commit();
            }
        });

        //选择类型
        this.btnChooseChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishAssessChooseTypeFragment publishAssessChooseTypeFragment = new PublishAssessChooseTypeFragment();
                publishAssessChooseTypeFragment.setOnSeletedChannelListener(new PublishAssessChooseTypeFragment.OnSeletedChannelListener() {
                    @Override
                    public void setChannel(Channel channel) {
                        selectedChannel = channel;
                        btnChooseChannel.setText(channel.getChannelName());
                    }
                });
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.content_container, publishAssessChooseTypeFragment);
                ft.commit();
            }
        });

        //邀请老师
        this.btnInviteTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InviteTeacherFragment inviteTeacherFragment = new InviteTeacherFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.content_container, inviteTeacherFragment);
                ft.commit();
            }
        });
    }

    private Bitmap getBitmapFormPath(String path) {
        Bitmap bitmap = null;
        try {
            if (!path.isEmpty()) {
                Bimp.getInstance().drr.clear();
                Bimp.getInstance().drr.add(photoPath);
                bitmap = Bimp.getInstance().revitionImageSize(Bimp.getInstance().drr.get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FragmentActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO://拍照
                    Bimp.getInstance().drr.clear();
                    Bimp.getInstance().drr.add(photoPath);
                    try {
                        Bitmap bitmap = Bimp.getInstance().revitionImageSize(Bimp.getInstance().drr.get(0));
                        Bimp.getInstance().bmp.clear();
                        Bimp.getInstance().bmp.add(bitmap);
                        addImg.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case PICK_PICTURE://相册
                    Uri uri = data.getData();
                    try {
                        //根据URL得到Bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(MainApplication.UIContext.getContentResolver(), uri);
                        //将返回的图片存入指定路径，以便上传至服务器
                        File dir = new File(Environment.getExternalStorageDirectory()
                                + "/myimage/");
                        if (!dir.exists()) {
                            dir.mkdir();
                        } else {
                            File[] files = dir.listFiles();
                            for (int i = 0; i < files.length; i++) {
                                if (files[i].isFile()) {
                                    files[i].delete();
                                }
                            }
                        }
                        File file = new File(dir, String.valueOf(System.currentTimeMillis()) + ".jpg");
                        photoPath = file.getPath();
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();

                        Bimp.getInstance().drr.clear();
                        Bimp.getInstance().drr.add(photoPath);
                        Bitmap releasePic = Bimp.getInstance().revitionImageSize(Bimp.getInstance().drr.get(0));
                        Bimp.getInstance().bmp.clear();
                        Bimp.getInstance().bmp.add(releasePic);
                        addImg.setImageBitmap(releasePic);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
