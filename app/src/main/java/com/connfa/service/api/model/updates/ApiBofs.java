package com.connfa.service.api.model.updates;

import com.connfa.model.data.Event;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiBofs {

    @SerializedName("days")
    private List<Day> mDays = new ArrayList<Day>();

    public List<Day> getDays() {
        return mDays;
    }

    public static class Day {

        @SerializedName("date")
        private String date;

        @SerializedName("programEvents")
        private List<Event> mProgramEvents = new ArrayList<Event>();

        @SerializedName("bofsEvents")
        private List<Event> mBofsEvents = new ArrayList<Event>();

        @SerializedName("events") // perhaps this field will be renamed to "socialEvents" in future
        private List<Event> mSocialsEvents = new ArrayList<Event>();

        public String getDate() {
            return date;
        }

        public List<Event> getEvents() {
            List<Event> result = new ArrayList<Event>();
            result.addAll(mProgramEvents);
            result.addAll(mBofsEvents);
            result.addAll(mSocialsEvents);
            return result;
        }
    }
}
