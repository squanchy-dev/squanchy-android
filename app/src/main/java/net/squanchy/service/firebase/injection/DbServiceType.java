package net.squanchy.service.firebase.injection;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Retention(RetentionPolicy.SOURCE)
public @interface DbServiceType {

    Type value();

    enum Type {
        AUTHENTICATED,
        UNAUTHENTICATED
    }
}
