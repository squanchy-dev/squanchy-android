package com.ls.drupalconapp.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ls.utils.BitmapUtils;

/**
 * Created by Kuhta on 01.09.2014.
 */
public class CircleNetworkImageView extends ImageView {
	private String mUrl;
	private int mDefaultImageId;
	private int mErrorImageId;
	private ImageLoader mImageLoader;
	private ImageLoader.ImageContainer mImageContainer;

	public CircleNetworkImageView(Context context) {
		super(context);
	}

	public CircleNetworkImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setImageUrl(String url, ImageLoader imageLoader) {
		mUrl = url;
		mImageLoader = imageLoader;

		loadImageIfNecessary(false);
	}

	public void setDefaultImageResId(int defaultImage) {
		mDefaultImageId = defaultImage;
	}

	public void setErrorImageResId(int errorImage) {
		mErrorImageId = errorImage;
	}

	void loadImageIfNecessary(final boolean isInLayoutPass) {
		if (TextUtils.isEmpty(mUrl)) {
			if (mImageContainer != null) {
				mImageContainer.cancelRequest();
				mImageContainer = null;
			}
			setDefaultImageOrNull();
			return;
		}

		if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
			if (mImageContainer.getRequestUrl().equals(mUrl)) {
				return;
			} else {
				mImageContainer.cancelRequest();
				setDefaultImageOrNull();
			}
		}

		ImageLoader.ImageContainer newContainer = mImageLoader.get(mUrl,
				new ImageLoader.ImageListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (mErrorImageId != 0) {
							setImageResource(mErrorImageId);
						}
					}

					@Override
					public void onResponse(final ImageLoader.ImageContainer response, boolean isImmediate) {
						if (isImmediate && isInLayoutPass) {
							post(new Runnable() {
								@Override
								public void run() {
									onResponse(response, false);
								}
							});
							return;
						}

						if (response.getBitmap() != null) {
							Bitmap bmp = response.getBitmap();
							bmp = BitmapUtils.getBitmapRect(bmp);
                            bmp = BitmapUtils.getRoundedCornerBitmap(bmp, 50);
							setImageBitmap(bmp);
						} else if (mDefaultImageId != 0) {
							setImageResource(mDefaultImageId);
						}
					}
				});
		mImageContainer = newContainer;
	}

	private void setDefaultImageOrNull() {
		if(mDefaultImageId != 0) {
			setImageResource(mDefaultImageId);
		}
		else {
			setImageBitmap(null);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		loadImageIfNecessary(true);
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mImageContainer != null) {
			mImageContainer.cancelRequest();
			setImageBitmap(null);
			mImageContainer = null;
		}
		super.onDetachedFromWindow();
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}
}
