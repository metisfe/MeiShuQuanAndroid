package com.metis.meishuquan.view.common;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.activity.info.ImagePreviewActivity;
import com.metis.meishuquan.model.circle.CircleImage;
import com.metis.meishuquan.util.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 9宫格图片，用于圈子图片显示
 * <p/>
 * Created by wangjin on 15/6/2.
 */
public class NinePictruesView extends LinearLayout {

    private static final float density = MainApplication.Resources.getDisplayMetrics().density;
    private List<CircleImage> lstCircleImage;
    private ArrayList<String> lstImgUrls;
    private int width = (int) density * 75;
    private int height = (int) density * 75;

    private List<ImageView> lstImageViews = new ArrayList<ImageView>();

    public NinePictruesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLstCircleImage(List<CircleImage> lstCircleImage) {
        if (lstCircleImage == null) {
            return;
        }
        if (lstImgUrls == null) {
            lstImgUrls = new ArrayList<String>();
        }
        this.lstCircleImage = lstCircleImage;
        this.lstImgUrls.clear();
        for (int i = 0; i < lstCircleImage.size(); i++) {
            lstImgUrls.add(lstCircleImage.get(i).OriginalImage);
        }
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeAllViews();
    }

    private void init() {
        this.setOrientation(VERTICAL);

        if (lstCircleImage != null && lstCircleImage.size() > 0) {
            int count = lstCircleImage.size();
            if (count == 1) {
                int width = (int) (lstCircleImage.get(0).ThumbnailsWidth * density);
                int height = (int) (lstCircleImage.get(0).ThumbnailsHeight * density);
                ImageView imageView = getImageView(lstCircleImage.get(0).Thumbnails, width, height, 0, 0);
                this.addView(imageView);
                lstImageViews.add(imageView);
            } else if (count > 1 && count <= 3) {
                LinearLayout childContainer = getChildContainer();
                for (int i = 0; i < count; i++) {
                    ImageView imageView = getImageView(lstCircleImage.get(i).Thumbnails, width, height, (int) density * 2, i);
                    childContainer.addView(imageView);
                    lstImageViews.add(imageView);
                }
                this.addView(childContainer);
            } else if (count == 4) {
                LinearLayout childContainer1 = getChildContainer();
                LinearLayout childContainer2 = getChildContainer();
                for (int i = 0; i < count; i++) {
                    if (i < 2) {
                        ImageView imageView = getImageView(lstCircleImage.get(i).Thumbnails, width, height, (int) density * 2, i);
                        childContainer1.addView(imageView);
                        lstImageViews.add(imageView);
                    } else {
                        ImageView imageView = getImageView(lstCircleImage.get(i).Thumbnails, width, height, (int) density * 2, i);
                        childContainer2.addView(imageView);
                        lstImageViews.add(imageView);
                    }
                }
                this.addView(childContainer1);
                this.addView(childContainer2);
            } else if (count > 4 && count <= 6) {
                LinearLayout childContainer1 = getChildContainer();
                LinearLayout childContainer2 = getChildContainer();
                for (int i = 0; i < count; i++) {
                    if (i < 3) {
                        ImageView imageView = getImageView(lstCircleImage.get(i).Thumbnails, width, height, (int) density * 2, i);
                        childContainer1.addView(imageView);
                        lstImageViews.add(imageView);
                    } else {
                        ImageView imageView = getImageView(lstCircleImage.get(i).Thumbnails, width, height, (int) density * 2, i);
                        childContainer2.addView(imageView);
                        lstImageViews.add(imageView);
                    }
                }
                this.addView(childContainer1);
                this.addView(childContainer2);
            } else if (count > 6) {
                LinearLayout childContainer1 = getChildContainer();
                LinearLayout childContainer2 = getChildContainer();
                LinearLayout childContainer3 = getChildContainer();
                for (int i = 0; i < count; i++) {
                    if (i < 3) {
                        ImageView imageView = getImageView(lstCircleImage.get(i).Thumbnails, width, height, (int) density * 2, i);
                        childContainer1.addView(imageView);
                        lstImageViews.add(imageView);
                    } else if (i >= 3 && i < 6) {
                        ImageView imageView = getImageView(lstCircleImage.get(i).Thumbnails, width, height, (int) density * 2, i);
                        childContainer2.addView(imageView);
                        lstImageViews.add(imageView);
                    } else if (i >= 6) {
                        ImageView imageView = getImageView(lstCircleImage.get(i).Thumbnails, width, height, (int) density * 2, i);
                        childContainer3.addView(imageView);
                        lstImageViews.add(imageView);
                    }
                }
                this.addView(childContainer1);
                this.addView(childContainer2);
                this.addView(childContainer3);
            }
        }
    }

    private ImageView getImageView(String url, int width, int height, int margin, final int index) {
        LayoutParams imgParam = new LayoutParams(width, height);
        if (margin != 0) {
            imgParam.setMargins(margin, margin, margin, margin);
        }
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(imgParam);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoaderUtils.getImageLoader(getContext()).displayImage(url, imageView);

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImagePreviewActivity.class);
                intent.putStringArrayListExtra(ImagePreviewActivity.KEY_IMAGE_URL_ARRAY, lstImgUrls);
                intent.putExtra(ImagePreviewActivity.KEY_START_INDEX, index);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        return imageView;
    }

    private LinearLayout getChildContainer() {
        LayoutParams childContainerParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout childContainer = new LinearLayout(getContext());
        childContainer.setLayoutParams(childContainerParams);
        childContainer.setOrientation(HORIZONTAL);
        return childContainer;
    }
}
