//
// Created by Administrator on 2019/9/11.
//

#include "com_github_jiawei_intelligent_parking_system_view_LinuxActivity.h"

JNIEXPORT jstring JNICALL Java_com_github_jiawei_intelligent_1parking_1system_view_LinuxActivity_stringFromJNI
  (JNIEnv *env, jobject){
  return env->NewStringUTF("Hello JiaWei from C++");
  }

