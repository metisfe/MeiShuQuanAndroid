package com.metis.meishuquan.fragment.Assess;

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

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.AssessOperator;
import com.metis.meishuquan.model.assess.Bimp;
import com.metis.meishuquan.model.assess.Channel;
import com.metis.meishuquan.model.contract.ReturnInfo;
import com.metis.meishuquan.util.Utils;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Fragment:发布点评
 * <p/>
 * Created by wj on 15/4/1.
 */
public class AssessPublishFragment extends Fragment {
    private FragmentManager fm;
    private Button btnCancel, btnChooseChannel, btnInviteTeacher, btnAssessButton;
    private EditText etDesc;
    private ImageView addImg;
    private AssessOperator assessOperator;
    private ViewGroup rootView;

    private Channel selectedChannel;//类型

    private Bimp bimp;
    private static final int TAKE_PHOTO = 1;
    private static final int PICK_PICTURE = 2;
    private String path = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_assess_publish, null);
        initView(rootView);
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
        this.addImg = (ImageView) rootView.findViewById(R.id.id_add_image);
        this.bimp = new Bimp();
    }

    private void initEvent() {
        //发布
        this.btnAssessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取作品描述信息
                String desc = etDesc.getText().toString();

                //得到图片的字节数组
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bimp.bmp.get(0).compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imgByte = stream.toByteArray();

                //文件描述信息
                String define = imgByte.length + "," + 1 + "," + imgByte.length;

                //发布提问
                assessOperator = AssessOperator.getInstance();
                assessOperator.uploadAssess(0, desc, selectedChannel.getChannelId(), 0, 1, define, imgByte, new ApiOperationCallback<ReturnInfo<String>>() {
                    @Override
                    public void onCompleted(ReturnInfo<String> result, Exception exception, ServiceFilterResponse response) {
                        if (result != null && result.getInfo().equals(String.valueOf(0))) {
                            Utils.alertMessageDialog("提示", "发布成功");
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.remove(AssessPublishFragment.this);
                            ft.commit();
                        }
                    }
                });
            }
        });

        //选择图片
        this.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PopupWindows(MainApplication.UIContext, rootView);
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

    /**
     * 选择图片来源对话框
     */
    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.choose_img_source_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            super.update();

            Button btnCamera = (Button) view.findViewById(R.id.item_popupwindows_camera);
            Button btnPhoto = (Button) view.findViewById(R.id.item_popupwindows_Photo);
            Button btnCancel = (Button) view.findViewById(R.id.item_popupwindows_cancel);

            btnCamera.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            btnPhoto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    pickFromGallery();
                    dismiss();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
        File file = new File(dir, String.valueOf(System.currentTimeMillis())
                + ".jpg");
        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PHOTO);
    }

    public void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
        startActivityForResult(intent, PICK_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FragmentActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO://拍照
                    bimp.drr.clear();
                    bimp.drr.add(path);
                    try {
                        Bitmap bitmap = bimp.revitionImageSize(bimp.drr.get(0));
                        bimp.bmp.clear();
                        bimp.bmp.add(bitmap);
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
                        path = file.getPath();
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();

                        bimp.drr.clear();
                        bimp.drr.add(path);
                        Bitmap releasePic = bimp.revitionImageSize(bimp.drr.get(0));
                        bimp.bmp.clear();
                        bimp.bmp.add(releasePic);
                        addImg.setImageBitmap(releasePic);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
