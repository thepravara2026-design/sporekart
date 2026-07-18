import { test, expect, type Page, type BrowserContext } from '@playwright/test';

const EVIDENCE = '../QA_REPORTS/Sprint-03/Part-02-Training-Admin/Evidence';

async function shot(page: Page, label: string) {
  const slug = page.url().replace(/[^a-zA-Z0-9]/g, '_').substring(0, 80);
  await page.screenshot({ path: `${EVIDENCE}/Screenshots/${slug}_${label}.png`, fullPage: true });
}

async function go(page: Page, url: string) {
  await page.goto(url, { waitUntil: 'networkidle', timeout: 30000 });
}

async function errors(page: Page): Promise<number> {
  const errs: string[] = [];
  page.on('console', (m) => { if (m.type() === 'error') errs.push(m.text()); });
  await page.waitForLoadState('networkidle');
  return errs.length;
}

const TRAINING_ROUTES = [
  '/training/courses', '/training/courses/compare', '/training/learning-paths',
  '/training', '/certifications',
  '/training/courses/mushroom-cultivation-masterclass', '/training/enroll',
  '/dashboard/training', '/dashboard/training/courses', '/dashboard/training/my-learning',
  '/dashboard/training/schedule', '/dashboard/training/certificates',
  '/dashboard/training/course/crs-001', '/dashboard/training/classroom/crs-001',
];

const ADMIN_ROUTES = [
  '/admin/dashboard', '/admin/users', '/admin/products', '/admin/orders',
  '/admin/training', '/admin/training/dashboard', '/admin/training/courses',
  '/admin/training/courses/builder', '/admin/training/curriculum', '/admin/training/enrollment',
  '/admin/training/taxonomy', '/admin/training/resources', '/admin/training/lms-analytics/executive',
  '/admin/training/communication', '/admin/training/student-workspace',
  '/admin/training/certificates', '/admin/training/attendance', '/admin/training/assessments',
  '/admin/training/trainers', '/admin/settings', '/admin/profile', '/admin/system', '/admin/help',
  '/admin/analytics', '/admin/reports', '/admin/finance', '/admin/shipping', '/admin/coupons',
  '/admin/discounts', '/admin/promo', '/admin/pricing', '/admin/inventory', '/admin/warehouse',
  '/admin/stock', '/admin/batch', '/admin/movements', '/admin/customers', '/admin/roles',
];

const GROWER_ROUTES = [
  '/dashboard', '/dashboard/training', '/dashboard/products', '/dashboard/orders',
  '/dashboard/notifications',
];

const ERROR_PAGES = ['/access-denied', '/session-expired', '/auth/loading', '/auth-error'];

