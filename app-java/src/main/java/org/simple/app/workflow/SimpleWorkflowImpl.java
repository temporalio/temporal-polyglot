package org.simple.app.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.workflow.ActivityStub;
import io.temporal.workflow.ExternalWorkflowStub;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleWorkflowImpl implements SimpleWorkflow {
    List<String> messageQueue = new ArrayList<>(10);
    String info = "This is a simple Java Workflow";


    @Override
    public String exec() {

        ExternalWorkflowStub externalWorkflowStub = Workflow.newUntypedExternalWorkflowStub("simple-workflow-go");

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
                        .setTaskQueue("simple-queue-go")
                        .build();
        ActivityStub goActivity = Workflow.newUntypedActivityStub(options);
        result += goActivity.execute("GoActivity", String.class, "JavaWorkflow");

        return result;
    }

    @Override
    public void receiveMessage(String message) {
        messageQueue.add(message);
    }

    @Override
    public String getInfo() {
        return info;
    }
}
