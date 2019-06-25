package com.fivefivelike.mybaselibrary.view.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.adapter.ShareAdapter;
import com.fivefivelike.mybaselibrary.adapter.entity.ShareItemEntity;
import com.fivefivelike.mybaselibrary.http.SingleRequest;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.FileUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.logger.KLog;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 郭青枫 on 2017/9/30.
 */

public class ShareDialog extends BaseDialog {
    private RecyclerView recycleview;
    private TextView tv_cancel;
    public ShareAdapter adapter;
    public List<ShareItemEntity> list;
    private SharePlatformChooseListener sharePlatformChooseListener;

    public ShareDialog(Activity context, SharePlatformChooseListener sharePlatformChooseListener) {
        super(context);
        this.sharePlatformChooseListener = sharePlatformChooseListener;
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_share;
    }

    @Override
    protected void startInit() {
        getWindow().setGravity(Gravity.BOTTOM);
        setWindowNoPadding();
        recycleview = (RecyclerView) findViewById(R.id.recycleview);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        list = new ArrayList<>();
        adapter = new ShareAdapter(mContext, list);
        recycleview.setLayoutManager(new GridLayoutManager(mContext, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recycleview.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (sharePlatformChooseListener != null) {
                    sharePlatformChooseListener.onPlatformChoose(ShareDialog.this, adapter.getDatas().get(position));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface SharePlatformChooseListener {
        void onPlatformChoose(Dialog dialog, ShareItemEntity shareObj);
    }


    public void refreshData(List<ShareItemEntity> list) {
        this.list.clear();
        if (list != null) {
            this.list.addAll(list);
        }
        adapter.notifyDataSetChanged();
    }


    /**
     * 获取正常走sharesdk分享的选项
     *
     * @return
     */
    public static List<ShareItemEntity> getNormalList() {
        List<ShareItemEntity> list = new ArrayList<>();
        list.add(new ShareItemEntity(CommonUtils.getString(R.string.str_share_wechat), "Wechat.NAME", R.drawable.sharewx));
        list.add(new ShareItemEntity(CommonUtils.getString(R.string.str_share_wechat_circle), " WechatMoments.NAME", R.drawable.sharewxpy));
        list.add(new ShareItemEntity(CommonUtils.getString(R.string.str_share_qq), "QQ.NAME", R.drawable.shareqq));
        //list.add(new ShareItemEntity("QQ空间", QZone.NAME, R.drawable.shareqqkj));
        //list.add(new ShareItemEntity("新浪微博", SinaWeibo.NAME, R.drawable.sharewb));
        return list;
    }



    /**
     * 调用sharesdk分享
     *
     * @param context
     * @param obj
     */
    //    public static void showShare(final Context context, final ShareEntity obj, PlatformActionListener platformActionListener) {
    //        final OnekeyShare oks = new OnekeyShare();
    //        oks.disableSSOWhenAuthorize();
    //        if (obj.getPlatform() != null) {
    //            oks.setPlatform(obj.getPlatform());
    //        }
    //        String title = obj.getTitle();
    //        String content = obj.getContent();
    //        final String url = obj.getUrl();
    //        String imageUrl = obj.getPic();
    //        if (StringUtil.isBlank(title)) {
    //            title = CommonUtils.getString(R.string.str_share_default_title);
    //        }
    //        if (StringUtil.isBlank(content)) {
    //            content = CommonUtils.getString(R.string.str_share_default_content);
    //        }
    //        oks.setUrl(url);
    //        oks.setTitleUrl(url);
    //        oks.setTitle(title);
    //        oks.setText(content);
    //        oks.setVenueName(CommonUtils.getString(R.string.app_name));
    //        oks.setImageUrl(imageUrl);
    //        oks.setCallback(platformActionListener);
    //        oks.show(GlobleContext.getInstance().getApplicationContext());
    //    }




    /**
     * 下载文件
     *
     * @param context
     * @param pathList
     * @param downLoadBack
     */
    public static void downFile(final Context context, final List<String> pathList, final DownLoadBack downLoadBack) {
        AndPermission.with(context)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        if (pathList == null || pathList.size() == 0)
                            return;
                        final List<File> fileList = new ArrayList<>();
                        final int[] count = new int[]{pathList.size()};
                        final NetConnectDialog dialog = new NetConnectDialog(context);
                        DownloadListener downloadListener = new DownloadListener() {
                            @Override
                            public void onDownloadError(int what, Exception exception) {
                                KLog.i();
                                count[0] = count[0] - 1;
                                if (fileList.size() == count[0]) {
                                    downLoadBack.onBack(fileList);
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
                                dialog.show();
                            }

                            @Override
                            public void onProgress(int what, int progress, long fileCount, long speed) {
                            }

                            @Override
                            public void onFinish(int what, String filePath) {
                                fileList.add(new File(filePath));
                                if (fileList.size() == count[0]) {
                                    downLoadBack.onBack(fileList);
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onCancel(int what) {
                                count[0] = count[0] - 1;
                                if (fileList.size() == count[0]) {
                                    downLoadBack.onBack(fileList);
                                    dialog.dismiss();
                                }
                            }
                        };
                        for (int i = 0; i < pathList.size(); i++) {
                            DownloadRequest downloadRequest = NoHttp.createDownloadRequest(pathList.get(i), FileUtil.getShareImagePath(context), false);
                            SingleRequest.getInstance().download(i, downloadRequest, downloadListener);
                        }
                    }

                }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> permissions) {
                // TODO what to do
                ToastUtil.show(CommonUtils.getString(R.string.str_permission_read_write));
            }
        }).start();

    }

    public interface DownLoadBack {
        void onBack(List<File> list);
    }

}
