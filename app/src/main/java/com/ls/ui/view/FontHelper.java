package com.ls.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;

public class FontHelper {

    private static FontHelper instance = null;

    private Typeface fontRobotoRegular;
    private Typeface fontRobotoThin;
    private Typeface fontRobotoLight;
    private Typeface fontRobotoMedium;
	private Typeface fontRobotoBold;

    private FontHelper() {
    }

    public static FontHelper getInstance(Context context) {
        if(instance == null) {
            init(context);
        }

        return instance;
    }

    /*
     * init FontHelper in Application in onCreate() method
     */
    public static void init(Context context) {
        if(instance == null) {
            instance = new FontHelper();
            instance.loadFont(context);
        }
    }

    public Typeface getFontRobotoRegular() {
        return fontRobotoRegular;
    }

    public void setFontRobotoRegular(Typeface fontRobotoRegular) {
        this.fontRobotoRegular = fontRobotoRegular;
    }

    public Typeface getFontRobotoThin() {
        return fontRobotoThin;
    }

    public void setFontRobotoThin(Typeface fontRobotoThin) {
        this.fontRobotoThin = fontRobotoThin;
    }

    public Typeface getFontRobotoLight() {
        return fontRobotoLight;
    }

    public void setFontRobotoLight(Typeface fontRobotoLight) {
        this.fontRobotoLight = fontRobotoLight;
    }

    public Typeface getFontRobotoMedium() {
        return fontRobotoMedium;
    }

    public void setFontRobotoMedium(Typeface fontRobotoMedium) {
        this.fontRobotoMedium = fontRobotoMedium;
    }

	public Typeface getFontRobotoBold(){
		return fontRobotoBold;
	}

	public void setFontRobotoBold(Typeface fontRobotoBold){
		this.fontRobotoBold = fontRobotoBold;
	}

    public void loadFont(final Context context) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                instance.setFontRobotoRegular(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf"));
                instance.setFontRobotoThin(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf"));
                instance.setFontRobotoLight(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));
                instance.setFontRobotoMedium(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf"));
				instance.setFontRobotoBold(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf"));
                return null;
            }

        }.execute();
    }
}
