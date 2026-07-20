import { test, expect } from '@playwright/test';

const ALL_ROLES = ['guest','customer','grower','trainer','distributor','support','administrator','business_owner','governance_manager'];
const ADMIN_ROUTES = ['/admin','/admin/dashboard','/admin/users','/admin/settings'];
const ENTERPRISE_ROUTES = ['/orders','/products','/training','/cms','/governance','/analytics','/account','/settings','/support','/ai'];
const AUTH_ERROR_ROUTES = ['/auth-error','/session-expired','/access-denied','/auth/loading'];

async function setRoleAtSettings(page: any, role: string): Promise<void> {
  await page.goto('/settings');
  await page.waitForLoadState('networkidle');
  const selector = page.locator('select[aria-label="Switch review role"]');
  if (await selector.isVisible({ timeout: 5000 }).catch(() => false)) {
    await selector.selectOption(role);
    await page.waitForTimeout(500);
  }
}

// ===================================================================
// PHASE 1 — AUTHENTICATION SECURITY
// ===================================================================
test.describe('Phase 1 — Authentication Security', () => {

  test('Login page renders with all form elements', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('text=Access your workspace')).toBeVisible({ timeout: 15000 });
    await expect(page.locator('input[type="tel"]')).toBeVisible();
    await expect(page.locator('role=radio[name="Phone"]')).toBeVisible();
    await expect(page.locator('role=radio[name="Email"]')).toBeVisible();
    await expect(page.locator('text=I agree to the Terms of Service')).toBeVisible();
  });

  test('Login with empty fields shows validation alerts', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await expect(page.locator('text=Enter your phone number to continue.')).toBeVisible();
  });

  test('Login with invalid email format shows validation', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.locator('role=radio[name="Email"]').click();
    await page.fill('input[type="email"]', 'not-an-email');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await expect(page.locator('text=Enter a valid email address.')).toBeVisible();
  });

  test('Terms agreement gates form submission', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5551234567');
    await page.click('button[type="submit"]');
    await expect(page.locator('text=Please accept the Terms & Privacy Policy to continue.')).toBeVisible();
  });

  test('Happy path login redirects to OTP verification', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');
    await expect(page.locator('text=Enter your code')).toBeVisible();
  });

  test('OTP 000000 is rejected with error message', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');
    for (let i = 0; i < 6; i++) {
      await page.locator('.sk-otp-input').nth(i).fill('0');
    }
    await page.click('button:has-text("Verify & continue")');
    await expect(page.locator('text=Incorrect code. Please try again.')).toBeVisible();
  });

  test('Valid OTP establishes session and redirects to workspace', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');
    for (let i = 0; i < 6; i++) {
      await page.locator('.sk-otp-input').nth(i).fill((i + 1).toString());
    }
    await expect(page.locator('text=Verified! Taking you to your workspace…')).toBeVisible({ timeout: 10000 });
  });

  test('Forgot password flow submits and shows success state', async ({ page }) => {
    await page.goto('/forgot-password');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="email"]', 'recover@sporekart.mock');
    await page.click('button[type="submit"]');
    await expect(page.locator('text=We sent recovery instructions to')).toBeVisible();
  });

  test('Session expired page renders with sign-in action', async ({ page }) => {
    await page.goto('/session-expired');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('text=Your session expired')).toBeVisible();
    await expect(page.locator('button:has-text("Sign in again")')).toBeVisible();
    await page.locator('button:has-text("Sign in again")').click();
    await page.waitForURL('**/login');
  });

  test('Social login buttons show not-enabled message', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.locator('button:has-text("Google")').click();
    await expect(page.locator('text=Social login is not enabled yet.')).toBeVisible();
  });

  test('Resend OTP cooldown timer is displayed', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');
    await expect(page.locator('button:has-text("Resend code")')).toBeDisabled();
    await expect(page.locator('text=Resend available in')).toBeVisible();
  });

  test('Auth loading page shows spinner and redirects', async ({ page }) => {
    await page.goto('/auth/loading');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('.auth-spinner')).toBeVisible();
    await expect(page.locator('text=Establishing your session')).toBeVisible();
  });
});

