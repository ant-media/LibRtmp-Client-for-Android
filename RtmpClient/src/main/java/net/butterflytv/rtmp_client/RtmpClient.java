package net.butterflytv.rtmp_client;

/**
 * Created by faraklit on 01.01.2016.
 */
public class RtmpClient {

    static {
        System.loadLibrary("rtmp-jni");
    }

    /**
     * opens the rtmp url
     * @param url
     * @param isPublishMode
     * @return return a minus value if it fails
     * -1 RTMP_Alloc error
     * -2 RTMP_SetupURL error
     * -3 RTMP_Connect error
     * -4 RTMP_ConnectStream error
     *
     * returns 1 if it is successful
     */
    public native int open(String url, boolean isPublishMode);

    public native int read(byte[] data, int offset, int size);

    public native int write(byte[] data);

    public native int seek(int seekTime);

    public native int pause(int pause);

    public native int close();

}
