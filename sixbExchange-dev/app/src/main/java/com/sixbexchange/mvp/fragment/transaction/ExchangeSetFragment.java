package com.sixbexchange.mvp.fragment.transaction;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.circledialog.CircleDialogHelper;
import com.circledialog.callback.ConfigInput;
import com.circledialog.params.InputParams;
import com.circledialog.view.listener.OnInputClickListener;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.sixbexchange.R;
import com.sixbexchange.mvp.databinder.ExchangeSetBinder;
import com.sixbexchange.mvp.delegate.ExchangeSetDelegate;
import com.sixbexchange.utils.UserSet;

import java.math.BigDecimal;

public class ExchangeSetFragment extends BaseDataBindFragment<ExchangeSetDelegate, ExchangeSetBinder> {

    @Override
    protected Class<ExchangeSetDelegate> getDelegateClass() {
        return ExchangeSetDelegate.class;
    }

    @Override
    public ExchangeSetBinder getDataBinder(ExchangeSetDelegate viewDelegate) {
        return new ExchangeSetBinder(viewDelegate);
    }


    public void setExchPosition(int exchPosition) {
        viewDelegate.viewHolder.lin_bitmex.setVisibility(exchPosition == 1 ? View.VISIBLE : View.GONE);
        viewDelegate.viewHolder.lin_okex.setVisibility(exchPosition == 0 ? View.VISIBLE : View.GONE);
    }

