package me.eugeniomarletti.renderthread;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;

public final class CanvasUtils {

    private CanvasUtils() {
    }

    private static final RectF TMP_RECT = Build.VERSION.SDK_INT < 21 ? new RectF() : null;

    public static void drawRoundRect(
            @NonNull Canvas canvas, float left, float top, float right, float bottom, float rx, float ry, @NonNull Paint paint) {

        if (Build.VERSION.SDK_INT < 21) {
            TMP_RECT.set(left, top, right, bottom);
            canvas.drawRoundRect(TMP_RECT, rx, ry, paint);
        } else {
            canvas.drawRoundRect(left, top, right, bottom, rx, ry, paint);
        }
    }
}
