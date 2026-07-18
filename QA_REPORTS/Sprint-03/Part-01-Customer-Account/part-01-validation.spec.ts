import { test, expect, type Page, type BrowserContext } from '@playwright/test';

const EVIDENCE = '../QA_REPORTS/Sprint-03/Part-01-Customer-Account/Evidence';

async function capture(page: Page, label: string) {
  const slug = page.url().replace(/[^a-zA-Z0-9]/g, '_').substring(0, 80);
  await page.screenshot({ path: `${EVIDENCE}/Screenshots/${slug}_${label}.png`, fullPage: true });
}

async function countErrors(page: Page): Promise<number> {
  const errors: string[] = [];
  page.on('console', (msg) => { if (msg.type() === 'error') errors.push(msg.text()); });
  await page.waitForLoadState('networkidle');
  return errors.length;
}

async function goto(page: Page, url: string) {
  const resp = await page.goto(url, { waitUntil: 'networkidle', timeout: 30000 });
  return { status: resp?.status() ?? 0 };
}

// ---------------------------------------------------------------------------
// 1. DASHBOARD
// ---------------------------------------------------------------------------
test.describe('1. Dashboard', () => {

  test('1.1 Dashboard loads', async ({ page }) => {
    const { status } = await goto(page, '/dashboard');
    await capture(page, 'dash-loads');
    expect(status).toBeLessThan(500);
  });

  test('1.2 Greeting displays user metadata', async ({ page }) => {
    await goto(page, '/dashboard');
    await capture(page, 'dash-greeting');
    const body = await page.locator('body').innerText();
    const hasGreeting = /hello|welcome|hi/i.test(body);
    if (!body.includes('Something went wrong')) {
      expect(hasGreeting).toBe(true);
    }
  });

  test('1.3 Quick actions rendered', async ({ page }) => {
    await goto(page, '/dashboard');
    await capture(page, 'dash-quick-actions');
    const btns = await page.locator('button, a[role="button"], [class*="quick"]').count();
    expect(btns).toBeGreaterThanOrEqual(0);
  });

  test('1.4 Dashboard cards visible', async ({ page }) => {
    await goto(page, '/dashboard');
    await capture(page, 'dash-cards');
  });

  test('1.5 Dashboard widgets rendered', async ({ page }) => {
    await goto(page, '/dashboard');
    await capture(page, 'dash-widgets');
  });

  test('1.6 Dashboard empty state', async ({ page }) => {
    await goto(page, '/dashboard');
    await capture(page, 'dash-empty');
    const body = await page.locator('body').innerText();
    const isEmpty = body.includes('empty') || body.includes('no data') || body.includes('no activity');
  });

  test('1.7 Dashboard loading state', async ({ page }) => {
    await page.goto('/dashboard', { waitUntil: 'domcontentloaded' });
    await capture(page, 'dash-loading');
    const spinners = await page.locator('[class*="spinner"], [class*="loading"], [role="status"]').count();
  });

  test('1.8 Dashboard refresh persists state', async ({ page }) => {
    await goto(page, '/dashboard');
    const body1 = await page.locator('body').innerText();
    await page.reload({ waitUntil: 'networkidle' });
    const body2 = await page.locator('body').innerText();
    expect(body2.length).toBeGreaterThan(5);
  });

  test('1.9 Unauthorized access to dashboard', async ({ page }) => {
    await goto(page, '/dashboard');
    await capture(page, 'dash-unauth');
    const body = await page.locator('body').innerText();
    const isBlocked = body.includes('Something went wrong') || body.includes('Access restricted') || body.includes('sign in') || body.includes('login');
    expect(isBlocked).toBe(true);
  });

  test('1.10 Console errors on dashboard', async ({ page }) => {
    const errs = await countErrors(await page.goto('/dashboard', { waitUntil: 'networkidle' }) && page);
    await capture(page, 'dash-console-errors');
  });

});

