package net.squanchy.about.licenses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import net.squanchy.R;
import net.squanchy.support.view.CardSpacingItemDecorator;

public class LicensesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_licenses);
        setupToolbar();

        RecyclerView licensesList = (RecyclerView) findViewById(R.id.libraries_list);
        licensesList.setAdapter(new LibrariesAdapter(this));
        licensesList.setLayoutManager(new LinearLayoutManager(this));

        int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.card_horizontal_margin);
        int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.card_vertical_margin);
        licensesList.addItemDecoration(new CardSpacingItemDecorator(horizontalSpacing, verticalSpacing));
    }

    @SuppressWarnings("ConstantConditions")     // We set the actionbar up ourselves in here, won't be null
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
