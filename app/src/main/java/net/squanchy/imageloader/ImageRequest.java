package net.squanchy.imageloader;

import android.widget.ImageView;

public interface ImageRequest<T> {

    void into(ImageView imageView);
}
