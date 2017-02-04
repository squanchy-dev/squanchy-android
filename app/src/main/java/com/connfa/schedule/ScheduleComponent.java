package com.connfa.schedule;

import com.connfa.injection.ActivityLifecycle;
import com.connfa.injection.ApplicationComponent;

import dagger.Component;

@ActivityLifecycle
@Component(modules = {ScheduleModule.class}, dependencies = ApplicationComponent.class)
interface ScheduleComponent {

    ScheduleService service();

    ScheduleNavigator navigator();
}
