package net.butterflytv.rtmp_client;

import java.io.IOException;

/**
 * Created by faraklit on 01.01.2016.
 */
public class RtmpClient {

    static {
        System.loadLibrary("rtmp-jni");
    }


    public final static int OPEN_SUCCESS = 1;

    public static class RtmpIOException extends IOException {

        /**
         * it means there is a problem in memory allocation
         */
        public final static int OPEN_ALLOC = -1;

        /**
         * it means there is a problem in setting url, check the rtmp url
         */
        public final static int OPEN_SETUP_URL = -2;

        /**
         *  it means there is a problem in connecting to the rtmp server,
         *  check there is an active network connection,
         *  check rtmp server is running,
         */
        public final static int OPEN_CONNECT = -3;

        /**
         *  it means there is a problem in connecting stream
         */
        public final static int OPEN_CONNECT_STREAM = -4;


        public final int errorCode;

        public RtmpIOException(int errorCode) {
            this.errorCode = errorCode;
        }

    }

    public void open(String url, boolean isPublishMode) throws RtmpIOException {
        int result = nativeOpen(url, isPublishMode);
        if (result != OPEN_SUCCESS) {
            throw new RtmpIOException(result);
        }
    }

    /**
     * opens the rtmp url
     * @param url
     * url of the stream
     * @param isPublishMode
     * if this is an publication it is true,
     * if connection is for getting stream it is false
     * @return return a minus value if it fails
     * returns

     *
     * returns {@link #OPEN_SUCCESS} if it is successful, throws RtmpIOException if it is failed
     */
    private native int nativeOpen(String url, boolean isPublishMode);

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
     * if it returns -1, it means stream is complete
     *  and close function can be called.
     *
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


    /**
     *
     * @param pause
     * @return true if stream can be paused, returns false if it is not paused
     */
    public native boolean pause(int pause);

    /**
     *
     * @return true if it is connected
     * false if it is not connected
     */
    public native boolean isConnected();

    /**
     * closes the connection. Dont forget to call
     * @return 0
     */
    public native void close();

}
