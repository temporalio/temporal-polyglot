package org.simple.app.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface SimpleActivity {
    @ActivityMethod(name = "javaSayHello")
    String sayHello(String from);
}
