package com.chenxf.ashmemclient;

import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.lang.reflect.Method;

public class GSBinderProxy {
    private static final String TAG = "chenxf#GSBinderProxy";
    static final String SERVICE_NAME = "android.binder.example";

    private IBinder mService;

    private static String ShareMemName = "shmTest";
    private static int ShareMemSize = 1280 * 1440 * 3 / 2;
    private int shmFd = -1;
    private static final int CREATE_SHM = 4;
    private static final int UPDATE_SHM_BUF = 5;

    public void createShm() {
        if (mService != null) {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInt(ShareMemSize);
            data.writeString(ShareMemName);
            try {
                Log.i(TAG, "start request to create shm");
                mService.transact(CREATE_SHM, data, reply, 0);
                Log.i(TAG, "transact done");
                ParcelFileDescriptor pfd = reply.readFileDescriptor();
                shmFd = pfd.getFd();
                Log.e(TAG, "get shm file descriptor, shmFd: " + shmFd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    /*
     * Get binder service
     */
    public void connectService() {
        IBinder binder = null;
        Log.d(TAG, "connectService");
        try {
            //android.os.ServiceManager is hide class, we can not invoke them from SDK. So we have to use reflect to invoke these classes.
            Object object = new Object();
            Method getService = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            Object obj = getService.invoke(object, new Object[]{new String(SERVICE_NAME)});
            binder = (IBinder) obj;
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        if (binder != null) {
            mService = binder;
            Log.d(TAG, "Find binder");
        } else
            Log.d(TAG, "Service is null.");
    }

    /**
     * 造一些数据，放到共享内存
     */
    public void sendData() {
        byte[] tempBuf = new byte[10];
        for (int i = 0; i < 10; i++) {
            tempBuf[i] = (byte)i;
        }
        ShmLib.sendNativeBuf(shmFd, ShareMemSize, tempBuf);

        notifyGS();
    }

    /**
     * 通知远端进程，读取shm数据
     */
    private void notifyGS() {
        if (mService != null) {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                mService.transact(UPDATE_SHM_BUF, data, reply, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
