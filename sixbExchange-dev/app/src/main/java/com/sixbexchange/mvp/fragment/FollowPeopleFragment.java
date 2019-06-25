package com.sixbexchange.mvp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BasePullFragment;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.view.GlideBannerImageLoader;
import com.sixbexchange.R;
import com.sixbexchange.adapter.FollowPeopleAdapter;
import com.sixbexchange.entity.bean.FollowPeopleBean;
import com.sixbexchange.entity.bean.OpenChartBean;
import com.sixbexchange.mvp.activity.WebActivityActivity;
import com.sixbexchange.mvp.databinder.BaseFragmentPullBinder;
import com.sixbexchange.mvp.delegate.BaseFragentPullDelegate;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;
/*
*跟单列表
* @author gqf
* @Description
* @Date  2019/4/3 0003 11:16
* @Param
* @return
**/
public class FollowPeopleFragment extends BasePullFragment<BaseFragentPullDelegate, BaseFragmentPullBinder> {

    @Override
    protected Class<BaseFragentPullDelegate> getDelegateClass() {
        return BaseFragentPullDelegate.class;
    }

    @Override
    public BaseFragmentPullBinder getDataBinder(BaseFragentPullDelegate viewDelegate) {
        return new BaseFragmentPullBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initList(new ArrayList<FollowPeopleBean>());
        addRequest(binder.getopenchart(
                "1", FollowPeopleFragment.this
        ));
        onRefresh();
    }

    FollowPeopleAdapter adapter;

    private void initList(List<FollowPeopleBean> data) {
        if (adapter == null) {
            adapter = new FollowPeopleAdapter(getActivity(), data);
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (getParentFragment().getParentFragment() instanceof MainFragment) {
                        ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(
                                FollowOrderDetailsFragment.newInstance(adapter.getDatas().get(position)
                                        .getId() + "")
                        );
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            viewDelegate.setIsLoadMore(false);
            initRecycleViewPull(adapter, new LinearLayoutManager(getActivity()));
            viewDelegate.viewHolder.pull_recycleview.setBackgroundColor(CommonUtils.getColor(R.color.base_bg));
        } else {
            getDataBack(adapter.getDatas(), data, adapter);
        }
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                initList(GsonUtil.getInstance().toList(data, FollowPeopleBean.class));
                break;
            case 0x134:
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
    @Override
    protected void refreshData() {
        addRequest(binder.followlist(this));
    }
}
