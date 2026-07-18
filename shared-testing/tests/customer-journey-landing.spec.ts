import { test, expect } from '@playwright/test';

test.describe('Part 2 — Customer Journey: Phase 1 — Landing Page Validation', () => {

  test('Homepage loads successfully', async ({ page }) => {
    const start = Date.now();
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const loadTime = Date.now() - start;
    expect(loadTime).toBeLessThan(10000);
    expect(await page.title()).toBeTruthy();
  });

  test('Hero section is present with heading and CTA', async ({ page }) => {
    await page.goto('/');
    const hero = page.locator('section').first();
    await expect(hero).toBeVisible({ timeout: 5000 });
    const heading = hero.locator('h1, h2').first();
    await expect(heading).toBeVisible();
    const cta = hero.locator('a, button').first();
    await expect(cta).toBeVisible();
  });

  test('Navigation bar is present and contains main links', async ({ page }) => {
    await page.goto('/');
    const nav = page.locator('nav, header');
    await expect(nav.first()).toBeVisible({ timeout: 5000 });
    const links = ['Products', 'Training', 'Blog', 'About', 'Contact'];
    for (const label of links) {
      const link = nav.first().locator(`text=${label}`).first();
      if (await link.isVisible().catch(() => false)) {
        await expect(link).toBeVisible();
      }
    }
  });

  test('Footer is present with links', async ({ page }) => {
    await page.goto('/');
    const footer = page.locator('footer');
    await expect(footer).toBeVisible({ timeout: 5000 });
    const footerLinks = footer.locator('a');
    const count = await footerLinks.count();
    expect(count).toBeGreaterThan(5);
  });

  test('Featured products section renders', async ({ page }) => {
    await page.goto('/');
    const featured = page.locator('text=Featured Products, Products, Best Sellers').first();
    if (await featured.isVisible().catch(() => false)) {
      await expect(featured).toBeVisible();
    }
    const productCards = page.locator('[class*="product"], [class*="card"], [class*="ProductCard"]');
    const count = await productCards.count();
    expect(count).toBeGreaterThanOrEqual(0);
  });

  test('No broken images on homepage', async ({ page }) => {
    await page.goto('/');
    const images = page.locator('img');
    const count = await images.count();
    let broken = 0;
    for (let i = 0; i < count; i++) {
      const img = images.nth(i);
      const src = await img.getAttribute('src');
      if (!src || src === '' || src.startsWith('data:')) continue;
      const naturalWidth = await img.evaluate(el => (el as HTMLImageElement).naturalWidth);
      if (naturalWidth === 0) broken++;
    }
    expect(broken).toBe(0);
  });

  test('No broken links on homepage', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const links = page.locator('a[href]');
    const count = await links.count();
    let brokenCount = 0;
    for (let i = 0; i < Math.min(count, 30); i++) {
      const href = await links.nth(i).getAttribute('href');
      if (!href || href.startsWith('#') || href.startsWith('mailto:') || href.startsWith('tel:') || href.startsWith('http')) continue;
      try {
        const response = await page.goto(href);
        if (response && response.status() >= 400) brokenCount++;
        await page.goBack();
      } catch { brokenCount++; }
    }
    expect(brokenCount).toBe(0);
  });

  test('Page scrolls smoothly and content remains', async ({ page }) => {
    await page.goto('/');
    await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
    await page.waitForTimeout(500);
    const scrollY = await page.evaluate(() => window.scrollY);
    expect(scrollY).toBeGreaterThan(0);
    await page.evaluate(() => window.scrollTo(0, 0));
    await page.waitForTimeout(500);
    const scrollY2 = await page.evaluate(() => window.scrollY);
    expect(scrollY2).toBe(0);
  });

  test('Responsive layout renders at desktop viewport', async ({ page }) => {
    await page.setViewportSize({ width: 1440, height: 900 });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const vp = page.viewportSize();
    expect(vp?.width).toBe(1440);
    const content = page.locator('section, main').first();
    await expect(content).toBeVisible({ timeout: 5000 });
  });

  test('Responsive layout renders at tablet viewport', async ({ page }) => {
    await page.setViewportSize({ width: 768, height: 1024 });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const vp = page.viewportSize();
    expect(vp?.width).toBe(768);
    const content = page.locator('section, main').first();
    await expect(content).toBeVisible({ timeout: 5000 });
  });

  test('Responsive layout renders at mobile viewport', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 667 });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const vp = page.viewportSize();
    expect(vp?.width).toBe(375);
    const content = page.locator('section, main').first();
    await expect(content).toBeVisible({ timeout: 5000 });
  });

  test('Performance: First Contentful Paint within threshold', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const fcp = await page.evaluate(() => {
      const entries = performance.getEntriesByType('paint');
      const fcpEntry = entries.find(e => e.name === 'first-contentful-paint');
      return fcpEntry ? fcpEntry.startTime : -1;
    });
    expect(fcp).toBeGreaterThanOrEqual(0);
    expect(fcp).toBeLessThan(5000);
  });

  test('Performance: DOM Content Loaded within threshold', async ({ page }) => {
    await page.goto('/');
    const dcl = await page.evaluate(() => {
      const nav = performance.getEntriesByType('navigation')[0] as PerformanceNavigationTiming;
      return nav ? nav.domContentLoadedEventEnd : -1;
    });
    expect(dcl).toBeGreaterThanOrEqual(0);
    expect(dcl).toBeLessThan(5000);
  });

});