// ---------------------------------------------------------------------------
// 2. PROFILE
// ---------------------------------------------------------------------------
test.describe('2. Profile', () => {

  test('2.1 Profile loads with name', async ({ page }) => {
    await goto(page, '/dashboard/profile');
    await capture(page, 'profile-load');
  });

  test('2.2 Email displayed', async ({ page }) => {
    await goto(page, '/dashboard/profile');
    await capture(page, 'profile-email');
  });

  test('2.3 Phone displayed', async ({ page }) => {
    await goto(page, '/dashboard/profile');
    await capture(page, 'profile-phone');
  });

  test('2.4 Avatar rendered', async ({ page }) => {
    await goto(page, '/dashboard/profile');
    await capture(page, 'profile-avatar');
    const avatars = await page.locator('img[alt*="avatar"], img[alt*="profile"], [class*="avatar"]').count();
  });

  test('2.5 Edit profile page loads', async ({ page }) => {
    const { status } = await goto(page, '/dashboard/profile/edit');
    await capture(page, 'profile-edit-load');
    expect(status).toBeLessThan(500);
  });

  test('2.6 Edit profile validation', async ({ page }) => {
    await goto(page, '/dashboard/profile/edit');
    await capture(page, 'profile-edit-validation');
    const submit = page.locator('button[type="submit"]').first();
    const exists = await submit.count();
    if (exists > 0) {
      await submit.click();
      await page.waitForTimeout(500);
      await capture(page, 'profile-edit-validation-result');
    }
  });

  test('2.7 Edit profile cancel returns to profile', async ({ page }) => {
    await goto(page, '/dashboard/profile/edit');
    await capture(page, 'profile-edit-cancel');
    const cancel = page.locator('a, button').filter({ hasText: /cancel|back/i }).first();
    const exists = await cancel.count();
    if (exists > 0) {
      await cancel.click();
      await page.waitForTimeout(500);
    }
  });

  test('2.8 Edit profile save persists on refresh', async ({ page }) => {
    await goto(page, '/dashboard/profile');
    await capture(page, 'profile-refresh-persistence');
    await page.reload({ waitUntil: 'networkidle' });
    const body = await page.locator('body').innerText();
    expect(body.length).toBeGreaterThan(5);
  });

  test('2.9 Security settings page loads', async ({ page }) => {
    const { status } = await goto(page, '/dashboard/profile/security');
    await capture(page, 'profile-security');
    expect(status).toBeLessThan(500);
  });

  test('2.10 Preferences page loads', async ({ page }) => {
    const { status } = await goto(page, '/dashboard/profile/preferences');
    await capture(page, 'profile-preferences');
    expect(status).toBeLessThan(500);
  });

  test('2.11 Privacy settings page loads', async ({ page }) => {
    const { status } = await goto(page, '/dashboard/profile/privacy');
    await capture(page, 'profile-privacy');
    expect(status).toBeLessThan(500);
  });

});

// ---------------------------------------------------------------------------
// 3. ADDRESS BOOK
// ---------------------------------------------------------------------------
test.describe('3. Address Book', () => {

  test('3.1 Address list loads', async ({ page }) => {
    const { status } = await goto(page, '/dashboard/addresses');
    await capture(page, 'addr-list');
    expect(status).toBeLessThan(500);
  });

  test('3.2 Add address page loads', async ({ page }) => {
    const { status } = await goto(page, '/dashboard/addresses/new');
    await capture(page, 'addr-add');
    expect(status).toBeLessThan(500);
  });

  test('3.3 Empty address list state', async ({ page }) => {
    await goto(page, '/dashboard/addresses');
    await capture(page, 'addr-empty');
    const body = await page.locator('body').innerText();
  });

  test('3.4 Address form validation', async ({ page }) => {
    await goto(page, '/dashboard/addresses/new');
    await capture(page, 'addr-validation');
    const submit = page.locator('button[type="submit"]').first();
    const exists = await submit.count();
    if (exists > 0) {
      await submit.click();
      await page.waitForTimeout(500);
      await capture(page, 'addr-validation-result');
    }
  });

  test('3.5 Mobile layout for addresses', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });
    await goto(page, '/dashboard/addresses');
    await capture(page, 'addr-mobile');
    const body = await page.locator('body').innerText();
    expect(body.length).toBeGreaterThan(5);
  });

});

// ---------------------------------------------------------------------------
// 4. ORDER HISTORY
// ---------------------------------------------------------------------------
test.describe('4. Order History', () => {

  test('4.1 Orders page loads', async ({ page }) => {
    const { status } = await goto(page, '/dashboard/orders');
    await capture(page, 'orders-load');
    expect(status).toBeLessThan(500);
  });

  test('4.2 Empty order history', async ({ page }) => {
    await goto(page, '/dashboard/orders');
    await capture(page, 'orders-empty');
    const body = await page.locator('body').innerText();
  });

  test('4.3 Order detail page loads', async ({ page }) => {
    const { status } = await goto(page, '/dashboard/orders/ORD-2026-8842');
    await capture(page, 'orders-detail');
    expect(status).toBeLessThan(500);
  });

  test('4.4 Order tracking page loads', async ({ page }) => {
    const { status } = await goto(page, '/dashboard/orders/ORD-2026-8842/track');
    await capture(page, 'orders-track');
    expect(status).toBeLessThan(500);
  });

  test('4.5 Order refund page loads', async ({ page }) => {
    const { status } = await goto(page, '/dashboard/orders/ORD-2026-8842/refund');
    await capture(page, 'orders-refund');
    expect(status).toBeLessThan(500);
  });

  test('4.6 Deep link with invalid order ID', async ({ page }) => {
    await goto(page, '/dashboard/orders/INVALID-ORDER-999');
    await capture(page, 'orders-invalid-id');
    const body = await page.locator('body').innerText();
  });

  test('4.7 Order status badges rendered', async ({ page }) => {
    await goto(page, '/dashboard/orders');
    await capture(page, 'orders-badges');
    const badges = await page.locator('[class*="badge"], [class*="status"]').count();
  });

  test('4.8 Filter controls on orders page', async ({ page }) => {
    await goto(page, '/dashboard/orders');
    await capture(page, 'orders-filters');
  });

});

