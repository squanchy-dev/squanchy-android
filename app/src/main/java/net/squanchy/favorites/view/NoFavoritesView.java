package net.squanchy.favorites.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import net.squanchy.R;

public class NoFavoritesView extends LinearLayout {

    private static final int MAGIC_NUMBER_TO_TRIGGER_ACHIEVEMENT = 5;
    private FloatingActionButton favoriteButton;

    public NoFavoritesView(Context context) {
        super(context);
    }

    public NoFavoritesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoFavoritesView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        super.setOrientation(VERTICAL);
        inflate(getContext(), R.layout.merge_no_favorites_view, this);

        favoriteButton = (FloatingActionButton) findViewById(R.id.favorite_fab_example);
        favoriteButton.setOnClickListener(new OnClickListener() {
            int counter = 0;

            @Override
            public void onClick(View view) {
                favoriteButton.setImageResource(
                        counter % 2 == 0 ?
                                R.drawable.ic_favorite_filled :
                                R.drawable.ic_favorite_empty
                );

                counter++;
                if (counter == MAGIC_NUMBER_TO_TRIGGER_ACHIEVEMENT) {
                    Snackbar.make(NoFavoritesView.this, R.string.achievement_unlocked, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("Changing orientation is not supported for " + NoFavoritesView.class.getSimpleName());
    }
}
