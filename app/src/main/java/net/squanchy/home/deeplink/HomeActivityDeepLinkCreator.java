package net.squanchy.home.deeplink;

import android.content.Context;
import android.content.Intent;

import net.squanchy.home.BottomNavigationSection;
import net.squanchy.home.HomeActivity;
import net.squanchy.support.lang.Optional;

import timber.log.Timber;

public class HomeActivityDeepLinkCreator {

    static final String KEY_INITIAL_PAGE_INDEX = "HomeActivity.initial_page_index";
    static final String KEY_DAY_ID = "HomeActivity.day_id";
    static final String KEY_EVENT_ID = "HomeActivity.event_id";

    private final Context context;

    public HomeActivityDeepLinkCreator(Context context) {
        this.context = context;
    }

    public Builder deepLinkTo(BottomNavigationSection section) {
        return new Builder(context).withSection(section);
    }

    public static class Builder {

        private final Context context;

        private BottomNavigationSection section;
        private Optional<String> dayId = Optional.absent();
        private Optional<String> eventId = Optional.absent();

        Builder(Context context) {
            // Not instantiable
            this.context = context;
        }

        Builder withSection(BottomNavigationSection section) {
            this.section = section;
            return this;
        }

        public Builder withDayId(Optional<String> dayId) {
            this.dayId = dayId;
            return this;
        }

        public Builder withEventId(Optional<String> eventId) {
            this.eventId = eventId;
            return this;
        }

        public Intent build() {
            Intent intent = createIntentForSection(context, section);
            return maybeAddIdsTo(intent);
        }

        private Intent createIntentForSection(Context context, BottomNavigationSection section) {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.putExtra(KEY_INITIAL_PAGE_INDEX, section.ordinal());
            return intent;
        }

        private Intent maybeAddIdsTo(Intent intent) {
            if (canHaveIds(section)) {
                return addIdsTo(intent);
            } else if (dayId.isPresent() || eventId.isPresent()) {
                throw new IllegalStateException("The section " + section + " does not support having IDs, and yet IDs were attached to the builder");
            } else {
                return intent;
            }
        }

        private boolean canHaveIds(BottomNavigationSection section) {
            return section == BottomNavigationSection.SCHEDULE;
        }

        private Intent addIdsTo(Intent intent) {
            Intent intentWithIds = new Intent(intent);
            if (dayId.isPresent()) {
                intentWithIds.putExtra(KEY_DAY_ID, dayId.get());
                if (eventId.isPresent()) {
                    intentWithIds.putExtra(KEY_EVENT_ID, eventId.get());
                }
            } else if (eventId.isPresent()) {
                Timber.e("Invalid deeplink containing an EventId (%s) but no DayId", eventId.get());
            }
            return intentWithIds;
        }
    }
}
