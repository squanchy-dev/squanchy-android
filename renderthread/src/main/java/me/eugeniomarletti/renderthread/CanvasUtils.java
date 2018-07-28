package me.eugeniomarletti.renderthread;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;

import androidx.annotation.NonNull;

@SuppressLint("ObsoleteSdkInt")     // Borrowed class, with a lower minSdkVersion
public final class CanvasUtils {

    private CanvasUtils() {
        // Not instantiable
    }

    private static final RectF TMP_RECT = new RectF();

    public static void drawRoundRect(
            @NonNull Canvas canvas,
            float left,
            float top,
            float right,
            float bottom,
            float rx,
            float ry,
            @NonNull Paint paint
    ) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            TMP_RECT.set(left, top, right, bottom);
            canvas.drawRoundRect(TMP_RECT, rx, ry, paint);
        } else {
            canvas.drawRoundRect(left, top, right, bottom, rx, ry, paint);
        }
    }
}
