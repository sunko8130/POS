package com.example.pos.delegate;

public interface OnRecyclerMultiItemClickListener extends BaseRecyclerListener {
    void onItemClick(int position);

    void onClickAction(int pos, int value);
}