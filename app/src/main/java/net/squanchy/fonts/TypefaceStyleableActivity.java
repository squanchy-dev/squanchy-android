package net.squanchy.fonts;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

public abstract class TypefaceStyleableActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypefaceManager.attachBaseContext(newBase));
    }
}
