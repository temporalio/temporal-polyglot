<?php

declare(strict_types=1);

namespace Temporal\Samples\Simple;

use Carbon\CarbonInterval;
use Temporal\Activity\ActivityOptions;
use Temporal\Workflow;

class SimpleWorkflow implements SimpleWorkflowInterface
{

    private array $inputJava = array();
    private array $inputGo = array();
    private string $results = "";
    private int $countJava = 0;
    private int $countGo = 0;

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