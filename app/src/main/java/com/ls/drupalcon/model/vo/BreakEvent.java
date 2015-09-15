package com.ls.drupalcon.model.vo;

public class BreakEvent extends AbstractEvent {

    private boolean isLanchBreak = false;

    @Override
    public boolean isBreakEvent() {
        return true;
    }

    public boolean isLanchBreak() {
        return isLanchBreak;
    }

    public void setLanchBreak(boolean isLanchBreak) {
        this.isLanchBreak = isLanchBreak;
    }
}