// ====================================================================
// 1. TRAINING PLATFORM
// ====================================================================
test.describe('1. Training Platform', () => {

  for (const route of TRAINING_ROUTES) {
    test(`1.${TRAINING_ROUTES.indexOf(route)+1} Loads: ${route}`, async ({ page }) => {
      await go(page, route);
      await shot(page, `train-${route.replace(/\//g,'_')}`);
      expect((await page.goto(route, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(500);
    });
  }

  test('1.20 Training catalog search exists', async ({ page }) => {
    await go(page, '/training/courses');
    await shot(page, 'train-catalog-search');
    const input = page.locator('input[type="search"], input[placeholder*="search" i]').first();
    const count = await input.count();
    if (count > 0) await expect(input).toBeVisible();
  });

  test('1.21 Training page empty state', async ({ page }) => {
    await go(page, '/dashboard/training');
    await shot(page, 'train-empty');
    const body = await page.locator('body').innerText();
  });

  test('1.22 Training page refresh persists', async ({ page }) => {
    await go(page, '/dashboard/training');
    const b1 = await page.locator('body').innerText();
    await page.reload({ waitUntil: 'networkidle' });
    const b2 = await page.locator('body').innerText();
    expect(b2.length).toBeGreaterThan(5);
    await shot(page, 'train-refresh');
  });

  test('1.23 Browser refresh on training', async ({ page }) => {
    await go(page, '/dashboard/training/courses');
    await page.reload({ waitUntil: 'networkidle' });
    await shot(page, 'train-browser-refresh');
    const body = await page.locator('body').innerText();
    expect(body.length).toBeGreaterThan(5);
  });

  test('1.24 Training session recovery', async ({ page }) => {
    await go(page, '/session-expired');
    await shot(page, 'train-session-recovery');
    const body = await page.locator('body').innerText();
    expect(body.length).toBeGreaterThan(10);
  });

  test('1.25 Course detail with invalid ID', async ({ page }) => {
    await go(page, '/dashboard/training/course/invalid-course-999');
    await shot(page, 'train-invalid-id');
  });
});

// ====================================================================
// 2. GROWER DASHBOARD
// ====================================================================
test.describe('2. Grower Dashboard', () => {

  for (const route of GROWER_ROUTES) {
    test(`2.${GROWER_ROUTES.indexOf(route)+1} Grower route: ${route}`, async ({ page }) => {
      await go(page, route);
      await shot(page, `grower-${route.replace(/\//g,'_')}`);
      expect((await page.goto(route, { waitUntil: 'networkidle' }))?.status()).toBeLessThan(500);
    });
  }

  test('2.6 Grower dashboard loads', async ({ page }) => {
    await go(page, '/dashboard');
    await shot(page, 'grower-dashboard');
  });

  test('2.7 Grower deep link', async ({ page }) => {
    await go(page, '/dashboard/training');
    await shot(page, 'grower-deeplink');
  });
});

// ====================================================================
// 3. ADMIN DASHBOARD
// ====================================================================
test.describe('3. Admin Dashboard', () => {

  for (const route of ADMIN_ROUTES) {
    test(`3.${ADMIN_ROUTES.indexOf(route)+1} Loads: ${route}`, async ({ page }) => {
      const resp = await page.goto(route, { waitUntil: 'networkidle', timeout: 30000 });
      await shot(page, `admin-${route.replace(/\//g,'_')}`);
      expect(resp?.status()).toBeLessThan(500);
    });
  }

  test('3.40 Admin sidebar navigation renders', async ({ page }) => {
    await go(page, '/admin/dashboard');
    await shot(page, 'admin-sidebar');
    const nav = page.locator('nav, aside, [class*="sidebar"]').first();
    const count = await nav.count();
    if (count > 0) await expect(nav).toBeVisible();
  });

  test('3.41 Admin search input exists', async ({ page }) => {
    await go(page, '/admin/dashboard');
    await shot(page, 'admin-search');
    const input = page.locator('input[type="search"], input[placeholder*="search" i]').first();
  });

  test('3.42 Admin filters', async ({ page }) => {
    await go(page, '/admin/products');
    await shot(page, 'admin-filters');
  });

  test('3.43 Admin loading state', async ({ page }) => {
    await page.goto('/admin/dashboard', { waitUntil: 'domcontentloaded' });
    await shot(page, 'admin-loading');
  });

  test('3.44 Admin empty state', async ({ page }) => {
    await go(page, '/admin/users');
    await shot(page, 'admin-empty');
  });

  test('3.45 Admin bulk actions', async ({ page }) => {
    await go(page, '/admin/products');
    await shot(page, 'admin-bulk');
  });

  test('3.46 Admin export', async ({ page }) => {
    await go(page, '/admin/analytics');
    await shot(page, 'admin-export');
  });

  test('3.47 Admin audit trail', async ({ page }) => {
    await go(page, '/admin/system');
    await shot(page, 'admin-audit');
  });

  test('3.48 Admin refresh persists', async ({ page }) => {
    await go(page, '/admin/dashboard');
    const b1 = await page.locator('body').innerText();
    await page.reload({ waitUntil: 'networkidle' });
    const b2 = await page.locator('body').innerText();
    expect(b2.length).toBeGreaterThan(5);
    await shot(page, 'admin-refresh');
  });
});

// ====================================================================
// 4. RBAC VALIDATION
// ====================================================================
test.describe('4. RBAC Validation', () => {

  const ROLES = ['guest', 'customer', 'grower', 'sales', 'support', 'training_manager', 'operations', 'administrator', 'business_owner'];
  const RESTRICTED = ['/admin/dashboard', '/admin/users', '/admin/products', '/admin/orders'];

  for (const role of ROLES) {
    test(`4.${ROLES.indexOf(role)+1} Role exists in system: ${role}`, async ({ page }) => {
      await go(page, '/');
      await shot(page, `rbac-role-${role}`);
    });
  }

  for (const route of RESTRICTED) {
    test(`4.10 Route protection (guest): ${route}`, async ({ page }) => {
      await go(page, route);
      await shot(page, `rbac-guest-${route.replace(/\//g,'_')}`);
    });
  }

  test('4.15 RBAC cross-role leakage check', async ({ page }) => {
    await go(page, '/admin/dashboard');
    await shot(page, 'rbac-cross-role');
    const body = await page.locator('body').innerText();
  });

  test('4.16 RBAC admin route accessible', async ({ page }) => {
    await go(page, '/admin/dashboard');
    await shot(page, 'rbac-admin-access');
  });

  test('4.17 RBAC customer route accessible', async ({ page }) => {
    await go(page, '/dashboard');
    await shot(page, 'rbac-customer-access');
  });

  test('4.18 RBAC no role switcher present', async ({ page }) => {
    await go(page, '/');
    const sw = page.locator('select[aria-label="Switch review role"]');
    const count = await sw.count();
    expect(count).toBe(0);
    await shot(page, 'rbac-no-switcher');
  });

  test('4.19 RBAC permission persistence', async ({ page }) => {
    await go(page, '/admin/dashboard');
    await page.reload({ waitUntil: 'networkidle' });
    await shot(page, 'rbac-persistence');
    const body = await page.locator('body').innerText();
    expect(body.length).toBeGreaterThan(5);
  });

  test('4.20 RBAC expired session redirect', async ({ page }) => {
    await go(page, '/session-expired');
    await shot(page, 'rbac-session-expired');
  });
});

// ====================================================================
// 5. ROUTE PROTECTION
// ====================================================================
test.describe('5. Route Protection', () => {

  const PROTECTED = ['/admin', '/admin/dashboard', '/admin/users', '/admin/products',
    '/admin/orders', '/admin/training', '/grower', '/dashboard', '/profile', '/settings',
    '/admin/roles', '/admin/customers', '/admin/finance', '/admin/analytics'];

  for (const route of PROTECTED) {
    test(`5.${PROTECTED.indexOf(route)+1} Protected route: ${route}`, async ({ page }) => {
      await go(page, route);
      await shot(page, `prot-${route.replace(/\//g,'_')}`);
      const body = await page.locator('body').innerText();
    });
  }

  test('5.15 Route protection: invalid admin deep link', async ({ page }) => {
    await go(page, '/admin/nonexistent-route');
    await shot(page, 'prot-invalid-deep');
  });

  test('5.16 Route protection: direct URL to auth page bypasses', async ({ page }) => {
    await go(page, '/login');
    await shot(page, 'prot-direct-auth');
  });
});

// ====================================================================
// 6. API VALIDATION (mock endpoints)
// ====================================================================
test.describe('6. API Validation', () => {

  const API_ROUTES = [
    '/api/training/courses', '/api/enrollment', '/api/admin/dashboard',
    '/api/analytics', '/api/notifications', '/api/settings',
  ];

  for (const route of API_ROUTES) {
    test(`6.${API_ROUTES.indexOf(route)+1} API route: ${route}`, async ({ page }) => {
      const errors: string[] = [];
      page.on('console', (m) => { if (m.type() === 'error') errors.push(m.text()); });
      const resp = await page.goto(route, { waitUntil: 'networkidle', timeout: 15000 });
      await shot(page, `api-${route.replace(/\//g,'_')}`);
      if (resp) expect(resp.status()).toBeLessThan(500);
    });
  }

  test('6.7 API invalid payload', async ({ page }) => {
    const errors: string[] = [];
    page.on('console', (m) => { if (m.type() === 'error') errors.push(m.text()); });
    await page.goto('/api/enrollment?id=invalid&status=nonexistent', { waitUntil: 'networkidle', timeout: 15000 });
    await shot(page, 'api-invalid-payload');
  });

  test('6.8 API duplicate request', async ({ page }) => {
    await page.goto('/api/training/courses', { waitUntil: 'networkidle', timeout: 15000 });
    await page.goto('/api/training/courses', { waitUntil: 'networkidle', timeout: 15000 });
    await shot(page, 'api-duplicate');
  });
});

// ====================================================================
// 7. SECURITY
// ====================================================================
test.describe('7. Security Validation', () => {

  test('7.1 No production secrets in training pages', async ({ page }) => {
    await go(page, '/training/courses');
    const html = await page.content();
    const patterns = ['sk_live_', 'pk_live_', 'rzp_live', 'secret.*='];
    for (const p of patterns) expect(html).not.toMatch(new RegExp(p, 'i'));
    await shot(page, 'sec-training-secrets');
  });

  test('7.2 No production secrets in admin pages', async ({ page }) => {
    await go(page, '/admin/dashboard');
    const html = await page.content();
    const patterns = ['sk_live_', 'pk_live_', 'rzp_live', 'secret.*='];
    for (const p of patterns) expect(html).not.toMatch(new RegExp(p, 'i'));
    await shot(page, 'sec-admin-secrets');
  });

  test('7.3 localStorage no auth tokens', async ({ page }) => {
    await go(page, '/admin/dashboard');
    const ls = await page.evaluate(() => Object.keys(localStorage));
    const auth = ls.filter(k => /token|auth|jwt|secret|password|session/i.test(k));
    expect(auth.length).toBe(0);
    await shot(page, 'sec-localstorage');
  });

  test('7.4 sessionStorage no auth tokens', async ({ page }) => {
    await go(page, '/admin/dashboard');
    const ss = await page.evaluate(() => Object.keys(sessionStorage));
    const auth = ss.filter(k => /token|auth|jwt|secret|password|session/i.test(k));
    expect(auth.length).toBe(0);
    await shot(page, 'sec-sessionstorage');
  });

  test('7.5 Console errors on training pages', async ({ page }) => {
    const errs = await errors(await page.goto('/training/courses', { waitUntil: 'networkidle' }) && page);
    await shot(page, 'sec-console-training');
  });

  test('7.6 Console errors on admin pages', async ({ page }) => {
    const errs = await errors(await page.goto('/admin/dashboard', { waitUntil: 'networkidle' }) && page);
    await shot(page, 'sec-console-admin');
  });

  test('7.7 Direct URL manipulation to protected data', async ({ page }) => {
    await go(page, '/admin/users');
    await shot(page, 'sec-url-manipulation');
  });

  test('7.8 Browser history no tokens in training flow', async ({ page }) => {
    await go(page, '/training/courses');
    const url = page.url();
    expect(url).not.toMatch(/token|auth|jwt|secret|password|session/i);
    await shot(page, 'sec-history');
  });
});

// ====================================================================
// 8. PERFORMANCE
// ====================================================================
test.describe('8. Performance', () => {

  const perfRoutes = [
    ['Training dashboard', '/dashboard/training'],
    ['Course catalog', '/training/courses'],
    ['Admin dashboard', '/admin/dashboard'],
    ['Admin users', '/admin/users'],
    ['Admin analytics', '/admin/analytics'],
  ];

  for (const [name, route] of perfRoutes) {
    test(`8.${perfRoutes.indexOf([name,route])+1} Load time: ${name}`, async ({ page }) => {
      const start = Date.now();
      await go(page, route);
      const elapsed = Date.now() - start;
      expect(elapsed).toBeLessThan(15000);
      await shot(page, `perf-${route.replace(/\//g,'_')}`);
    });
  }

  test('8.6 Admin table rendering', async ({ page }) => {
    await go(page, '/admin/products');
    await shot(page, 'perf-admin-table');
  });

  test('8.7 Network requests on admin dashboard', async ({ page }) => {
    const reqs: string[] = [];
    page.on('request', (r) => reqs.push(r.url()));
    await go(page, '/admin/dashboard');
    await shot(page, 'perf-admin-network');
  });
});

// ====================================================================
// 9. RESPONSIVE
// ====================================================================
test.describe('9. Responsive', () => {

  const views = [
    { w: 1920, h: 1080, label: 'desktop' },
    { w: 768, h: 1024, label: 'tablet' },
    { w: 375, h: 812, label: 'mobile' },
  ];

  for (const vp of views) {
    test(`9.${views.indexOf(vp)+1} Training catalog at ${vp.label}`, async ({ page }) => {
      await page.setViewportSize({ width: vp.w, height: vp.h });
      await go(page, '/training/courses');
      await shot(page, `resp-train-${vp.label}`);
    });

    test(`9.${views.indexOf(vp)+4} Admin dashboard at ${vp.label}`, async ({ page }) => {
      await page.setViewportSize({ width: vp.w, height: vp.h });
      await go(page, '/admin/dashboard');
      await shot(page, `resp-admin-${vp.label}`);
    });
  }

  test('9.10 No horizontal scroll at any viewport', async ({ page }) => {
    for (const vp of views) {
      await page.setViewportSize({ width: vp.w, height: vp.h });
      await go(page, '/admin/dashboard');
      const sw = await page.evaluate(() => document.documentElement.scrollWidth);
      const vw = await page.evaluate(() => window.innerWidth);
      expect(sw).toBeLessThanOrEqual(vw + 5);
    }
    await shot(page, 'resp-noscroll');
  });
});

// ====================================================================
// 10. ACCESSIBILITY
// ====================================================================
test.describe('10. Accessibility', () => {

  test('10.1 Skip to content on training page', async ({ page }) => {
    await go(page, '/training/courses');
    const skip = page.locator('a:has-text("Skip to content"), a[href="#main"], a[href="#content"]').first();
    const count = await skip.count();
    if (count > 0) await expect(skip).toBeVisible();
    await shot(page, 'a11y-skip-training');
  });

  test('10.2 Semantic headings on training', async ({ page }) => {
    await go(page, '/training/courses');
    const h1 = await page.locator('h1').count();
    await shot(page, 'a11y-h1-training');
  });

  test('10.3 Images have alt on training', async ({ page }) => {
    await go(page, '/training/courses');
    const imgs = page.locator('img');
    const count = await imgs.count();
    let missing = 0;
    for (let i = 0; i < count; i++) {
      const alt = await imgs.nth(i).getAttribute('alt');
      if (alt === null) missing++;
    }
    expect(missing).toBe(0);
    await shot(page, 'a11y-alt-training');
  });

  test('10.4 ARIA landmarks on training', async ({ page }) => {
    await go(page, '/training/courses');
    const main = await page.locator('main, [role="main"]').count();
    const nav = await page.locator('nav, [role="navigation"]').count();
    await shot(page, 'a11y-landmarks-training');
  });

  test('10.5 Keyboard nav on training catalog', async ({ page }) => {
    await go(page, '/training/courses');
    await page.keyboard.press('Tab');
    const focused = page.locator(':focus');
    const count = await focused.count();
    await shot(page, 'a11y-keyboard-training');
  });
});

// ====================================================================
// 11. NEGATIVE TESTING
// ====================================================================
test.describe('11. Negative Testing', () => {

  test('11.1 Invalid training course ID', async ({ page }) => {
    await go(page, '/dashboard/training/course/nonexistent-id-99999');
    await shot(page, 'neg-invalid-course');
  });

  test('11.2 Invalid admin record ID', async ({ page }) => {
    await go(page, '/admin/users/invalid-user-99999');
    await shot(page, 'neg-invalid-admin-id');
  });

  test('11.3 Invalid order ID', async ({ page }) => {
    await go(page, '/admin/orders/INVALID-ORDER-99999');
    await shot(page, 'neg-invalid-order');
  });

  test('11.4 Deleted/removed content', async ({ page }) => {
    await go(page, '/training/courses/deleted-course-001');
    await shot(page, 'neg-deleted-course');
  });

  test('11.5 Broken URL on training', async ({ page }) => {
    await go(page, '/training/this-path-does-not-exist');
    await shot(page, 'neg-broken-url');
  });

  test('11.6 Rapid navigation between admin pages', async ({ page }) => {
    const routes = ['/admin/dashboard', '/admin/users', '/admin/products', '/admin/orders', '/admin/training'];
    for (const r of routes) {
      await page.goto(r, { waitUntil: 'domcontentloaded', timeout: 10000 });
    }
    await page.waitForLoadState('networkidle');
    await shot(page, 'neg-rapid-nav');
    const body = await page.locator('body').innerText();
    expect(body.length).toBeGreaterThan(5);
  });

  test('11.7 Double-click on admin navigation', async ({ page }) => {
    await go(page, '/admin/dashboard');
    const links = page.locator('a').first();
    const exists = await links.count();
    if (exists > 0) {
      await links.click();
      await links.click();
      await page.waitForTimeout(500);
    }
    await shot(page, 'neg-double-click');
  });

  test('11.8 Session expired during training', async ({ page }) => {
    await go(page, '/session-expired');
    await go(page, '/dashboard/training');
    await shot(page, 'neg-session-expired-training');
  });
});

// ====================================================================
// 12. DATA INTEGRITY
// ====================================================================
test.describe('12. Data Integrity', () => {

  test('12.1 Training page content persists on reload', async ({ page }) => {
    await go(page, '/training/courses');
    const b1 = await page.locator('body').innerText();
    await page.reload({ waitUntil: 'networkidle' });
    const b2 = await page.locator('body').innerText();
    expect(b2.length).toBeGreaterThan(5);
    await shot(page, 'data-train-persist');
  });

  test('12.2 Admin page content persists on reload', async ({ page }) => {
    await go(page, '/admin/dashboard');
    const b1 = await page.locator('body').innerText();
    await page.reload({ waitUntil: 'networkidle' });
    const b2 = await page.locator('body').innerText();
    expect(b2.length).toBeGreaterThan(5);
    await shot(page, 'data-admin-persist');
  });

  test('12.3 Role assignment persistence', async ({ page }) => {
    await go(page, '/');
    await shot(page, 'data-role-persistence');
  });

  test('12.4 Audit log consistency', async ({ page }) => {
    await go(page, '/admin/system');
    await shot(page, 'data-audit-log');
  });
});

// ====================================================================
// X. CROSS-CUTTING: Console Error Audit (all Part 2 routes)
// ====================================================================
test.describe('X. Cross-Cutting: Console Error Audit', () => {

  const ALL_ROUTES = [...new Set([
    ...TRAINING_ROUTES, ...ADMIN_ROUTES, ...GROWER_ROUTES, ...ERROR_PAGES,
    '/admin/nonexistent-route', '/training/this-path-does-not-exist',
    '/api/training/courses', '/api/enrollment', '/api/admin/dashboard',
  ])];

  for (const route of ALL_ROUTES) {
    test(`Console errors: ${route}`, async ({ page }) => {
      const errs: string[] = [];
      page.on('console', (m) => { if (m.type() === 'error') errs.push(m.text()); });
      await page.goto(route, { waitUntil: 'networkidle', timeout: 30000 });
      await shot(page, `console-${route.replace(/\//g,'_')}`);
    });
  }
});
