package com.wandell.rich.reactblocks.GameBoard;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;

import com.plattysoft.leonids.ParticleSystem;
import com.wandell.rich.reactblocks.Helpers.TypewriterTextView;
import com.wandell.rich.reactblocks.MainActivity;
import com.wandell.rich.reactblocks.R;
import com.wandell.rich.reactblocks.State;

import static com.wandell.rich.reactblocks.State.TAG;

public class GameScore extends TypewriterTextView {

    private int currentScore = 0;
    private int currentExplosion;
    private MainActivity mainActivity;

    public GameScore(Context context) {
        super(context);
        init(context);
    }

    public GameScore(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameScore(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        this.mainActivity = (MainActivity)context;
        State.gameScore = this;
    }

    public void addPoints(int points){
        this.currentScore += points;
        int explosion = this.currentScore / 10000;
        this.setText(Integer.toString(this.currentScore));
        if(explosion > this.currentExplosion) {
            showExplosion();
        }
        this.currentExplosion = explosion;
    }

    public int getPoints(){
        return this.currentScore;
    }

    private void showExplosion() {
        Log.d(TAG, "context is MainActivity");
        MediaPlayer mPlayer = MediaPlayer.create(getContext(), R.raw.points_blast);
        mPlayer.start();
        ParticleSystem p = new ParticleSystem(
                mainActivity,
                50,
                R.drawable.animated_confetti,
                3000
        );
        p.setSpeedRange(0.2f, 0.5f)
                .oneShot(this, 50);
    }
}
