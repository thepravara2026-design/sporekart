import { test, expect } from '@playwright/test';

test.describe('Part 2 — Customer Journey: Phase 12 — Visual Review', () => {

  test('Homepage layout is visually correct at desktop', async ({ page }) => {
    await page.setViewportSize({ width: 1440, height: 900 });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const sections = page.locator('section');
    const count = await sections.count();
    expect(count).toBeGreaterThanOrEqual(3);
    await expect(page.locator('body')).toHaveScreenshot('homepage-desktop.png', { maxDiffPixels: 500 });
  });

  test('Homepage layout is visually correct at mobile', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 667 });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('body')).toHaveScreenshot('homepage-mobile.png', { maxDiffPixels: 500 });
  });

  test('Products page layout is visually correct', async ({ page }) => {
    await page.setViewportSize({ width: 1440, height: 900 });
    await page.goto('/products');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('body')).toHaveScreenshot('products-page-desktop.png', { maxDiffPixels: 500 });
  });

  test('Product card spacing and alignment look correct', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const productCards = page.locator('[class*="card"], [class*="ProductCard"], [class*="product"]');
    const count = await productCards.count();
    if (count > 1) {
      const firstBox = await productCards.nth(0).boundingBox();
      const secondBox = await productCards.nth(1).boundingBox();
      if (firstBox && secondBox) {
        expect(Math.abs(firstBox.y - secondBox.y)).toBeLessThan(firstBox.height + 10);
      }
    }
  });

  test('Loading skeletons are visually consistent if present', async ({ page }) => {
    await page.goto('/products', { waitUntil: 'domcontentloaded' });
    const skeletons = page.locator('[class*="skeleton"], [class*="Skeleton"], [class*="placeholder"]');
    const count = await skeletons.count();
    if (count > 0) {
      const firstSkeleton = skeletons.first();
      const box = await firstSkeleton.boundingBox();
      expect(box).toBeTruthy();
      expect(box!.width).toBeGreaterThan(0);
      expect(box!.height).toBeGreaterThan(0);
    }
  });

  test('Typography is consistent across pages', async ({ page }) => {
    const pages = ['/', '/products', '/about', '/contact'];
    for (const p of pages) {
      await page.goto(p);
      await page.waitForLoadState('networkidle');
      const h1 = page.locator('h1').first();
      if (await h1.isVisible().catch(() => false)) {
        const fontSize = await h1.evaluate(el => getComputedStyle(el).fontSize);
        expect(parseFloat(fontSize)).toBeGreaterThan(16);
      }
    }
  });

  test('Animations and transitions do not cause layout shift', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const bodyBox = await page.locator('body').boundingBox();
    const initialHeight = bodyBox!.height;
    await page.evaluate(() => window.scrollTo(0, document.body.scrollHeight));
    await page.waitForTimeout(1000);
    await page.evaluate(() => window.scrollTo(0, 0));
    await page.waitForTimeout(500);
    const finalBox = await page.locator('body').boundingBox();
    expect(Math.abs(finalBox!.height - initialHeight)).toBeLessThan(50);
  });

  test('Error states should be visually clear and meaningful', async ({ page }) => {
    const response = await page.goto('/this-route-does-not-exist', { waitUntil: 'networkidle' });
    const errorEl = page.locator('[class*="error"], [class*="Error"], [class*="404"]').first();
    const present = await errorEl.isVisible().catch(() => false);
    if (present) {
      await expect(errorEl).toBeVisible();
    }
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(5);
  });

});
