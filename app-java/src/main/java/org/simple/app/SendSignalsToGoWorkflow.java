package org.simple.app;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.workflow.*;

import java.util.ArrayList;
import java.util.List;

public class SendSignalsToGoWorkflow {
    static final String TASK_QUEUE = "simple-queue";
    static final String WORKFLOW_ID = "SimpleWorkflowJava";

    @WorkflowInterface
    public interface SimpleWorkflow {
        @WorkflowMethod
        String exec();

        @SignalMethod(name = "fromgo")
        void receiveMessage(String message);
    }

    public static class SimpleWorkflowImpl implements SimpleWorkflow {
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

        System.out.println( workflow.exec() );

        System.exit(0);
    }

}
