package simple

import (
	"context"

	"go.temporal.io/sdk/activity"
	"go.temporal.io/sdk/client"
)

func GoActivity(ctx context.Context, from string) (string, error) {
	logger := activity.GetLogger(ctx)
	logger.Info("Go Activity", "from", from)

	// Query Java Workflow
	c, err := client.NewClient(client.Options{})
	if err != nil {
		logger.Info("Unable to create client", err)
	}
	resp, err := c.QueryWorkflow(context.Background(), "SimpleWorkflowJava", "", "queryJavaInfo")
	var qr string
	resp.Get(&qr)

	return "Go Activity - hello from: " + from + " Query result: " + qr, nil
}
