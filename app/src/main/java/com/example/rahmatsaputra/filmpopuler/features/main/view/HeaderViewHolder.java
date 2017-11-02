package com.example.rahmatsaputra.filmpopuler.features.main.view;

import android.view.View;
import android.widget.TextView;

import com.example.rahmatsaputra.filmpopuler.R;
import com.example.rahmatsaputra.filmpopuler.features.main.model.HeaderItem;
import com.example.rahmatsaputra.filmpopuler.features.main.model.MainItem;

/**
 * Created by RahmatSaputra on 24/10/2017.
 */

public class HeaderViewHolder extends BaseViewHolder {
    TextView headerField;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        headerField = itemView.findViewById(R.id.main_header_field);
    }

    @Override
    public void bindView(MainItem item) {
        final HeaderItem headerItem = (HeaderItem) item;
        headerField.setText(headerItem.getHeaderTitle());
    }
}
