package com.wandell.rich.reactblocks.Helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TypewriterTextView extends TextView {
    public TypewriterTextView(Context context) {
        super(context);
        init();
    }

    public TypewriterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TypewriterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/american_typewriter_bold.ttf");
        this.setTypeface(tf);
    }
}
