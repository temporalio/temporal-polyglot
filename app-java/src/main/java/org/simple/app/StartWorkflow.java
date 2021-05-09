package org.simple.app;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.simple.app.workflow.SimpleWorkflow;

public class StartWorkflow {
    public static void main(String[] args) {

        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);


        WorkflowOptions workflowOptions =
                WorkflowOptions.newBuilder().setTaskQueue("simple-queue").setWorkflowId("SimpleWorkflowJava").build();

        // Create the workflow client stub. It is used to start the workflow execution.
        SimpleWorkflow workflow = client.newWorkflowStub(SimpleWorkflow.class, workflowOptions);

        System.out.println( workflow.exec() );

        System.exit(0);
    }
}
