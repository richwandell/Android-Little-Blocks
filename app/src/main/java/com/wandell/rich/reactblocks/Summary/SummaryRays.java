package com.wandell.rich.reactblocks.Summary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.wandell.rich.reactblocks.MainActivity;
import com.wandell.rich.reactblocks.R;

import static com.wandell.rich.reactblocks.State.TAG;


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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw fired");

        if(this.frame % 10 != 0) {
            this.frame++;
            invalidate();
            return;
        }

        int frame = this.imageFrame % 6;
        Drawable d;
        switch(frame) {
            case 0:
                d = getResources().getDrawable(R.mipmap.ray1);
                setBackground(d);
                break;

            case 1:
                d = getResources().getDrawable(R.mipmap.ray2);
                setBackground(d);
                break;

            case 2:
                d = getResources().getDrawable(R.mipmap.ray3);
                setBackground(d);
                break;

            case 3:
                d = getResources().getDrawable(R.mipmap.ray4);
                setBackground(d);
                break;

            case 4:
                d = getResources().getDrawable(R.mipmap.ray5);
                setBackground(d);
                break;

            case 5:
                d = getResources().getDrawable(R.mipmap.ray6);
                setBackground(d);
                break;

            case 6:
                d = getResources().getDrawable(R.mipmap.ray7);
                setBackground(d);
                break;
        }


        this.imageFrame++;
        this.frame++;
        super.onDraw(canvas);
        invalidate();
    }

    private void rotate() {
        Animation rotate = AnimationUtils.loadAnimation(this.mainActivity, R.anim.ray_rotate);
        this.startAnimation(rotate);
    }
}
