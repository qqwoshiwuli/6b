package com.fivefivelike.mybaselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.utils.logger.KLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by 郭青枫 on 2018/2/26 0026.
 */

public class UUIDS {
    private static final String TAG = UUIDS.class.getName();
    private static UUIDS device;
    private Context context;
    private final static String DEFAULT_NAME = "system_device_id";
    private final static String DEFAULT_FILE_NAME = "system_device_id";
    private final static String DEFAULT_DEVICE_ID = "dervice_id";
    private final static String FILE_ANDROID = Environment.getExternalStoragePublicDirectory("Android") + File.separator + DEFAULT_FILE_NAME;
    private final static String FILE_DCIM = Environment.getExternalStoragePublicDirectory("DCIM") + File.separator + DEFAULT_FILE_NAME;
    private static SharedPreferences preferences = null;

    public UUIDS(Context context) {
        this.context = context;
    }

    private String uuid;

    public static UUIDS buidleID(Context context) {
        if (device == null) {
            synchronized (UUIDS.class) {
                if (device == null) {
                    device = new UUIDS(context);
                }
            }
        }
        return device;
    }

    public static String getUUID() {
        if (preferences == null) {
            Log.d(TAG, "Please check the UUIDS.buidleID in Application (this).Check ()");
            return "dervice_id";
        }
        return preferences.getString("dervice_id", "dervice_id");
    }

    public static String getUUID(Context context) {
        if (preferences == null) {
            Log.d(TAG, "Please check the UUIDS.buidleID in Application (this).Check ()");
            UUIDS.buidleID(context).check();
            return getUUID(context);
        }
        String string = preferences.getString("dervice_id", "dervice_id");
        return string;
    }

    //生成一个128位的唯一标识符
    private String createUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    public static void setUUIDS(String id) {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(DEFAULT_DEVICE_ID, id);
            editor.commit();
        }
    }

    public void check() {
        preferences = context.getSharedPreferences(DEFAULT_NAME, 0);
        uuid = preferences.getString(DEFAULT_DEVICE_ID, null);
        if (uuid == null || ObjectUtils.equals("dervice_id", uuid)) {
            uuid = createUUID();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(DEFAULT_DEVICE_ID, uuid);
            editor.commit();
            Log.d(TAG, "save uuid SharePref:" + uuid);
        } else {
            if (checkAndroidFile() == null) {
                saveAndroidFile(uuid);
            }
            if (checkDCIMFile() == null) {
                saveDCIMFile(uuid);
            }
        }
        Log.d(TAG, "result uuid:" + uuid);
    }

    private String checkAndroidFile() {
        BufferedReader reader = null;
        try {
            File file = new File(FILE_ANDROID);
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAndroidFile(String id) {
        try {
            File file = new File(FILE_ANDROID);
            KLog.d(file);
            FileWriter writer = new FileWriter(file);
            writer.write(id);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String checkDCIMFile() {
        BufferedReader reader = null;
        try {
            File file = new File(FILE_DCIM);
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDCIMFile(String id) {
        try {
            File file = new File(FILE_DCIM);
            KLog.d(file);
            FileWriter writer = new FileWriter(file);
            writer.write(id);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
