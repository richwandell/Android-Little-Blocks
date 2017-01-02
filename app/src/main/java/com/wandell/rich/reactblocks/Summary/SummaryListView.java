package com.wandell.rich.reactblocks.Summary;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wandell.rich.reactblocks.BuildConfig;
import com.wandell.rich.reactblocks.MainActivity;
import com.wandell.rich.reactblocks.R;
import com.wandell.rich.reactblocks.State;

public class SummaryListView extends ListView {

    public static SummaryListView summaryListView;

    private SummaryAdapter adapter;

    private static int CURRENT_ITEM_ID = 0;

    private Item startGame = new Item("Start Game", R.mipmap.s_icon, new OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity.mainActivity.startGame();
        }
    });

    private Item showAchievements = new Item("Google Play Achievements", R.mipmap.play_ac_ribbon, new OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity.mainActivity.showAchievements();
        }
    });

    private Item showLeaderboard = new Item("Google Play Leaderboard", R.mipmap.play_controller, new OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity.mainActivity.showLeaderboard();
        }
    });

    private Item showGamesLogin = new Item("Enable Google Play Games", R.mipmap.play_controller, new OnClickListener() {
        @Override
        public void onClick(View view) {
            State.googlePlayEnabled = true;
            if(MainActivity.googleApiClient.isConnected()){
                MainActivity.googleApiClient.reconnect();
            }else {
                MainActivity.googleApiClient.connect();
            }
        }
    });

    private Item testAchievement = new Item("Test Achievement 1", R.mipmap.play_controller, new OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    });


    public SummaryListView(Context context) {
        super(context);
        init();
    }

    public SummaryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SummaryListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Item[] makeItems(Item[] items){
        if(BuildConfig.DEBUG){
            Item[] newItems = new Item[items.length + 1];
            for(int i = 0; i < items.length; i++){
                newItems[i] = items[i];
            }
            newItems[newItems.length - 1] = testAchievement;
            return newItems;
        }
        return items;
    }

    private void init(){
        summaryListView = this;
        if(State.googlePlayEnabled) {
            if(MainActivity.googleApiClient.isConnected()) {
                showLoggedinMenu();
                return;
            }
        }
        showLoggedoutMenu();
    }

    public void showLoggedoutMenu(){
        adapter = new SummaryAdapter(makeItems(new Item[]{
                startGame,
                showGamesLogin
        }));
        setAdapter(adapter);
    }

    public void showLoggedinMenu(){
        adapter = new SummaryAdapter(makeItems(new Item[]{
            startGame,
            showAchievements,
            showLeaderboard
        }));
        setAdapter(adapter);
    }

    public class Item implements Comparable{
        private String text;
        private int image;
        private int id;
        private OnClickListener listener;

        public Item(String text, int image, @Nullable OnClickListener listener){
            CURRENT_ITEM_ID++;
            id = CURRENT_ITEM_ID;
            this.text = text;
            this.image = image;
            this.listener = listener;
        }

        public int getId(){
            return id;
        }

        @Override
        public int compareTo(Object item) {
            if(item instanceof Item) {
                if (this.id == ((Item) item).getId()) {
                    return 0;
                } else if (this.id < ((Item) item).getId()) {
                    return -1;
                }
                return 1;
            }
            return -2;
        }
    }

    private class SummaryAdapter extends BaseAdapter{

        private Item[] items;

        SummaryAdapter(Item[] items){
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.length;
        }

        @Override
        public Item getItem(int i) {
            return this.items[i];
        }

        @Override
        public long getItemId(int i) {
            return this.items[i].getId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = inflate(getContext(), R.layout.sb_score_list_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.sb_score_list_item_image);
            TextView txt = (TextView) v.findViewById(R.id.sb_score_list_item_text);
            txt.setText(this.items[i].text);
            img.setImageResource(this.items[i].image);
            if(this.items[i].listener != null){
                v.setOnClickListener(this.items[i].listener);
            }
            return v;
        }
    }
}
