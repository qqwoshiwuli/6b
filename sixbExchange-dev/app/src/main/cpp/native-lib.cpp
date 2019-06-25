#include <jni.h>
#include <android/log.h>
#include <string>
#include "native-lib.h"

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "security", __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, "security", __VA_ARGS__))


extern "C"
JNIEXPORT jfloat JNICALL Java_com_sixbexchange_utils_NdkUtils_getSum(
        JNIEnv *env,
        jobject,
        jint a,
        jint b,
        jfloatArray data,
        jint n
) {
    jfloat sum = 0;
    jfloat *num = env->GetFloatArrayElements(data, NULL);
    for (int i = a; i <= b; i++) {
        sum += num[i];
    }
    return sum / n;
}

static int verifySign(JNIEnv *env);

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return JNI_ERR;
    }
    if (verifySign(env) == JNI_OK) {
        return JNI_VERSION_1_4;
    }
    LOGE("签名不一致!");
    return JNI_ERR;
}

static jobject getApplication(JNIEnv *env) {
    jobject application = NULL;
    jclass activity_thread_clz = env->FindClass("android/app/ActivityThread");
    if (activity_thread_clz != NULL) {
        jmethodID currentApplication = env->GetStaticMethodID(
                activity_thread_clz, "currentApplication", "()Landroid/app/Application;");
        if (currentApplication != NULL) {
            application = env->CallStaticObjectMethod(activity_thread_clz, currentApplication);
        } else {
            LOGE("Cannot find method: currentApplication() in ActivityThread.");
        }
        env->DeleteLocalRef(activity_thread_clz);
    } else {
        LOGE("Cannot find class: android.app.ActivityThread");
    }

    return application;
}

static const char *SIGN = "3082033b30820223a00302010202045c67827f300d06092a864886f70d01010b0500304e310b300906035504061302626d310b300906035504081302626d310b300906035504071302626d310b3009060355040a1302626d310b3009060355040b1302626d310b300906035504031302626d301e170d3139303131313037343431375a170d3434303130353037343431375a304e310b300906035504061302626d310b300906035504081302626d310b300906035504071302626d310b3009060355040a1302626d310b3009060355040b1302626d310b300906035504031302626d30820122300d06092a864886f70d01010105000382010f003082010a0282010100a4b69e25d889361a7ab2907d1895f824b1b48708ccfb4bc1e6097c487210d25d75b6c5e317f74d11257a432c66a5a1c09f1404c9ef0cf5aaadac05b2260516c1650ef9dbeb1842906b726addefd58b3e2fd94711e3d42497b94921dc9b81f6acc1898680ed1fb4adb3ab97dc0357d38861d6bdaf9b8c66863c4076fa96c9fc21fee1c1e52e0c1a3c9e2e2a181402fcc0aa63158bd39488518b8d7f8558788b1c1758aeb8c4fc94d15e76b9318310a7ec5c52add9616ab9cf3338c8bd3ee28e29e656329c574a899ed5999f1a40070b40ab8b9f699ed913b922f5ac39f9a959dbb174143e650654634032a22373eb07c69c394944ed49bb3519b0d04b901b9f870203010001a321301f301d0603551d0e04160414182e77953772a4da946a1062e95ccd2882a6efc8300d06092a864886f70d01010b050003820101005d13c3ee5cbbf7e2ddbdda9f1968bdb3513fd35473e69b5e1798dcbde9adec491ee2b5d9d329531b512df0df055c6fb19732ab32343143edaa1286153b8dc33a685c1416065a08d9dca5e083bc9eddb008652eae7b81179bba4fa6de6910d8e7c410d8cf7e7115b34e3eeacc61c55ac2795a335c7aa7d9ff0150ca6b6fbbe39f44f256a57a10e3942c5e26a9ddef42d09debddc7f7a5485dff09aa69efaa713cb1c671bf9c5cfd8cb90b68560000f8cbcd9b3f8909bcc3cdf4738c4683be90dca91ac933cb80113a80531a6a7eace795c53eb50b59e7c7c0a3031993dc26dab3238b0d9fdb509b6ea6463c06a579f214b913898e22c6a612315126e5ad49f088";

static int verifySign(JNIEnv *env) {
    // Application object
    return JNI_OK;

    jobject application = getApplication(env);
    if (application == NULL) {
        return JNI_ERR;
    }
    // Context(ContextWrapper) class
    jclass context_clz = env->GetObjectClass(application);
    // getPackageManager()
    jmethodID getPackageManager = env->GetMethodID(context_clz, "getPackageManager",
                                                   "()Landroid/content/pm/PackageManager;");
    // android.content.pm.PackageManager object
    jobject package_manager = env->CallObjectMethod(application, getPackageManager);
    // PackageManager class
    jclass package_manager_clz = env->GetObjectClass(package_manager);
    // getPackageInfo()
    jmethodID getPackageInfo = env->GetMethodID(package_manager_clz, "getPackageInfo",
                                                "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    // context.getPackageName()
    jmethodID getPackageName = env->GetMethodID(context_clz, "getPackageName",
                                                "()Ljava/lang/String;");
    // call getPackageName() and cast from jobject to jstring
    jstring package_name = (jstring) (env->CallObjectMethod(application, getPackageName));
    // PackageInfo object
    jobject package_info = env->CallObjectMethod(package_manager, getPackageInfo, package_name, 64);
    // class PackageInfo
    jclass package_info_clz = env->GetObjectClass(package_info);
    // field signatures
    jfieldID signatures_field = env->GetFieldID(package_info_clz, "signatures",
                                                "[Landroid/content/pm/Signature;");
    jobject signatures = env->GetObjectField(package_info, signatures_field);
    jobjectArray signatures_array = (jobjectArray) signatures;
    jobject signature0 = env->GetObjectArrayElement(signatures_array, 0);
    jclass signature_clz = env->GetObjectClass(signature0);

    jmethodID toCharsString = env->GetMethodID(signature_clz, "toCharsString",
                                               "()Ljava/lang/String;");
    // call toCharsString()
    jstring signature_str = (jstring) (env->CallObjectMethod(signature0, toCharsString));

    // release
    env->DeleteLocalRef(application);
    env->DeleteLocalRef(context_clz);
    env->DeleteLocalRef(package_manager);
    env->DeleteLocalRef(package_manager_clz);
    env->DeleteLocalRef(package_name);
    env->DeleteLocalRef(package_info);
    env->DeleteLocalRef(package_info_clz);
    env->DeleteLocalRef(signatures);
    env->DeleteLocalRef(signature0);
    env->DeleteLocalRef(signature_clz);

    const char *sign = env->GetStringUTFChars(signature_str, NULL);
    if (sign == NULL) {
        LOGE("分配内存失败");
        return JNI_ERR;
    }

    //LOGI("应用中读取到的签名为：%s", sign);
   // LOGI("native中预置的签名为：%s", SIGN);
    int result = strcmp(sign, SIGN);
    // 使用之后要释放这段内存
    env->ReleaseStringUTFChars(signature_str, sign);
    env->DeleteLocalRef(signature_str);
    if (result == 0) { // 签名一致
        return JNI_OK;
    }

    return JNI_ERR;
}


jstring Java_com_sixbexchange_utils_NdkUtils_key(JNIEnv *env, jclass type) {
    return env->NewStringUTF("v7qdDkoLPfZUFquMSIUNnacw7djoS9c4nvoqrONFL85fi9B8mEe8goxMcjhn6WFp");
}
