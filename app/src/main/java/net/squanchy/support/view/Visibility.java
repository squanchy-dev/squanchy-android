package net.squanchy.support.view;

import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef(value = {View.GONE, View.INVISIBLE, View.VISIBLE})
@Retention(RetentionPolicy.SOURCE)
public @interface Visibility {
    // Nothing to do, it's an IntDef annotation. Used by Lint only.
}
