package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.BLL.AssessOperator;
import com.metis.meishuquan.model.BLL.UserInfoOperator;
import com.metis.meishuquan.model.enums.FileUploadTypeEnum;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.shared.TitleView;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

public class AdvanceActivity extends BaseActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 400;

    private TitleView mTitleView = null;

    private EditText mAdvanceEt, mContactEt;

    private ImageView mImage = null;

    private String mPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance);

        mAdvanceEt = (EditText)findViewById(R.id.advance_input);
        mContactEt = (EditText)findViewById(R.id.advance_contact);
        mImage = (ImageView)findViewById(R.id.advance_image);

        mTitleView = (TitleView)findViewById(R.id.title);
        mTitleView.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleView.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackMsg = mAdvanceEt.getText().toString();
                if (TextUtils.isEmpty(feedbackMsg)) {
                    Toast.makeText(AdvanceActivity.this, R.string.advance_not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                UserInfoOperator.getInstance().feedback(feedbackMsg, mPath);
                finish();
                Toast.makeText(AdvanceActivity.this, R.string.advance_thanks, Toast.LENGTH_SHORT).show();
            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    mPath = ImageLoaderUtils.getFilePathFromUri(AdvanceActivity.this, data.getData());
                    ImageLoaderUtils.getImageLoader(AdvanceActivity.this).displayImage(ImageDownloader.Scheme.FILE.wrap(mPath), mImage);
                }
                break;
        }
    }

    private void getImage () {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

}
