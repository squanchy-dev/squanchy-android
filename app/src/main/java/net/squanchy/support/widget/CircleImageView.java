/*
 * Portions derived from https://gist.github.com/chrisbanes/9091754
 * For those portions:
 *
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.squanchy.support.widget;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

public class CircleImageView extends ImageViewWithForeground {

    private static final CircularOutlineProvider CIRCULAR_OUTLINE_PROVIDER = new CircularOutlineProvider();

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        super.setOutlineProvider(CIRCULAR_OUTLINE_PROVIDER);
        super.setClipToOutline(true);
        super.setScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    public final void setOutlineProvider(ViewOutlineProvider provider) {
        throw new UnsupportedOperationException("Cannot set an outline provider on a CircleImageView");
    }

    @Override
    public final void setClipToOutline(boolean clipToOutline) {
        throw new UnsupportedOperationException("Cannot set clipping to outline on a CircleImageView");
    }

    @Override
    public final void setScaleType(ScaleType scaleType) {
        throw new UnsupportedOperationException("Cannot set scale type on a CircleImageView");
    }

    @Override
    public ViewOutlineProvider getOutlineProvider() {
        return CIRCULAR_OUTLINE_PROVIDER;
    }

    @Override
    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != h) {
            throw new IllegalArgumentException("The width and height of this view must be identical");
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private static final class CircularOutlineProvider extends ViewOutlineProvider {

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }
    }
}
