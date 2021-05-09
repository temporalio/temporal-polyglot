package main

import (
	"log"

	"go.temporal.io/sdk/client"
	"go.temporal.io/sdk/worker"

	"simple.workflow/v1"
)

func main() {
	// The client and worker are heavyweight objects that should be created once per process.
	c, err := client.NewClient(client.Options{})
	if err != nil {
		log.Fatalln("Unable to create client", err)
	}
	defer c.Close()

	w := worker.New(c, "simple-queue", worker.Options{})

	// register go workflow
	w.RegisterWorkflow(simple.Workflow)

	// register go activity
	w.RegisterActivity(simple.GoActivity)

	err = w.Run(worker.InterruptCh())
	if err != nil {
		log.Fatalln("Unable to start worker", err)
	}
}