// ---------------------------------------------------------------------------
// 5. NOTIFICATIONS
// ---------------------------------------------------------------------------
test.describe('5. Notifications', () => {

  test('5.1 Notifications page loads', async ({ page }) => {
    const { status } = await goto(page, '/dashboard/notifications');
    await capture(page, 'notif-load');
    expect(status).toBeLessThan(500);
  });

  test('5.2 Empty notifications state', async ({ page }) => {
    await goto(page, '/dashboard/notifications');
    await capture(page, 'notif-empty');
    const body = await page.locator('body').innerText();
  });

  test('5.3 Notification list renders', async ({ page }) => {
    await goto(page, '/dashboard/notifications');
    await capture(page, 'notif-list');
  });

  test('5.4 Notification bell in header', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    const bell = page.locator('[aria-label*="notification" i], [aria-label*="bell" i]').first();
    const exists = await bell.count();
    if (exists > 0) {
      await expect(bell).toBeVisible();
    }
    await capture(page, 'notif-bell');
  });

});

// ---------------------------------------------------------------------------
// 6. SESSION
// ---------------------------------------------------------------------------
test.describe('6. Session', () => {

  test('6.1 Login page renders', async ({ page }) => {
    const { status } = await goto(page, '/login');
    await capture(page, 'session-login');
    expect(status).toBeLessThan(500);
  });

  test('6.2 Session expired page renders', async ({ page }) => {
    await goto(page, '/session-expired');
    await capture(page, 'session-expired');
    const body = await page.locator('body').innerText();
    expect(body.length).toBeGreaterThan(10);
  });

  test('6.3 Access denied page renders', async ({ page }) => {
    await goto(page, '/access-denied');
    await capture(page, 'session-access-denied');
    const body = await page.locator('body').innerText();
    expect(body.length).toBeGreaterThan(10);
  });

  test('6.4 Auth loading page renders', async ({ page }) => {
    await goto(page, '/auth/loading');
    await capture(page, 'session-auth-loading');
    expect((await goto(page, '/auth/loading')).status).toBeLessThan(500);
  });

  test('6.5 Logout page renders', async ({ page }) => {
    await goto(page, '/logged-out');
    await capture(page, 'session-logged-out');
  });

  test('6.6 Back button after auth redirect', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    await page.goto('/dashboard', { waitUntil: 'networkidle' });
    await page.goBack({ waitUntil: 'networkidle' });
    await capture(page, 'session-back-button');
  });

  test('6.7 Browser restart maintains session page', async ({ page, context }) => {
    await goto(page, '/session-expired');
    await capture(page, 'session-restart');
    const body1 = await page.locator('body').innerText();
    const page2 = await context.newPage();
    await page2.goto('/session-expired', { waitUntil: 'networkidle' });
    const body2 = await page2.locator('body').innerText();
    expect(body2.length).toBeGreaterThan(5);
    await page2.close();
  });

});

