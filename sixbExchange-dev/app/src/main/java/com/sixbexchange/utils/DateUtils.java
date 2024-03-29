package com.sixbexchange.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Date Utils  <br/>
 *
 * @author Tony Tian
 * @version 1.0.0
 * @date 2018/2/3 20:30
 */
public class DateUtils {

    public static String TIME_STYLE_S1 = "yyyy-MM-dd";
    public static String TIME_STYLE_S2 = "yyyy-MM-dd HH:mm";
    public static String TIME_STYLE_S3 = "yyyy-MM-dd HH:mm:ss";
    public static String TIME_STYLE_S4 = "yyyy-MM-dd HH:mm:ss:S";
    public static String TIME_STYLE_S5 = "yyyy-MM-dd HH:mm:ss:S E zZ";
    public static String TIME_STYLE_S6 = "yyyyMMddHHmmssS";
    public static String TIME_STYLE_S7 = "yyyy年MM月dd日HH时mm分ss秒";
    public static String TIME_STYLE_S10 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static String TIME_STYLE_S11 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX";
    public static String TIME_STYLE_S13 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ";
    public static String TIME_STYLE_S12 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
    public static SimpleDateFormat SDFS8 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+08:00'");
    //2018-12-29T13:52:33.103002+08:00
    public static SimpleDateFormat SDFS9 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'+08:00'");


