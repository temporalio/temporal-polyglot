import { greet } from '@activities/greeter';
// A workflow that simply calls an activity
async function main(name) {
    return greet(name);
}
// Declare the workflow's type to be checked by the Typescript compiler
export const workflow = { main };
// @@@SNIPEND
//# sourceMappingURL=example.js.map