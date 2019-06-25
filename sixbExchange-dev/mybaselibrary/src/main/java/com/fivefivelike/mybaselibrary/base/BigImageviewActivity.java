package com.fivefivelike.mybaselibrary.base;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.biv.view.BigImageView;
import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.logger.KLog;
import com.fivefivelike.mybaselibrary.view.ProgressPieIndicator;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.album.util.PermissionUtils;
import com.yanzhenjie.statusview.StatusUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 郭青枫 on 2017/10/16.
 */

public class BigImageviewActivity extends BaseActivity<BigImageveiwDelegate> {
    private static final int PERMISSION_STORAGE = 1;
    private List<String> mPathList = new ArrayList();
    private int mCurrentItemPosition;
    private MenuItem mFinishMenuItem;
    private boolean mCheckable;
    private Map<String, Boolean> mCheckedMap;
    public static Action<List<String>> sResult;
    public static Action<String> sCancel;
    @NonNull
    private Widget mWidget;
    private int mNavigationAlpha;
    private int mRequestCode;

    @Override
    protected Class<BigImageveiwDelegate> getDelegateClass() {
        return BigImageveiwDelegate.class;
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusUtils.setFullToStatusBar(this);
        setSupportActionBar(viewDelegate.viewHolder.toolbar);
        initIntentData();
        if (mPathList == null) {
            KLog.e("Parameter error.",
                    new IllegalArgumentException("The checkedList can be null."));
            onGalleryCancel();
        } else if (mPathList.size() == 0 || mCurrentItemPosition == mPathList.size()) {
            KLog.e("Parameter error.",
                    new IllegalArgumentException("The currentPosition is " + mCurrentItemPosition + ","
                            + " the checkedList.size() is " + mPathList.size()));
            onGalleryCancel();
        } else {
            mCheckedMap = new HashMap<>();
            for (String path : mPathList) {
                mCheckedMap.put(path, true);
            }
            initializeWidget();
            initializePager();

            requestPermission(PERMISSION_STORAGE);
        }
    }

    /**
     * Initialize widget.
     */
    private void initializeWidget() {
        int navigationColor = mWidget.getNavigationBarColor();
        navigationColor = AlbumUtils.getAlphaColor(navigationColor, mNavigationAlpha);
        StatusUtils.setFullToNavigationBar(this);
        StatusUtils.setNavigationBarColor(this, navigationColor);

        setTitle(mWidget.getTitle());

        if (!mCheckable) {
            findViewById(R.id.bottom_root).setVisibility(View.GONE);
        } else {
            ColorStateList itemSelector = mWidget.getMediaItemCheckSelector();
            viewDelegate.viewHolder.cb_album_check.setSupportButtonTintList(itemSelector);

            viewDelegate.viewHolder.cb_album_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = viewDelegate.viewHolder.cb_album_check.isChecked();
                    mCheckedMap.put(mPathList.get(mCurrentItemPosition), isChecked);
                    setCheckedCountUI(getCheckCount());
                }
            });
        }
    }


    /**
     * Get check item count.
     */
    private int getCheckCount() {
        int checkedCount = 0;
        for (Map.Entry<String, Boolean> entry : mCheckedMap.entrySet()) {
            if (entry.getValue())
                checkedCount += 1;
        }
        return checkedCount;
    }

    /**
     * 获取返回来的数据
     */
    private void initIntentData() {
        Intent intent = getIntent();
        mRequestCode = intent.getIntExtra(Album.KEY_INPUT_REQUEST_CODE, 0);
        mWidget = intent.getParcelableExtra(Album.KEY_INPUT_WIDGET);
        mPathList = (List<String>) intent.getSerializableExtra(Album.KEY_INPUT_CHECKED_LIST);
        mCurrentItemPosition = intent.getIntExtra(Album.KEY_INPUT_CURRENT_POSITION, 0);
        mCheckable = intent.getBooleanExtra(Album.KEY_INPUT_GALLERY_CHECKABLE, true);
        mNavigationAlpha = intent.getIntExtra(Album.KEY_INPUT_NAVIGATION_ALPHA, 80);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_menu_preview, menu);
        mFinishMenuItem = menu.findItem(R.id.album_menu_finish);
        if (!mCheckable)
            mFinishMenuItem.setVisible(false);
        else
            setCheckedCountUI(getCheckCount());
        return true;
    }

    /**
     * Set the number of selected pictures.
     */
    private void setCheckedCountUI(int count) {
        String finishStr = getString(R.string.album_menu_finish);
        finishStr += "(" + count + " / " + mPathList.size() + ")";
        mFinishMenuItem.setTitle(finishStr);
    }

    /**
     * Initialize ViewPager.
     */
    private void initializePager() {
        if (mPathList != null) {
            if (mPathList.size() > 3)
                viewDelegate.viewHolder.view_pager.setOffscreenPageLimit(3);
            else if (mPathList.size() > 2)
                viewDelegate.viewHolder.view_pager.setOffscreenPageLimit(2);
        }
        viewDelegate.viewHolder.view_pager.addOnPageChangeListener(mPageChangeListener);
    }


    /**
     * Scan, but unknown permissions.
     *
     * @param requestCode request code.
     */
    private void requestPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permission = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            String[] deniedPermissions = PermissionUtils.getDeniedPermissions(this, permission);

            if (deniedPermissions.length == 0) {
                dispatchGrantedPermission(requestCode);
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        deniedPermissions,
                        requestCode);
            }
        } else {
            dispatchGrantedPermission(requestCode);
        }
    }

    /**
     * Dispatch granted permission.
     */
    private void dispatchGrantedPermission(int requestCode) {
        switch (requestCode) {
            case PERMISSION_STORAGE: {
                BigImageViewAdapter previewAdapter = new BigImageViewAdapter();
                viewDelegate.viewHolder.view_pager.setAdapter(previewAdapter);
                viewDelegate.viewHolder.view_pager.setCurrentItem(mCurrentItemPosition);
                mPageChangeListener.onPageSelected(mCurrentItemPosition);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE: {
                if (PermissionUtils.isGrantedResult(grantResults))
                    dispatchGrantedPermission(requestCode);
                else
                    albumPermissionDenied();
                break;
            }
        }
    }

    /**
     * The permission for Album is denied.
     */
    private void albumPermissionDenied() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.album_title_permission_failed)
                .setMessage(R.string.album_permission_storage_failed_hint)
                .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onGalleryCancel();
                    }
                })
                .show();
    }

    /**
     * Listener of ViewPager changed.
     */
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mCurrentItemPosition = position;
            viewDelegate.viewHolder.cb_album_check.setChecked(mCheckedMap.get(mPathList.get(mCurrentItemPosition)));
            viewDelegate.viewHolder.toolbar.setSubtitle(mCurrentItemPosition + 1 + " / " + mPathList.size());
        }
    };

    private void onGalleryResult() {
        if (sResult != null) {
            ArrayList<String> checkedList = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : mCheckedMap.entrySet()) {
                if (entry.getValue())
                    checkedList.add(entry.getKey());
            }
            sResult.onAction(mRequestCode, checkedList);
        }
        setResult(RESULT_OK);
        finish();
    }

    private void onGalleryCancel() {
        if (sCancel != null)
            sCancel.onAction(mRequestCode, "User canceled.");
        setResult(RESULT_CANCELED);
        finish();
    }

