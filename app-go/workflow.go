package simple

import (
	"fmt"
	"strconv"
	"time"

	"go.temporal.io/sdk/workflow"
)

const (
	SimpleSignalName = "simplesignal"
)

// Workflow is a Hello World workflow definition.
func Workflow(ctx workflow.Context, name string) (string, error) {

	ao := workflow.ActivityOptions{
		StartToCloseTimeout: 1 * time.Minute,
	}

	ctx = workflow.WithActivityOptions(ctx, ao)

	workflowID := workflow.GetInfo(ctx).WorkflowExecution.ID
	logger := workflow.GetLogger(ctx)
	logger.Info("Simple Workflow started with ID.",
		"ID", workflowID)

	var retValues string
	retValues = "\n"
	simpleCh := workflow.GetSignalChannel(ctx, SimpleSignalName)

	// Receive 10 signals from Java workflow
	for i := 0; i < 10; i++ {
		var mysignal string
		simpleCh.Receive(ctx, &mysignal)
		logger.Info("Signal received.", "Signal", mysignal)
		retValues += mysignal + "\n"
	}

	// Send 10 signals to Java workflow
	for i := 0; i < 10; i++ {
		workflow.SignalExternalWorkflow(ctx, "SimpleWorkflowJava", "", "receiveMessage", "Hello from Go workflow: "+strconv.Itoa(i)).Get(ctx, nil)
	}

	// Call Java activity
	var result string
	err := workflow.ExecuteActivity(ctx, "javaSayHello", "GoWorkflow").Get(ctx, &result)
	logger.Info(fmt.Sprintf("Java Activity returns %v, %v", result, err))
	retValues += result + "\n"

	logger.Info("Simple Workflow ended with results.",
		"result", retValues)

	return retValues, nil
}
