package com.github.jiawei.intelligent_parking_system.activity;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.PixelFormat;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.github.jiawei.intelligent_parking_system.R;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

public class WifiActivity extends AppCompatActivity {

    private EditText wifi;
    private EditText pwd;
    private Button connect;
    private CardView wifiView;
    private WifiManager mWifiManager;

    private Button floatButton;
    private WindowManager.LayoutParams mLayoutParams;
    private WindowManager mWindowManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        Logger.addLogAdapter(new AndroidLogAdapter());

        initView();

        addWindow();
        updateWindow();
    }




    private void initView(){
        wifiView=findViewById(R.id.wifiView);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi=findViewById(R.id.wifi_et);
        pwd=findViewById(R.id.pwd);
        connect=findViewById(R.id.connect);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect(wifi.getText().toString(),pwd.getText().toString());
            }
        });

        wifiView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x= (int) event.getRawX();
                int y= (int) event.getRawY();

                int lastX=0;
                int lastY=0;

                if (event.getAction()==MotionEvent.ACTION_DOWN){

                }

                if (event.getAction()==MotionEvent.ACTION_MOVE){
                    ObjectAnimator animator=ObjectAnimator.ofFloat(this,"translationX",x-lastX);
                }

                lastX=x;
                lastY=y;

                return false;
            }
        });

    }


    public void addWindow(){
        floatButton =new Button(this);
        floatButton.setText("可以拖拽的BUtton");

        mWindowManager=this.getWindowManager();

        mLayoutParams=new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,0,0, PixelFormat.TRANSPARENT);
//        mLayoutParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
//                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        mLayoutParams.flags=WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        mLayoutParams.gravity= Gravity.TOP | Gravity.LEFT ;
        mLayoutParams.x=200;
        mLayoutParams.y=500;
        mLayoutParams.type=WindowManager.LayoutParams.TYPE_APPLICATION_STARTING;

        mWindowManager.addView(floatButton,mLayoutParams);

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scrollY>0:代表此刻View上滑了，反之下滑。起始为0
                //scrollX>0:代表此刻View左滑了，反之右滑。起始伟0
                if (wifi.getScrollY()>0){
                    wifi.scrollBy(0,-300);
                }else {
                    wifi.scrollBy(0,300);
                }
            }
        });

    }

    public void updateWindow(){
        floatButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int rawX=(int) event.getRawX();
                int rawY=(int) event.getRawY();

                if (event.getAction()==MotionEvent.ACTION_MOVE){
                    mLayoutParams.x=rawX;
                    mLayoutParams.y=rawY;
                    mWindowManager.updateViewLayout(floatButton,mLayoutParams);
                }

                return false;
            }
        });
    }



    private void connect(String ssid,String pwd){
          Logger.d("try connect!");

//        WifiConfiguration config = new WifiConfiguration();
//        config.allowedAuthAlgorithms.clear();
//        config.allowedGroupCiphers.clear();
//        config.allowedKeyManagement.clear();
//        config.allowedPairwiseCiphers.clear();
//        config.allowedProtocols.clear();
//        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(ssid);
        if(tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
            Logger.d("remove Network!");
        }

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        // 指定对应的SSID
        config.SSID = "\"" + ssid + "\"";

        config.preSharedKey = "\"" + pwd + "\"";

        Logger.d("connect,wifi:"+config.SSID +"pwd:"+config.preSharedKey+".");

        config.hiddenSSID = true;
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.status = WifiConfiguration.Status.ENABLED;

        int netId = mWifiManager.addNetwork(config);
        // 这个方法的第一个参数是需要连接wifi网络的networkId，第二个参数是指连接当前wifi网络是否需要断开其他网络
        // 无论是否连接上，都返回true。。。。
        mWifiManager.enableNetwork(netId, true);
        Logger.d("try finish");
    }

    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        Logger.d("now wifi:"+"\""+SSID+"\"");
        for (WifiConfiguration existingConfig : existingConfigs) {
            Logger.d("scan wifi:"+existingConfig.SSID);
            if (existingConfig.SSID.equals("\""+SSID+"\"")) {
                return existingConfig;
            }
        }
        return null;
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
////        int rawX=(int) event.getRawX();
////        int rawY=(int) event.getRawY();
////        System.out.println("rawX:"+rawX+",rawY:"+rawY);
////
////        if (event.getAction()==MotionEvent.ACTION_MOVE){
////            mLayoutParams.x=rawX;
////            mLayoutParams.y=rawY;
////            mWindowManager.updateViewLayout(floatButton,mLayoutParams);
////        }
//
//
//        return false;
//    }
}
