#include <malloc.h>
#include "librtmp-jni.h"
#include "rtmp.h"
#include <android/log.h>
//
// Created by faraklit on 01.01.2016.
//


#define  LOG_TAG    "someTag"

#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

//RTMP *rtmp = NULL;


JNIEXPORT jlong JNICALL
Java_net_butterflytv_rtmp_1client_RtmpClient_nativeAlloc(JNIEnv *env, jobject instance) {
    RTMP *rtmp = RTMP_Alloc();
    if (rtmp == NULL) {
        return -1;
    }
    return (long)rtmp;
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    open
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_net_butterflytv_rtmp_1client_RtmpClient_nativeOpen
        (JNIEnv * env, jobject thiz, jstring url_, jboolean isPublishMode, jlong rtmpPointer) {

    const char *url = (*env)->GetStringUTFChars(env, url_, 0);
    RTMP *rtmp = (RTMP *) rtmpPointer;
   // rtmp = RTMP_Alloc();
    if (rtmp == NULL) {
        return -1;
    }

	RTMP_Init(rtmp);
	int ret = RTMP_SetupURL(rtmp, url);

    if (!ret) {
        RTMP_Free(rtmp);
        return -2;
    }
    if (isPublishMode) {
        RTMP_EnableWrite(rtmp);
    }

	ret = RTMP_Connect(rtmp, NULL);
    if (!ret) {
        RTMP_Free(rtmp);
        return -3;
    }
	ret = RTMP_ConnectStream(rtmp, 0);

    if (!ret) {
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
JNIEXPORT jint JNICALL Java_net_butterflytv_rtmp_1client_RtmpClient_nativeRead
        (JNIEnv * env, jobject thiz, jbyteArray data_, jint offset, jint size, jlong rtmpPointer) {

    RTMP *rtmp = (RTMP *) rtmpPointer;
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
    if (readCount == 0) {
        return -1;
    }
 	return readCount;
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    write
 * Signature: ([CI)I
 */
JNIEXPORT jint JNICALL Java_net_butterflytv_rtmp_1client_RtmpClient_nativeWrite
        (JNIEnv * env, jobject thiz, jcharArray data, jint size, jlong rtmpPointer) {

    RTMP *rtmp = (RTMP *) rtmpPointer;
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
JNIEXPORT bool JNICALL Java_net_butterflytv_rtmp_1client_RtmpClient_nativePause
        (JNIEnv * env, jobject thiz, jboolean pause, jlong rtmpPointer) {

    RTMP *rtmp = (RTMP *) rtmpPointer;
    if (rtmp == NULL) {
        throwIOException(env, "First call open function");
    }

    int DoPause = 0;
    if (pause == JNI_TRUE) {
        DoPause = 1;
    }
    return RTMP_Pause(rtmp, DoPause);
}

/*
 * Class:     net_butterflytv_rtmp_client_RtmpClient
 * Method:    close
 * Signature: ()I
 */
JNIEXPORT void JNICALL Java_net_butterflytv_rtmp_1client_RtmpClient_nativeClose
        (JNIEnv * env, jobject thiz, jlong rtmpPointer) {

    RTMP *rtmp = (RTMP *) rtmpPointer;
    if (rtmp != NULL) {
        RTMP_Close(rtmp);
        RTMP_Free(rtmp);
    }
}


JNIEXPORT bool JNICALL
Java_net_butterflytv_rtmp_1client_RtmpClient_nativeIsConnected(JNIEnv *env, jobject instance, jlong rtmpPointer)
{
    RTMP *rtmp = (RTMP *) rtmpPointer;
    if (rtmp == NULL) {
        return false;
    }
     int connected = RTMP_IsConnected(rtmp);
     if (connected) {
        return true;
     }
     else {
        return false;
     }
}

jint throwIOException (JNIEnv *env, char *message )
{
    jclass Exception = (*env)->FindClass(env, "java/io/IOException");
    return (*env)->ThrowNew(env, Exception, message);
}
