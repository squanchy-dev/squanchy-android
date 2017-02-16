package net.squanchy.navigation.drawer;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import net.squanchy.R;
import net.squanchy.navigation.Navigator;

public class SelectionNavigator implements NavigationView.OnNavigationItemSelectedListener{

    private final Navigator navigator;

    public static SelectionNavigator initWith(@NonNull NavigationView target,
                                              @NonNull Navigator navigator) {

        return new SelectionNavigator(target, navigator);
    }

    private SelectionNavigator(@NonNull NavigationView target,
                               @NonNull Navigator navigator) {

        target.setNavigationItemSelectedListener(this);
        this.navigator = navigator;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_schedule:
                navigator.toSchedule();
                return true;
            case R.id.navigation_favorites:
                navigator.toFavorites();
                return true;
            case R.id.navigation_speakers:
                navigator.toSpeakers();
                return true;
        }

        return false;
    }
}
