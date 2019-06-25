package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.widget.ImageView;
import com.youth.banner.loader.ImageLoader;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;

/**
 * Created by 郭青枫 on 2018/2/12 0012.
 */

public class GlideBannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        if (path instanceof String) {
            GlideUtils.loadImage((String) path, imageView,GlideUtils.getNoCacheRO());
        } else if (path instanceof Integer) {
            imageView.setImageResource((int) path);
        }
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

}