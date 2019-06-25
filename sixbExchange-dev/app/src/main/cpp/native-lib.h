//
// Created by chenenyu on 2017/3/15.
//

#include <jni.h>

#ifndef ANDROIDSECURITY_NATIVE_SECURITY_H
#define ANDROIDSECURITY_NATIVE_SECURITY_H

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL
Java_com_gqfbitmex_utils_NdkUtils_key(JNIEnv *env, jclass type);


#ifdef __cplusplus
}
#endif

#endif //ANDROIDSECURITY_NATIVE_SECURITY_H
