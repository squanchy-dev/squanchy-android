package net.squanchy.model.managers;

import android.content.Context;

import net.squanchy.model.dao.EventDao;
import net.squanchy.model.dao.SpeakerDao;
import net.squanchy.model.data.Speaker;
import net.squanchy.service.api.ConnfaRepository;

import java.util.List;

import io.reactivex.Observable;

public class SpeakerManager extends SynchronousItemManager<Speaker.Holder, String> {

    private SpeakerDao mSpeakerDao;

    public SpeakerManager(Context context) {
        super(context);
        this.mSpeakerDao = new SpeakerDao(context);
    }

    @Override
    protected String getEntityRequestTag() {
        return "speakers";
    }

    @Override
    protected boolean storeResponse(Speaker.Holder requestResponse, String tag) {
        List<Speaker> speakers = requestResponse.getSpeakers();
        if (speakers == null) {
            return false;
        }

        EventDao eventDao = new EventDao(getContext());
        mSpeakerDao.saveOrUpdateDataSafe(speakers);
        for (Speaker speaker : speakers) {
            if (speaker != null) {
                if (speaker.isDeleted()) {
                    mSpeakerDao.deleteDataSafe(speaker.getId());
                    eventDao.deleteEventAndSpeakerBySpeaker(speaker.getId());
                }
            }
        }

        return true;
    }

    @Override
    protected Observable<Speaker.Holder> doFetch(ConnfaRepository repository) {
        return repository.speakers();
    }

    public List<Speaker> getSpeakers() {
        return mSpeakerDao.selectSpeakersOrderedByName();
    }

    public List<Speaker> getSpeakers(long speakerId) {
        return mSpeakerDao.getDataSafe(speakerId);
    }

    public List<Speaker> getSpeakersByEventId(long eventId) {
        return mSpeakerDao.getSpeakersByEventId(eventId);
    }

    public Speaker getSpeakerById(long id) {
        List<Speaker> speakerById = mSpeakerDao.getSpeakerById(id);
        if (!speakerById.isEmpty()) {
            return speakerById.get(0);
        } else {
            return null;
        }
    }

    public void clear() {
        mSpeakerDao.deleteAll();
    }
}