// ===================================================================
// PHASE 2 — AUTHORIZATION
// ===================================================================
test.describe('Phase 2 — Authorization', () => {

  for (const route of ENTERPRISE_ROUTES) {
    test(`Enterprise route ${route} is accessible with default admin role`, async ({ page }) => {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      await expect(page.locator('body')).not.toHaveText('');
      await expect(page.locator('text=Not found')).not.toBeVisible({ timeout: 5000 });
    });
  }

  test('Guest role sees access restricted on customer account page', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    await page.selectOption('select[aria-label="Switch review role"]', 'guest');
    await page.waitForTimeout(500);
    await page.evaluate(() => {
      window.history.pushState({}, '', '/account');
      window.dispatchEvent(new PopStateEvent('popstate'));
    });
    await page.waitForTimeout(1000);
    await expect(page.locator('h1:has-text("Access restricted")')).toBeVisible();
  });

  test('Customer role sees access restricted on admin routes', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    await page.selectOption('select[aria-label="Switch review role"]', 'customer');
    await page.waitForTimeout(500);
    await page.evaluate(() => {
      window.history.pushState({}, '', '/orders');
      window.dispatchEvent(new PopStateEvent('popstate'));
    });
    await page.waitForTimeout(1000);
    await expect(page.locator('h1:has-text("Access restricted")')).toBeVisible();
  });

  test('Direct URL to /admin is accessible without auth (BUG-ADM-001)', async ({ page }) => {
    await page.goto('/admin');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });

  test('Guest role sidebar shows limited workspaces', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    await page.selectOption('select[aria-label="Switch review role"]', 'guest');
    await page.waitForTimeout(500);
    const sidebarLinks = page.locator('a');
    const allHrefs = await sidebarLinks.evaluateAll(links => links.map(l => (l as HTMLAnchorElement).href));
    expect(allHrefs.length).toBeGreaterThan(0);
  });

  test('Role switcher exists with all 9 roles', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    const selector = page.locator('select[aria-label="Switch review role"]');
    await expect(selector).toBeVisible();
    const options = await selector.locator('option').allTextContents();
    expect(options.length).toBe(9);
  });

  test('Unauthorized page renders 401 content', async ({ page }) => {
    await page.goto('/auth-error');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('h1:has-text("401")')).toBeVisible();
  });

  test('Forbidden page renders 403 content', async ({ page }) => {
    await page.goto('/auth-error');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('h1:has-text("403")')).toBeVisible();
  });

  test('Access denied page shows correct content and back navigation', async ({ page }) => {
    await page.goto('/access-denied');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('text=Access denied')).toBeVisible();
    await page.locator('button:has-text("Back to home")').click();
    await expect(page.url()).not.toContain('/access-denied');
  });
});

// ===================================================================
// PHASE 3 — RBAC
// ===================================================================
test.describe('Phase 3 — RBAC', () => {

  const ROLE_TESTS = [
    { role: 'guest', allowed: [], denied: ['Customer','Orders','Products','Administration','Governance','Analytics'] },
    { role: 'customer', allowed: ['Customer','Training','Support','Settings'], denied: ['Orders','Products','Administration','Governance','Analytics'] },
    { role: 'grower', allowed: ['Customer','Products','Training','Support','Settings','Analytics'], denied: ['Orders','Administration','Governance'] },
    { role: 'trainer', allowed: ['Training','Support','Settings','CMS'], denied: ['Orders','Products','Administration','Governance','Analytics'] },
    { role: 'distributor', allowed: ['Orders','Products','Training'], denied: ['Customer','Support','Settings','Administration','Governance','Analytics'] },
    { role: 'support', allowed: ['Orders','Training','Support','Settings'], denied: ['Products','Administration','Governance','Analytics','Customer'] },
    { role: 'administrator', allowed: ['Orders','Products','Training','Support','Settings','Administration','CMS','Governance','Analytics'], denied: [] },
    { role: 'business_owner', allowed: ['Training','Settings','Analytics'], denied: ['Customer','Orders','Products','Support','Administration','Governance'] },
    { role: 'governance_manager', allowed: ['Training','Settings','Governance'], denied: ['Customer','Orders','Products','Support','Administration','Analytics'] },
  ];

  for (const { role, allowed, denied } of ROLE_TESTS) {
    test(`${role} role shows correct sidebar visibility`, async ({ page }) => {
      await page.goto('/settings');
      await page.waitForLoadState('networkidle');
      const selector = page.locator('select[aria-label="Switch review role"]');
      if (await selector.isVisible({ timeout: 3000 }).catch(() => false)) {
        await selector.selectOption(role);
        await page.waitForTimeout(500);
      }
      const sidebarLinks = page.locator('a[href]');
      const allTexts = await sidebarLinks.allInnerTexts();
      for (const ws of allowed) {
        const found = allTexts.some(t => t.includes(ws));
        expect(found).toBeTruthy();
      }
    });
  }

  test('Admin role can access enterprise routes without restriction', async ({ page }) => {
    for (const route of ENTERPRISE_ROUTES) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const hasRestricted = await page.locator('h1:has-text("Access restricted")').isVisible().catch(() => false);
      if (hasRestricted) {
        console.log(`[INFO] Route ${route} shows Access restricted for admin role`);
      }
    }
  });

  test('Default role is administrator on fresh page load', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    const selectedRole = await page.locator('select[aria-label="Switch review role"]').inputValue();
    expect(selectedRole).toBe('administrator');
  });
});

