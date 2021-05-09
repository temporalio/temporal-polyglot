package org.simple.app.workflow;

import io.temporal.workflow.ExternalWorkflowStub;
import io.temporal.workflow.Workflow;

import java.util.ArrayList;
import java.util.List;

public class SimpleWorkflowImpl implements SimpleWorkflow {
    List<String> messageQueue = new ArrayList<>(10);

    @Override
    public String exec() {
        ExternalWorkflowStub externalWorkflowStub = Workflow.newUntypedExternalWorkflowStub("simple_workflow-go");

        // Send 10 signals to Go workflow
        for(int i=0; i < 10; i++) {
            externalWorkflowStub.signal("simplesignal", "Hello from Java Workflow: " + i);
        }

        // Receive 10 signals from Go workflow
        Workflow.await(() -> messageQueue.size() == 10);

        String result = "";
        for(String m : messageQueue) {
            result += m + "\n";
        }

        return result;
    }

    @Override
    public void receiveMessage(String message) {
        messageQueue.add(message);
    }
}
