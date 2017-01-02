package com.wandell.rich.reactblocks.LittleBox;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;


import com.wandell.rich.reactblocks.R;
import com.wandell.rich.reactblocks.State;

import java.util.Random;


public class LittleBox extends LinearLayout {

    private int colorNumber;

    private int value;

    private int yValue;

    private static int PINK = 100;
    private static int PURPLE = 200;
    private static int BLUE = 300;
    private static int YELLOW = 350;
    private static int RED = 400;
    private static int GREEN = 450;
    private static int BROWN = 200;

    private LittleBoxContainer container;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            container.setDying(new int[]{yValue}, false);
            State.gameScore.addPoints(getColorValue());
            State.gameBoard.lookForPoints();
        }
    };

    private boolean dying;

    private boolean grouped;

    private Animation animIn;

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
        this.setOnClickListener(this.listener);
        this.setupColor();
    }

    public void setParent(LittleBoxContainer container) {
        this.container = container;
    }

    public void setyValue(int y){
        this.yValue = y;
    }

    public void setDying(){
        this.dying = true;
        animIn = AnimationUtils.loadAnimation(getContext(), R.anim.lb_die_1);
        this.setAnimation(animIn);
        this.startAnimation(animIn);
    }

    public void setGrouped(boolean left, boolean top){
        this.grouped = true;

        LayerDrawable layer;
        if(left && top){
            layer = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.lb_gold_ul);
        }else if(left){
            layer = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.lb_gold_ll);
        }else if(top){
            layer = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.lb_gold_ur);
        }else{
            layer = (LayerDrawable) ContextCompat.getDrawable(getContext(), R.drawable.lb_gold_lr);
        }
        this.setBackground(layer);
        this.refreshDrawableState();
        this.value *= 4;
        this.colorNumber = -1;
    }

    public boolean getDying(){
        return this.dying;
    }

    public int getyValue(){
        return this.yValue;
    }

    public int getColorValue(){
        return this.value;
    }

    public int getColorNumber(){
        return this.colorNumber;
    }

    private void setupColor() {
        Random r = new Random();
        colorNumber = r.nextInt(7);
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
