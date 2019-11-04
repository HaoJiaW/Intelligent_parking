package com.github.jiawei.intelligent_parking_system.activity;

import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.jiawei.intelligent_parking_system.R;
import com.github.jiawei.intelligent_parking_system.helper.NetWorkHelper;

public class Camera2TestActivity extends AppCompatActivity {

    private CameraManager cameraManager;

    private Button openCamera;
    private EditText ipInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Explode explode=new Explode();
        getWindow().setEnterTransition(explode);
        getWindow().setExitTransition(explode);

        setContentView(R.layout.activity_camera2_test);

        openCamera=findViewById(R.id.openCamera);
        ipInput=findViewById(R.id.ipInput);


        ipInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NetWorkHelper.dstIp=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Camera2TestActivity.this,Camera2Activity.class));
            }
        });


    }

}
