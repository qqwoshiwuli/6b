package com.fivefivelike.mybaselibrary.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.FileUtil;
import com.fivefivelike.mybaselibrary.utils.GlobleContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by 郭青枫 on 2016/11/29.
 * 图片加载统一入口
 */

public class GlideUtils {
    public static final String BASE_URL = "http://forotc.com/avatar/23.png";

    public static String getBaseUrl() {
        return BASE_URL + "/avatar/";
    }

    public static RequestOptions getNoCacheRO() {
        return new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
    }

    //http://forotc.com/avatar/23.png
    public static void loadImage(String url, ImageView icon) {
        if (icon == null) {
            return;
        }
        int errorId;
        if (icon instanceof CircleImageView) {
            errorId = R.drawable.touxiang;
        } else {
            errorId = R.drawable.loaderor;
        }
        loadImage(url, icon, new RequestOptions()
                .centerCrop()
                .error(errorId), null);
    }

    public static void loadImage(String url, ImageView icon, RequestOptions requestOptions) {
        if (icon == null) {
            return;
        }
        int errorId;
        if (icon instanceof CircleImageView) {
            errorId = R.drawable.touxiang;
        } else {
            errorId = R.drawable.loaderor;
        }
        if (TextUtils.isEmpty(requestOptions.getErrorId() + "")) {
            requestOptions.error(errorId);
        }
        loadImage(url, icon, requestOptions, null);
    }


    public static void loadImage(String url, ImageView icon
            , RequestOptions requestOptions,
                                 RequestListener<Drawable> requestListener) {
        if (!TextUtils.isEmpty(url) && !url.startsWith("http") && !new File(url).exists()) {
            url = "http://" + "/avatar/" + url;
        }
        if (TextUtils.isEmpty(url)) {
            url = BASE_URL;
        }
        if (BASE_URL.equals(url)) {
            icon.setImageResource(requestOptions.getErrorId());
            return;
        }
        Glide.with(GlobleContext.getInstance().getApplicationContext())
                .load(url).
                apply(requestOptions
                )
                .listener(requestListener)
                .into(icon);
    }


    public static File saveBitmap(Context context, String name, Bitmap bitmap) {
        // 创建一个位于SD卡上的文件
        File file = new File(FileUtil.getIamgePath(context),
                name);
        if (file.exists()) {
            return file;
        }
        FileOutputStream out = null;
        try {
            // 打开指定文件输出流
            out = new FileOutputStream(file);
            // 将位图输出到指定文件
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    out);
            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
