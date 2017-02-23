package net.squanchy.search;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import net.squanchy.fonts.TypefaceStyleableActivity;

public abstract class AbstractSearchActivity extends TypefaceStyleableActivity implements SearchView.OnQueryTextListener,
        MenuItemCompat.OnActionExpandListener {

    protected SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menuRes(), menu);
        MenuItem searchItem = menu.findItem(searchId());
        searchItem.expandActionView();
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchItem, this);
        loadSearchConfig();
        return true;
    }

    protected abstract void loadSearchConfig();

    @LayoutRes
    protected abstract int layoutRes();

    @MenuRes
    protected abstract int menuRes();

    @IdRes
    protected abstract int searchId();

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
