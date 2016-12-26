package com.wandell.rich.reactblocks.GameBoard;

import android.content.Context;
import android.util.AttributeSet;

import com.wandell.rich.reactblocks.Helpers.TypewriterTextView;


public class GameScore extends TypewriterTextView {

    private int currentScore = 0;

    public GameScore(Context context) {
        super(context);
        init();
    }

    public GameScore(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameScore(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        GameBoardActivity.gameScore = this;
    }

    public void addPoints(int points){
        this.currentScore += points;
        this.setText(Integer.toString(this.currentScore));
    }

    public int getPoints(){
        return this.currentScore;
    }
}
