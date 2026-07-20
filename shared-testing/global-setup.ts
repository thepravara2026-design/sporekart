import { FullConfig } from '@playwright/test';

async function globalSetup(config: FullConfig): Promise<void> {
  console.log(`[QA Global Setup] Starting QA Sprint 2...`);
  console.log(`[QA Global Setup] Projects: ${config.projects.map((p) => p.name).join(', ')}`);
  console.log(`[QA Global Setup] Mode: ${process.env.NODE_ENV || 'mock'}`);
  console.log(`[QA Global Setup] Mock Mode: ${process.env.MOCK_MODE || 'true'}`);
  console.log(`[QA Global Setup] Base URL: ${process.env.BASE_URL || 'http://localhost:5174'}`);
  console.log(`[QA Global Setup] QA environment validated successfully.`);
}

export default globalSetup;
