package com.wandell.rich.reactblocks.LittleBox;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wandell.rich.reactblocks.GameBoard.GameBoard;
import com.wandell.rich.reactblocks.R;
import com.wandell.rich.reactblocks.State;

import java.util.ArrayList;


public class LittleBoxContainer extends LinearLayout {

    public static int rows;

    private ArrayList<LittleBox> littleBoxes = new ArrayList<>();

    private ArrayList<LittleBox> visibleBoxes = new ArrayList<>();

    private GameBoard board;

    private int xValue = 0;

    public LittleBoxContainer(Context context) {
        super(context);
        init();
    }

    public LittleBoxContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LittleBoxContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.little_box_container, this);
        this.setupHierarchy();
    }

    public boolean hasBoxAt(int y){
        int boxIndex = this.visibleBoxes.size() - 1;
        return boxIndex >= y;
    }

    public LittleBox getBoxAt(int y){
        return this.visibleBoxes.get(y);
    }

    public void lookForPoints() {
        LittleBox a;
        LittleBox b;
        LittleBox c;
        int ac;
        int bc;
        int cc;

        for (int y = 2; y < visibleBoxes.size(); y++) {
            a = visibleBoxes.get(y - 2);
            b = visibleBoxes.get(y - 1);
            c = visibleBoxes.get(y);
            ac = a.getColorNumber();
            bc = b.getColorNumber();
            cc = c.getColorNumber();

            if (
                    (
                            (ac == bc)
                                    ||
                                    (ac == -1 || bc == -1)
                    ) &&
                            (
                                    (ac == cc)
                                            ||
                                            (ac == -1 || cc == -1)
                            )
                    ) {
                this.setDying(
                        new int[]{a.getyValue(), b.getyValue(), c.getyValue()},
                        true
                );
                State.gameScore.addPoints(
                        a.getColorValue() + b.getColorValue() + c.getColorValue()
                );
            }
        }
    }

    public void setDying(int[] dying, boolean playClank) {
        board.setNeedsRemove(true);
        LittleBox box;
        for (int aDying : dying) {
            box = this.littleBoxes.get(aDying);
            box.setDying();
        }

        if(playClank){
            board.playMetalClank();
        }
    }

    public void setGrouped(int[] grouped, boolean left){
        LittleBox box;
        for(int aGrouped : grouped){
            box = this.littleBoxes.get(aGrouped);
            box.setGrouped(left, aGrouped == grouped[0]);
        }
    }

    public int removeBoxes(){
        this.visibleBoxes = new ArrayList<>();

        for (LittleBox aBox : this.littleBoxes) {
            if (!aBox.getDying()) {
                this.visibleBoxes.add(aBox);
            }else{
                aBox.setVisibility(View.GONE);
            }
        }
        return this.visibleBoxes.size();
    }

    private void setupHierarchy() {
        this.littleBoxes = new ArrayList<>();
        ViewGroup boxes = (ViewGroup) this.getChildAt(0);
        int childCount = boxes.getChildCount();
        LittleBoxContainer.rows = childCount;
        LittleBox box;
        for (int y = 0; y < childCount; y++) {
            box = (LittleBox) boxes.getChildAt(y);
            this.littleBoxes.add(box);
            this.visibleBoxes.add(box);
            box.setParent(this);
            box.setyValue(y);
        }
    }

    public void setParent(GameBoard board) {
        this.board = board;
    }

    public void setxValue(int xValue) {
        this.xValue = xValue;
    }
}
