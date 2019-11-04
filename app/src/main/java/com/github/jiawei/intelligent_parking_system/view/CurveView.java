package com.github.jiawei.intelligent_parking_system.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

public class CurveView extends View {

    //路径和画笔
    private Path mPath;
    private Paint mPaint;

    //记录屏幕的宽高
    private int mScreenHeight;
    private int mScreenWidth;

    private int mOffset;

    private float curve1X1;
    private float curve1X2;
    private float curve1X3;

    private float curve1Y1;
    private float curve1Y2;
    private float curve1Y3;

    private float curve2X1;
    private float curve2X2;
    private float curve2X3;

    private float curve2Y1;
    private float curve2Y2;
    private float curve2Y3;


    public CurveView(Context context) {
        super(context);
        init(context);
    }


    public CurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    /**
     * 进行初始化的一些操作
     */
    private void init(Context context) {
        //获取屏幕的宽高
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;




        //路径,画笔设置
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(8);

    //    setViewanimator();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();


        curve1X1=(float) (0.33*mScreenWidth+15);
        curve1Y1=(float) (0.6*mScreenHeight-65);

        curve1X2=(float) (0.33*mScreenWidth+45);
        curve1Y2=(float) (0.6*mScreenHeight-15);

        curve1X3=(float) (0.33*mScreenWidth+60);
        curve1Y3=(float) (0.6*mScreenHeight);

        curve2X1=(float) (0.67*mScreenWidth-45);
        curve2Y1=(float) (0.6*mScreenHeight-15);

        curve2X2=(float) (0.67*mScreenWidth-15);
        curve2Y2=(float) (0.6*mScreenHeight-65);

        curve2X3=(float) (0.67*mScreenWidth);
        curve2Y3=(float) (0.6*mScreenHeight-80);


        mPath.moveTo(0,(float)(0.6*mScreenHeight-80));
        mPath.lineTo((float) 0.33*mScreenWidth,(float) (0.6*mScreenHeight-80));
        mPath.cubicTo(curve1X1,curve1Y1,curve1X2,curve1Y2,curve1X3,curve1Y3);
        mPath.lineTo((float)(0.67*mScreenWidth-60),(float) 0.6*mScreenHeight);
        mPath.cubicTo(curve2X1,curve2Y1,curve2X2,curve2Y2,curve2X3,curve2Y3);
        mPath.lineTo(mScreenWidth,(float)(0.6*mScreenHeight-80));
        mPath.lineTo(mScreenWidth,0);
        mPath.lineTo(0,0);


//        //贝塞尔曲线
//        mPath.moveTo(-mScreenWidth + mOffset, mScreenHeight / 2);
//
////        mPath.quadTo(-mScreenWidth * 3 / 4 + mOffset, mScreenHeight / 2 - 100, -mScreenWidth / 2 + mOffset, mScreenHeight / 2);
////        mPath.quadTo(-mScreenWidth / 4 + mOffset, mScreenHeight / 2 + 100, 0 + mOffset, mScreenHeight / 2);
////        mPath.quadTo(mScreenWidth / 4 + mOffset, mScreenHeight / 2 - 100, mScreenWidth / 2 + mOffset, mScreenHeight / 2);
////        mPath.quadTo(mScreenWidth * 3 / 4 + mOffset, mScreenHeight / 2 + 100, mScreenWidth + mOffset, mScreenHeight / 2);
//
//        //简化写法
//        for (int i = 0; i < 2; i++) {
//            mPath.quadTo(-mScreenWidth * 3 / 4 + (mScreenWidth * i) + mOffset, mScreenHeight / 2 - 100, -mScreenWidth / 2 + (mScreenWidth * i) + mOffset, mScreenHeight / 2);
//            mPath.quadTo(-mScreenWidth / 4 + (mScreenWidth * i) + mOffset, mScreenHeight / 2 + 100, +(mScreenWidth * i) + mOffset, mScreenHeight / 2);
//        }
//
//        //空白部分填充
//        mPath.lineTo(mScreenWidth, mScreenHeight);
//        mPath.lineTo(0, mScreenHeight);

        canvas.drawPath(mPath, mPaint);

    }

    /**
     * 设置动画效果
     */
    private void setViewanimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mScreenWidth);
        valueAnimator.setDuration(1200);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }


}