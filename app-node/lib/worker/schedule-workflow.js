"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
// @@@SNIPSTART nodejs-hello-client
const client_1 = require("@temporalio/client");
async function run() {
    // Connect to localhost and use the "default" namespace
    const connection = new client_1.Connection();
    // Create a typed client for the workflow defined above
    const example = connection.workflow('example', { taskQueue: 'tutorial' });
    const result = await example.start('Temporal');
    console.log(result); // Hello, Temporal
}
run().catch((err) => {
    console.error(err);
    process.exit(1);
});
// @@@SNIPEND
//# sourceMappingURL=schedule-workflow.js.map