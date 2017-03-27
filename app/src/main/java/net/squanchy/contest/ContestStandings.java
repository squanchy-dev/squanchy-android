package net.squanchy.contest;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ContestStandings {

    public static ContestStandings create(long goal, int current) {
        return new AutoValue_ContestStandings(goal, current);
    }

    public abstract long goal();

    public abstract int current();
}
