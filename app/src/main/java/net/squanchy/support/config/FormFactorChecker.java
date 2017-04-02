package net.squanchy.support.config;

import android.content.Context;

import net.squanchy.R;

class FormFactorChecker {

    private final Context context;

    FormFactorChecker(Context context) {
        this.context = context;
    }

    boolean isTablet() {
        return context.getResources()
                .getBoolean(R.bool.isTablet);
    }
}
