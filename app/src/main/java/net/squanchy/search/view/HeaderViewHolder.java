package net.squanchy.search.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    public HeaderViewHolder(View itemView) {
        super(itemView);
    }

    public void updateWith(HeaderType headerType) {
        ((TextView) itemView).setText(headerType.headerTextResourceId());
    }

    public void updateWith(CharSequence label) {
        ((TextView) itemView).setText(label);
    }
}
