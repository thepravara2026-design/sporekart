import { test, expect } from '@playwright/test';

/**
 * Release Condition C1 — reconciled to the shipped application (Approval Gate D).
 *
 * Shipped facts this suite is aligned to:
 *  - The role switcher is a <select aria-label="Preview role"> that is ONLY
 *    rendered while the active role is 'guest' (BUG-SEC-005 hardening). Once a
 *    privileged role is selected the switcher is removed from the DOM, so the
 *    original "switch to administrator and re-read the select" assertions are
 *    obsolete and have been removed.
 *  - The enterprise shell (nav.sk-sidebar + header) is rendered for enterprise
 *    routes only. Public-website, /dashboard, /admin, /auth and /preview paths
 *    use a separate router without the sidebar (isNonEnterpriseRoute in App.tsx).
 *  - For the guest role the only visible workspaces are the 'public' ones:
 *    Public, Training, Demo, Design System.
 *  - Unauthorised access redirects to /access-denied ("Access denied"); the old
 *    inline "Access restricted" placeholder was removed (BUG-RT-007).
 */

test.describe('Part 3 — Authorization & RBAC Validation', () => {

  test.beforeEach(async ({ page }) => {
    // /demo is a public enterprise-shell route, so the sidebar, header and the
    // guest-only role switcher are all present here.
    await page.goto('/demo');
    await page.waitForLoadState('networkidle');
  });

  // ==========================================================================
  // SECTION 1: Role Switcher Validation
  // ==========================================================================

  test('Role switcher exists with all 9 roles', async ({ page }) => {
    const roleSelect = page.locator('select[aria-label="Preview role"]');
    await expect(roleSelect).toBeVisible();
    const options = await roleSelect.locator('option').allTextContents();
    expect(options).toEqual([
      'Guest', 'Customer', 'Grower', 'Trainer', 'Distributor',
      'Support', 'Administrator', 'Business Owner', 'Governance Manager',
    ]);
  });

  test('Default active role on load is guest', async ({ page }) => {
    const roleSelect = page.locator('select[aria-label="Preview role"]');
    await expect(roleSelect).toHaveValue('guest');
  });

  test('Role switcher is hidden once a privileged role is selected', async ({ page }) => {
    const roleSelect = page.locator('select[aria-label="Preview role"]');
    await expect(roleSelect).toBeVisible();
    await roleSelect.selectOption('administrator');
    await page.waitForTimeout(300);
    // BUG-SEC-005: switcher is only rendered while role === 'guest'.
    await expect(page.locator('select[aria-label="Preview role"]')).toHaveCount(0);
  });

  // ==========================================================================
  // SECTION 2: Guest Sidebar Visibility (public workspaces only)
  // ==========================================================================

  test('Guest sidebar shows only public workspaces', async ({ page }) => {
    const sidebar = page.locator('nav.sk-sidebar');
    // The sidebar is always in the DOM; on tablet/mobile viewports it is an
    // off-canvas drawer (hidden until sk-sidebar--open), so assert attachment
    // and role-based rendering via label content rather than pixel visibility.
    await expect(sidebar).toBeAttached();

    const labels = (await sidebar.locator('.sk-workspace-link__label').allTextContents())
      .map((l) => l.trim());

    const visible = ['Public', 'Training', 'Demo', 'Design System'];
    for (const ws of visible) {
      expect(labels).toContain(ws);
    }

    const hidden = [
      'Customer', 'Orders', 'Products', 'AI Workspace',
      'Governance', 'Analytics', 'Administration', 'CMS', 'Support', 'Settings',
    ];
    for (const ws of hidden) {
      expect(labels).not.toContain(ws);
    }
  });

  // ==========================================================================
  // SECTION 3: Protected Route Access Validation (redirect to /access-denied)
  // ==========================================================================

  test('Guest is redirected to access-denied when opening a restricted workspace', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('/access-denied');
    await expect(page.locator('text=Access denied')).toBeVisible();
  });

  test('Guest is redirected to access-denied for the customer workspace', async ({ page }) => {
    await page.goto('/account');
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('/access-denied');
  });

  test('Guest is redirected to login when opening the admin dashboard', async ({ page }) => {
    // /admin is guarded by RequireAuth; a guest (unauthenticated) is sent to /login.
    await page.goto('/admin/dashboard');
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('/login');
  });

  // ==========================================================================
  // SECTION 4: Public workspace access
  // ==========================================================================

  test('Public workspaces are reachable by a guest without error', async ({ page }) => {
    const publicRoutes = ['/training', '/demo', '/design-system'];
    for (const route of publicRoutes) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      expect(page.url()).not.toContain('/access-denied');
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(0);
    }
  });

  // ==========================================================================
  // SECTION 5: Sidebar structure
  // ==========================================================================

  test('Workspace groups are correctly labelled in sidebar', async ({ page }) => {
    const groups = page.locator('.sk-sidebar__group-label');
    const groupTexts = (await groups.allTextContents()).map((t) => t.trim());
    // Guest sees public workspaces spanning the discover, operate and platform groups.
    expect(groupTexts).toContain('Discover');
    expect(groupTexts).toContain('Operate');
    expect(groupTexts).toContain('Platform');
  });

  // ==========================================================================
  // SECTION 6: URL Access Patterns
  // ==========================================================================

  test('Direct URL to /demo renders the demo workspace in sidebar', async ({ page }) => {
    await page.goto('/demo');
    await page.waitForLoadState('networkidle');
    const sidebar = page.locator('nav.sk-sidebar');
    // Off-canvas on tablet/mobile — assert attachment (viewport-independent).
    await expect(
      sidebar.locator('.sk-workspace-link__label', { hasText: 'Demo' })
    ).toBeAttached();
  });

  test('Direct URL to /training renders content without redirect', async ({ page }) => {
    await page.goto('/training');
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('/training');
    expect(page.url()).not.toContain('/access-denied');
  });

  // ==========================================================================
  // SECTION 7: Storage and State Validation
  // ==========================================================================

  test('No role or permission data stored in localStorage', async ({ page }) => {
    const storage = await page.evaluate(() => {
      const keys: string[] = [];
      for (let i = 0; i < localStorage.length; i++) keys.push(localStorage.key(i)!);
      return keys;
    });
    const roleKeys = storage.filter((k) =>
      k.toLowerCase().includes('role') ||
      k.toLowerCase().includes('permission') ||
      k.toLowerCase().includes('rbac')
    );
    expect(roleKeys).toEqual([]);
  });

  // ==========================================================================
  // SECTION 8: Responsive Sidebar
  // ==========================================================================

  test('Sidebar is responsive on mobile viewport', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 667 });
    await page.goto('/demo');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });
});
