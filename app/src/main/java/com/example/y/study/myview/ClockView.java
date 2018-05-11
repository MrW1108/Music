package com.example.y.study.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ClockView extends View {
    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();

        /*画出外部圆盘*/
        Paint paintCircle=new Paint();
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setAntiAlias(true);
        paintCircle.setStrokeWidth(5);
        canvas.drawCircle(w /2, h /2, w /2,paintCircle);

       /* 画出表盘内刻度*/
        Paint paintDegree=new Paint();
        paintDegree.setStrokeWidth(3);
        for (int i=0;i<12;i++){
            if (i==0||i==3||i==6||i==9){
                paintDegree.setStrokeWidth(8);
                paintDegree.setTextSize(40);
                canvas.drawLine(w/2,h/2-w/2,w/2,h/2-w/2+90,paintDegree);
                String degree=String.valueOf(i);
                canvas.drawText(degree,w/2-paintDegree.measureText(degree)/2,h/2-w/2+110,paintDegree);
            }else{
                paintDegree.setStrokeWidth(3);
                paintDegree.setTextSize(25);
                canvas.drawLine(w/2,h/2-w/2,w/2,h/2-w/2+60,paintDegree);
                String degree=String.valueOf(i);
                canvas.drawText(degree,w/2-paintDegree.measureText(degree)/2,h/2-w/2+80,paintDegree);
            }
            canvas.rotate(30,w/2,h/2);      //旋转画布  每绘制一条线旋转一次画布，每次转30°
        }
        canvas.save();

        /*画出时针和分针*/
        Paint paintHour=new Paint();
        paintHour.setStrokeWidth(20);
        Paint paintMin=new Paint();
        paintMin.setStrokeWidth(10);
        canvas.translate(w/2,h/2);  //原点移动至圆心
        canvas.drawLine(0,0,100,100,paintHour);
        canvas.drawLine(0,0,0,-200,paintMin);
        canvas.restore();
    }
}
