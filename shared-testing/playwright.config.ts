import { defineConfig, devices } from '@playwright/test';

const reportDir = process.env.PLAYWRIGHT_REPORT_DIR || 'Sprint-01/Playwright';

export default defineConfig({
  testDir: './tests',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 1,
  workers: process.env.CI ? 1 : undefined,
  reporter: [
    ['html', { outputFolder: `../QA_REPORTS/${reportDir}/html-report` }],
    ['json', { outputFile: `../QA_REPORTS/${reportDir}/results.json` }],
    ['junit', { outputFile: `../QA_REPORTS/${reportDir}/junit.xml` }],
    ['list'],
  ],
  use: {
    baseURL: process.env.BASE_URL || 'http://localhost:5173',
    trace: {
      mode: 'on',
      snapshots: true,
      screenshots: true,
    },
    video: 'on',
    screenshot: 'on',
    actionTimeout: 15000,
    navigationTimeout: 30000,
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
    {
      name: 'firefox',
      use: { ...devices['Desktop Firefox'] },
    },
    {
      name: 'webkit',
      use: { ...devices['Desktop Safari'] },
    },
    {
      name: 'mobile-chrome',
      use: { ...devices['Pixel 5'] },
    },
    {
      name: 'mobile-safari',
      use: { ...devices['iPhone 13'] },
    },
    {
      name: 'tablet',
      use: { ...devices['iPad (gen 7)'] },
    },
  ],
});
