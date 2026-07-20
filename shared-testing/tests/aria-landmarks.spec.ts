import { test, expect } from '@playwright/test';
import AxeBuilder from '@axe-core/playwright';

test.describe('ARIA Landmarks — Sprint C-003', () => {
  const ROUTES = ['/', '/dashboard', '/login', '/products'];

  for (const route of ROUTES) {
    test(`Page "${route}" has required ARIA landmarks`, async ({ page }) => {
      await page.goto(route);
      await page.waitForLoadState('networkidle');

      const landmarks = await page.evaluate(() => {
        const result: string[] = [];
        const selectors = [
          'header[role="banner"], header:not([role])',
          'nav[role="navigation"], nav:not([role])',
          'main[role="main"], main:not([role])',
          'footer[role="contentinfo"], footer:not([role])',
          '[role="banner"]',
          '[role="navigation"]',
          '[role="main"]',
          '[role="contentinfo"]',
        ];
        selectors.forEach((sel) => {
          const els = document.querySelectorAll(sel);
          els.forEach((el) => {
            const tag = el.tagName.toLowerCase();
            const role = el.getAttribute('role') || '(implicit)';
            const label = el.getAttribute('aria-label') || '';
            result.push(`${tag} role="${role}"${label ? ` label="${label}"` : ''}`);
          });
        });
        return result;
      });

      expect(landmarks.length).toBeGreaterThanOrEqual(4);
      expect(landmarks.some((l) => l.includes('header') || l.includes('banner'))).toBeTruthy();
      expect(landmarks.some((l) => l.includes('nav') || l.includes('navigation'))).toBeTruthy();
      expect(landmarks.some((l) => l.includes('main'))).toBeTruthy();
      expect(landmarks.some((l) => l.includes('footer') || l.includes('contentinfo'))).toBeTruthy();
    });
  }

  test('Skip-to-content link is present and functional', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');

    const skipLink = page.locator('.sk-skip');
    await expect(skipLink).toBeVisible();
    await expect(skipLink).toHaveAttribute('href', '#main');
  });

  test('Breadcrumb has correct ARIA navigation landmark', async ({ page }) => {
    await page.goto('/dashboard');
    await page.waitForLoadState('networkidle');

    const breadcrumbNav = page.locator('nav[aria-label="Breadcrumb"]');
    await expect(breadcrumbNav).toBeVisible();
    await expect(breadcrumbNav.locator('ol')).toBeVisible();
  });

  test('No critical axe violations on route', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');

    const results = await new AxeBuilder({ page })
      .withTags(['wcag2a', 'wcag2aa'])
      .analyze();

    const criticalSerious = results.violations.filter(
      (v) => v.impact === 'critical' || v.impact === 'serious'
    );
    expect(criticalSerious.length).toBe(0);
  });
});
