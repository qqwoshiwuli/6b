package com.sixbexchange.mvp.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.StringUtils;
import com.circledialog.CircleDialogHelper;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.mvp.databinder.RechargeAddressBinder;
import com.sixbexchange.mvp.delegate.RechargeAddressDelegate;
import com.sixbexchange.mvp.dialog.LeverageDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/*
*充值地址
* @author gqf
* @Description
* @Date  2019/4/3 0003 11:17
* @Param
* @return
**/
public class RechargeAddressFragment extends BaseDataBindFragment<RechargeAddressDelegate, RechargeAddressBinder> {

    @Override
    protected Class<RechargeAddressDelegate> getDelegateClass() {
        return RechargeAddressDelegate.class;
    }

    @Override
    public RechargeAddressBinder getDataBinder(RechargeAddressDelegate viewDelegate) {
        return new RechargeAddressBinder(viewDelegate);
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


    public static RechargeAddressFragment newInstance(
            String typeStr,
            int exchPosition
    ) {
        RechargeAddressFragment newFragment = new RechargeAddressFragment();
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
    protected void bindEvenListenerBuyState(Bundle savedInstanceState) {
        super.bindEvenListenerBuyState(savedInstanceState);
        if (savedInstanceState != null) {
            typeStr = savedInstanceState.getString("typeStr", "");
            exchPosition = savedInstanceState.getInt("exchPosition");
        } else {
            typeStr = this.getArguments().getString("typeStr", "");
            exchPosition = this.getArguments().getInt("exchPosition");
        }
        viewDelegate.viewHolder.tv_content.setText(content);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        addRequest(binder.addrinfo(typeStr, exchPosition, this));
        addRequest(binder.getAccountDetail(this));
        viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

    }

    boolean isShow = false;

    public void showDialog() {
        if (!isShow && (StringUtils.equalsIgnoreCase("eos", typeStr) ||
                StringUtils.equalsIgnoreCase("xrp", typeStr))) {
            isShow = true;
            CircleDialogHelper.initDefaultDialog(getActivity(),
                    typeStr + "充值需要输入MEMO,否则不会到账", null)
                    .setNegative(CommonUtils.getString(R.string.str_cancel), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getActivity().onBackPressed();
                        }
                    }).show();
        }
    }


    List<String> coins;
    String addr;
    String remark;
    String notice;
    LeverageDialog leverageDialog;

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                addr = GsonUtil.getInstance().getValue(data, "addr");
                remark = GsonUtil.getInstance().getValue(data, "remark");
                notice = GsonUtil.getInstance().getValue(data, "notice");
                viewDelegate.viewHolder.lin_memo.setVisibility(TextUtils.isEmpty(remark) ? View.GONE : View.VISIBLE);
                viewDelegate.viewHolder.tv_address.setText(addr + "");
                viewDelegate.viewHolder.tv_memo.setText(remark + "");
                viewDelegate.viewHolder.tv_copy_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UiHeplUtils.copy(getActivity(), addr, false);
                    }
                });
                viewDelegate.viewHolder.tv_copy_memo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UiHeplUtils.copy(getActivity(), remark, false);
                    }
                });
                Observable.create(
                        new ObservableOnSubscribe<Bitmap>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
                                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(
                                        addr,
                                        400,
                                        CommonUtils.getColor(R.color.black),
                                        ((BitmapDrawable) getResources().getDrawable(R.mipmap.artboard)).getBitmap()
                                );
                                e.onNext(bitmap);
                                e.onComplete();
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onTerminateDetach()
                        .subscribe(
                                new Consumer<Bitmap>() {
                                    @Override
                                    public void accept(Bitmap bitmap) throws Exception {
                                        viewDelegate.viewHolder.iv_address.setImageBitmap(bitmap);
                                    }
                                }
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
                                    viewDelegate.viewHolder.iv_address.setImageDrawable(null);
                                    viewDelegate.viewHolder.tv_content.setText("");
                                    viewDelegate.viewHolder.tv_address.setText("");
                                    viewDelegate.viewHolder.tv_memo.setText("");
                                    viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
                                    addRequest(binder.addrinfo(typeStr, exchPosition, RechargeAddressFragment.this));
                                }
                            });
                        }
                        leverageDialog.showDilaog(typeStr, coins);
                    }
                });
                break;
            case 0x125:

                break;
        }
    }

}
