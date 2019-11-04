package com.github.jiawei.intelligent_parking_system.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.jiawei.intelligent_parking_system.R;
import com.github.jiawei.intelligent_parking_system.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class IntroduceActivity extends AppCompatActivity {
   // private View mView;
    private List<View> pagerList;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private List<ImageView> mDotImageViewList; //小圆点的集合
    private LinearLayout dotLayout;   //小圆点的集合
    private int mPosition=0; //小圆点当前位置
    private Button toLoginBtn;
    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LayoutInflater inflater=LayoutInflater.from(this);
       // mView=inflater.inflate(R.layout.activity_introduce,null);
        setContentView(R.layout.activity_introduce);

        initView();
        initDoitView();
        initButton();
    }

    private void initView(){
            viewPager=findViewById(R.id.id_intrduce_viewpager);
            LayoutInflater inflater2=getLayoutInflater();
            pagerList=new ArrayList<>();
            pagerList.add(inflater2.inflate(R.layout.introduce_viewpager_first,null,false));
            pagerList.add(inflater2.inflate(R.layout.introduce_viewpager_second,null,false));
            pagerList.add(inflater2.inflate(R.layout.introduce_viewpager_third,null,false));

            adapter=new ViewPagerAdapter(pagerList);
            viewPager.setAdapter(adapter);

            view=inflater2.inflate(R.layout.introduce_viewpager_third,null,false);

    }

    private void initDoitView(){
        mDotImageViewList=new ArrayList<>();
        dotLayout=findViewById(R.id.id_dot_layout);
        for (int i=0;i<3;i++){
            ImageView dotImg=new ImageView(this);
            if (i==mPosition){
                dotImg.setImageResource(R.drawable.dot_selector);
            }else {
                dotImg.setImageResource(R.drawable.dot_unselector);
            }
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                    .WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i>0){
                params.leftMargin=10;
            }
            dotImg.setLayoutParams(params);
            dotLayout.addView(dotImg);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mPosition=i;
                for (int j=0;j<3;j++){
                    ImageView imageView= (ImageView) dotLayout.getChildAt(j);
                    if (i==j){
                        imageView.setImageResource(R.drawable.dot_selector);
                    }else {
                        imageView.setImageResource(R.drawable.dot_unselector);
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initButton(){
        toLoginBtn=view.findViewById(R.id.id_intrduce_btn);
        toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