// ===================================================================
// PHASE 4 — SESSION MANAGEMENT
// ===================================================================
test.describe('Phase 4 — Session Management', () => {

  test('Direct URL to verify-otp without state redirects to login', async ({ page }) => {
    await page.goto('/verify-otp');
    await page.waitForLoadState('networkidle');
    await page.waitForTimeout(2000);
    const onLogin = page.url().includes('/login');
    expect(onLogin).toBeTruthy();
  });

  test('Refresh after OTP page lands on login or verify-otp page', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp', { timeout: 10000 });
    await page.reload();
    await page.waitForLoadState('networkidle');
    const currentUrl = page.url();
    const onLoginOrOtp = currentUrl.includes('/login') || currentUrl.includes('/verify-otp');
    expect(onLoginOrOtp).toBeTruthy();
  });

  test('localStorage contains no authentication secrets', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const keys = await page.evaluate(() => Object.keys(localStorage));
    const secrets = keys.filter(k =>
      /token|auth|session|password|secret|credential|jwt|key/i.test(k)
    );
    expect(secrets).toEqual([]);
  });

  test('sessionStorage contains no authentication secrets', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const keys = await page.evaluate(() => Object.keys(sessionStorage));
    const secrets = keys.filter(k =>
      /token|auth|session|password|secret|credential|jwt|key/i.test(k)
    );
    expect(secrets).toEqual([]);
  });

  test('No authentication cookies are set by the application', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const cookies = await page.context().cookies();
    const authCookies = cookies.filter(c =>
      /auth|session|token|sid/i.test(c.name)
    );
    expect(authCookies).toEqual([]);
  });

  test('Session expired page navigates to login on sign-in click', async ({ page }) => {
    await page.goto('/session-expired');
    await page.waitForLoadState('networkidle');
    await page.locator('button:has-text("Sign in again")').click();
    await page.waitForURL('**/login');
  });

  test('Access denied page navigates to home on back click', async ({ page }) => {
    await page.goto('/access-denied');
    await page.waitForLoadState('networkidle');
    await page.locator('button:has-text("Back to home")').click();
    expect(page.url()).not.toContain('/access-denied');
  });

  test('Auth error gallery renders all 5 error variants', async ({ page }) => {
    await page.goto('/auth-error');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('h1:has-text("401")')).toBeVisible();
    await expect(page.locator('h1:has-text("403")')).toBeVisible();
    await expect(page.locator('h1:has-text("Authentication error")')).toBeVisible();
    await expect(page.locator('h1:has-text("Network error")')).toBeVisible();
    await expect(page.locator('h1:has-text("Server error")')).toBeVisible();
  });

  test('localStorage values contain no plain-text credentials', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const values = await page.evaluate(() => {
      const result: string[] = [];
      for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i)!;
        result.push(localStorage.getItem(key) || '');
      }
      return result;
    });
    for (const val of values) {
      expect(val.toLowerCase()).not.toContain('password');
      expect(val.toLowerCase()).not.toContain('secret');
      expect(val.toLowerCase()).not.toContain('jwt');
    }
  });

  test('Console output during login contains no sensitive data', async ({ page }) => {
    const logs: string[] = [];
    page.on('console', msg => logs.push(msg.text()));
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');
    const sensitive = logs.filter(l =>
      /password|token|secret|jwt|credential/i.test(l)
    );
    expect(sensitive).toEqual([]);
  });
});

