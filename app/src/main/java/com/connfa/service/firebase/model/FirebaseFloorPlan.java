package com.connfa.service.firebase.model;

import java.util.List;

public class FirebaseFloorPlan {
    public Long floorPlanId;
    public String floorPlanName;
    public String floorPlanImageURL;
    public Boolean deleted;
    public Long order;

    public static class Holder {

        public List<FirebaseFloorPlan> floorPlans;
    }
}
