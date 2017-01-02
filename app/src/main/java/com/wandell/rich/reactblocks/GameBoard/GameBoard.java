package com.wandell.rich.reactblocks.GameBoard;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.games.Games;
import com.wandell.rich.reactblocks.LittleBox.LittleBox;
import com.wandell.rich.reactblocks.LittleBox.LittleBoxContainer;
import com.wandell.rich.reactblocks.R;
import com.wandell.rich.reactblocks.State;
import com.wandell.rich.reactblocks.MainActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class GameBoard extends RelativeLayout {


    private boolean needsRemove = false;

    private ArrayList<LittleBoxContainer> littleBoxContainers = new ArrayList<>();

    private Timer timer = null;

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
        State.gameBoard = this;
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

    public void playRobot(){
        MediaPlayer mPlayer = MediaPlayer.create(getContext(), R.raw.robot);
        mPlayer.start();
    }

    private void lfpVertival(){
        for(LittleBoxContainer cont : this.littleBoxContainers){
            cont.lookForPoints();
        }
    }

    private void lfpHorizontal(){
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
                        State.gameScore.addPoints(
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
    }

    private void lfpQuad(){
        boolean found = false;
        LittleBoxContainer a; LittleBoxContainer b; LittleBoxContainer c;
        LittleBox aa; LittleBox bb; LittleBox aaa; LittleBox bbb;
        int _a; int _b; int _aa; int _bb;

        for(int x = 1; x < this.littleBoxContainers.size(); x++) {
            a = this.littleBoxContainers.get(x-1);
            b = this.littleBoxContainers.get(x);

            for(int y = 1; y < LittleBoxContainer.rows; y++){
                if(a.hasBoxAt(y) && b.hasBoxAt(y)){
                    aa = a.getBoxAt(y);
                    bb = b.getBoxAt(y);
                    aaa = a.getBoxAt(y-1);
                    bbb = b.getBoxAt(y-1);

                    _a = aa.getColorNumber();
                    _b = bb.getColorNumber();
                    _aa = aaa.getColorNumber();
                    _bb = bbb.getColorNumber();
                    if(_a == _b && _a == _aa && _a == _bb){
                        State.gameScore.addPoints(aa.getColorValue() * 4);

                        a.setGrouped(new int[]{
                                aa.getyValue(),
                                aaa.getyValue()
                        }, true);

                        b.setGrouped(new int[]{
                                bb.getyValue(),
                                bbb.getyValue()
                        }, false);
                        found = true;
                        playRobot();
                    }
                }
            }
        }
        if(found){
            unlockGoldBlockCreator();
        }
    }

    public void lookForPoints(){
        lfpVertival();
        lfpHorizontal();
        lfpQuad();

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
        int numBoxes = 0;
        ArrayList<LittleBoxContainer> temp = new ArrayList<>();
        for(LittleBoxContainer cont : this.littleBoxContainers){
            numBoxes = cont.removeBoxes();
            if(numBoxes > 0){
                temp.add(cont);
            }
        }
        this.littleBoxContainers = temp;
        if(this.littleBoxContainers.size() == 0){
            endGame();
        }
    }

    private void endGame(){
        State.GAMES_PLAYED++;
        checkGameNumberAchievements();
        MainActivity.mainActivity.showSummary();
        submitScore();
    }

    private void checkGameNumberAchievements(){
        if(State.GAMES_PLAYED == 1) {
            String bm = getResources().getString(R.string.achievement_level_1_block_master);
            Games.Achievements.unlock(MainActivity.googleApiClient, bm);
        }
    }

    private void submitScore(){
        boolean connected = MainActivity.googleApiClient.isConnected();
        if (connected) {
            Games.Leaderboards.submitScore(MainActivity.googleApiClient,
                    getContext().getString(R.string.leaderboard_top_single_game),
                    State.gameScore.getPoints());
        }
    }

    private void unlockGoldBlockCreator(){
        boolean connected = MainActivity.googleApiClient.isConnected();
        if (connected) {
            Games.Achievements.unlock(MainActivity.googleApiClient, getContext().getString(R.string.achievement_gold_block_creator));
        }
    }
}
