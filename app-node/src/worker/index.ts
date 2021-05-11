import { Worker } from '@temporalio/worker';

async function run() {
  // Automatically locate and register Activities and Workflows relative to __dirname
  // (assuming package was bootstrapped with `npm init @temporalio`).
  // Worker connects to localhost by default and uses console error for logging.
  // Customize the Worker by passing more options to create().
  // create() tries to connect to the server and will throw if a connection could not be established.
  const worker = await Worker.create({ workDir: __dirname, 
    taskQueue: 'simple-queue-node',
    });
  // Start accepting tasks on the `simple-queue-go` queue
  await worker.run();
}

run().catch((err) => {
  console.error(err);
  process.exit(1);
});
