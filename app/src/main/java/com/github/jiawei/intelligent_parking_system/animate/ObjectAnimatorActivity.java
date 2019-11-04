package com.github.jiawei.intelligent_parking_system.animate;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.jiawei.intelligent_parking_system.R;

public class ObjectAnimatorActivity extends AppCompatActivity {

    private View testView;
    private Button translateButton;
    private Button scaleButton;
    private Button rotateButton;
    private Button alphaButton;

    private ObjectAnimator translateAnimator;
    private ObjectAnimator scaleAnimator;
    private ObjectAnimator rotateAnimator;
    private ObjectAnimator alphaAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_animator);

        testView=findViewById(R.id.testView);
        translateButton=findViewById(R.id.translateButton);
        scaleButton=findViewById(R.id.scaleButton);
        rotateButton=findViewById(R.id.rotateButton);
        alphaButton=findViewById(R.id.alphaButton);

        translateAnimator=ObjectAnimator.ofFloat(testView,"translationX",0,300);
        translateAnimator.setRepeatMode(ValueAnimator.REVERSE);
        translateAnimator.setRepeatCount(1);
        translateAnimator.setDuration(500);

        scaleAnimator=ObjectAnimator.ofFloat(testView,"scaleX",1f,0.3f);
        scaleAnimator.setDuration(500);

        rotateAnimator=ObjectAnimator.ofFloat(testView,"rotation",0,360);
        rotateAnimator.setDuration(500);

        alphaAnimator=ObjectAnimator.ofFloat(testView,"alpha",1,0.5f);
        alphaAnimator.setDuration(500);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateAnimator.start();
            }
        });

        scaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleAnimator.start();
            }
        });

        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateAnimator.start();
            }
        });

        alphaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alphaAnimator.start();
            }
        });


    }
}
