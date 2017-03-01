package net.squanchy.eventdetails.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import net.squanchy.R;
import net.squanchy.eventdetails.domain.view.ExperienceLevel;

public class ExperienceLevelIconView extends android.support.v7.widget.AppCompatImageView {

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

    public void setExperienceLevel(String experienceLevel) {
        Drawable drawable = getDrawable();
        drawable.setLevel(ExperienceLevel.getRawLevel(experienceLevel));
    }
}
