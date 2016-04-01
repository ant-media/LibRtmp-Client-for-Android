#include <jni.h>

#include "flvmuxer/xiecc_rtmp.h"


/**
 * if it returns bigger than 0 it is successfull
 */
JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_1client_RTMPMuxer_open(JNIEnv *env, jobject instance, jstring url_) {
    const char *url = (*env)->GetStringUTFChars(env, url_, 0);

    int result = rtmp_open_for_write(url);

    (*env)->ReleaseStringUTFChars(env, url_, url);
    return result;
}


JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_1client_RTMPMuxer_writeAudio(JNIEnv *env, jobject instance,
                                                       jbyteArray data_, jint offset, jint length,
                                                       jint timestamp) {
    jbyte *data = (*env)->GetByteArrayElements(env, data_, NULL);

    jint result = rtmp_sender_write_audio_frame(data, length, timestamp, 0);

    (*env)->ReleaseByteArrayElements(env, data_, data, 0);
    return result;
}

JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_1client_RTMPMuxer_writeVideo(JNIEnv *env, jobject instance,
                                                       jbyteArray data_, jint offset, jint length,
                                                       jint timestamp) {
    jbyte *data = (*env)->GetByteArrayElements(env, data_, NULL);

    jint result = rtmp_sender_write_video_frame(data, length, timestamp, 0, 0);

    (*env)->ReleaseByteArrayElements(env, data_, data, 0);

    return result;
}

JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_1client_RTMPMuxer_close(JNIEnv *env, jobject instance) {
    rtmp_close();

    return 0;

}

JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_1client_RTMPMuxer_isConnected(JNIEnv *env, jobject instance) {
    return rtmp_is_connected();
}