package com.chenxf.ashmemclient;


import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;

public class MainActivity extends Activity implements View.OnClickListener {
    private GSBinderProxy mGSBinderProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGSBinderProxy = new GSBinderProxy();
        mGSBinderProxy.connectService();

        findViewById(R.id.button_1).setOnClickListener(this);
        findViewById(R.id.button_2).setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_1) {
            mGSBinderProxy.createShm();
        } else if(view.getId() == R.id.button_2) {
            mGSBinderProxy.sendData();
        }
    }
}