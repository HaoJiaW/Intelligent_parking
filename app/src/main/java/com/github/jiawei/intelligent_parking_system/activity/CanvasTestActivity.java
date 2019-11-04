package com.github.jiawei.intelligent_parking_system.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.jiawei.intelligent_parking_system.MainActivity;
import com.github.jiawei.intelligent_parking_system.R;
import com.github.jiawei.intelligent_parking_system.utils.ScreenUnitUtils;
import com.github.jiawei.intelligent_parking_system.view.CustomImageView;
import com.github.jiawei.intelligent_parking_system.view.MyView;
import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.iigo.library.PowerView;
import com.kongzue.dialog.v2.Notification;

import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

import static com.gitonway.lee.niftynotification.lib.Effects.standard;

public class CanvasTestActivity extends AppCompatActivity {


    private MyView myView;
 //   private ChargingView chargingView;
    private PowerView powerView;
    private CustomImageView customImageView;
    private Button invalidateBtn;
    private Button clipBtn;
    private int progress=0;
    private Timer timer=new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_test);
       // x.view().inject(this);

        myView=findViewById(R.id.my_view);
        //chargingView=findViewById(R.id.cv2);
        powerView=findViewById(R.id.pv);

        invalidateBtn=findViewById(R.id.invalidate_btn);
        clipBtn=findViewById(R.id.clip_btn);
        customImageView=findViewById(R.id.custom_imageview);
        customImageView.setLayoutParams(new RelativeLayout.LayoutParams(ScreenUnitUtils.Px2Dp(this,
                128),ScreenUnitUtils.Px2Dp(this,
                128)));
        customImageView.invalidate();

        timer.schedule(timerTask,0,1000);

        invalidateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progress==100){
                    progress-=(Math.random()*10+1);
                }else {
                    progress+=(Math.random()*10+1);
                }
           //     myView.setProgress(progress);
                powerView.setPowerColor(getResources().getColor(R.color.colorPrimary_green));
                powerView.setBgColor(Color.WHITE);


       //         chargingView.setProgress(progress);
                powerView.setProgress(progress);
            }
        });

        clipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //progress-=(Math.random()*10+1);
               // myView.setProgress2(progress);
                /*if (progress>0){
                    chargingView.setProgress(progress);
                    powerView.setProgress(progress);
                }*/
                powerView.setPowerColor(getResources().getColor(R.color.e));
                powerView.setBgColor(Color.WHITE);
            }
        });


    }


    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            progress+=Math.random()*10+1;
            if (progress>100){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Notification.show(CanvasTestActivity.this,0, R.drawable.full_reduce,"警告", "电量已充满", 3000, Notification.TYPE_ERROR)
//                                .setOnNotificationClickListener(new Notification.OnNotificationClickListener() {
//                                    @Override
//                                    public void OnClick(int id) {
//                                        //  Toast.makeText(context,"点击了通知",SHOW_TIME_SHORT).show();
//                                    }
//                                });
                        Configuration cfg=new Configuration.Builder()
                                .setAnimDuration(700)
                                .setDispalyDuration(1500)
                                .setBackgroundColor("#FFBDC3C7")
                                .setTextColor("#FF444444")
                                .setIconBackgroundColor("#FFFFFFFF")
                                .setTextPadding(5)                      //dp
                                .setViewHeight(48)                      //dp
                                .setTextLines(2)                        //You had better use setViewHeight and setTextLines together
                                .setTextGravity(Gravity.CENTER)         //only text def  Gravity.CENTER,contain icon Gravity.CENTER_VERTICAL
                                .build();

                        NiftyNotificationView.build(CanvasTestActivity.this,"电量已充满", standard,R.id.view_group,cfg)
                                .setIcon(R.drawable.full_reduce)               //remove this line ,only text
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //add your code
                                    }
                                })
                                .show();
                    }
                });
                progress=0;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    powerView.setProgress(progress);
                }
            });
        }
    };

    /**
     * 显示一个普通的通知
     *
     * @param context 上下文
     */
//    public static void showNotification(Context context) {
//        Notification notification = new NotificationCompat.Builder(context)
//                /**设置通知左边的大图标**/
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                /**设置通知右边的小图标**/
//                .setSmallIcon(R.mipmap.ic_launcher)
//                /**通知首次出现在通知栏，带上升动画效果的**/
//                .setTicker("通知来了")
//                /**设置通知的标题**/
//                .setContentTitle("这是一个通知的标题")
//                /**设置通知的内容**/
//                .setContentText("这是一个通知的内容这是一个通知的内容")
//                /**通知产生的时间，会在通知信息里显示**/
//                .setWhen(System.currentTimeMillis())
//                /**设置该通知优先级**/
//                .setPriority(Notification.PRIORITY_DEFAULT)
//                /**设置这个标志当用户单击面板就可以让通知将自动取消**/
//                .setAutoCancel(true)
//                /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
//                .setOngoing(false)
//                /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
//                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
//                .setContentIntent(PendingIntent.getActivity(context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT))
//                .build();
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//        /**发起通知**/
//        notificationManager.notify(0, notification);
//    }
}
