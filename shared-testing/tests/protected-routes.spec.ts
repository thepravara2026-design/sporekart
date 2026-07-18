import { test, expect } from '@playwright/test';

test.describe('Part 4 — Protected Routes Validation', () => {

  test('Access is restricted for unauthorized roles on customer routes', async ({ page }) => {
    // 1. Set role to guest (unauthorized for customer account overview)
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.selectOption('select[aria-label="Switch review role"]', 'guest');

    // 2. Try navigating to protected customer route directly
    await page.goto('/account');
    await page.waitForLoadState('networkidle');

    // 3. Verify access restricted placeholder panel is visible
    await expect(page.locator('text=Access restricted')).toBeVisible();
    await expect(page.locator('text=Your current role (guest) cannot view this page.')).toBeVisible();
  });

  test('Access is restricted for customer role on administrator routes', async ({ page }) => {
    // 1. Set role to customer (unauthorized for admin/orders overview)
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.selectOption('select[aria-label="Switch review role"]', 'customer');

    // 2. Try navigating to protected order queue route
    await page.goto('/orders');
    await page.waitForLoadState('networkidle');

    // 3. Verify access restricted placeholder panel is visible
    await expect(page.locator('text=Access restricted')).toBeVisible();
    await expect(page.locator('text=Your current role (customer) cannot view this page.')).toBeVisible();
  });

  test('Authorized role can successfully access protected workspace', async ({ page }) => {
    // 1. Set role to customer (authorized for customer account overview)
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.selectOption('select[aria-label="Switch review role"]', 'customer');

    // 2. Navigate to customer route
    await page.goto('/account');
    await page.waitForLoadState('networkidle');

    // 3. Verify the actual page loads and doesn't display access restricted
    await expect(page.locator('text=Access restricted')).not.toBeVisible();
    await expect(page.locator('text=Account home')).toBeVisible();
  });
});