    int selectOverLoad = 1;
    int selectLeafOrCoin = 0;//0:币 1：张

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("设置中心"));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewDelegate.getmToolbarTitle().getLayoutParams();
        layoutParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        layoutParams.rightMargin = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_40px);
        viewDelegate.getmToolbarTitle().setLayoutParams(layoutParams);
        viewDelegate.getmToolbarBack().setText(CommonUtils.getString(R.string.ic_seting1));
        viewDelegate.getmToolbarBackLin().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TrParentsFragment) getParentFragment()).closeDrawer();
            }
        });
        viewDelegate.viewHolder.lin_select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOverLoad = 1;
                setSelectOverLoad();
            }
        });
        viewDelegate.viewHolder.lin_select3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOverLoad = 0;
                setSelectOverLoad();
            }
        });
        viewDelegate.viewHolder.lin_leaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLeafOrCoin = 1;
                setLeafOrCoin();
            }
        });
        viewDelegate.viewHolder.lin_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLeafOrCoin = 0;
                setLeafOrCoin();
            }
        });

        addRequest(binder.getConfig(this));

        selectOverLoad = Integer.parseInt(UserSet.getinstance().getOverloadType());
        setSelectOverLoad();
        selectLeafOrCoin = Integer.parseInt(UserSet.getinstance().getLeafOrCoin());
        setLeafOrCoin();

        viewDelegate.viewHolder.et_overload_time.setText(UserSet.getinstance().getOverloadTime());
        viewDelegate.viewHolder.et_overload_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircleDialogHelper.initDefaultInputDialog(getActivity(), "设置下单次数", "请输入下单次数", "确定", new OnInputClickListener() {
                    @Override
                    public void onClick(String s, View v) {
                        if (TextUtils.isEmpty(s.toString())) {
                            ToastUtil.show("请输入1-100之间的数字,否则默认为15次");
                            UserSet.getinstance().setOverloadTime("15");
                        } else {
                            if (UiHeplUtils.isDouble(s.toString())) {
                                if (Integer.parseInt(s.toString()) > 100 || Integer.parseInt(s.toString()) <= 0) {
                                    ToastUtil.show("请输入1-100之间的数字,否则默认为15次");
                                    UserSet.getinstance().setOverloadTime("15");
                                } else {
                                    UserSet.getinstance().setOverloadTime(s.toString());
                                }
                            } else {
                                ToastUtil.show("请输入1-100之间的数字,否则默认为15次");
                                UserSet.getinstance().setOverloadTime("15");
                            }
                        }
                        viewDelegate.viewHolder.et_overload_time.setText(UserSet.getinstance().getOverloadTime());
                        addRequest(binder.saveConfig(
                                UserSet.getinstance().getOverloadType(),
                                UserSet.getinstance().getOverloadTime(),
                                UserSet.getinstance().getHandPrice(),
                                ExchangeSetFragment.this
                        ));
                    }
                }).configInput(new ConfigInput() {

                    @Override
                    public void onConfig(InputParams params) {
                        params.defaultText = UserSet.getinstance().getOverloadTime();
                    }
                }).show();
            }
        });
        viewDelegate.viewHolder.et_price.setText(UserSet.getinstance().getHandPrice());
        viewDelegate.viewHolder.et_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircleDialogHelper.initDefaultInputDialog(getActivity(), "设置市价比例", "请输入市价比例", "确定", new OnInputClickListener() {
                    @Override
                    public void onClick(String s, View v) {
                        if (TextUtils.isEmpty(s.toString())) {
                            ToastUtil.show("请输入偏离价,否则默认为0.5%");
                            UserSet.getinstance().setHandPrice("0.5");
                        } else {
                            if (UiHeplUtils.isDouble(s.toString())) {
                                if (new BigDecimal(s.toString()).doubleValue() > 100 ||
                                        new BigDecimal(s.toString()).doubleValue() <= 0) {
                                    ToastUtil.show("请输入偏离价,否则默认为0.5%");
                                    UserSet.getinstance().setHandPrice("0.5");
                                } else {
                                    UserSet.getinstance().setHandPrice(s.toString());
                                }
                            } else {
                                UserSet.getinstance().setHandPrice("0.5");
                            }
                        }
                        viewDelegate.viewHolder.et_price.setText(UserSet.getinstance().getHandPrice());
                        addRequest(binder.saveConfig(
                                UserSet.getinstance().getOverloadType(),
                                UserSet.getinstance().getOverloadTime(),
                                UserSet.getinstance().getHandPrice(),
                                ExchangeSetFragment.this
                        ));
                    }
                }).configInput(new ConfigInput() {

                    @Override
                    public void onConfig(InputParams params) {
                        params.defaultText = UserSet.getinstance().getHandPrice();
                    }
                }).show();
            }
        });


    }

    private void setLeafOrCoin() {
        if (selectLeafOrCoin == 1) {
            viewDelegate.viewHolder.iv_leaf.setImageDrawable(CommonUtils.getDrawable(R.drawable.ic_radio_select));
            viewDelegate.viewHolder.iv_coin.setImageDrawable(CommonUtils.getDrawable(R.drawable.ic_check_off));
        } else {
            viewDelegate.viewHolder.iv_leaf.setImageDrawable(CommonUtils.getDrawable(R.drawable.ic_check_off));
            viewDelegate.viewHolder.iv_coin.setImageDrawable(CommonUtils.getDrawable(R.drawable.ic_radio_select));
        }
        addRequest(binder.setOkexSetting(selectLeafOrCoin + "", this));
    }

    private void setSelectOverLoad() {
        viewDelegate.viewHolder.iv_select2.setVisibility(View.INVISIBLE);
        viewDelegate.viewHolder.iv_select3.setVisibility(View.INVISIBLE);
        viewDelegate.viewHolder.et_overload_time.setEnabled(false);
        if (selectOverLoad == 1) {
            viewDelegate.viewHolder.et_overload_time.setEnabled(true);
            viewDelegate.viewHolder.iv_select2.setVisibility(View.VISIBLE);
            UserSet.getinstance().setOverloadType("1");
        } else if (selectOverLoad == 0) {
            viewDelegate.viewHolder.iv_select3.setVisibility(View.VISIBLE);
            UserSet.getinstance().setOverloadType("0");
        }

    }

    @Override
    protected void onServiceError(String data, String info, int status, int requestCode) {
        super.onServiceError(data, info, status, requestCode);
        switch (requestCode) {
            case 0x124:
                selectLeafOrCoin = Integer.parseInt(UserSet.getinstance().getLeafOrCoin());
                setLeafOrCoin();
                break;
        }
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                UserSet.getinstance().setOverloadType(GsonUtil.getInstance().getValue(data, "dealFailType"));
                setSelectOverLoad();
                UserSet.getinstance().setOverloadTime(GsonUtil.getInstance().getValue(data, "continueTime"));
                viewDelegate.viewHolder.et_overload_time.setText(UserSet.getinstance().getOverloadTime());
                UserSet.getinstance().setHandPrice(GsonUtil.getInstance().getValue(data, "radio"));
                viewDelegate.viewHolder.et_price.setText(UserSet.getinstance().getHandPrice());
                break;
            case 0x124:
                UserSet.getinstance().setLeafOrCoin(selectLeafOrCoin + "");
                //通知交易页面刷新
                ((TrParentsFragment) getParentFragment()).refreshOkex();
                break;
        }
    }

}
