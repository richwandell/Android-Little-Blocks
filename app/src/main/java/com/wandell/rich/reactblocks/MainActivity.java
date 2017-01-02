package com.wandell.rich.reactblocks;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.stats.PlayerStats;
import com.google.android.gms.games.stats.Stats;
import com.wandell.rich.reactblocks.GameBoard.GameBoardFragment;
import com.wandell.rich.reactblocks.Summary.SummaryFragment;
import com.wandell.rich.reactblocks.Summary.SummaryListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static MainActivity mainActivity;
    public static GoogleApiClient googleApiClient;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES_ERROR = 1973;
    public static final int REQUEST_ACHIEVEMENTS = 1983;
    public static final int REQUEST_LEADERBOARD = 1984;

    public static final String TAG = "richd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.main_fragment_container);
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
        mainActivity = this;

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();

        showSummary();
    }

    public void startGame() {
        getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.main_fragment, new GameBoardFragment())
                .addToBackStack("gameBoardFragment")
                .commit();
    }

    public void showSummary(){
        getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.main_fragment, new SummaryFragment())
                .addToBackStack("summaryFragment")
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();

        if (MainActivity.googleApiClient != null && MainActivity.googleApiClient.isConnected()) {
            Intent i = getIntent();
            if (i.hasExtra("achievements")) {
                ArrayList<String> achievements = i.getStringArrayListExtra("achievements");
                for (String ach : achievements) {
                    Games.Achievements.unlock(MainActivity.googleApiClient, ach);
                }
            }

            if (i.hasExtra("score")) {
                int score = i.getIntExtra("score", 0);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("richd", "connected");
        loggedIn();
    }

    public void showAchievements(){
        Intent achievementsIntent = Games.Achievements.getAchievementsIntent(googleApiClient);
        startActivityForResult(achievementsIntent, REQUEST_ACHIEVEMENTS);
    }

    public void showLeaderboard(){
        String leaderBoard = getString(R.string.leaderboard_top_single_game);
        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(googleApiClient,
                leaderBoard), REQUEST_LEADERBOARD);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("richd", "connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_GOOGLE_PLAY_SERVICES);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, REQUEST_GOOGLE_PLAY_SERVICES_ERROR);
            dialog.show();
        }
    }

    private void loggedIn(){
        Log.d(TAG, "logged into google play games");
        State.googlePlayEnabled = true;
        SummaryListView.summaryListView.showLoggedinMenu();
        checkPlayerStats();
    }

    public void checkPlayerStats() {
        PendingResult<Stats.LoadPlayerStatsResult> result = Games.Stats.loadPlayerStats(googleApiClient, false);

        result.setResultCallback(new ResultCallback<Stats.LoadPlayerStatsResult>() {

             public void onResult(Stats.LoadPlayerStatsResult result) {
                 Status status = result.getStatus();

                 if (status.isSuccess()) {
                     PlayerStats stats = result.getPlayerStats();

                     if (stats != null) {
                         Log.d(TAG, "Player stats loaded");
                         if (stats.getDaysSinceLastPlayed() > 7) {
                             Log.d(TAG, "It's been longer than a week");
                         }
                         if (stats.getNumberOfSessions() > 1000) {
                             Log.d(TAG, "Veteran player");
                         }
                         if (stats.getChurnProbability() == 1) {
                             Log.d(TAG, "Player is at high risk of churn");
                         }
                     }
                 } else {
                     Log.d(TAG, "Failed to fetch Stats Data status: "
                             + status.getStatusMessage());
                 }
             }

         });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    if (!googleApiClient.isConnected()) {
                        googleApiClient.connect();
                    }
                    loggedIn();
                } else if (resultCode == GamesActivityResultCodes.RESULT_SIGN_IN_FAILED) {
                    Log.d("richd", "sign in failed");
                }

                break;
            case REQUEST_GOOGLE_PLAY_SERVICES_ERROR:
                break;

            case REQUEST_LEADERBOARD:
            case REQUEST_ACHIEVEMENTS:
                if(resultCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED){
                    SummaryListView.summaryListView.showLoggedoutMenu();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
