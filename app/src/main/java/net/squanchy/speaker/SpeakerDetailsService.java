package net.squanchy.speaker;

import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.repository.SpeakerRepository;
import net.squanchy.speaker.domain.view.Speaker;

import io.reactivex.Observable;

class SpeakerDetailsService {

    private final SpeakerRepository speakerRepository;
    private final FirebaseAuthService authService;

    SpeakerDetailsService(SpeakerRepository speakerRepository, FirebaseAuthService authService) {
        this.speakerRepository = speakerRepository;
        this.authService = authService;
    }

    public Observable<Speaker> speaker(String speakerId) {
        return authService.ifUserSignedInThenObservableFrom(userId -> speakerRepository.speaker(speakerId));
    }
}
