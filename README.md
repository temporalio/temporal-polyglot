## Temporal Simple Polyglot example

This demo shows a Java workflow signalling a Go workflow

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

3) Start the Java workflow:
```shell script
cd app-java
mvn compile exec:java -Dexec.mainClass="org.simple.app.SendSignalsToGoWorkflow"
```

### Seeing the results:
Look at the logs printed in the same window where you ran the Go workflow starter, you should ge:

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
```