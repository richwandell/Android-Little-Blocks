package com.wandell.rich.reactblocks;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


public class GameBoard extends RelativeLayout {
    public GameBoard(Context context) {
        super(context);
        init();
    }

    public GameBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.game_board, this);
    }
}
