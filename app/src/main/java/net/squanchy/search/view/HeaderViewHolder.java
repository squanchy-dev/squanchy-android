package net.squanchy.search.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class HeaderViewHolder extends RecyclerView.ViewHolder {

    HeaderViewHolder(View itemView) {
        super(itemView);
    }

    void updateWith(HeaderType headerType) {
        ((TextView) itemView).setText(headerType.headerTextResourceId());
    }
}
