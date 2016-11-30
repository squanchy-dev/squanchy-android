package com.connfa.model;

import android.content.Context;

import com.connfa.R;
import com.connfa.model.data.Event;
import com.connfa.model.data.Speaker;
import com.connfa.model.data.TimeRange;
import com.connfa.model.data.Track;
import com.connfa.model.managers.BofsManager;
import com.connfa.model.managers.EventManager;
import com.connfa.model.managers.FavoriteManager;
import com.connfa.model.managers.ProgramManager;
import com.connfa.model.managers.SocialManager;
import com.connfa.model.managers.SpeakerManager;
import com.connfa.model.managers.TracksManager;
import com.connfa.ui.adapter.item.EventItemCreator;
import com.connfa.ui.adapter.item.EventListItem;
import com.connfa.ui.adapter.item.HeaderItem;
import com.connfa.ui.adapter.item.ProgramItem;
import com.connfa.ui.adapter.item.TimeRangeItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class EventGenerator {

    private final Context mContext;

    private EventManager mEventManager;
    private BofsManager mBofsManager;
    private SocialManager mSocialManager;
    private ProgramManager mProgramManager;

    private boolean mShouldBreak;

    public EventGenerator(Context context) {
        this.mContext = context;
        this.mEventManager = Model.getInstance().getEventManager();
        this.mBofsManager = Model.getInstance().getBofsManager();
        this.mSocialManager = Model.getInstance().getSocialManager();
        this.mProgramManager = Model.getInstance().createProgramManager(context);
    }

    public List<EventListItem> generate(long day, int eventClass, @NotNull EventItemCreator eventItemCreator) {
        List<EventListItem> eventListItems;
        if (eventClass == Event.SOCIALS_CLASS) {
            eventListItems = mSocialManager.getSocialItemsSafe(day);
        } else {
            eventListItems = mBofsManager.getBofsItemsSafe(day);
        }
        if (mShouldBreak) {
            return new ArrayList<>();
        }

        List<TimeRange> ranges = mEventManager.getDistrictTimeRangeSafe(eventClass, day);
        if (mShouldBreak) {
            return new ArrayList<>();
        }

        return getEventItems(eventItemCreator, eventListItems, ranges);
    }

    public List<EventListItem> generate(long day, int eventClass, List<Long> levelIds, List<Long> trackIds, @NotNull EventItemCreator eventItemCreator) {
        List<EventListItem> eventListItems = mProgramManager.getProgramItemsSafe(eventClass, day, levelIds, trackIds);
        if (mShouldBreak) {
            return new ArrayList<>();
        }

        List<TimeRange> ranges = mEventManager.getDistrictTimeRangeSafe(eventClass, day, levelIds, trackIds);
        if (mShouldBreak) {
            return new ArrayList<>();
        }

        Collections.sort(eventListItems, new Comparator<EventListItem>() {
            @Override
            public int compare(EventListItem first, EventListItem second) {
                double order1 = first.getEvent().getOrder();
                double order2 = second.getEvent().getOrder();

                if (order1 > order2) {
                    return 1;
                } else if (order1 < order2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return getEventItems(eventItemCreator, eventListItems, ranges);
    }

    public List<EventListItem> generateForFavorites(long day, @NotNull EventItemCreator eventItemCreator) {

        FavoriteManager favoriteManager = new FavoriteManager(mContext);
        List<Long> favoriteEventIds = favoriteManager.getFavoriteEventsSafe();

        List<EventListItem> eventListItems = mProgramManager.getFavoriteProgramItemsSafe(favoriteEventIds, day);
        if (mShouldBreak) {
            return new ArrayList<>();
        }

        return sortFavorites(favoriteEventIds, eventListItems, day, eventItemCreator);
    }

    private List<EventListItem> sortFavorites(List<Long> favoriteEventIds, List<EventListItem> eventListItems, long day,
                                              @NotNull EventItemCreator eventItemCreator) {
        List<EventListItem> result = new ArrayList<>();

        if (eventListItems.isEmpty()) {
            return result;
        }

        List<EventListItem> schedules = new ArrayList<>();
        List<EventListItem> bofs = new ArrayList<>();
        List<EventListItem> socials = new ArrayList<>();

        for (EventListItem eventListItem : eventListItems) {
            Event event = eventListItem.getEvent();
            if (Event.PROGRAM_CLASS == event.getEventClass()) {
                schedules.add(eventListItem);
            } else if (Event.BOFS_CLASS == event.getEventClass()) {
                bofs.add(eventListItem);
            } else if (Event.SOCIALS_CLASS == event.getEventClass()) {
                socials.add(eventListItem);
            }
        }

        if (!schedules.isEmpty()) {

            List<TimeRange> ranges = mEventManager.getDistrictFavoriteTimeRangeSafe(Event.PROGRAM_CLASS, favoriteEventIds, day);
            schedules = getEventItems(eventItemCreator, schedules, ranges);
            schedules.add(0, new HeaderItem(mContext.getString(R.string.Sessions)));

        }

        if (!bofs.isEmpty()) {

            List<TimeRange> ranges = mEventManager.getDistrictFavoriteTimeRangeSafe(Event.BOFS_CLASS, favoriteEventIds, day);
            bofs = getEventItems(eventItemCreator, bofs, ranges);
            bofs.add(0, new HeaderItem(mContext.getString(R.string.bofs)));

        }

        if (!socials.isEmpty()) {

            List<TimeRange> ranges = mEventManager.getDistrictFavoriteTimeRangeSafe(Event.SOCIALS_CLASS, favoriteEventIds, day);
            socials = getEventItems(eventItemCreator, socials, ranges);
            socials.add(0, new HeaderItem(mContext.getString(R.string.social_events)));

        }

        result.addAll(schedules);
        result.addAll(bofs);
        result.addAll(socials);

        return result;
    }

    private List<EventListItem> getEventItems(EventItemCreator eventItemCreator, List<EventListItem> events, List<TimeRange> ranges) {
        List<EventListItem> result = new ArrayList<>();

        for (TimeRange timeRange : ranges) {
            if (mShouldBreak) {
                return result;
            }

            List<EventListItem> timeRangeEvents = new ArrayList<>();
            for (EventListItem eventListItem : events) {
                Event event = eventListItem.getEvent();
                if (event == null) {
                    continue;
                }

                if (timeRange.equals(event.getTimeRange())) {
                    timeRangeEvents.add(eventListItem);
                }
            }

            result.addAll(generateEventItems(timeRangeEvents, eventItemCreator));
        }

        return result;
    }

    private List<EventListItem> fetchEventItems(List<Event> events) {
        SpeakerManager speakerManager = Model.getInstance().getSpeakerManager();
        TracksManager tracksManager = Model.getInstance().getTracksManager();
        List<EventListItem> result = new ArrayList<>();

        for (Event event : events) {
            List<Long> speakersIds = mEventManager.getEventSpeakerSafe(event.getId());
            List<Speaker> speakers = new ArrayList<>();
            List<String> speakersNames = new ArrayList<>();

            for (Long id : speakersIds) {
                speakers.addAll(speakerManager.getSpeakers(id));
            }

            Track track = tracksManager.getTrack(event.getTrack());
            TimeRangeItem item = new TimeRangeItem();
            item.setEvent(event);
            item.setTrack(track != null ? track.getName() : null);

            for (Speaker speaker : speakers) {
                speakersNames.add(speaker.getFirstName() + " " + speaker.getLastName());
            }

            item.setSpeakers(speakersNames);
            result.add(item);
        }
        return result;
    }

    private List<EventListItem> generateEventItems(List<EventListItem> eventListItems,
                                                   EventItemCreator eventItemCreator) {
        TracksManager tracksManager = Model.getInstance().getTracksManager();
        List<EventListItem> result = new ArrayList<>();

        if (eventListItems.size() > 0) {
            EventListItem item = eventListItems.get(0);
            Event event = item.getEvent();
            if (event == null) {
                return result;
            }

            TimeRangeItem timeRangeItem = (TimeRangeItem) eventItemCreator.getItem(event);
            if (event.getTimeRange().getFromTime() != null) {
                Calendar eventFromTime = event.getTimeRange().getFromTime();
                long eventDate = event.getTimeRange().getDate();
                Date date = parseEventDate(eventFromTime, eventDate);
                timeRangeItem.setDate(date);
            }
            timeRangeItem.setSpeakers(item.getSpeakers());

            Track track = tracksManager.getTrack(event.getTrack());
            timeRangeItem.setTrack(track != null ? track.getName() : null);

            if (eventListItems.get(0) instanceof ProgramItem) {
                ProgramItem firstItem = (ProgramItem) eventListItems.get(0);
                timeRangeItem.setSpeakers(firstItem.getSpeakers());
                timeRangeItem.setTrack(firstItem.getTrack());
            }

            if (eventListItems.size() > 1) {
                timeRangeItem.setFirst(true);
                eventListItems.remove(0);
                eventListItems.get(eventListItems.size() - 1).setLast(true);
                result.add(timeRangeItem);
                result.addAll(eventListItems);
            } else {
                result.add(timeRangeItem);
            }

        }

        return result;
    }

    private Date parseEventDate(Calendar fromTime, long dateTime) {
        Calendar monthYear = Calendar.getInstance();
        monthYear.setTimeInMillis(dateTime);

        Calendar time = Calendar.getInstance();
        time.set(monthYear.get(Calendar.YEAR), monthYear.get(Calendar.MONTH), monthYear.get(Calendar.DAY_OF_MONTH));
        time.set(Calendar.HOUR_OF_DAY, fromTime.get(Calendar.HOUR_OF_DAY));
        time.set(Calendar.MINUTE, fromTime.get(Calendar.MINUTE));

        return time.getTime();
    }

    public void setShouldBreak(boolean shouldBreak) {
        mShouldBreak = shouldBreak;
        mProgramManager.getEventDao().setShouldBreak(shouldBreak);
    }
}
