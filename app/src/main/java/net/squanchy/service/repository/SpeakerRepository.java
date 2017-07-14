package net.squanchy.service.repository;

import java.util.List;

import net.squanchy.speaker.domain.view.Speaker;

import io.reactivex.Observable;

import static net.squanchy.support.lang.Lists.map;

public interface SpeakerRepository {

    Observable<List<Speaker>> speakers();

    Observable<Speaker> speaker(String speakerId);
}
