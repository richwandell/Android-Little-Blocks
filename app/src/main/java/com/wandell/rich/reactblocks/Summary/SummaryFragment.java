package com.wandell.rich.reactblocks.Summary;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Player;
import com.wandell.rich.reactblocks.Helpers.TypewriterTextView;
import com.wandell.rich.reactblocks.MainActivity;
import com.wandell.rich.reactblocks.R;

import static com.wandell.rich.reactblocks.State.TAG;


public class SummaryFragment extends Fragment {

    FrameLayout summaryFragmentLayout;
    FrameLayout playButton;

    private boolean playButtonDown = false;

    private View.OnClickListener playButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity.mainActivity.startGame();
        }
    };

    private View.OnTouchListener playButtonTouched = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            FrameLayout layout = (FrameLayout)v;
            int childCount = layout.getChildCount();
            float text_size;
            switch(action){
                case MotionEvent.ACTION_DOWN:
                    text_size = getResources().getDimensionPixelSize(R.dimen.play_button_text_size_large);
                    for(int i = 0; i < childCount -1; i++){
                        TypewriterTextView child = (TypewriterTextView) layout.getChildAt(i);
                        child.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                    if(!v.isPressed()) {
                        text_size = getResources().getDimensionPixelSize(R.dimen.play_button_text_size);
                        for (int i = 0; i < childCount - 1; i++) {
                            TypewriterTextView child = (TypewriterTextView) layout.getChildAt(i);
                            child.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);
                        }
                    }
                    break;
            }
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "SummaryFragment.onCreateView");
        View v = inflater.inflate(R.layout.summary, container, false);
        summaryFragmentLayout = (FrameLayout)v;
        playButton = (FrameLayout) v.findViewById(R.id.play_button);
        playButton.setOnClickListener(playButtonClicked);
        playButton.setOnTouchListener(playButtonTouched);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "SummaryFragment.onViewCreated");
    }

    public void showPlayerIcon(Player currentPlayer) {
        Log.d(TAG, "SummaryFragment.showPlayerIcon");
        Uri icon = currentPlayer.getIconImageUri();
        ImageView view = (ImageView) summaryFragmentLayout.findViewById(R.id.summary_player_image);
        ImageManager imageManager = ImageManager.create(getActivity());
        imageManager.loadImage(view, icon);
        TextView tview = (TextView) summaryFragmentLayout.findViewById(R.id.summary_player_name);
        tview.setText(currentPlayer.getDisplayName());
    }
}
