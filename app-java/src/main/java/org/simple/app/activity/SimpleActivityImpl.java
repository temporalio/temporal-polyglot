package org.simple.app.activity;

public class SimpleActivityImpl implements SimpleActivity {
    @Override
    public String sayHello(String from) {
        return "Java SimpleActivity - hello from: " + from;
    }
}
