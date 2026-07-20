import { test, expect } from '@playwright/test';

test.describe('Part 7 — Mobile & Responsive Validation', () => {

  test('Desktop layout (1920x1080) details', async ({ page }) => {
    await page.setViewportSize({ width: 1920, height: 1080 });
    await page.goto('/login');
    await page.waitForLoadState('networkidle');

    // Desktop: brand column and auth card both visible side-by-side
    await expect(page.locator('.auth-layout__brand')).toBeVisible();
    await expect(page.locator('.auth-card')).toBeVisible();

    // Verify grid layout properties
    const brandBox = await page.locator('.auth-layout__brand').boundingBox();
    const cardBox = await page.locator('.auth-card').boundingBox();
    
    expect(brandBox).toBeTruthy();
    expect(cardBox).toBeTruthy();
    // They should sit side-by-side (brand is left of card)
    expect(brandBox!.x).toBeLessThan(cardBox!.x);

    await page.screenshot({ path: '../QA_REPORTS/Sprint-02/Evidence/Screenshots/viewport-desktop.png' });
  });

  test('Tablet layout (768x1024) details', async ({ page }) => {
    await page.setViewportSize({ width: 768, height: 1024 });
    await page.goto('/login');
    await page.waitForLoadState('networkidle');

    // Tablet: brand and auth card visible, check layout collapse rules
    await expect(page.locator('.auth-card')).toBeVisible();
    await page.screenshot({ path: '../QA_REPORTS/Sprint-02/Evidence/Screenshots/viewport-tablet.png' });
  });

  test('Mobile layout (375x812) details', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });
    await page.goto('/login');
    await page.waitForLoadState('networkidle');

    // Mobile: brand column is hidden (display: none / collapsed), only auth card is visible
    await expect(page.locator('.auth-layout__brand')).not.toBeVisible();
    await expect(page.locator('.auth-card')).toBeVisible();

    // Verify header mobile layout elements
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    // Check that the shell has the mobile menu toggle button visible
    await expect(page.locator('button[aria-label="Open sidebar"]')).toBeVisible();

    await page.screenshot({ path: '../QA_REPORTS/Sprint-02/Evidence/Screenshots/viewport-mobile.png' });
  });
});
