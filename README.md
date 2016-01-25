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

This project is developed for Butterfly TV <a href="http://www.butterflytv.net/"><img src="http://www.butterflytv.net/wp-content/uploads/2014/08/icon-butterflyTV-150x150.png" width="30"></a>

<a href="https://play.google.com/store/apps/details?id=com.butterfly">
  <img alt="Get it on Google Play" width="200px" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png">
</a>
