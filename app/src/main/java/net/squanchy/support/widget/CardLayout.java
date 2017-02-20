package net.squanchy.support.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CardLayout extends FrameLayout {

    public CardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        setOutlineProvider(new NarrowerOutlineProvider(this));
    }


}
