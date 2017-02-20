package net.squanchy.support.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import net.squanchy.R;

public class CardLayout extends FrameLayout {

    public CardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.cardViewDefaultStyle);
    }

    public CardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        setOutlineProvider(new NarrowerOutlineProvider(this));
    }


}
