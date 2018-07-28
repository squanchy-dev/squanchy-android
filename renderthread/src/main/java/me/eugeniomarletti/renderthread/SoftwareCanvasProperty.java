package me.eugeniomarletti.renderthread;

import androidx.annotation.NonNull;

public final class SoftwareCanvasProperty<T> extends CanvasProperty<T> {

    @NonNull
    private T value;

    SoftwareCanvasProperty(@NonNull T initialValue) {
        value = initialValue;
    }

    @NonNull
    T getValue() {
        return value;
    }

    void setValue(@NonNull T value) {
        this.value = value;
    }

    @Override
    public boolean isHardware() {
        return false;
    }
}
