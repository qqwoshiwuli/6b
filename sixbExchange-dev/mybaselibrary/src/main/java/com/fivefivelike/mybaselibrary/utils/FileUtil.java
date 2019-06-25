package com.fivefivelike.mybaselibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * 文件处理工具类
 * 判断sd卡是否安装{@link #existSdcard()}
 * 获取SD卡的path{@link #getSdPath()}
 * 读取assets下的文本数据{@link #getStringFromAssets(Context, String)}
 * 读取文本流文件{@link #readStream(InputStream)}
 * 获得文件大小{@link #getFileSize(File)}
 * 返回文件大小{@link #formatFileSize(File)}
 * 拷贝资源文件到sd卡{@link #copyResToSdcard(Context, int, String)}
 * 创建文件至SD卡{@link #createFile(String, String)}
 * 创建文件夹到SD卡根目录下{@link #createFileDir(String)}
 * 获取文件后缀名{@link #getMIMEType(File)}
 * 后缀名格式表{@link #MIME_MapTable}
 * {计算sdcard上的剩余空间,新方法无法支持api-18以下的系统@link #freeSpaceOnSdCard()}
 * 删除文件及文件目录下的所有文件{@link #deleteFileUnderFolder(File)}
 * 根据文件名获取资源id{@link #getResIdByFieldName(String, Context, Class)}
 * 获取存储路径，如果sd卡存在，使用sd卡，否则存储本地{@link #getFileCustom}
 * 获取sd卡包名目录{@link #getSDcardPackage(Context)}
 * 获取存储音频的文件夹路径{@link #getVoicePath(Context)}
 * 获取存储图片的路径{@link #getIamgePath(Context)}
 */
public class FileUtil {
    /**
     * sd卡不存在
     */
    public static final int COPY_FILE_RESULT_TYPE_SDCARD_NOT_EXIST = 101;
    /**
     * 拷贝成功
     */
    public static final int COPY_FILE_RESULT_TYPE_COPY_SUCCESS = 102;
    /**
     * 拷贝失败
     */
    public static final int COPY_FILE_RESULT_TYPE_COPY_FAILD = 103;
    /**
     * 目录错误
     */
    public static final int COPY_FILE_RESULT_TYPE_DIR_ERROR = 104;
    /**
     * 源文件不存在
     */
    public static final int COPY_FILE_RESULT_TYPE_SOURCE_FILE_NOT_EXIST = 105;
    /**
     * 源文件已经存在
     */
    public static final int COPY_FILE_RESULT_TYPE_SOURCE_FILE_EXIST = 106;
    public static final String CACHE_DIR_NAME = "." + StringUtil.getMd5Value("courseware");
    public static final String CACHE_CATE_DIR_NAME = "." + StringUtil.getMd5Value("cate");
    public static final String CACHE_DATA_DIR_NAME = "." + StringUtil.getMd5Value("data");
    public static final String CACHE_VIDEO_DIR_NAME = "." + StringUtil.getMd5Value("video");

    /**
     * 判断sd卡是否安装
     *
     * @return
     * @author M.c
     */
    public static boolean existSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取SD卡的path
     *
     * @return
     * @author M.c
     * @since 2014-11-09
     */
    public static String getSdPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory(); // 获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    }

    /**
     * 读取assets下的文本数据
     *
     * @param fileName
     * @return
     */
    public static String getStringFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context
                    .getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取文本流文件
     *
     * @param is
     * @return
     */
    public static String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while (i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * 获得文件大小
     *
     * @param
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    private static long getFileSize(File f) throws IOException {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
        }
        return s;
    }

    /**
     * 返回文件大小
     *
     * @param
     * @return
     * @throws IOException
     */
    public static String formatFileSize(File f) {// 转换文件大小
        String fileSizeString = "";
        try {
            long fileS = getFileSize(f);
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileS < 1024) {
                fileSizeString = df.format((double) fileS) + "b";
            } else if (fileS < 1048576) {
                fileSizeString = df.format((double) fileS / 1024) + "kb";
            } else if (fileS < 1073741824) {
                fileSizeString = df.format((double) fileS / 1048576) + "mb";
            } else {
                fileSizeString = df.format((double) fileS / 1073741824) + "gb";
            }
        } catch (Exception e) {
        }
        return fileSizeString;
    }

    /**
     * 拷贝资源文件到sd卡
     *
     * @param context
     * @param resId
     * @param databaseFilename 如数据库文件拷贝到sd卡中
     */
    public static void copyResToSdcard(Context context, int resId,
                                       String databaseFilename) {// name为sd卡下制定的路径
        try {
            // 不存在得到数据库输入流对象
            InputStream is = context.getResources().openRawResource(resId);
            // 创建输出流
            FileOutputStream fos = new FileOutputStream(databaseFilename);
            // 将数据输出
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            // 关闭资源
            fos.close();
            is.close();
        } catch (Exception e) {
        }
    }

    /**
     * 拷贝文件(默认不删除源文件)
     *
     * @param context
     * @param fromPath 源文件完整路径
     * @param toPath   目标文件完整路径
     * @return 返回拷贝状态(详见该类静态变量: {@linkplain #COPY_FILE_RESULT_TYPE_COPY_FAILD},
     * {@link #COPY_FILE_RESULT_TYPE_COPY_SUCCESS},
     * {@link #COPY_FILE_RESULT_TYPE_DIR_ERROR},
     * {@link #COPY_FILE_RESULT_TYPE_SDCARD_NOT_EXIST},
     * {@link #COPY_FILE_RESULT_TYPE_SOURCE_FILE_EXIST},
     * {@link #COPY_FILE_RESULT_TYPE_SOURCE_FILE_NOT_EXIST})
     * @see #copyFile(Context, String, String, boolean)
     */
    public static int copyFile(Context context, String fromPath, String toPath) {
        return copyFile(context, fromPath, toPath, false);
    }

    /**
     * 拷贝文件(默认不删除源文件)
     *
     * @param context
     * @param fromFile 源文件
     * @param toFile   目标文件
     * @return 返回拷贝状态(详见该类静态变量: {@linkplain #COPY_FILE_RESULT_TYPE_COPY_FAILD},
     * {@link #COPY_FILE_RESULT_TYPE_COPY_SUCCESS},
     * {@link #COPY_FILE_RESULT_TYPE_DIR_ERROR},
     * {@link #COPY_FILE_RESULT_TYPE_SDCARD_NOT_EXIST},
     * {@link #COPY_FILE_RESULT_TYPE_SOURCE_FILE_EXIST},
     * {@link #COPY_FILE_RESULT_TYPE_SOURCE_FILE_NOT_EXIST})
     * @see #copyFile(Context, File, File,
     * boolean)
     */
    public static int copyFile(Context context, File fromFile, File toFile) {
        return copyFile(context, fromFile, toFile, false);
    }

    /**
     * 拷贝文件(可以选择是否删除源文件)
     *
     * @param context
     * @param fromPath    源文件路径
     * @param toPath      目标文件路径
     * @param isDeleteRes 是否删除源文件
     * @return 返回拷贝状态(详见该类静态变量: {@linkplain #COPY_FILE_RESULT_TYPE_COPY_FAILD},
     * {@link #COPY_FILE_RESULT_TYPE_COPY_SUCCESS},
     * {@link #COPY_FILE_RESULT_TYPE_DIR_ERROR},
     * {@link #COPY_FILE_RESULT_TYPE_SDCARD_NOT_EXIST},
     * {@link #COPY_FILE_RESULT_TYPE_SOURCE_FILE_EXIST},
     * {@link #COPY_FILE_RESULT_TYPE_SOURCE_FILE_NOT_EXIST})
     */
    public static int copyFile(Context context, String fromPath, String toPath,
                               boolean isDeleteRes) {
        if (!StringUtil.isBlank(fromPath) || !StringUtil.isBlank(toPath)) {
            File fromFile = new File(fromPath);
            File toFile = new File(toPath);
            return copyFile(context, fromFile, toFile, isDeleteRes);
        } else {
            return COPY_FILE_RESULT_TYPE_DIR_ERROR;
        }
    }

    /**
     * 拷贝文件(可以选择是否删除源文件)
     *
     * @param context
     * @param fromFile    源文件
     * @param toFile      目标文件
     * @param isDeleteRes 是否删除源文件
     * @return 返回拷贝状态 (详见该类静态变量: {@linkplain #COPY_FILE_RESULT_TYPE_COPY_FAILD},
     * {@link #COPY_FILE_RESULT_TYPE_COPY_SUCCESS},
     * {@link #COPY_FILE_RESULT_TYPE_DIR_ERROR},
     * {@link #COPY_FILE_RESULT_TYPE_SDCARD_NOT_EXIST},
     * {@link #COPY_FILE_RESULT_TYPE_SOURCE_FILE_EXIST},
     * {@link #COPY_FILE_RESULT_TYPE_SOURCE_FILE_NOT_EXIST})
     */

    private static int copyFile(Context context, File fromFile, File toFile,
                                boolean isDeleteRes) {
        if (fromFile == null || toFile == null) {
            return COPY_FILE_RESULT_TYPE_DIR_ERROR;
        }
        if (existSdcard()) {
            try {
                if (fromFile.exists()) {
                    String toPath = toFile.getAbsolutePath();
                    String toDir = toPath.substring(0, toPath.lastIndexOf("/"));
                    File toDirFile = new File(toDir);
                    if (!toDirFile.exists()) {
                        toDirFile.mkdirs();
                    }
                    if (!toFile.exists()) {
                        toFile.createNewFile();
                    }
                    FileInputStream in = new FileInputStream(fromFile);
                    FileOutputStream out = new FileOutputStream(toFile);
                    byte[] buffer = new byte[1024];
                    int count = 0;
                    while ((count = in.read(buffer)) > 0) {
                        out.write(buffer, 0, count);
                    }
                    // 关闭资源
                    out.close();
                    in.close();
                } else {
                    return COPY_FILE_RESULT_TYPE_SOURCE_FILE_NOT_EXIST;
                }
                if (isDeleteRes) {// 判断是否删除源文件
                    if (fromFile.exists()) {
                        fromFile.delete();
                    }
                }
                if (toFile.exists()) {
                    return COPY_FILE_RESULT_TYPE_COPY_SUCCESS;
                } else {
                    return COPY_FILE_RESULT_TYPE_COPY_FAILD;
                }
            } catch (Exception e) {
                return COPY_FILE_RESULT_TYPE_COPY_FAILD;
            }
        } else {
            return COPY_FILE_RESULT_TYPE_SDCARD_NOT_EXIST;
        }
    }

    public static void writeStrToFile(String path, String content) {
        try {
            confirmFileExist(path);
            FileOutputStream fos = new FileOutputStream(path);
            Writer os = new OutputStreamWriter(fos, "UTF-8");
            os.write(content);
            os.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean confirmFileExist(String path) throws IOException {
        String toDir = path.substring(0, path.lastIndexOf("/"));
        File toDirFile = new File(toDir);
        File toFile = new File(path);
        if (!toDirFile.exists()) {
            if (!toDirFile.mkdirs()) {
                return false;
            }
        }
        if (!toFile.exists()) {
            if (!toFile.createNewFile()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 创建文件至SD卡
     *
     * @param
     * @param dirName
     * @param fileName /dirname/+文件名
     * @return
     */
    public static File createFile(String dirName, String fileName) {
        if (existSdcard()) {
            File dir = new File(getSdPath() + dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return new File(dir, fileName);
        }
        return null;
    }

    /**
     * 创建文件夹到SD卡根目录下
     *
     * @param
     * @param dirName 文件夹名字 栗子: /dirname
     * @return
     */
    public static File createFileDir(String dirName) {
        if (existSdcard()) {
            File dir = new File(getSdPath() + dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir;
        }
        return null;
    }

    /**
     * 获取文件后缀名
     *
     * @param file
     * @see #MIME_MapTable
     */
    public String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        String end = fName.substring(dotIndex, fName.length()).toLowerCase(Locale.getDefault());
        if (end == "")
            return type;
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    /**
     * 后缀名格式表
     */
    private final String[][] MIME_MapTable = {{".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"}, {".bmp", "image/bmp"},
            {".c", "text/plain"}, {".class", "application/octet-stream"},
            {".conf", "text/plain"}, {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".exe", "application/octet-stream"}, {".gif", "image/gif"},
            {".gtar", "application/x-gtar"}, {".gz", "application/x-gzip"},
            {".h", "text/plain"}, {".htm", "text/html"},
            {".html", "text/html"}, {".jar", "application/java-archive"},
            {".java", "text/plain"}, {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"}, {".js", "application/x-javascript"},
            {".log", "text/plain"}, {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"}, {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"}, {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"}, {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"}, {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"}, {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"},
            {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"}, {".xml", "text/xml"},
            {".xml", "text/plain"}, {".z", "application/x-compress"},
            {".zip", "application/zip"}, {"", "*/*"}};

    /**
     * 计算sdcard上的剩余空间,新方法无法支持api-18以下的系统
     *
     * @return
     */
    @SuppressLint("NewApi")
    public static long freeSpaceOnSdCard() {
        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks * blockSize;

    }

    /**
     * 删除文件及文件目录下的所有文件
     *
     * @param file
     */
    public static void deleteFileUnderFolder(File file) {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    return;
                }
                for (File f : childFile) {
                    deleteFileUnderFolder(f);
                }
            }
        }
    }

    /**
     * 根据文件名获取资源id
     *
     * @param variableName
     * @param context
     * @param c
     * @return
     */
    public static int getResIdByFieldName(String variableName, Context context,
                                          Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取存储路径，如果sd卡存在，使用sd卡，否则存储本地
     *
     * @param folderName
     * @param fileName
     * @param format
     * @return
     */
    public static File getFileCustom(String folderName, String fileName,
                                     String format) {
        File file = null;
        String saveDir = "";
        if (existSdcard()) {
            saveDir = getSdPath() + File.separator + folderName;
        } else {
            saveDir = File.separator + folderName;
        }
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (null == format || "".equals(format)) {
            file = new File(saveDir, fileName);
        } else {
            file = new File(saveDir, fileName + "." + format);
        }
        file.delete();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 获取sd卡包名目录
     *
     * @param context
     * @return
     */
    public static String getSDcardPackage(Context context) {

        //        return getSdPath() + "/" + context.getPackageName();
        return getSdPath() + "/" + context.getPackageName();

    }

    /**
     * 获取裁剪保存路径
     *
     * @param context
     * @param imageName
     * @return
     */
    public static File getCropFile(Context context, String imageName) {
        String saveDir = getSDcardPackage(context) + "/crop";
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(saveDir, imageName);
    }

    public static File getCropFileDir(Context context) {
        String saveDir = getSDcardPackage(context) + "/crop";
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取存储音频的文件夹路径
     *
     * @param context
     * @return
     */
    public static String getVoicePath(Context context) {
        String voicePath = getSDcardPackage(context) + "/voice";
        File dir = new File(voicePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return voicePath;
    }

    public static void createLotOfDir(Context context) {
        for (int i = 0; i < 200; i++) {
            String dirPath = getSDcardPackage(context) + "/." + StringUtil.getMd5Value(i + "");
            for (int j = 0; j < 20; j++) {
                String childPath = dirPath + "/." + StringUtil.getMd5Value(j + "child");
                File dir = new File(childPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
        }

    }

    /**
     * 获取存储图片的路径
     *
     * @param context
     * @return
     */
    public static String getIamgePath(Context context) {
        String voicePath = getSDcardPackage(context) + "/image";
        File dir = new File(voicePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return voicePath;
    }

    /**
     * 获取分享储图片的路径
     *
     * @param context
     * @return
     */
    public static String getShareImagePath(Context context) {
        String voicePath = getSDcardPackage(context) + "/shareimg";
        File dir = new File(voicePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return voicePath;
    }

    /**
     * 获取存储裁剪的路径
     *
     * @param context
     * @return
     */
    public static String getCropPath(Context context) {
        String cropPath = getIamgePath(context) + File.separator + "/crop";
        File dir = new File(cropPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return cropPath;
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }


}
