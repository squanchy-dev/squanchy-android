package me.eugeniomarletti.renderthread;

import androidx.annotation.NonNull;
import me.eugeniomarletti.renderthread.typeannotation.CanvasProperty;

public final class HardwareCanvasProperty<T> extends me.eugeniomarletti.renderthread.CanvasProperty<T> {

    @NonNull
    @CanvasProperty
    private final Object property;

    HardwareCanvasProperty(@CanvasProperty @NonNull Object property) {
        this.property = property;
    }

    @NonNull
    @CanvasProperty
    Object getProperty() {
        return property;
    }

    @Override
    public boolean isHardware() {
        return true;
    }
}
