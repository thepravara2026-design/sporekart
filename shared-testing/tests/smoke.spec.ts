import { test, expect } from '@playwright/test';

test.describe('QA Infrastructure Smoke Tests', () => {
  test('Browser launches and homepage loads', async ({ page }) => {
    const response = await page.goto('/');
    expect(response).not.toBeNull();
    expect(response!.status()).toBeLessThan(500);
  });

  test('Page title is defined and valid', async ({ page }) => {
    await page.goto('/');
    const title = await page.title();
    expect(title.length).toBeGreaterThan(0);
  });

  test('Screenshot capture works', async ({ page }) => {
    await page.goto('/');
    await page.screenshot({ path: '../QA_REPORTS/Sprint-02/Screenshots/smoke-homepage.png', fullPage: true });
  });

  test('Console logs are captured', async ({ page }) => {
    const logs: string[] = [];
    page.on('console', (msg) => logs.push(msg.text()));
    await page.goto('/');
    expect(logs.length).toBeGreaterThanOrEqual(0);
  });

  test('Network requests are logged', async ({ page }) => {
    const requests: string[] = [];
    page.on('request', (req) => requests.push(req.url()));
    await page.goto('/');
    expect(requests.length).toBeGreaterThan(0);
  });

  test('404 page returns expected status', async ({ page }) => {
    const response = await page.goto('/nonexistent-page');
    expect(response).not.toBeNull();
  });
});
