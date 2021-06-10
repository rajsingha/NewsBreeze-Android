#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_newsbreeze_app_helpers_Konstant_getAPIKey(JNIEnv *env, jobject thiz) {
    const std::string apiKey = "{Your newsapi goes here}"; // put your newsapi here.
    return env->NewStringUTF(apiKey.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_newsbreeze_app_helpers_Konstant_getRootURL(JNIEnv *env, jobject thiz) {
    std::string rootURL = "https://newsapi.org/v2/";
    return env->NewStringUTF(rootURL.c_str());
}