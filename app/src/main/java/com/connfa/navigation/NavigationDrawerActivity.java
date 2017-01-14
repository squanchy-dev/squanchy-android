package com.connfa.navigation;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.connfa.R;
import com.connfa.navigation.drawer.NavigationDrawer;

public abstract class NavigationDrawerActivity extends AppCompatActivity {

    private NavigationDrawer navigationDrawer;
    private ViewGroup contentRoot;

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

        contentRoot = (ViewGroup) findViewById(R.id.content_root);
        inflateActivityContent(contentRoot);

        initializeActivity(savedInstanceState);
    }

    protected final ViewGroup contentRoot() {
        return contentRoot;
    }

    protected abstract void inflateActivityContent(ViewGroup parent);

    protected abstract void initializeActivity(Bundle savedInstanceState);

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
