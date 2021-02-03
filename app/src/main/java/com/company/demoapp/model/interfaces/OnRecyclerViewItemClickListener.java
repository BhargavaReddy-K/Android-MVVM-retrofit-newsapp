package com.company.demoapp.model.interfaces;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface OnRecyclerViewItemClickListener<V extends RecyclerView.ViewHolder, D> {
    void onRecyclerViewItemClick( @NonNull View view, @NonNull D d);
}
