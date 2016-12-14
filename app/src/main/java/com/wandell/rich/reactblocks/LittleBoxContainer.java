package com.wandell.rich.reactblocks;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public class LittleBoxContainer extends LinearLayout {

    public LittleBoxContainer(Context context) {
        super(context);
        init();
    }

    public LittleBoxContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LittleBoxContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.little_box_container, this);
    }
}
