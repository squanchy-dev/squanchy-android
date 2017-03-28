package net.squanchy.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;
import net.squanchy.navigation.Navigator;

public class AboutActivity extends TypefaceStyleableActivity {

    private static final String SQUANCHY_WEBSITE = "https://squanchy.net";
    private static final String SQUANCHY_GITHUB = "https://github.com/rock3r/squanchy";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        setupToolbar();

        Navigator navigator = AboutInjector.obtain(this)
                .navigator();

        findViewById(R.id.website_button).setOnClickListener(
                view -> navigator.toExternalUrl(SQUANCHY_WEBSITE)
        );

        findViewById(R.id.github_button).setOnClickListener(
                view -> navigator.toExternalUrl(SQUANCHY_GITHUB)
        );

        findViewById(R.id.foss_button).setOnClickListener(
                view -> navigator.toFossLicenses()
        );
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
