package com.mytrang.drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

public class SimpleDrawingView extends View {
    private final int paintColor = Color.BLACK;

    private Paint drawPaint, canvasPaint;

    private Bitmap bitmap;

    private Bitmap redDot, greenDot;
    private int x1, x2, y1, y2;
    private int check = 0;
    private int i = 0;
    private float scale;

    private Canvas canvas;
    private List<Points> mpoint;

    private Path path;

    public SimpleDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();

    }

    private void setupPaint() {
        drawPaint = new Paint();
        path = new Path();
        mpoint = getListPoint();

        // set màu cho nét vẽ
        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);

        //set giá trị độ rộng của nét vẽ
        drawPaint.setStrokeWidth(20);

        // set kiểu cho nét vẽ
        // FILL: dùng để tô đối tượng,
        // STROKE: dùng để vẽ đường
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);

        ///set stype ở những điêm kết thúc của 2 đường thẳng và có giá trị sau
        /// ROUND: bo tròn nét vẽ ở 2 đầu mút của đoạn thẳng
        //SQUARE: nét vẽ bình thường, sắc cạnh
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        scale = getResources().getDisplayMetrics().density; //This scale value is proportional to device, and it will handle your problem.


        Points p = mpoint.get(i);
        x1 = p.getX1();
        y1 = p.getY1();
        x2 = p.getX2();
        y2 = p.getY2();
        redDot = Bitmap.createScaledBitmap(drawableToBitmap(R.drawable.red_dot), 30, 30, false);
        greenDot = Bitmap.createScaledBitmap(drawableToBitmap(R.drawable.green_dot), 30, 30, false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawCircle(50,50,20,drawPaint);
//        drawPaint.setColor(Color.GREEN);
//        canvas.drawCircle(50,150,20,drawPaint);
//        drawPaint.setColor(Color.BLUE);
//        canvas.drawCircle(50,250,20,drawPaint);
//        for (Point p : listPoit) {
//            canvas.drawCircle(p.x, p.y, 5, drawPaint);
//        }


        canvas.drawBitmap(greenDot, x1, y1, null);
        canvas.drawBitmap(redDot, x2, y2, null);

        canvas.drawBitmap(bitmap, 0, 0, canvasPaint);
        canvas.drawPath(path, drawPaint);
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        path.moveTo(x, y);
        float a = Math.abs(x - x1);
        float b = Math.abs(y - y1);
        if (a < 50 && b < 50) {
            check = 1;

            Log.e("X1-Y1", "OKKkkkkkkkkkkkkkk");
        } else {
            check = 0;
            Log.e("NO", "NONONOOOOOOOOOOO");
        }
        Log.e("check", " " + check + "");
    }

    // when ACTION_UP stop touch
    public void upTouch(float x, float y) {
        path.lineTo(x, y);
        float a = Math.abs(x - x2);
        float b = Math.abs(y - y2);
        if (a < 50 && b < 50 && check == 1) {
            i++;
            if (i < mpoint.size()) {
                Points p = mpoint.get(i);
                x1 = p.getX1();
                y1 = p.getY1();
                x2 = p.getX2();
                y2 = p.getY2();
                Log.e("x1y1: ", x1 + " " + y1 + " " + x2 + " " + y2);
            } else {
//                i = 0;
            }
//            x1 = (int) (Math.random() * getWidth());
//            y1 = (int) (Math.random() * getHeight());
            Log.e("x1y1: ", x1 + " " + y1 + " " + x2 + " " + y2);
//
//            x2 = (int) (Math.random() * getWidth());
//
//            y2 = (int) (Math.random() * getHeight());

            check = 0;
            Log.e("check", " " + check + " " + " i: " + i);
            Log.e("X2-Y2", "OKKkkkkkkkkkkkkkk");
        } else {
//            check = 1;
            path.reset();
            Log.e("NO", "NONONOOOOOOOOOOO");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float X = event.getX();
        float Y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startTouch(X, Y);
                Log.e("start", "x:=" + X + " y: " + Y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(X, Y);
                break;
            case MotionEvent.ACTION_UP:
                upTouch(X, Y);
                canvas.drawPath(path, drawPaint);
                path.reset();
                Log.e("end", "x:=" + X + " y: " + Y);
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void clear() {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
//        mpoint = new ArrayList<>();
        i = 0;
        Points p = mpoint.get(i);
        x1 = p.getX1();
        y1 = p.getY1();
        x2 = p.getX2();
        y2 = p.getY2();

////        mpoint = getListPoint();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    public Bitmap drawableToBitmap(@DrawableRes int drawableID) {

        Drawable d = ResourcesCompat.getDrawable(getResources(), drawableID, null);

        if (d instanceof BitmapDrawable) {
            return ((BitmapDrawable) d).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);

        return bitmap;
    }

    public List<Points> getListPoint() {
        mpoint = new ArrayList<>();
        Points p1 = new Points(220, 420, 220, 770);
        Points p2 = new Points(220, 420, 500, 770);
        Points p3 = new Points(220, 600, 480, 600);
        Points p4 = new Points(220, 740, 480, 740);
        mpoint.add(p1);
        mpoint.add(p2);
        mpoint.add(p3);
        mpoint.add(p4);

        return mpoint;
    }
}
