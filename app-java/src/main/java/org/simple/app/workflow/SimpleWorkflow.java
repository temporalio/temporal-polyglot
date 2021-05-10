package org.simple.app.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface SimpleWorkflow {
    @WorkflowMethod
    String exec();

    @SignalMethod
    void receiveMessage(String message);

    @QueryMethod(name = "queryJavaInfo")
    String getInfo();
}
