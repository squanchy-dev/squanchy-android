package net.squanchy.speaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;

public class SpeakerDetailsActivity extends TypefaceStyleableActivity {

    private static final String EXTRA_SPEAKER_ID = SpeakerDetailsActivity.class.getCanonicalName() + ".speaker_id";;

    public static Intent createIntent(Context context, String speakerId) {
        Intent intent = new Intent(context, SpeakerDetailsActivity.class);
        intent.putExtra(EXTRA_SPEAKER_ID, speakerId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speaker_details);
    }
}
