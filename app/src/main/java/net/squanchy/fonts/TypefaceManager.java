package net.squanchy.fonts;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;

import net.squanchy.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public final class TypefaceManager {

    private TypefaceManager() {
        // Not instantiable
    }

    public static void init() {
        CalligraphyConfig.initDefault(createConfiguration());
    }

    private static CalligraphyConfig createConfiguration() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Quicksand-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    static ContextWrapper attachBaseContext(@NonNull Context context) {
        return CalligraphyContextWrapper.wrap(context);
    }
}
