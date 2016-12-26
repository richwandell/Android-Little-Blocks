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

import com.wandell.rich.reactblocks.R;

public class SummaryListView extends ListView {

    public static SummaryListView summaryListView;

    private SummaryAdapter adapter;

    private static int CURRENT_ITEM_ID = 0;

    private Item startGame = new Item("Start Game", R.mipmap.s_icon, new OnClickListener() {
        @Override
        public void onClick(View view) {
            SummaryActivity.summaryActivity.startGame();
        }
    });

    private Item showAchievements = new Item("Google Play Achievements", R.mipmap.play_ac_ribbon, new OnClickListener() {
        @Override
        public void onClick(View view) {
            SummaryActivity.summaryActivity.showAchievements();
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

    private void init(){
        summaryListView = this;
        adapter = new SummaryAdapter(new Item[]{
                startGame
        });
        setAdapter(adapter);
    }

    public void showLoggedinMenu(){
        adapter = new SummaryAdapter(new Item[]{
                startGame,
                showAchievements
        });
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
