package com.wandell.rich.reactblocks.Summary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.wandell.rich.reactblocks.MainActivity;
import com.wandell.rich.reactblocks.R;

public class SummaryRays extends AppCompatImageView {

    private MainActivity mainActivity;
    private Paint paint;
    private Bitmap rays;
    private Path path;
    private int frame;
    private int width = 0;
    private int height = 0;
    private int imageFrame;

    private static Handler handler;
    private Paint bluePaint;

    private Canvas previousCanvas;

    public SummaryRays(Context context) {
        super(context);
        init(context);
    }

    public SummaryRays(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SummaryRays(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        if(!this.isInEditMode()) {
            this.mainActivity = (MainActivity) context;
        }
        this.path = new Path();
        this.paint = new Paint();
        this.paint.setColor(Color.RED);
        this.bluePaint = new Paint();
        this.bluePaint.setColor(Color.BLUE);

        if(handler == null) {
            handler = new Handler();
            final Runnable r = new Runnable() {

                public void run() {
                    invalidate();
                    handler.postDelayed(this, 200);
                }
            };
            handler.post(r);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(this.width == 0) {
            this.width = getMeasuredWidth();
            this.height = getMeasuredHeight();
        }


        float extraRotate = this.frame / 2;

        float startX = width / 2;
        float startY = height;

        int radius = height;
        float numRays = 10f;

        float rayDistance = (360.00f / numRays);


        for(int i = 0; i < numRays; i++) {
            float theta1 = -((rayDistance * (float)i) + extraRotate);
            float theta2 = -((rayDistance * (float)i) + 20.0f + extraRotate);

            theta1 = (float) Math.toRadians(theta1);
            theta2 = (float) Math.toRadians(theta2);

            float endX1 = (float)((float)radius * Math.sin(theta1));
            float endY1 = (float)((float)radius * Math.cos(theta1));

            float endX2 = (float)((float)radius * Math.sin(theta2));
            float endY2 = (float)((float)radius * Math.cos(theta2));

            path.reset();
            path.moveTo(startX, startY);
            path.lineTo(endX2, endY2);
            path.lineTo(endX1, endY1);
            path.lineTo(startX, startY);

            canvas.drawPath(path, paint);
        }

        previousCanvas = canvas;

        super.onDraw(canvas);

        this.frame++;
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        Log.d(TAG, "onDraw fired");
//
//        if(this.frame % 10 != 0) {
//            this.frame++;
//            invalidate();
//            return;
//        }
//
//        int frame = this.imageFrame % 6;
//        Drawable d;
//        switch(frame) {
//            case 0:
//                d = getResources().getDrawable(R.mipmap.ray1);
//                setBackground(d);
//                break;
//
//            case 1:
//                d = getResources().getDrawable(R.mipmap.ray2);
//                setBackground(d);
//                break;
//
//            case 2:
//                d = getResources().getDrawable(R.mipmap.ray3);
//                setBackground(d);
//                break;
//
//            case 3:
//                d = getResources().getDrawable(R.mipmap.ray4);
//                setBackground(d);
//                break;
//
//            case 4:
//                d = getResources().getDrawable(R.mipmap.ray5);
//                setBackground(d);
//                break;
//
//            case 5:
//                d = getResources().getDrawable(R.mipmap.ray6);
//                setBackground(d);
//                break;
//
//            case 6:
//                d = getResources().getDrawable(R.mipmap.ray7);
//                setBackground(d);
//                break;
//        }
//
//
//        this.imageFrame++;
//        this.frame++;
//        super.onDraw(canvas);
//        invalidate();
//    }

    private void rotate() {
        Animation rotate = AnimationUtils.loadAnimation(this.mainActivity, R.anim.ray_rotate);
        this.startAnimation(rotate);
    }
}
