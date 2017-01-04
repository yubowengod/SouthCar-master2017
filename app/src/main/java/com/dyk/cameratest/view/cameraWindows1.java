package com.dyk.cameratest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by GOD on 2017/1/3.
 */
public class cameraWindows1 extends ImageView {

    private Paint mAreaPaint,paint,paint2;
    private int count = 0;

    public cameraWindows1(Context context, AttributeSet attrs) {
        super(context, attrs);

        mAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint = new Paint();
        paint2 = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        //获取布局的画面大小
        float screenWidth =canvas.getWidth();
        float screenHeight =canvas.getHeight();

        int X = (int) (screenWidth/1.5);
        int Y = (int) (screenHeight/2.4);
        int XX = (int) (screenWidth/6);

        //设置画布为阴影，黑色，透明度
        mAreaPaint.setColor(Color.BLACK);
        mAreaPaint.setStyle(Paint.Style.FILL);
        mAreaPaint.setAlpha(20);

        //设置画布为蓝色，用于设置扫描窗口边缘的四个角
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7f);

        //设置画布为蓝色，用于设置扫描线
        paint2.setColor(Color.BLUE);
        paint2.setStrokeWidth(2f);

        /***画阴影部分的矩形框****/
        canvas.drawRect(0,0,screenWidth,(screenHeight - Y)/2,mAreaPaint);
        canvas.drawRect(0,(screenHeight - Y)/2,(screenWidth-X)/2,screenHeight,mAreaPaint);
        canvas.drawRect((screenWidth-X)/2+X,(screenHeight - Y)/2,screenWidth,screenHeight,mAreaPaint);
        canvas.drawRect((screenWidth-X)/2,(screenHeight - Y)/2+Y,(screenWidth-X)/2+X,screenHeight,mAreaPaint);

        /***四个角***/
        canvas.drawLine((screenWidth-X)/2,(screenHeight - Y)/2,(screenWidth-X)/2+XX,(screenHeight - Y)/2,paint);
        canvas.drawLine((screenWidth-X)/2,(screenHeight - Y)/2,(screenWidth-X)/2,(screenHeight - Y)/2+XX,paint);
        canvas.drawLine((screenWidth-X)/2+X-XX,(screenHeight - Y)/2,(screenWidth-X)/2+X,(screenHeight - Y)/2,paint);
        canvas.drawLine((screenWidth-X)/2+X,(screenHeight - Y)/2,(screenWidth-X)/2+X,(screenHeight - Y)/2+XX,paint);
        canvas.drawLine((screenWidth-X)/2,(screenHeight - Y)/2+Y-XX,(screenWidth-X)/2,(screenHeight - Y)/2+Y,paint);
        canvas.drawLine((screenWidth-X)/2,(screenHeight - Y)/2+Y,(screenWidth-X)/2+XX,(screenHeight - Y)/2+Y,paint);
        canvas.drawLine((screenWidth-X)/2+X,(screenHeight - Y)/2+Y-XX,(screenWidth-X)/2+X,(screenHeight - Y)/2+Y,paint);
        canvas.drawLine((screenWidth-X)/2+X-XX,(screenHeight - Y)/2+Y,(screenWidth-X)/2+X,(screenHeight - Y)/2+Y,paint);

        /*******扫描线*********/
        if(count >Y-20)
            count = 0;
        canvas.drawLine((screenWidth-X)/2+20,(screenHeight - Y)/2+10+count,(screenWidth-X)/2+X-20,(screenHeight - Y)/2+10+count,paint2);
        count+=2;

        invalidate();  //刷新canvas，目的是更新扫描线

    }
}

