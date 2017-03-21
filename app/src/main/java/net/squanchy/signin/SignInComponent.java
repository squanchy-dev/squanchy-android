package net.squanchy.signin;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;

import dagger.Component;

@ActivityLifecycle
@Component(modules = SignInModule.class, dependencies = ApplicationComponent.class)
public interface SignInComponent {

    SignInService service();
}