    static {
        DateUtils.SDF.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    /**
     * convert UTC timestamp：2018-02-03T16:56:29.919Z  -> object: Sun Feb 04 00:56:29 CST 2018
     */
    //    public static Date parseUTCTime(final String utcTime) {
    //        if (TextUtils.isEmpty(utcTime)) {
    //            return null;
    //        }
    //        final SimpleDateFormat sdfi = (SimpleDateFormat) DateUtils.SDF.clone();
    //        try {
    //            return sdfi.parse(utcTime);
    //        } catch (ParseException e) {
    //            e.printStackTrace();
    //            return new Date();
    //        }
    //    }

    /**
     * Returns the time string in format. <br/>
     * Styles:
     * <p>
     * <blockquote><pre>
     *         1:  2018-03-01
     *         2:  2018-03-01 15:53
     *         3:  2018-03-01 15:53:43
     *         4:  2018-03-01 15:53:43:288
     *         5:  2018-03-01 15:53:43:288 Thu CST+0800
     *         6:  20180301155343288
     *         7:  2018年03月01日15时53分43秒
     *         8:  2018-03-01T07:53:43.288Z
     *         9:  1519890823.288
     * `default`:  Thu Mar 01 15:53:43 CST 2018
     * </pre></blockquote>
     * </p>
     *
     * @param time  Date object, if time=null, returns the current time.
     * @param style Format number
     * @return String time string in format
     */
    public static String timeToString(Date time, final int style) {
        if (time == null) {
            time = new Date();
        }
        String timeStyle = null;
        switch (style) {
            case 1: {
                timeStyle = DateUtils.TIME_STYLE_S1;
                break;
            }
            case 2: {
                timeStyle = DateUtils.TIME_STYLE_S2;
                break;
            }
            case 3: {
                timeStyle = DateUtils.TIME_STYLE_S3;
                break;
            }
            case 4: {
                timeStyle = DateUtils.TIME_STYLE_S4;
                break;
            }
            case 5: {
                timeStyle = DateUtils.TIME_STYLE_S5;
                break;
            }
            case 6: {
                timeStyle = DateUtils.TIME_STYLE_S6;
                break;
            }
            case 7: {
                timeStyle = DateUtils.TIME_STYLE_S7;
                break;
            }
            case 8: {
                final SimpleDateFormat sdf = (SimpleDateFormat) DateUtils.SDF.clone();
                return sdf.format(time);
            }
            case 9: {
                return DateUtils.getEpochTime(time);
            }
            case 10: {
                timeStyle = DateUtils.TIME_STYLE_S10;
                break;
            }
            default: {
                return time.toString();
            }
        }
        return new SimpleDateFormat(timeStyle).format(time);
    }

    public static Date getTime(String time, String type) {
        try {
            return new SimpleDateFormat(type).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * {@link DateUtils#timeToString(Date, int)}
     *
     * @param time Date object, if time=null, returns null.
     */
    public static String timeToStringNull(final Date time, final int style) {
        return time == null ? null : DateUtils.timeToString(time, style);
    }

    /**
     * UNIX timestamp ISO 8601 rule eg: 2018-02-03T05:34:14.110Z
     */
    public static String getUnixTime() {
        return System.currentTimeMillis() + "";
    }

    /**
     * Date
     * epoch time   eg: 1517662142.557
     */
    public static String getEpochTime(final Date... time) {
        long milliseconds;
        if (time != null && time.length > 0) {
            milliseconds = time[0].getTime();
        } else {
            milliseconds = System.currentTimeMillis();
        }
        milliseconds = milliseconds - 28800000L;
        final BigDecimal bd1 = new BigDecimal(milliseconds);
        final BigDecimal bd2 = new BigDecimal(1000);
        return bd1.divide(bd2).toString();
    }

    /**
     * convert UTC timestamp：2018-02-03T16:56:29.919Z  -> object: Sun Feb 04 00:56:29 CST 2018
     */
    public static Date parseUTCTime(final String utcTime) {
        if (TextUtils.isEmpty(utcTime)) {
            return null;
        }
        final SimpleDateFormat sdfi = (SimpleDateFormat) DateUtils.SDF.clone();
        try {
            return sdfi.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * convert UTC timestamp：2018-12-29T13:42:45+08:00  -> string:"1627823728332" 毫秒
     */
    public static String parse1TokenTime1ToLongStr(final String utcTime) {
        if (TextUtils.isEmpty(utcTime)) {
            return null;
        }
        final SimpleDateFormat sdfi = (SimpleDateFormat) DateUtils.SDFS8.clone();
        try {
            return String.valueOf(sdfi.parse(utcTime).getTime());
        } catch (Throwable e) {
            return utcTime;
        }
    }

    /**
     * convert UTC timestamp：2018-12-29T13:52:33.103002+08:00  -> string:"1627823728332" 毫秒
     */
    public static String parse1TokenTime2ToLongStr(final String utcTime) {
        if (TextUtils.isEmpty(utcTime)) {
            return null;
        }
        final SimpleDateFormat sdfi = (SimpleDateFormat) DateUtils.SDFS9.clone();
        try {
            return String.valueOf(sdfi.parse(utcTime).getTime());
        } catch (Throwable e) {
            return utcTime;
        }
    }


    /**
     * convert decimal timestamp：1517676989.919 ->   -> object: Sun Feb 04 00:56:29 CST 2018
     */
    public static Date parseDecimalTime(final String decimalTime) {
        if (TextUtils.isEmpty(decimalTime)) {
            return null;
        }
        final BigDecimal bd1 = new BigDecimal(decimalTime);
        final BigDecimal bd2 = new BigDecimal(1000);
        return new Date(bd1.multiply(bd2).longValue());
    }


    /**
     * 1 Day in Millis
     */
    public static final long DAY = 24L * 60L * 60L * 1000L;

    /**
     * 1 Week in Millis
     */
    public static final long WEEK = 7 * DAY;

    /* An array of custom date formats */
    private static final DateFormat[] CUSTOM_DATE_FORMATS;

    /* The Default Timezone to be used */
    private static final TimeZone TIMEZONE = TimeZone.getDefault(); //$NON-NLS-1$

    /**
     * Tries different date formats to parse against the given string
     * representation to retrieve a valid Date object.
     *
     * @param strdate Date as String
     * @return Date The parsed Date
     */
    public static Date parseDate(String strdate) {

        /* Return in case the string date is not set */
        if (strdate == null || strdate.length() == 0)
            return null;

        Date result = null;
        strdate = strdate.trim();
        if (strdate.length() > 10) {

            /* Open: deal with +4:00 (no zero before hour) */
            if ((strdate.substring(strdate.length() - 5).indexOf("+") == 0 || strdate.substring(strdate.length() - 5).indexOf("-") == 0) && strdate.substring(strdate.length() - 5).indexOf(":") == 2) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                String sign = strdate.substring(strdate.length() - 5, strdate.length() - 4);
                strdate = strdate.substring(0, strdate.length() - 5) + sign + "0" + strdate.substring(strdate.length() - 4); //$NON-NLS-1$
            }

            String dateEnd = strdate.substring(strdate.length() - 6);

            /*
             * try to deal with -05:00 or +02:00 at end of date replace with -0500 or
             * +0200
             */
            if ((dateEnd.indexOf("-") == 0 || dateEnd.indexOf("+") == 0) && dateEnd.indexOf(":") == 3) { //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
                if (!"GMT".equals(strdate.substring(strdate.length() - 9, strdate.length() - 6))) { //$NON-NLS-1$
                    String oldDate = strdate;
                    String newEnd = dateEnd.substring(0, 3) + dateEnd.substring(4);
                    strdate = oldDate.substring(0, oldDate.length() - 6) + newEnd;
                }
            }
        }

        /* Try to parse the date */
        int i = 0;
        while (i < CUSTOM_DATE_FORMATS.length) {
            try {

                /*
                 * This Block needs to be synchronized, because the parse-Method in
                 * SimpleDateFormat is not Thread-Safe.
                 */
                synchronized (CUSTOM_DATE_FORMATS[i]) {
                    return CUSTOM_DATE_FORMATS[i].parse(strdate);
                }
            } catch (ParseException e) {
                i++;
            } catch (NumberFormatException e) {
                i++;
            }
        }
        return result;
    }

    /** Initialize the array of common date formats and formatter */
    static {

        /* Create Date Formats */
        final String[] possibleDateFormats = {
                /* RFC 1123 with 2-digit Year */"EEE, dd MMM yy HH:mm:ss z",
                /* RFC 1123 with 4-digit Year */"EEE, dd MMM yyyy HH:mm:ss z",
                /* RFC 1123 with no Timezone */"EEE, dd MMM yy HH:mm:ss",
                /* Variant of RFC 1123 */"EEE, MMM dd yy HH:mm:ss",
                /* RFC 1123 with no Seconds */"EEE, dd MMM yy HH:mm z",
                /* Variant of RFC 1123 */"EEE dd MMM yyyy HH:mm:ss",
                /* RFC 1123 with no Day */"dd MMM yy HH:mm:ss z",
                /* RFC 1123 with no Day or Seconds */"dd MMM yy HH:mm z",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:ssZ",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:ss'Z'",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:sszzzz",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:ss z",
                /* ISO 8601 */"yyyy-MM-dd'T'HH:mm:ssz",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:ss.SSSz",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HHmmss.SSSz",
                /* ISO 8601 slightly modified */"yyyy-MM-dd'T'HH:mm:ss",
                /* ISO 8601 w/o seconds */"yyyy-MM-dd'T'HH:mmZ",
                /* ISO 8601 w/o seconds */"yyyy-MM-dd'T'HH:mm'Z'",
                /* RFC 1123 without Day Name */"dd MMM yyyy HH:mm:ss z",
                /* RFC 1123 without Day Name and Seconds */"dd MMM yyyy HH:mm z",
                /* Simple Date Format */"yyyy-MM-dd",
                /* Simple Date Format */"MMM dd, yyyy"};

        /* Create the dateformats */
        CUSTOM_DATE_FORMATS = new SimpleDateFormat[possibleDateFormats.length];

        for (int i = 0; i < possibleDateFormats.length; i++) {
            CUSTOM_DATE_FORMATS[i] = new SimpleDateFormat(possibleDateFormats[i], Locale.getDefault());
            CUSTOM_DATE_FORMATS[i].setTimeZone(TIMEZONE);
        }
    }

    public static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    public static final String yyyy_MM_dd_HH_mm_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String HH_mm = "HH:mm";

    public static final Map<String, SimpleDateFormat> dateFormatMap = new HashMap<>();

    public static String format_yyyy_MM_dd_HH_mm(long timeInMillis) {
        return getDateFormat(yyyy_MM_dd_HH_mm).format(timeInMillis);
    }
    public static String format_yyyy_MM_dd_HH_mm_ss(long timeInMillis) {
        return getDateFormat(yyyy_MM_dd_HH_mm_ss).format(timeInMillis);
    }
    public static String format_HH_mm(long timeInMillis) {
        return getDateFormat(HH_mm).format(timeInMillis);
    }

    public static SimpleDateFormat getDateFormat(String pattern) {
        SimpleDateFormat dateFormat = dateFormatMap.get(pattern);
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(pattern);
            dateFormatMap.put(pattern, dateFormat);
        }
        return dateFormat;
    }

    public static String format_yyyy_MM_dd_HH_mm(Date date) {
        return getDateFormat(yyyy_MM_dd_HH_mm).format(date);
    }

    public static String format_yyyy_MM_dd_HH_mm_SSS(Date date) {
        return getDateFormat(yyyy_MM_dd_HH_mm_SSS).format(date);
    }
}
