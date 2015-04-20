package com.metis.meishuquan.activity.info;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.meishuquan.R;
import com.metis.meishuquan.fragment.commons.ImagePreviewFragment;
import com.uk.co.senab.photoview.PhotoView;

public class ImagePreviewActivity extends BaseActivity {

    private static final String KEY_IMAGE_URL_ARRAY = "image_url_array",
                                KEY_THUMB_URL_ARRAY = "thumb_url_array";

    private ImagePreviewFragment mPreviewFragment = null;

    private String[] mUrlArray = {
        "http://imgsrc.baidu.com/forum/w%3D580/sign=b78cddba77094b36db921be593cc7c00/ca1d8701a18b87d6a15e887a060828381f30fdbc.jpg",
            "http://wenwen.soso.com/p/20090715/20090715093745-279331914.jpg",
            "http://www.fm971.net/MUSIC/UploadFiles_7504/201106/2011062401223033.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3121197861,99117914&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=2397531836,2879697423&fm=21&gp=0.jpg",
            "http://a.hiphotos.baidu.com/baike/s%3D220/sign=68a715622df5e0feea188e036c6134e5/bba1cd11728b471021aae7b7c3cec3fdfd0323c7.jpg"
    }, mThumbArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        mPreviewFragment = (ImagePreviewFragment)getSupportFragmentManager()
                .findFragmentById(R.id.image_preview_fragment);

        //mUrlArray = getIntent().getStringArrayExtra(KEY_IMAGE_URL_ARRAY);
        mThumbArray = getIntent().getStringArrayExtra(KEY_THUMB_URL_ARRAY);

        mPreviewFragment.setUrlArray(mUrlArray);
    }

}
