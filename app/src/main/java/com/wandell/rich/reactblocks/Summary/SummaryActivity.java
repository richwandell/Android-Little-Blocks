package com.wandell.rich.reactblocks.Summary;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.achievement.Achievements;
import com.wandell.rich.reactblocks.GameBoard.GameBoardActivity;
import com.wandell.rich.reactblocks.R;


public class SummaryActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static SummaryActivity summaryActivity;
    public static GoogleApiClient googleApiClient;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES_ERROR = 1973;
    public static final int REQUEST_GET_ACCOUNTS_PERMISSIONS = 1982;
    public static final int REQUEST_ACHIEVEMENTS = 1983;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_summary);
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
        summaryActivity = this;
        if (getIntent().hasExtra("score")) {

        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_GET_ACCOUNTS_PERMISSIONS);
        }
    }

    public void startGame() {
        Intent i = new Intent(this, GameBoardActivity.class);
        startActivity(i);
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
        Log.d("richd", "connected");
        SummaryListView.summaryListView.showLoggedinMenu();
    }

    public void showAchievements(){
        Intent achievementsIntent = Games.Achievements.getAchievementsIntent(googleApiClient);
        startActivityForResult(achievementsIntent, REQUEST_ACHIEVEMENTS);
    }

    private void showAccountName(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_GET_ACCOUNTS_PERMISSIONS);
            return;
        }
        String currentAccountName = Games.getCurrentAccountName(googleApiClient);
        Log.d("richd", currentAccountName);
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

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("richd", "result ok");
                } else if (resultCode == GamesActivityResultCodes.RESULT_SIGN_IN_FAILED) {
                    Log.d("richd", "sign in failed");
                }

                break;
            case REQUEST_GOOGLE_PLAY_SERVICES_ERROR:
                break;

            case REQUEST_ACHIEVEMENTS:
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("richd", Integer.toString(requestCode));
        switch (requestCode) {
            case REQUEST_GET_ACCOUNTS_PERMISSIONS:
                int getAccounts = -1;
                for (int i = 0; i < permissions.length; i++) {
                    if (Manifest.permission.GET_ACCOUNTS.compareTo(permissions[i]) == 0) {
                        getAccounts = grantResults[i];
                        break;
                    }
                }
                if (getAccounts == 0) {
                    showAccountName();
                }
        }
    }
}
