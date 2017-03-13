package net.squanchy.schedule.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import net.squanchy.R;
import net.squanchy.schedule.domain.view.Event;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class OtherEventItemView extends EventItemView {

    private final DateTimeFormatter dateTimeFormatter;

    private TextView titleView;
    private TextView timestampView;
    private ImageView illustrationView;

    public OtherEventItemView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.cardViewDefaultStyle);
    }

    public OtherEventItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.dateTimeFormatter = DateTimeFormat.shortTime();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        timestampView = (TextView) findViewById(R.id.timestamp);
        titleView = (TextView) findViewById(R.id.title);
        illustrationView = (ImageView) findViewById(R.id.illustration);
    }

    @Override
    public void updateWith(Event event) {
        ensureSupportedType(event.type());

        timestampView.setText(dateTimeFormatter.print(event.startTime()));
        titleView.setText(event.title());

        illustrationView.setImageResource(illustrationFor(event.type()));
    }

    @DrawableRes
    private int illustrationFor(Event.Type type) {
        switch (type) {
            case COFFEE_BREAK:
            case LUNCH:
            case OTHER:
            case REGISTRATION:
            case SOCIAL:
                return R.drawable.illustration_lunch;        // TODO replace these with the correct images once we have them
            default:
                throw new IllegalArgumentException("Type not supported: " + type.name());
        }
    }

    private void ensureSupportedType(Event.Type type) {
        if (type == Event.Type.COFFEE_BREAK
                || type == Event.Type.LUNCH
                || type == Event.Type.OTHER
                || type == Event.Type.REGISTRATION
                || type == Event.Type.SOCIAL) {
            return;
        }
        throw new IllegalArgumentException("Event with type " + type.name() + " is not supported by this view");
    }
}
