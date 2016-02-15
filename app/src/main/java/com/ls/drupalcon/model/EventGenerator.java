package com.ls.drupalcon.model;

import com.ls.drupalcon.R;
import com.ls.drupalcon.app.App;
import com.ls.drupalcon.model.data.Event;
import com.ls.drupalcon.model.data.Speaker;
import com.ls.drupalcon.model.data.TimeRange;
import com.ls.drupalcon.model.data.Track;
import com.ls.drupalcon.model.data.Type;
import com.ls.drupalcon.model.managers.BofsManager;
import com.ls.drupalcon.model.managers.EventManager;
import com.ls.drupalcon.model.managers.ProgramManager;
import com.ls.drupalcon.model.managers.SocialManager;
import com.ls.drupalcon.model.managers.SpeakerManager;
import com.ls.drupalcon.model.managers.TracksManager;
import com.ls.ui.adapter.item.BofsItem;
import com.ls.ui.adapter.item.EventItemCreator;
import com.ls.ui.adapter.item.EventListItem;
import com.ls.ui.adapter.item.HeaderItem;
import com.ls.ui.adapter.item.ProgramItem;
import com.ls.ui.adapter.item.TimeRangeItem;

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

                if (order1 < order2) {
                    return 1;
                } else if (order1 > order2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

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
                    return Double.compare(eventListItem.getEvent().getFromMillis(), eventListItem2.getEvent().getFromMillis());
                }
            });
            schedules.add(0, new HeaderItem(App.getContext().getString(R.string.Sessions)));
            schedules.get(schedules.size() - 1).setLast(true);
        }

        if (!bofs.isEmpty()) {
            Collections.sort(bofs, new Comparator<EventListItem>() {
                @Override
                public int compare(EventListItem eventListItem, EventListItem eventListItem2) {
                    return Double.compare(eventListItem.getEvent().getFromMillis(), eventListItem2.getEvent().getFromMillis());
                }
            });
            bofs.add(0, new HeaderItem(App.getContext().getString(R.string.bofs)));
            bofs.get(bofs.size() - 1).setLast(true);
        }

        if (!socials.isEmpty()) {
            Collections.sort(socials, new Comparator<EventListItem>() {
                @Override
                public int compare(EventListItem eventListItem, EventListItem eventListItem2) {
                    return Double.compare(eventListItem.getEvent().getFromMillis(), eventListItem2.getEvent().getFromMillis());
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
            EventListItem item = eventListItems.get(0);
            Event event = item.getEvent();
            if (event == null) {
                return result;
            }

            long typeId = event.getType();

            TimeRangeItem timeRangeItem = (TimeRangeItem) eventItemCreator.getItem(event);
            if (event.getTimeRange().getFromTime() != null) {
                Calendar eventFromTime = event.getTimeRange().getFromTime();
                long eventDate = event.getTimeRange().getDate();
                Date date = parseEventDate(eventFromTime, eventDate);
                timeRangeItem.setDate(date);

                if (item instanceof ProgramItem) {
                    timeRangeItem.setSpeakers(((ProgramItem) item).getSpeakers());
                } else if (item instanceof BofsItem) {
                    timeRangeItem.setSpeakers(((BofsItem) item).getSpeakers());
                }
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

        return time.getTime();
    }

    public void setShouldBreak(boolean shouldBreak) {
        mShouldBreak = shouldBreak;
        mProgramManager.getEventDao().setShouldBreak(shouldBreak);
    }
}
