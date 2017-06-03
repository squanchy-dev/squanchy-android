package net.squanchy.eventdetails.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import net.squanchy.R;
import net.squanchy.eventdetails.domain.view.ExperienceLevel;

public class ExperienceLevelIconView extends AppCompatImageView {

    public ExperienceLevelIconView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExperienceLevelIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        super.setImageResource(R.drawable.ic_experience);
    }

    public void setExperienceLevel(ExperienceLevel experienceLevel) {
        Drawable drawable = getDrawable();
        drawable.setLevel(getLevelOf(experienceLevel));
    }

    @IntRange(from = 0, to  = 2)
    private int getLevelOf(ExperienceLevel experienceLevel) {
        switch(experienceLevel) {
            case BEGINNER:
                return 0;
            case INTERMEDIATE:
                return 1;
            case ADVANCED:
                return 2;
            default:
                throw new IllegalArgumentException("Invalid experience level value: " + experienceLevel.rawLevel());
        }
    }
}
