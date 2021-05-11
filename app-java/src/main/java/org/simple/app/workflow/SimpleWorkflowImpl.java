package org.simple.app.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.ActivityStub;
import io.temporal.workflow.ExternalWorkflowStub;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SimpleWorkflowImpl implements SimpleWorkflow {
    private final List<String> messageQueue = new ArrayList<>(10);
    private static final String INFO = "This is a simple Java Workflow";


    @Override
    public String exec() {

        ExternalWorkflowStub externalGoWorkflowStub = Workflow.newUntypedExternalWorkflowStub("simple-workflow-go");

        // Send 10 signals to Go workflow
        for(int i=0; i < 10; i++) {
            externalGoWorkflowStub.signal("simplesignal", "Hello from Java Workflow: " + i);
        }

        // Receive 10 signals from Go workflow
        Workflow.await(() -> messageQueue.size() == 10);

        String result = "";
        for(String m : messageQueue) {
            result += m + "\n";
        }

        // Call the Go Activity
        ActivityOptions options =
                ActivityOptions.newBuilder()
                        .setStartToCloseTimeout(Duration.ofSeconds(3))
                        .setTaskQueue("simple-queue-go")
                        .build();
        ActivityStub goActivity = Workflow.newUntypedActivityStub(options);
        result += goActivity.execute("GoActivity", String.class, "JavaWorkflow");

        ExternalWorkflowStub externalPHPWorkflowStub = Workflow.newUntypedExternalWorkflowStub("simple-workflow-php");

        // Send 10 signals to PHP workflow
        for(int i=0; i < 10; i++) {
            externalPHPWorkflowStub.signal("javaMessage", "Hello from Java Workflow: " + i);
        }

        return result;
    }

    @Override
    public void receiveMessage(String message) {
        messageQueue.add(message);
    }

    @Override
    public String getInfo() {
        return INFO;
    }
}
