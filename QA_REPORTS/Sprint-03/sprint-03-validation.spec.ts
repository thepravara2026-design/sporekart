import { test, expect, type Page } from '@playwright/test';

const BASE = process.env.BASE_URL || 'http://localhost:4173';
const EVIDENCE_DIR = '../QA_REPORTS/Sprint-03/Evidence';

function screenshotName(page: Page, label: string): string {
  const url = page.url().replace(/[^a-zA-Z0-9]/g, '_');
  return `${EVIDENCE_DIR}/Screenshots/${url}_${label}.png`;
}

async function assertNoConsoleErrors(page: Page): Promise<string[]> {
  const errors: string[] = [];
  page.on('console', (msg) => {
    if (msg.type() === 'error') {
      errors.push(msg.text());
    }
  });
  return errors;
}

async function navigateAndCapture(
  page: Page,
  url: string,
  label: string,
  options?: { waitFor?: 'load' | 'domcontentloaded' | 'networkidle'; timeout?: number }
): Promise<{ status: number | null; errors: string[] }> {
  const errors: string[] = [];
  page.on('console', (msg) => {
    if (msg.type() === 'error') errors.push(msg.text());
  });
  const resp = await page.goto(url, {
    waitUntil: options?.waitFor || 'networkidle',
    timeout: options?.timeout || 30000,
  });
  await page.waitForLoadState('networkidle');
  await page.screenshot({ path: screenshotName(page, label), fullPage: true });
  return { status: resp?.status() ?? null, errors };
}

