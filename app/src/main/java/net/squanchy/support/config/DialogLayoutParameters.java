package net.squanchy.support.config;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.Window;

public final class DialogLayoutParameters {

    private final FormFactorChecker formFactorChecker;
    private final int height;

    public static DialogLayoutParameters fullHeight(Activity activity) {
        return new DialogLayoutParameters(
                new FormFactorChecker(activity),
                ViewGroup.LayoutParams.MATCH_PARENT
        );
    }

    public static DialogLayoutParameters wrapHeight(Activity activity) {
        return new DialogLayoutParameters(
                new FormFactorChecker(activity),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    private DialogLayoutParameters(FormFactorChecker formFactorChecker, int height) {
        this.formFactorChecker = formFactorChecker;
        this.height = height;
    }

    public void applyTo(Window window) {
        if (formFactorChecker.isTablet()) {
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, height);
        } else {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);
        }
    }
}
