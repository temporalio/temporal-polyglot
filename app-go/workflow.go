package simple

import (
	"fmt"
	"log"
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
		StartToCloseTimeout: 2 * time.Minute,
	}

	ctx = workflow.WithActivityOptions(ctx, ao)

	workflowID := workflow.GetInfo(ctx).WorkflowExecution.ID
	logger := workflow.GetLogger(ctx)
	logger.Info("Simple Workflow started with ID.",
		"ID", workflowID)

	var retValues string
	retValues = "\n"

	// Setup signal Channel
	simpleCh := workflow.GetSignalChannel(ctx, SimpleSignalName)

	// Setup Query Handler
	queryResult := "This is a simple Go Workflow"
	err := workflow.SetQueryHandler(ctx, "queryGoInfo", func(input []byte) (string, error) {
		return queryResult, nil
	})
		log.Fatalln("SetQueryHandler failed", err.Error())
	}

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
	aoj := workflow.ActivityOptions{
		TaskQueue:           "simple-queue-java",
		StartToCloseTimeout: 5 * time.Second,
	}
	ctx = workflow.WithActivityOptions(ctx, aoj)
	err = workflow.ExecuteActivity(ctx, "SayHello", "GoWorkflow").Get(ctx, &result)
	logger.Info(fmt.Sprintf("Java Activity returns %v, %v", result, err))
	retValues += result + "\n"

	logger.Info("Simple Workflow ended with results.",
		"result", retValues)

	return retValues, nil
}
