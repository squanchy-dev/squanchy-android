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

public class BofsManager extends EventManager {

    public BofsManager(Context context) {
        super(context);
    }

    @Override
    protected String getEntityRequestTag() {
        return "bofs";
    }

    @Override
    protected boolean storeResponse(Event.Holder requestResponse, String tag) {
        List<Event.Day> bofs = requestResponse.getDays();
        if (bofs == null) {
            return false;
        }

        List<Long> ids = mEventDao.selectFavoriteEventsSafe();
        for (Event.Day day : bofs) {

            for (Event event : day.getEvents()) {
                if (event != null) {

                    Date date = DateUtils.convertEventDayDate(getContext(), day.getDate());
                    if (date != null) {
                        event.setDate(date);
                    }
                    event.setEventClass(Event.BOFS_CLASS);

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
        return repository.bofs();
    }

    public List<Long> getBofsDays() {
        PreferencesManager preferencesManager = PreferencesManager.create(getContext());
        List<Long> levelIds = preferencesManager.getExpLevels();
        List<Long> trackIds = preferencesManager.getTracks();

        if (levelIds.isEmpty() && trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateSafe(Event.BOFS_CLASS);
        }

        if (!levelIds.isEmpty() && !trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateByTrackAndLevelIdsSafe(Event.BOFS_CLASS, levelIds, trackIds);
        }

        if (!levelIds.isEmpty() && trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateByLevelIdsSafe(Event.BOFS_CLASS, levelIds);

        }
        return mEventDao.selectDistrictDateByTrackIdsSafe(Event.BOFS_CLASS, trackIds);
    }

    public List<EventListItem> getBofsItemsSafe(long day) {
        return mEventDao.selectBofsItemsSafe(Event.BOFS_CLASS, day);
    }
}
