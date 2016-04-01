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
     * url of the stream
     * @param isPublishMode
     * if this is an publication it is true,
     * if connection is for getting stream it is false
     * @return return a minus value if it fails
     * -1 RTMP_Alloc error
     * -2 RTMP_SetupURL error
     * -3 RTMP_Connect error
     * -4 RTMP_ConnectStream error
     *
     * returns 1 if it is successful
     */
    public native int open(String url, boolean isPublishMode);

    /**
     * read data from rtmp connection
     * @param data
     * buffer that will be filled
     * @param offset
     * offset to read data
     * @param size
     * size of the data to be reat
     * @return
     * number of bytes to be read
     */
    public native int read(byte[] data, int offset, int size);

    public native int write(byte[] data);

    public native int seek(int seekTime);

    public native int pause(int pause);

    /**
     *
     * @return 1 if it is connected
     * 0 if it is not connected
     */
    public native int isConnected();

    /**
     * closes the connection. Dont forget to call
     * @return 0
     */
    public native int close();

}
