package net.squanchy.notification;

import android.content.Context;

import net.squanchy.analytics.Analytics;
import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {NotificationModule.class}, dependencies = ApplicationComponent.class)
interface NotificationComponent {

    NotificationService service();

    NotificationCreator notificationCreator();

    Notifier notifier();

    Context context();

    Analytics analytics();
}
