package com.github.jiawei.intelligent_parking_system.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jiawei.intelligent_parking_system.R;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LinuxActivity extends AppCompatActivity {


    private TextView tv;
    private EditText et;
    private Button btn;


    private DataInputStream dis = null;
    private Runtime runtime;

    // 加载native-lib，不加lib前缀
    static {
        System.loadLibrary("native-lib");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linux);

        runtime=Runtime.getRuntime();

        tv=findViewById(R.id.tv);
        et=findViewById(R.id.et);
        btn=findViewById(R.id.btn);

        tv.setText(stringFromJNI());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG","et:"+et.getText().toString());
                String result=getResult(et.getText().toString());
                Log.i("TAG","result:"+result);
                tv.setText(result);
            }
        });



    }


    private String getResult(String admin){

        String result="result";

        try {
            runtime.exec("su");
            StringBuilder sb=new StringBuilder();

            Log.i("TAG","step1");


            Process p=runtime.exec(admin);

            InputStream in=p.getInputStream();
            dis=new DataInputStream(in);

            Log.i("TAG","step2");


            String line;
            while ((line=dis.readLine())!=null){
                sb.append(line);
            }

            Log.i("TAG","step3:"+sb.toString());


            result=sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * native-lib中的原生方法
     */
    public native String stringFromJNI();

}
