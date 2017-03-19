package net.squanchy.notification;

import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {NotificationModule.class}, dependencies = ApplicationComponent.class)
interface NotificationComponent {

    NotificationService service();

}
