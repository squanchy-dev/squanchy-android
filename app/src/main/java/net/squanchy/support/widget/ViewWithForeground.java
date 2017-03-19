package net.squanchy.support.widget;

import android.graphics.drawable.Drawable;

public interface ViewWithForeground {

    int foregroundGravity();

    void setForegroundGravity(int foregroundGravity);

    void setForeground(Drawable drawable);

    Drawable foreground();
}
