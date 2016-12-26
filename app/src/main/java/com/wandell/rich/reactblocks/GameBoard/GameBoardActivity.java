package com.wandell.rich.reactblocks.GameBoard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.wandell.rich.reactblocks.R;
import com.wandell.rich.reactblocks.Summary.SummaryActivity;

public class GameBoardActivity extends AppCompatActivity {

    public static GameBoard gameBoard;
    public static GameScore gameScore;
    public static GameBoardActivity gameBoardActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_board);
        View mDecorView = getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gameBoardActivity = this;
    }

    public void startScoreBoard(){
        Intent i = new Intent(GameBoardActivity.gameBoardActivity, SummaryActivity.class);
        i.putExtra("score", gameScore.getPoints());
        startActivity(i);
    }


}
