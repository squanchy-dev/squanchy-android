package com.ls.ui.view;

import com.ls.util.image.DrupalImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class CircleImageView extends DrupalImageView {

    private Bitmap cachedImage;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap roundBitmap = null;
        if (cachedImage != null) {
            roundBitmap = cachedImage;
        } else {

            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                roundBitmap = getCroppedBitmap(bitmap, getWidth());
            }
        }

        if (roundBitmap != null) {

            canvas.drawBitmap(roundBitmap, 0, 0, null);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        cachedImage = null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            cachedImage = null;
        }
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {

        Bitmap sbmp;

        bmp = getSquareBitmap(bmp);


        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        } else {
            sbmp = bmp;
        }

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);

        canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2,
                sbmp.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

    private static Bitmap getSquareBitmap(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }

        int size = Math.min(bmp.getHeight(), bmp.getWidth());
        int left = (bmp.getWidth() - size) / 2;
        int top = (bmp.getHeight() - size) / 2;
        return Bitmap.createBitmap(bmp, left, top, size, size);
    }


}
