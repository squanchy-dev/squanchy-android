package net.squanchy.notification;

import android.content.Context;

import net.squanchy.analytics.Analytics;
import net.squanchy.analytics.AnalyticsModule;
import net.squanchy.analytics.ProximityAnalytics;
import net.squanchy.injection.ActivityLifecycle;
import net.squanchy.injection.ApplicationComponent;
import net.squanchy.injection.ApplicationLifecycle;

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
