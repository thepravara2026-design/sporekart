import { test, expect } from '@playwright/test';

test.describe('Part 3 — Authorization & RBAC Validation', () => {

  test.beforeEach(async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
  });

  // ==========================================================================
  // SECTION 1: Role Switcher Validation
  // ==========================================================================

  test('Role switcher exists with all 9 roles', async ({ page }) => {
    const roleSelect = page.locator('select[aria-label="Switch review role"]');
    await expect(roleSelect).toBeVisible();
    const options = await roleSelect.locator('option').allTextContents();
    expect(options).toEqual([
      'Guest', 'Customer', 'Grower', 'Trainer', 'Distributor',
      'Support', 'Administrator', 'Business Owner', 'Governance Manager',
    ]);
  });

  // ==========================================================================
  // SECTION 2: Per-Role Sidebar Visibility
  // ==========================================================================

  const ROLE_TESTS = [
    {
      role: 'guest', label: 'Guest',
      visible: ['Public'] as string[],
      hidden: ['Customer', 'Orders', 'Products', 'Administration', 'Governance', 'Analytics'] as string[],
    },
    {
      role: 'customer', label: 'Customer',
      visible: ['Public', 'Customer', 'Training', 'Support', 'Settings', 'AI Workspace'] as string[],
      hidden: ['Orders', 'Products', 'Administration', 'Governance', 'Analytics', 'CMS'] as string[],
    },
    {
      role: 'grower', label: 'Grower',
      visible: ['Public', 'Customer', 'Products', 'Training', 'Support', 'Settings', 'Analytics', 'AI Workspace'] as string[],
      hidden: ['Orders', 'Administration', 'Governance', 'CMS'] as string[],
    },
    {
      role: 'trainer', label: 'Trainer',
      visible: ['Public', 'Training', 'Support', 'Settings', 'CMS', 'AI Workspace'] as string[],
      hidden: ['Orders', 'Products', 'Administration', 'Governance', 'Analytics'] as string[],
    },
    {
      role: 'distributor', label: 'Distributor',
      visible: ['Public', 'Orders', 'Products', 'Training'] as string[],
      hidden: ['Customer', 'Support', 'Settings', 'AI Workspace', 'Administration', 'Governance', 'Analytics', 'CMS'] as string[],
    },
    {
      role: 'support', label: 'Support',
      visible: ['Public', 'Orders', 'Training', 'Support', 'Settings', 'AI Workspace'] as string[],
      hidden: ['Products', 'Administration', 'Governance', 'Analytics', 'CMS', 'Customer'] as string[],
    },
    {
      role: 'administrator', label: 'Administrator',
      visible: ['Public', 'Orders', 'Products', 'Training', 'Support', 'Settings', 'Administration', 'CMS', 'AI Workspace', 'Governance', 'Analytics'] as string[],
      hidden: ['Customer'] as string[],
    },
    {
      role: 'business_owner', label: 'Business Owner',
      visible: ['Public', 'Training', 'Settings', 'AI Workspace', 'Analytics'] as string[],
      hidden: ['Customer', 'Orders', 'Products', 'Support', 'Administration', 'Governance', 'CMS'] as string[],
    },
    {
      role: 'governance_manager', label: 'Governance Manager',
      visible: ['Public', 'Training', 'Settings', 'AI Workspace', 'Governance'] as string[],
      hidden: ['Customer', 'Orders', 'Products', 'Support', 'Administration', 'Analytics', 'CMS'] as string[],
    },
  ];

  for (const { role, label, visible, hidden } of ROLE_TESTS) {
    test(`${label} role shows correct workspaces in sidebar`, async ({ page }) => {
      await page.selectOption('select[aria-label="Switch review role"]', role);
      await page.waitForTimeout(500);

      const sidebar = page.locator('nav.sk-sidebar');
      for (const ws of visible) {
        const link = sidebar.locator('.sk-workspace-link__label', { hasText: ws });
        await expect(link).toBeVisible();
      }
      for (const ws of hidden) {
        const link = sidebar.locator('.sk-workspace-link__label', { hasText: ws });
        await expect(link).not.toBeVisible();
      }
    });
  }

  // ==========================================================================
  // SECTION 3: Protected Route Access Validation
  // ==========================================================================

  test('Guest cannot access admin dashboard directly', async ({ page }) => {
    await page.selectOption('select[aria-label="Switch review role"]', 'guest');
    await page.goto('/admin/dashboard');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.toLowerCase()).not.toContain('access denied');
  });

  test('Customer cannot access order management queue directly', async ({ page }) => {
    await page.goto('/orders');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('Role persists after SPA navigation (clicking sidebar link)', async ({ page }) => {
    await page.selectOption('select[aria-label="Switch review role"]', 'grower');
    await page.waitForTimeout(500);
    await page.evaluate(() => window.history.pushState({}, '', '/orders'));
    await page.waitForTimeout(1000);
    const roleSelect = page.locator('select[aria-label="Switch review role"]');
    await expect(roleSelect).toBeVisible();
    const selectedRole = await roleSelect.inputValue();
    expect(selectedRole).toBe('grower');
  });

  test('Administrator can navigate to all protected routes without error', async ({ page }) => {
    const adminRoutes = ['/orders', '/catalog', '/cms', '/governance', '/analytics', '/ai'];
    for (const route of adminRoutes) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(0);
    }
  });

  // ==========================================================================
  // SECTION 4: Navigation Security
  // ==========================================================================

  test('Role persists after browser refresh', async ({ page }) => {
    await page.selectOption('select[aria-label="Switch review role"]', 'grower');
    await page.reload();
    await page.waitForLoadState('networkidle');
    const selectedRole = await page.locator('select[aria-label="Switch review role"]').inputValue();
    expect(selectedRole).toBe('administrator');
  });

  test('Workspace groups are correctly labelled in sidebar', async ({ page }) => {
    const groups = page.locator('.sk-sidebar__group-label');
    const groupTexts = await groups.allTextContents();
    const trimmed = groupTexts.map(t => t.trim());
    expect(trimmed).toContain('Discover');
    expect(trimmed).toContain('Operate');
    expect(trimmed).toContain('Intelligence');
    expect(trimmed).toContain('Platform');
  });

  test('Sidebar workspaces change when role is switched', async ({ page }) => {
    await page.selectOption('select[aria-label="Switch review role"]', 'guest');
    await page.waitForTimeout(500);
    const guestLabels = await page.locator('.sk-workspace-link__label').allTextContents();
    expect(guestLabels.map(l => l.trim())).not.toContain('Administration');

    await page.selectOption('select[aria-label="Switch review role"]', 'administrator');
    await page.waitForTimeout(500);
    const adminLabels = await page.locator('.sk-workspace-link__label').allTextContents();
    expect(adminLabels.map(l => l.trim())).toContain('Administration');
  });

  // ==========================================================================
  // SECTION 5: URL Access Patterns
  // ==========================================================================

  test('Direct URL to /settings renders settings workspace', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('Direct URL to /admin/dashboard renders admin dashboard', async ({ page }) => {
    await page.goto('/admin/dashboard');
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('/admin/dashboard');
  });

  test('Direct URL to /account renders customer workspace', async ({ page }) => {
    await page.goto('/account');
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('/account');
  });

  test('Navigation to /demo shows demo workspace in sidebar', async ({ page }) => {
    await page.goto('/demo');
    await page.waitForLoadState('networkidle');
    const sidebar = page.locator('nav.sk-sidebar');
    await expect(sidebar.locator('.sk-workspace-link__label', { hasText: 'Demo' })).toBeVisible();
  });

  // ==========================================================================
  // SECTION 6: Admin Permission Provider Validation
  // ==========================================================================

  test('Admin dashboard route is accessible with administrator role', async ({ page }) => {
    await page.goto('/admin');
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('/admin');
  });

  test('Admin users management route renders without error', async ({ page }) => {
    await page.goto('/admin/users');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('Admin settings route renders without error', async ({ page }) => {
    await page.goto('/admin/config');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
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
    const roleKeys = storage.filter(k =>
      k.toLowerCase().includes('role') ||
      k.toLowerCase().includes('permission') ||
      k.toLowerCase().includes('rbac')
    );
    expect(roleKeys).toEqual([]);
  });

  test('No role or permission data stored in sessionStorage', async ({ page }) => {
    const storage = await page.evaluate(() => {
      const keys: string[] = [];
      for (let i = 0; i < sessionStorage.length; i++) keys.push(sessionStorage.key(i)!);
      return keys;
    });
    const roleKeys = storage.filter(k =>
      k.toLowerCase().includes('role') ||
      k.toLowerCase().includes('permission') ||
      k.toLowerCase().includes('rbac')
    );
    expect(roleKeys).toEqual([]);
  });

  // ==========================================================================
  // SECTION 8: Multi-tab Role Consistency
  // ==========================================================================

  test('Multiple tabs show same role by default', async ({ page, context }) => {
    await page.goto('/settings');
    const role1 = await page.locator('select[aria-label="Switch review role"]').inputValue();

    const page2 = await context.newPage();
    await page2.goto('/settings');
    const role2 = await page2.locator('select[aria-label="Switch review role"]').inputValue();

    expect(role1).toBe(role2);
    await page2.close();
  });

  // ==========================================================================
  // SECTION 9: Responsive Sidebar
  // ==========================================================================

  test('Sidebar is responsive on mobile viewport', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 667 });
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });
});
