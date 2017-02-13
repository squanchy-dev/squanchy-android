package net.squanchy.navigation;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
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
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    @Override
    public final void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        onCreate(savedInstanceState);
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_drawer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        View drawerContents = findViewById(R.id.navigation_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);
        drawerLayout.addDrawerListener(drawerToggle);

        navigationDrawer = new NavigationDrawer(drawerLayout, drawerContents);

        container = (ViewGroup) findViewById(R.id.navigation_drawer_main_container);
        inflateActivityContent(container);

        initializeActivity(savedInstanceState);

        SelectionNavigator.initWith((NavigationView) drawerContents, navigate());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        drawerLayout.removeDrawerListener(drawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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
