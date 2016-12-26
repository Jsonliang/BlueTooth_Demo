package com.jsonliang.bluetooth_demo;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by Jsonliang on 2016/12/5.
 */

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private String mSocketType = "";

    public ConnectThread(BluetoothDevice device){
        mmDevice = device ;
        BluetoothSocket temp = null ;
        try {
            temp = device.createRfcommSocketToServiceRecord(UUID.fromString("UUDID"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmSocket = temp ;
    }

    @Override
    public void run() {


    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
        }
    }
}
