package com.metis.meishuquan.activity.info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;
import com.metis.meishuquan.activity.act.ActDetailActivity;
import com.metis.meishuquan.fragment.commons.ListDialogFragment;
import com.metis.meishuquan.fragment.commons.QRFragment;
import com.metis.meishuquan.model.commons.User;
import com.metis.meishuquan.util.ImageLoaderUtils;
import com.metis.meishuquan.view.shared.TitleView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NameCardQrActivity extends BaseActivity {

    public static final String TAG = NameCardQrActivity.class.getSimpleName();

    public static final String KEY_DATA_STR = null;

    private QRFragment mQrFragment = null;

    private ImageView mProfileIv = null;
    private TextView mNameTv, mGradeTv, mIntroduceTv;

    private ListDialogFragment mListfragment = null;
    private List<ActionDelegate> mActionList = new ArrayList<ActionDelegate>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        mActionList.add(new ActionDelegate(0, getString(R.string.name_card_save)));
        mActionList.add(new ActionDelegate(1, getString(R.string.name_card_share)));

        //final String string = getIntent().getStringExtra(KEY_DATA_STR);

        JsonObject json = new JsonObject();
        User user = MainApplication.userInfo;
        json.addProperty(User.KEY_USER_ID, user.getUserId());
        json.addProperty(User.KEY_NICK_NAME, user.getName());
        json.addProperty(User.KEY_USERAVATAR, user.getUserAvatar());
        mQrFragment = (QRFragment)getSupportFragmentManager().findFragmentById(R.id.qr_fragment);
        Log.v(TAG, "onCreate " + user.getPhoneNum());
        mQrFragment.showQrCodeWith(/*json.toString()*/user.getPhoneNum());
        mQrFragment.setOnImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListfragment = ListDialogFragment.getInstance();
                mListfragment.setAdapter(new ActionAdapter());
                mListfragment.show(getSupportFragmentManager(), "abc");
            }
        });

        mProfileIv = (ImageView)findViewById(R.id.qr_profile);
        mNameTv = (TextView)findViewById(R.id.qr_name);
        mGradeTv = (TextView)findViewById(R.id.qr_grade);
        mIntroduceTv = (TextView)findViewById(R.id.qr_self_introduce);

        ImageLoaderUtils.getImageLoader(this).displayImage(
                user.getUserAvatar(),
                mProfileIv,
                ImageLoaderUtils.getRoundDisplayOptions(getResources().getDimensionPixelSize(R.dimen.my_info_profile_size)));
        mNameTv.setText(user.getName());
        mGradeTv.setText(user.getGrade());
        mIntroduceTv.setText(user.getSelfIntroduce());
    }

    @Override
    public String getTitleCenter() {
        return getString(R.string.my_info_name_card);
    }

    @Override
    public void onTitleRightPressed() {
        //startActivity(new Intent (QrActivity.this, ImagePreviewActivity.class));
        //startActivity(new Intent(NameCardQrActivity.this, QrScanActivity.class));
        //startActivity(new Intent (this, ActDetailActivity.class));
    }

    private class ActionAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mActionList.size();
        }

        @Override
        public ActionDelegate getItem(int position) {
            return mActionList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(NameCardQrActivity.this).inflate(R.layout.layout_list_dialog_item, null);
            TextView tv = (TextView)convertView.findViewById(R.id.list_dialog_item);
            final ActionDelegate delegate = getItem(position);
            tv.setText(delegate.getItemText());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListfragment.dismiss();
                    Bitmap bmp = mQrFragment.getQrBitmap();
                    String path = ImageLoaderUtils.saveBitmapAtMediaDir("qr.jpg", bmp);
                    File qrFile = null;
                    if (path != null) {
                       qrFile = new File (path);
                    } else {
                        Toast.makeText(NameCardQrActivity.this, "error to handle this", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    switch (delegate.getId()) {
                        case 0:
                            Toast.makeText(NameCardQrActivity.this, path, Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            qrFile.deleteOnExit();
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(qrFile));
                            shareIntent.setType("image/*");
                            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.name_card_send_to)));
                            break;
                    }
                }
            });
            return tv;
        }
    }

    private class ActionDelegate {

        private String mItemText = null;
        private int mId;

        public ActionDelegate (int id, String name) {
            mId = id;
            mItemText = name;
        }

        public String getItemText() {
            return mItemText;
        }

        public void setItemText(String mItemText) {
            this.mItemText = mItemText;
        }

        public int getId() {
            return mId;
        }

        public void setId(int mId) {
            this.mId = mId;
        }
    }

}
