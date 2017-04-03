package net.squanchy.support.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import net.squanchy.R;

public class MaxSizeFrameLayout extends FrameLayout {

    @Px
    private static final int NO_CONSTRAINTS = Integer.MAX_VALUE;

    @Px
    private int maxWidth;

    @Px
    private int maxHeight;

    public MaxSizeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxSizeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MaxSizeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaxSizeFrameLayout, defStyleAttr, defStyleRes);

        try {
            setMaxWidth(a.getDimensionPixelSize(R.styleable.MaxSizeFrameLayout_android_maxWidth, NO_CONSTRAINTS));
            setMaxHeight(a.getDimensionPixelSize(R.styleable.MaxSizeFrameLayout_android_maxHeight, NO_CONSTRAINTS));
        } finally {
            a.recycle();
        }
    }

    @Px
    public int maxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(@Px int maxWidth) {
        this.maxWidth = maxWidth <= 0 ? NO_CONSTRAINTS : maxWidth;
    }

    @Px
    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(@Px int maxHeight) {
        this.maxHeight = maxHeight <= 0 ? NO_CONSTRAINTS : maxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int constrainedWidthSpec = widthMeasureSpec;
        int constrainedHeightSpec = heightMeasureSpec;

        if (MeasureSpec.getSize(widthMeasureSpec) > maxWidth) {
            constrainedWidthSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
        }
        if (MeasureSpec.getSize(heightMeasureSpec) > maxHeight) {
            constrainedHeightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
        }

        super.onMeasure(constrainedWidthSpec, constrainedHeightSpec);
    }
}
