package com.sixbexchange.mvp.fragment.transaction.okex;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.circledialog.CircleDialogHelper;
import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BasePullFragment;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.sixbexchange.R;
import com.sixbexchange.adapter.OkexHoldPositionAdapter;
import com.sixbexchange.entity.bean.ExchSelectPositionBean;
import com.sixbexchange.entity.bean.HoldPositionBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.entity.bean.UserLoginInfo;
import com.sixbexchange.mvp.databinder.BaseFragmentPullBinder;
import com.sixbexchange.mvp.delegate.BaseFragentPullDelegate;
import com.sixbexchange.mvp.dialog.ClosePositionDialog;
import com.sixbexchange.mvp.dialog.LevelDialog;
import com.sixbexchange.mvp.fragment.TransactionFragment;
import com.sixbexchange.utils.BigUIUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OkexTrPositionFragment extends BasePullFragment<BaseFragentPullDelegate, BaseFragmentPullBinder> {

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
        initList(new ArrayList<HoldPositionBean>());
    }

    OkexHoldPositionAdapter adapter;

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (adapter != null && tradeDetailBean != null) {
            onRefresh();
        }
    }

    LevelDialog levelDialog;
    int selectPosition = 0;
    ClosePositionDialog closePositionDialog;

    private void initList(List<HoldPositionBean> data) {
        if (adapter == null) {
            adapter = new OkexHoldPositionAdapter(getActivity(), data);
            adapter.setDefaultClickLinsener(new DefaultClickLinsener() {
                @Override
                public void onClick(View view, int p, Object item) {
                    selectPosition = p;
                    if (view.getId() == R.id.tv_change) {
                        if (levelDialog == null) {
                            levelDialog = new LevelDialog(getActivity(), new DefaultClickLinsener() {
                                @Override
                                public void onClick(View view, int position, Object item) {
                                    addRequest(binder.changeLeverage(
                                            tradeDetailBean.getExchange(),
                                            adapter.getDatas().get(selectPosition).getContract(),
                                            ((String) item).replace("x", ""),
                                            OkexTrPositionFragment.this
                                    ));
                                }
                            });
                        }
                        levelDialog.showDilaog(
                                adapter.getDatas().get(p).getLever_rate());
                    } else if (view.getId() == R.id.tv_close) {
//                        if (closePositionDialog == null) {
//                            closePositionDialog = new ClosePositionDialog(getActivity(), new DefaultClickLinsener() {
//                                @Override
//                                public void onClick(View view, int position, Object item) {
//                                    if (position == 0) {
//                                        CircleDialogHelper.initDefaultDialog(
//                                                getActivity(),
//                                                "确定市价全平" + "\n" +
//                                                        adapter.getDatas().get(selectPosition).getDetail().getContractName() +
//                                                        (adapter.getDatas().get(selectPosition).getTotalAmount().contains("-")
//                                                                ? "空头" : "多头") +
//                                                        (adapter.getDatas().get(selectPosition).getDetail().getLever_rate() + "X"),
//                                                new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        addRequest(binder.placeOrder(
//                                                                tradeDetailBean.getExchange(),
//                                                                "1",
//                                                                "",
//                                                                adapter.getDatas().get(selectPosition).getTotalAmount().contains("-") ? 6 : 5,
//                                                                adapter.getDatas().get(selectPosition).getContract(),
//                                                                adapter.getDatas().get(selectPosition).getContract(),
//                                                                adapter.getDatas().get(selectPosition).getAvailable().replace("-", ""),
//                                                                adapter.getDatas().get(selectPosition).getTotalAmount().contains("-") ? "b" : "s",
//                                                                OkexTrPositionFragment.this
//                                                        ));
//                                                    }
//                                                }
//                                        ).show();
//                                    } else if (position == 1) {
//                                        String[] info = ((String) item).split("/");
//                                        addRequest(binder.placeOrder(
//                                                tradeDetailBean.getExchange(),
//                                                "0",
//                                                info[0],
//                                                adapter.getDatas().get(selectPosition).getTotalAmount().contains("-") ? 4 : 3,
//                                                adapter.getDatas().get(selectPosition).getContract(),
//                                                adapter.getDatas().get(selectPosition).getContract(),
//                                                info[1],
//                                                adapter.getDatas().get(selectPosition).getTotalAmount().contains("-") ? "b" : "s",
//                                                OkexTrPositionFragment.this
//                                        ));
//                                    }
//                                }
//                            });
//                        }
                        CircleDialogHelper.initDefaultDialog(
                                getActivity(),
                                "确定市价全平" + "\n" +
                                        adapter.getDatas().get(selectPosition).getDetail().getContractName() +
                                        (adapter.getDatas().get(selectPosition).getTotalAmount().contains("-")
                                                ? "空头" : "多头") +
                                        (adapter.getDatas().get(selectPosition).getDetail().getLever_rate() + "X"),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addRequest(binder.placeOrder(
                                                tradeDetailBean.getExchange(),
                                                "1",
                                                "",
                                                adapter.getDatas().get(selectPosition).getTotalAmount().contains("-") ? 6 : 5,
                                                adapter.getDatas().get(selectPosition).getContract(),
                                                adapter.getDatas().get(selectPosition).getContract(),
                                                adapter.getDatas().get(selectPosition).getAvailable().replace("-", ""),
                                                adapter.getDatas().get(selectPosition).getTotalAmount().contains("-") ? "b" : "s",
                                                OkexTrPositionFragment.this
                                        ));
                                    }
                                }
                        ).show();
//                        closePositionDialog.showDialog(adapter.getDatas().get(p));
                    } else {
                        UiHeplUtils.shareImgViewBySdk(getActivity(),
                                share(
                                        adapter.getDatas().get(p).getUnrealizedRate(),
                                        new BigDecimal(adapter.getDatas().get(p).getTotalAmount()).doubleValue() >= 0 ?
                                                "多头 " + "20X" : "空头 " + "20X",
                                        adapter.getDatas().get(p).getDetail().getContractName(),
                                        adapter.getDatas().get(p).getCurrentPrice(),
                                        adapter.getDatas().get(p).getAverageOpenPrice(),
                                        adapter.getDatas().get(p).getDetail().getPriceUnit()
                                ), null
                        );
                    }
                }
            });
            viewDelegate.viewHolder.pull_recycleview.setBackgroundColor(CommonUtils.getColor(R.color.base_mask));
            initHeader();
            initRecycleViewPull(adapter, new LinearLayoutManager(getActivity()));
        } else {
            adapter.setTradeDetailBean(tradeDetailBean);
            getDataBack(adapter.getDatas(), data, adapter);
        }
    }

    ImageView iv_bottom;

    private View share(String inComeRate, String type, String coin, String nowPrice, String avgPrice, String unit) {
        ImageView iv_bg;
        TextView tv_type;
        TextView tv_share_coin;
        TextView tv_in_come_rate;
        TextView tv_position_now_price;
        TextView tv_opening_avg_price;
        View rootView = getLayoutInflater().inflate(R.layout.layout_share_in_come, null);
        iv_bg = rootView.findViewById(R.id.iv_bg);
        tv_type = rootView.findViewById(R.id.tv_type);
        tv_share_coin = rootView.findViewById(R.id.tv_share_coin);
        tv_in_come_rate = rootView.findViewById(R.id.tv_in_come_rate);
        tv_position_now_price = rootView.findViewById(R.id.tv_position_now_price);
        tv_opening_avg_price = rootView.findViewById(R.id.tv_opening_avg_price);
        iv_bottom = rootView.findViewById(R.id.iv_bottom);
        if (new BigDecimal(inComeRate).doubleValue() >= 0) {
            GlideUtils.loadImage("https://mapp.bicoin.info/6b/shouyi/6bshare-yinli.png",
                    iv_bg, GlideUtils.getNoCacheRO());
            tv_in_come_rate.setText(Html.fromHtml(
                    "<small><small>+</small></small>" +
                            inComeRate +
                            "<small><small>%</small></small>"
            ));
            tv_type.setBackground(new RadiuBg(
                    CommonUtils.getColor(R.color.decreasing_color),
                    5, 5, 5, 5
            ));
            tv_in_come_rate.setTextColor(CommonUtils.getColor(R.color.decreasing_color));
        } else {
            GlideUtils.loadImage("https://mapp.bicoin.info/6b/shouyi/6bshare-kuisun.png",
                    iv_bg, GlideUtils.getNoCacheRO());
            tv_in_come_rate.setText(Html.fromHtml(
                    "<small><small>-</small></small>" +
                            new BigDecimal(inComeRate).multiply(new BigDecimal("-1")).toPlainString() +
                            "<small><small>%</small></small>"
            ));
            tv_type.setBackground(new RadiuBg(
                    CommonUtils.getColor(R.color.increasing_color),
                    5, 5, 5, 5
            ));
            tv_in_come_rate.setTextColor(CommonUtils.getColor(R.color.increasing_color));
        }
        Observable.create(
                new ObservableOnSubscribe<Bitmap>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
                        Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(
                                "https://blz.bicoin.com.cn/web/modules/6BDownload.html?intiviteCode="
                                        + UserLoginInfo.getLoginInfo().getIcode(),
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
                                iv_bottom.setImageBitmap(bitmap);
                            }
                        }
                );
        tv_type.setText(type);
        tv_share_coin.setText(coin);
        tv_position_now_price.setText(BigUIUtil.getinstance().getSymbol(unit) + BigUIUtil.getinstance().bigPrice(nowPrice));
        tv_opening_avg_price.setText(BigUIUtil.getinstance().getSymbol(unit) + BigUIUtil.getinstance().bigPrice(avgPrice));
        return rootView;
    }

    public IconFontTextview tv_coin;


    private void initHeader() {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_position_header, null);
        this.tv_coin = (IconFontTextview) rootView.findViewById(R.id.tv_coin);
        tv_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下拉选择币种
                ((OkexTrParentsFragment) getParentFragment()).showSelectCoins(v,
                        selectPositionBean.getName());
            }
        });
        viewDelegate.viewHolder.fl_pull.addView(rootView, 0);
    }

    ExchSelectPositionBean selectPositionBean;
    TradeDetailBean tradeDetailBean;

    public void initTradeDetail(ExchSelectPositionBean s, TradeDetailBean t) {
        //请求新数据 获取新的币种列表
        selectPositionBean = s;
        tradeDetailBean = t;
        tv_coin.setText(s.getName() + " " + CommonUtils.getString(R.string.ic_Down));
        initList(new ArrayList<HoldPositionBean>());
        onRefresh();
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                List<HoldPositionBean> holdPositionBeans = GsonUtil.getInstance().toList(data, HoldPositionBean.class);
                initList(holdPositionBeans);
                break;
            case 0x125:
                //平仓
                tv_coin.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefresh();
                    }
                }, 500);
                break;
            case 0x127:
                //更新杠杆
                onRefresh();
                //刷新交易页面
                ((TransactionFragment) getParentFragment()).initTrTransaction();
                break;
        }
    }

    @Override
    protected void refreshData() {
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(true);
        addRequest(binder.getCoinPosition(
                tradeDetailBean.getExchange(),
                selectPositionBean.getCurrency(),
                this));
    }
}
