<?php

declare(strict_types=1);

namespace Temporal\Samples\Simple;

use Temporal\Workflow\WorkflowInterface;
use Temporal\Workflow\WorkflowMethod;
use Temporal\Workflow\SignalMethod;

#[WorkflowInterface]
interface SimpleWorkflowInterface
{
    /**
     * @param string $name
     * @return string
     */
    #[WorkflowMethod(name: "Simple.exec")]
    public function exec(
        string $name
    );

    #[SignalMethod]
    public function javaMessage(
        string $message
    ): void;

    #[SignalMethod]
    public function goMessage(
        string $message
    ): void;
}