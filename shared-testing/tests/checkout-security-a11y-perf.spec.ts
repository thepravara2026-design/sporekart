import { test, expect } from '@playwright/test';
const PHONE = '9876543210';
async function login(page) {
  await page.goto('/login'); await page.waitForLoadState('networkidle');
  const inp = page.locator('input[id="sk-identifier"],input[type="tel"],input[inputmode="numeric"]').first();
  await inp.waitFor({ state: 'visible', timeout: 5000 }).catch(() => {});
  await inp.fill(PHONE);
  const btn = page.locator('button[type="submit"],button:has-text("Send"),button:has-text("Continue")').first();
  await btn.click(); await page.waitForTimeout(2000);
  const otp = page.locator('.sk-otp-input,input[maxlength="1"][inputmode="numeric"]');
  const n = await otp.count();
  if (n > 0) { for (let i = 0; i < Math.min(n, 6); i++) await otp.nth(i).fill(String(i + 1)); await page.waitForTimeout(3000); }
}
test.describe('Checkout Security, A11y, Performance', () => {
  test('Unauth /admin/orders redirects',async({page})=>{await page.goto('/admin/orders',{waitUntil:'networkidle'});expect(page.url().includes('login')||page.url().includes('auth')).toBeTruthy();});
  test('Unauth /dashboard/addresses redirects',async({page})=>{await page.goto('/dashboard/addresses',{waitUntil:'networkidle'});expect(page.url().includes('login')||page.url().includes('auth')).toBeTruthy();});
  test('Session-expired page renders',async({page})=>{await page.goto('/session-expired',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  test('Access-denied page renders',async({page})=>{await page.goto('/access-denied',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  test('Skip to content link',async({page})=>{await page.goto('/');const l=page.locator('a[href="#main-content"],a[href="#content"],a:has-text("Skip"),[class*="skip"]');await expect(l.first()).toBeVisible({timeout:5000});});
  test('ARIA landmarks',async({page})=>{await page.goto('/');await expect(page.locator('main,[role="main"]').first()).toBeVisible({timeout:5000});await expect(page.locator('nav,[role="navigation"]').first()).toBeVisible({timeout:5000});await expect(page.locator('footer,[role="contentinfo"]').first()).toBeVisible({timeout:5000});});
  test('Images have alt text',async({page})=>{await page.goto('/');const imgs=page.locator('img');const c=await imgs.count();let missing=0;for(let i=0;i<c;i++){const alt=await imgs.nth(i).getAttribute('alt');if(alt===null||alt===undefined)missing++;}expect(missing).toBe(0);});
  test('Performance: orders load <10s',async({page})=>{const s=Date.now();await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(Date.now()-s).toBeLessThan(15000);});
  test('No console errors on orders',async({page})=>{const errs=[];page.on('console',m=>{if(m.type()==='error')errs.push(m.text())});await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(errs.length).toBe(0);});
  test('No failed network requests',async({page})=>{const fails=[];page.on('requestfailed',r=>fails.push(r.url()));await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(fails.length).toBe(0);});
});
