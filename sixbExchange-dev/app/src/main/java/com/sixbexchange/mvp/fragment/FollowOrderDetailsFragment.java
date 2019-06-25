package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.FollowOrderDetailsBean;
import com.sixbexchange.mvp.activity.WebActivityActivity;
import com.sixbexchange.mvp.databinder.FollowOrderDetailsBinder;
import com.sixbexchange.mvp.delegate.FollowOrderDetailsDelegate;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
* 跟单详情
* @author gqf
* @Description
* @Date  2019/4/3 0003 11:16
* @Param
* @return
**/
public class FollowOrderDetailsFragment extends BaseDataBindFragment<FollowOrderDetailsDelegate, FollowOrderDetailsBinder> {

    @Override
    protected Class<FollowOrderDetailsDelegate> getDelegateClass() {
        return FollowOrderDetailsDelegate.class;
    }

    @Override
    public FollowOrderDetailsBinder getDataBinder(FollowOrderDetailsDelegate viewDelegate) {
        return new FollowOrderDetailsBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("6b.top跟单详情"));

        viewDelegate.viewHolder.tv_customer_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivityActivity.startAct(getActivity(),
                        "https://bicoin.oss-cn-beijing.aliyuncs.com/6b/userweb/kefu.html");
            }
        });
    }

    FollowOrderDetailsBean s;

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState != null) {
            id = savedInstanceState.getString("id", "");
        } else {
            id = this.getArguments().getString("id", "");
        }
        viewDelegate.setToolColor(R.color.mark_color, false);
        addRequest(binder.followdetail(id, this));
    }

    public static FollowOrderDetailsFragment newInstance(
            String id
    ) {
        FollowOrderDetailsFragment newFragment = new FollowOrderDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    String id = "";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("id", id);
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                s = GsonUtil.getInstance().toObj(data, FollowOrderDetailsBean.class);
                initView();
                break;
        }
    }

    private void initView() {
        GlideUtils.loadImage(s.getIcon(), viewDelegate.viewHolder.iv_pic);
        viewDelegate.viewHolder.tv_name.setText(s.getNickName());
        viewDelegate.viewHolder.tv_sub.setText(s.getTitle());
        viewDelegate.viewHolder.tv_rate.setText(s.getBenitfitRate());
        viewDelegate.viewHolder.tv_days.setText(s.getCloseDay());

        viewDelegate.viewHolder.tv_real_rate.setText(s.getBicoinRate() + "%");

        viewDelegate.viewHolder.tv_max_money.setText(
                new BigDecimal(s.getAllMoney())
                        .multiply(new BigDecimal(s.getAmountMax()))
                        .divide(new BigDecimal(s.getAllAmount()), 2, RoundingMode.DOWN)
                        .stripTrailingZeros()
                        .toPlainString());

        viewDelegate.viewHolder.tv_follow_money.setText(
                new BigDecimal(s.getAllMoney())
                        .multiply(new BigDecimal(s.getAmountMin()))
                        .divide(new BigDecimal(s.getAllAmount()), 2, RoundingMode.DOWN)
                        .stripTrailingZeros()
                        .toPlainString());


        viewDelegate.viewHolder.tv_follow_unit.setText(s.getCurrency());
        viewDelegate.viewHolder.tv_max_unit.setText(s.getCurrency());

        viewDelegate.viewHolder.tv_progress_start.setText(
                "已抢投" +
                        new BigDecimal(s.getAllAmount())
                                .subtract(new BigDecimal(s.getRestAmount()))
                                .multiply(new BigDecimal("100"))
                                .divide(new BigDecimal(s.getAllAmount()), 2, RoundingMode.DOWN)
                                .stripTrailingZeros()
                                .toPlainString()
                        + "%"
        );
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewDelegate.viewHolder.tv_progress_start.getLayoutParams();
        layoutParams.weight = new BigDecimal(s.getAllAmount()).subtract(new BigDecimal(s.getRestAmount())).floatValue();
        viewDelegate.viewHolder.tv_progress_start.setLayoutParams(layoutParams);
        viewDelegate.viewHolder.tv_progress_start.setBackground(new RadiuBg(
                CommonUtils.getColor(R.color.color_25A73F),
                999, 999, 999, 999
        ));

        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) viewDelegate.viewHolder.view_progress_end.getLayoutParams();
        layoutParams2.weight = new BigDecimal(s.getRestAmount()).floatValue();
        viewDelegate.viewHolder.view_progress_end.setLayoutParams(layoutParams2);
        viewDelegate.viewHolder.tv_all.setText(" "+s.getAllMoney());
        viewDelegate.viewHolder.tv_all_unit.setText(" "+ s.getCurrency()+",剩余 ");
        viewDelegate.viewHolder.tv_rest.setText( new BigDecimal(s.getAllMoney())
                .multiply(new BigDecimal(s.getRestAmount()))
                .divide(new BigDecimal(s.getAllAmount()), 2, RoundingMode.DOWN)
                .stripTrailingZeros()
                .toPlainString());
        viewDelegate.viewHolder.tv_rest_unit.setText(" "+ s.getCurrency()+"");


        viewDelegate.viewHolder.tv_content_interest.setText(s.getShareMemo());
        viewDelegate.viewHolder.tv_content_introduction.setText(s.getMemo());

        viewDelegate.viewHolder.tv_start_time.setText(s.getEndTimeStr());
        viewDelegate.viewHolder.tv_updata_income.setText(s.getBenifitRefresh());
        viewDelegate.viewHolder.tv_send_income.setText(s.getBenifitDistribution());


        viewDelegate.viewHolder.tv_info1.setText(s.getStandard());
        viewDelegate.viewHolder.tv_info2.setText(s.getPrincipal());
        viewDelegate.viewHolder.tv_info3.setText(s.getStopRate() + "%");
        viewDelegate.viewHolder.tv_info4.setText(s.getAllMoney() + s.getCurrency());
        viewDelegate.viewHolder.tv_info5.setText(s.getDealType());
        viewDelegate.viewHolder.tv_info6.setText(s.getMoneyManage());
        viewDelegate.viewHolder.tv_info7.setText(s.getOwnMoney() + s.getCurrency());
        viewDelegate.viewHolder.tv_info8.setText(s.getLeverage());


        viewDelegate.viewHolder.tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(FollowOrderFragment.newInstance(id));
            }
        });
        //private Integer status;//1 未开始   ，  0 进行中 ，2 运行中 ，3 已结束
        if (s.getStatus() == 1) {
            viewDelegate.viewHolder.tv_buy.setEnabled(false);
            viewDelegate.viewHolder.tv_buy.setText("即将开始");
            viewDelegate.viewHolder.tv_buy.setSolidColor(CommonUtils.getColor(R.color.black_transparent_100));
        } else if (s.getStatus() == 0) {
            viewDelegate.viewHolder.tv_buy.setEnabled(true);
            viewDelegate.viewHolder.tv_buy.setText("立即抢投");
            viewDelegate.viewHolder.tv_buy.setSolidColor(CommonUtils.getColor(R.color.color_FA8C16));
        } else if (s.getStatus() == 2) {
            viewDelegate.viewHolder.tv_buy.setEnabled(false);
            viewDelegate.viewHolder.tv_buy.setText("运行中");
            viewDelegate.viewHolder.tv_buy.setSolidColor(CommonUtils.getColor(R.color.color_25A73F));
        } else if (s.getStatus() == 3) {
            viewDelegate.viewHolder.tv_buy.setEnabled(false);
            viewDelegate.viewHolder.tv_buy.setText("已结束");
            viewDelegate.viewHolder.tv_buy.setSolidColor(CommonUtils.getColor(R.color.black_transparent_100));
        }

    }

}
