package net.squanchy.imageloader;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

final class GlideImageLoader implements ImageLoader {

    private static final String FIREBASE_STORAGE_URL_SCHEMA = "gs://";

    private final RequestManager requestManager;

    GlideImageLoader(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public ImageRequest load(String url) {
        if (url.startsWith(FIREBASE_STORAGE_URL_SCHEMA)) {
            throw new IllegalArgumentException("To load images from Firebase Storage please obtain the StorageReference first and use that one.");
        }
        return new GlideUrlImageRequest(requestManager, url);
    }

    @Override
    public ImageRequest load(StorageReference storageReference) {
        return new GlideFirebaseImageRequest(requestManager, storageReference);
    }

    private static final class GlideUrlImageRequest extends GlideImageRequest<Uri> {

        private final Uri uri;

        GlideUrlImageRequest(RequestManager requestManager, String url) {
            super(requestManager);
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

        GlideFirebaseImageRequest(RequestManager requestManager, StorageReference storageReference) {
            super(requestManager);
            this.storageReference = storageReference;
        }

        DrawableTypeRequest<StorageReference> startLoading() {
            return glide()
                    .using(new FirebaseImageLoader())
                    .load(storageReference);
        }
    }

    private abstract static class GlideImageRequest<T> implements ImageRequest<T> {

        private final RequestManager requestManager;

        GlideImageRequest(RequestManager requestManager) {
            this.requestManager = requestManager;
        }

        @Override
        public void into(ImageView imageView) {
            startLoading().into(imageView);
        }

        abstract DrawableTypeRequest<T> startLoading();

        final RequestManager glide() {
            return requestManager;
        }
    }
}
