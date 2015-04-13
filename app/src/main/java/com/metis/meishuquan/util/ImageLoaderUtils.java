package com.metis.meishuquan.util;

import android.content.Context;

import com.metis.meishuquan.R;
import com.metis.meishuquan.ui.display.SquareRoundDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by WJ on 2015/4/13.
 */
public class ImageLoaderUtils {
    public static ImageLoader getImageLoader (Context context) {
        ImageLoader loader = ImageLoader.getInstance();
        if (!loader.isInited()) {
            loader.init(ImageLoaderConfiguration.createDefault(context));
        }
        return loader;
    }

    public static DisplayImageOptions getRoundDisplayOptions (int size) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new SquareRoundDisplayer(size))
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .showImageOnLoading(R.drawable.ic_launcher)
                .build();
        return options;
    }
}
