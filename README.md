# Librtmp_Client_for_Android
It is probably the smallest(~60KB) rtmp client for android. It calls librtmp functions over JNI interface.
With all cpu architectures(arm, arm7, arm8, x86, x86-64, mips) its size is getting about 300KB

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

## Install ##

- Add repository to your build.gradle
```sh
repositories {
    ...
    maven { url  "https://dl.bintray.com/mekya/maven" }
    ...
}
```

- Add dependency to your build gradle
```sh
dependencies {
    ...
    compile 'net.butterflytv.utils:rtmp-client:0.1.1'
    ...
}
```

- That's all. You can use RtmpClient class