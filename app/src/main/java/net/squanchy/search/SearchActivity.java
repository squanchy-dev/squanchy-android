package net.squanchy.search;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;

public class SearchActivity extends TypefaceStyleableActivity implements SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener {

    private SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQuery("", false);
        searchView.clearFocus();
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        loadSearchConfig();
        return true;
    }

    private void loadSearchConfig() {
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(this, SearchActivity.class);
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(this, newText, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        //This activity should always be expanded
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        finish();
        return false;
    }
}
