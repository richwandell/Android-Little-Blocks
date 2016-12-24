package com.wandell.rich.reactblocks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SummaryListView extends ListView {

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
        setAdapter(new SummaryAdapter(this, new String[]{
                "Start Game",
        }, new int[]{
                R.mipmap.s_icon
        }));
    }

    private class SummaryAdapter extends BaseAdapter{

        private String[] names;

        private int[] images;

        SummaryAdapter(SummaryListView listView, String[] names, int[] images){
            this.names = names;
            this.images = images;
        }

        @Override
        public int getCount() {
            return this.names.length;
        }

        @Override
        public Object getItem(int i) {
            return this;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = inflate(getContext(), R.layout.sb_score_list_item, null);
            ImageView img = (ImageView) v.findViewById(R.id.sb_score_list_item_image);
            TextView txt = (TextView) v.findViewById(R.id.sb_score_list_item_text);
            txt.setText(this.names[i]);
            img.setImageResource(this.images[i]);

            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    SummaryActivity.summaryActivity.startGame();
                }
            });

            return v;
        }
    }
}
