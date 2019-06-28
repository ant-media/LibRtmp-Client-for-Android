package net.butterflytv.rtmp_client;

import java.io.IOException;

/**
 * Created by faraklit on 01.01.2016.
 */
public class RtmpClient {

    static {
        System.loadLibrary("rtmp-jni");
    }


    private final static int OPEN_SUCCESS = 1;
    private final static int TIMEOUT_IN_MS = 10000;
    private long rtmpPointer = 0;

    private int rtmpError = RtmpIOException.RTMP_ERROR_NONE;

    /** Socket send timeout value in milliseconds */
    private int sendTimeoutInMs = TIMEOUT_IN_MS;
    /** Socket receive timeout value in seconds */
    private int receiveTimeoutInMs = TIMEOUT_IN_MS;

    public static class RtmpIOException extends IOException {

        /**
         * No error
         */
        public final static int RTMP_ERROR_NONE = 0;

        /**
         * RTMP read has received an EOF or READ_COMPLETE from the server
         */
        public final static int RTMP_READ_DONE = -1;

        /**
         * it means there is a problem in memory allocation
         */
        public final static int OPEN_ALLOC = -2;

        /**
         * it means there is a problem in setting url, check the rtmp url
         */
        public final static int OPEN_SETUP_URL = -3;

        /**
         *  it means there is a problem in connecting to the rtmp server,
         *  check there is an active network connection,
         *  check rtmp server is running,
         */
        public final static int OPEN_CONNECT = -4;

        /**
         *  it means there is a problem in connecting stream
         */
        public final static int OPEN_CONNECT_STREAM = -5;

        /**
         * Received an unknown option from the RTMP server
         */
        public final static int UNKNOWN_RTMP_OPTION = -6;

        /**
         * RTMP server sent a packet with unknown AMF type
         */
        public final static int UNKNOWN_RTMP_AMF_TYPE = -7;

        /**
         * DNS server is not reachable
         */
        public final static int DNS_NOT_REACHABLE = -8;

        /**
         * Could not establish a socket connection to the server
         */
        public final static int SOCKET_CONNECT_FAIL = -9;

        /**
         * SOCKS negotiation failed
         */
        public final static int SOCKS_NEGOTIATION_FAIL = -10;

        /**
         * Could not create a socket to connect to RTMP server
         */
        public final static int SOCKET_CREATE_FAIL = -11;

        /**
         * SSL connection requested but not supported by the client
         */
        public final static int NO_SSL_TLS_SUPP = -12;

        /**
         * Could not connect to the server for handshake
         */
        public final static int HANDSHAKE_CONNECT_FAIL = -13;

        /**
         * Handshake with the server failed
         */
        public final static int HANDSHAKE_FAIL = -14;

        /**
         * RTMP server connection failed
         */
        public final static int RTMP_CONNECT_FAIL = -15;

        /**
         * Connection to the server lost
         */
        public final static int CONNECTION_LOST = -16;

        /**
         * Received an unexpected timestamp from the server
         */
        public final static int RTMP_KEYFRAME_TS_MISMATCH = -17;

        /**
         * The RTMP stream received is corrupted
         */
        public final static int RTMP_READ_CORRUPT_STREAM = -18;

        /**
         * Memory allocation failed
         */
        public final static int RTMP_MEM_ALLOC_FAIL = -19;

        /**
         * Stream indicated a bad datasize, could be corrupted
         */
        public final static int RTMP_STREAM_BAD_DATASIZE = -20;

        /**
         * RTMP packet received is too small
         */
        public final static int RTMP_PACKET_TOO_SMALL = -21;

        /**
         * Could not send packet to RTMP server
         */
        public final static int RTMP_SEND_PACKET_FAILED = -22;

        /**
         * Sending pause to the server failed
         */
        public final static int RTMP_PAUSE_FAIL = -23;

        /**
         * Missing a :// in the URL
         */
        public final static int URL_MISSING_PROTOCOL = -24;

        /**
         * Hostname is missing in the URL
         */
        public final static int URL_MISSING_HOSTNAME = -25;

