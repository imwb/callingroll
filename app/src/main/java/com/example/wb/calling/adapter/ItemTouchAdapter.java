package com.example.wb.calling.adapter;

/**
 * Created by wb on 16/2/18.
 */
public interface ItemTouchAdapter {
    void onItemMove(int fromPosition,int toPosition);
    void onItemDismiss(int position);
}
