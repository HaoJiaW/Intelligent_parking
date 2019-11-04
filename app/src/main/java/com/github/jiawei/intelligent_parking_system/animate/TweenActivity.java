package com.github.jiawei.intelligent_parking_system.animate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jiawei.intelligent_parking_system.R;

public class TweenActivity extends AppCompatActivity {

    private TextView testView;
    private RelativeLayout rootView;
    private Button beginButton;
    private Animation translate;
    private Animation scale;
    private Animation rotate;
    private Animation translateAplhaSet;
    private Animation translateAnticipateInterpolator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tween);

        testView=findViewById(R.id.testView);
        beginButton=findViewById(R.id.beginButton);
        rootView=findViewById(R.id.rootView);

        rootView.post(new Runnable() {
            @Override
            public void run() {
                addLayoutAnimation(rootView);
            }
        });


        translate= AnimationUtils.loadAnimation(TweenActivity.this,R.anim.translate_test);
        scale= AnimationUtils.loadAnimation(TweenActivity.this,R.anim.scale_test);
        rotate= AnimationUtils.loadAnimation(TweenActivity.this,R.anim.rotate_test);
        translateAplhaSet= AnimationUtils.loadAnimation(TweenActivity.this,R.anim.transtale_alpha_set);
        translateAnticipateInterpolator= AnimationUtils.loadAnimation(TweenActivity.this,R.anim.anticipate_interpolator_test);

        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   testView.startAnimation(translate);
                //testView.startAnimation(scale);
                testView.startAnimation(translateAnticipateInterpolator);
            }
        });

    }

    private void addLayoutAnimation(ViewGroup view){
        Animation animation=AnimationUtils.loadAnimation(this,R.anim.layout_animation_test);
        LayoutAnimationController layoutAnimationController=new LayoutAnimationController(animation);
        layoutAnimationController.setDelay(0.3f);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        view.setLayoutAnimation(layoutAnimationController);
    }
}
