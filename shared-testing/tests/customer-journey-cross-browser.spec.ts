import { test, expect } from '@playwright/test';

test.describe('Part 2 — Customer Journey: Phase 10 — Cross-Browser Validation', () => {

  test('Homepage loads on all viewport sizes', async ({ page }) => {
    const viewports = [
      { width: 1440, height: 900, label: 'desktop' },
      { width: 1024, height: 768, label: 'laptop' },
      { width: 768, height: 1024, label: 'tablet' },
      { width: 375, height: 667, label: 'mobile' },
    ];
    for (const vp of viewports) {
      await page.setViewportSize({ width: vp.width, height: vp.height });
      const response = await page.goto('/');
      expect(response?.status()).toBeLessThan(400);
      await page.waitForLoadState('networkidle');
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(20);
    }
  });

  test('Navigation bar adapts to all viewport sizes', async ({ page }) => {
    await page.setViewportSize({ width: 1440, height: 900 });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const navDesktop = page.locator('nav, header').first();
    await expect(navDesktop).toBeVisible();

    await page.setViewportSize({ width: 375, height: 667 });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const navMobile = page.locator('nav, header, [class*="mobile-menu"], [class*="drawer"]').first();
    await expect(navMobile).toBeVisible();
  });

  test('Products page renders consistently across viewports', async ({ page }) => {
    await page.setViewportSize({ width: 1440, height: 900 });
    let response = await page.goto('/products');
    expect(response?.status()).toBeLessThan(400);

    await page.setViewportSize({ width: 768, height: 1024 });
    response = await page.goto('/products');
    expect(response?.status()).toBeLessThan(400);

    await page.setViewportSize({ width: 375, height: 667 });
    response = await page.goto('/products');
    expect(response?.status()).toBeLessThan(400);
  });

  test('Key pages render correctly across all viewports', async ({ page }) => {
    const routes = ['/', '/products', '/about', '/contact', '/training', '/blog'];
    const viewports = [
      { width: 1440, height: 900 },
      { width: 768, height: 1024 },
      { width: 375, height: 667 },
    ];
    for (const route of routes) {
      for (const vp of viewports) {
        await page.setViewportSize({ width: vp.width, height: vp.height });
        const response = await page.goto(route);
        expect(response?.status()).toBeLessThan(400);
        await page.waitForLoadState('networkidle');
      }
    }
  });

  test('Responsive images load correctly on all viewports', async ({ page }) => {
    const viewports = [375, 768, 1024, 1440];
    for (const width of viewports) {
      await page.setViewportSize({ width, height: 900 });
      await page.goto('/');
      await page.waitForLoadState('networkidle');
      const images = page.locator('img');
      const count = await images.count();
      let broken = 0;
      for (let i = 0; i < Math.min(count, 10); i++) {
        const src = await images.nth(i).getAttribute('src');
        if (src && !src.startsWith('data:')) {
          const nw = await images.nth(i).evaluate(el => (el as HTMLImageElement).naturalWidth);
          if (nw === 0) broken++;
        }
      }
      expect(broken).toBe(0);
    }
  });

  test('Horizontal scroll is not present on any page', async ({ page }) => {
    const routes = ['/', '/products', '/about', '/contact', '/blog', '/training', '/search'];
    for (const route of routes) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const hasHorizontalScroll = await page.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth);
      expect(hasHorizontalScroll).toBe(false);
    }
  });

});
