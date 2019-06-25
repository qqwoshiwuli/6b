package com.fivefivelike.mybaselibrary.utils;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.CookieManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.circledialog.CircleDialogHelper;
import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.adapter.entity.ShareItemEntity;
import com.fivefivelike.mybaselibrary.base.BigImageviewActivity;
import com.fivefivelike.mybaselibrary.view.AddPicAdapter;
import com.fivefivelike.mybaselibrary.view.GridSpacingItemDecoration;
import com.fivefivelike.mybaselibrary.view.dialog.ShareDialog;
import com.fivefivelike.mybaselibrary.view.dialog.ShareImgDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.durban.Controller;
import com.yanzhenjie.durban.Durban;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.SettingService;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


/**
 * Created by 郭青枫 on 2017/8/5.
 * <p>
 * ui 处理统一工具
 */

public class UiHeplUtils {
    public static final String SINA_PACKAGE_NAME = "com.sina.weibo";
    public static final String QQ_PACKAGE_NAME = "com.tencent.mobileqq";
    public static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";
    public static final String SINA_ACT_NAME = "com.sina.weibo.composerinde.ComposerDispatchActivity";
    public static final String QQ_ACT_NAME = "com.tencent.mobileqq.activity.JumpActivity";
    public static final String WECHAT_ACT_NAME = "com.tencent.mm.ui.tools.ShareImgUI";
    public static final String WECHAT_MONMENT_ACT_NAME = "com.tencent.mm.ui.tools.ShareToTimeLineUI";

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    public static float getPXfromDP(float value, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 根据设计稿计算实际宽高
     *
     * @param context
     * @param paddingRes 总间距 px
     * @param viewNum    每行数量
     * @param relWith    设计稿宽度px
     * @param relHei     设计稿高度px
     * @return
     */
    public static int[] cacularWidAndHei(Context context, @DimenRes int paddingRes, int viewNum, int relWith, int relHei) {
        int[] size = new int[2];
        int width = (int) CommonUtils.getDimensionPixelSize(relWith);
        int hight = (int) CommonUtils.getDimensionPixelSize(relHei);
        int paddingValue = context.getResources().getDimensionPixelSize(paddingRes);
        int screenWidth = getScreenW(context, false);
        int viewWidth = (screenWidth - paddingValue) / viewNum;
        int viewHeight = viewWidth * hight / width;
        size[0] = viewWidth;
        size[1] = viewHeight;
        return size;
    }

    public static int[] cacularWidAndHei(Context context, @DimenRes int widthRes, @DimenRes int paddingRes, int viewNum, int relWith, int relHei) {
        int[] size = new int[2];
        int width = (int) CommonUtils.getDimensionPixelSize(relWith);
        int hight = (int) CommonUtils.getDimensionPixelSize(relHei);
        int paddingValue = context.getResources().getDimensionPixelSize(paddingRes);
        int screenWidth = getScreenW(context, false) - context.getResources().getDimensionPixelSize(widthRes);
        int viewWidth = (screenWidth - paddingValue) / viewNum;
        int viewHeight = viewWidth * hight / width;
        size[0] = viewWidth;
        size[1] = viewHeight;
        return size;
    }

    public static void setCacularWidAndHei(int[] ints, View view) {
        if (ints != null) {
            if (view != null) {
                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) view.getLayoutParams();
                layoutParams.width = ints[0];
                layoutParams.height = ints[1];
                view.setLayoutParams(layoutParams);
            }
        }
    }

