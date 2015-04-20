package com.metis.meishuquan.view.popup;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.main.AssessFragment;

import java.io.File;

/**
 * Created by wangjin on 15/4/20.
 */
public class ChoosePhotoPopupWindow extends PopupWindow {

    private Fragment mFragment;
    private Context mContext;

    private static final int TAKE_PHOTO = 1;
    private static final int PICK_PICTURE = 2;
    private AssessFragment.OnPathChannedListner listner;

    public void getPath(AssessFragment.OnPathChannedListner listner) {
        this.listner = listner;
    }

    public ChoosePhotoPopupWindow(Context mContext, Fragment fragment, View parent) {
        this.mContext = mContext;
        this.mFragment = fragment;

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
        String path = file.getPath();
        listner.setPath(path);
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        mFragment.startActivityForResult(openCameraIntent, TAKE_PHOTO);
    }

    public void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
        mFragment.startActivityForResult(intent, PICK_PICTURE);
    }
}