        /**
         * The port number indicated in the URL is wrong.
         */
        public final static int URL_INCORRECT_PORT = -26;

        /**
         * The RTMP client is in an illegal state
         */
        public final static int RTMP_ILLEGAL_STATE = -27;

        /**
         * RTMP client has encountered an unexpected error
         */
        public final static int RTMP_GENERIC_ERROR = -28;

        public final int errorCode;

        public RtmpIOException(String message, int errorCode) {
            super(message);
            this.errorCode = errorCode;
        }

    }

    private RtmpIOException createRtmpIOException(int errorCode) {
        String message;
        switch (errorCode) {
            case RtmpIOException.OPEN_ALLOC:
                message = "Could not allocate memory to RTMP structure";
                break;
            case RtmpIOException.OPEN_SETUP_URL:
                message = "Could not open URL";
                break;
            case RtmpIOException.OPEN_CONNECT:
                message = "Could not connect to RTMP server";
                break;
            case RtmpIOException.OPEN_CONNECT_STREAM:
                message = "Could not open the stream";
                break;
            case RtmpIOException.UNKNOWN_RTMP_OPTION:
                message = "Unknown RTMP option received";
                break;
            case RtmpIOException.UNKNOWN_RTMP_AMF_TYPE:
                message = "Unknown RTMP AMF type received";
                break;
            case RtmpIOException.DNS_NOT_REACHABLE:
                message = "DNS Service not reachable";
                break;
            case RtmpIOException.SOCKET_CONNECT_FAIL:
                message = "Could not connect to RTMP server";
                break;
            case RtmpIOException.SOCKS_NEGOTIATION_FAIL:
                message = "SOCKS negotiation failed";
                break;
            case RtmpIOException.SOCKET_CREATE_FAIL:
                message = "Socket creation failed";
                break;
            case RtmpIOException.NO_SSL_TLS_SUPP:
                message = "RTMP client doesn't support SSL";
                break;
            case RtmpIOException.HANDSHAKE_CONNECT_FAIL:
                message = "Could not connect for handshake";
                break;
            case RtmpIOException.HANDSHAKE_FAIL:
                message = "Handshake with server failed";
                break;
            case RtmpIOException.RTMP_CONNECT_FAIL:
                message = "RTMP connect failed";
                break;
            case RtmpIOException.CONNECTION_LOST:
                message = "Connection to server lost";
                break;
            case RtmpIOException.RTMP_KEYFRAME_TS_MISMATCH:
                message = "RTMP keyframe timestamp mismatch";
                break;
            case RtmpIOException.RTMP_READ_CORRUPT_STREAM:
                message = "RTMP stream corrupt";
                break;
            case RtmpIOException.RTMP_MEM_ALLOC_FAIL:
                message = "Could not allocate memory";
                break;
            case RtmpIOException.RTMP_STREAM_BAD_DATASIZE:
                message = "Received bad data size";
                break;
            case RtmpIOException.RTMP_PACKET_TOO_SMALL:
                message = "RTMP packet is too small";
                break;
            case RtmpIOException.RTMP_SEND_PACKET_FAILED:
                message = "Could not send packet to RTMP server";
                break;
            case RtmpIOException.RTMP_PAUSE_FAIL:
                message = "Could not pause stream";
                break;
            case RtmpIOException.URL_MISSING_PROTOCOL:
                message = "URL is missing protocol (://)";
                break;
            case RtmpIOException.URL_MISSING_HOSTNAME:
                message = "URL is missing hostname";
                break;
            case RtmpIOException.URL_INCORRECT_PORT:
                message = "URL port is incorrect";
                break;
            case RtmpIOException.RTMP_ILLEGAL_STATE:
                message = "Client may not have been initialised with a call to open()";
                break;
            case RtmpIOException.RTMP_GENERIC_ERROR:
                message = "Unexpected error in RTMP client";
                break;
            default:
                message = "Unknown error!";
                break;
        }
        return new RtmpIOException("Error " + errorCode + ": " + message, errorCode);
    }

