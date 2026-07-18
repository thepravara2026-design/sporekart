import { test, expect } from '@playwright/test';

test.describe('Part 6 — Browser Compatibility', () => {

  test('Login layout renders consistently', async ({ page }, testInfo) => {
    // 1. Navigate to login
    await page.goto('/login');
    await page.waitForLoadState('networkidle');

    // 2. Verify key layout components are rendered correctly
    await expect(page.locator('.auth-card')).toBeVisible();
    await expect(page.locator('.auth-card__title')).toHaveText('Access your workspace');
    
    // 3. Verify brand section exists (hidden on mobile screen sizes, but visible on desktop)
    const viewportWidth = page.viewportSize()?.width ?? 1280;
    if (viewportWidth >= 768) {
      await expect(page.locator('.auth-layout__brand')).toBeVisible();
    }

    // 4. Capture screenshot for layout validation
    const browserName = testInfo.project.name;
    await page.screenshot({
      path: `../QA_REPORTS/Sprint-02/Evidence/Screenshots/login-layout-${browserName}.png`,
      fullPage: true,
    });
  });
});