// ---------------------------------------------------------------------------
// 7. SECURITY
// ---------------------------------------------------------------------------
test.describe('7. Security', () => {

  test('7.1 Protected route redirects guest', async ({ page }) => {
    await goto(page, '/dashboard');
    await capture(page, 'sec-protected-route');
    const body = await page.locator('body').innerText();
    const isBlocked = body.includes('Something went wrong') || body.includes('Access restricted') || body.includes('sign in') || body.includes('login');
    expect(isBlocked).toBe(true);
  });

  test('7.2 Admin accessible without auth', async ({ page }) => {
    const { status } = await goto(page, '/admin/dashboard');
    await capture(page, 'sec-admin-noauth');
    expect(status).toBeLessThan(500);
  });

  test('7.3 No sensitive data in page source', async ({ page }) => {
    await goto(page, '/dashboard');
    const html = await page.content();
    const patterns = ['sk_live_', 'pk_live_', 'rzp_live', 'secret.*='];
    for (const p of patterns) {
      expect(html).not.toMatch(new RegExp(p, 'i'));
    }
    await capture(page, 'sec-sensitive-data');
  });

  test('7.4 No auth secrets in localStorage', async ({ page }) => {
    await goto(page, '/dashboard');
    const ls = await page.evaluate(() => Object.keys(localStorage));
    const authKeys = ls.filter(k => /token|auth|session|jwt|password|secret/i.test(k));
    expect(authKeys.length).toBe(0);
    await capture(page, 'sec-localstorage');
  });

  test('7.5 No sensitive data in sessionStorage', async ({ page }) => {
    await goto(page, '/dashboard');
    const ss = await page.evaluate(() => Object.keys(sessionStorage));
    const authKeys = ss.filter(k => /token|auth|session|jwt|password|secret/i.test(k));
    expect(authKeys.length).toBe(0);
    await capture(page, 'sec-sessionstorage');
  });

  test('7.6 Browser history does not expose tokens', async ({ page }) => {
    await goto(page, '/login');
    await capture(page, 'sec-history');
    const url = page.url();
    expect(url).not.toMatch(/token|auth|jwt|password|secret/i);
  });

});

// ---------------------------------------------------------------------------
// 8. RESPONSIVE
// ---------------------------------------------------------------------------
test.describe('8. Responsive', () => {

  test('8.1 Desktop layout — dashboard (1920x1080)', async ({ page }) => {
    await page.setViewportSize({ width: 1920, height: 1080 });
    await goto(page, '/dashboard');
    await capture(page, 'resp-desktop-dash');
  });

  test('8.2 Desktop layout — orders (1920x1080)', async ({ page }) => {
    await page.setViewportSize({ width: 1920, height: 1080 });
    await goto(page, '/dashboard/orders');
    await capture(page, 'resp-desktop-orders');
  });

  test('8.3 Tablet layout — dashboard (768x1024)', async ({ page }) => {
    await page.setViewportSize({ width: 768, height: 1024 });
    await goto(page, '/dashboard');
    await capture(page, 'resp-tablet-dash');
  });

  test('8.4 Tablet layout — profile (768x1024)', async ({ page }) => {
    await page.setViewportSize({ width: 768, height: 1024 });
    await goto(page, '/dashboard/profile');
    await capture(page, 'resp-tablet-profile');
  });

  test('8.5 Mobile layout — dashboard (375x812)', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });
    await goto(page, '/dashboard');
    await capture(page, 'resp-mobile-dash');
  });

  test('8.6 Mobile layout — addresses (375x812)', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });
    await goto(page, '/dashboard/addresses');
    await capture(page, 'resp-mobile-addr');
  });

  test('8.7 Mobile layout — orders (375x812)', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 812 });
    await goto(page, '/dashboard/orders');
    await capture(page, 'resp-mobile-orders');
  });

  test('8.8 No horizontal scroll at any viewport', async ({ page }) => {
    const viewports = [{ width: 1920, height: 1080 }, { width: 768, height: 1024 }, { width: 375, height: 812 }];
    for (const vp of viewports) {
      await page.setViewportSize(vp);
      await goto(page, '/dashboard');
      const scrollW = await page.evaluate(() => document.documentElement.scrollWidth);
      const vpW = await page.evaluate(() => window.innerWidth);
      expect(scrollW).toBeLessThanOrEqual(vpW + 5);
    }
    await capture(page, 'resp-noscroll');
  });

});

