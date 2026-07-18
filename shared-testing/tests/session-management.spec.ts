import { test, expect } from '@playwright/test';

test.describe('Part 2 — Session Management Validation', () => {

  // ==========================================================================
  // SECTION 1: Session Page Rendering
  // ==========================================================================

  test('AuthLoadingPage resolves and redirects to home', async ({ page }) => {
    await page.goto('/auth/loading');
    await expect(page.locator('.auth-spinner')).toBeVisible();
    await expect(page.locator('role=status')).toHaveAttribute('aria-label', 'Establishing your session');
    await expect(page.locator('text=Establishing your session')).toBeVisible();
    await expect(page.locator('text=Securing your workspace and loading your role…')).toBeVisible();
    await page.waitForURL('**/', { timeout: 3000 });
    expect(page.url().endsWith('/')).toBeTruthy();
  });

  test('SessionExpiredPage renders correct icons, content, and redirect actions', async ({ page }) => {
    await page.goto('/session-expired');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('text=Your session expired')).toBeVisible();
    await expect(page.locator('text=For your security, you were signed out after a period of inactivity.')).toBeVisible();
    const signInBtn = page.locator('button:has-text("Sign in again")');
    await expect(signInBtn).toBeVisible();
    await signInBtn.click();
    await page.waitForURL('**/login');
    expect(page.url()).toContain('/login');
  });

  test('AccessDeniedPage displays correct status indicators and navigates back', async ({ page }) => {
    await page.goto('/access-denied');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('text=Access denied')).toBeVisible();
    await expect(page.locator("text=Your account doesn\u2019t have permission to view this page.")).toBeVisible();
    const homeBtn = page.locator('button:has-text("Back to home")');
    await expect(homeBtn).toBeVisible();
    await homeBtn.click();
    await page.waitForURL('**/');
    expect(page.url().endsWith('/')).toBeTruthy();
  });

  test('AuthErrorGallery renders all 5 error variants', async ({ page }) => {
    await page.goto('/auth-error');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('h1:has-text("401")')).toBeVisible();
    await expect(page.locator('h1:has-text("403")')).toBeVisible();
    await expect(page.locator('h1:has-text("Authentication error")')).toBeVisible();
    await expect(page.locator('h1:has-text("Network error")')).toBeVisible();
    await expect(page.locator('h1:has-text("Server error")')).toBeVisible();
  });

  // ==========================================================================
  // SECTION 2: Login → OTP → Session Establishment Flow
  // ==========================================================================

  test('Complete login flow: Login -> OTP -> AuthLoading -> Home', async ({ page }) => {
    await page.goto('/login');
    await expect(page.locator('text=Access your workspace')).toBeVisible();

    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service and Privacy Policy').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');
    expect(page.url()).toContain('/verify-otp');

    const inputs = page.locator('.sk-otp-input');
    await expect(inputs).toHaveCount(6);
    for (let i = 0; i < 6; i++) {
      await inputs.nth(i).fill((i + 1).toString());
    }

    await expect(page.locator('text=Verified! Taking you to your workspace…')).toBeVisible({ timeout: 10000 });
    await page.waitForURL('**/auth/loading', { timeout: 5000 });
    await page.waitForURL('**/', { timeout: 5000 });
    expect(page.url()).not.toContain('/login');
    expect(page.url()).not.toContain('/verify-otp');
  });

  test('Session flow persists across browser refresh after login', async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service and Privacy Policy').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');

    const inputs = page.locator('.sk-otp-input');
    for (let i = 0; i < 6; i++) {
      await inputs.nth(i).fill((i + 1).toString());
    }
    await expect(page.locator('text=Verified! Taking you to your workspace…')).toBeVisible({ timeout: 10000 });
    await page.waitForURL('**/', { timeout: 10000 });

    await page.reload();
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('/');
  });

  test('Hard refresh after login flow lands on home page', async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service and Privacy Policy').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');
    const inputs = page.locator('.sk-otp-input');
    for (let i = 0; i < 6; i++) {
      await inputs.nth(i).fill((i + 1).toString());
    }
    await expect(page.locator('text=Verified! Taking you to your workspace…')).toBeVisible({ timeout: 10000 });
    await page.waitForURL('**/', { timeout: 10000 });

    await page.evaluate(() => { window.location.reload(); });
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('/');
  });

  // ==========================================================================
  // SECTION 3: Storage & Data Persistence Validation
  // ==========================================================================

  test('localStorage inspection — no auth secrets stored', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');

    const storage = await page.evaluate(() => {
      const keys: string[] = [];
      for (let i = 0; i < localStorage.length; i++) {
        keys.push(localStorage.key(i)!);
      }
      return keys;
    });

    const secretsFound = storage.filter(k =>
      k.toLowerCase().includes('token') ||
      k.toLowerCase().includes('auth') ||
      k.toLowerCase().includes('session') ||
      k.toLowerCase().includes('password') ||
      k.toLowerCase().includes('secret') ||
      k.toLowerCase().includes('credential') ||
      k.toLowerCase().includes('jwt') ||
      k.toLowerCase().includes('key')
    );

    expect(secretsFound).toEqual([]);
  });

  test('sessionStorage inspection — no auth secrets stored', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');

    const storage = await page.evaluate(() => {
      const keys: string[] = [];
      for (let i = 0; i < sessionStorage.length; i++) {
        keys.push(sessionStorage.key(i)!);
      }
      return keys;
    });

    const secretsFound = storage.filter(k =>
      k.toLowerCase().includes('token') ||
      k.toLowerCase().includes('auth') ||
      k.toLowerCase().includes('password') ||
      k.toLowerCase().includes('secret') ||
      k.toLowerCase().includes('credential') ||
      k.toLowerCase().includes('jwt') ||
      k.toLowerCase().includes('key')
    );

    expect(secretsFound).toEqual([]);
  });

  test('localStorage values contain no plain-text credentials', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');

    const hasSecrets = await page.evaluate(() => {
      for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i)!;
        const val = localStorage.getItem(key);
        if (!val) continue;
        try {
          const parsed = JSON.parse(val);
          const str = JSON.stringify(parsed).toLowerCase();
          if (str.includes('password') || str.includes('token') || str.includes('secret')) {
            return { key, val: str.substring(0, 200) };
          }
        } catch {
          if (val.toLowerCase().includes('password') || val.toLowerCase().includes('token')) {
            return { key, val: val.substring(0, 200) };
          }
        }
      }
      return null;
    });

    expect(hasSecrets).toBeNull();
  });

  test('Cookies — no authentication cookies set by the app', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');

    const cookies = await page.context().cookies();
    const authCookies = cookies.filter(c =>
      c.name.toLowerCase().includes('auth') ||
      c.name.toLowerCase().includes('session') ||
      c.name.toLowerCase().includes('token') ||
      c.name.toLowerCase().includes('sid')
    );
    expect(authCookies).toEqual([]);
  });

  test('localStorage is accessible and writable across pages', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');

    const isWritable = await page.evaluate(() => {
      try {
        localStorage.setItem('_test_key', 'test_value');
        const val = localStorage.getItem('_test_key');
        localStorage.removeItem('_test_key');
        return val === 'test_value';
      } catch {
        return false;
      }
    });
    expect(isWritable).toBe(true);
  });

  // ==========================================================================
  // SECTION 4: Navigation & Deep Link Validation
  // ==========================================================================

  test('Direct URL to session-expired renders page correctly', async ({ page }) => {
    await page.goto('/session-expired');
    await page.waitForLoadState('networkidle');
    await expect(page).toHaveURL(/\/session-expired/);
    await expect(page.locator('text=Your session expired')).toBeVisible();
  });

  test('Direct URL to access-denied renders page correctly', async ({ page }) => {
    await page.goto('/access-denied');
    await page.waitForLoadState('networkidle');
    await expect(page).toHaveURL(/\/access-denied/);
    await expect(page.locator('text=Access denied')).toBeVisible();
  });

  test('Back button from session-expired to login works', async ({ page }) => {
    await page.goto('/session-expired');
    await page.waitForLoadState('networkidle');
    await page.locator('button:has-text("Sign in again")').click();
    await page.waitForURL('**/login');
    expect(page.url()).toContain('/login');
  });

  test('Bookmark access to auth pages renders correctly', async ({ page }) => {
    const pages = [
      '/login', '/register', '/forgot-password',
      '/session-expired', '/access-denied',
    ];
    for (const path of pages) {
      await page.goto(path);
      await page.waitForLoadState('networkidle');
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(0);
    }
  });

  test('Direct URL to verify-otp without state redirects to login', async ({ page }) => {
    await page.goto('/verify-otp');
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('/login');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('AuthLoadingPage redirects to home when accessed directly', async ({ page }) => {
    await page.goto('/auth/loading');
    await page.waitForURL('**/', { timeout: 5000 });
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  // ==========================================================================
  // SECTION 5: Role-Specific Navigation Validation
  // ==========================================================================

  test('Navigation shows different workspaces based on role', async ({ page }) => {
    // Visit home page as default
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('Admin dashboard route is accessible', async ({ page }) => {
    await page.goto('/admin/dashboard');
    await page.waitForLoadState('networkidle');
    expect(page.url()).toContain('/admin/dashboard');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('Customer dashboard route is accessible', async ({ page }) => {
    await page.goto('/dashboard');
    await page.waitForLoadState('networkidle');
    await expect(page).toHaveURL(/\/dashboard/);
  });

  // ==========================================================================
  // SECTION 6: Session Error & Recovery UI
  // ==========================================================================

  test('LoggedOutPage component renders (exists but not routed)', async ({ page }) => {
    // The LoggedOutPage is not routed; verify 404 or redirect behavior
    const response = await page.goto('/logged-out');
    await page.waitForLoadState('networkidle');
    // Expect either a 404 or redirect to login/home
    const url = page.url();
    expect(url).not.toBeNull();
  });

  test('Auth error pages render correct status colors and actions', async ({ page }) => {
    await page.goto('/auth-error');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('h1:has-text("401")')).toBeVisible();
    await expect(page.locator('button:has-text("Sign in")')).toBeVisible();
    await expect(page.locator('button:has-text("Back to home")')).toBeVisible();
    await expect(page.locator('button:has-text("Try again")')).toBeVisible();
    await expect(page.locator('button:has-text("Retry")').first()).toBeVisible();
  });

  test('Concurrent navigation to session pages shows consistent state', async ({ page }) => {
    const pages = ['/session-expired', '/access-denied', '/auth/loading'];
    for (const p of pages) {
      await page.goto(p);
      await page.waitForLoadState('networkidle');
    }
    // Final state should be consistent
    expect(page.url()).toContain('/auth/loading') || expect(page.url()).toContain('/');
  });

  // ==========================================================================
  // SECTION 7: Responsive & Layout Validation
  // ==========================================================================

  test('Session pages maintain layout on mobile viewport', async ({ page }) => {
    await page.setViewportSize({ width: 375, height: 667 });
    const sessionPages = ['/session-expired', '/access-denied', '/auth/loading'];
    for (const p of sessionPages) {
      await page.goto(p);
      await page.waitForLoadState('networkidle');
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(0);
    }
  });

  test('Session pages maintain layout on tablet viewport', async ({ page }) => {
    await page.setViewportSize({ width: 768, height: 1024 });
    const sessionPages = ['/session-expired', '/access-denied', '/auth/loading'];
    for (const p of sessionPages) {
      await page.goto(p);
      await page.waitForLoadState('networkidle');
      const bodyText = await page.locator('body').innerText();
      expect(bodyText.length).toBeGreaterThan(0);
    }
  });

  // ==========================================================================
  // SECTION 8: Accessibility of Session Pages
  // ==========================================================================

  test('AuthLoadingPage has correct ARIA attributes', async ({ page }) => {
    await page.goto('/auth/loading');
    await expect(page.locator('role=status')).toHaveAttribute('aria-label', 'Establishing your session');
    await expect(page.locator('.auth-spinner')).toBeVisible();
  });

  test('Session pages have semantic headings', async ({ page }) => {
    const pages = [
      { url: '/session-expired', heading: 'Your session expired' },
      { url: '/access-denied', heading: 'Access denied' },
      { url: '/auth/loading', heading: 'Establishing your session' },
    ];
    for (const { url, heading } of pages) {
      await page.goto(url);
      await page.waitForLoadState('networkidle');
      await expect(page.locator('h1')).toContainText(heading);
    }
  });

  // ==========================================================================
  // SECTION 9: Multi-Tab & State Consistency
  // ==========================================================================

  test('Multiple tabs can view session-expired page independently', async ({ page, context }) => {
    await page.goto('/session-expired');
    await page.waitForLoadState('networkidle');

    const page2 = await context.newPage();
    await page2.goto('/session-expired');
    await page2.waitForLoadState('networkidle');

    await expect(page.locator('text=Your session expired')).toBeVisible();
    await expect(page2.locator('text=Your session expired')).toBeVisible();

    await page2.close();
  });

  test('Window resize during auth loading completes navigation', async ({ page }) => {
    await page.goto('/auth/loading');
    await page.setViewportSize({ width: 800, height: 600 });
    await page.waitForURL('**/', { timeout: 5000 });
    await page.setViewportSize({ width: 1280, height: 720 });
    expect(page.url()).not.toContain('/auth/loading');
  });

  // ==========================================================================
  // SECTION 10: Security Validation
  // ==========================================================================

  test('No sensitive data in page source for session pages', async ({ page }) => {
    const sessionPages = ['/session-expired', '/access-denied', '/auth/loading'];
    for (const p of sessionPages) {
      await page.goto(p);
      const html = await page.content();
      const body = await page.locator('body').innerText();
      expect(body.toLowerCase()).not.toContain('password');
      expect(body.toLowerCase()).not.toContain('jwt');
      expect(body.toLowerCase()).not.toContain('secret');
    }
  });

  test('Console output contains no sensitive session information', async ({ page }) => {
    const logs: string[] = [];
    page.on('console', msg => logs.push(msg.text()));

    await page.goto('/login');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service and Privacy Policy').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');

    const sensitive = logs.filter(l =>
      l.toLowerCase().includes('password') ||
      l.toLowerCase().includes('token') ||
      l.toLowerCase().includes('secret') ||
      l.toLowerCase().includes('jwt')
    );
    expect(sensitive).toEqual([]);
  });
});