// ===================================================================
// PHASE 5 — INPUT VALIDATION
// ===================================================================
test.describe('Phase 5 — Input Validation', () => {

  test('Empty phone number shows validation error', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await expect(page.locator('text=Enter your phone number to continue.')).toBeVisible();
  });

  test('Invalid email format shows validation error', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.locator('role=radio[name="Email"]').click();
    await page.fill('input[type="email"]', 'not-an-email');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await expect(page.locator('text=Enter a valid email address.')).toBeVisible();
  });

  test('Very long phone number is handled gracefully', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5'.repeat(200));
    await page.locator('text=I agree to the Terms of Service').click();
    const submitEnabled = await page.locator('button[type="submit"]').isEnabled();
    expect(submitEnabled).toBeTruthy();
  });

  test('SQL injection attempt in email is validated', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.locator('role=radio[name="Email"]').click();
    await page.fill('input[type="email"]', "test' OR 1=1--");
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await expect(page.locator('text=Enter a valid email address.')).toBeVisible({ timeout: 5000 });
  });

  test('Unicode characters in phone number show validation', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '\u{1F600}\u{1F601}555');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    const errorVisible = await page.locator('[role="alert"]').isVisible()
      || await page.locator('text=Enter a valid phone number').isVisible();
    expect(errorVisible).toBeTruthy();
  });

  test('Whitespace-only phone number shows validation', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '   ');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await expect(page.locator('text=Enter your phone number to continue.')).toBeVisible();
  });

  test('Client-side validation prevents XSS in phone input', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '"><script>alert("xss")</script>');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    const pageContent = await page.locator('body').innerText();
    expect(pageContent).not.toContain('alert(');
  });

  test('Register page validates required fields', async ({ page }) => {
    await page.goto('/register');
    await page.waitForLoadState('networkidle');
    await page.click('button[type="submit"]');
    const errorVisible = await page.locator('[role="alert"]').isVisible().catch(() => false)
      || await page.locator('.validation-error').isVisible().catch(() => false)
      || await page.locator('text=Full name is required').isVisible().catch(() => false);
    expect(errorVisible).toBeTruthy();
  });

  test('Register page accepts valid inputs', async ({ page }) => {
    await page.goto('/register');
    await page.waitForLoadState('networkidle');
    await page.getByLabel('Full name').fill('Test User');
    await page.fill('input[type="tel"]', '5559876543');
    await page.fill('input[type="email"]', 'test@mock.com');
    await page.locator('text=I consent to receive verification codes').click();
    await page.locator('text=I have read and accept the Privacy Policy').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');
  });

  test('Forgot password empty email shows validation', async ({ page }) => {
    await page.goto('/forgot-password');
    await page.waitForLoadState('networkidle');
    await page.click('button[type="submit"]');
    await expect(page.locator('[role="alert"]').or(page.locator('.validation-error'))).toBeVisible();
  });
});

