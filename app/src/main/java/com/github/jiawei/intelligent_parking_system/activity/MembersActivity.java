package com.github.jiawei.intelligent_parking_system.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.jiawei.intelligent_parking_system.R;

public class MembersActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        backImg=findViewById(R.id.member_back_img);
        backImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.member_back_img:
                finish();
                break;
        }
    }
}
