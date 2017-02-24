package net.squanchy.imageloader;

import com.google.firebase.storage.StorageReference;

public interface ImageLoader {

    ImageRequest load(String url);

    ImageRequest load(StorageReference storageReference);
}
