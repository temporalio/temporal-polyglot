package simple

import (
	"strconv"

	"go.temporal.io/sdk/workflow"
)

const (
	SimpleSignalName = "simplesignal"
)

// Workflow is a Hello World workflow definition.
func Workflow(ctx workflow.Context, name string) (string, error) {
	workflowID := workflow.GetInfo(ctx).WorkflowExecution.ID
	logger := workflow.GetLogger(ctx)
	logger.Info("Simple Workflow started with ID.",
		"ID", workflowID)

	var sigValues string
	simpleCh := workflow.GetSignalChannel(ctx, SimpleSignalName)
	// Receive 10 signals from Java workflow
	for i := 0; i < 10; i++ {
		var mysignal string
		simpleCh.Receive(ctx, &mysignal)
		logger.Info("Signal received.", "Signal", mysignal)
		sigValues += mysignal + "\n"
	}
	// Send 10 signals to Java workflow
	for i := 0; i < 10; i++ {
		workflow.SignalExternalWorkflow(ctx, "SimpleWorkflowJava", "", "fromgo", "Hello from Go workflow: "+strconv.Itoa(i)).Get(ctx, nil)
	}

	logger.Info("Simple Workflow ended with results.",
		"result", sigValues)

	return sigValues, nil
}
