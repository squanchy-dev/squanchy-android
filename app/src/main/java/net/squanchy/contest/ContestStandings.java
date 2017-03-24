package net.squanchy.contest;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ContestStandings {

    public static ContestStandings create(int goal, int current, String message){
        return new AutoValue_ContestStandings(goal, current, message);
    }

    public abstract int goal();

    public abstract int current();

    public abstract String message();

}
