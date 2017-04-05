package net.squanchy.contest;

import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseAchievements;
import net.squanchy.support.system.CurrentTime;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.Observable.combineLatest;

class ContestService {

    private final RemoteConfig remoteConfig;
    private final FirebaseDbService dbService;
    private final FirebaseAuthService authService;
    private final CurrentTime currentTime;

    ContestService(RemoteConfig remoteConfig, FirebaseDbService dbService, FirebaseAuthService authService, CurrentTime currentTime) {
        this.remoteConfig = remoteConfig;
        this.dbService = dbService;
        this.authService = authService;
        this.currentTime = currentTime;
    }

    Observable<ContestStandings> standings() {
        return combineLatest(
                authService.ifUserSignedInThenObservableFrom(dbService::achievements),
                remoteConfig.contestGoal().toObservable(),
                buildStandings()
        ).subscribeOn(Schedulers.io());
    }

    private BiFunction<FirebaseAchievements, Long, ContestStandings> buildStandings() {
        return (achievements, goal) -> ContestStandings.create(goal, achievements.getAchievements().size());
    }

    Completable addAchievement(String achievementId) {
        return authService.ifUserSignedInThenCompletableFrom(userId -> dbService.addAchievement(userId, achievementId, currentTime.currentTimestamp()))
                .subscribeOn(Schedulers.io());
    }
}
