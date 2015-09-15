package com.ls.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class UIUtils {

    private UIUtils() {
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static void addColorSpanToTextView(TextView txtView, String strToSpan, String separator, int spanColor) {
        if (strToSpan == null) {
            txtView.setVisibility(View.GONE);
            return;
        }

        txtView.append(separator);

        SpannableString spanStr = new SpannableString(strToSpan);
        spanStr.setSpan(new ForegroundColorSpan(spanColor), 0, spanStr.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtView.append(spanStr);
    }

    public static void addSizeSupersciptSpanToTextView(TextView txtView, String strToSpan, float size) {
        if (strToSpan == null) {
            txtView.setVisibility(View.GONE);
            return;
        }

        SpannableString spanStr = new SpannableString(strToSpan);
        spanStr.setSpan(new RelativeSizeSpan(size), 0, strToSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new SuperscriptSpan(), 0, strToSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtView.append(spanStr);
    }

    public static void addMultiFontSpanToTextView(TextView txtView, Typeface typeFace,
                                                  String strToSpan, float size) {
        if (strToSpan == null) {
            txtView.setVisibility(View.GONE);
            return;
        }

        Spannable spanRAI = new SpannableString(strToSpan + " ");
        spanRAI.setSpan(new MultiFontsTypefaceSpan("", typeFace),
                0, spanRAI.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtView.append(spanRAI);
    }

    public static Bitmap decodeSampledBitmap(AssetManager assetManager, String filePath, int reqWidth, int reqHeight) {
        Bitmap result = null;

        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(assetManager.open(filePath), null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            result = BitmapFactory.decodeStream(assetManager.open(filePath), null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
