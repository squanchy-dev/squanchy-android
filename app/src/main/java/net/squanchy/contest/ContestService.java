package net.squanchy.contest;

import net.squanchy.remoteconfig.RemoteConfig;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ContestService {

    RemoteConfig remoteConfig;

    public ContestService(RemoteConfig remoteConfig) {
        this.remoteConfig = remoteConfig;
    }

    public Single<ContestStandings> updateContest(String checkpointId) {
        return remoteConfig.contestGoal()
                .flatMap(goal -> buildContestStanding(goal, checkpointId))
                .subscribeOn(Schedulers.io());
    }

    private Single<ContestStandings> buildContestStanding(Integer total, String checkpointId){
        // TODO add real impl
        return Single.just(ContestStandings.create(total, 0, ""));
    }
}