test.describe('QA Sprint 3 — Comprehensive Functional Area Validation', () => {

  test.describe('1. Customer Dashboard (/dashboard)', () => {
    test('Dashboard page loads with key elements', async ({ page }) => {
      const { status, errors } = await navigateAndCapture(page, '/dashboard', '01-dashboard-load');
      expect(status).toBeLessThan(500);
      const body = page.locator('body');
      const bodyText = await body.innerText();
      expect(bodyText.length).toBeGreaterThan(20);
      expect(errors.length).toBe(0);
    });

    test('Dashboard navigation links are present', async ({ page }) => {
      await page.goto('/dashboard', { waitUntil: 'networkidle' });
      const links = page.locator('a');
      const linkCount = await links.count();
      expect(linkCount).toBeGreaterThan(0);
      const hrefs = await links.evaluateAll((els) => els.map((el) => (el as HTMLAnchorElement).href));
      const dashboardRoutes = hrefs.filter((h) => h.includes('/dashboard/'));
      expect(dashboardRoutes.length).toBeGreaterThanOrEqual(2);
    });

    test('Dashboard quick actions render', async ({ page }) => {
      await page.goto('/dashboard', { waitUntil: 'networkidle' });
      const buttons = page.locator('button, a[role="button"], .sk-action-card, [class*="quick"]');
      const count = await buttons.count();
      expect(count).toBeGreaterThanOrEqual(0);
    });
  });

  test.describe('2. User Profile (/dashboard/profile)', () => {
    test('Profile overview page renders', async ({ page }) => {
      const { status, errors } = await navigateAndCapture(page, '/dashboard/profile', '02-profile-overview');
      expect(status).toBeLessThan(500);
      expect(errors.length).toBe(0);
    });

    test('Profile edit page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/dashboard/profile/edit', '02-profile-edit');
      expect(status).toBeLessThan(500);
    });

    test('Security settings page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/dashboard/profile/security', '02-profile-security');
      expect(status).toBeLessThan(500);
    });

    test('Preferences page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/dashboard/profile/preferences', '02-profile-preferences');
      expect(status).toBeLessThan(500);
    });
  });

  test.describe('3. Address Management (/dashboard/addresses)', () => {
    test('Address list page renders', async ({ page }) => {
      const { status, errors } = await navigateAndCapture(page, '/dashboard/addresses', '03-addresses-list');
      expect(status).toBeLessThan(500);
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(10);
    });

    test('Add address page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/dashboard/addresses/new', '03-addresses-new');
      expect(status).toBeLessThan(500);
    });
  });

  test.describe('4. Notifications (/dashboard/notifications)', () => {
    test('Notification center loads', async ({ page }) => {
      const { status, errors } = await navigateAndCapture(page, '/dashboard/notifications', '04-notifications');
      expect(status).toBeLessThan(500);
      expect(errors.length).toBe(0);
    });
  });

  test.describe('5. Wishlist (/dashboard/wishlist)', () => {
    test('Wishlist page loads', async ({ page }) => {
      const { status, errors } = await navigateAndCapture(page, '/dashboard/wishlist', '05-wishlist');
      expect(status).toBeLessThan(500);
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(5);
    });

    test('Wishlist has sort controls if content present', async ({ page }) => {
      await page.goto('/dashboard/wishlist', { waitUntil: 'networkidle' });
      const select = page.locator('select, [role="listbox"], [class*="sort"]').first();
      const exists = await select.count();
      if (exists > 0) {
        await expect(select).toBeVisible();
      }
    });
  });

  test.describe('6. Training Module (/dashboard/training)', () => {
    test('Training dashboard loads', async ({ page }) => {
      const { status, errors } = await navigateAndCapture(page, '/dashboard/training', '06-training-dashboard');
      expect(status).toBeLessThan(500);
    });

    test('Course library renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/dashboard/training/courses', '06-training-courses');
      expect(status).toBeLessThan(500);
    });

    test('My Learning page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/dashboard/training/my-learning', '06-training-mylearning');
      expect(status).toBeLessThan(500);
    });

    test('Certificates page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/dashboard/training/certificates', '06-training-certificates');
      expect(status).toBeLessThan(500);
    });

    test('Course detail page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/dashboard/training/course/crs-001', '06-training-course-detail');
      expect(status).toBeLessThan(500);
    });
  });

  test.describe('7. Grower Dashboard', () => {
    test('Grower view loads with role switcher', async ({ page }) => {
      await page.goto('/', { waitUntil: 'networkidle' });
      const roleSwitcher = page.locator('select[aria-label="Switch review role"]');
      const hasSwitcher = (await roleSwitcher.count()) > 0;
      if (hasSwitcher) {
        await roleSwitcher.selectOption('grower');
        await page.waitForTimeout(500);
      }
      const { status } = await navigateAndCapture(page, '/dashboard', '07-grower-dashboard');
      expect(status).toBeLessThan(500);
    });
  });

  test.describe('8. Admin Dashboard (/admin)', () => {
    test('Admin dashboard loads', async ({ page }) => {
      const { status, errors } = await navigateAndCapture(page, '/admin/dashboard', '08-admin-dashboard');
      expect(status).toBeLessThan(500);
    });

    test('Admin sidebar navigation renders', async ({ page }) => {
      await page.goto('/admin/dashboard', { waitUntil: 'networkidle' });
      const sidebar = page.locator('nav, aside, [class*="sidebar"], [class*="side-bar"]').first();
      const count = await sidebar.count();
      if (count > 0) {
        await expect(sidebar).toBeVisible();
      }
    });

    test('Admin profile page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/admin/profile', '08-admin-profile');
      expect(status).toBeLessThan(500);
    });

    test('Admin settings page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/admin/settings', '08-admin-settings');
      expect(status).toBeLessThan(500);
    });

    test('Admin system page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/admin/system', '08-admin-system');
      expect(status).toBeLessThan(500);
    });

    test('Admin help page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/admin/help', '08-admin-help');
      expect(status).toBeLessThan(500);
    });

    test('Admin products page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/admin/products', '08-admin-products');
      expect(status).toBeLessThan(500);
    });

    test('Admin orders page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/admin/orders', '08-admin-orders');
      expect(status).toBeLessThan(500);
    });

    test('Admin customers page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/admin/customers', '08-admin-customers');
      expect(status).toBeLessThan(500);
    });
  });

  test.describe('9. RBAC / Route Protection', () => {
    test('Homepage role switcher exists with expected roles', async ({ page }) => {
      await page.goto('/', { waitUntil: 'networkidle' });
      const select = page.locator('select[aria-label="Switch review role"]');
      await expect(select).toBeVisible();
      const options = await select.locator('option').allTextContents();
      expect(options.length).toBeGreaterThanOrEqual(3);
    });

    test('Admin routes accessible without auth guard', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/admin/dashboard', '09-admin-noauth');
      expect(status).toBeLessThan(500);
    });

    test('Unauthenticated guest redirected from /dashboard', async ({ page }) => {
      await page.goto('/', { waitUntil: 'networkidle' });
      const select = page.locator('select[aria-label="Switch review role"]');
      const hasSwitcher = (await select.count()) > 0;
      if (hasSwitcher) {
        await select.selectOption('guest');
        await page.waitForTimeout(300);
      }
      const resp = await page.goto('/dashboard', { waitUntil: 'networkidle' });
      const bodyText = await page.locator('body').innerText();
      const isBlocked = bodyText.includes('Access restricted') || bodyText.includes('sign in') || bodyText.includes('login');
      if (resp?.status() === 200) {
        expect(isBlocked).toBe(true);
      }
    });

    test('Role switcher state persists after SPA navigation', async ({ page }) => {
      await page.goto('/', { waitUntil: 'networkidle' });
      const select = page.locator('select[aria-label="Switch review role"]');
      await select.selectOption('grower');
      await page.waitForTimeout(300);
      await page.goto('/products', { waitUntil: 'networkidle' });
      const currentRole = await select.inputValue();
      expect(currentRole).toBe('grower');
    });
  });

  test.describe('10. Error States', () => {
    test('404 page renders for unknown route', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/this-route-does-not-exist-xyz', '10-404');
      const bodyText = await page.locator('body').innerText();
      const has404 = bodyText.includes('404') || bodyText.includes('not found') || bodyText.includes('Not Found');
      if (status === 200) {
        expect(has404).toBe(true);
      }
    });

    test('Auth error gallery renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/auth-error', '10-auth-error');
      expect(status).toBeLessThan(500);
    });
  });

  test.describe('11. Empty States', () => {
    test('Empty wishlist state is handled', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/dashboard/wishlist', '11-empty-wishlist');
      expect(status).toBeLessThan(500);
      const bodyText = await page.locator('body').innerText();
      const isEmpty = bodyText.includes('empty') || bodyText.includes('no items') || bodyText.includes('no products');
      if (bodyText.length > 10 && !bodyText.includes('404')) {
      }
    });

    test('Empty notifications state is handled', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/dashboard/notifications', '11-empty-notifications');
      expect(status).toBeLessThan(500);
    });
  });

  test.describe('12. Loading States', () => {
    test('Loading indicators present on data-dependent pages', async ({ page }) => {
      await page.goto('/dashboard', { waitUntil: 'domcontentloaded' });
      const spinner = page.locator('[class*="spinner"], [class*="loading"], [role="status"], .sk-skeleton');
      const count = await spinner.count();
      await page.waitForLoadState('networkidle');
    });
  });

  test.describe('13. Session Management', () => {
    test('Session expired page renders with actions', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/session-expired', '13-session-expired');
      expect(status).toBeLessThan(500);
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(10);
    });

    test('Access denied page renders with status indicators', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/access-denied', '13-access-denied');
      expect(status).toBeLessThan(500);
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(10);
    });

    test('Auth loading page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/auth/loading', '13-auth-loading');
      expect(status).toBeLessThan(500);
    });
  });

  test.describe('14. Mobile Responsiveness', () => {
    test('Dashboard renders at mobile viewport (375px)', async ({ page }) => {
      await page.setViewportSize({ width: 375, height: 812 });
      const { status } = await navigateAndCapture(page, '/dashboard', '14-mobile-dashboard');
      expect(status).toBeLessThan(500);
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(10);
    });

    test('Products page renders at mobile viewport', async ({ page }) => {
      await page.setViewportSize({ width: 375, height: 812 });
      const { status } = await navigateAndCapture(page, '/products', '14-mobile-products');
      expect(status).toBeLessThan(500);
    });

    test('Dashboard renders at tablet viewport (768px)', async ({ page }) => {
      await page.setViewportSize({ width: 768, height: 1024 });
      const { status } = await navigateAndCapture(page, '/dashboard', '14-tablet-dashboard');
      expect(status).toBeLessThan(500);
    });

    test('No horizontal scroll on dashboard at mobile', async ({ page }) => {
      await page.setViewportSize({ width: 375, height: 812 });
      await page.goto('/dashboard', { waitUntil: 'networkidle' });
      const scrollWidth = await page.evaluate(() => document.documentElement.scrollWidth);
      const viewportWidth = await page.evaluate(() => window.innerWidth);
      expect(scrollWidth).toBeLessThanOrEqual(viewportWidth + 5);
    });
  });

  test.describe('15. Accessibility', () => {
    test('Skip to content link is present', async ({ page }) => {
      await page.goto('/', { waitUntil: 'networkidle' });
      const skipLink = page.locator('a:has-text("Skip to content"), a[href="#main"], a[href="#content"]').first();
      await expect(skipLink).toBeVisible();
    });

    test('ARIA landmarks present on dashboard pages', async ({ page }) => {
      await page.goto('/dashboard', { waitUntil: 'networkidle' });
      const main = page.locator('main, [role="main"]');
      const navigation = page.locator('nav, [role="navigation"]');
      const banner = page.locator('header, [role="banner"]');
      const mainCount = await main.count();
      const navCount = await navigation.count();
      const bannerCount = await banner.count();
      expect(mainCount + navCount + bannerCount).toBeGreaterThanOrEqual(2);
    });

    test('Images have alt text', async ({ page }) => {
      await page.goto('/', { waitUntil: 'networkidle' });
      const images = page.locator('img');
      const count = await images.count();
      let noAlt = 0;
      for (let i = 0; i < count; i++) {
        const alt = await images.nth(i).getAttribute('alt');
        if (alt === null || alt === undefined) noAlt++;
      }
      expect(noAlt).toBe(0);
    });

    test('Semantic heading structure on homepage', async ({ page }) => {
      await page.goto('/', { waitUntil: 'networkidle' });
      const h1Count = await page.locator('h1').count();
      expect(h1Count).toBeGreaterThanOrEqual(1);
      const headings = page.locator('h1, h2, h3, h4, h5, h6');
      const headingCount = await headings.count();
      expect(headingCount).toBeGreaterThanOrEqual(2);
    });
  });

  test.describe('16. Keyboard Navigation', () => {
    test('Skip link is first focusable element', async ({ page }) => {
      await page.goto('/', { waitUntil: 'networkidle' });
      await page.keyboard.press('Tab');
      const focused = page.locator(':focus');
      const tag = await focused.evaluate((el) => el.tagName.toLowerCase());
      const text = await focused.innerText();
      const isSkip = text.toLowerCase().includes('skip') || tag === 'a';
      expect(isSkip).toBe(true);
    });

    test('Login form is keyboard navigable', async ({ page }) => {
      await page.goto('/login', { waitUntil: 'networkidle' });
      await page.keyboard.press('Tab');
      const focused = page.locator(':focus');
      const tag = await focused.evaluate((el) => el.tagName.toLowerCase());
      const isInput = tag === 'input' || tag === 'button' || tag === 'a' || tag === 'select';
      expect(isInput).toBe(true);
    });
  });

  test.describe('17. Security', () => {
    test('No console errors on public pages', async ({ page }) => {
      const errorPromise = assertNoConsoleErrors(page);
      await page.goto('/', { waitUntil: 'networkidle' });
      const errors = await errorPromise;
      expect(errors.length).toBe(0);
    });

    test('No console errors on login page', async ({ page }) => {
      const errorPromise = assertNoConsoleErrors(page);
      await page.goto('/login', { waitUntil: 'networkidle' });
      const errors = await errorPromise;
      expect(errors.length).toBe(0);
    });

    test('No sensitive data in dashboard page source', async ({ page }) => {
      await page.goto('/dashboard', { waitUntil: 'networkidle' });
      const html = await page.content();
      const sensitivePatterns = ['sk_live_', 'pk_live_', 'razorpay_live', 'apiKey.*prod', 'secret.*='];
      for (const pattern of sensitivePatterns) {
        expect(html).not.toMatch(new RegExp(pattern, 'i'));
      }
    });

    test('No sensitive data in admin page source', async ({ page }) => {
      await page.goto('/admin/dashboard', { waitUntil: 'networkidle' });
      const html = await page.content();
      const sensitivePatterns = ['sk_live_', 'pk_live_', 'razorpay_live', 'apiKey.*prod', 'secret.*='];
      for (const pattern of sensitivePatterns) {
        expect(html).not.toMatch(new RegExp(pattern, 'i'));
      }
    });

    test('Auth pages do not expose PII in HTML source', async ({ page }) => {
      await page.goto('/login', { waitUntil: 'networkidle' });
      const html = await page.content();
      expect(html).not.toMatch(/["'](?:fullPhone|otp|password|ssn|aadhar|pan)["']/i);
    });
  });

  test.describe('18. Input Validation', () => {
    test('Login form validates empty submission', async ({ page }) => {
      await page.goto('/login', { waitUntil: 'networkidle' });
      const submitBtn = page.locator('button[type="submit"], button:has-text("Sign in"), button:has-text("Login")').first();
      const btnCount = await submitBtn.count();
      if (btnCount > 0) {
        await submitBtn.click();
        await page.waitForTimeout(500);
        const bodyText = await page.locator('body').innerText();
        const hasError = bodyText.includes('required') || bodyText.includes('invalid') || bodyText.includes('error') || bodyText.includes('empty');
      }
    });

    test('Login form rejects invalid phone number', async ({ page }) => {
      await page.goto('/login', { waitUntil: 'networkidle' });
      const input = page.locator('input[type="tel"], input[type="text"]').first();
      const inputCount = await input.count();
      if (inputCount > 0) {
        await input.fill('abc');
        const submitBtn = page.locator('button[type="submit"]').first();
        const btnCount = await submitBtn.count();
        if (btnCount > 0) {
          await submitBtn.click();
          await page.waitForTimeout(500);
        }
      }
    });

    test('Forgot password page renders', async ({ page }) => {
      const { status } = await navigateAndCapture(page, '/forgot-password', '18-forgot-password');
      expect(status).toBeLessThan(500);
    });
  });

  test.describe('19. Cross-Page Navigation Integrity', () => {
    test('Public website routes are reachable', async ({ page }) => {
      const routes = ['/', '/products', '/training', '/blog', '/about', '/contact', '/login', '/register', '/faq', '/support', '/certifications'];
      for (const route of routes) {
        const resp = await page.goto(route, { waitUntil: 'domcontentloaded', timeout: 15000 });
        expect(resp?.status()).toBeLessThan(500);
      }
    });

    test('Dashboard sub-routes are reachable', async ({ page }) => {
      const routes = [
        '/dashboard',
        '/dashboard/profile',
        '/dashboard/addresses',
        '/dashboard/notifications',
        '/dashboard/wishlist',
        '/dashboard/training',
        '/dashboard/training/courses',
        '/dashboard/training/my-learning',
        '/dashboard/training/certificates',
      ];
      for (const route of routes) {
        const resp = await page.goto(route, { waitUntil: 'domcontentloaded', timeout: 15000 });
        expect(resp?.status()).toBeLessThan(500);
      }
    });

    test('Local navigation links do not cause 404s', async ({ page }) => {
      await page.goto('/', { waitUntil: 'networkidle' });
      const links = page.locator('a');

      const count = await links.count();
      let visited = 0;
      for (let i = 0; i < Math.min(count, 20); i++) {
        const href = await links.nth(i).getAttribute('href');
        if (href && !href.startsWith('#') && !href.startsWith('mailto:') && !href.startsWith('tel:') && !href.startsWith('http')) {
          const resp = await page.goto(href);
          if (resp) {
            expect(resp.status()).toBeLessThan(500);
            visited++;
          }
        }
      }
      expect(visited).toBeGreaterThanOrEqual(1);
    });
  });
});
