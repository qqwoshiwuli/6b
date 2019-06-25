package com.sixbexchange.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.WsOrderBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 郭青枫 on 2018/1/10 0010.
 * 用户订单
 */

public class WsTrBMOrdersAdapter extends BaseAdapter<WsOrderBean> {


    DefaultClickLinsener defaultClickLinsener;
    private TextView tv_type;
    private TextView tv_name;
    private RoundButton tv_close;
    private TextView tv_open_amount;
    private TextView tv_open_price;
    private TextView tv_hold_amount;
    private TextView tv_margin;

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public WsTrBMOrdersAdapter(Context context, List<WsOrderBean> datas) {
        super(context, R.layout.adapter_tr_order, datas);

    }


    public void setWsData(WsOrderBean data) {
        for (int i = 0; i < getDatas().size(); i++) {
            if (ObjectUtils.equals(getDatas().get(i).getExchange_oid(), data.getExchange_oid())) {
                if (ObjectUtils.equals("2", data.getState())) {
                    getDatas().remove(i);
                    notifyDataSetChanged();
                } else {
                    getDatas().set(i, data);
                    notifyItemChanged(i);
                }
                return;
            }
        }
        getDatas().add(data);
        notifyDataSetChanged();
    }

    public void removeOrder(String orderId) {
        for (int i = 0; i < getDatas().size(); i++) {
            if (ObjectUtils.equals(orderId, getDatas().get(i).getExchange_oid())) {
                getDatas().remove(i);
                notifyDataSetChanged();
                break;
            }
        }
        if (createOrderId.contains(orderId)) {
            createOrderId.remove(orderId);
        }
    }

    public List<String> createOrderId;

    public Disposable addNewOrder(WsOrderBean data) {
        getDatas().add(0, data);
        notifyDataSetChanged();
        createOrderId.add(data.getExchange_oid());
        return removeOrderDisposable(data);
    }

    public Disposable removeOrderDisposable(WsOrderBean data) {
        Disposable subscribe = Observable.just(data).delay(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .subscribe(new Consumer<WsOrderBean>() {
                    @Override
                    public void accept(WsOrderBean s) throws Exception {
                        removeOrder(s.getExchange_oid());
                    }
                });
        return subscribe;
    }

    @Override
    protected void convert(ViewHolder holder, WsOrderBean s, final int position) {
        tv_name = holder.getView(R.id.tv_name);
        tv_type = holder.getView(R.id.tv_type);
        tv_open_amount = holder.getView(R.id.tv_open_amount);
        tv_hold_amount = holder.getView(R.id.tv_hold_amount);
        tv_open_price = holder.getView(R.id.tv_open_price);
        tv_close = holder.getView(R.id.tv_close);
        tv_margin = holder.getView(R.id.tv_margin);

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, null);
            }
        });

        tv_name.setText(s.getCurrencyPairName());


        if (ObjectUtils.equals("b", s.getBs())) {
            tv_type.setText("买入");
            tv_type.setBackground(new RadiuBg(
                    CommonUtils.getColor(UserSet.getinstance().getRiseColor()),
                    5, 5, 5, 5
            ));
        } else if (ObjectUtils.equals("s", s.getBs())) {
            tv_type.setText("卖出");
            tv_type.setBackground(new RadiuBg(
                    CommonUtils.getColor(UserSet.getinstance().getDropColor()),
                    5, 5, 5, 5
            ));
        }

        tv_open_amount.setText(BigUIUtil.getinstance().bigAmount(s.getEntrust_amount()));
        tv_hold_amount.setText(BigUIUtil.getinstance().bigAmount(s.getDealt_amount()));


        tv_open_price.setText(BigUIUtil.getinstance().getSymbol(s.getPriceUnit()) + " " +
                BigUIUtil.getinstance().bigPrice(s.getEntrust_price()));


    }

}