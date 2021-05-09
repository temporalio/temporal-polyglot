package org.simple.app.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.ActivityStub;
import io.temporal.workflow.ExternalWorkflowStub;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SimpleWorkflowImpl implements SimpleWorkflow {
    List<String> messageQueue = new ArrayList<>(10);

    @Override
    public String exec() {
        ExternalWorkflowStub externalWorkflowStub = Workflow.newUntypedExternalWorkflowStub("simple_workflow-go");

        // Send 10 signals to Go worksflow
        for(int i=0; i < 10; i++) {
            externalWorkflowStub.signal("simplesignal", "Hello from Java Workflow: " + i);
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
                        .build();
        ActivityStub goActivity = Workflow.newUntypedActivityStub(options);
        result += goActivity.execute("GoActivity", String.class, "JavaWorkflow");

        return result;
    }

    @Override
    public void receiveMessage(String message) {
        messageQueue.add(message);
    }
}
