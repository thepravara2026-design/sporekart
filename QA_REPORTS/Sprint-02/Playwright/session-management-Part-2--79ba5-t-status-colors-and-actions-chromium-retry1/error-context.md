# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: session-management.spec.ts >> Part 2 — Session Management Validation >> Auth error pages render correct status colors and actions
- Location: tests\session-management.spec.ts:306:7

# Error details

```
Error: expect(locator).toBeVisible() failed

Locator: locator('button:has-text("Retry")')
Expected: visible
Error: strict mode violation: locator('button:has-text("Retry")') resolved to 2 elements:
    1) <button type="button" aria-busy="false" class="sk-btn ds-rd" aria-disabled="false">…</button> aka getByRole('button', { name: 'Retry' }).first()
    2) <button type="button" aria-busy="false" class="sk-btn ds-rh" aria-disabled="false">…</button> aka getByRole('button', { name: 'Retry' }).nth(1)

Call log:
  - Expect "toBeVisible" with timeout 5000ms
  - waiting for locator('button:has-text("Retry")')

```

# Page snapshot

```yaml
- generic [ref=e2]:
  - link "Skip to content" [ref=e3] [cursor=pointer]:
    - /url: "#main"
  - generic [ref=e4]:
    - main [ref=e6]:
      - img [ref=e8]
      - heading "401 · Sign in required" [level=1] [ref=e11]
      - paragraph [ref=e12]: You need to authenticate before viewing this resource. Please sign in to continue.
      - generic [ref=e13]:
        - button "Sign in" [ref=e14] [cursor=pointer]:
          - generic [ref=e15]: Sign in
        - button "Go to home" [ref=e16] [cursor=pointer]:
          - generic [ref=e17]: Go to home
    - main [ref=e19]:
      - img [ref=e21]
      - heading "403 · Forbidden" [level=1] [ref=e24]
      - paragraph [ref=e25]: You don’t have the required role to access this resource. Role-based access control blocked this request.
      - generic [ref=e26]:
        - button "Back to home" [ref=e27] [cursor=pointer]:
          - generic [ref=e28]: Back to home
        - button "Contact support" [ref=e29] [cursor=pointer]:
          - generic [ref=e30]: Contact support
    - main [ref=e32]:
      - img [ref=e34]
      - heading "Authentication error" [level=1] [ref=e36]
      - paragraph [ref=e37]: Something went wrong while verifying your identity. This is usually temporary — please try signing in again.
      - generic [ref=e38]:
        - button "Try again" [ref=e39] [cursor=pointer]:
          - generic [ref=e40]: Try again
        - button "Contact support" [ref=e41] [cursor=pointer]:
          - generic [ref=e42]: Contact support
    - main [ref=e44]:
      - img [ref=e46]
      - heading "Network error" [level=1] [ref=e50]
      - paragraph [ref=e51]: We couldn’t reach the authentication service. Check your connection and retry.
      - generic [ref=e52]:
        - button "Retry" [ref=e53] [cursor=pointer]:
          - generic [ref=e54]: Retry
        - button "Go to home" [ref=e55] [cursor=pointer]:
          - generic [ref=e56]: Go to home
    - main [ref=e58]:
      - img [ref=e60]
      - heading "Server error" [level=1] [ref=e64]
      - paragraph [ref=e65]: The authentication service is temporarily unavailable. Our team has been notified. Please try again shortly.
      - generic [ref=e66]:
        - button "Retry" [ref=e67] [cursor=pointer]:
          - generic [ref=e68]: Retry
        - button "Go to home" [ref=e69] [cursor=pointer]:
          - generic [ref=e70]: Go to home
```

# Test source

