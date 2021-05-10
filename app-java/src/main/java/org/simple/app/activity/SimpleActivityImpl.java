package org.simple.app.activity;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowStub;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.util.Optional;

public class SimpleActivityImpl implements SimpleActivity {
    @Override
    public String sayHello(String from) {

        // Query Go Workflow
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkflowStub goWorkflowStub = client.newUntypedWorkflowStub("simple-workflow-go", Optional.empty(), Optional.empty());
        String queryResult = goWorkflowStub.query("queryGoInfo", String.class);


        return "Java SimpleActivity - hello from: " + from + " Query result: " + queryResult;
    }
}
