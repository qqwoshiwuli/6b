package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.StringUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.entity.bean.CoinAddressBean;
import com.sixbexchange.entity.bean.WithdrawCoinBean;
import com.sixbexchange.mvp.databinder.WithdrawCoinBinder;
import com.sixbexchange.mvp.delegate.WithdrawCoinDelegate;
import com.sixbexchange.mvp.dialog.LeverageDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
*提现申请
* @author gqf
* @Description
* @Date  2019/4/3 0003 13:36
* @Param
* @return
**/
public class WithdrawCoinFragment extends BaseDataBindFragment<WithdrawCoinDelegate, WithdrawCoinBinder> {

    @Override
    protected Class<WithdrawCoinDelegate> getDelegateClass() {
        return WithdrawCoinDelegate.class;
    }

    @Override
    public WithdrawCoinBinder getDataBinder(WithdrawCoinDelegate viewDelegate) {
        return new WithdrawCoinBinder(viewDelegate);
    }

    String content = "1.资金充值会在区块链网络确认后的20分钟内到账；\n" +
            "2.6B钱包与OKEx进行资金划转，0手续费，10秒内到账；\n" +
            "3.资金提现会在一个工作日内处理完成，当天18前的提现申请当天处理，18点后周六周日延后处理；\n" +
            "4.BitMEX为独立账户体系，开户需要0.001BTC，在正常交易3日后返还；\n" +
            "5.暂不支持BitMEX的资金划转，请直接向BitMEX地址充值或提现；\n" +
            "6.BitMEX的充值会在一个网络确认后20分钟内到账，提现会在工作日18点前处理完毕，每周日的提现延后处理；\n" ;

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("提币"));
        viewDelegate.viewHolder.tv_content.setText(content);

        viewDelegate.viewHolder.tv_set_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(AddressManagementFragment.newInstance(typeStr, exchPosition), 0x123);
            }
        });
        viewDelegate.viewHolder.tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequest(binder.mobile(WithdrawCoinFragment.this));
            }
        });
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == 0x123) {
                addRequest(binder.extract(typeStr, this));
            }
        }
    }

    public static WithdrawCoinFragment newInstance(
            String typeStr,
            int exchPosition
    ) {
        WithdrawCoinFragment newFragment = new WithdrawCoinFragment();
        Bundle bundle = new Bundle();
        bundle.putString("typeStr", typeStr);
        bundle.putInt("exchPosition", exchPosition);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    String typeStr = "";
    int exchPosition = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("typeStr", typeStr);
        outState.putInt("exchPosition", exchPosition);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        viewDelegate.viewHolder.tv_pw.setText("");
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState != null) {
            typeStr = savedInstanceState.getString("typeStr", "");
            exchPosition = savedInstanceState.getInt("exchPosition");
        } else {
            typeStr = this.getArguments().getString("typeStr", "");
            exchPosition = this.getArguments().getInt("exchPosition");
        }

        viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
        addRequest(binder.getAccountDetail(this));
        addRequest(binder.extract(typeStr, this));
        addRequest(binder.getSingleBalance(typeStr, exchPosition + 1 + "", WithdrawCoinFragment.this));
        viewDelegate.viewHolder.tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectAddr == null) {
                    ToastUtil.show("请选择地址");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_num.getText().toString())) {
                    ToastUtil.show("请输入提币数量");
                    return;
                }
                if (!UiHeplUtils.isDouble(viewDelegate.viewHolder.tv_num.getText().toString())) {
                    ToastUtil.show("请输入正确的数字");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_code.getText().toString())) {
                    ToastUtil.show("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_pw.getText().toString())) {
                    ToastUtil.show("请输入资金密码");
                    return;
                }
                addRequest(binder.sendExtract(
                        (StringUtils.equalsIgnoreCase("eos", typeStr) ||
                                StringUtils.equalsIgnoreCase("xrp", typeStr)) ? selectAddr.getMemo() : "",
                        selectAddr.getAddr(),
                        typeStr,
                        viewDelegate.viewHolder.tv_num.getText().toString(),
                        withdrawCoinBean.getFee() + "",
                        viewDelegate.viewHolder.tv_code.getText().toString(),
                        exchPosition + 1 + "",
                        viewDelegate.viewHolder.tv_pw.getText().toString(),
                        WithdrawCoinFragment.this
                ));
            }

        });
        viewDelegate.viewHolder.lin_select_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coinAddressBeans == null) {
                    ToastUtil.show("未获取到地址");
                    return;
                }
                if (ListUtils.isEmpty(coinAddressBeans)) {
                    ToastUtil.show("您还没有当前币种的提币地址，请添加");
                    return;
                }
                addressDialog = new LeverageDialog(getActivity(), new DefaultClickLinsener() {
                    @Override
                    public void onClick(View view, int position, Object item) {
                        selectAddr = coinAddressBeans.get(position);
                        viewDelegate.viewHolder.tv_addr_name.setText(
                                selectAddr.getRemark()
                        );
                        viewDelegate.viewHolder.tv_addr.setText(
                                selectAddr.getAddr() +
                                        ((StringUtils.equalsIgnoreCase("eos", typeStr) ||
                                                StringUtils.equalsIgnoreCase("xrp", typeStr)) ?
                                                " : " + selectAddr.getMemo() : "")
                        );

                    }
                });

                String select = "";
                if (selectAddr != null) {
                    select = selectAddr.getRemark();
                }
                List<String> address = new ArrayList<>();
                for (int i = 0; i < coinAddressBeans.size(); i++) {
                    address.add(
                            coinAddressBeans.get(i).getRemark()
                    );
                }
                addressDialog.showDilaog(select, address);
            }
        });


    }

    List<String> coins;
    LeverageDialog leverageDialog;
    List<CoinAddressBean> coinAddressBeans;
    CoinAddressBean selectAddr;

    LeverageDialog addressDialog;
    WithdrawCoinBean withdrawCoinBean;

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                withdrawCoinBean = GsonUtil.getInstance().toObj(data, WithdrawCoinBean.class);
                coinAddressBeans = GsonUtil.getInstance().toList(data, "list", CoinAddressBean.class);
                viewDelegate.viewHolder.tv_content.setText(
                        "提币手续费" + withdrawCoinBean.getFee() + typeStr + "\n\n" +
                                content
                );
                break;
            case 0x124:
                List<String> list = GsonUtil.getInstance().toList(
                        data, String.class
                );
                Map<String, String> map = GsonUtil.getInstance().toMap(list.get(exchPosition),
                        new TypeReference<Map<String, String>>() {
                        });
                map.remove("name");
                map.remove("position");
                coins = new ArrayList<>();
                if (map.containsKey("XBT")) {
                    map.put("BTC", map.get("XBT"));
                    map.remove("XBT");
                }
                for (String key : map.keySet()) {
                    coins.add(key);
                }
                viewDelegate.viewHolder.lin_select_coin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (leverageDialog == null) {
                            leverageDialog = new LeverageDialog(getActivity(), new DefaultClickLinsener() {
                                @Override
                                public void onClick(View view, int position, Object item) {
                                    typeStr = coins.get(position);
                                    viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
                                    viewDelegate.viewHolder.tv_num.setText("");
                                    viewDelegate.viewHolder.tv_content.setText("");
                                    viewDelegate.viewHolder.tv_addr.setText("");
                                    viewDelegate.viewHolder.tv_code.setText("");
                                    viewDelegate.viewHolder.tv_pw.setText("");
                                    selectAddr = null;
                                    addRequest(binder.extract(typeStr, WithdrawCoinFragment.this));
                                    addRequest(binder.getSingleBalance(typeStr, exchPosition + 1 + "", WithdrawCoinFragment.this));
                                }
                            });
                        }
                        leverageDialog.showDilaog(typeStr, coins);
                    }
                });
                break;
            case 0x126:
                addRequest(binder.vcode(data, this));
                break;
            case 0x125:
                addRequest(UiHeplUtils.getCode(viewDelegate.viewHolder.tv_get_code, 60));
                break;
            case 0x128:
                viewDelegate.viewHolder.tv_num.setHint("当前可用" +
                        GsonUtil.getInstance().getValue(data, "withdrawAvailable") + typeStr);
                break;
        }
    }

}
