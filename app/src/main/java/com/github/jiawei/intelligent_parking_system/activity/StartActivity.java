package com.github.jiawei.intelligent_parking_system.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.jiawei.intelligent_parking_system.MainActivity;
import com.github.jiawei.intelligent_parking_system.R;

public class StartActivity extends AppCompatActivity {

    private View mView;
    private ImageView logoImg;
    private LinearLayout hideLayout;
    private LinearLayout visibleLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater=LayoutInflater.from(this);
        mView=inflater.inflate(R.layout.activity_start,null);
        setContentView(mView);

        logoImg=mView.findViewById(R.id.logo_img);
        hideLayout=mView.findViewById(R.id.hide_layout);
        visibleLayout=mView.findViewById(R.id.visible_layout);


        initStartAnimation();
    }

    private void initStartAnimation(){
        Animation animation=AnimationUtils.loadAnimation(this,R.anim.splash);
        logoImg.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation=AnimationUtils.loadAnimation(StartActivity.this,R.anim.text_splash_position);
                visibleLayout.setAnimation(animation);
                animation=AnimationUtils.loadAnimation(StartActivity.this,R.anim.text_canvas);
                hideLayout.setAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    redirectTo();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
       /* AlphaAnimation animation=new AlphaAnimation(0.7f,1.0f);
        animation.setDuration(2500);
        mView.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/



        }

        private void redirectTo(){
            finish();
            Intent intent=new Intent(StartActivity.this,CanvasTestActivity.class);
            startActivity(intent);
        }

}