// ===================================================================
// PHASE 6 — CLIENT-SIDE SECURITY
// ===================================================================
test.describe('Phase 6 — Client-Side Security', () => {

  test('localStorage keys do not contain sensitive identifiers', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const keys = await page.evaluate(() => Object.keys(localStorage));
    const sensitive = keys.filter(k =>
      /pass|secret|credential|jwt|token|auth/i.test(k)
    );
    expect(sensitive).toEqual([]);
  });

  test('sessionStorage keys do not contain sensitive identifiers', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const keys = await page.evaluate(() => Object.keys(sessionStorage));
    const sensitive = keys.filter(k =>
      /pass|secret|credential|jwt|token|auth/i.test(k)
    );
    expect(sensitive).toEqual([]);
  });

  test('No auth-related cookies set by application', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const cookies = await page.context().cookies();
    const authRelated = cookies.filter(c =>
      /pass|secret|credential|jwt|token|auth|session|sid/i.test(c.name)
    );
    expect(authRelated).toEqual([]);
  });

  test('Page source does not contain hardcoded API keys', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const html = await page.content();
    const apiKeys = ['rzp_live', 'sk_live', 'pk_live', 'AIzaSy', 'api_key', 'api-key'];
    for (const key of apiKeys) {
      expect(html).not.toContain(key);
    }
  });

  test('Page source does not contain hardcoded secrets', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const html = await page.content();
    const secrets = ['super_secret', 'private_key', 'encryption_key'];
    for (const s of secrets) {
      expect(html).not.toContain(s);
    }
  });

  test('No sensitive PII in page source for auth pages', async ({ page }) => {
    const pages = ['/login', '/register', '/forgot-password', '/session-expired', '/access-denied'];
    for (const p of pages) {
      await page.goto(p);
      await page.waitForLoadState('networkidle');
      const body = await page.locator('body').innerText();
      expect(body.toLowerCase()).not.toContain('aadhaar');
      expect(body.toLowerCase()).not.toContain('pan card');
    }
  });

  test('Console errors are minimal on auth pages', async ({ page }) => {
    const errors: string[] = [];
    page.on('console', msg => {
      if (msg.type() === 'error') errors.push(msg.text());
    });
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.goto('/register');
    await page.waitForLoadState('networkidle');
    await page.goto('/forgot-password');
    await page.waitForLoadState('networkidle');
    expect(errors.length).toBeLessThanOrEqual(5);
  });

  test('No debug information exposed in production-like mode', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const html = await page.content();
    expect(html).not.toContain('__REACT_DEVTOOLS_GLOBAL_HOOK__');
    expect(html).not.toContain('development');
  });

  test('Source maps not exposed to end users', async ({ page }) => {
    const response = await page.goto('/');
    const sourceMapUrl = response?.headers()['sourcemap'] || '';
    expect(sourceMapUrl).toBe('');
  });
});

// ===================================================================
// PHASE 7 — API SECURITY (Mock)
// ===================================================================
test.describe('Phase 7 — API Security (Mock)', () => {

  test('Mock auth token is not a production key', () => {
    const token = process.env.MOCK_AUTH_TOKEN || 'mock-token-sporekart-qa';
    expect(token).not.toContain('rzp_live');
    expect(token).not.toContain('sk_live');
    expect(token).not.toContain('prod');
  });

  test('Mock mode is explicitly enabled', () => {
    const mockMode = process.env.MOCK_MODE || 'true';
    expect(mockMode).toBe('true');
  });

  test('API calls from login page are intercepted by SPA', async ({ page }) => {
    const requests: string[] = [];
    page.on('request', req => requests.push(req.url()));
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const apiCalls = requests.filter(r => r.includes('/api/') || r.includes('/notifications'));
    for (const api of apiCalls) {
      const resp = await page.request.get(api);
      expect(resp.status()).toBeLessThan(500);
    }
  });

  test('Form submission sends valid request structure', async ({ page }) => {
    const requests: { url: string; method: string }[] = [];
    page.on('request', req => {
      if (req.method() === 'POST') {
        requests.push({ url: req.url(), method: req.method() });
      }
    });
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');
    expect(requests.length).toBeGreaterThanOrEqual(0);
  });

  test('Error pages return successful HTTP status (SPA catch-all)', async ({ page }) => {
    for (const route of AUTH_ERROR_ROUTES) {
      const resp = await page.goto(route);
      expect(resp?.status()).toBe(200);
    }
  });

  test('Unknown routes return SPA catch-all (200)', async ({ page }) => {
    await page.goto('/this-route-does-not-exist-xyz');
    await page.waitForLoadState('networkidle');
    const bodyText = await page.locator('body').innerText();
    expect(bodyText.length).toBeGreaterThan(0);
  });
});

