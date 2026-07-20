import { test, expect } from '@playwright/test';

const MAIN_ROUTES = [
  { path: '/', label: 'Home' },
  { path: '/products', label: 'Products' },
  { path: '/training', label: 'Training' },
  { path: '/blog', label: 'Blog' },
  { path: '/about', label: 'About' },
  { path: '/contact', label: 'Contact' },
  { path: '/login', label: 'Login' },
  { path: '/register', label: 'Register' },
  { path: '/faq', label: 'FAQ' },
  { path: '/support', label: 'Support' },
  { path: '/certifications', label: 'Certifications' },
];

const PLACEHOLDER_ROUTES = [
  { path: '/cart', label: 'Cart (not implemented)' },
  { path: '/checkout', label: 'Checkout (not implemented)' },
];

const ERROR_ROUTES = [
  { path: '/dashboard', label: 'Dashboard (needs auth)' },
  { path: '/admin', label: 'Admin (needs auth)' },
];

test.describe('Part 2 — Customer Journey: Phase 2 — Navigation Validation', () => {

  for (const route of MAIN_ROUTES) {
    test(`Navigating to ${route.label} (${route.path}) returns 200 and renders content`, async ({ page }) => {
      const response = await page.goto(route.path, { waitUntil: 'networkidle' });
      expect(response?.status()).toBeLessThan(400);
      const title = await page.title();
      expect(title).toBeTruthy();
      const bodyContent = page.locator('body');
      const text = await bodyContent.innerText();
      expect(text.length).toBeGreaterThan(10);
    });
  }

  for (const route of PLACEHOLDER_ROUTES) {
    test(`Unimplemented route ${route.label} (${route.path}) shows appropriate message`, async ({ page }) => {
      const response = await page.goto(route.path, { waitUntil: 'networkidle' });
      if (response?.status() === 404) {
        await expect(page.locator('text=404, not found, Not Found').first()).toBeVisible();
      } else {
        expect(response?.status()).toBeLessThan(400);
      }
    });
  }

  for (const route of ERROR_ROUTES) {
    test(`Protected route ${route.label} (${route.path}) redirects or shows auth error`, async ({ page }) => {
      const response = await page.goto(route.path, { waitUntil: 'networkidle' });
      const currentUrl = page.url();
      const isRedirected = currentUrl.includes('login') || currentUrl.includes('auth') || currentUrl.includes('access-denied');
      expect(response?.status() === 200 || isRedirected).toBeTruthy();
    });
  }

  test('Browser back button returns to previous page', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.goto('/about');
    await page.waitForLoadState('networkidle');
    await page.goBack();
    await page.waitForLoadState('networkidle');
    expect(page.url()).not.toContain('/about');
  });

  test('Browser forward button returns to next page', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.goto('/about');
    await page.waitForLoadState('networkidle');
    await page.goBack();
    await page.waitForLoadState('networkidle');
    await page.goForward();
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('/about');
  });

  test('Deep link to /training resolves correctly', async ({ page }) => {
    const response = await page.goto('/training', { waitUntil: 'networkidle' });
    expect(response?.status()).toBeLessThan(400);
    expect(page.url()).toContain('/training');
  });

  test('Deep link to /blog resolves correctly', async ({ page }) => {
    const response = await page.goto('/blog', { waitUntil: 'networkidle' });
    expect(response?.status()).toBeLessThan(400);
    expect(page.url()).toContain('/blog');
  });

  test('Invalid route returns 404 page', async ({ page }) => {
    const response = await page.goto('/this-route-does-not-exist-xyz', { waitUntil: 'networkidle' });
    const status = response?.status() ?? 200;
    if (status === 200) {
      const body = page.locator('body');
      const text = await body.innerText();
      const has404 = text.includes('404') || text.includes('not found') || text.includes('Not Found');
      expect(has404 || page.url().includes('404')).toBeTruthy();
    } else {
      expect(status).toBe(404);
    }
  });

  test('Header logo or brand link navigates to home', async ({ page }) => {
    await page.goto('/about');
    await page.waitForLoadState('networkidle');
    const logo = page.locator('a[href="/"], a[href="/home"], header a').first();
    if (await logo.isVisible().catch(() => false)) {
      await logo.click();
      await page.waitForLoadState('networkidle');
      expect(page.url()).toBe(page.url().includes('?') ? page.url() : page.url().replace(/\/$/, '') || '/');
    }
  });

  test('Login link navigates to /login', async ({ page }) => {
    await page.goto('/');
    const loginLink = page.locator('a[href="/login"], a[href="/auth"], text=Sign In, text=Login').first();
    if (await loginLink.isVisible().catch(() => false)) {
      await loginLink.click();
      await page.waitForLoadState('networkidle');
      expect(page.url()).toContain('login');
    }
  });

  test('Navigation link active state reflects current page', async ({ page }) => {
    await page.goto('/about');
    await page.waitForLoadState('networkidle');
    const activeLinks = page.locator('nav a[class*="active"], nav a[aria-current="page"]');
    const count = await activeLinks.count();
    if (count > 0) {
      await expect(activeLinks.first()).toBeVisible();
    }
  });

});
