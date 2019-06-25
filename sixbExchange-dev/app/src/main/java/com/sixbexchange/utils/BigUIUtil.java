package com.sixbexchange.utils;

import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/*
* 资产行情数据处理
* @author gqf
* @Description
* @Date  2019/4/3 0003 13:48
* @Param
* @return
**/
public class BigUIUtil {

    private static class helper {
        private static BigUIUtil exchangeRateUtil = new BigUIUtil();
    }

    public static BigUIUtil getinstance() {
        return helper.exchangeRateUtil;
    }

    private BigUIUtil() {
        initSymbol();
    }

    LinkedHashMap<String, String> symbol;

    private void initSymbol() {
        symbol = new LinkedHashMap<>();
        //        （CNY）¥
        //（USD）$
        //（EUR）€
        //（GBP）￡
        //（JPY）円
        //（KRW）₩
        //（LTC）Ł
        //                (BTC)฿
        //                (ETH)E
        symbol.put("CNY", "¥");
        symbol.put("USD", "$");
        symbol.put("EUR", "€");
        symbol.put("GBP", "￡");
        symbol.put("JPY", "円");
        symbol.put("KRW", "₩");
        symbol.put("LTC", "Ł");
        symbol.put("BTC", "฿");
        symbol.put("ETH", "E");
        symbol.put("USDT", "₮");
        symbol.put("USD", "$");
    }

    public String getSymbol(String unit) {
        if (symbol == null) {
            return unit;
        }
        if (TextUtils.isEmpty(unit)) {
            return unit;
        }
        for (Map.Entry<String, String> entry : symbol.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (k.equalsIgnoreCase(unit)) {
                return v;
            }
        }
        return unit;
    }

