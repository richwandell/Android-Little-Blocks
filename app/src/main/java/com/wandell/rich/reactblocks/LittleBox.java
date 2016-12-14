package com.wandell.rich.reactblocks;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Random;


public class LittleBox extends LinearLayout {

    private String color;
    private int value;

    private static int PINK = 100;
    private static int PURPLE = 200;
    private static int BLUE = 300;
    private static int YELLOW = 350;
    private static int RED = 400;
    private static int GREEN = 450;
    private static int BROWN = 200;

    public LittleBox(Context context) {
        super(context);
        init();
    }

    public LittleBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LittleBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("richd", Integer.toString(value));
            }
        });

        Random r = new Random();
        int colorNumber = r.nextInt(7);
        View v = inflate(getContext(), R.layout.little_box, this);
        StateListDrawable selector;
        switch (colorNumber) {
            case 0:
                selector = (StateListDrawable) ContextCompat.getDrawable(getContext(), R.drawable.little_box_pink);
                v.setBackground(selector);
                v.refreshDrawableState();
                this.value = LittleBox.PINK;
                break;
            case 1:
                selector = (StateListDrawable) ContextCompat.getDrawable(getContext(), R.drawable.little_box_purple);
                v.setBackground(selector);
                v.refreshDrawableState();
                this.value = LittleBox.PURPLE;
                break;
            case 2:
                selector = (StateListDrawable) ContextCompat.getDrawable(getContext(), R.drawable.little_box_blue);
                v.setBackground(selector);
                v.refreshDrawableState();
                this.value = LittleBox.BLUE;
                break;
            case 3:
                selector = (StateListDrawable) ContextCompat.getDrawable(getContext(), R.drawable.little_box_yellow);
                v.setBackground(selector);
                v.refreshDrawableState();
                this.value = LittleBox.YELLOW;
                break;
            case 4:
                selector = (StateListDrawable) ContextCompat.getDrawable(getContext(), R.drawable.little_box_red);
                v.setBackground(selector);
                v.refreshDrawableState();
                this.value = LittleBox.RED;
                break;
            case 5:
                selector = (StateListDrawable) ContextCompat.getDrawable(getContext(), R.drawable.little_box_green);
                v.setBackground(selector);
                v.refreshDrawableState();
                this.value = LittleBox.GREEN;
                break;
            case 6:
                selector = (StateListDrawable) ContextCompat.getDrawable(getContext(), R.drawable.little_box_brown);
                v.setBackground(selector);
                v.refreshDrawableState();
                this.value = LittleBox.BROWN;
                break;
        }
    }
}
