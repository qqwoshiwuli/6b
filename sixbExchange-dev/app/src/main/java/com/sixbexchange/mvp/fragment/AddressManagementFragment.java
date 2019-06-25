package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.StringUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.adapter.CoinAddressAdapter;
import com.sixbexchange.entity.bean.CoinAddressBean;
import com.sixbexchange.mvp.databinder.AddressManagementBinder;
import com.sixbexchange.mvp.delegate.AddressManagementDelegate;
import com.sixbexchange.mvp.dialog.LeverageDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressManagementFragment extends BaseDataBindFragment<AddressManagementDelegate, AddressManagementBinder> {

    @Override
    protected Class<AddressManagementDelegate> getDelegateClass() {
        return AddressManagementDelegate.class;
    }

    @Override
    public AddressManagementBinder getDataBinder(AddressManagementDelegate viewDelegate) {
        return new AddressManagementBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("管理地址"));
        viewDelegate.viewHolder.tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequest(binder.mobile(AddressManagementFragment.this));
            }
        });
    }

    public static AddressManagementFragment newInstance(
            String typeStr,
            int exchPosition
    ) {
        AddressManagementFragment newFragment = new AddressManagementFragment();
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
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState != null) {
            typeStr = savedInstanceState.getString("typeStr", "");
            exchPosition = savedInstanceState.getInt("exchPosition");
        } else {
            typeStr = this.getArguments().getString("typeStr", "");
            exchPosition = this.getArguments().getInt("exchPosition");
        }
        viewDelegate.viewHolder.lin_memo.setVisibility((StringUtils.equalsIgnoreCase("eos", typeStr) ||
                StringUtils.equalsIgnoreCase("xrp", typeStr)) ? View.VISIBLE : View.GONE);
        viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
        viewDelegate.viewHolder.tv_already.setText("已添加的" + typeStr + "地址");
        addRequest(binder.getAccountDetail(this));
        addRequest(binder.extractAddr(typeStr, this));
        initList(new ArrayList<CoinAddressBean>());
        viewDelegate.viewHolder.tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_addr.getText().toString())) {
                    ToastUtil.show("请输入地址");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_num.getText().toString())) {
                    ToastUtil.show("请输入备注");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_code.getText().toString())) {
                    ToastUtil.show("请输入验证码");
                    return;
                }
                if ((StringUtils.equalsIgnoreCase("eos", typeStr) ||
                        StringUtils.equalsIgnoreCase("xrp", typeStr)) &&
                        TextUtils.isEmpty(viewDelegate.viewHolder.tv_memo.getText().toString())) {
                    ToastUtil.show("请输入memo");
                    return;
                }
                addRequest(binder.addExtractAddr(
                        viewDelegate.viewHolder.tv_addr.getText().toString(),
                        typeStr,
                        viewDelegate.viewHolder.tv_num.getText().toString(),
                        viewDelegate.viewHolder.tv_memo.getText().toString(),
                        viewDelegate.viewHolder.tv_code.getText().toString(),
                        AddressManagementFragment.this
                ));
            }
        });

    }

    List<String> coins;
    LeverageDialog leverageDialog;

    CoinAddressAdapter addressAdapter;

    private void initList(List<CoinAddressBean> data) {
        if (addressAdapter == null) {
            addressAdapter = new CoinAddressAdapter(getActivity(), data);
            addressAdapter.setDefaultClickLinsener(new DefaultClickLinsener() {
                @Override
                public void onClick(View view, int position, Object item) {
                    addRequest(binder.delExtractAddr(addressAdapter.getDatas().get(position).getId()
                            + "", AddressManagementFragment.this));
                }
            });
            viewDelegate.viewHolder.recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewDelegate.viewHolder.recycler_view.setAdapter(addressAdapter);
        } else {
            addressAdapter.setData(data);
        }
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                initList(GsonUtil.getInstance().toList(data, CoinAddressBean.class));
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
                                    viewDelegate.viewHolder.lin_memo.setVisibility((StringUtils.equalsIgnoreCase("eos", typeStr) ||
                                            StringUtils.equalsIgnoreCase("xrp", typeStr)) ? View.VISIBLE : View.GONE);
                                    viewDelegate.viewHolder.tv_already.setText("已添加的" + typeStr + "地址");
                                    addRequest(binder.extractAddr(typeStr, AddressManagementFragment.this));
                                }
                            });
                        }
                        leverageDialog.showDilaog(typeStr, coins);
                    }
                });
                break;
            case 0x125:
                setFragmentResult(RESULT_OK, null);
                addRequest(binder.extractAddr(typeStr, this));
                break;
            case 0x126:
                setFragmentResult(RESULT_OK, null);
                addRequest(binder.extractAddr(typeStr, this));
                break;
            case 0x127:
                addRequest(UiHeplUtils.getCode(viewDelegate.viewHolder.tv_get_code, 60));
                break;
            case 0x128:
                addRequest(binder.vcode(data, this));
                break;
        }
    }
}
