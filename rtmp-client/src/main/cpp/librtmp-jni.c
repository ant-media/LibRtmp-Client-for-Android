#include <malloc.h>
#include "librtmp-jni.h"
#include "rtmp.h"
#include <android/log.h>
//
// Created by faraklit on 01.01.2016.
//


#define  LOG_TAG    "someTag"

#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

RTMP *rtmp = NULL;
/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    open
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_net_butterflytv_rtmp_1client_RtmpClient_open
        (JNIEnv * env, jobject thiz, jstring url_, jboolean isPublishMode) {

    const char *url = (*env)->GetStringUTFChars(env, url_, 0);
    rtmp = RTMP_Alloc();
    if (rtmp == NULL) {
        throwRtmpIOException(env, -1, "Rtmp allocation problem");
        return -1;
    }

	RTMP_Init(rtmp);
	int ret = RTMP_SetupURL(rtmp, url);

    if (!ret) {
        RTMP_Free(rtmp);
        throwRtmpIOException(env, -2, "Rtmp setup url problem, check the rtmp url");
        return -2;
    }
    if (isPublishMode) {
        RTMP_EnableWrite(rtmp);
    }

	ret = RTMP_Connect(rtmp, NULL);
    if (!ret) {
        RTMP_Free(rtmp);
        throwRtmpIOException(env, -3, "Rtmp connect problem, check that you are connected to network and rtmp server is running");
        return -3;
    }
	ret = RTMP_ConnectStream(rtmp, 0);

    if (!ret) {
        throwRtmpIOException(env, -4, "Rtmp connect stream error");
        return -4;
    }
    (*env)->ReleaseStringUTFChars(env, url_, url);
    return 1;
}



/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    read
 * Signature: ([CI)I
 */
JNIEXPORT jint JNICALL Java_net_butterflytv_rtmp_1client_RtmpClient_read
        (JNIEnv * env, jobject thiz, jbyteArray data_, jint offset, jint size) {

    if (rtmp == NULL) {
        throwIOException(env, "First call open function");
    }
    int connected = RTMP_IsConnected(rtmp);
    if (!connected) {
        throwIOException(env, "Connection to server is lost");
    }

    char* data = malloc(size*sizeof(char));

    int readCount = RTMP_Read(rtmp, data, size);

    if (readCount > 0) {
        (*env)->SetByteArrayRegion(env, data_, offset, readCount, data);  // copy
    }
    free(data);

 	return readCount;
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    write
 * Signature: ([CI)I
 */
JNIEXPORT jint JNICALL Java_net_butterflytv_rtmp_1client_RtmpClient_write
        (JNIEnv * env, jobject thiz, jcharArray data, jint size) {

    if (rtmp == NULL) {
        throwIOException(env, "First call open function");
    }

    int connected = RTMP_IsConnected(rtmp);
    if (!connected) {
        throwIOException(env, "Connection to server is lost");
    }

    return RTMP_Write(rtmp, data, size);
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    seek
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_butterflytv_rtmp_1client_RtmpClient_seek
        (JNIEnv * env, jobject thiz, jint seekTime) {

    return 0;
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    pause
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_net_butterflytv_rtmp_1client_RtmpClient_pause
        (JNIEnv * env, jobject thiz, jint pauseTime) {

    if (rtmp == NULL) {
        throwIOException(env, "First call open function");
    }
    return RTMP_Pause(rtmp, pauseTime);
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_butterflytv_rtmp_1client_RtmpClient_close
        (JNIEnv * env, jobject thiz) {

    if (rtmp != NULL) {
        RTMP_Close(rtmp);
        RTMP_Free(rtmp);
    }
    return 0;
}


JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_1client_RtmpClient_isConnected(JNIEnv *env, jobject instance)
{
    if (rtmp == NULL) {
        return 0;
    }
     int connected = RTMP_IsConnected(rtmp);
     if (connected) {
        return 1;
     }
     else {
        return 0;
     }
}

jint throwIOException (JNIEnv *env, char *message )
{
    jclass Exception = (*env)->FindClass(env, "java/io/IOException");
    return (*env)->ThrowNew(env, Exception, message);
}

jint throwRtmpIOException(JNIEnv *env, int errorCode, char* message)
{
    jclass cls = (*env)->FindClass(env, "net/butterflytv/rtmp_client/RtmpClient$RtmpIOException");
    jmethodID mid = (*env)->GetMethodID(env, cls, "<init>", "(ILjava/lang/String;)V");
    jstring jmessage = (*env)->NewStringUTF(env, message);
    jthrowable e = (*env)->NewObject(env, cls, mid, errorCode, jmessage);

    /* Now throw the exception */
    return (*env)->Throw(env, e);
}
