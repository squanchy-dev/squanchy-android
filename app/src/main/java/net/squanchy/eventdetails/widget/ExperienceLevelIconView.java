package net.squanchy.eventdetails.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import net.squanchy.R;
import net.squanchy.eventdetails.domain.view.ExperienceLevel;

public class ExperienceLevelIconView extends ImageView {

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
        drawable.setLevel(experienceLevel.rawLevel());
    }
}
