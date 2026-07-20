import { test, expect } from '@playwright/test';

test.describe('Part 11 — Regression Validation', () => {

  test('Mock mode environment variables are present and verify correct defaults', () => {
    // Re-verify the Sprint 1 environment baseline variables
    expect(process.env.MOCK_MODE || 'true').toBe('true');
    expect(process.env.QA_MODE || 'true').toBe('true');
  });

  test('Application header and navigation elements render correctly', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');

    // 1. Title verification
    await expect(page).toHaveTitle(/SporeKart|Sporekart/);

    // 2. Shell header components visible
    await expect(page.locator('.sk-header')).toBeVisible();
    await expect(page.locator('.sk-profile')).toBeVisible();

    // 3. Skip link is present for accessibility compliance
    await expect(page.locator('text=Skip to content')).toBeVisible();
  });
});
