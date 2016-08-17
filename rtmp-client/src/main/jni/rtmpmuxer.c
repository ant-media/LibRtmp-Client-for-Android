#include <jni.h>
#include <malloc.h>
#include <rtmp.h>

#include "flvmuxer/xiecc_rtmp.h"


/**
 * if it returns bigger than 0 it is successfull
 */
JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_1client_RTMPMuxer_open(JNIEnv *env, jobject instance, jstring url_, jint video_width, jint video_height) {
    const char *url = (*env)->GetStringUTFChars(env, url_, 0);

    int result = rtmp_open_for_write(url, video_width, video_height);

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

JNIEXPORT jint JNICALL
Java_net_butterflytv_rtmp_1client_RTMPMuxer_read(JNIEnv *env, jobject instance, jbyteArray data_,
                                                 jint offset, jint size) {


    char* data = malloc(size*sizeof(char));

    int readCount = rtmp_read_date(data, size);

    if (readCount > 0) {
        (*env)->SetByteArrayRegion(env, data_, offset, readCount, data);  // copy
    }
    free(data);

    return readCount;

}