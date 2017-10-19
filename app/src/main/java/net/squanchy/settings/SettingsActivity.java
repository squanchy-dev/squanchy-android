package net.squanchy.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import net.squanchy.R;
import net.squanchy.settings.view.SettingsHeaderLayout;
import net.squanchy.signin.SignInService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class SettingsActivity extends AppCompatActivity {

    private SignInService signInService;
    private SettingsHeaderLayout headerLayout;
    private Disposable subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        headerLayout = (SettingsHeaderLayout) findViewById(R.id.settings_header);

        setupToolbar();

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .commit();

        SettingsActivityComponent component = SettingsInjector.obtainForActivity(this);
        signInService = component.signInService();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        subscription = signInService.currentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> headerLayout.updateWith(user));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscription.dispose();
    }
}
