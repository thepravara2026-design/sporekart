import { test, expect } from '@playwright/test';

/**
 * Release Condition C1 — reconciled to the shipped application (Approval Gate D).
 *
 * Shipped facts this suite is aligned to:
 *  - Unauthorised access no longer renders an inline "Access restricted" panel.
 *    Both the route guard (RequireAuth) and the workspace shell (WorkspacePage,
 *    BUG-RT-007) redirect to the dedicated /access-denied screen, whose visible
 *    title is "Access denied".
 *  - Enterprise workspace routes (/account, /orders, /settings, ...) resolve to
 *    the workspace shell; when the active role cannot view them the app
 *    redirects to /access-denied.
 *  - /dashboard and /admin are guarded by RequireAuth. A guest (unauthenticated)
 *    is redirected to /login; a non-permitted authenticated role is redirected
 *    to /access-denied.
 *  - The role switcher (select[aria-label="Preview role"]) only exists while the
 *    role is 'guest'.
 */

test.describe('Part 4 — Protected Routes Validation', () => {

  test('Guest is denied access to a customer workspace route', async ({ page }) => {
    // Default role is guest; the customer workspace requires customer/grower.
    await page.goto('/account');
    await page.waitForLoadState('networkidle');

    expect(page.url()).toContain('/access-denied');
    await expect(page.locator('text=Access denied')).toBeVisible();
  });

  test('Guest is denied access to an operational order queue route', async ({ page }) => {
    await page.goto('/orders');
    await page.waitForLoadState('networkidle');

    expect(page.url()).toContain('/access-denied');
    await expect(page.locator('text=Access denied')).toBeVisible();
  });

  test('Guest is denied access to the settings workspace', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');

    expect(page.url()).toContain('/access-denied');
    await expect(page.locator('text=Access denied')).toBeVisible();
  });

  test('Guest hitting a guarded dashboard route is redirected to login', async ({ page }) => {
    await page.goto('/dashboard');
    await page.waitForLoadState('networkidle');

    expect(page.url()).toContain('/login');
  });

  test('Guest hitting a guarded admin route is redirected to login', async ({ page }) => {
    await page.goto('/admin/dashboard');
    await page.waitForLoadState('networkidle');

    expect(page.url()).toContain('/login');
  });

  test('Public workspace routes remain reachable without denial', async ({ page }) => {
    await page.goto('/training');
    await page.waitForLoadState('networkidle');

    expect(page.url()).toContain('/training');
    expect(page.url()).not.toContain('/access-denied');
    await expect(page.locator('text=Access denied')).not.toBeVisible();
  });
});
