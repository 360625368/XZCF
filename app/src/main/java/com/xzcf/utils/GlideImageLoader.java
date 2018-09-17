package com.xzcf.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    private static final long serialVersionUID = 2371771205122846234L;

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (context != null) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}