```ts
  213 |   test('Theme preference persists in localStorage across pages', async ({ page }) => {
  214 |     await page.goto('/');
  215 |     await page.waitForLoadState('networkidle');
  216 | 
  217 |     const theme = await page.evaluate(() => localStorage.getItem('sporekart-theme'));
  218 |     expect(typeof theme).toBe('string');
  219 |   });
  220 | 
  221 |   // ==========================================================================
  222 |   // SECTION 4: Navigation & Deep Link Validation
  223 |   // ==========================================================================
  224 | 
  225 |   test('Direct URL to session-expired renders page correctly', async ({ page }) => {
  226 |     await page.goto('/session-expired');
  227 |     await page.waitForLoadState('networkidle');
  228 |     await expect(page).toHaveURL(/\/session-expired/);
  229 |     await expect(page.locator('text=Your session expired')).toBeVisible();
  230 |   });
  231 | 
  232 |   test('Direct URL to access-denied renders page correctly', async ({ page }) => {
  233 |     await page.goto('/access-denied');
  234 |     await page.waitForLoadState('networkidle');
  235 |     await expect(page).toHaveURL(/\/access-denied/);
  236 |     await expect(page.locator('text=Access denied')).toBeVisible();
  237 |   });
  238 | 
  239 |   test('Back button from session-expired to login works', async ({ page }) => {
  240 |     await page.goto('/session-expired');
  241 |     await page.waitForLoadState('networkidle');
  242 |     await page.locator('button:has-text("Sign in again")').click();
  243 |     await page.waitForURL('**/login');
  244 |     expect(page.url()).toContain('/login');
  245 |   });
  246 | 
  247 |   test('Bookmark access to auth pages renders correctly', async ({ page }) => {
  248 |     const pages = [
  249 |       { path: '/login', expectPath: '/login' },
  250 |       { path: '/register', expectPath: '/register' },
  251 |       { path: '/forgot-password', expectPath: '/forgot-password' },
  252 |       { path: '/verify-otp', expectPath: '/verify-otp' },
  253 |       { path: '/session-expired', expectPath: '/session-expired' },
  254 |       { path: '/access-denied', expectPath: '/access-denied' },
  255 |       { path: '/auth/loading', expectPath: '/' },
  256 |     ];
  257 |     for (const { path, expectPath } of pages) {
  258 |       await page.goto(path);
  259 |       await page.waitForLoadState('networkidle');
  260 |       const url = page.url();
  261 |       expect(url.endsWith(expectPath) || url.includes(expectPath)).toBeTruthy();
  262 |       const bodyText = await page.locator('body').innerText();
  263 |       expect(bodyText.length).toBeGreaterThan(0);
  264 |     }
  265 |   });
  266 | 
  267 |   // ==========================================================================
  268 |   // SECTION 5: Role-Specific Navigation Validation
  269 |   // ==========================================================================
  270 | 
  271 |   test('Navigation shows different workspaces based on role', async ({ page }) => {
  272 |     // Visit home page as default
  273 |     await page.goto('/');
  274 |     await page.waitForLoadState('networkidle');
  275 |     const bodyText = await page.locator('body').innerText();
  276 |     expect(bodyText.length).toBeGreaterThan(0);
  277 |   });
  278 | 
  279 |   test('Admin dashboard route is accessible', async ({ page }) => {
  280 |     await page.goto('/admin/dashboard');
  281 |     await page.waitForLoadState('networkidle');
  282 |     expect(page.url()).toContain('/admin/dashboard');
  283 |     const bodyText = await page.locator('body').innerText();
  284 |     expect(bodyText.length).toBeGreaterThan(0);
  285 |   });
  286 | 
  287 |   test('Customer dashboard route is accessible', async ({ page }) => {
  288 |     await page.goto('/dashboard');
  289 |     await page.waitForLoadState('networkidle');
  290 |     await expect(page).toHaveURL(/\/dashboard/);
  291 |   });
  292 | 
  293 |   // ==========================================================================
  294 |   // SECTION 6: Session Error & Recovery UI
  295 |   // ==========================================================================
  296 | 
  297 |   test('LoggedOutPage component renders (exists but not routed)', async ({ page }) => {
  298 |     // The LoggedOutPage is not routed; verify 404 or redirect behavior
  299 |     const response = await page.goto('/logged-out');
  300 |     await page.waitForLoadState('networkidle');
  301 |     // Expect either a 404 or redirect to login/home
  302 |     const url = page.url();
  303 |     expect(url).not.toBeNull();
  304 |   });
  305 | 
  306 |   test('Auth error pages render correct status colors and actions', async ({ page }) => {
  307 |     await page.goto('/auth-error');
  308 |     await page.waitForLoadState('networkidle');
  309 |     await expect(page.locator('h1:has-text("401")')).toBeVisible();
  310 |     await expect(page.locator('button:has-text("Sign in")')).toBeVisible();
  311 |     await expect(page.locator('button:has-text("Back to home")')).toBeVisible();
  312 |     await expect(page.locator('button:has-text("Try again")')).toBeVisible();
> 313 |     await expect(page.locator('button:has-text("Retry")')).toBeVisible();
      |                                                            ^ Error: expect(locator).toBeVisible() failed
  314 |   });
  315 | 
  316 |   test('Concurrent navigation to session pages shows consistent state', async ({ page }) => {
  317 |     const pages = ['/session-expired', '/access-denied', '/auth/loading'];
  318 |     for (const p of pages) {
  319 |       await page.goto(p);
  320 |       await page.waitForLoadState('networkidle');
  321 |     }
  322 |     // Final state should be consistent
  323 |     expect(page.url()).toContain('/auth/loading') || expect(page.url()).toContain('/');
  324 |   });
  325 | 
  326 |   // ==========================================================================
  327 |   // SECTION 7: Responsive & Layout Validation
  328 |   // ==========================================================================
  329 | 
  330 |   test('Session pages maintain layout on mobile viewport', async ({ page }) => {
  331 |     await page.setViewportSize({ width: 375, height: 667 });
  332 |     const sessionPages = ['/session-expired', '/access-denied', '/auth/loading'];
  333 |     for (const p of sessionPages) {
  334 |       await page.goto(p);
  335 |       await page.waitForLoadState('networkidle');
  336 |       const bodyText = await page.locator('body').innerText();
  337 |       expect(bodyText.length).toBeGreaterThan(0);
  338 |     }
  339 |   });
  340 | 
  341 |   test('Session pages maintain layout on tablet viewport', async ({ page }) => {
  342 |     await page.setViewportSize({ width: 768, height: 1024 });
  343 |     const sessionPages = ['/session-expired', '/access-denied', '/auth/loading'];
  344 |     for (const p of sessionPages) {
  345 |       await page.goto(p);
  346 |       await page.waitForLoadState('networkidle');
  347 |       const bodyText = await page.locator('body').innerText();
  348 |       expect(bodyText.length).toBeGreaterThan(0);
  349 |     }
  350 |   });
  351 | 
  352 |   // ==========================================================================
  353 |   // SECTION 8: Accessibility of Session Pages
  354 |   // ==========================================================================
  355 | 
  356 |   test('AuthLoadingPage has correct ARIA attributes', async ({ page }) => {
  357 |     await page.goto('/auth/loading');
  358 |     await expect(page.locator('role=status')).toHaveAttribute('aria-label', 'Establishing your session');
  359 |     await expect(page.locator('.auth-spinner')).toBeVisible();
  360 |   });
  361 | 
  362 |   test('Session pages have semantic headings', async ({ page }) => {
  363 |     const pages = [
  364 |       { url: '/session-expired', heading: 'Your session expired' },
  365 |       { url: '/access-denied', heading: 'Access denied' },
  366 |       { url: '/auth/loading', heading: 'Establishing your session' },
  367 |     ];
  368 |     for (const { url, heading } of pages) {
  369 |       await page.goto(url);
  370 |       await page.waitForLoadState('networkidle');
  371 |       await expect(page.locator('h1')).toContainText(heading);
  372 |     }
  373 |   });
  374 | 
  375 |   // ==========================================================================
  376 |   // SECTION 9: Multi-Tab & State Consistency
  377 |   // ==========================================================================
  378 | 
  379 |   test('Multiple tabs can view session-expired page independently', async ({ page, context }) => {
  380 |     await page.goto('/session-expired');
  381 |     await page.waitForLoadState('networkidle');
  382 | 
  383 |     const page2 = await context.newPage();
  384 |     await page2.goto('/session-expired');
  385 |     await page2.waitForLoadState('networkidle');
  386 | 
  387 |     await expect(page.locator('text=Your session expired')).toBeVisible();
  388 |     await expect(page2.locator('text=Your session expired')).toBeVisible();
  389 | 
  390 |     await page2.close();
  391 |   });
  392 | 
  393 |   test('Window resize during auth loading completes navigation', async ({ page }) => {
  394 |     await page.goto('/auth/loading');
  395 |     await page.setViewportSize({ width: 800, height: 600 });
  396 |     await page.waitForURL('**/', { timeout: 5000 });
  397 |     await page.setViewportSize({ width: 1280, height: 720 });
  398 |     expect(page.url()).not.toContain('/auth/loading');
  399 |   });
  400 | 
  401 |   // ==========================================================================
  402 |   // SECTION 10: Security Validation
  403 |   // ==========================================================================
  404 | 
  405 |   test('No sensitive data in page source for session pages', async ({ page }) => {
  406 |     const sessionPages = ['/session-expired', '/access-denied', '/auth/loading'];
  407 |     for (const p of sessionPages) {
  408 |       await page.goto(p);
  409 |       const html = await page.content();
  410 |       const body = await page.locator('body').innerText();
  411 |       expect(body.toLowerCase()).not.toContain('password');
  412 |       expect(body.toLowerCase()).not.toContain('jwt');
  413 |       expect(body.toLowerCase()).not.toContain('secret');
```