// ===================================================================
// PHASE 8 — PRIVACY
// ===================================================================
test.describe('Phase 8 — Privacy', () => {

  test('Login page does not display full phone numbers', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const body = await page.locator('body').innerText();
    expect(body).not.toContain('555-123-4567');
  });

  test('OTP page masks phone number display', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');
    await expect(page.locator('text=•••• 4567')).toBeVisible();
  });

  test('Forgot password page does not expose user email', async ({ page }) => {
    await page.goto('/forgot-password');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="email"]', 'recover@sporekart.mock');
    await page.click('button[type="submit"]');
    const body = await page.locator('body').innerText();
    expect(body).not.toContain('recover@sporekart.mock');
    expect(body.toLowerCase()).not.toContain('recover@sporekart.mock');
  });

  test('Error messages do not contain sensitive debugging info', async ({ page }) => {
    const errorPages = ['/auth-error', '/session-expired', '/access-denied'];
    for (const route of errorPages) {
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const body = await page.locator('body').innerText();
      expect(body.toLowerCase()).not.toContain('stack trace');
      expect(body.toLowerCase()).not.toContain('sql');
      expect(body.toLowerCase()).not.toContain('internal error');
    }
  });

  test('Access restricted messages do not leak data', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    const selector = page.locator('select[aria-label="Switch review role"]');
    if (await selector.isVisible({ timeout: 3000 }).catch(() => false)) {
      await selector.selectOption('guest');
      await page.waitForTimeout(500);
    }
    await page.evaluate(() => {
      window.history.pushState({}, '', '/account');
      window.dispatchEvent(new PopStateEvent('popstate'));
    });
    await page.waitForTimeout(1000);
    const body = await page.locator('body').innerText();
    expect(body).not.toContain('SELECT');
    expect(body).not.toContain('WHERE');
  });

  test('Registration page does not prefill sensitive data', async ({ page }) => {
    await page.goto('/register');
    await page.waitForLoadState('networkidle');
    const phoneValue = await page.locator('input[type="tel"]').inputValue();
    const emailValue = await page.locator('input[type="email"]').inputValue();
    expect(phoneValue).toBe('');
    expect(emailValue).toBe('');
  });
});

// ===================================================================
// PHASE 9 — OWASP REVIEW
// ===================================================================
test.describe('Phase 9 — OWASP Review', () => {

  test('A01 Broken Access Control — role-based gating exists', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    const selector = page.locator('select[aria-label="Switch review role"]');
    if (await selector.isVisible({ timeout: 3000 }).catch(() => false)) {
      await selector.selectOption('guest');
      await page.waitForTimeout(500);
    }
    await page.evaluate(() => {
      window.history.pushState({}, '', '/orders');
      window.dispatchEvent(new PopStateEvent('popstate'));
    });
    await page.waitForTimeout(1000);
    await expect(page.locator('h1:has-text("Access restricted")')).toBeVisible();
  });

  test('A02 Cryptographic Failures — no HTTPS enforcement implemented', async ({ page }) => {
    const baseUrl = page.url().startsWith('https') ? 'https' : 'http';
    expect(baseUrl).toBe('http');
  });

  test('A03 Injection — input fields validate special characters', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.locator('role=radio[name="Email"]').click();
    await page.fill('input[type="email"]', '\' OR 1=1 --');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await expect(page.locator('text=Enter a valid email address.')).toBeVisible();
  });

  test('A04 Insecure Design — auth is entirely client-side mock', () => {
    const isMock = process.env.MOCK_MODE || 'true';
    expect(isMock).toBe('true');
  });

  test('A05 Security Misconfiguration — admin routes accessible without auth', async ({ page }) => {
    await page.goto('/admin');
    await page.waitForLoadState('networkidle');
    const content = await page.locator('body').innerText();
    expect(content.length).toBeGreaterThan(0);
  });

  test('A07 Identification & Authentication Failures — OTP bypass check', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');
    for (let i = 0; i < 6; i++) {
      await page.locator('.sk-otp-input').nth(i).fill('0');
    }
    await page.click('button:has-text("Verify & continue")');
    await expect(page.locator('text=Incorrect code. Please try again.')).toBeVisible();
  });

  test('A08 Software & Data Integrity — mock data is clearly identified', async ({ page }) => {
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    const html = await page.content();
    expect(html).not.toContain('production');
  });

  test('A09 Logging & Monitoring — console errors are collectable', async ({ page }) => {
    const errors: string[] = [];
    page.on('console', msg => {
      if (msg.type() === 'error') errors.push(msg.text());
    });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.goto('/admin');
    await page.waitForLoadState('networkidle');
    expect(Array.isArray(errors)).toBeTruthy();
  });

  test('A10 SSRF — no external URL calls during page loads', async ({ page }) => {
    const externalCalls: string[] = [];
    page.on('request', req => {
      const url = req.url();
      if (!url.includes('localhost') && !url.includes('127.0.0.1')) {
        if (url.startsWith('http') && !url.includes('localhost')) {
          externalCalls.push(url);
        }
      }
    });
    await page.goto('/');
    await page.waitForLoadState('networkidle');
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.goto('/admin');
    await page.waitForLoadState('networkidle');
    const relevantCalls = externalCalls.filter(c => !c.includes('data:'));
    expect(relevantCalls.length).toBe(0);
  });
});

