import { Workflow } from '@temporalio/workflow';
export interface Example extends Workflow {
    main(name: string): Promise<string>;
}
//# sourceMappingURL=workflows.d.ts.map