package com.wandell.rich.reactblocks.GameBoard;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wandell.rich.reactblocks.R;

import static com.wandell.rich.reactblocks.State.TAG;


public class GameBoardFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "GameBoardFragment.onViewCreated");
    }
}
