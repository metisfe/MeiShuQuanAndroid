package com.metis.meishuquan.activity.info;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.commons.ImagePreviewFragment;
import com.metis.meishuquan.fragment.commons.ImagePreviewSingleFragment;
import com.metis.meishuquan.fragment.commons.ListDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagePreviewActivity extends BaseActivity {

    private static final String TAG = ImagePreviewActivity.class.getSimpleName();

    public static final String KEY_IMAGE_URL_ARRAY = "image_url_array",
            KEY_THUMB_URL_ARRAY = "thumb_url_array",
            KEY_START_INDEX = "start_index";

    private ImagePreviewFragment mPreviewFragment = null;

//    private String[] mUrlArray = {
//        "http://imgsrc.baidu.com/forum/w%3D580/sign=b78cddba77094b36db921be593cc7c00/ca1d8701a18b87d6a15e887a060828381f30fdbc.jpg",
//            "http://wenwen.soso.com/p/20090715/20090715093745-279331914.jpg",
//            "http://www.fm971.net/MUSIC/UploadFiles_7504/201106/2011062401223033.jpg",
//            "http://img5.imgtn.bdimg.com/it/u=3121197861,99117914&fm=21&gp=0.jpg",
//            "http://img1.imgtn.bdimg.com/it/u=2397531836,2879697423&fm=21&gp=0.jpg",
//            "http://a.hiphotos.baidu.com/baike/s%3D220/sign=68a715622df5e0feea188e036c6134e5/bba1cd11728b471021aae7b7c3cec3fdfd0323c7.jpg"
//    }, mThumbArray = null;

    private List<String> lstImageUrls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        hideTitleBar();
        mPreviewFragment = (ImagePreviewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.image_preview_fragment);

        lstImageUrls = getIntent().getStringArrayListExtra(KEY_IMAGE_URL_ARRAY);
        int index = getIntent().getIntExtra(KEY_START_INDEX, 0);
//        mUrlArray = getIntent().getStringArrayExtra(KEY_IMAGE_URL_ARRAY);
//        mThumbArray = getIntent().getStringArrayExtra(KEY_THUMB_URL_ARRAY);

        mPreviewFragment.setLstImageUrl(lstImageUrls);
        mPreviewFragment.setCurrentIndex(index);
        mPreviewFragment.setOnPhotoClickListener(new ImagePreviewSingleFragment.OnPhotoClickListener() {
            @Override
            public void onPhotoClick(View v, String url) {
                finish();
                overridePendingTransition(0, R.anim.activity_zoomout);
            }

            @Override
            public void onLongClick(View v, final String url) {
                ListDialogFragment.getInstance().setAdapter(new ListDialogFragment.SimpleAdapter(ImagePreviewActivity.this, new String[]{getString(R.string.save_pics)}));
                ListDialogFragment.getInstance().show(getSupportFragmentManager(), TAG);
                ListDialogFragment.getInstance().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ListDialogFragment.getInstance().dismiss();
                        String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                        HttpUtils utils = new HttpUtils();
                        utils.download(url, str + File.separator + System.currentTimeMillis() + ".jpg", new RequestCallBack<File>() {
                            @Override
                            public void onSuccess(ResponseInfo<File> responseInfo) {
                                Toast.makeText(ImagePreviewActivity.this, getString(R.string.save_at, responseInfo.result.getAbsolutePath()), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                Toast.makeText(ImagePreviewActivity.this, getString(R.string.save_failed), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

}
