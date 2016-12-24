package com.wandell.rich.reactblocks;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class GameBoard extends RelativeLayout {


    private boolean needsRemove = false;

    private ArrayList<LittleBoxContainer> littleBoxContainers = new ArrayList<>();

    private Timer timer = null;

    public boolean isNeedsRemove() {
        return needsRemove;
    }

    public void setNeedsRemove(boolean needsRemove) {
        this.needsRemove = needsRemove;
    }

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
        MainActivity.gameBoard = this;
        inflate(getContext(), R.layout.game_board, this);
        this.setupHierarchy();
    }

    private void setupHierarchy(){
        ViewGroup boxes = (ViewGroup) this.getChildAt(0);
        int childCount = boxes.getChildCount();
        LittleBoxContainer cont;
        for(int x = 0; x < childCount; x++){
            cont = (LittleBoxContainer) boxes.getChildAt(x);
            this.littleBoxContainers.add(cont);
            cont.setParent(this);
            cont.setxValue(x);
        }
    }

    public void playMetalClank(){
        MediaPlayer mPlayer = MediaPlayer.create(getContext(), R.raw.metalclank);
        mPlayer.start();
    }

    public void lookForPoints(){
        for(LittleBoxContainer cont : this.littleBoxContainers){
            cont.lookForPoints();
        }

        LittleBoxContainer a; LittleBoxContainer b; LittleBoxContainer c;
        LittleBox aa; LittleBox bb; LittleBox cc;
        int _x; int _y; int _z;

        for(int x = 2; x < this.littleBoxContainers.size(); x++) {
            a = this.littleBoxContainers.get(x-2);
            b = this.littleBoxContainers.get(x-1);
            c = this.littleBoxContainers.get(x);

            for(int y = 0; y < LittleBoxContainer.rows; y++){
                if(a.hasBoxAt(y) && b.hasBoxAt(y) && c.hasBoxAt(y)){
                    aa = a.getBoxAt(y);
                    bb = b.getBoxAt(y);
                    cc = c.getBoxAt(y);

                    _x = aa.getColorNumber();
                    _y = bb.getColorNumber();
                    _z = cc.getColorNumber();
                    if(_x == _y && _x == _z){
                        MainActivity.gameScore.addPoints(
                                aa.getColorValue() + bb.getColorValue() + cc.getColorValue()
                        );
                        a.setDying(new int[]{aa.getyValue()}, false);
                        b.setDying(new int[]{bb.getyValue()}, false);
                        c.setDying(new int[]{cc.getyValue()}, false);

                        needsRemove = true;
                        playMetalClank();
                    }
                }
            }
        }

        if(timer != null){
            try {
                timer.cancel();
            }catch(IllegalStateException e){}
        }

        timer = new Timer();

        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                if(needsRemove){
                    needsRemove = false;
                    MainActivity.mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            removeBoxes();
                            lookForPoints();
                        }
                    });
                }
            }
        }, 1000);

    }

    private void removeBoxes(){
        for(LittleBoxContainer cont : this.littleBoxContainers){
            cont.removeBoxes();
        }
    }
}
