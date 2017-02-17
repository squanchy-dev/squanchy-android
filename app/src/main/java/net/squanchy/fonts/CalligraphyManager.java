package net.squanchy.fonts;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;

import net.squanchy.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CalligraphyManager {

    public static void init() {

        CalligraphyConfig.initDefault(createConfigurationBuilder());
    }

    private static CalligraphyConfig createConfigurationBuilder() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Quicksand-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    public static ContextWrapper attachBaseContext(@NonNull Context context) {
        return CalligraphyContextWrapper.wrap(context);
    }
}
