package com.wandell.rich.reactblocks;


import com.wandell.rich.reactblocks.GameBoard.GameBoard;
import com.wandell.rich.reactblocks.GameBoard.GameScore;
import com.wandell.rich.reactblocks.Helpers.DBOpenHelper;

public class State {
    public static String TAG = "richd";
    public static int GAMES_PLAYED = 0;
    public static GameScore gameScore;
    public static GameBoard gameBoard;
    public static boolean googlePlayEnabled = false;
    public static boolean clicking = false;
    public static int GOLD_BLOCKS = 0;
    public static DBOpenHelper.DevicePlayer devicePlayer;
}
