package com.ls.drupalconapp.model;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.app.App;
import com.ls.drupalconapp.model.data.Event;
import com.ls.drupalconapp.model.data.Speaker;
import com.ls.drupalconapp.model.data.TimeRange;
import com.ls.drupalconapp.model.data.Track;
import com.ls.drupalconapp.model.data.Type;
import com.ls.drupalconapp.model.managers.BofsManager;
import com.ls.drupalconapp.model.managers.EventManager;
import com.ls.drupalconapp.model.managers.ProgramManager;
import com.ls.drupalconapp.model.managers.SocialManager;
import com.ls.drupalconapp.model.managers.SpeakerManager;
import com.ls.drupalconapp.model.managers.TracksManager;
import com.ls.drupalconapp.ui.adapter.item.EventItemCreator;
import com.ls.drupalconapp.ui.adapter.item.EventListItem;
import com.ls.drupalconapp.ui.adapter.item.HeaderItem;
import com.ls.drupalconapp.ui.adapter.item.ProgramItem;
import com.ls.drupalconapp.ui.adapter.item.TimeRangeItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EventGenerator {

    private EventManager mEventManager;
    private BofsManager mBofsManager;
    private SocialManager mSocialManager;
    private ProgramManager mProgramManager;

    private boolean mShouldBreak;

    public EventGenerator() {
        mEventManager = Model.instance().getEventManager();
        mBofsManager = Model.instance().getBofsManager();
        mSocialManager = Model.instance().getSocialManager();
        mProgramManager = Model.instance().createProgramManager();
    }

    public List<EventListItem> generate(long day, int eventClass, @NotNull EventItemCreator eventItemCreator) {
        List<TimeRange> ranges = mEventManager.getDistrictTimeRangeSafe(eventClass, day);

        List<EventListItem> eventListItems;
        if (eventClass == Event.SOCIALS_CLASS) {
            eventListItems = mSocialManager.getSocialItemsSafe(day);
        } else {
            eventListItems = mBofsManager.getBofsItemsSafe(day);
        }

        return getEventItems(eventItemCreator, eventListItems, ranges);
    }

    public List<EventListItem> generate(long day, int eventClass, List<Long> levelIds, List<Long> trackIds, @NotNull EventItemCreator eventItemCreator) {
        List<EventListItem> eventListItems = mProgramManager.getProgramItemsSafe(eventClass, day, levelIds, trackIds);
        if (mShouldBreak) {
            return new ArrayList<>();
        }

        List<TimeRange> ranges = mEventManager.getDistrictTimeRangeSafe(eventClass, day, levelIds, trackIds);
        return getEventItems(eventItemCreator, eventListItems, ranges);
    }

    public List<EventListItem> generateForFavorites(long day) {
        List<Event> events = mEventManager.getEventsByIdsAndDaySafe(day);
        return sortFavorites(fetchEventItems(events));
    }

    private List<EventListItem> sortFavorites(List<EventListItem> eventListItems) {
        List<EventListItem> result = new ArrayList<EventListItem>();

        if (eventListItems.isEmpty()) {
            return result;
        }

        List<EventListItem> schedules = new ArrayList<EventListItem>();
        List<EventListItem> bofs = new ArrayList<EventListItem>();
        List<EventListItem> socials = new ArrayList<EventListItem>();

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
            Collections.sort(schedules, new Comparator<EventListItem>() {
                @Override
                public int compare(EventListItem eventListItem, EventListItem eventListItem2) {
                    return Double.compare(eventListItem.getEvent().getFromTimeStamp(), eventListItem2.getEvent().getFromTimeStamp());
                }
            });
            schedules.add(0, new HeaderItem(App.getContext().getString(R.string.Schedule)));
            schedules.get(schedules.size() - 1).setLast(true);
        }

        if (!bofs.isEmpty()) {
            Collections.sort(bofs, new Comparator<EventListItem>() {
                @Override
                public int compare(EventListItem eventListItem, EventListItem eventListItem2) {
                    return Double.compare(eventListItem.getEvent().getFromTimeStamp(), eventListItem2.getEvent().getFromTimeStamp());
                }
            });
            bofs.add(0, new HeaderItem(App.getContext().getString(R.string.bofs)));
            bofs.get(bofs.size() - 1).setLast(true);
        }

        if (!socials.isEmpty()) {
            Collections.sort(socials, new Comparator<EventListItem>() {
                @Override
                public int compare(EventListItem eventListItem, EventListItem eventListItem2) {
                    return Double.compare(eventListItem.getEvent().getFromTimeStamp(), eventListItem2.getEvent().getFromTimeStamp());
                }
            });
            socials.add(0, new HeaderItem(App.getContext().getString(R.string.social_events)));
            socials.get(socials.size() - 1).setLast(true);
        }

        result.addAll(schedules);
        result.addAll(bofs);
        result.addAll(socials);

        return result;
    }

    private List<EventListItem> getEventItems(EventItemCreator eventItemCreator, List<EventListItem> events, List<TimeRange> ranges) {
        List<EventListItem> result = new ArrayList<EventListItem>();

        for (TimeRange timeRange : ranges) {
            if (mShouldBreak) {
                return result;
            }

            List<EventListItem> timeRangeEvents = new ArrayList<EventListItem>();
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
        SpeakerManager speakerManager = Model.instance().getSpeakerManager();
        TracksManager tracksManager = Model.instance().getTracksManager();
        List<EventListItem> result = new ArrayList<>();

        for (Event event : events) {
            List<Long> speakersIds = mEventManager.getEventSpeakerSafe(event.getId());
            List<Speaker> speakers = new ArrayList<Speaker>();
            List<String> speakersNames = new ArrayList<>();

            for (Long id : speakersIds) {
                speakers.addAll(speakerManager.getSpeakers(id));
            }

            Track track = tracksManager.getTrack(event.getTrack());
            TimeRangeItem item = new TimeRangeItem();
            item.setEvent(event);
            item.setFromTime(event.getFromTime());
            item.setToTime(event.getToTime());
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
        List<EventListItem> result = new ArrayList<EventListItem>();

        if (eventListItems.size() > 0) {
            Event firstEvent = eventListItems.get(0).getEvent();
            if (firstEvent == null) {
                return result;
            }

            long typeId = firstEvent.getType();

            TimeRangeItem timeRangeItem = (TimeRangeItem) eventItemCreator.getItem(firstEvent);
            if (firstEvent.getTimeRange().getFromTime() != null) {
                Calendar eventFromTime = firstEvent.getTimeRange().getFromTime();
                long eventDate = firstEvent.getTimeRange().getDate();
                Date date = parseEventDate(eventFromTime, eventDate);
                timeRangeItem.setDate(date);
            }

            switch ((int) typeId) {

                case Type.NONE:
                case Type.SPEACH:
                case Type.SPEACH_OF_DAY:

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

                    break;

                case Type.GROUP:
                case Type.WALKING:
                case Type.COFFEBREAK:
                case Type.LUNCH:
                case Type.REGISTRATION:
                    result.add(timeRangeItem);
                    break;

                case Type.ALL_DAY:
                    result.add(timeRangeItem);
//                    result.addAll(eventListItems);
                    break;
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

        return new Date(time.getTimeInMillis());
    }

    public void setShouldBreak(boolean shouldBreak) {
        mShouldBreak = shouldBreak;
        mProgramManager.getEventDao().setShouldBreak(shouldBreak);
    }
}
