package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.TypeReference;
import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.FollowOrderDetailsBean;
import com.sixbexchange.entity.bean.WalletCoinBean;
import com.sixbexchange.mvp.activity.WebActivityActivity;
import com.sixbexchange.mvp.databinder.FollowOrderBinder;
import com.sixbexchange.mvp.delegate.FollowOrderDelegate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/*
* 跟投页面
* @author gqf
* @Description
* @Date  2019/4/3 0003 11:16
* @Param
* @return
**/
public class FollowOrderFragment extends BaseDataBindFragment<FollowOrderDelegate, FollowOrderBinder> {

    @Override
    protected Class<FollowOrderDelegate> getDelegateClass() {
        return FollowOrderDelegate.class;
    }

    @Override
    public FollowOrderBinder getDataBinder(FollowOrderDelegate viewDelegate) {
        return new FollowOrderBinder(viewDelegate);
    }

    int num = 1;

    boolean isSelect = true;

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("投资确认"));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState != null) {
            id = savedInstanceState.getString("id", "");
        } else {
            id = this.getArguments().getString("id", "");
        }
        addRequest(binder.followdetail(id, this));
        viewDelegate.viewHolder.tv_protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivityActivity.startAct(getActivity(),
                        "https://bicoin.oss-cn-beijing.aliyuncs.com/6b/userweb/gendanxieyi.html");
            }
        });
    }

    public static FollowOrderFragment newInstance(
            String id
    ) {
        FollowOrderFragment newFragment = new FollowOrderFragment();
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

    FollowOrderDetailsBean s;

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                s = GsonUtil.getInstance().toObj(data, FollowOrderDetailsBean.class);
                initView();
                addRequest(binder.getAccountDetail(this));
                break;
            case 0x124:
                startWithPop(new FollowOrderSuccessFragment());
                break;
            case 0x125:
                List<String> list = GsonUtil.getInstance().toList(
                        data, String.class
                );
                Map<String, String> map = GsonUtil.getInstance().toMap(list.get(0),
                        new TypeReference<Map<String, String>>() {
                        });
                WalletCoinBean walletCoinBean = GsonUtil.getInstance().toObj(map.get(s.getCurrency()), WalletCoinBean.class);
                viewDelegate.viewHolder.tv_have.setText(
                        new BigDecimal(walletCoinBean.getAvi()).subtract(
                                new BigDecimal(walletCoinBean.getLock())
                        ).stripTrailingZeros().toPlainString() + s.getCurrency());
                break;
        }
    }

    TextWatcher textWatcher;

    private void initView() {
        BigDecimal divide = new BigDecimal(s.getRestAmount()).multiply(new BigDecimal("100"))
                .divide(new BigDecimal(s.getAllAmount()), 2, RoundingMode.DOWN);
        BigDecimal subtract = new BigDecimal("100").subtract(divide);

        viewDelegate.viewHolder.tv_progress.setText(
                "当前进度" + subtract.toPlainString() + "%"
        );
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewDelegate.viewHolder.view_start.getLayoutParams();
        layoutParams.weight = subtract.floatValue();
        viewDelegate.viewHolder.view_start.setLayoutParams(layoutParams);
        viewDelegate.viewHolder.view_start.setBackground(new RadiuBg(
                CommonUtils.getColor(R.color.color_25A73F),
                999, 999, 999, 999
        ));

        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) viewDelegate.viewHolder.view_end.getLayoutParams();
        layoutParams2.weight = divide.floatValue();
        viewDelegate.viewHolder.view_end.setLayoutParams(layoutParams2);
        BigDecimal divide1 = new BigDecimal(s.getAllMoney()).divide(new BigDecimal(s.getAllAmount()), 2, RoundingMode.DOWN);

        viewDelegate.viewHolder.tv_min.setText(divide1.toPlainString() + s.getCurrency());
        viewDelegate.viewHolder.tv_lass.setText(
                Html.fromHtml(
                        s.getRestAmount() +
                                "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) + "\"><small><small><small>" +
                                "(共" + divide1.multiply(new BigDecimal(s.getRestAmount())).toPlainString() + s.getCurrency() + ")"
                                + "</small></small></small></font>"
                ));

        viewDelegate.viewHolder.tv_content.setText(
                Html.fromHtml(
                        "当前项目" +
                                "<font color=\"" + CommonUtils.getStringColor(R.color.color_25A73F) + "\">" +
                                s.getAmountMin() + "</font>" +
                                "份起投，每份单价" +
                                new BigDecimal(s.getAllMoney()).divide(new BigDecimal(s.getAllAmount()), 2, RoundingMode.DOWN).toPlainString() +
                                s.getCurrency() +
                                ",单用户最多可投" +
                                "<font color=\"" + CommonUtils.getStringColor(R.color.color_25A73F) + "\">" +
                                s.getAmountMax() + "</font>" +
                                "份,目前总剩余" +
                                "<font color=\"" + CommonUtils.getStringColor(R.color.color_25A73F) + "\">" +
                                s.getRestAmount() + "</font>" +
                                "份。"
                )
        );
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable input) {
                if (UiHeplUtils.isDouble(input.toString())) {
                    int i = Integer.parseInt(input.toString());
                    if (i > Integer.parseInt(s.getAmountMin()) || i < Integer.parseInt(s.getAmountMax())) {
                        num = i;
                        calculation();
                    } else {
                        viewDelegate.viewHolder.tv_num.removeTextChangedListener(textWatcher);
                        viewDelegate.viewHolder.tv_num.setText(num + "");
                        viewDelegate.viewHolder.tv_num.addTextChangedListener(textWatcher);
                    }
                } else {
                    num = 0;
                }
            }
        };
        viewDelegate.viewHolder.tv_num.addTextChangedListener(textWatcher);
        num = Integer.parseInt(s.getAmountMin());
        viewDelegate.viewHolder.iv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > Integer.parseInt(s.getAmountMin())) {
                    num--;
                    calculation();
                }
            }
        });
        viewDelegate.viewHolder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num < Integer.parseInt(s.getAmountMax())) {
                    num++;
                    calculation();
                }
            }
        });
        viewDelegate.viewHolder.iv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelect = !isSelect;
                viewDelegate.viewHolder.iv_check.setImageDrawable(
                        CommonUtils.getDrawable(isSelect ? R.drawable.ic_check : R.drawable.ic_check_off)
                );
            }
        });

        calculation();
        viewDelegate.viewHolder.tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num == 0) {
                    ToastUtil.show("请输入数量");
                    return;
                }
                if (isSelect) {
                    addRequest(binder.followattend(id, num + "", FollowOrderFragment.this));
                } else {
                    ToastUtil.show("请同意6b.top 跟单协议");
                }
            }
        });
    }


    private void calculation() {
        viewDelegate.viewHolder.tv_num.removeTextChangedListener(textWatcher);
        viewDelegate.viewHolder.tv_num.setText(num + "");
        viewDelegate.viewHolder.tv_num.addTextChangedListener(textWatcher);
        String money = new BigDecimal(this.s.getAllMoney()).multiply(new BigDecimal("" + num)).divide(new BigDecimal(this.s.getAllAmount()), 2, RoundingMode.DOWN).toPlainString();
        viewDelegate.viewHolder.tv_follow_num.setText(
                "共" + money + s.getCurrency()
        );
        viewDelegate.viewHolder.tv_subtract_wallet.setText(
                "下单后自动将从您的6B钱包扣除" + money + s.getCurrency()
        );
    }


}
