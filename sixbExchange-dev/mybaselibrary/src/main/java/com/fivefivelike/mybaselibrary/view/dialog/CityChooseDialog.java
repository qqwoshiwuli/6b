package com.fivefivelike.mybaselibrary.view.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.wheelview.CityChooseAdapter;
import com.wheelview.OnWheelChangedListener;
import com.wheelview.Wheel3DView;
import com.wheelview.WheelView;
import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.entity.AreaObj;
import com.fivefivelike.mybaselibrary.utils.AreaUtils;

import java.util.List;


public class CityChooseDialog extends BaseDialog implements OnWheelChangedListener {
    private OnChooseCityListener chooseListner;

    public CityChooseDialog(Activity context, OnChooseCityListener chooseListener) {
        super(context);
        this.chooseListner = chooseListener;
    }

    private Wheel3DView province;
    private Wheel3DView city;
    private Wheel3DView area;
    private List<AreaObj> proList;
    private List<AreaObj> cityList;
    private List<AreaObj> areaList;
    private TextView cancel;
    private TextView commit;
    private AreaObj proObj, cityObj, areaObj;

    @Override
    protected int getLayout() {
        return R.layout.dialog_citychoose;
    }

    @Override
    protected void startInit() {
        getWindow().setGravity(Gravity.BOTTOM);
        setWindowNoPadding();
        province = getView(R.id.wheel_province);
        city = getView(R.id.wheel_city);
        area = getView(R.id.wheel_area);
        int textsize = mContext.getResources().getDimensionPixelSize(R.dimen.trans_28px);
        province.setTextSize(textsize);
        city.setTextSize(textsize);
        area.setTextSize(textsize);
        cancel = getView(R.id.cancel);
        commit = getView(R.id.commit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                chooseListner.chooseBack(proObj, cityObj, areaObj);
            }
        });
        province.setOnWheelChangedListener(this);
        city.setOnWheelChangedListener(this);
        area.setOnWheelChangedListener(this);
        proList = AreaUtils.getInstance().getProList();
        if (proList != null && proList.size() > 0) {
            proObj = proList.get(0);
            province.setAdapter(new CityChooseAdapter(proList));
            province.setCurrentIndex(0);
            updateCities();
        }
    }

    private void updateAreas() {
        areaList = AreaUtils.getInstance().getAreaList(cityList.get(city.getCurrentIndex()).getAreaid());
        if (areaList != null && areaList.size() > 0) {
            areaObj = areaList.get(0);
        } else {
            areaObj = null;
        }
        area.setAdapter(new CityChooseAdapter(areaList));
        area.setCurrentIndex(0);
    }

    private void updateCities() {
        int current = province.getCurrentIndex();
        cityList = AreaUtils.getInstance().getCityList(proList.get(current).getAreaid());
        if (cityList != null && cityList.size() > 0) {
            cityObj = cityList.get(0);

        } else {
            cityObj = null;
        }
        city.setAdapter(new CityChooseAdapter(cityList));
        city.setCurrentIndex(0);
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == province) {
            updateCities();
            proObj = proList.get(newValue);
        } else if (wheel == city) {
            updateAreas();
            cityObj = cityList.get(newValue);
        } else if (wheel == area) {
            areaObj = areaList.get(newValue);
        }
    }

    public interface OnChooseCityListener {
        void chooseBack(AreaObj province, AreaObj city, AreaObj area);
    }
}
