package com.chenxf.ashmemclient;

public class ShmLib {
    private static final String TAG = "ShmLib";

    static {
        System.loadLibrary("native-lib");
    }

    public static native int sendNativeBuf(int shmFd, int shmSize, byte[] bytearray);
}