## Temporal Simple Polyglot example

This demo shows:
* How to signal a Go Workflow from a Java workflow (and vice versa)
* How to invoke an Activity written in Go from a Workflow written in Java (and vice versa)
* How to query a Go Workflow from a Java Activity (and vice versa)

### Running the demo

1) Start the Temporal Server:
```shell script
git clone https://github.com/temporalio/docker-compose.git
cd  docker-compose
docker compose up
```

2) Start the Go worker and starter:
```shell script
cd app-go
go run worker/main.go
go run starter/main.go
```

3) Start the Java worker and starter:
```shell script
cd app-java
mvn compile exec:java -Dexec.mainClass="org.simple.app.StartWorker"
mvn compile exec:java -Dexec.mainClass="org.simple.app.StartWorkflow"
```

### Seeing the results:
1) Look at the logs printed in the same window where you ran the Go workflow starter, you should ge:

```shell script
Workflow result: Hello from Java Workflow: 0
Hello from Java Workflow: 1
Hello from Java Workflow: 2
Hello from Java Workflow: 3
Hello from Java Workflow: 4
Hello from Java Workflow: 5
Hello from Java Workflow: 6
Hello from Java Workflow: 7
Hello from Java Workflow: 8
Hello from Java Workflow: 9
Java SimpleActivity - hello from: GoWorkflow Query result: This is a simple Go Workflow
```

2) Look at the results of our Java workflow, you should get:
```shell script
Hello from Go workflow: 0
Hello from Go workflow: 1
Hello from Go workflow: 2
Hello from Go workflow: 3
Hello from Go workflow: 4
Hello from Go workflow: 5
Hello from Go workflow: 6
Hello from Go workflow: 7
Hello from Go workflow: 8
Hello from Go workflow: 9
Go Activity - hello from: JavaWorkflow Query result: This is a simple Java Workflow
```
