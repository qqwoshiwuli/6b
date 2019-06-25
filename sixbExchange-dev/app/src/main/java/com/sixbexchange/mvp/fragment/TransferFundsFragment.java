package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.WalletCoinBean;
import com.sixbexchange.mvp.databinder.TransferFundsBinder;
import com.sixbexchange.mvp.delegate.TransferFundsDelegate;
import com.sixbexchange.mvp.dialog.LeverageDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransferFundsFragment extends BaseDataBindFragment<TransferFundsDelegate, TransferFundsBinder> {

    @Override
    protected Class<TransferFundsDelegate> getDelegateClass() {
        return TransferFundsDelegate.class;
    }

    @Override
    public TransferFundsBinder getDataBinder(TransferFundsDelegate viewDelegate) {
        return new TransferFundsBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

    }

    String content = "1.资金充值会在区块链网络确认后的20分钟内到账；\n" +
            "2.6B钱包与OKEx进行资金划转，0手续费，10秒内到账；\n" +
            "3.资金提现会在一个工作日内处理完成，当天18前的提现申请当天处理，18点后周六周日延后处理；\n" +
            "4.BitMEX为独立账户体系，开户需要0.001BTC，在正常交易3日后返还；\n" +
            "5.暂不支持BitMEX的资金划转，请直接向BitMEX地址充值或提现；\n" +
            "6.BitMEX的充值会在一个网络确认后20分钟内到账，提现会在工作日18点前处理完毕，每周日的提现延后处理；\n" ;

    public static TransferFundsFragment newInstance(
            String typeStr,
            String exchName,
            int exchPosition
    ) {
        TransferFundsFragment newFragment = new TransferFundsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("typeStr", typeStr);
        bundle.putString("exchName", exchName);
        bundle.putInt("exchPosition", exchPosition);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    String typeStr = "";
    String exchName = "";
    int exchPosition = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("typeStr", typeStr);
        outState.putString("exchName", exchName);
        outState.putInt("exchPosition", exchPosition);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        viewDelegate.viewHolder.tv_content.setText(content);
        if (savedInstanceState != null) {
            typeStr = savedInstanceState.getString("typeStr", "");
            exchName = savedInstanceState.getString("exchName", "");
            exchPosition = savedInstanceState.getInt("exchPosition");
        } else {
            typeStr = this.getArguments().getString("typeStr", "");
            exchName = this.getArguments().getString("exchName", "");
            exchPosition = this.getArguments().getInt("exchPosition");
        }
        if (ObjectUtils.equals(typeStr, "XBT")) {
            typeStr = "BTC";
        }
        viewDelegate.viewHolder.tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outExch == null) {
                    ToastUtil.show("请选择转出钱包");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_num.getText().toString())) {
                    ToastUtil.show("请输入转出数量");
                    return;
                }
                if (ObjectUtils.equals(inExch.get("name"), outExch.get("name"))) {
                    ToastUtil.show("不能选择相同交易所钱包");
                    return;
                }
                addRequest(binder.accounttrans(
                        outExch.get("position"),
                        exchPosition + 1 + "",
                        typeStr,
                        viewDelegate.viewHolder.tv_num.getText().toString(),
                        TransferFundsFragment.this
                ));
            }
        });
        inExchNames = new ArrayList<>();
        outExchNames = new ArrayList<>();
        intExchList = new ArrayList<>();
        outExchList = new ArrayList<>();
        data = CacheUtils.getInstance().getString(CacheName.ExchWalletCache);
        initCoins();
        getOutExchList();
        initExchList();
    }

    Map<String, String> inExch;
    LeverageDialog inExchDialog;

    private void getInExchList() {
//        for (int i = 0; i < intExchList.size(); i++) {
//            if (ObjectUtils.equals(exchPosition + 1 + "", intExchList.get(i).get("position"))) {
//                inExch = intExchList.get(i);
//                viewDelegate.viewHolder.tv_in.setText(inExchNames.get(i));
//            }
//        }
        viewDelegate.viewHolder.tv_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inExchDialog == null) {
                    inExchDialog = new LeverageDialog(getActivity(), new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            initExchList();
                            inExch = intExchList.get(position);
                            viewDelegate.viewHolder.tv_in.setText(inExchNames.get(position));
                            exchPosition = Integer.parseInt(intExchList.get(position).get("position")) - 1;
                            initCoins();
                            if (inExch!=null&&outExch!=null&&ObjectUtils.equals(inExch.get("name"), outExch.get("name"))) {
                                for (int i = 0; i < outExchNames.size(); i++) {
                                    if (!inExchNames.get(position).equals(outExchNames.get(i)))
                                    {
                                        outExch = outExchList.get(i);
                                        viewDelegate.viewHolder.tv_out.setText(outExchNames.get(i));

                                        WalletCoinBean walletCoinBean =
                                                GsonUtil.getInstance().toObj(outExch.get(typeStr),
                                                        WalletCoinBean.class);
                                        viewDelegate.viewHolder.tv_num.setHint("当前可用" +
                                                new BigDecimal(walletCoinBean.getWithdrawAvailable())
                                                        .stripTrailingZeros().toPlainString() + typeStr
                                        );
                                        return;
                                    }
                                }
                            }
                        }
                    });
                    inExchDialog.setSize(15);
                }
                inExchDialog.showDilaog(inExch != null ? inExch.get("name") + "钱包" : "", inExchNames);
            }
        });
    }

    String data;

    private void initCoins() {
        List<String> list = GsonUtil.getInstance().toList(
                data, String.class
        );
        Map<String, String> map = GsonUtil.getInstance().toMap(list.get(exchPosition),
                new TypeReference<Map<String, String>>() {
                });
        map.remove("name");
        map.remove("position");
        coins = new ArrayList<>();
        String coin = "";
        for (String key : map.keySet()) {
            if (ObjectUtils.equals(key, "XBT")) {
                coins.add("BTC");
            } else {
                coins.add(key);
            }
            if (typeStr.equals(key)) {
                coin = typeStr;
            }
        }
        if (TextUtils.isEmpty(coin)) {
            typeStr = coins.get(0);
        }

        viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
        viewDelegate.viewHolder.lin_select_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leverageDialog == null) {
                    leverageDialog = new LeverageDialog(getActivity(), new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            typeStr = coins.get(position);
                            viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
                            initExchList();
                            getInExchList();
                        }
                    });
                }
                leverageDialog.showDilaog(typeStr, coins);
            }
        });
    }

    List<Map<String, String>> intExchList;
    List<Map<String, String>> outExchList;
    Map<String, String> outExch;
    LeverageDialog outExchDialog;
    List<String> inExchNames;
    List<String> outExchNames;

    private void initExchList() {
        List<String> list = GsonUtil.getInstance().toList(
                data, String.class
        );
        intExchList.clear();
        outExchList.clear();
        inExchNames.clear();
        outExchNames.clear();
        boolean isHaveInExch = false;
        boolean isHaveOutExch = false;

        Map<String, String> map;
        for (int i = 0; i < list.size(); i++) {
            map = GsonUtil.getInstance().toMap(list.get(i),
                    new TypeReference<Map<String, String>>() {
                    });
            if (map.containsKey("XBT")) {
                map.put("BTC", map.get("XBT"));
                map.remove("XBT");
            }
            if (map.containsKey(typeStr)) {
                if (!map.get("name").toLowerCase().contains("bitmex")) {
                    if (!typeStr.toLowerCase().equals("usdt")||map.get("name").toLowerCase().contains("6b"))
                    {
                        outExchList.add(map);
                        outExchNames.add(map.get("name") + "钱包");
                    }
                    intExchList.add(map);
                    inExchNames.add(map.get("name") + "钱包");
                }
            }
            if (typeStr.toLowerCase().equals("usdt"))
            {
               ToastUtil.show("该用户暂无地址!");
            }
        }
        for (int i = 0; i < list.size(); i++) {
            map = GsonUtil.getInstance().toMap(list.get(i),
                    new TypeReference<Map<String, String>>() {
                    });
            if (map.containsKey("XBT")) {
                map.put("BTC", map.get("XBT"));
                map.remove("XBT");
            }
            if (inExch == null) {
                getInExchList();
            }
            if (map.containsKey(typeStr)) {

                if (inExch != null &&
                        ObjectUtils.equals(map.get("name"), inExch.get("name"))) {
                    isHaveInExch = true;
                }
                if (outExch != null && ObjectUtils.equals(map.get("name"), outExch.get("name"))) {
                    isHaveOutExch = true;
                }
            }
        }

        if (!isHaveInExch) {
            exchPosition = 0;
            initCoins();
            getInExchList();
        }
        if (!isHaveOutExch) {
            getOutExchList();
        }else {
            WalletCoinBean walletCoinBean = GsonUtil.getInstance().toObj(outExch.get(typeStr), WalletCoinBean.class);
            viewDelegate.viewHolder.tv_num.setHint("当前可用" +
                    walletCoinBean.getWithdrawAvailable() + typeStr
            );
        }
    }


    private void getOutExchList() {
        outExch = null;
//        viewDelegate.viewHolder.tv_num.setHint("请输入转出数量");
        viewDelegate.viewHolder.tv_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outExchDialog == null) {
                    outExchDialog = new LeverageDialog(getActivity(), new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            outExch = outExchList.get(position);
                            viewDelegate.viewHolder.tv_out.setText(outExchNames.get(position));
                            if (null!=inExch&&null!=outExch&&ObjectUtils.equals(inExch.get("name"), outExch.get("name"))) {
                                for (int i = 0; i < inExchNames.size(); i++) {
                                    if (!outExchNames.get(position).equals(inExchNames.get(i)))
                                    {
                                        inExch = intExchList.get(i);
                                        viewDelegate.viewHolder.tv_in.setText(inExchNames.get(i));
                                       return;
                                    }
                                }
                            }
                            WalletCoinBean walletCoinBean =
                                    GsonUtil.getInstance().toObj(outExch.get(typeStr),
                                            WalletCoinBean.class);
                            viewDelegate.viewHolder.tv_num.setHint("当前可用" +
                                    new BigDecimal(walletCoinBean.getWithdrawAvailable())
                                            .stripTrailingZeros().toPlainString() + typeStr
                            );
                        }
                    });
                    outExchDialog.setSize(15);
                }
                outExchDialog.showDilaog(outExch != null ?
                        outExch.get("name") + "钱包" : "", outExchNames);
            }
        });
    }


    List<String> coins;
    LeverageDialog leverageDialog;


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:

                break;
        }
    }

}
