package net.squanchy.contest;

import net.squanchy.remoteconfig.RemoteConfig;
import net.squanchy.service.firebase.FirebaseAuthService;
import net.squanchy.service.firebase.FirebaseDbService;
import net.squanchy.service.firebase.model.FirebaseAchievements;

import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class ContestService {

    private final RemoteConfig remoteConfig;
    private final FirebaseDbService dbService;
    private final FirebaseAuthService authService;

    public ContestService(RemoteConfig remoteConfig, FirebaseDbService dbService, FirebaseAuthService authService) {
        this.remoteConfig = remoteConfig;
        this.dbService = dbService;
        this.authService = authService;
    }

    public Single<ContestStandings> standings() {
        return authService.ifUserSignedInThenObservableFrom(dbService::achievements)
                .first(FirebaseAchievements.empty())
                .zipWith(remoteConfig.contestGoal(), buildStandings())
                .subscribeOn(Schedulers.io());
    }

    private BiFunction<FirebaseAchievements, Integer, ContestStandings> buildStandings() {
        return (achievements, goal) -> ContestStandings.create(goal, achievements.map.size());
    }

    public Single<ContestStandings> addAchievement(String checkpointId) {
        return authService.ifUserSignedInThenCompletableFrom(userId -> dbService.addAchievement(checkpointId, userId, System.currentTimeMillis()))
                .andThen(standings()).subscribeOn(Schedulers.io());
    }
}
