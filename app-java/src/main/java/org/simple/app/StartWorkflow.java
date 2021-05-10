package org.simple.app;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.simple.app.workflow.SimpleWorkflow;

public class StartWorkflow {
    public static void main(String[] args) {

        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);

        WorkflowOptions goWorkflowOptions =
                WorkflowOptions.newBuilder().setTaskQueue("simple-queue-java").setWorkflowId("SimpleWorkflowJava").build();

        // Create the workflow client stub. It is used to start the workflow execution.
        SimpleWorkflow workflow = client.newWorkflowStub(SimpleWorkflow.class, goWorkflowOptions);

        System.out.println( workflow.exec() );

        try {
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);

    }
}
