package net.squanchy.model.managers;

import android.content.Context;

import net.squanchy.model.PreferencesManager;
import net.squanchy.model.data.Event;
import net.squanchy.service.api.SquanchyRepository;
import net.squanchy.ui.adapter.item.EventListItem;
import net.squanchy.utils.DateUtils;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public class SocialManager extends EventManager {

    private final PreferencesManager preferencesManager;

    public SocialManager(Context context) {
        super(context);
        this.preferencesManager = PreferencesManager.create(context);
    }

    @Override
    protected String getEntityRequestTag() {
        return "social";
    }

    @Override
    protected boolean storeResponse(Event.Holder requestResponse, String tag) {
        List<Event.Day> socials = requestResponse.getDays();
        if (socials == null) {
            return false;
        }

        List<Long> ids = mEventDao.selectFavoriteEventsSafe();
        for (Event.Day day : socials) {

            for (Event event : day.getEvents()) {
                if (event != null) {

                    Date date = DateUtils.convertEventDayDate(getContext(), day.getDate());
                    if (date != null) {
                        event.setDate(date);
                    }
                    event.setEventClass(Event.SOCIALS_CLASS);

                    for (long id : ids) {
                        if (event.getId() == id) {
                            event.setFavorite(true);
                            break;
                        }
                    }

                    mEventDao.saveOrUpdateSafe(event);
                    saveEventSpeakers(event);

                    if (event.isDeleted()) {
                        deleteEvent(event);
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected Observable<Event.Holder> doFetch(SquanchyRepository repository) {
        return repository.socialEvents();
    }

    public List<Long> getSocialsDays() {
        List<Long> levelIds = preferencesManager.getExpLevels();
        List<Long> trackIds = preferencesManager.getTracks();

        if (levelIds.isEmpty() & trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateSafe(Event.SOCIALS_CLASS);

        } else if (!levelIds.isEmpty() & !trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateByTrackAndLevelIdsSafe(Event.SOCIALS_CLASS, levelIds, trackIds);

        } else if (!levelIds.isEmpty() & trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateByLevelIdsSafe(Event.SOCIALS_CLASS, levelIds);

        } else {
            return mEventDao.selectDistrictDateByTrackIdsSafe(Event.SOCIALS_CLASS, trackIds);
        }
    }

    public List<EventListItem> getSocialItemsSafe(long day) {
        return mEventDao.selectSocialItemsSafe(Event.SOCIALS_CLASS, day);
    }
}
