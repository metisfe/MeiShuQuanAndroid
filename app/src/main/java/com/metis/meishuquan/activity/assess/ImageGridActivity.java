package com.metis.meishuquan.activity.assess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.metis.meishuquan.R;
import com.metis.meishuquan.model.assess.Bimp;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImageGridActivity extends Activity {
    public static final String EXTRA_IMAGE_LIST = "imagelist";

    List<ImageItem> dataList;
    GridView gridView;
    ImageGridAdapter adapter;
    AlbumHelper helper;
    Button btnComplete;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ImageGridActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        //得至相册中的所有照片
        dataList = (List<ImageItem>) getIntent().getSerializableExtra(
                EXTRA_IMAGE_LIST);

        initView();
        btnComplete = (Button) findViewById(R.id.id_btn_complete);
        btnComplete.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<String>();
                Collection<String> c = adapter.map.values();
                Iterator<String> it = c.iterator();
                for (; it.hasNext(); ) {
                    list.add(it.next());
                }

                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (Bimp.getInstance().drr.size() < 9) {
                            Bimp.getInstance().drr.add(list.get(i));
                        }
                    }
                }
                finish();
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bimp.getInstance().drr.clear();
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
                mHandler);
        gridView.setAdapter(adapter);
        adapter.setTextCallback(new ImageGridAdapter.TextCallback() {
            public void onListen(int count) {
                btnComplete.setText("完成" + "(" + count + ")");
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                adapter.notifyDataSetChanged();
            }
        });

    }
}
