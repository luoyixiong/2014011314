package com.example.graphicsapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Luo on 2016/9/7.
 */
public class SimpleView extends View {

    public SimpleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        canvas.drawRect(10,10,100,100,paint);

        Path path = new Path();
        path.moveTo(10,120);
        path.lineTo(200,120);
        path.lineTo(100,200);
        path.close();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path,paint);

        paint.setTextSize(50);
        paint.setColor(Color.CYAN);
        paint.setStrikeThruText(true);
        canvas.drawText("hello world",20,300,paint);

        Path pathText = new Path();
        pathText.addCircle(400,800,300,Path.Direction.CCW);

        canvas.drawTextOnPath("Draw the text, with origin at (x,y), using the specified paint, along the specified path.",
                pathText,0,10,paint);
    }
}
