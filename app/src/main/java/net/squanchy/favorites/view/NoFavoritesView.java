package net.squanchy.favorites.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.squanchy.R;

public class NoFavoritesView extends LinearLayout {

    private FloatingActionButton favoriteButton;
    private TextView text;

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

        text = (TextView) findViewById(R.id.text);
        favoriteButton = (FloatingActionButton) findViewById(R.id.favorite_fab_example);
        favoriteButton.setOnClickListener(new OnClickListener() {
            private boolean favorite;

            @Override
            public void onClick(View view) {
                favorite = !favorite;
                favoriteButton.setImageResource(
                        favorite ?
                                R.drawable.ic_favorite_filled :
                                R.drawable.ic_favorite_empty
                );
            }
        });
    }
}