    /**
     * 获取屏幕像素尺寸
     *
     * @return : true:高 false:宽
     * @author yff 2013-10-31下午1:08:22
     */
    public static int getScreenW(Context context, boolean isHeight) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        return isHeight ? metrics.heightPixels : metrics.widthPixels;
    }

    /**
     * 根据文字长度计算占多少中文字符宽度
     */
    public static float stringToLenght(String content) {
        float lenght = 0;
        if (!TextUtils.isEmpty(content)) {
            for (int i = 0; i < content.length(); i++) {

                String txt = String.valueOf(content.charAt(i));

                Pattern p = Pattern.compile("[0-9]*");
                Matcher m = p.matcher(txt);
                if (m.matches()) {
                    lenght = lenght + 0.5f;
                }
                p = Pattern.compile("[a-zA-Z]");
                m = p.matcher(txt);
                if (m.matches()) {
                    lenght = lenght + 0.8f;
                }
                p = Pattern.compile("[\u4e00-\u9fa5]");
                m = p.matcher(txt);
                if (m.matches()) {
                    lenght = lenght + 1;
                }

            }
        }
        return lenght;
    }

    /**
     * 修改手机号中间四个为*
     */
    public static String hidePhone(String phone) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(phone.substring(0, 3));
        stringBuffer.append(" **** ");
        stringBuffer.append(phone.substring(phone.length() - 4, phone.length()));
        return stringBuffer.toString();
    }

    /**
     * 性别转换
     *
     * @param num
     * @return
     */
    public static String sex(String num) {
        if ("1".equals(num)) {
            return "男";
        }
        if ("2".equals(num)) {
            return "女";
        }
        return "";
    }

    /**
     * 版本号比较
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (TextUtils.isEmpty(version1) || TextUtils.isEmpty(version2)) {
            return 0;
        }
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        Log.d("HomePageActivity", "version1Array==" + version1Array.length);
        Log.d("HomePageActivity", "version2Array==" + version2Array.length);
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        Log.d("HomePageActivity", "verTag2=2222=" + version1Array[index]);
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    /**
     * 判断手机格式是否正确
     *
     * @param mobiles
     * @return 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
    public static boolean isMobileNO(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 获取RecyclerView 滑动高度
     */
    public int getScollYDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    /**
     * 一键设置webview
     *
     * @param webView
     */
    public static void webviewRegister(WebView webView) {
        //允许访问文件
        WebSettings webSettings = webView.getSettings();
        //
        webSettings.setDomStorageEnabled(true);
        //支持缩放，默认为true。
        webSettings.setSupportZoom(false);
        //调整图片至适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //设置默认编码
        webSettings.setDefaultTextEncodingName("utf-8");
        ////设置自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //允许访问文件
        webSettings.setAllowFileAccess(true);
        //开启javascript
        webSettings.setJavaScriptEnabled(true);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //获取触摸焦点
        webView.requestFocusFromTouch();
        //http/https混合加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //设置跨域cookie读取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                }
                return true;
            }
        });
    }

    /**
     * 倒计时
     *
     * @param textView
     * @param count
     * @return
     */
    public static Disposable getCode(final TextView textView, final long count) {
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return count - aLong; // 由于是倒计时，需要将倒计时的数字反过来
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        textView.setEnabled(false);
                    }
                }).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        textView.setText(aLong + CommonUtils.getString(R.string.str_resend));
                        textView.setGravity(Gravity.CENTER);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        textView.setEnabled(true);
                        textView.setText(CommonUtils.getString(R.string.str_resend));
                        textView.setGravity(Gravity.CENTER);
                    }
                }, new io.reactivex.functions.Action() {
                    @Override
                    public void run() throws Exception {
                        textView.setEnabled(true);
                        textView.setText(CommonUtils.getString(R.string.str_resend));
                        textView.setGravity(Gravity.CENTER);
                    }
                });
    }

    /**
     * 获取验证码
     */
    public static void getCode(final CompositeDisposable compositeDisposable, final TextView textView, final long count) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return count - aLong; // 由于是倒计时，需要将倒计时的数字反过来
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        textView.setEnabled(false);
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        textView.setText(aLong + CommonUtils.getString(R.string.str_sec) + CommonUtils.getString(R.string.str_after) + CommonUtils.getString(R.string.str_send_again));
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        textView.setEnabled(true);
                        textView.setText(CommonUtils.getString(R.string.str_get_code));
                    }
                });
    }


    //设置图库样式
    public static Widget getDefaultAlbumWight(String title) {
        return Widget.newDarkBuilder(GlobleContext.getInstance().getApplicationContext())
                .title(title) // Title.
                .statusBarColor(CommonUtils.getColor(R.color.toolbar_bg)) // StatusBar color.
                .toolBarColor(CommonUtils.getColor(R.color.toolbar_bg)) // Toolbar color.
                .navigationBarColor(Color.WHITE) // Virtual NavigationBar color of Android5.0+.
                .mediaItemCheckSelector(CommonUtils.getColor(R.color.color_blue),
                        CommonUtils.getColor(R.color.color_blue_dark)) // Image or video selection box.
                .bucketItemCheckSelector(CommonUtils.getColor(R.color.color_blue),
                        CommonUtils.getColor(R.color.color_blue_dark)) // Select the folder selection box.
                .buttonStyle( // Used to configure the style of button when the image/video is not found.
                        Widget.ButtonStyle.newDarkBuilder(GlobleContext.getInstance().getApplicationContext()) // With Widget's Builder model.
                                .setButtonSelector(Color.WHITE, Color.WHITE) // Button selector.
                                .build()
                ).build();
    }

    /**
     * 牌照path转 AlbumFile
     */
    public static AlbumFile stringToAlbumFile(String result) {
        File file = new File(result);
        String name = file.getName();
        AlbumFile albumFile = new AlbumFile();
        albumFile.setPath(result);
        albumFile.setName(name);
        String title = name;
        if (name.contains("."))
            title = name.split("\\.")[0];
        albumFile.setTitle(title);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(result));
        albumFile.setMimeType(mimeType);
        long nowTime = System.currentTimeMillis();
        albumFile.setAddDate(nowTime);
        albumFile.setModifyDate(nowTime);
        albumFile.setSize(file.length());
        albumFile.setThumbPath(result);
        albumFile.setMediaType(AlbumFile.TYPE_IMAGE);
        albumFile.setChecked(true);

        return albumFile;
    }

    public static List<AlbumFile> stringsToAlbumFiles(List<String> results) {
        List<AlbumFile> albumFileList = new ArrayList<>();
        for (String result : results) {
            File file = new File(result);
            String name = file.getName();
            AlbumFile albumFile = new AlbumFile();
            albumFile.setPath(result);
            albumFile.setName(name);
            String title = name;
            if (name.contains("."))
                title = name.split("\\.")[0];
            albumFile.setTitle(title);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(result));
            albumFile.setMimeType(mimeType);
            long nowTime = System.currentTimeMillis();
            albumFile.setAddDate(nowTime);
            albumFile.setModifyDate(nowTime);
            albumFile.setSize(file.length());
            albumFile.setThumbPath(result);
            albumFile.setMediaType(AlbumFile.TYPE_IMAGE);
            albumFile.setChecked(true);
            albumFileList.add(albumFile);
        }
        return albumFileList;
    }

    /**
     * path转File
     *
     * @param results
     * @return
     */
    public static List<File> stringsToFiles(List<String> results) {
        List<File> fileList = new ArrayList<>();
        for (String result : results) {
            File file = new File(result);
            fileList.add(file);
        }
        return fileList;
    }

    /**
     * html 编码
     *
     * @param str
     * @return
     */
    public static String fmtString(String str) {
        String notice = "";
        try {
            notice = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException ex) {

        }
        return notice;
    }


    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @return
     */
    public static boolean isListNull(Collection collection) {
        if (collection == null || collection.size() == 0)
            return true;
        else
            return false;
    }

    public static Map<String, List> getValueListMap(List list, String... paramsName) {
        Map<String, List> map = new HashMap<>();
        if (list == null || paramsName == null)
            return map;
        for (String item : paramsName) {
            map.put(item, new ArrayList<>());
        }
        for (Object t : list) {
            // 得到类对象
            Class userCla = (Class) t.getClass();
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置属性是可以访问的
                try {
                    for (String item : paramsName) {
                        if (f.getName().equals(item)) {
                            map.get(item).add(f.get(t));
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }


    /**
     * 吧String 集合转成用逗号隔开
     *
     * @param list
     */
    public static String pinjieString(List<String> list) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0, size = list.size(); i < size; i++) {
            if (i == size - 1) {
                buffer.append(list.get(i));
            } else {
                buffer.append(list.get(i) + ",");
            }
        }
        return buffer.toString();
    }

    /**
     * 将集合转换成map
     *
     * @param list
     * @param key
     * @return
     */
    public static Map<String, Object> listToFileMap(List list, String key) {
        if (list != null && list.size() > 0) {
            Map<String, Object> fileMap = new HashMap<>();
            List<String> strList = new ArrayList<>();
            for (Object item : list) {
                if (new File(item.toString()).exists()) {
                    strList.add(item.toString());
                }
            }
            for (int i = 0, size = strList.size(); i < size; i++) {
                fileMap.put(key + "[" + i + "]", new File(strList.get(i)));
            }
            return fileMap;
        }
        return null;
    }


    /**
     * 判断提交必填参数是否为空
     *
     * @param content
     * @return
     */
    public static boolean judgeRequestContentIsNull(String content, String toastString) {
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show(toastString);
            return true;
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        String regex = "-[0-9]+(.[0-9]+)?|[0-9]+(.[0-9]+)?";
        if (str == null || !str.matches(regex)) {
            return false;
        }
        return true;
    }

    /**
     * 打开网页
     */
    public static void startWeb(Context context, String url) {
        String new_url = url.replace("\\", "");
        Uri uri = Uri.parse(new_url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("url", new_url);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveList = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        Log.i("MainActivity", new_url + "resolveList size:" + resolveList.size());
        if (resolveList.size() > 0) {
            String title = "选择浏览器打开";
            Intent intentChooser = Intent.createChooser(intent, title);
            context.startActivity(intentChooser);
        } else {
            ToastUtils.showShort("没有浏览器可以打开此网页!");
        }
    }

    /**
     * 数字 前面加0
     */
    public static String numIntToString(int num, int digits) {
        String str = String.format("%0" + digits + "d", num);
        return str;
    }

    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + " B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + " KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + " MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + " GB";
        }
    }

    /**
     * gly 添加
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取手机的密度
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    public static void getPhoto(final FragmentActivity fragmentActivity, final com.yanzhenjie.album.Action<String> cameraLinsener, final com.yanzhenjie.album.Action<ArrayList<AlbumFile>> photoLinsener
            , final int selectNum) {
        getPhoto(fragmentActivity, cameraLinsener, photoLinsener, null, selectNum);
    }

    public static void getPhoto(final FragmentActivity fragmentActivity,
                                final com.yanzhenjie.album.Action<String> cameraLinsener,
                                final com.yanzhenjie.album.Action<ArrayList<AlbumFile>> photoLinsener,
                                final View.OnClickListener onClickListener, final int selectNum) {
        AndPermission.with(fragmentActivity)
                .runtime()
                .permission(Permission.CAMERA, Permission.CAMERA)
                .rationale(new Rationale<List<String>>() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        // 如果用户继续：
                        executor.execute();

                        // 如果用户中断：
                        //executor.cancel();
                    }
                })
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showPhotoDialog(fragmentActivity, cameraLinsener, photoLinsener, onClickListener, selectNum);
                    }

                }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> permissions) {
                // TODO what to do
                onDeniedAndPermission(permissions);
            }
        })
                .start();
    }

    private static void camera(FragmentActivity fragmentActivity, com.yanzhenjie.album.Action<String> a) {
        Album.camera(fragmentActivity) // 相机功能。
                .image() // 拍照。
                .requestCode(0x123)
                .onResult(a)
                .start();
    }

    private static void photo(FragmentActivity fragmentActivity, com.yanzhenjie.album.Action<ArrayList<AlbumFile>> a, int selectNum) {
        if (selectNum == 1) {
            Album.image(fragmentActivity) // 选择图片。
                    .singleChoice()
                    .requestCode(0x123)
                    .camera(true)
                    .columnCount(3)
                    .onResult(a)
                    .start();
        } else {
            Album.image(fragmentActivity) // 选择图片。
                    .multipleChoice()
                    .requestCode(0x123)
                    .camera(true)
                    .columnCount(3)
                    .selectCount(selectNum)
                    .onResult(a)
                    .start();
        }
    }

    private static void showPhotoDialog(final FragmentActivity fragmentActivity,
                                        final com.yanzhenjie.album.Action<String> cameraLinsener,
                                        final com.yanzhenjie.album.Action<ArrayList<AlbumFile>> photoLinsener,
                                        final View.OnClickListener onClickListener,
                                        final int selectNum) {
        String[] item = CommonUtils.getStringArray(R.array.sa_select_pic);
        CircleDialogHelper.initDefaultItemDialog(fragmentActivity, item, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    camera(fragmentActivity, cameraLinsener);
                } else {
                    photo(fragmentActivity, photoLinsener, selectNum);
                }
            }
        }).setNegative(CommonUtils.getString(R.string.str_cancel), onClickListener)
                .setCanceledOnTouchOutside(false)
                .show();
    }

    public static void reducePhoto(final Context context, List<String> paths, OnCompressListener listener) {
        Luban.with(context)
                .load(paths)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setTargetDir(SDCardUtils.getSDCardPaths().get(0) + "/AndroidSamples")  // 设置压缩后文件存储位置
                .setCompressListener(listener).launch();    //启动压缩
    }


    /**
     * 修剪图片
     */
    public static final int CROP_CODE_1 = 291;
    /**
     * 图片路径
     */
    private String mFilepath = SDCardUtils.getSDCardPaths().get(0) + "/AndroidSamples";

    public static void cropPhoto(Activity activity, String path, int width, int height) {
        Durban.with(activity)
                // 裁剪界面的标题。
                .title(CommonUtils.getString(R.string.str_durban_title))
                .statusBarColor(CommonUtils.getColor(R.color.mark_color))
                .toolBarColor(CommonUtils.getColor(R.color.mark_color))
                .navigationBarColor(CommonUtils.getColor(R.color.mark_color))
                // 图片路径list或者数组。
                .inputImagePaths(path)
                // 图片输出文件夹路径。
                .outputDirectory(SDCardUtils.getSDCardPaths().get(0) + "/AndroidSamples")
                // 裁剪图片输出的最大宽高。
                // 裁剪时的宽高比。
                .aspectRatio(width, height)
                // 图片压缩格式：JPEG、PNG。
                .compressFormat(Durban.COMPRESS_JPEG)
                // 图片压缩质量，请参考：Bitmap#compress(Bitmap.CompressFormat, int, OutputStream)
                .compressQuality(80)
                // 裁剪时的手势支持：ROTATE, SCALE, ALL, NONE.
                .gesture(Durban.GESTURE_ALL)
                .controller(
                        Controller.newBuilder()
                                .enable(false) // 是否开启控制面板。
                                .rotation(true) // 是否有旋转按钮。
                                .rotationTitle(true) // 旋转控制按钮上面的标题。
                                .scale(true) // 是否有缩放按钮。
                                .scaleTitle(true) // 缩放控制按钮上面的标题。
                                .build()) // 创建控制面板配置。
                .requestCode(0x123)
                .start();
    }

    public static void galleryPhoto(final FragmentActivity fragmentActivity,
                                    final com.yanzhenjie.album.Action<ArrayList<String>> photoLinsener,
                                    final com.yanzhenjie.album.Action<String> cancleLinsener,
                                    final Boolean check,
                                    final List<String> paths,
                                    final String title) {
        // 浏览一般的String路径：
        Album.gallery(fragmentActivity)
                .widget(Widget.newDarkBuilder(fragmentActivity).title(title).build())
                .requestCode(200) // 请求码，会在listener中返回。
                .checkedList((ArrayList<String>) paths) // 要浏览的图片列表：ArrayList<String>。
                .navigationAlpha(50) // Android5.0+的虚拟导航栏的透明度。
                .checkable(check) // 是否有浏览时的选择功能。
                .onResult(photoLinsener)
                .onCancel(cancleLinsener)
                .start(); // 千万不要忘记调用start()方法。
    }

    /**
     * @param list                存路径的集合
     * @param addPicAdapter       适配器
     * @param activity
     * @param recyclerView
     * @param cloumnCount         每行数量
     * @param cloumnAllPaddingRes 总行间距
     * @param addPicRes           添加图标资源
     * @param bigImageSelect      进入大图是否有选择操作
     */
    public static void initChoosePicRv(final List<String> list,
                                       final AddPicAdapter addPicAdapter,
                                       final FragmentActivity activity,
                                       RecyclerView recyclerView,
                                       int cloumnCount,
                                       @DimenRes int cloumnAllPaddingRes,
                                       @DrawableRes int addPicRes,
                                       final boolean bigImageSelect,
                                       final int selectIconNum) {
        cloumnCount = cloumnCount == 0 ? 4 : cloumnCount;
        cloumnAllPaddingRes = cloumnAllPaddingRes == 0 ? R.dimen.trans_100px : cloumnAllPaddingRes;
        recyclerView.setLayoutManager(new GridLayoutManager(activity, cloumnCount) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.setNestedScrollingEnabled(false);
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(cloumnCount,
                    activity.getResources().getDimensionPixelSize(cloumnAllPaddingRes) / (cloumnCount + 1),
                    true));
        }
        recyclerView.setAdapter(addPicAdapter);
        addPicAdapter.setCloumnCount(cloumnCount);
        if (addPicRes != 0) {
            addPicAdapter.setAddPicRes(addPicRes);
            addPicAdapter.setShowAdd(true);
        } else {
            addPicAdapter.setShowAdd(false);
        }
        addPicAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                int itemViewType = addPicAdapter.getItemViewType(position);
                switch (itemViewType) {
                    case AddPicAdapter.TYPE_ADD://添加图片
                        getPhoto(activity, new com.yanzhenjie.album.Action<String>() {
                            @Override
                            public void onAction(int requestCode, @android.support.annotation.NonNull String result) {
                                if (!TextUtils.isEmpty(result)) {
                                    list.add(result);
                                    addPicAdapter.notifyDataSetChanged();
                                }
                            }
                        }, new com.yanzhenjie.album.Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(int requestCode, @android.support.annotation.NonNull ArrayList<AlbumFile> result) {
                                if (result != null) {
                                    for (AlbumFile item : result) {
                                        list.add(item.getPath());
                                    }
                                    addPicAdapter.notifyDataSetChanged();
                                }
                            }
                        }, selectIconNum - list.size());
                        break;
                    case AddPicAdapter.TYPE_PIC://显示大图
                        BigImageviewActivity.toBigImage(activity)
                                .checkedList(list)
                                .currentPosition(position)
                                .checkable(bigImageSelect)
                                .setmWidget(getDefaultAlbumWight(CommonUtils.getString(R.string.str_gallery)))
                                .onResult(new com.yanzhenjie.album.Action<List<String>>() {
                                    @Override
                                    public void onAction(int requestCode, @android.support.annotation.NonNull List<String> result) {
                                        list.clear();
                                        list.addAll(result);
                                        addPicAdapter.notifyDataSetChanged();
                                    }
                                }).start();
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }


    public static void showBigImg(FragmentActivity activity,
                                  List<String> list, int position) {
        BigImageviewActivity.toBigImage(activity)
                .checkedList(list)
                .currentPosition(position)
                .checkable(false)
                .setmWidget(getDefaultAlbumWight(CommonUtils.getString(R.string.str_gallery)))
                .onResult(new com.yanzhenjie.album.Action<List<String>>() {
                    @Override
                    public void onAction(int requestCode, @android.support.annotation.NonNull List<String> result) {
                    }
                }).start();
    }

    public static Bitmap screenShot(FragmentActivity activity) {
        //获取当前屏幕的大小
        int width = activity.getWindow().getDecorView().getRootView().getWidth();
        int height = activity.getWindow().getDecorView().getRootView().getHeight();
        //生成相同大小的图片
        Bitmap temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //找到当前页面的跟布局
        View view = activity.getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(false);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片
        temBitmap = view.getDrawingCache();
        //Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.shareimg_bottom);
        //temBitmap = verticalBitmap(temBitmap, bmp, AndroidUtil.getBottomStatusHeight(activity));
        return temBitmap;
    }

    public static final int REQUEST_MEDIA_PROJECTION = 18;

    public static Bitmap screenLongShot(FragmentActivity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图
            return screenShot(activity);
        } else {
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                    activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            activity.startActivityForResult(
                    mediaProjectionManager.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION);
            return null;
        }
    }


    public static void shareImgs(final FragmentActivity activity, final List<Bitmap> bitmaps) {
        ShareDialog shareDialog;
        shareDialog = new ShareDialog(activity, new ShareDialog.SharePlatformChooseListener() {
            @Override
            public void onPlatformChoose(Dialog dialog, final ShareItemEntity shareObj) {
                dialog.dismiss();
                if (shareObj.isSystemShare()) {//系统分享
                    List<String> names = new ArrayList<>();
                    List<File> files = new ArrayList<>();
                    for (int i = 0; i < bitmaps.size(); i++) {
                        names.add("/sdcard/" + "BCOIN_share" + System.currentTimeMillis() + ".png");
                    }
                    downBitmapToFile(activity, bitmaps, names, false);
                    for (int i = 0; i < names.size(); i++) {
                        files.add(new File(names.get(i)));
                    }
                    shareWithSystem(activity, shareObj, files, "");
                } else {//sharesdk分享

                }
            }
        });
        List<ShareItemEntity> systemList = getSystemList(activity);
        if (systemList == null || systemList.size() == 0) {
            ToastUtil.show(CommonUtils.getString(R.string.str_share_no));
            return;
        }
        shareDialog.refreshData(systemList);
        shareDialog.show();

        //        ArrayList<File> files = new ArrayList<>();
        //        for(int i=0;i<bitmaps.size();i++){
        //            files.add(ShareDialog.saveMyBitmap("/sdcard/" + "BCOIN_share" + System.currentTimeMillis() + ".png",bitmaps.get(i)));
        //        }
        //        shareSystem(activity,files,"BCOIN_share");
    }


    /**
     * 获取本地分享选项
     *
     * @param context
     * @return
     */
    public static List<ShareItemEntity> getSystemList(Context context) {
        List<ShareItemEntity> list = new ArrayList<>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("image/*");
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(shareIntent, 0);
        for (ResolveInfo info : resInfo) {
            ActivityInfo activityInfo = info.activityInfo;
            String packageName = activityInfo.packageName;
            String className = activityInfo.name;
            if (packageName.equals(WECHAT_PACKAGE_NAME) && className.equals(WECHAT_ACT_NAME)) {
                list.add(new ShareItemEntity(CommonUtils.getString(R.string.str_share_wechat), "Wechat.NAME", R.drawable.sharewx, WECHAT_PACKAGE_NAME, WECHAT_ACT_NAME));
            } else if (packageName.equals(WECHAT_PACKAGE_NAME) && className.equals(WECHAT_MONMENT_ACT_NAME)) {
                list.add(new ShareItemEntity(CommonUtils.getString(R.string.str_share_wechat_circle), "WechatMoments.NAME", R.drawable.sharewxpy, WECHAT_PACKAGE_NAME, WECHAT_MONMENT_ACT_NAME));
            } else if (packageName.equals(QQ_PACKAGE_NAME) && className.equals(QQ_ACT_NAME)) {
                list.add(new ShareItemEntity(CommonUtils.getString(R.string.str_share_qq), "QQ.NAME", R.drawable.shareqq, QQ_PACKAGE_NAME, QQ_ACT_NAME));
            } else if (packageName.equals(SINA_PACKAGE_NAME) && className.equals(SINA_ACT_NAME)) {
                list.add(new ShareItemEntity(CommonUtils.getString(R.string.str_share_sina), "SinaWeibo.NAME", R.drawable.sharewb, SINA_PACKAGE_NAME, SINA_ACT_NAME));
            }
        }
        return list;
    }

    /**
     * 调用系统分享
     *
     * @param context
     * @param itemEntity
     * @param fileImages
     */
    public static void shareWithSystem(Context context, ShareItemEntity itemEntity, List<File> fileImages, String shareTxt) {
        try {
            Intent intent = new Intent();
            ComponentName comp = new ComponentName(itemEntity.getPackageName(), itemEntity.getActivityName());
            intent.setComponent(comp);
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");
            intent.putExtra("Kdescription", shareTxt);
            //            intent.putExtra("Kdescription", "11111111");
            if (fileImages == null || fileImages.size() == 0) {
                ToastUtil.show(CommonUtils.getString(R.string.str_share_nocontent));
                return;
            }
            ArrayList<Uri> uris = new ArrayList<>();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                for (File file : fileImages) {
                    uris.add(Uri.fromFile(file));
                }
            } else {
                for (File file : fileImages) {
                    uris.add(Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null)));
                }
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            ((Activity) context).startActivityForResult(intent, 1000);
        } catch (Throwable e) {
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public static Bitmap convertViewToBitmap2(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.WHITE);
        // Draw view to canvas
        v.draw(c);
        return b;
    }

    public static void shareImgView(final FragmentActivity activity, View shareView) {
        ShareImgDialog shareDialog;
        shareDialog = new ShareImgDialog(activity, shareView,
                new ShareImgDialog.SharePlatformChooseListener() {
                    @Override
                    public void onPlatformChoose(Dialog dialog, final ShareItemEntity shareObj) {
                        dialog.dismiss();
                        List<String> names = new ArrayList<>();
                        List<File> files = new ArrayList<>();
                        List<Bitmap> bitmaps = new ArrayList<>();
                        Bitmap drawingCache = convertViewToBitmap2(((ShareImgDialog) dialog).getLin_share_img());
                        bitmaps.add(drawingCache);
                        for (int i = 0; i < 1; i++) {
                            names.add("/sdcard/" + "share" + System.currentTimeMillis() + ".png");
                        }
                        downBitmapToFile(activity, bitmaps, names, false);
                        for (int i = 0; i < names.size(); i++) {
                            files.add(new File(names.get(i)));
                        }
                        if (shareObj.isSystemShare()) {//系统分享
                            shareWithSystem(activity, shareObj, files, "");
                        } else {//sharesdk分享
                        }
                    }
                });
        List<ShareItemEntity> systemList = getSystemList(activity);
        if (systemList == null || systemList.size() == 0) {
            ToastUtil.show(CommonUtils.getString(R.string.str_share_no));
            return;
        }
        shareDialog.refreshData(systemList);
        shareDialog.show();
    }

    public static ShareImgDialog shareImgViewBySdk(final FragmentActivity activity, View shareView, final UMShareListener umShareListener) {
        ShareImgDialog shareDialog;
        shareDialog = new ShareImgDialog(activity, shareView,
                new ShareImgDialog.SharePlatformChooseListener() {
                    @Override
                    public void onPlatformChoose(Dialog dialog,
                                                 final ShareItemEntity shareObj) {
                        dialog.dismiss();
                        List<String> names = new ArrayList<>();
                        List<Bitmap> bitmaps = new ArrayList<>();
                        Bitmap drawingCache = convertViewToBitmap2
                                (((ShareImgDialog) dialog).getLin_share_img());
                        ((ShareImgDialog) dialog).getLin_share_img().removeAllViews();
                        bitmaps.add(drawingCache);
                        for (int i = 0; i < 1; i++) {
                            names.add("/sdcard/" + "share" + System.currentTimeMillis() + ".png");
                        }
                        downBitmapToFile(activity, bitmaps, names, false);
                        UMImage image = new UMImage(activity,
                                drawingCache);//本地文件
                        SHARE_MEDIA setPlatform = SHARE_MEDIA.WEIXIN;
                        if (shareObj.getPlatfornName().equals("Wechat.NAME")) {
                            setPlatform = SHARE_MEDIA.WEIXIN;
                        } else if (shareObj.getPlatfornName().equals("WechatMoments.NAME")) {
                            setPlatform = SHARE_MEDIA.WEIXIN_CIRCLE;
                        } else {
                            ToastUtil.show(
                                    CommonUtils.getString(R.string.str_file_save) + names.get(0)
                            );
                            return;
                        }
                        new ShareAction(activity)
                                .setPlatform(setPlatform)//传入平台
                                .withMedia(image)
                                .setCallback(umShareListener)//回调监听器
                                .share();
                    }
                });
        List<ShareItemEntity> systemList = ShareImgDialog.getNormalList();
        if (systemList == null || systemList.size() == 0) {
            ToastUtil.show(CommonUtils.getString(R.string.str_share_no));
            return shareDialog;
        }
        shareDialog.refreshData(systemList);
        shareDialog.show();
        return shareDialog;
    }


    /**
     * bitmap保存本地
     */
    public static void saveMyBitmap(String bitName, Bitmap mBitmap) {
        if (mBitmap == null) {
            return;
        }
        File f = new File(bitName);// new File("/sdcard/" + bitName + ".png");
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            MediaStore.Images.Media.insertImage(GlobleContext.getInstance().getApplicationContext().getContentResolver(),
                    f.getAbsolutePath(), bitName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        GlobleContext.getInstance().getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(f.getPath()))));
    }

    /**
     * 下载文件
     *
     * @param context
     * @param pathList
     */
    public static void downBitmapToFile(final Context context, final List<Bitmap> pathList, final List<String> names, final boolean isShowDialog) {
        AndPermission.with(context)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .rationale(new Rationale<List<String>>() {
                    @Override
                    public void showRationale(Context context, List<String> data, RequestExecutor executor) {
                        executor.execute();
                    }
                })
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        for (int i = 0; i < pathList.size(); i++) {
                            if (pathList.get(i) != null) {
                                saveMyBitmap(names.get(i), pathList.get(i));
                            }
                        }
                        if (isShowDialog) {
                            CircleDialogHelper.initDefaultToastDialog((FragmentActivity) context, CommonUtils.getString(R.string.str_file_save), null).show();
                        }
                    }

                }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> permissions) {
                // TODO what to do
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission
                            .WRITE_EXTERNAL_STORAGE)) {
                        ToastUtil.show(CommonUtils.getString(R.string.str_permission_read_write));
                    }
                    //申请权限
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x123);

                } else {
                    for (int i = 0; i < pathList.size(); i++) {
                        if (pathList.get(i) != null) {
                            saveMyBitmap(names.get(i), pathList.get(i));
                        }
                    }
                    if (isShowDialog) {
                        CircleDialogHelper.initDefaultToastDialog((FragmentActivity) context,
                                CommonUtils.getString(R.string.str_file_save), null).show();
                    }
                }
            }
        }).start();
    }

    /**
     * 调用系统分享
     *
     * @param context
     * @param fileImages
     */
    public static void shareSystem(Context context, List<File> fileImages, String shareTxt) {
        try {
            Intent intent = new Intent();
            //ComponentName comp = new ComponentName(itemEntity.getPackageName(), itemEntity.getActivityName());
            //intent.setComponent(comp);
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");
            intent.putExtra("Kdescription", shareTxt);

            //            intent.putExtra("Kdescription", "11111111");
            if (fileImages == null || fileImages.size() == 0) {
                ToastUtil.show(CommonUtils.getString(R.string.str_share_nocontent));
                return;
            }
            ArrayList<Uri> uris = new ArrayList<>();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                for (File file : fileImages) {
                    uris.add(Uri.fromFile(file));
                }
            } else {
                for (File file : fileImages) {
                    uris.add(Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null)));
                }
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            ((Activity) context).startActivityForResult(intent, 1000);
        } catch (Throwable e) {
        }
    }


    /**
     * 纵向拼接
     * <图片纵向拼接>
     *
     * @param first
     * @param second
     * @return 返回Bitmap
     */
    public static Bitmap verticalBitmap(Bitmap first, Bitmap second, float bottomHeight) {
        int width2 = first.getWidth();
        int width3 = second.getWidth();
        int width = Math.max(width2, width3);
        int height2 = first.getHeight();
        int height3 = second.getHeight();


        Matrix matrix = new Matrix();
        // 缩放原图
        float scale = ((float) width2) / ((float) width3);

        matrix.postScale(scale, scale);
        int width1 = second.getWidth();
        int height1 = second.getHeight();

        Bitmap scaleBtm = Bitmap.createBitmap(second, 0, 0, width1, height1, matrix, true);

        Bitmap result = Bitmap.createBitmap(width2, (int) (height2 + scaleBtm.getHeight() - bottomHeight), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(scaleBtm, 0, canvas.getHeight() - scaleBtm.getHeight(), null);
        return result;
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            if (TextUtils.isEmpty(value)) {
                return false;
            }
            if (value.contains(".")) {
                if (ObjectUtils.equals(".", value)) {
                    return false;
                } else {
                    if (ObjectUtils.equals(".", value.substring(value.length() - 1, value.length()))) {
                        return false;
                    }
                }
            }
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDoubleToast(String value) {
        boolean isDouble = isDouble(value);
        if (!isDouble) {
            ToastUtil.show(CommonUtils.getString(R.string.str_input_true_double));
        }
        return isDouble;
    }

    /**
     * 判断数字小数位数
     */
    public static int numPointAfterLength(String value) {
        String[] tempS = (value + "").split("\\.");
        if (tempS.length > 1) {
            char[] tempC = tempS[1].toCharArray();
            int resultNum = tempC.length;
            return resultNum;
        } else {
            return 0;
        }
    }


    public static void onDeniedAndPermission(List<String> permissions) {
        if (AndPermission.hasAlwaysDeniedPermission(GlobleContext.getInstance().getApplicationContext(), permissions)) {
            // 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。

            SettingService settingService = AndPermission.permissionSetting(GlobleContext.getInstance().getApplicationContext());

            // 如果用户同意去设置：
            settingService.execute();

            // 如果用户不同意去设置：
            settingService.cancel();
        }
    }

    //粘贴
    public static void copy(Context c, String txt, boolean isSHare) {
        ClipboardManager mClipboardManager = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", txt);
        mClipboardManager.setPrimaryClip(myClip);
        ToastUtil.show("已复制地址到粘贴板：\n" + txt + (isSHare ? " \n感谢您的分享！" : ""));
    }

    public static void removeListView(final RecyclerView recyclerView, final CommonAdapter adapter, final int position) {
        if (recyclerView.getChildAt(position) != null) {
            if (adapter.getDatas().size() > position) {
                adapter.getDatas().remove(position);
                adapter.notifyItemRemoved(position);
                if (position < adapter.getDatas().size()) { // 如果移除的是最后一个，忽略
                    adapter.notifyItemRangeChanged(position, adapter.getDatas().size() - position);
                }
            }
        }
    }

    public static void removeAnim(final RecyclerView recyclerView, final CommonAdapter adapter, final int position) {
        if (recyclerView.getChildAt(position) != null) {
            if (adapter.getDatas().size() > position) {
                ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                int height = 0;
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    height = height + recyclerView.getChildAt(i).getHeight();
                }
                layoutParams.height = height;
                recyclerView.setLayoutParams(layoutParams);
                final int removeViewHeight = recyclerView.getChildAt(position).getHeight();
                final int recyclerViewHeight = height;
                adapter.getDatas().remove(position);
                adapter.notifyItemRemoved(position);
                if (position < adapter.getDatas().size()) { // 如果移除的是最后一个，忽略
                    adapter.notifyItemRangeChanged(position, adapter.getDatas().size() - position);
                }
                final RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        return true;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                };
                recyclerView.addOnItemTouchListener(onItemTouchListener);
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ValueAnimator animator = ValueAnimator.ofInt(recyclerViewHeight, recyclerViewHeight - removeViewHeight);
                        animator.setDuration(recyclerView.getItemAnimator().getRemoveDuration());
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        animator.start();
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                if (recyclerView != null && onItemTouchListener != null) {
                                    Integer animatedValue = (Integer) animation.getAnimatedValue();
                                    ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                                    if (animatedValue == recyclerViewHeight - removeViewHeight) {
                                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                        recyclerView.removeOnItemTouchListener(onItemTouchListener);
                                    } else {
                                        layoutParams.height = animatedValue;
                                    }
                                    recyclerView.setLayoutParams(layoutParams);
                                }
                            }
                        });
                    }
                }, recyclerView.getItemAnimator().getRemoveDuration() + recyclerView.getItemAnimator().getChangeDuration());
            }
        }
    }

}
