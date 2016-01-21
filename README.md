# Librtmp_Client_for_Android
It is probably the smallest(~60KB) rtmp client for android. It calls librtmp functions over JNI interface


It compiles librtmp library without ssl. 

You can call below functions of **RtmpClient** from Java. FYI, **_write_** function has not been tested.

For live streams add **" live=1"** at the end of the url when calling the **_open_** function

* *public native int open(String url, boolean isPublishMode);*
* *public native int read(byte[] data, int offset, int size);*
* *public native int write(byte[] data);*
* *public native int seek(int seekTime);*
* *public native int pause(int pause);*
* *public native int close();*


Don't forget calling the **_close_** function after you are done. If you don't, there will be memory leakage
