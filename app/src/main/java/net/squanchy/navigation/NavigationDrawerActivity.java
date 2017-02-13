package net.squanchy.navigation;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.squanchy.R;
import net.squanchy.navigation.drawer.NavigationDrawer;
import net.squanchy.navigation.drawer.SelectionNavigator;

public abstract class NavigationDrawerActivity extends AppCompatActivity {

    private NavigationDrawer navigationDrawer;
    private ViewGroup container;

    @Override
    public final void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        onCreate(savedInstanceState);
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_drawer);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        View drawerContents = findViewById(R.id.navigation_drawer);
        navigationDrawer = new NavigationDrawer(drawerLayout, drawerContents);

        container = (ViewGroup) findViewById(R.id.navigation_drawer_main_container);
        inflateActivityContent(container);

        initializeActivity(savedInstanceState);

        SelectionNavigator.initWith((NavigationView) drawerContents, navigate());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigationDrawer.open();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected abstract void inflateActivityContent(ViewGroup parent);

    protected abstract void initializeActivity(Bundle savedInstanceState);

    protected final ViewGroup container() {
        return container;
    }

    protected abstract Navigator navigate();

    @Override
    public void onBackPressed() {
        if (navigationDrawer.isOpen()) {
            navigationDrawer.close();
        } else {
            super.onBackPressed();
        }
    }

}
