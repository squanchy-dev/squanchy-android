package net.squanchy.imageloader;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

final class GlideImageLoader implements ImageLoader {

    private static final String FIREBASE_STORAGE_URL_SCHEMA = "gs://";

    private final Context context;

    GlideImageLoader(Context context) {
        this.context = context;
    }

    @Override
    public ImageRequest load(String url) {
        if (url.startsWith(FIREBASE_STORAGE_URL_SCHEMA)) {
            throw new IllegalArgumentException("To load images from Firebase Storage please obtain the StorageReference first and use that one.");
        }
        return new GlideUrlImageRequest(context, url);
    }

    @Override
    public ImageRequest load(StorageReference storageReference) {
        return new GlideFirebaseImageRequest(context, storageReference);
    }

    private static final class GlideUrlImageRequest extends GlideImageRequest<Uri> {

        private final Uri uri;

        GlideUrlImageRequest(Context context, String url) {
            super(context);
            this.uri = Uri.parse(url);
        }

        public void into(ImageView imageView) {
            startLoading().into(imageView);
        }

        @Override
        DrawableTypeRequest<Uri> startLoading() {
            return glide().load(uri);
        }
    }

    private static final class GlideFirebaseImageRequest extends GlideImageRequest<StorageReference> {

        private final StorageReference storageReference;

        GlideFirebaseImageRequest(Context context, StorageReference storageReference) {
            super(context);
            this.storageReference = storageReference;
        }

        DrawableTypeRequest<StorageReference> startLoading() {
            return glide()
                    .using(new FirebaseImageLoader())
                    .load(storageReference);
        }
    }

    private abstract static class GlideImageRequest<T> implements ImageRequest<T> {

        private final Context context;

        GlideImageRequest(Context context) {
            this.context = context;
        }

        @Override
        public void into(ImageView imageView) {
            startLoading().into(imageView);
        }

        abstract DrawableTypeRequest<T> startLoading();

        final RequestManager glide() {
            return Glide.with(context);
        }
    }
}
