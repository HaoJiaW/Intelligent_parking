package com.github.jiawei.intelligent_parking_system.view;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jiawei.intelligent_parking_system.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class BtActivity extends AppCompatActivity {


    private EditText content;
    private Button transfer;
    private Button paired;
    private LinearLayout listView;

    private BluetoothAdapter bluetoothAdapter;

    private BluetoothDevice currentDevice=null;

    private List<String> list=new ArrayList<>();
    private Map<String,BluetoothDevice> btDevices=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);
        content=findViewById(R.id.write_content);
        transfer=findViewById(R.id.tran_button);
        paired=findViewById(R.id.paired_button);
        listView=findViewById(R.id.listview);


        initBt();


        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        paired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BtActivity.this,"query aa!！",Toast.LENGTH_LONG).show();

                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

                if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices) {
                        String deviceName = device.getName();

                        final TextView item=new TextView(BtActivity.this);
                        item.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                ,dip2px(BtActivity.this,20)));
                        item.setPadding(dip2px(BtActivity.this,16),0,0,0);
                        item.invalidate();
                        item.setText(deviceName);
                        item.setTextColor(Color.RED);
                        item.setTextSize(COMPLEX_UNIT_SP,12);
                        item.setSingleLine(true);
                        item.setEllipsize(TextUtils.TruncateAt.END);
                        item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                currentDevice=btDevices.get(item.getText().toString());
                            }
                        });
                        listView.addView(item);

                        btDevices.put(deviceName,device);
                    }
                }

            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentDevice==null){
                    Toast.makeText(BtActivity.this,"no device!！",Toast.LENGTH_LONG).show();
                    return;
                }

                byte[] bytes=content.getText().toString().getBytes();


                Thread thread=new ConnectThread(currentDevice,bytes);
                thread.start();
                Toast.makeText(BtActivity.this,"thread is start!！",Toast.LENGTH_LONG).show();
            }
        });

    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.

                Toast.makeText(BtActivity.this,"find new device!！",Toast.LENGTH_LONG).show();


                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
              //  String deviceHardwareAddress = device.getAddress(); // MAC address

                final TextView item=new TextView(BtActivity.this);
                item.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        ,dip2px(BtActivity.this,20)));
                item.setPadding(dip2px(BtActivity.this,16),0,0,0);
                item.invalidate();
                item.setText(deviceName);
                item.setTextSize(COMPLEX_UNIT_SP,12);
                item.setSingleLine(true);
                item.setEllipsize(TextUtils.TruncateAt.END);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentDevice=btDevices.get(item.getText().toString());
                    }
                });
                listView.addView(item);

                btDevices.put(deviceName,device);
            }
        }
    };


    private void initBt(){
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 100);
            Toast.makeText(BtActivity.this,"start bt!！",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode==100){
                Toast.makeText(this,"start bt finish！",Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;

        private byte[] mmBuffer;

        UUID MY_UUID=UUID.fromString("86cc0d70-260b-4d6b-bb03-b1f77fa3470c");

        public ConnectThread(BluetoothDevice device,byte[] bytes) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmBuffer=bytes;
            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);

            } catch (IOException e) {
                Log.e( "TAG","Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.

                if (!mmSocket.isConnected()){
                    mmSocket.connect();
                }



            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e("TAG", "Could not close the client socket", closeException);
                }
                return;
            }

            manageMyConnectedSocket(mmSocket);

           // cancel();

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("TAG", "Could not close the client socket", e);
            }
        }

        private void manageMyConnectedSocket(BluetoothSocket mmSocket){
            try {
                OutputStream tmpOut = mmSocket.getOutputStream();
                tmpOut.write(mmBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale+0.5f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}

