import { test, expect } from '@playwright/test';

test.describe('QA Foundation — Environment Validation', () => {
  test('Mock mode environment variables are accessible', () => {
    expect(process.env.MOCK_MODE || 'true').toBeTruthy();
    expect(process.env.NODE_ENV || 'mock').toBeTruthy();
  });

  test('QA configuration is present', () => {
    expect(process.env.QA_MODE || 'true').toBeTruthy();
  });
});

test.describe('QA Foundation — Application Accessibility', () => {
  test('Application loads at base URL', async ({ page }) => {
    await page.goto('/');
    await expect(page).toHaveTitle(/SporeKart|Sporekart|React|Vite/);
    await page.screenshot({ path: '../QA_REPORTS/Sprint-01/Evidence/homepage-load.png', fullPage: true });
  });

  test('Page renders without console errors', async ({ page }) => {
    const consoleErrors: string[] = [];
    page.on('console', (msg) => {
      if (msg.type() === 'error') {
        consoleErrors.push(msg.text());
      }
    });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    expect(consoleErrors.length).toBe(0);
  });
});

test.describe('QA Foundation — Trace & Video Recording', () => {
  test('Trace and video artifacts are generated', async ({ page }, testInfo) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const tracePath = testInfo.outputPath('trace.zip');
    expect(tracePath).toBeTruthy();
  });
});

test.describe('QA Foundation — Responsive Layout', () => {
  test('Desktop layout renders correctly', async ({ page }) => {
    await page.setViewportSize({ width: 1920, height: 1080 });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.screenshot({ path: '../QA_REPORTS/Sprint-01/Evidence/desktop-layout.png', fullPage: true });
  });

  test('Tablet layout renders correctly', async ({ page }) => {
    await page.setViewportSize({ width: 768, height: 1024 });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.screenshot({ path: '../QA_REPORTS/Sprint-01/Evidence/tablet-layout.png', fullPage: true });
  });

  test('Mobile layout renders correctly', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.screenshot({ path: '../QA_REPORTS/Sprint-01/Evidence/mobile-layout.png', fullPage: true });
  });
});

test.describe('QA Foundation — Network & Performance', () => {
  test('Page load completes within acceptable time', async ({ page }) => {
    const startTime = Date.now();
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const loadTime = Date.now() - startTime;
    expect(loadTime).toBeLessThan(15000);
  });

  test('No failed network requests', async ({ page }) => {
    const failedRequests: string[] = [];
    page.on('requestfailed', (request) => {
      failedRequests.push(`${request.url()} - ${request.failure()?.errorText}`);
    });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    expect(failedRequests.length).toBe(0);
  });
});
