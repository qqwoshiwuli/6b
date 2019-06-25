package com.sixbexchange.mvp.fragment;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.circledialog.CircleDialogHelper;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.AppUtil;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.fivefivelike.mybaselibrary.view.GlideBannerImageLoader;
import com.google.gson.Gson;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.AppVersion;
import com.sixbexchange.entity.bean.OpenChartBean;
import com.sixbexchange.entity.bean.UserLoginInfo;
import com.sixbexchange.mvp.activity.LoginAndRegisteredActivity;
import com.sixbexchange.mvp.activity.WebActivityActivity;
import com.sixbexchange.mvp.databinder.MineBinder;
import com.sixbexchange.mvp.delegate.MineDelegate;
import com.sixbexchange.mvp.dialog.UpdateDialog;
import com.sixbexchange.server.UpdateService;
import com.sixbexchange.utils.UserSet;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/*
* 用户主页
* @author gqf
* @Description
* @Date  2019/4/3 0003 11:13
* @Param
* @return
**/

public class MineFragment extends BaseDataBindFragment<MineDelegate, MineBinder> {

    @Override
    protected Class<MineDelegate> getDelegateClass() {
        return MineDelegate.class;
    }

    @Override
    public MineBinder getDataBinder(MineDelegate viewDelegate) {
        return new MineBinder(viewDelegate);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("个人中心")
                //.setmRightImg1(CommonUtils.getString(R.string.ic_envelope) + " 通知中心")
                .setShowBack(false));
//        GlideUtils.loadImage("http://bicoin.oss-cn-beijing.aliyuncs.com/6b/img/userinvite.png",
//                viewDelegate.viewHolder.iv_invite, GlideUtils.getNoCacheRO());
        addRequest(binder.getopenchart(
                "2", MineFragment.this
        ));
//        viewDelegate.viewHolder.iv_invite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getParentFragment() instanceof MainFragment) {
//                    ((MainFragment) getParentFragment()).startBrotherFragment(new InviteFriendsFragment());
//                }
//            }
//        });


