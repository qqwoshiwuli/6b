package com.fivefivelike.mybaselibrary.view.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.DateUtils;
import com.fivefivelike.mybaselibrary.utils.StringUtil;
import com.wheelview.NumberWheelAdapter;
import com.wheelview.OnWheelChangedListener;
import com.wheelview.WheelView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 郭青枫 on 2016/11/16.
 * 个人资料
 */

public class BirthdayDialog extends BaseDialog implements OnWheelChangedListener {
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private String yearStr, monthStr, dayStr;
    private  OnTimeChooseListener listener;
    public BirthdayDialog(Activity context, OnTimeChooseListener listener) {
        super(context);
        this.listener=listener;
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_birthday;
    }

    @Override
    protected void startInit() {
        getWindow().setGravity(Gravity.BOTTOM);
        setWindowNoPadding();
        year = (WheelView) findViewById(R.id.year);
        month = (WheelView) findViewById(R.id.month);
        day = (WheelView) findViewById(R.id.day);
        int textsize=  mContext.getResources().getDimensionPixelSize(R.dimen.trans_28px);
        year.setTextSize(textsize);
        month.setTextSize(textsize);
        day.setTextSize(textsize);
        year.setVisibleItems(7);
        month.setVisibleItems(7);
        day.setVisibleItems(7);
        int currentyear = Integer.parseInt(DateUtils.getCurrYear());
        int currMonth = DateUtils.getCurrMonth();
        int currDay = DateUtils.getCurrDay();
        year.setAdapter(new NumberWheelAdapter(1900, currentyear,"%s年"));
        month.setAdapter(new NumberWheelAdapter(1, 12, "%02d月"));
        year.setOnClickListener(this);
        month.setOnClickListener(this);
        year.setCurrentIndex(currentyear - 1900);
        month.setCurrentIndex(currMonth - 1);
        yearStr = year.getAdapter().getItem(currentyear - 1900);
        monthStr = month.getAdapter().getItem(currMonth - 1);
        day.setOnClickListener(this);
        day.setAdapter(new NumberWheelAdapter(1,getDayNum()));
        day.setCurrentIndex(currDay-1);
        dayStr = day.getAdapter().getItem(currDay - 1);
        getView(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getView(R.id.confirm).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.setOnTimeChooseListener(yearStr.substring(0,yearStr.length()-1) + "-" + monthStr.substring(0,monthStr.length()-1) + "-"
                        + dayStr);
                dismiss();
            }
        });
    }


    /**
     * 设置默认显示的日期
     *
     * @param dateStr 形如2015-12-1的日期字符串
     */
    public BirthdayDialog setDefaultDate(String dateStr) {
        Date date;
        if (TextUtils.isEmpty(dateStr)) {
            dateStr = "1990-1-1";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            date = format.parse(dateStr);
            year.setCurrentIndex(date.getYear());
            month.setCurrentIndex(date.getMonth());
            day.setCurrentIndex(date.getDate() - 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    private int getDayNum() {
        int m=monthStr.length()-1;
        int y=yearStr.length()-1;

        String month=monthStr.substring(0,m);
        String year=yearStr.substring(0,y);
        if (month.equals("01")||month.equals("03")||month.equals("05")||
                month.equals("07")||month.equals("08")||month.equals("10")||month.equals("12")) {
            return 31;
        } else if (month.equals("04")||month.equals("06")||month.equals("09")||month.equals("11")) {
            return 30;
        } else {
            if ((Integer.parseInt(year) % 4 == 0 && Integer
                    .parseInt(year) % 100 != 0)
                    || Integer.parseInt(year) % 400 == 0) {

                return 29;
            } else {
                return 28;
            }
        }

    }
    public interface OnTimeChooseListener {
         void setOnTimeChooseListener(String time);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == year) {
            yearStr = year.getAdapter().getItem(newValue);
        }
        if (wheel == month) {
            monthStr = month.getAdapter().getItem(newValue);
            if(!StringUtil.isBlank(dayStr)){
                day.setAdapter(new NumberWheelAdapter(1,getDayNum(),"%02d"));
                day.setCurrentIndex(0);
            }
        }

        if (wheel == day) {
            dayStr = day.getAdapter().getItem(newValue);
        }
    }
}
