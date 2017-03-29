package net.squanchy.support.view;

import android.graphics.Rect;
import android.support.annotation.Px;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CardSpacingItemDecorator extends RecyclerView.ItemDecoration {

    @Px
    private final int horizontalSpacing;
    @Px
    private final int verticalSpacing;

    public CardSpacingItemDecorator(@Px int horizontalSpacing, @Px int verticalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int count = state.getItemCount();

        int topSpacing = verticalSpacing / 2;
        int bottomSpacing = topSpacing;
        if (position == 0) {
            topSpacing = verticalSpacing;
        } else if (position == count - 1) {
            bottomSpacing = verticalSpacing;
        }

        outRect.set(horizontalSpacing, topSpacing, horizontalSpacing, bottomSpacing);
    }
}