//    @Override
//    public void onBackPressed() {
//        onGalleryCancel();
//    }ao

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.album_menu_finish) {
            onGalleryResult();
        } else if (id == android.R.id.home) {
            onGalleryCancel();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        sResult = null;
        sCancel = null;
        super.onDestroy();
    }


    private class BigImageViewAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPathList == null ? 0 : mPathList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BigImageView imageView = new BigImageView(BigImageviewActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            imageView.setProgressIndicator(new ProgressPieIndicator());
            imageView.showImage(mPathList.get(position));
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((View) object));
        }
    }


    /**
     * 开始调用
     *
     * @param context
     * @return
     */
    public static BigmageViewWrapper toBigImage(Context context) {
        return new BigmageViewWrapper(context);
    }


    public static class BigmageViewWrapper {
        @NonNull
        final Context mContext;
        Action<List<String>> mResult;
        Action<String> mCancel;
        int mRequestCode;
        @Nullable
        Widget mWidget;
        @Nullable
        List<String> mChecked;
        @IntRange(from = 1, to = Integer.MAX_VALUE)
        int mCurrentPosition = 0;
        boolean mCheckable = true;
        @IntRange(from = 0, to = 255)
        int mNavigationAlpha = 80;

        public BigmageViewWrapper(@NonNull Context context) {
            this.mContext = context;
            mWidget = Widget.getDefaultWidget(context);
        }

        public BigmageViewWrapper setmWidget(@Nullable Widget mWidget) {
            this.mWidget = mWidget;
            return this;
        }

        /**
         * Set the action when result.
         */
        public final BigmageViewWrapper onResult(Action<List<String>> result) {
            this.mResult = result;
            return this;
        }

        /**
         * Set the list has been selected.
         */
        public final BigmageViewWrapper checkedList(List<String> checked) {
            this.mChecked = checked;
            return this;
        }

        /**
         * Set the show position of List.
         */
        public BigmageViewWrapper currentPosition(@IntRange(from = 1, to = Integer.MAX_VALUE) int currentPosition) {
            this.mCurrentPosition = currentPosition;
            return this;
        }

        /**
         * The ability to select pictures.
         */
        public BigmageViewWrapper checkable(boolean checkable) {
            this.mCheckable = checkable;
            return this;
        }

        /**
         * Set alpha of NavigationBar.
         */
        public BigmageViewWrapper navigationAlpha(@IntRange(from = 0, to = 255) int alpha) {
            this.mNavigationAlpha = alpha;
            return this;
        }

        /**
         * Set the action when canceling.
         */
        public final BigmageViewWrapper onCancel(Action<String> cancel) {
            this.mCancel = cancel;
            return this;
        }

        /**
         * Request tag.
         */
        public final BigmageViewWrapper requestCode(int requestCode) {
            this.mRequestCode = requestCode;
            return this;
        }

        /**
         * Set the widget property.
         */
        public final BigmageViewWrapper widget(@Nullable Widget widget) {
            this.mWidget = widget;
            return this;
        }

        /**
         * Start up.
         */
        public void start() {
            sResult = mResult;
            sCancel = mCancel;
            Intent intent = new Intent(mContext, BigImageviewActivity.class);
            intent.putExtra(Album.KEY_INPUT_REQUEST_CODE, mRequestCode);
            intent.putExtra(Album.KEY_INPUT_WIDGET, mWidget);
            intent.putExtra(Album.KEY_INPUT_CHECKED_LIST, (Serializable) mChecked);
            intent.putExtra(Album.KEY_INPUT_CURRENT_POSITION, mCurrentPosition);
            intent.putExtra(Album.KEY_INPUT_GALLERY_CHECKABLE, mCheckable);
            intent.putExtra(Album.KEY_INPUT_NAVIGATION_ALPHA, mNavigationAlpha);
            mContext.startActivity(intent);
        }
    }

}
