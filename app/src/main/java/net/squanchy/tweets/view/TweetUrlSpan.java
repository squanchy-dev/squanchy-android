/*
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

package net.squanchy.tweets.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import timber.log.Timber;

public class TweetUrlSpan extends ClickableSpan {

    private final String url;
    private final int linkColor;

    TweetUrlSpan(String url, int linkColor) {
        this.url = url;
        this.linkColor = linkColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(linkColor);
    }

    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        Intent intent = createIntentWith(context);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Timber.e(e, "Unable to start activity for Twitter url: %s", url);
        }
    }

    private Intent createIntentWith(Context context) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        return intent;
    }
}
