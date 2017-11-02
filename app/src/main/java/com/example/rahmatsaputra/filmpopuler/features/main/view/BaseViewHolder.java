package com.example.rahmatsaputra.filmpopuler.features.main.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.rahmatsaputra.filmpopuler.features.main.model.MainItem;

/**
 * Created by RahmatSaputra on 21/10/2017.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindView(MainItem item);
}
