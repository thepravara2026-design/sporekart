# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: session-management.spec.ts >> Part 2 — Session Management Validation >> Bookmark access to auth pages renders correctly
- Location: tests\session-management.spec.ts:247:7

# Error details

```
Error: expect(received).toBeTruthy()

Received: false
```

# Page snapshot

```yaml
- generic [ref=e2]:
  - link "Skip to content" [ref=e3] [cursor=pointer]:
    - /url: "#main"
  - generic [ref=e4]:
    - complementary [ref=e5]:
      - generic [ref=e6]:
        - generic [ref=e7]:
          - img [ref=e9]
          - text: SporeKart
        - heading [level=1] [ref=e12]: Welcome back to SporeKart.
        - paragraph [ref=e13]: Sign in to manage cultivars, training and orders — securely and in seconds.
        - list [ref=e14]:
          - listitem [ref=e15]:
            - img [ref=e17]
            - text: Traceable, lab-verified cultivars
          - listitem [ref=e20]:
            - img [ref=e22]
            - text: Enterprise-grade security & RBAC
          - listitem [ref=e24]:
            - img [ref=e26]
            - text: Reliable cold-chain delivery
      - blockquote [ref=e32]:
        - text: “SporeKart cut our onboarding from days to minutes and gave every partner a single source of truth.”
        - generic [ref=e33]: — Cultivation Lead, Partner Greenhouse
    - main [ref=e34]:
      - link "Skip to form" [ref=e35] [cursor=pointer]:
        - /url: "#auth-main"
      - generic [ref=e36]:
        - generic [ref=e37]:
          - generic [ref=e38]:
            - img [ref=e39]
            - text: Sign in
          - heading "Access your workspace" [level=2] [ref=e42]
          - paragraph [ref=e43]: Use your phone or email to receive a secure one-time code.
        - generic [ref=e45]:
          - radiogroup "Sign in method" [ref=e46]:
            - radio "Phone" [checked] [ref=e47] [cursor=pointer]:
              - img [ref=e48]
              - text: Phone
            - radio "Email" [ref=e50] [cursor=pointer]:
              - img [ref=e51]
              - text: Email
          - generic [ref=e54]:
            - generic [ref=e55]: Phone number*
            - generic [ref=e56]:
              - img [ref=e58]
              - textbox "Phone number" [ref=e60]:
                - /placeholder: +1 555 000 1234
          - generic [ref=e61]:
            - generic [ref=e63]:
              - checkbox "Remember me" [checked]
              - img [ref=e65]
              - generic [ref=e67]: Remember me
            - link "Forgot access?" [ref=e68] [cursor=pointer]:
              - /url: /forgot-password
          - generic [ref=e70]:
            - checkbox "I agree to the Terms of Service and Privacy Policy"
            - generic [ref=e72]: I agree to the Terms of Service and Privacy Policy
          - button "Send secure code" [ref=e74] [cursor=pointer]:
            - generic [ref=e75]: Send secure code
        - separator "or" [ref=e76]
        - group "Social sign-in options" [ref=e77]:
          - button "Continue with Google" [ref=e78] [cursor=pointer]:
            - img [ref=e79]
            - text: Continue with Google
          - button "Continue with Apple" [ref=e81] [cursor=pointer]:
            - img [ref=e82]
            - text: Continue with Apple
          - button "Continue with Facebook" [ref=e84] [cursor=pointer]:
            - img [ref=e85]
            - text: Continue with Facebook
        - paragraph [ref=e87]:
          - text: New to SporeKart?
          - link "Create an account" [ref=e88] [cursor=pointer]:
            - /url: /register
        - paragraph [ref=e89]:
          - text: Need help? Visit
          - link "Support" [ref=e90] [cursor=pointer]:
            - /url: /support
          - text: or review our
          - link "Privacy Policy" [ref=e91] [cursor=pointer]:
            - /url: /privacy-policy
          - text: .
```

# Test source

```ts
  161 |       k.toLowerCase().includes('auth') ||
  162 |       k.toLowerCase().includes('password') ||
  163 |       k.toLowerCase().includes('secret') ||
  164 |       k.toLowerCase().includes('credential') ||
  165 |       k.toLowerCase().includes('jwt') ||
  166 |       k.toLowerCase().includes('key')
  167 |     );
  168 | 
  169 |     expect(secretsFound).toEqual([]);
  170 |   });
  171 | 
  172 |   test('localStorage values contain no plain-text credentials', async ({ page }) => {
  173 |     await page.goto('/');
  174 |     await page.waitForLoadState('networkidle');
  175 | 
  176 |     const hasSecrets = await page.evaluate(() => {
  177 |       for (let i = 0; i < localStorage.length; i++) {
  178 |         const key = localStorage.key(i)!;
  179 |         const val = localStorage.getItem(key);
  180 |         if (!val) continue;
  181 |         try {
  182 |           const parsed = JSON.parse(val);
  183 |           const str = JSON.stringify(parsed).toLowerCase();
  184 |           if (str.includes('password') || str.includes('token') || str.includes('secret')) {
  185 |             return { key, val: str.substring(0, 200) };
  186 |           }
  187 |         } catch {
  188 |           if (val.toLowerCase().includes('password') || val.toLowerCase().includes('token')) {
  189 |             return { key, val: val.substring(0, 200) };
  190 |           }
  191 |         }
  192 |       }
  193 |       return null;
  194 |     });
  195 | 
  196 |     expect(hasSecrets).toBeNull();
  197 |   });
  198 | 
  199 |   test('Cookies — no authentication cookies set by the app', async ({ page }) => {
  200 |     await page.goto('/login');
  201 |     await page.waitForLoadState('networkidle');
  202 | 
  203 |     const cookies = await page.context().cookies();
  204 |     const authCookies = cookies.filter(c =>
  205 |       c.name.toLowerCase().includes('auth') ||
  206 |       c.name.toLowerCase().includes('session') ||
  207 |       c.name.toLowerCase().includes('token') ||
  208 |       c.name.toLowerCase().includes('sid')
  209 |     );
  210 |     expect(authCookies).toEqual([]);
  211 |   });
  212 | 
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
> 261 |       expect(url.endsWith(expectPath) || url.includes(expectPath)).toBeTruthy();
      |                                                                    ^ Error: expect(received).toBeTruthy()
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
  313 |     await expect(page.locator('button:has-text("Retry")')).toBeVisible();
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
```