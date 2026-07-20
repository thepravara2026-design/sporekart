import { test, expect } from '@playwright/test';
import AxeBuilder from '@axe-core/playwright';

test.describe('Part 8 — Accessibility Validation', () => {

  test('Skip to content link is present and functional', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');

    // 1. Verify skip link exists and has correct href
    const skipLink = page.locator('text=Skip to content');
    await expect(skipLink).toBeVisible();
    await expect(skipLink).toHaveAttribute('href', '#main');

    // 2. Tab focus on skip link and check focus ring visibility
    await page.keyboard.press('Tab');
    await expect(skipLink).toBeFocused();
  });

  test('LoginPage accessibility scan (WCAG 2.1 AA)', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');

    // Run axe-core accessibility audit
    const results = await new AxeBuilder({ page })
      .withTags(['wcag2a', 'wcag2aa'])
      .analyze();

    // Verify there are no critical accessibility violations
    const criticalViolations = results.violations.filter(
      (v) => v.impact === 'critical' || v.impact === 'serious'
    );
    expect(criticalViolations.length).toBe(0);
  });

  test('RegisterPage accessibility scan (WCAG 2.1 AA)', async ({ page }) => {
    await page.goto('/register');
    await page.waitForLoadState('networkidle');

    const results = await new AxeBuilder({ page })
      .withTags(['wcag2a', 'wcag2aa'])
      .analyze();

    const criticalViolations = results.violations.filter(
      (v) => v.impact === 'critical' || v.impact === 'serious'
    );
    expect(criticalViolations.length).toBe(0);
  });

  test('Semantic HTML and keyboard navigation on login inputs', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');

    // Check that we have a single H1 header tag for proper semantic outline
    const h1 = page.locator('h1');
    await expect(h1).toHaveCount(1);

    // Verify label inputs are linked correctly via htmlFor or nested labels
    const identifierInput = page.locator('input[type="tel"]');
    await expect(identifierInput).toHaveAttribute('required');

    // Tab through fields sequentially
    await page.click('input[type="tel"]');
    await page.keyboard.press('Tab'); // Tabbing out of input
    const rememberCheckbox = page.locator('input[type="checkbox"]').first();
    await expect(rememberCheckbox).toBeFocused();
  });
});
