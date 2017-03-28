package net.squanchy.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;

public class AboutActivity extends TypefaceStyleableActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        setupToolbar();

        findViewById(R.id.website_button).setOnClickListener(
                // TODO add a navigator and navigate
                view -> Toast.makeText(this, "Website NAO", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
