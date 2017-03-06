package net.squanchy.navigation;

public interface Navigator {

    void up();

    void toEventDetails(String dayId, String eventId);

    void toSearch();

}
