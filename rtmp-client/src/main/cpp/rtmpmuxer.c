#include <jni.h>
#include <malloc.h>
#include <rtmp.h>

#include "flvmuxer/xiecc_rtmp.h"


/**
 * if it returns bigger than 0 it is successful
 */
JNIEXPORT jint JNICALL
Java_io_antmedia_rtmp_1client_RTMPMuxer_open(JNIEnv* env, jobject thiz, jstring url_,
                                                 jint video_width, jint video_height) {
    const char *url = (*env)->GetStringUTFChars(env, url_, NULL);

    int result = rtmp_open_for_write(url, video_width, video_height);

    (*env)->ReleaseStringUTFChars(env, url_, url);
    return result;
}

JNIEXPORT jint JNICALL
Java_io_antmedia_rtmp_1client_RTMPMuxer_writeAudio(JNIEnv* env, jobject thiz, jbyteArray data_,
                                                       jint offset, jint length, jlong timestamp) {
    jbyte *data = (*env)->GetByteArrayElements(env, data_, NULL);

    jint result = rtmp_sender_write_audio_frame(&data[offset], length, timestamp, 0);

    (*env)->ReleaseByteArrayElements(env, data_, data, JNI_ABORT);
    return result;
}

JNIEXPORT jint JNICALL
Java_io_antmedia_rtmp_1client_RTMPMuxer_writeVideo(JNIEnv* env, jobject thiz, jbyteArray data_,
                                                       jint offset, jint length, jlong timestamp) {
    jbyte *data = (*env)->GetByteArrayElements(env, data_, NULL);

    jint result = rtmp_sender_write_video_frame(&data[offset], length, timestamp, 0, 0);

    (*env)->ReleaseByteArrayElements(env, data_, data, JNI_ABORT);

    return result;
}

JNIEXPORT jint JNICALL
Java_io_antmedia_rtmp_1client_RTMPMuxer_close(JNIEnv* env, jobject thiz) {
    rtmp_close();

    return 0;

}

JNIEXPORT jboolean JNICALL
Java_io_antmedia_rtmp_1client_RTMPMuxer_isConnected(JNIEnv* env, jobject thiz) {
    return rtmp_is_connected() ? true : false;
}

JNIEXPORT jint JNICALL
Java_io_antmedia_rtmp_1client_RTMPMuxer_read(JNIEnv* env, jobject thiz, jbyteArray data_,
                                                 jint offset, jint size) {

    char* data = malloc(size);

    int readCount = rtmp_read_date(data, size);

    if (readCount > 0) {
        (*env)->SetByteArrayRegion(env, data_, offset, readCount, data);  // copy
    }
    free(data);

    return readCount;

}
