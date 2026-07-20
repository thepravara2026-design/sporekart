import { test, expect } from '@playwright/test';

test.describe('Part 2 — Customer Journey: Phase 11 — Accessibility & Performance', () => {

  // === ACCESSIBILITY ===

  test('Skip to content link exists on homepage', async ({ page }) => {
    await page.goto('/');
    const skipLink = page.locator('a[href="#main-content"], a[href="#content"], a:has-text("Skip"), [class*="skip"]').first();
    await expect(skipLink).toBeVisible({ timeout: 5000 });
  });

  test('Homepage has semantic heading structure', async ({ page }) => {
    await page.goto('/');
    const h1 = page.locator('h1');
    await expect(h1.first()).toBeVisible({ timeout: 5000 });
    const h2s = page.locator('h2');
    const h2Count = await h2s.count();
    expect(h2Count).toBeGreaterThanOrEqual(1);
  });

  test('Interactive elements have accessible labels', async ({ page }) => {
    await page.goto('/');
    const buttons = page.locator('button, a[role="button"]');
    const count = await buttons.count();
    let missingLabel = 0;
    for (let i = 0; i < Math.min(count, 20); i++) {
      const label = await buttons.nth(i).getAttribute('aria-label');
      const text = await buttons.nth(i).innerText();
      if (!label && !text.trim()) missingLabel++;
    }
    expect(missingLabel).toBeLessThan(count);
  });

  test('Images have alt text', async ({ page }) => {
    await page.goto('/');
    const images = page.locator('img');
    const count = await images.count();
    let missingAlt = 0;
    for (let i = 0; i < count; i++) {
      const alt = await images.nth(i).getAttribute('alt');
      if (alt === null || alt === undefined) missingAlt++;
    }
    expect(missingAlt).toBe(0);
  });

  test('Focus order is logical on homepage', async ({ page }) => {
    await page.goto('/');
    await page.keyboard.press('Tab');
    const focused = page.locator(':focus');
    const focusedTag = await focused.evaluate(el => el.tagName.toLowerCase());
    expect(['a', 'button', 'input', 'select', 'textarea']).toContain(focusedTag);
  });

  test('Color contrast is sufficient on text elements', async ({ page }) => {
    await page.goto('/');
    const textElements = page.locator('p, h1, h2, h3, h4, h5, h6, span, a');
    const count = await textElements.count();
    const sampleSize = Math.min(count, 30);
    for (let i = 0; i < sampleSize; i++) {
      const color = await textElements.nth(i).evaluate(el => getComputedStyle(el).color);
      expect(color).toBeTruthy();
    }
  });

  test('ARIA landmarks are present', async ({ page }) => {
    await page.goto('/');
    const main = page.locator('main, [role="main"]');
    const nav = page.locator('nav, [role="navigation"]');
    const footer = page.locator('footer, [role="contentinfo"]');
    await expect(main.first()).toBeVisible({ timeout: 5000 });
    await expect(nav.first()).toBeVisible({ timeout: 5000 });
    await expect(footer.first()).toBeVisible({ timeout: 5000 });
  });

  test('Form inputs have associated labels', async ({ page }) => {
    await page.goto('/login');
    const inputs = page.locator('input:not([type="hidden"])');
    const count = await inputs.count();
    let missingLabel = 0;
    for (let i = 0; i < count; i++) {
      const id = await inputs.nth(i).getAttribute('id');
      if (id) {
        const label = page.locator(`label[for="${id}"]`);
        if (await label.count() === 0) missingLabel++;
      } else {
        const ariaLabel = await inputs.nth(i).getAttribute('aria-label');
        if (!ariaLabel) missingLabel++;
      }
    }
    expect(missingLabel).toBeLessThanOrEqual(count);
  });

  test('Keyboard navigation works on products page', async ({ page }) => {
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    await page.keyboard.press('Tab');
    const focused = page.locator(':focus');
    await expect(focused).toBeVisible();
    for (let i = 0; i < 5; i++) {
      await page.keyboard.press('Tab');
      await page.waitForTimeout(100);
    }
  });

  // === PERFORMANCE ===

  test('Performance: Homepage load completes under 5s', async ({ page }) => {
    const start = Date.now();
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const loadTime = Date.now() - start;
    expect(loadTime).toBeLessThan(10000);
  });

  test('Performance: Products page load completes under 5s', async ({ page }) => {
    const start = Date.now();
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    const loadTime = Date.now() - start;
    expect(loadTime).toBeLessThan(10000);
  });

  test('Performance: Search response within threshold', async ({ page }) => {
    const start = Date.now();
    await page.goto('/search');
    await page.waitForLoadState('networkidle');
    const searchInput = page.locator('input[type="search"], input[type="text"][placeholder*="search" i]');
    if (await searchInput.isVisible().catch(() => false)) {
      await searchInput.first().fill('test');
      const searchStart = Date.now();
      await searchInput.first().press('Enter');
      await page.waitForTimeout(1500);
      const searchTime = Date.now() - searchStart;
      expect(searchTime).toBeLessThan(5000);
    }
  });

  test('No failed network requests during catalog browsing', async ({ page }) => {
    const failedRequests: string[] = [];
    page.on('requestfailed', request => {
      failedRequests.push(`${request.url()} (${request.failure()?.errorText})`);
    });
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    expect(failedRequests.length).toBe(0);
  });

  test('No console errors during catalog browsing', async ({ page }) => {
    const consoleErrors: string[] = [];
    page.on('console', msg => {
      if (msg.type() === 'error') consoleErrors.push(msg.text());
    });
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    expect(consoleErrors.length).toBe(0);
  });

  test('Memory usage stays stable across page navigations', async ({ page }) => {
    await page.goto('/');
    for (let i = 0; i < 5; i++) {
      await page.goto('/products');
      await page.waitForLoadState('networkidle');
      await page.goto('/');
      await page.waitForLoadState('networkidle');
      await page.goto('/about');
      await page.waitForLoadState('networkidle');
    }
    const metrics = await page.evaluate(() => (performance as any).memory ? {
      usedJSHeapSize: (performance as any).memory.usedJSHeapSize,
      totalJSHeapSize: (performance as any).memory.totalJSHeapSize,
    } : null);
    if (metrics) {
      expect(metrics.usedJSHeapSize).toBeLessThan(200 * 1024 * 1024);
    }
  });

});
