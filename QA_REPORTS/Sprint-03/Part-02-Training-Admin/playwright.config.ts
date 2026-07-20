import { defineConfig, devices } from '../../../shared-testing/node_modules/@playwright/test';

const reportDir = process.env.PLAYWRIGHT_REPORT_DIR || 'Sprint-03/Part-02-Training-Admin';

export default defineConfig({
  testDir: '.',
  fullyParallel: false,
  forbidOnly: !!process.env.CI,
  retries: 0,
  workers: 1,
  reporter: [
    ['html', { outputFolder: `../../${reportDir}/html-report` }],
    ['json', { outputFile: `../../${reportDir}/results.json` }],
    ['junit', { outputFile: `../../${reportDir}/junit.xml` }],
    ['list'],
  ],
  use: {
    baseURL: process.env.BASE_URL || 'http://localhost:4173',
    trace: { mode: 'on', snapshots: true, screenshots: true },
    video: 'on',
    screenshot: 'on',
    actionTimeout: 15000,
    navigationTimeout: 30000,
  },
  projects: [
    { name: 'chromium', use: { ...devices['Desktop Chrome'] } },
  ],
});
