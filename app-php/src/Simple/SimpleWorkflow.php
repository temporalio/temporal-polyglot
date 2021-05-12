<?php

declare(strict_types=1);

namespace Temporal\Samples\Simple;

use Carbon\CarbonInterval;
use Temporal\Activity\ActivityOptions;
use Temporal\Common\RetryOptions;
use Temporal\Workflow;
use Temporal\Exception\Failure;

class SimpleWorkflow implements SimpleWorkflowInterface
{

    private array $inputJava = array();
    private array $inputGo = array();
    private string $results = "";
    private int $countJava = 0;
    private int $countGo = 0;

    private string $stringType;

    public function exec(string $name)
    {
        
        // Wait for Java signals
        yield Workflow::await(fn() => $this->countJava == 10);
        foreach ($this->inputJava as $value) {
           $this->results .= $value . "\n";
       }

        // Wait for Go signals
        yield Workflow::await(fn() => $this->countGo == 10);
        foreach ($this->inputGo as $value) {
           $this->results .= $value . "\n";
        }

        // Call Node Activity and add error
        try {
            
            $options = ActivityOptions::new()
            ->withTaskQueue('simple-queue-node')
            ->withScheduleToCloseTimeout(CarbonInterval::seconds(10))
            ->withRetryOptions(
                RetryOptions::new()->withMaximumAttempts(1)
            );

            yield Workflow::executeActivity('["@activities/nodeactivity", "nodeActivity"]', ["From PHP Workflow"], $options);
        } catch(Failure\ActivityFailure $e) {
            $cause = $e->getPrevious();
            $this->results .= "Error from Node Activity: " . $cause->getOriginalMessage() . "\n";
        }
    
        
        return $this->results;
    }

    public function javaMessage(string $message): void
    {
        array_push($this->inputJava, $message);
        $this->countJava++;
    }

    public function goMessage(string $message): void
    {
        array_push($this->inputGo, $message);
        $this->countGo++;
    }
}