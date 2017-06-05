package net.squanchy.favorites.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import net.squanchy.R;

public class FavoritesSignedInEmptyLayout extends LinearLayout {

    private static final int TAPS_TO_TRIGGER_INITIAL_ACHIEVEMENT = 5;
    private static final int TAPS_TO_TRIGGER_PERSEVERANCE_ACHIEVEMENT = 15;

    private FloatingActionButton favoriteButton;

    public FavoritesSignedInEmptyLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FavoritesSignedInEmptyLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        super.setOrientation(VERTICAL);
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("Changing orientation is not supported for " + FavoritesSignedInEmptyLayout.class.getSimpleName());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        favoriteButton = (FloatingActionButton) findViewById(R.id.favorite_fab_example);
        favoriteButton.setOnClickListener(achievementAwardingClickListener());
    }

    private OnClickListener achievementAwardingClickListener() {
        return new OnClickListener() {

            private int counter = 0;

            @Override
            public void onClick(View view) {
                favoriteButton.setImageResource(
                        counter % 2 == 0
                                ? R.drawable.ic_favorite_filled
                                : R.drawable.ic_favorite_empty
                );

                counter++;
                if (counter == TAPS_TO_TRIGGER_INITIAL_ACHIEVEMENT) {
                    showAchievement(R.string.favorites_achievement_fast_learner);
                } else if (counter == TAPS_TO_TRIGGER_PERSEVERANCE_ACHIEVEMENT) {
                    showAchievement(R.string.favorites_achievement_persevering);
                    favoriteButton.setEnabled(false);
                }
            }

            private void showAchievement(int stringResId) {
                Snackbar.make(FavoritesSignedInEmptyLayout.this, readAsHtml(stringResId), Snackbar.LENGTH_LONG).show();
            }

            private CharSequence readAsHtml(int stringResId) {
                String text = favoriteButton.getResources().getString(stringResId);
                return Html.fromHtml(text);
            }
        };
    }
}