        viewDelegate.viewHolder.lin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivityActivity.startAct(getActivity(),
                        "https://bcoin2018.mikecrm.com/K8BBMl1");
            }
        });
        viewDelegate.viewHolder.lin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivityActivity.startAct(getActivity(),
                        "https://bicoin.oss-cn-beijing.aliyuncs.com/6b/userweb/jiarushequn.html");
            }
        });
        viewDelegate.viewHolder.lin6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivityActivity.startAct(getActivity(),
                        "https://bicoin.oss-cn-beijing.aliyuncs.com/6b/userweb/changjianwenti.html");
            }
        });
        viewDelegate.viewHolder.lin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequest(binder.getlatestversion(
                        AppUtils.getAppVersionName(), MineFragment.this
                ));
            }
        });

    }

    UserLoginInfo userLoginInfo;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        userLoginInfo = UserLoginInfo.getLoginInfo();
        viewDelegate.viewHolder.tv_name.setText(userLoginInfo.getMobile());
        viewDelegate.viewHolder.tv_uid.setText("UID " + userLoginInfo.getUid());
        if (UiHeplUtils.compareVersion(UserSet.getinstance().getSystemVersion(),
                AppUtils.getAppVersionName()) == 1) {
            viewDelegate.viewHolder.tv_version.setText("有新版本");
        } else {
            viewDelegate.viewHolder.tv_version.setText("当前版本 V" + AppUtils.getAppVersionName());
        }
        GlideUtils.loadImage(userLoginInfo.gethImg(),
                viewDelegate.viewHolder.iv_pic);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        viewDelegate.viewHolder.tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginAndRegisteredActivity.class));
                getActivity().finish();
            }
        });
        viewDelegate.viewHolder.lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragment() instanceof MainFragment) {
                    ((MainFragment) getParentFragment()).startBrotherFragment(new SecurityCenterFragment());
                }
            }
        });
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                //版本更新
                appVersion = GsonUtil.getInstance().toObj(data, AppVersion.class);
                version();
            break;
            case 0x124:
                openChartBeans=GsonUtil.getInstance().toList(data,OpenChartBean.class);
                if (openChartBeans.size()>0)
                {

                    imagePath.clear();
                    for (int i = 0; i < openChartBeans.size(); i++) {
                        imagePath.add(openChartBeans.get(i).getImgUrl());
                        imagetitle.add(""+i);
//                        Log.i("wuli","url="+openChartBeans.get(i).getImgUrl());
                    }
                    banners();
                }
                break;
        }
    }
    List<OpenChartBean> openChartBeans;
    ArrayList<String> imagePath=new ArrayList<>();
    ArrayList<String> imagetitle=new ArrayList<>();
    GlideBannerImageLoader imageLoader;
    public void banners()
    {
        viewDelegate.viewHolder.mBanner.setVisibility(View.VISIBLE);
        imageLoader=new GlideBannerImageLoader();
        //设置样式，里面有很多种样式可以自己都看看效果
        viewDelegate.viewHolder.mBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        viewDelegate.viewHolder.mBanner.setImageLoader(imageLoader);
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        viewDelegate. viewHolder.mBanner.setBannerAnimation(Transformer.ZoomOutSlide);
        //轮播图片的文字
        viewDelegate.viewHolder.mBanner.setBannerTitles(imagetitle);
        //设置轮播间隔时间
        viewDelegate.viewHolder.mBanner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        viewDelegate.viewHolder.mBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        viewDelegate. viewHolder.mBanner.setIndicatorGravity(BannerConfig.NOT_INDICATOR);
        //设置图片加载地址
        viewDelegate.viewHolder.mBanner.setImages(imagePath)
                //轮播图的监听
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        if (openChartBeans.get(position).getIsSkip()==1)
                        {
                            if (openChartBeans.get(position).getIsNeed()==1)
                            {
                                Intent intent= new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(openChartBeans.get(position).getSkipUrl());
                                intent.setData(content_url);
                                startActivity(intent);
                            }else
                            {
                                if (openChartBeans.get(position).getSkipUrl().contains("invitationPage"))
                                {
                                    if (getParentFragment() instanceof MainFragment) {
                                        ((MainFragment) getParentFragment()).startBrotherFragment(new SecurityCenterFragment());
                                    }
                                }else
                                {
                                   WebActivityActivity.startAct(getContext(),openChartBeans.get(position).getSkipUrl());
                                }
                            }
                        }
                    }
                })
                //开始调用的方法，启动轮播图。
                .start();
    };
    AppVersion appVersion;

    private void version() {
        if (UiHeplUtils.compareVersion(appVersion.getSystemVersion(), AppUtils.getAppVersionName()) == 1) {
            new UpdateDialog(viewDelegate.getActivity())
                    .setAppVersion(appVersion)
                    .setDefaultClickLinsener(new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            if (position == 1) {
                                if (AppUtil.isWifi(viewDelegate.getActivity())) {
                                    updataApp();
                                } else {
                                    CircleDialogHelper.initDefaultDialog(viewDelegate.getActivity(), CommonUtils.getString(R.string.isinstall_in_no_wifi), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            updataApp();
                                        }
                                    }).show();
                                }
                            }
                        }
                    }).showDialog();
        } else {
            ToastUtil.show(CommonUtils.getString(R.string.str_no_new_version));
        }

    }

    private void updataApp() {
        AndPermission.with(this)
                .permission(Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        UpdateService.
                                Builder.create(appVersion.getDownloadAddr())
                                .setStoreDir("update")
                                .setIcoResId(R.drawable.artboard)
                                .setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
                                .setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL)
                                .setAppVersion(appVersion)
                                .build(viewDelegate.getActivity());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        ToastUtil.show(CommonUtils.getString(R.string.str_permission_read_write));
                    }
                }).start();

    }

}
