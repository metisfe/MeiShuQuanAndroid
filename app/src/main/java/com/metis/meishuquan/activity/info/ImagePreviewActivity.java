package com.metis.meishuquan.activity.info;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.metis.meishuquan.R;
import com.uk.co.senab.photoview.PhotoView;

public class ImagePreviewActivity extends BaseActivity {

    private PhotoView mPreview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        mPreview = (PhotoView)this.findViewById(R.id.image_preview_image);

        Drawable drawable = getDrawable(R.drawable.ic_launcher);
        mPreview.setImageDrawable(drawable);
    }

}