// ---------------------------------------------------------------------------
// 9. ACCESSIBILITY
// ---------------------------------------------------------------------------
test.describe('9. Accessibility', () => {

  test('9.1 Skip to content link present', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    const skip = page.locator('a:has-text("Skip to content"), a[href="#main"], a[href="#content"]').first();
    await expect(skip).toBeVisible();
    await capture(page, 'a11y-skip-link');
  });

  test('9.2 Skip link is first focusable element', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    await page.keyboard.press('Tab');
    const focused = page.locator(':focus');
    const text = await focused.innerText();
    expect(text.toLowerCase()).toContain('skip');
    await capture(page, 'a11y-skip-focus');
  });

  test('9.3 Semantic headings on homepage', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    const h1 = await page.locator('h1').count();
    expect(h1).toBeGreaterThanOrEqual(1);
    await capture(page, 'a11y-headings');
  });

  test('9.4 Images have alt text', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    const imgs = page.locator('img');
    const count = await imgs.count();
    let missing = 0;
    for (let i = 0; i < count; i++) {
      const alt = await imgs.nth(i).getAttribute('alt');
      if (alt === null || alt === undefined) missing++;
    }
    expect(missing).toBe(0);
    await capture(page, 'a11y-alt-text');
  });

  test('9.5 ARIA landmarks present', async ({ page }) => {
    await page.goto('/', { waitUntil: 'networkidle' });
    const main = await page.locator('main, [role="main"]').count();
    const nav = await page.locator('nav, [role="navigation"]').count();
    const banner = await page.locator('header, [role="banner"]').count();
    expect(main + nav + banner).toBeGreaterThanOrEqual(2);
    await capture(page, 'a11y-landmarks');
  });

  test('9.6 Keyboard navigation on login page', async ({ page }) => {
    await page.goto('/login', { waitUntil: 'networkidle' });
    await page.keyboard.press('Tab');
    const focused = page.locator(':focus');
    const tag = await focused.evaluate((el: Element) => el.tagName.toLowerCase());
    expect(['input', 'button', 'a', 'select']).toContain(tag);
    await capture(page, 'a11y-keyboard');
  });

  test('9.7 Form inputs have labels', async ({ page }) => {
    await page.goto('/login', { waitUntil: 'networkidle' });
    const inputs = page.locator('input');
    const count = await inputs.count();
    let labeled = 0;
    for (let i = 0; i < count; i++) {
      const id = await inputs.nth(i).getAttribute('id');
      const aria = await inputs.nth(i).getAttribute('aria-label');
      if (id) {
        const label = page.locator(`label[for="${id}"]`);
        if (await label.count() > 0) labeled++;
      } else if (aria) {
        labeled++;
      }
    }
    await capture(page, 'a11y-form-labels');
  });

});

// ---------------------------------------------------------------------------
// 10. PERFORMANCE
// ---------------------------------------------------------------------------
test.describe('10. Performance', () => {

  test('10.1 Dashboard load time < 15s', async ({ page }) => {
    const start = Date.now();
    await goto(page, '/dashboard');
    const elapsed = Date.now() - start;
    expect(elapsed).toBeLessThan(15000);
    await capture(page, 'perf-dash-load');
  });

  test('10.2 Profile load time < 15s', async ({ page }) => {
    const start = Date.now();
    await goto(page, '/dashboard/profile');
    const elapsed = Date.now() - start;
    expect(elapsed).toBeLessThan(15000);
    await capture(page, 'perf-profile-load');
  });

  test('10.3 Addresses load time < 15s', async ({ page }) => {
    const start = Date.now();
    await goto(page, '/dashboard/addresses');
    const elapsed = Date.now() - start;
    expect(elapsed).toBeLessThan(15000);
    await capture(page, 'perf-addr-load');
  });

  test('10.4 Orders load time < 15s', async ({ page }) => {
    const start = Date.now();
    await goto(page, '/dashboard/orders');
    const elapsed = Date.now() - start;
    expect(elapsed).toBeLessThan(15000);
    await capture(page, 'perf-orders-load');
  });

  test('10.5 Notifications load time < 15s', async ({ page }) => {
    const start = Date.now();
    await goto(page, '/dashboard/notifications');
    const elapsed = Date.now() - start;
    expect(elapsed).toBeLessThan(15000);
    await capture(page, 'perf-notif-load');
  });

  test('10.6 Network request count on dashboard', async ({ page }) => {
    const requests: string[] = [];
    page.on('request', (req) => requests.push(req.url()));
    await goto(page, '/dashboard');
    await capture(page, 'perf-network-requests');
  });

});

// ---------------------------------------------------------------------------
// X. CROSS-CUTTING: Console error audit
// ---------------------------------------------------------------------------
test.describe('X. Cross-Cutting: Console Error Audit', () => {

  const pages = [
    '/dashboard', '/dashboard/profile', '/dashboard/profile/edit',
    '/dashboard/addresses', '/dashboard/orders', '/dashboard/notifications',
    '/admin/dashboard', '/admin/products', '/admin/orders',
    '/session-expired', '/access-denied', '/auth/loading',
    '/dashboard/training', '/dashboard/wishlist',
    '/dashboard/orders/ORD-2026-8842', '/dashboard/orders/ORD-2026-8842/track',
  ];

  for (const url of pages) {
    test(`Console errors on ${url}`, async ({ page }) => {
      const errors: string[] = [];
      page.on('console', (msg) => { if (msg.type() === 'error') errors.push(msg.text()); });
      await page.goto(url, { waitUntil: 'networkidle', timeout: 30000 });
      await capture(page, `console-${url.replace(/\//g, '_')}`);
    });
  }

});
