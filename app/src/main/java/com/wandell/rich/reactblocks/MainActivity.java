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
import com.google.android.gms.games.Player;
import com.google.android.gms.games.stats.PlayerStats;
import com.google.android.gms.games.stats.Stats;
import com.wandell.rich.reactblocks.GameBoard.GameBoardFragment;
import com.wandell.rich.reactblocks.Helpers.DBOpenHelper;
import com.wandell.rich.reactblocks.Summary.SummaryFragment;
import com.wandell.rich.reactblocks.Summary.SummaryListView;

import static com.wandell.rich.reactblocks.State.TAG;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static MainActivity mainActivity;
    public static GoogleApiClient googleApiClient;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES_ERROR = 1973;
    public static final int REQUEST_ACHIEVEMENTS = 1983;
    public static final int REQUEST_LEADERBOARD = 1984;

    private SummaryFragment summaryFragment;


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
        //setupLocalDatabase();
    }

    public void setupLocalDatabase(){
        DBOpenHelper database = new DBOpenHelper(this);
        DBOpenHelper.DevicePlayer devicePlayer = database.getDevicePlayer(0, null);
        Log.d(TAG, "device player name: " + devicePlayer.getName());
    }

    public void startGame() {
        State.GOLD_BLOCKS = 0;
        getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.main_fragment, new GameBoardFragment())
                .addToBackStack("gameBoardFragment")
                .commit();
    }

    public void showSummary(){
        DBOpenHelper database = new DBOpenHelper(this);
        DBOpenHelper.DevicePlayer dp = database.getDevicePlayer(0, null);
        if(summaryFragment == null) {
            summaryFragment = new SummaryFragment();
        }
        getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.main_fragment, summaryFragment)
                .addToBackStack("summaryFragment")
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
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
        Log.d(TAG, "connected");
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
        Log.d(TAG, "connection suspended");
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
        Log.d(TAG, "MainActivity.loggedIn");
        State.googlePlayEnabled = true;
        displayLoggedInSummary();
        checkPlayerStats();
    }

    public void displayLoggedInSummary(){
        Player currentPlayer = Games.Players.getCurrentPlayer(googleApiClient);
        summaryFragment.showPlayerIcon(currentPlayer);
        SummaryListView.summaryListView.showLoggedinMenu();
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
                    } else {
                        loggedIn();
                    }
                } else if (resultCode == GamesActivityResultCodes.RESULT_SIGN_IN_FAILED) {
                    Log.d(TAG, "sign in failed");
                }

                break;
            case REQUEST_GOOGLE_PLAY_SERVICES_ERROR:
                Log.d(TAG, "REQUEST_GOOGLE_PLAY_SERVICES_ERROR");
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
