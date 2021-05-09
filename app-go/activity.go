package simple

import (
	"context"

	"go.temporal.io/sdk/activity"
)

func GoActivity(ctx context.Context, from string) (string, error) {
	logger := activity.GetLogger(ctx)
	logger.Info("Go Activity", "from", from)
	return "Go Activity - hello from: " + from, nil
}
