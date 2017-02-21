package net.squanchy.support.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import net.squanchy.R;

public class CardLayout extends FrameLayout {

    public CardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.cardViewDefaultStyle);
    }

    public CardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CardLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        super.setClipToOutline(false);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardLayout, defStyleAttr, defStyleRes);
        int insetHorizontal;
        int insetTop;
        int insetBottom;
        int radius;
        try {
            ensureHasNecessaryAttributes(a);
            insetHorizontal = a.getDimensionPixelSize(R.styleable.CardLayout_cardInsetHorizontal, 0);
            insetTop = a.getDimensionPixelSize(R.styleable.CardLayout_cardInsetTop, 0);
            insetBottom = a.getDimensionPixelSize(R.styleable.CardLayout_cardInsetBottom, 0);
            radius = a.getDimensionPixelSize(R.styleable.CardLayout_cardCornerRadius, 0);
        } finally {
            a.recycle();
        }

        applyInsetsAndRadius(insetHorizontal, insetTop, insetBottom, radius);
    }

    private void ensureHasNecessaryAttributes(TypedArray a) {
        if (!a.hasValue(R.styleable.CardLayout_cardInsetHorizontal)) {
            throw new IllegalStateException("Missing the cardInsetHorizontal attribute in the style");
        }
        if (!a.hasValue(R.styleable.CardLayout_cardInsetTop)) {
            throw new IllegalStateException("Missing the cardInsetTop attribute in the style");
        }
        if (!a.hasValue(R.styleable.CardLayout_cardInsetBottom)) {
            throw new IllegalStateException("Missing the cardInsetBottom attribute in the style");
        }
        if (!a.hasValue(R.styleable.CardLayout_cardCornerRadius)) {
            throw new IllegalStateException("Missing the cardCornerRadius attribute in the style");
        }
    }

    private void applyInsetsAndRadius(int insetHorizontal, int insetTop, int insetBottom, int radius) {
        super.setOutlineProvider(
                new NarrowerOutlineProvider(insetHorizontal, insetTop, insetBottom, radius)
        );
    }

    @Override
    public final void setOutlineProvider(ViewOutlineProvider provider) {
        throw new UnsupportedOperationException("Cannot set an outline provider on a CardLayout");
    }

    @Override
    public final void setClipToOutline(boolean clipToOutline) {
        throw new UnsupportedOperationException("Cannot set clipping to outline on a CardLayout");
    }

    private static class NarrowerOutlineProvider extends ViewOutlineProvider {

        @Px
        private final int insetHorizontal;

        @Px
        private final int insetTop;

        @Px
        private final int insetBottom;

        @Px
        private final int radius;

        private NarrowerOutlineProvider(@Px int insetHorizontal, @Px int insetTop, @Px int insetBottom, @Px int radius) {
            this.insetHorizontal = insetHorizontal;
            this.insetTop = insetTop;
            this.insetBottom = insetBottom;
            this.radius = radius;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(
                    insetHorizontal,
                    insetTop,
                    view.getWidth() - insetHorizontal,
                    view.getHeight() - insetBottom,
                    radius
            );
        }
    }
}
