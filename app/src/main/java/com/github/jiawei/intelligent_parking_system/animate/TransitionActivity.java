package com.github.jiawei.intelligent_parking_system.animate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.jiawei.intelligent_parking_system.R;

public class TransitionActivity extends AppCompatActivity {

    private Button intentButton;
    private ImageView shareView;

    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Explode explode=new Explode();
        getWindow().setEnterTransition(explode);
        getWindow().setExitTransition(explode);


        setContentView(R.layout.activity_transition);

        intentButton=findViewById(R.id.intentButton);
        shareView=findViewById(R.id.shareView);

        animator=ObjectAnimator.ofFloat(intentButton,"alpha",1f,1f);
        animator.setDuration(2000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                Intent intent=new Intent(TransitionActivity.this,ValueAnimatorActivity.class);
                Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(TransitionActivity.this,shareView,"shareView")
                        .toBundle();
                startActivity(intent,bundle);
            }
        });

        animator.start();



        intentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Android 4.0 以前的写法
//                startActivity(new Intent(TransitionActivity.this,ValueAnimatorActivity.class));
//                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

//                Intent intent=new Intent(TransitionActivity.this,ValueAnimatorActivity.class);
//                Bundle options= ActivityOptionsCompat.makeSceneTransitionAnimation(TransitionActivity.this).toBundle();
//                startActivity(intent,options);

                Intent intent=new Intent(TransitionActivity.this,ValueAnimatorActivity.class);
                Bundle bundle= ActivityOptions.makeSceneTransitionAnimation(TransitionActivity.this,shareView,"shareView")
                        .toBundle();
                startActivity(intent,bundle);

            }
        });

    }
}
