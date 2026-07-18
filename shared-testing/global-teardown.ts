import { FullConfig } from '@playwright/test';

async function globalTeardown(config: FullConfig): Promise<void> {
  console.log(`[QA Global Teardown] QA Sprint 2 session completed.`);
  console.log(`[QA Global Teardown] Projects executed: ${config.projects.map((p) => p.name).join(', ')}`);
}

export default globalTeardown;
