package com.sixbexchange.entity.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by 郭青枫 on 2018/1/16 0016.
 */

public class ExchangeData implements Parcelable {


    private String exchange; //交易所
    private String exch; //交易所
    private String symbol;//e.g. btc,eth...简称
    private String sym;//e.g. btc,eth...
    private String unit;//兑换货币单位 BTC/USD 中的 USD
    private String tradePair;//e.g. BTC/USD symbol/unit
    private String side;//1买2卖
    private String last;//最新价
    private String la;//最新价
    private String high;//最高价
    private String h;//最高价
    private String low;//最低价
    private String l;//最低价
    private String open;//24小时开盘价
    private String o;//24小时开盘价
    private String close;//24小时收盘价
    private String c;//24小时收盘价
    private String volume; //24成交量
    private String vol; //24成交量
    private String amount;//24小时成交额
    private String amt;//24小时成交额
    private String ask;//卖一
    private String bid;//买一
    private String change;//24小时涨跌幅
    private String ch;//24小时涨跌幅
    private String symName;//全称
    private long timestamp;
    private long ts;
    private String onlyKey;//市场的唯一标识 例如：Okex_ETH_BTC "choicePrice": null// 用户选择的市场价格
    private String key;//市场的唯一标识 例如：Okex_ETH_BTC "choicePrice": null// 用户选择的市场价格
    private String sign;
    private boolean focus;
    private Long focusId;
    private String img;
    private String name;
    private String klineFrom;
    private String logo;//交易所图标

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getKlineFrom() {
        return klineFrom;
    }

    public void setKlineFrom(String klineFrom) {
        this.klineFrom = klineFrom;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }


    public Long getFocusId() {
        return focusId;
    }

    public void setFocusId(Long focusId) {
        this.focusId = focusId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSymName() {
        if(TextUtils.isEmpty(symName)){
            return name;
        }

        return symName;
    }

    public void setSymName(String symName) {
        this.symName = symName;
    }


    public String getExchange() {
        if (TextUtils.isEmpty(exchange)) {
            return exch;
        }
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getExch() {
        return exch;
    }

    public void setExch(String exch) {
        this.exch = exch;
        this.exchange = exch;
    }



    public String getSymbol() {
        if (TextUtils.isEmpty(symbol)) {
            return sym;
        }
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
        this.symbol = sym;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTradePair() {
        return tradePair;
    }

    public void setTradePair(String tradePair) {
        this.tradePair = tradePair;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getLast() {
        if (TextUtils.isEmpty(last)) {
            return la;
        }
        return last;
    }

    public void setLast(String last) {
        this.last = last;
        this.la=last;
    }

    public String getLa() {
        return la;
    }

    public void setLa(String la) {
        this.la = la;
        this.last = la;
    }

    public String getHigh() {
        if (TextUtils.isEmpty(high)) {
            return h;
        }
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
        this.high = h;
    }

    public String getLow() {
        if (TextUtils.isEmpty(low)) {
            return l;
        }
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
        this.low = l;
    }

    public String getOpen() {
        if (TextUtils.isEmpty(open)) {
            return o;
        }
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
        this.open = o;
    }

    public String getClose() {
        if (TextUtils.isEmpty(close)) {
            return c;
        }
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
        this.close = c;
    }

    public String getVolume() {
        if (TextUtils.isEmpty(volume)) {
            return vol;
        }
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
        this.vol = volume;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
        this.volume = vol;
    }

    public String getAmount() {
        if (TextUtils.isEmpty(amount)) {
            return amt;
        }
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
        this.amount = amt;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getChange() {
        if (TextUtils.isEmpty(change)) {
            return ch;
        }
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
        this.change = ch;
    }

    public long getTimestamp() {
        if (timestamp == 0) {
            return ts;
        }
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
        this.timestamp = ts;
    }

    public String getOnlyKey() {
        if (TextUtils.isEmpty(onlyKey)) {
            return key;
        }
        return onlyKey;
    }

    public void setOnlyKey(String onlyKey) {
        this.onlyKey = onlyKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        this.onlyKey = key;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.exchange);
        dest.writeString(this.exch);
        dest.writeString(this.symbol);
        dest.writeString(this.sym);
        dest.writeString(this.unit);
        dest.writeString(this.tradePair);
        dest.writeString(this.side);
        dest.writeString(this.last);
        dest.writeString(this.la);
        dest.writeString(this.high);
        dest.writeString(this.h);
        dest.writeString(this.low);
        dest.writeString(this.l);
        dest.writeString(this.open);
        dest.writeString(this.o);
        dest.writeString(this.close);
        dest.writeString(this.c);
        dest.writeString(this.volume);
        dest.writeString(this.vol);
        dest.writeString(this.amount);
        dest.writeString(this.amt);
        dest.writeString(this.ask);
        dest.writeString(this.bid);
        dest.writeString(this.change);
        dest.writeString(this.ch);
        dest.writeString(this.symName);
        dest.writeLong(this.timestamp);
        dest.writeLong(this.ts);
        dest.writeString(this.onlyKey);
        dest.writeString(this.key);
        dest.writeString(this.sign);
        dest.writeByte(this.focus ? (byte) 1 : (byte) 0);
        dest.writeValue(this.focusId);
        dest.writeString(this.img);
        dest.writeString(this.name);
        dest.writeString(this.klineFrom);
        dest.writeString(this.logo);
    }

    public ExchangeData() {
    }

    protected ExchangeData(Parcel in) {
        this.exchange = in.readString();
        this.exch = in.readString();
        this.symbol = in.readString();
        this.sym = in.readString();
        this.unit = in.readString();
        this.tradePair = in.readString();
        this.side = in.readString();
        this.last = in.readString();
        this.la = in.readString();
        this.high = in.readString();
        this.h = in.readString();
        this.low = in.readString();
        this.l = in.readString();
        this.open = in.readString();
        this.o = in.readString();
        this.close = in.readString();
        this.c = in.readString();
        this.volume = in.readString();
        this.vol = in.readString();
        this.amount = in.readString();
        this.amt = in.readString();
        this.ask = in.readString();
        this.bid = in.readString();
        this.change = in.readString();
        this.ch = in.readString();
        this.symName = in.readString();
        this.timestamp = in.readLong();
        this.ts = in.readLong();
        this.onlyKey = in.readString();
        this.key = in.readString();
        this.sign = in.readString();
        this.focus = in.readByte() != 0;
        this.focusId = (Long) in.readValue(Long.class.getClassLoader());
        this.img = in.readString();
        this.name = in.readString();
        this.klineFrom = in.readString();
        this.logo = in.readString();
    }

    public static final Creator<ExchangeData> CREATOR = new Creator<ExchangeData>() {
        @Override
        public ExchangeData createFromParcel(Parcel source) {
            return new ExchangeData(source);
        }

        @Override
        public ExchangeData[] newArray(int size) {
            return new ExchangeData[size];
        }
    };
}
