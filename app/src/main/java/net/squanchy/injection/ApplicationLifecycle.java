package net.squanchy.injection;

import javax.inject.Scope;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Scope
@Documented
@Retention(SOURCE)
public @interface ApplicationLifecycle {

}
