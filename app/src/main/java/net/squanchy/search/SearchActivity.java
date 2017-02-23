package net.squanchy.search;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import net.squanchy.R;

public class SearchActivity extends AbstractSearchActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void loadSearchConfig() {
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(this, SearchActivity.class);
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName));
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_search;
    }

    @Override
    protected int menuRes() {
        return R.menu.search_menu;
    }

    @Override
    protected int searchId() {
        return R.id.action_search;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(this, newText, Toast.LENGTH_SHORT).show();
        return false;
    }
}