    /**
     *  Sets the socket's send timeout value
     * @param sendTimeoutInMs
     * The send timeout value for the rtmp socket in milliseconds.
     * Parameter expects a non-zero positive integer and will reset timeout to the default value
     * (10000 ms) if zero or a negative integer is passed.
     *  */
    public void setSendTimeout(int sendTimeoutInMs) {
        if (sendTimeoutInMs > 0) {
            this.sendTimeoutInMs = sendTimeoutInMs;
        } else {
            this.sendTimeoutInMs = TIMEOUT_IN_MS;
        }
    }

    /**
     *  Sets the socket's receive timeout value
     * @param receiveTimeoutInMs
     * The receive timeout value for the rtmp socket in milliseconds.
     * Parameter expects a non-zero positive integer and will reset timeout to the default value
     * (10000 ms) if zero or a negative integer is passed.
     *  */
    public void setReceiveTimeout(int receiveTimeoutInMs) {
        if (receiveTimeoutInMs > 0) {
            this.receiveTimeoutInMs = receiveTimeoutInMs;
        } else {
            this.receiveTimeoutInMs = TIMEOUT_IN_MS;
        }
    }

    public void open(String url, boolean isPublishMode) throws RtmpIOException {
        rtmpPointer = nativeAlloc();
        if (rtmpPointer == 0) {
            throw this.createRtmpIOException(RtmpIOException.OPEN_ALLOC);
        }
        int result = nativeOpen(url, isPublishMode, rtmpPointer, sendTimeoutInMs,
            receiveTimeoutInMs);
        if (result != OPEN_SUCCESS) {
            rtmpPointer = 0;
            throw this.createRtmpIOException(result);
        }
    }

    private native long nativeAlloc();

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
    private native int nativeOpen(String url, boolean isPublishMode, long rtmpPointer,
        int sendTimeoutInMs, int receiveTimeoutInMs);

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
    public int read(byte[] data, int offset, int size) throws IOException, IllegalStateException {
        int ret = nativeRead(data, offset, size, rtmpPointer);
        if (ret < RtmpIOException.RTMP_ERROR_NONE && ret != RtmpIOException.RTMP_READ_DONE) {
            throw this.createRtmpIOException(ret);
        }
        return ret;
    }

    private native int nativeRead(byte[] data, int offset, int size, long rtmpPointer) throws IllegalStateException;

    /**
     *
     * @param data
     * @return number of bytes written
     * @throws IOException if connection is not opened or connection to server is lost
     */
    public int write(byte[] data) throws IOException, IllegalStateException  {
        return write(data, 0, data.length);
    }

    /**
     *
     * @param data
     * @return number of bytes written
     * @throws IOException if connection is not opened or connection to server is lost
     */
    public int write(byte[] data, int offset, int size) throws IOException, IllegalStateException {
        rtmpError = RtmpIOException.RTMP_ERROR_NONE;
        int ret = nativeWrite(data, offset, size, rtmpPointer);
        if (ret < RtmpIOException.RTMP_ERROR_NONE) {
            throw this.createRtmpIOException(ret);
        }
        return ret;
    }

    private native int nativeWrite(byte[] data, int offset, int size, long rtmpPointer) throws IllegalStateException;


    /**
     * @param pause
     * if pause is true then stream is going to be paused
     *
     * If pause is false, it unpauses the stream and it is ready to to play again
     *
     * @return true if it is successfull else returns false
     */
    public boolean pause(boolean pause) throws RtmpIOException, IllegalStateException {
        int ret = nativePause(pause, rtmpPointer);
        if (ret < RtmpIOException.RTMP_ERROR_NONE) {
            throw this.createRtmpIOException(ret);
        }
        return true;
    }
    private native int nativePause(boolean pause, long rtmpPointer) throws IllegalStateException;



    /**
     *
     * @return true if it is connected
     * false if it is not connected
     */
    public boolean isConnected() {
        return nativeIsConnected(rtmpPointer);
    }

    private native boolean nativeIsConnected(long rtmpPointer);

    /**
     *
     * closes the connection. Dont forget to call
     */
    public void close() {
        nativeClose(rtmpPointer);
        rtmpPointer = 0;
    }

    private native void nativeClose(long rtmpPointer);

}
