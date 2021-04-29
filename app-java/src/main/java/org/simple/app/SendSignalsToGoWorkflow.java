package org.simple.app;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.workflow.ExternalWorkflowStub;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

public class SendSignalsToGoWorkflow {
    static final String TASK_QUEUE = "simple-queue";
    static final String WORKFLOW_ID = "SimpleWorkflowJava";

    @WorkflowInterface
    public interface SimpleWorkflow {
        @WorkflowMethod
        void exec();
    }

    public static class SimpleWorkflowImpl implements SimpleWorkflow {
        @Override
        public void exec() {
            ExternalWorkflowStub externalWorkflowStub = Workflow.newUntypedExternalWorkflowStub("simple_workflow-go");

            for(int i=0; i < 10; i++) {
                externalWorkflowStub.signal("simplesignal", "Hello from Java Workflow: " + i);
            }
        }
    }

    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker(TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(SimpleWorkflowImpl.class);
        factory.start();

        WorkflowOptions workflowOptions =
                WorkflowOptions.newBuilder().setTaskQueue(TASK_QUEUE).setWorkflowId(WORKFLOW_ID).build();

        // Create the workflow client stub. It is used to start the workflow execution.
        SimpleWorkflow workflow = client.newWorkflowStub(SimpleWorkflow.class, workflowOptions);

        workflow.exec();

        System.exit(0);
    }

}
