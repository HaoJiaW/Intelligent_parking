package com.github.jiawei.intelligent_parking_system.animate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.github.jiawei.intelligent_parking_system.R;

public class ValueAnimatorActivity extends AppCompatActivity {

    private TextView valueTextView;
    private Button beginButton;

    private ValueAnimator valueAnimator;
    private ObjectAnimator xAnimator;
    private ObjectAnimator scaleXAnimator;
    private ObjectAnimator scaleYAnimator;
    private ObjectAnimator animatorX;
    private ObjectAnimator animatorX1;

    private ObjectAnimator pathAnimator;

    private AnimatorSet animatorSet;

    private float cx;
    private float cy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //实现Explode动画效果
//        Explode explode=new Explode();
//        getWindow().setEnterTransition(explode);
//        getWindow().setExitTransition(explode);

        //实现Slide动画效果
//        Slide slide=new Slide();
//        slide.setSlideEdge(Gravity.LEFT);
//        slide.setDuration(300);
//        slide.excludeTarget(R.id.titleBar,true);
//
//        getWindow().setEnterTransition(slide);
//        getWindow().setExitTransition(slide);

//        TransitionSet transitionSet=new TransitionSet();
//        transitionSet.addTransition(new ChangeBounds());
//        transitionSet.addTransition(new ChangeImageTransform());
//        getWindow().setSharedElementEnterTransition(transitionSet);

        //实现Fade动画效果 (淡入效果)
//        Fade fade=new Fade();
//        getWindow().setEnterTransition(fade);
//        getWindow().setExitTransition(fade);


        setContentView(R.layout.activity_value_animator);

        valueTextView=findViewById(R.id.valueTextView);
        beginButton=findViewById(R.id.beginButton);

        valueAnimator=ValueAnimator.ofInt(0,8888);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //当前的数值
                int currentValue= (int) animation.getAnimatedValue();

                valueTextView.setText(String.valueOf(currentValue));
            }
        });


        valueTextView.post(new Runnable() {
            @Override
            public void run() {
                cx=valueTextView.getX();
                cy=valueTextView.getY();
                Log.i("TAG","cx:"+String.valueOf(cx));
                initView();
            }
        });

    }

    private void initView(){
        xAnimator=ObjectAnimator.ofFloat(valueTextView,"x",cx,0f);
        xAnimator.setDuration(2000);

        scaleXAnimator=ObjectAnimator.ofFloat(valueTextView,"scaleX",1f,0.5f);
        scaleYAnimator=ObjectAnimator.ofFloat(valueTextView,"scaleY",1f,0.5f);
        animatorSet=new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(scaleXAnimator,scaleYAnimator);

        animatorX=ObjectAnimator.ofFloat(valueTextView,"x",cx,0f);
        animatorX1=ObjectAnimator.ofFloat(valueTextView,"x",cx);

        animatorSet=new AnimatorSet();
        animatorSet.play(scaleXAnimator).with(scaleYAnimator);
        animatorSet.play(scaleYAnimator).with(animatorX);
        animatorSet.play(animatorX1).after(animatorX);
        animatorSet.setDuration(1000);

        android.graphics.Path path=new Path();
        path.moveTo(cx,cy);
        path.lineTo(cx+200,cy+200);

        pathAnimator=ObjectAnimator.ofFloat(valueTextView,View.X,View.Y,path);
        pathAnimator.setDuration(500);
        pathAnimator.setRepeatMode(ValueAnimator.REVERSE);
        pathAnimator.setRepeatCount(1);

        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueAnimator.start();
                //    xAnimator.start();
//                animatorSet.start();
                pathAnimator.start();
            }
        });

    }

}
