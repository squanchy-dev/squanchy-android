package net.squanchy.service.firebase.model;

import java.util.List;

public class FirebaseType {

    public Long typeId;

    public String typeName;

    public String typeIconURL;

    public Long order;

    public Boolean deleted;

    public static class Holder {

        public List<FirebaseType> types;

    }

}
