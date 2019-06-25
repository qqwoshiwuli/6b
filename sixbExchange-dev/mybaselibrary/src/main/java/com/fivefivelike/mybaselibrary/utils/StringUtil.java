package com.fivefivelike.mybaselibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.widget.EditText;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 26英文字母字符串{@link #ENGLIST_LETTER
 * 判断字符串是否为空或者空字符串{@link #isBlank(String)}
 * 当字符串为空时返回""{@link #nullToStr(String)}
 * 保留小数位数{@link #getDecimal(int, double)#getDecimal(int, float)}
 * getDip{@link #getDip(Context, float)}
 * isChinese{@link #isChinese(char)}
 * 判断字符串是否为无符号数字{@link #isNum(String)}
 * 将double转换为字符串，保留小数点位数{@link #DoubleToAmountString(Double, int)}
 * {提取英文的首字母，非英文字母用#代替@link {@link #getInitialAlphaEn(String)}
 * 去除String中的某一个字符{@link #removeAllChar(String, String)}
 * 获取非空edittext{@link #getEditText(EditText)}
 * MD5加密 32位小写{@link #getMd5Value(String)}
 * 获取字符长度{@link #strLength(String)}
 * 改变字符串部分文字颜色{@link #changStringColor(String, int, int, int)}
 * 集合字符串以,拼接{@link #listToString(List)}
 * 是否为邮箱{@link #isEmail(String)}
 * 手机号正则{@link #isMobileNO(String)}
 * 获取ip地址{@link #getIPAddress(Context)}
 * 将得到的int类型的IP转换为String类型{@link #intIP2StringIP(int)}
 * 生日转化为年龄{@link #birthdayToAge(String)}
 */
public class StringUtil {


    /**
     * 26英文字母字符串
     */
    public static String[] ENGLIST_LETTER = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * 判断字符串是否为空或者空字符串 如果字符串是空或空字符串则返回true，否则返回false。也可以使用Android自带的TextUtil
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 当字符串为空时返回""
     *
     * @param str
     * @return
     */
    public static String nullToStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 保留小数
     *
     * @param num   保留几位小数
     * @param value 需要改变的值
     * @author String  返回的结果值
     */
    public static String getDecimal(int num, float value) {
        StringBuffer buffer = new StringBuffer("0.");
        for (int i = 0; i < num; i++) {
            buffer.append("0");
        }

        DecimalFormat format = new DecimalFormat(buffer.toString());
        return format.format(value);
    }

    /**
     * 保留小数
     *
     * @param num   保留几位小数
     * @param value 需要改变的值
     * @author String  返回的结果值
     */
    public static String getDecimal(int num, double value) {
        StringBuffer buffer = new StringBuffer("0.");
        for (int i = 0; i < num; i++) {
            buffer.append("0");
        }
        DecimalFormat format = new DecimalFormat(buffer.toString());
        return format.format(value);
    }

    /**
     * 把数值转换成dip像素
     *
     * @param context
     * @param value
     * @return
     */
    public static float getDip(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 判断是否是中文
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否超过指定字符数(待测试)
     *
     * @param content
     * @param stringNum 指定字符数 如：140
     * @return
     */
    public static boolean countStringLength(String content, int stringNum) {
        int result = 0;
        if (content != null && !"".equals(content)) {
            char[] contentArr = content.toCharArray();
            if (contentArr != null) {
                for (int i = 0; i < contentArr.length; i++) {
                    char c = contentArr[i];
                    if (isChinese(c)) {
                        result += 3;
                    } else {
                        result += 1;
                    }
                }
            }
        }
        if (result > stringNum * 3) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否为无符号数字
     *
     * @param num
     * @return
     */
    public static boolean isNum(String num) {
        if (isBlank(num)) {
            return false;
        }
        String check = "^[0-9]*$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(num);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * 将double转换为字符串，保留小数点位数
     *
     * @param doubleNum 需要解析的double
     * @param digitNum  小数点位数，小于0则默认0
     * @return
     */
    public static String DoubleToAmountString(Double doubleNum, int digitNum) {
        if (digitNum < 0)
            digitNum = 0;
        StringBuilder strBuilder = new StringBuilder("#");
        for (int i = 0; i < digitNum; i++) {
            if (i == 0)
                strBuilder.append(".#");
            else
                strBuilder.append("#");
        }
        DecimalFormat df = new DecimalFormat(strBuilder.toString());
        return df.format(doubleNum);
    }

    /**
     * 提取英文的首字母，非英文字母用#代替
     *
     * @param str
     * @return
     */
    public static String getInitialAlphaEn(String str) {
        if (str == null) {
            return "#";
        }

        if (str.trim().length() == 0) {
            return "#";
        }

        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是26字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase(Locale.getDefault()); // 大写输出
        } else {
            return "#";
        }
    }


    /**
     * 去除String中的某一个字符
     *
     * @param orgStr
     * @param splitStr 要去除的字符串
     * @return
     */
    public static String removeAllChar(String orgStr, String splitStr) {
        String[] strArray = orgStr.split(splitStr);
        String res = "";
        for (String tmp : strArray) {
            res += tmp;
        }
        return res;
    }

    /**
     * 获取非空edittext
     *
     * @param text
     * @return
     */
    public static String getEditText(EditText text) {
        if (null == text || text.getText().toString().trim().equals("")) {
            return "";
        }
        return text.getText().toString().trim();
    }

    /**
     * MD5加密 32位小写
     *
     * @param sSecret
     * @return
     */
    public static String getMd5Value(String sSecret) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(sSecret.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取字符长度
     *
     * @param value
     * @return
     */
    public static int strLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 改变字符串部分文字颜色
     *
     * @param content 整体内容
     * @param start   开始位置
     * @param end     结束位置
     * @return
     */
    public static SpannableStringBuilder changStringColor(String content,
                                                          int start, int end, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static SpannableStringBuilder changStringColor(String content,
                                                          int[] start, int[] end, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        for (int i = 0; i < end.length; i++) {
            ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
            builder.setSpan(redSpan, start[i], end[i], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    /**
     * 集合字符串以,拼接
     *
     * @param stringList
     * @return
     */
    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * @param email
     * @return
     * @name isEmail
     * @return_type boolean
     */
    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 手机号正则
     *
     * @param mobiles 手机号
     * @return true:是手机号 false:不是手机号
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^(1[3,4,5,8])\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 获取ip地址
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 0.125
     *
     * @param db
     * @return
     */
    public String getRound(double db) {
        String result = "";
        if (db > 0) {
            result = (double) ((int) ((db + 0.125) * 4)) / 4 + "";
        } else {
            result = (double) ((int) ((db - 0.125) * 4)) / 4 + "";
        }
        return result;
    }

    /**
     * 生日转化为年龄
     *
     * @return
     */
    public static String birthdayToAge(String birthday) {

        if (TextUtils.isEmpty(birthday) || birthday.length() < 4) {
            return "0";
        }
        int bir = Integer.parseInt(birthday.substring(0, 4));
        int cur_year = Integer.parseInt(DateUtils.getCurrYear());

        return (cur_year - bir) + "";
    }

    /**
     * 拆分集合
     *
     * @param <T>
     * @param resList 要拆分的集合
     * @param count   每个集合的元素个数
     * @return 返回拆分后的各个集合
     */
    public static <T> List<List<T>> split(List<T> resList, int count) {

        if (resList == null || count < 1) {
            return null;
        }
        List<List<T>> ret = new ArrayList<>();
        int size = resList.size();
        if (size <= count) { //数据量不足count指定的大小
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            //前面pre个集合，每个大小都是count个元素
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<T>();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if (last > 0) {
                List<T> itemList = new ArrayList<T>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;

    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            //            return size + Byte;
            return 0 + "KB";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