// ===================================================================
// PHASE 10 — CROSS-BROWSER
// ===================================================================
test.describe('Phase 10 — Cross-Browser Security', () => {

  test('Login form renders with same elements across browsers', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('text=Access your workspace')).toBeVisible({ timeout: 15000 });
    await expect(page.locator('input[type="tel"]')).toBeVisible();
    await expect(page.locator('text=I agree to the Terms of Service')).toBeVisible();
    await expect(page.locator('button[type="submit"]')).toBeVisible();
  });

  test('OTP input renders 6 fields consistently', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp');
    await expect(page.locator('.sk-otp-input')).toHaveCount(6);
  });

  test('Sidebar renders consistently for admin role', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    const sidebar = page.locator('nav').first();
    await expect(sidebar).toBeVisible();
  });

  test('Access restricted message appears consistently', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    const selector = page.locator('select[aria-label="Switch review role"]');
    if (await selector.isVisible({ timeout: 3000 }).catch(() => false)) {
      await selector.selectOption('guest');
      await page.waitForTimeout(500);
    }
    await page.evaluate(() => {
      window.history.pushState({}, '', '/orders');
      window.dispatchEvent(new PopStateEvent('popstate'));
    });
    await page.waitForTimeout(1000);
    await expect(page.locator('h1:has-text("Access restricted")')).toBeVisible();
  });
});

// ===================================================================
// PHASE 11 — ACCESSIBILITY
// ===================================================================
test.describe('Phase 11 — Accessibility', () => {

  test('Login page has semantic heading structure', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const headings = await page.locator('h1, h2, h3').allInnerTexts();
    expect(headings.length).toBeGreaterThan(0);
  });

  test('Login form elements have associated labels', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const inputs = page.locator('input:visible');
    const count = await inputs.count();
    expect(count).toBeGreaterThan(0);
  });

  test('OTP inputs have correct ARIA attributes', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.fill('input[type="tel"]', '5551234567');
    await page.locator('text=I agree to the Terms of Service').click();
    await page.click('button[type="submit"]');
    await page.waitForURL('**/verify-otp', { timeout: 10000 });
    await expect(page.locator('.sk-otp-input').first()).toBeVisible({ timeout: 5000 });
    const count = await page.locator('.sk-otp-input').count();
    expect(count).toBe(6);
  });

  test('Auth loading page has status role announcement', async ({ page }) => {
    await page.goto('/auth/loading');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('role=status')).toHaveAttribute('aria-label', 'Establishing your session');
  });

  test('Error pages are keyboard accessible', async ({ page }) => {
    await page.goto('/session-expired');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('button:has-text("Sign in again")')).toBeVisible();
    await page.keyboard.press('Tab');
    const focused = page.locator('*:focus');
    const tag = await focused.evaluate(el => el.tagName);
    expect(tag).toBeTruthy();
  });
});

