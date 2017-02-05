package net.squanchy.ui.adapter.item;

public class ProgramItem extends EventListItem {

    private String mTrack;
    private String mLevel;

    @Override
    public int getAdapterType() {
        return TYPE_PROGRAM;
    }

    public String getTrack() {
        return mTrack;
    }

    public void setTrack(String track) {
        mTrack = track;
    }

    public String getLevel() {
        return mLevel;
    }

    public void setLevel(String level) {
        mLevel = level;
    }

}
