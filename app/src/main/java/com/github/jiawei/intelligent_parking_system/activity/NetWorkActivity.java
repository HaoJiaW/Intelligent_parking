package com.github.jiawei.intelligent_parking_system.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jiawei.intelligent_parking_system.R;
import com.github.jiawei.intelligent_parking_system.utils.OkHttpUtils;
import com.github.lguipeng.library.animcheckbox.AnimCheckBox;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.yzq.zxinglibrary.encode.CodeCreator;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import pl.coreorb.selectiondialogs.dialogs.ColorSelectDialog;
import pl.coreorb.selectiondialogs.utils.ColorPalettes;

public class NetWorkActivity extends AppCompatActivity {


    private final static Logger log = LoggerFactory.getLogger(NetWorkActivity.class);


    private OkHttpClient client;
    private Request request;
    private Response response;
    private TextView tv;
    private TextView tv2;


    private PopupWindow popupWindow;
    private View popupView;

    private AnimCheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_work);
        final OkHttpUtils utils=new OkHttpUtils();
        tv=findViewById(R.id.tv);
        tv2=findViewById(R.id.tv2);
        checkBox=findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new AnimCheckBox.OnCheckedChangeListener() {
            @Override
            public void onChange(AnimCheckBox view, boolean checked) {
                if (checked){
                    tv2.setText("选中");
                }else {
                    tv2.setText("未选中");
                }
            }
        });


        final RequestBody body = new FormBody.Builder()
                .add("data","{\n" +
                        "\"db\":\"study2\",\n" +
                        "    \"table\":\"user\", #表名\n" +
                        "    \"jsonMessage\":{\n" +
                        "        \"id\":\"3\",\n" +
                        "        \"name\":\"快乐的美羊羊3号\",\n" +
                        "        \"pwd\":\"dashuaibi\",\n" +
                        "        \"type\":\"admin\"\n" +
                        "    }\n" +
                        "}").build()
                ;
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    new Thread(new Runnable() {
                    @Override
                    public void run() {
                        client =new OkHttpClient();
                        request =new Request.Builder().url("http://106.14.212.56:8080/happy/add/").post(body).build();
                        try {
                            response = client.newCall(request).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                       final String content=response.body().toString();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textView=findViewById(R.id.tv);
                                textView.setText(content);
                            }
                        });
                    }
                }).start();*/

            //startActivity(new Intent(NetWorkActivity.this,DecoderActivity.class));
                // String content=utils.requestMethod("http://106.14.212.56:8080/happy/add/",body).toString();

                /*Intent intent = new Intent(NetWorkActivity.this, CaptureActivity.class);
                *//*ZxingConfig是配置类
                 *可以设置是否显示底部布局，闪光灯，相册，
                 * 是否播放提示音  震动
                 * 设置扫描框颜色等
                 * 也可以不传这个参数
                 * *//*
                ZxingConfig config = new ZxingConfig();
                config.setPlayBeep(true);//是否播放扫描声音 默认为true
                config.setShake(true);//是否震动  默认为true
                config.setDecodeBarCode(true);//是否扫描条形码 默认为true
                config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
                config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
                config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
                config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, 1);*/
                boolean s=false;
                String path=Environment.getExternalStorageDirectory().getPath()+"/hjw"+"/hh"+"/b.txt";
                File file=new File(path);
                if (!file.exists()) {
                    // s=file.mkdir();
                    //mkdirs()相比较mkdir()会创建多级目录
                    s =  file.getParentFile().mkdirs();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        log.error("创建文件 异常", e);
                    }
                }else{
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                       log.error("创建文件 异常", e);
                    }
                }

                Toast.makeText(NetWorkActivity.this,"创建："+s,Toast.LENGTH_SHORT).show();




               /* if (popupWindow==null){
                    popupView = View.inflate(NetWorkActivity.this,R.layout.qr_code_view,null);
                    DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
                    popupWindow=new PopupWindow(popupView,(int) (displayMetrics.widthPixels*0.6),(int) (displayMetrics.widthPixels*0.6));
                }

                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);

                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                Bitmap bitmap = CodeCreator.createQRCode("nihao", 400, 400, logo);

                ImageView img =  popupView.findViewById(R.id.img);
                img.setImageBitmap(bitmap);

                lightOff();
                popupWindow.showAtLocation(tv,Gravity.CENTER,0,0);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        lightOn();
                    }
                });*/

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                tv.setText("扫描结果为：" + content);
            }
        }
    }


    private void lightOn(){
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.alpha=1.0f;
        getWindow().setAttributes(params);
    }
    private void lightOff(){
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.alpha=0.5f;
        getWindow().setAttributes(params);
    }


}
