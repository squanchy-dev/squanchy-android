package com.ls.ui.drawer;

public class DrawerMenuItem {
    private long id;
    private String name;
    private int iconRes;
    private int selIconRes;

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public void setSelIconRes(int selIconRes) {
        this.selIconRes = selIconRes;
    }

    public int getSelIconRes() {
        return selIconRes;
    }

    private boolean group;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }
}
