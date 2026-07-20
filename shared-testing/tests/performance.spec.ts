import { test, expect } from '@playwright/test';

test.describe('Part 9 — Performance Baseline', () => {

  test('Page loads complete within performance threshold', async ({ page }) => {
    const startTime = Date.now();
    await page.goto('/login');
    await page.waitForLoadState('load');
    const loadTime = Date.now() - startTime;

    // Verify page loads fully in under 2.5 seconds (standard performance budget)
    expect(loadTime).toBeLessThan(2500);

    // Collect performance entries
    const performanceTiming = await page.evaluate(() => {
      const [entry] = performance.getEntriesByType('navigation') as PerformanceNavigationTiming[];
      return {
        domContentLoaded: entry ? entry.domContentLoadedEventEnd : 0,
        loadEnd: entry ? entry.loadEventEnd : 0,
      };
    });

    expect(performanceTiming.domContentLoaded).toBeLessThan(2000);
  });

  test('No failed network requests or broken assets', async ({ page }) => {
    const failedRequests: string[] = [];

    // Track request failures
    page.on('requestfailed', (request) => {
      failedRequests.push(`${request.url()} - ${request.failure()?.errorText}`);
    });

    // Track non-ok HTTP responses
    page.on('response', (response) => {
      const status = response.status();
      if (status >= 400 && status < 600) {
        failedRequests.push(`${response.url()} - HTTP Status ${status}`);
      }
    });

    await page.goto('/login');
    await page.waitForLoadState('networkidle');

    // Ensure all critical scripts and stylesheets load successfully
    expect(failedRequests.length).toBe(0);
  });
});
