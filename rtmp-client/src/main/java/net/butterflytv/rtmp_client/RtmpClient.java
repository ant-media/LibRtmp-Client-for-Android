package net.butterflytv.rtmp_client;

import java.io.IOException;

/**
 * Created by faraklit on 01.01.2016.
 */
public class RtmpClient {

    static {
        System.loadLibrary("rtmp-jni");
    }



    public final static int OPEN_ERROR_ALLOC = -1;
    public final static int OPEN_ERROR_SETUP_URL = -2;
    public final static int OPEN_ERROR_CONNECT = -3;
    public final static int OPEN_ERROR_CONNECT_STREAM = -4;
    public final static int OPEN_SUCCESS = 1;

    /**
     * opens the rtmp url
     * @param url
     * url of the stream
     * @param isPublishMode
     * if this is an publication it is true,
     * if connection is for getting stream it is false
     * @return return a minus value if it fails
     * returns
     * {@link #OPEN_ERROR_ALLOC} if there is a problem in memory allocation
     * {@link #OPEN_ERROR_SETUP_URL} if there is a problem in setting url
     * {@link #OPEN_ERROR_CONNECT} if there is a problem in connecting to the rtmp server
     * {@link #OPEN_ERROR_CONNECT_STREAM} if there is a problem in connecting stream
     *
     * returns {@link #OPEN_SUCCESS} if it is successful
     */
    public native int open(String url, boolean isPublishMode);

    /**
     * read data from rtmp connection
     *
     * @param data
     * buffer that will be filled
     * @param offset
     * offset to read data
     * @param size
     * size of the data to be reat
     * @return
     * number of bytes to be read
     *
     * if it returns 0, it means stream is complete
     *  and close function can be called.
     *
     * There is no negative return value
     *
     *  @throws IOException if connection is not opened or connection to server is lost
     *
     */

    public native int read(byte[] data, int offset, int size) throws IOException;

    /**
     *
     * @param data
     * @return number of bytes written
     * @throws IOException if connection is not opened or connection to server is lost
     */
    public native int write(byte[] data) throws IOException;

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
