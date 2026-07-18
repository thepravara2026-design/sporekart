# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: checkout-security-a11y-perf.spec.ts >> Checkout Security, A11y, Performance >> Unauth /dashboard/addresses redirects
- Location: tests\checkout-security-a11y-perf.spec.ts:16:7

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
    - banner [ref=e5]:
      - navigation "Account actions" [ref=e6]:
        - menuitem "Settings" [ref=e8] [cursor=pointer]:
          - img [ref=e10]
          - generic [ref=e13]: Settings
        - menuitem "Help" [ref=e15] [cursor=pointer]:
          - img [ref=e17]
          - generic [ref=e20]: Help
        - menuitem "Logout" [ref=e22] [cursor=pointer]:
          - img [ref=e24]
          - generic [ref=e27]: Logout
    - generic [ref=e28]:
      - complementary [ref=e29]:
        - navigation "Customer workspace navigation" [ref=e30]:
          - generic [ref=e31]:
            - generic [ref=e33]:
              - img [ref=e35]
              - generic [ref=e38]: SporeKart
            - button "Collapse sidebar" [ref=e39] [cursor=pointer]:
              - img [ref=e40]
          - menu [ref=e43]:
            - menuitem "Dashboard" [ref=e47] [cursor=pointer]:
              - img [ref=e49]
              - generic [ref=e51]: Dashboard
            - menuitem "Orders" [ref=e55] [cursor=pointer]:
              - img [ref=e57]
              - generic [ref=e60]: Orders
            - menuitem "Wishlist" [ref=e64] [cursor=pointer]:
              - img [ref=e66]
              - generic [ref=e68]: Wishlist
            - menuitem "Training" [ref=e72] [cursor=pointer]:
              - img [ref=e74]
              - generic [ref=e77]: Training
            - menuitem "Products" [ref=e81] [cursor=pointer]:
              - img [ref=e83]
              - generic [ref=e87]: Products
            - menuitem "Addresses" [ref=e91] [cursor=pointer]:
              - img [ref=e93]
              - generic [ref=e96]: Addresses
            - menuitem "Support" [ref=e100] [cursor=pointer]:
              - img [ref=e102]
              - generic [ref=e105]: Support
            - menuitem "Notifications" [ref=e109] [cursor=pointer]:
              - img [ref=e111]
              - generic [ref=e114]: Notifications
          - generic [ref=e116]: v1.0.0-beta · Customer Workspace
      - main [ref=e117]:
        - generic [ref=e119]:
          - generic [ref=e121]:
            - heading "Addresses" [level=1] [ref=e122]
            - paragraph [ref=e123]: Dashboard
          - navigation "Breadcrumb" [ref=e124]:
            - list [ref=e125]:
              - listitem [ref=e126]:
                - link "Dashboard" [ref=e127] [cursor=pointer]:
                  - /url: /dashboard
                  - generic [ref=e128]: Dashboard
              - listitem [ref=e129]:
                - img [ref=e130]
              - listitem [ref=e132]:
                - generic [ref=e134]: Addresses
          - generic [ref=e136]:
            - img [ref=e138]
            - heading "Addresses" [level=1] [ref=e141]
            - paragraph [ref=e142]: Manage shipping and billing addresses.
            - button "Add Address" [ref=e143] [cursor=pointer]:
              - generic [ref=e144]: Add Address
          - generic [ref=e145]:
            - generic [ref=e146]: © 2026 SporeKart. All rights reserved.
            - navigation [ref=e147]:
              - link "Privacy" [ref=e148] [cursor=pointer]:
                - /url: /privacy-policy
              - link "Terms" [ref=e149] [cursor=pointer]:
                - /url: /terms-and-conditions
              - link "Support" [ref=e150] [cursor=pointer]:
                - /url: /support
```

# Test source

```ts
  1  | ﻿import { test, expect } from '@playwright/test';
  2  | const PHONE = '9876543210';
  3  | async function login(page) {
  4  |   await page.goto('/login'); await page.waitForLoadState('networkidle');
  5  |   const inp = page.locator('input[id="sk-identifier"],input[type="tel"],input[inputmode="numeric"]').first();
  6  |   await inp.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  7  |   await inp.fill(PHONE);
  8  |   const btn = page.locator('button[type="submit"],button:has-text("Send"),button:has-text("Continue")').first();
  9  |   await btn.click(); await page.waitForTimeout(2000);
  10 |   const otp = page.locator('.sk-otp-input,input[maxlength="1"][inputmode="numeric"]');
  11 |   const n = await otp.count();
  12 |   if (n > 0) { for (let i = 0; i < Math.min(n, 6); i++) await otp.nth(i).fill(String(i + 1)); await page.waitForTimeout(3000); }
  13 | }
  14 | test.describe('Checkout Security, A11y, Performance', () => {
  15 |   test('Unauth /admin/orders redirects',async({page})=>{await page.goto('/admin/orders',{waitUntil:'networkidle'});expect(page.url().includes('login')||page.url().includes('auth')).toBeTruthy();});
> 16 |   test('Unauth /dashboard/addresses redirects',async({page})=>{await page.goto('/dashboard/addresses',{waitUntil:'networkidle'});expect(page.url().includes('login')||page.url().includes('auth')).toBeTruthy();});
     |                                                                                                                                                                                                    ^ Error: expect(received).toBeTruthy()
  17 |   test('Session-expired page renders',async({page})=>{await page.goto('/session-expired',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  18 |   test('Access-denied page renders',async({page})=>{await page.goto('/access-denied',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  19 |   test('Skip to content link',async({page})=>{await page.goto('/');const l=page.locator('a[href="#main-content"],a[href="#content"],a:has-text("Skip"),[class*="skip"]');await expect(l.first()).toBeVisible({timeout:5000});});
  20 |   test('ARIA landmarks',async({page})=>{await page.goto('/');await expect(page.locator('main,[role="main"]').first()).toBeVisible({timeout:5000});await expect(page.locator('nav,[role="navigation"]').first()).toBeVisible({timeout:5000});await expect(page.locator('footer,[role="contentinfo"]').first()).toBeVisible({timeout:5000});});
  21 |   test('Images have alt text',async({page})=>{await page.goto('/');const imgs=page.locator('img');const c=await imgs.count();let missing=0;for(let i=0;i<c;i++){const alt=await imgs.nth(i).getAttribute('alt');if(alt===null||alt===undefined)missing++;}expect(missing).toBe(0);});
  22 |   test('Performance: orders load <10s',async({page})=>{const s=Date.now();await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(Date.now()-s).toBeLessThan(15000);});
  23 |   test('No console errors on orders',async({page})=>{const errs=[];page.on('console',m=>{if(m.type()==='error')errs.push(m.text())});await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(errs.length).toBe(0);});
  24 |   test('No failed network requests',async({page})=>{const fails=[];page.on('requestfailed',r=>fails.push(r.url()));await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(fails.length).toBe(0);});
  25 | });
  26 | 
```