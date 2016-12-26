package com.jsonliang.bluetooth_demo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import static com.jsonliang.bluetooth_demo.Constant.STATE_NONE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BluetoothAdapter bluetoothAdapter ;
    private ArrayList<String>  array = new ArrayList<>();
    private ListAdapter adapter ;
    private ListView lv ;
    private String TAG = MainActivity.class.getSimpleName();
    public static int mState = STATE_NONE ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("蓝牙测试");
        Button button = (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.lv);
        button.setOnClickListener(this);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter() ;
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver,filter);
        adapter = new ListAdapter();
        lv.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        // 第一步 ：检查是否有蓝牙设备 .
        if(bluetoothAdapter == null){
            Toast.makeText(this, "该设备没有蓝牙设备", Toast.LENGTH_SHORT).show();
            return ;
        }
        // 第二步 ：检查蓝牙设备是否已经打开
        if(!bluetoothAdapter.isEnabled()){
            // 1. 首先判断系统是否大于或等于 6.0
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,1000);
        }else{ // 如果已经开启,则搜索设备
            Log.i(TAG, "onClick: 开始搜索设备");
            bluetoothAdapter.startDiscovery();//
            Set<BluetoothDevice>  devices = bluetoothAdapter.getBondedDevices() ;// 获取到绑定的设备
            if(devices !=null && devices.size() >0){
                for(BluetoothDevice bd : devices){
                    array.add(bd.getName() + "\n" + bd.getAddress());
                    adapter.notifyDataSetChanged();
                }
            }

        }
        // 第三步 ：
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                Log.i(TAG, "onReceive: 搜索到设备" + intent.getAction());
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                array.add(device.getName() +"\n" + device.getAddress());
                adapter.notifyDataSetChanged();
            }
        }
    };


    public class ListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return array == null ? 0 : array.size();
        }

        @Override
        public String getItem(int position) {
            return array == null ? null :array.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,null);
                TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(tv);
            }

            TextView textView = (TextView) convertView.getTag();
            textView.setText(array.get(position));
            return convertView;
        }
    }
}
