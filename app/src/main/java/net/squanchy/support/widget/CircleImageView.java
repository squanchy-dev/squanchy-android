package net.squanchy.support.widget;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        super.setOutlineProvider(new CircularOutlineProvider());
        super.setClipToOutline(true);
    }

    @Override
    public final void setOutlineProvider(ViewOutlineProvider provider) {
        throw new UnsupportedOperationException("Cannot set an outline provider on a CircleImageView");
    }

    @Override
    public final void setClipToOutline(boolean clipToOutline) {
        throw new UnsupportedOperationException("Cannot set clipping to outline on a CircleImageView");
    }

    @Override
    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != h) {
            throw new IllegalArgumentException("The width and height of this view must be identical");
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private static final class CircularOutlineProvider extends ViewOutlineProvider {

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }
    }
}