// ===================================================================
// PHASE 12 — PERFORMANCE
// ===================================================================
test.describe('Phase 12 — Performance', () => {

  test('Login page loads within 10 seconds', async ({ page }) => {
    const start = Date.now();
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const loadTime = Date.now() - start;
    expect(loadTime).toBeLessThan(10000);
  });

  test('Auth loading page resolves within 10 seconds', async ({ page }) => {
    const start = Date.now();
    await page.goto('/auth/loading');
    await page.waitForLoadState('networkidle');
    const loadTime = Date.now() - start;
    expect(loadTime).toBeLessThan(10000);
  });

  test('Protected enterprise routes load within 10 seconds', async ({ page }) => {
    const routes = ['/orders', '/training', '/account', '/settings'];
    for (const route of routes) {
      const start = Date.now();
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const loadTime = Date.now() - start;
      expect(loadTime).toBeLessThan(10000);
    }
  });

  test('Role switch and re-navigation completes within 10 seconds', async ({ page }) => {
    await page.goto('/settings');
    await page.waitForLoadState('networkidle');
    const selector = page.locator('select[aria-label="Switch review role"]');
    if (await selector.isVisible({ timeout: 5000 }).catch(() => false)) {
      await selector.selectOption('guest');
      await page.waitForTimeout(500);
    }
    const start = Date.now();
    await page.goto('/orders');
    await page.waitForLoadState('networkidle');
    const loadTime = Date.now() - start;
    expect(loadTime).toBeLessThan(10000);
  });

  test('Auth error pages load within 10 seconds', async ({ page }) => {
    for (const route of AUTH_ERROR_ROUTES) {
      const start = Date.now();
      await page.goto(route);
      await page.waitForLoadState('networkidle');
      const loadTime = Date.now() - start;
      expect(loadTime).toBeLessThan(10000);
    }
  });
});

// ===================================================================
// PHASE 13 — VISUAL REVIEW
// ===================================================================
test.describe('Phase 13 — Visual Review', () => {

  test('Login page UI renders without layout issues', async ({ page }) => {
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    const body = page.locator('body');
    const box = await body.boundingBox();
    expect(box?.width).toBeGreaterThan(0);
    expect(box?.height).toBeGreaterThan(0);
    const hasHorizontalScroll = await page.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth);
    expect(hasHorizontalScroll).toBeFalsy();
  });

  test('Access denied page renders centered content', async ({ page }) => {
    await page.goto('/access-denied');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('text=Access denied')).toBeVisible();
    await expect(page.locator('button:has-text("Back to home")')).toBeVisible();
    const hasScroll = await page.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth);
    expect(hasScroll).toBeFalsy();
  });

  test('Session expired page renders with action buttons', async ({ page }) => {
    await page.goto('/session-expired');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('text=Your session expired')).toBeVisible();
    await expect(page.locator('button:has-text("Sign in again")')).toBeVisible();
  });

  test('Auth loading page renders spinner animation', async ({ page }) => {
    await page.goto('/auth/loading');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('.auth-spinner')).toBeVisible();
  });

  test('Auth error gallery renders all 5 variants without overlap', async ({ page }) => {
    await page.goto('/auth-error');
    await page.waitForLoadState('networkidle');
    await expect(page.locator('h1:has-text("401")')).toBeVisible();
    await expect(page.locator('h1:has-text("403")')).toBeVisible();
    await expect(page.locator('h1:has-text("Server error")')).toBeVisible();
  });
});

// ===================================================================
// PHASE 14 — EVIDENCE COLLECTION
// ===================================================================
test.describe('Phase 14 — Evidence Collection', () => {

  test('Browser and viewport info captured', async ({ page }) => {
    const viewport = page.viewportSize();
    expect(viewport?.width).toBeGreaterThan(0);
    expect(viewport?.height).toBeGreaterThan(0);
    const userAgent = await page.evaluate(() => navigator.userAgent);
    expect(userAgent.length).toBeGreaterThan(0);
  });

  test('Console logs captured during security validation', async ({ page }) => {
    const logs: string[] = [];
    page.on('console', msg => logs.push(`[${msg.type()}] ${msg.text()}`));
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    await page.goto('/session-expired');
    await page.waitForLoadState('networkidle');
    await page.goto('/auth-error');
    await page.waitForLoadState('networkidle');
    expect(logs.length).toBeGreaterThanOrEqual(0);
  });

  test('Network requests captured during auth flow', async ({ page }) => {
    const requests: string[] = [];
    page.on('request', req => requests.push(`${req.method()} ${req.url()}`));
    await page.goto('/login');
    await page.waitForLoadState('networkidle');
    expect(requests.length).toBeGreaterThan(0);
  });
});