    //价格单位 显示规则
    //    >>>价格
    //        123456.12
    //        1234.12
    //        123.12
    //        12.1234
    //        1.1234
    //        0.1111
    //        0.01111
    //        0.001111
    //        0.0001111
    //        0.00001111
    //        0.00000111
    //        0.00000011
    //        0.00000001
    //        0
    //
    //
    //
    public String bigPrice(String price) {
        if (TextUtils.isEmpty(price) || "NaN".equals(price)) {
            return "";
        }
        if ("null".equals(price)) {
            return "";
        }
        if (!UiHeplUtils.isDouble(price)) {
            return price;
        }
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(price);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        if (bigDecimal.doubleValue() == 0) {
            return "0";
        }

        StringBuffer stringBuffer = new StringBuffer();
        if (new BigDecimal("100").compareTo(bigDecimal) == -1) {
            stringBuffer.append(bigDecimal.setScale(2, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString());
        } else if (new BigDecimal("0.1").compareTo(bigDecimal) == -1) {
            stringBuffer.append(bigDecimal.setScale(4, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString());
        } else if (new BigDecimal("0.01").compareTo(bigDecimal) == -1) {
            stringBuffer.append(bigDecimal.setScale(5, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString());
        } else if (new BigDecimal("0.001").compareTo(bigDecimal) == -1) {
            stringBuffer.append(bigDecimal.setScale(6, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString());
        } else if (new BigDecimal("0.00000001").compareTo(bigDecimal) == -1) {
            stringBuffer.append(bigDecimal.setScale(8, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString());
        } else if (new BigDecimal("-100").compareTo(bigDecimal) == 1) {
            stringBuffer.append(bigDecimal.setScale(2, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString());
        } else if (new BigDecimal("-0.1").compareTo(bigDecimal) == 1) {
            stringBuffer.append(bigDecimal.setScale(4, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString());
        } else if (new BigDecimal("-0.01").compareTo(bigDecimal) == 1) {
            stringBuffer.append(bigDecimal.setScale(5, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString());
        } else if (new BigDecimal("-0.001").compareTo(bigDecimal) == 1) {
            stringBuffer.append(bigDecimal.setScale(6, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString());
        } else if (new BigDecimal("-0.00000001").compareTo(bigDecimal) == 1) {
            stringBuffer.append(bigDecimal.setScale(8, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString());
        } else {
            return "0";
        }
        return stringBuffer.toString();
    }

    public void rateTextView(String rate, TextView textView) {
        rateTextView(rate, 2, textView, true, true);
    }

    public void rateTextView(String rate, int size, TextView textView, boolean isChangeColor, boolean isAdd) {
        if (!UiHeplUtils.isDouble(rate)) {
            return;
        }
        if (textView == null) {
            return;
        }
        if (new BigDecimal(rate).doubleValue() >= 0) {
            if (isChangeColor) {
                textView.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
            }
            textView.setText((isAdd ? "+" : "") + new BigDecimal(rate)
                    .setScale(size, BigDecimal.ROUND_UP).stripTrailingZeros().toPlainString() + "%");
        } else {
            if (isChangeColor) {
                textView.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
            }
            textView.setText(new BigDecimal(rate).setScale(size, BigDecimal.ROUND_UP)
                    .stripTrailingZeros().toPlainString() + "%");
        }
    }

    //量 单位 显示规则
    //        >>>量
    //0
    //        0.0001
    //        0.0011
    //        0.011
    //        0.11
    //        1.23
    //        12.35
    //        123.46
    //        1235
    //        1.23万
    //        12.3万
    //        123万
    //        1235万
    //        1.2亿
    //        12.3亿
    //        123亿
    //        1235亿
    //
    //
    public String bigAmount(String amount) {
        if (TextUtils.isEmpty(amount)) {
            return "";
        }
        if ("null".equals(amount)) {
            return "";
        }
        if ("NaN".equals(amount)) {
            return "";
        }
        if ("0".equals(amount)) {
            return "0";
        }
        if (!UiHeplUtils.isDouble(amount)) {
            return amount;
        }
        BigDecimal bigDecimal = new BigDecimal(amount);

        String symbol = "";
        if (bigDecimal.doubleValue() < 0) {
            bigDecimal = bigDecimal.multiply(new BigDecimal("-1"));
            symbol = "-";
        }
        if (bigDecimal.doubleValue() == 0) {
            return "0";
        }
        if (new BigDecimal("10000000000").compareTo(bigDecimal) == -1) {
            return symbol + bigDecimal.multiply(new BigDecimal("0.00000001"))
                    .setScale(0, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString() + "亿";
        } else if (new BigDecimal("100000000").compareTo(bigDecimal) == -1) {
            return symbol + bigDecimal.multiply(new BigDecimal("0.00000001"))
                    .setScale(1, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString() + "亿";
        } else if (new BigDecimal("1000000").compareTo(bigDecimal) == -1) {
            return symbol + bigDecimal.multiply(new BigDecimal("0.0001"))
                    .setScale(0, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString() + "万";
        } else if (new BigDecimal("100000").compareTo(bigDecimal) == -1) {
            return symbol + bigDecimal.multiply(new BigDecimal("0.0001"))
                    .setScale(1, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString() + "万";
        } else if (new BigDecimal("10000").compareTo(bigDecimal) == -1) {
            return symbol + bigDecimal.multiply(new BigDecimal("0.0001"))
                    .setScale(2, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString() + "万";
        } else if (new BigDecimal("1000").compareTo(bigDecimal) == -1) {//大于1000
            return symbol + bigDecimal.multiply(new BigDecimal("1"))
                    .setScale(0, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString();
        } else if (new BigDecimal("0").compareTo(bigDecimal) == -1 && (new BigDecimal("0.1").compareTo(bigDecimal) == -1 || new BigDecimal("0.1").compareTo(bigDecimal) == 0)) {//大于0
            return symbol + bigDecimal.multiply(new BigDecimal("1"))
                    .setScale(2, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString();
        } else if (new BigDecimal("0.1").compareTo(bigDecimal) == 1 && (new BigDecimal("0.01").compareTo(bigDecimal) == -1 || new BigDecimal("0.01").compareTo(bigDecimal) == 0)) {//0.011
            return symbol + bigDecimal.multiply(new BigDecimal("1"))
                    .setScale(3, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString();
        } else if (new BigDecimal("0.01").compareTo(bigDecimal) == 1 && (new BigDecimal("0.001").compareTo(bigDecimal) == -1 || new BigDecimal("0.001").compareTo(bigDecimal) == 0)) {//0.0011
            return symbol + bigDecimal.multiply(new BigDecimal("1"))
                    .setScale(4, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString();
        } else if (new BigDecimal("0.001").compareTo(bigDecimal) == 1) {//0.0001
            return symbol + bigDecimal.multiply(new BigDecimal("1"))
                    .setScale(4, BigDecimal.ROUND_DOWN)
                    .stripTrailingZeros().toPlainString();
        } else {
            return "0";
        }
    }

    public String showLeafOrCoin(String unit, String coin) {
        if (ObjectUtils.equals("张", unit)) {
            if (ObjectUtils.equals("1", UserSet.getinstance().getLeafOrCoin())) {
                return unit;
            } else {
                return coin;
            }
        }
        return unit;
    }

    public String bigLeafToCoin(String leaf, String price, String symbol) {
        if (!UiHeplUtils.isDouble(price)) {
            return leaf;
        }
        if (ObjectUtils.equals("1", UserSet.getinstance().getLeafOrCoin())) {
            return leaf;
        } else {
            return new BigDecimal(leaf)
                    .multiply(new BigDecimal(StringUtils.equalsIgnoreCase("BTC", symbol) ? "100" : "10"))
                    .divide(new BigDecimal(price), 4, RoundingMode.DOWN)
                    .stripTrailingZeros().toPlainString();
        }
    }

    public String bigCoinToLeaf(String coin, String price, String symbol) {
        if (!UiHeplUtils.isDouble(price)) {
            return "0";
        }
        return new BigDecimal(coin)
                .multiply(new BigDecimal(price))
                .divide(new BigDecimal(StringUtils.equalsIgnoreCase("BTC", symbol) ? "100" : "10")
                        , 0, BigDecimal.ROUND_HALF_UP)
                .stripTrailingZeros().toPlainString();
    }

    public String bigEnglishNum(String change, int size) {
        if (TextUtils.isEmpty(change)) {
            return "";
        }
        if ("null".equals(change)) {
            return "";
        }
        if (!UiHeplUtils.isDouble(change)) {
            return change;
        }
        StringBuffer stringBuffer = new StringBuffer(",###,###,###,###,###,###");
        if (size > 0) {
            stringBuffer.append(".");
            for (int i = 0; i < size; i++) {
                stringBuffer.append("0");
            }
        }
        BigDecimal bigDecimal = new BigDecimal(change);
        DecimalFormat df = new DecimalFormat(stringBuffer.toString());
        String format = df.format(bigDecimal);
        if (ObjectUtils.equals(format.charAt(0), '.')) {
            format = "0" + format;
        }
        return format;
    }

    public String rateText(String rateTxt) {
        if (TextUtils.isEmpty(rateTxt)) {
            return "";
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(rateTxt);
            String txt = bigDecimal.setScale(2, BigDecimal.ROUND_UP)
                    .stripTrailingZeros().toPlainString();
            if (bigDecimal.doubleValue() > 0) {
                return "+" + txt;
            }
            return txt;
        } catch (Exception e) {
            return "";
        }
    }
}
