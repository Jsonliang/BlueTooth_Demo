package com.jsonliang.bluetooth_demo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

import static com.jsonliang.bluetooth_demo.Constant.MY_UUID_INSECURE;
import static com.jsonliang.bluetooth_demo.Constant.MY_UUID_SECURE;
import static com.jsonliang.bluetooth_demo.Constant.NAME_INSECURE;
import static com.jsonliang.bluetooth_demo.Constant.NAME_SECURE;
import static com.jsonliang.bluetooth_demo.Constant.STATE_CONNECTED;
import static com.jsonliang.bluetooth_demo.Constant.STATE_CONNECTING;
import static com.jsonliang.bluetooth_demo.Constant.STATE_LISTEN;
import static com.jsonliang.bluetooth_demo.Constant.STATE_NONE;
import static com.jsonliang.bluetooth_demo.MainActivity.mState;

/**
 *  这个类主要一直用来监视Socket动态
 */

public class AcceptThread extends Thread {
    // 本地的服务Socket
    private final BluetoothServerSocket mmServerSocket;
    private String mSocketType;
    private final BluetoothAdapter mAdapter ;
    private final static String TAG = AcceptThread.class.getSimpleName() ;

    public AcceptThread(boolean secure, BluetoothAdapter adapter) {
        BluetoothServerSocket tmp = null;
        mSocketType = secure ? "Secure" : "Insecure";
        mAdapter = adapter ;
        // Create a new listening server socket
        try {
            if (secure) {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,
                        MY_UUID_SECURE);
            } else {
                tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
                        NAME_INSECURE, MY_UUID_INSECURE);
            }
        } catch (IOException e) {
            Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        Log.d(TAG, "Socket Type: " + mSocketType +
                "BEGIN mAcceptThread" + this);
        setName("AcceptThread" + mSocketType);

        BluetoothSocket socket = null;

        // Listen to the server socket if we're not connected
        while (mState != STATE_CONNECTED) {
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e);
                break;
            }

            // If a connection was accepted
            if (socket != null) {
                synchronized (AcceptThread.this) {
                    switch (mState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING: // 正在连接
                            // Situation normal. Start the connected thread.
                            connected(socket, socket.getRemoteDevice(),
                                    mSocketType);
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Could not close unwanted socket", e);
                            }
                            break;
                    }
                }
            }
        }
        Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);

    }

        public void cancel() {
        Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
        }
    }


}
