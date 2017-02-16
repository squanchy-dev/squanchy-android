package net.squanchy.navigation;

public interface Navigator {

    void up();

    void toEventDetails(int dayId, int eventId);

    void toSchedule();

    void toFavorites();

    void toSpeakers();

}
