package com.jsonliang.bluetooth_demo;

import java.util.UUID;

/**
 * Created by Jsonliang on 2016/12/5.
 */

public class Constant {

    public static final String NAME_SECURE = "BluetoothChatSecure";
    public static final String NAME_INSECURE = "BluetoothChatInsecure";

    public static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    // 当前连接状态
    public static final int STATE_NONE = 0;       // 无连接
    public static final int STATE_LISTEN = 1;     // 正在监听过来的连接
    public static final int STATE_CONNECTING = 2; // 正在连接
    public static final int STATE_CONNECTED = 3;  // 连上远程设备

    public static final int MESSAGE_DEVICE_NAME  = 4 ;
    public static final String DEVICE_NAME = "device_name";


}
