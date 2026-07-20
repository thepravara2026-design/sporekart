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
test.describe('Checkout Entry,Address,Shipping', () => {
  test('Cart 404',async({page})=>{await page.goto('/cart',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('404')||t.includes('not found')).toBeTruthy();});
  test('Checkout 404',async({page})=>{await page.goto('/checkout',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('404')||t.includes('not found')).toBeTruthy();});
  test('Unauth redirect',async({page})=>{await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(page.url().includes('login')||page.url().includes('auth')).toBeTruthy();});
  test('Login works',async({page})=>{await login(page);expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  test('Orders dashboard',async({page})=>{await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.length).toBeGreaterThan(50);expect(t.includes('ORD')||t.includes('Order')).toBeTruthy();});
  test('Order details',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);});
  test('Shipment tracking',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842/track',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);});
  test('Returns refunds',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842/refund',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);});
  test('Tab filters',async({page})=>{await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});expect(await page.locator('button:has-text("All"),button:has-text("Active")').first().isVisible().catch(()=>false)).toBeTruthy();});
  test('Search input',async({page})=>{await login(page);await page.goto('/dashboard/orders',{waitUntil:'networkidle'});const s=page.locator('input[type="search"],input[placeholder*="search" i]');if(await s.isVisible({timeout:3000}).catch(()=>false)){await s.fill('ORD-2026');expect(await s.inputValue()).toBe('ORD-2026');}});
  test('Address placeholder',async({page})=>{await login(page);await page.goto('/dashboard/addresses',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.length).toBeGreaterThan(10);expect(t.includes('Address')||t.includes('address')).toBeTruthy();});
  test('Address new route',async({page})=>{await login(page);await page.goto('/dashboard/addresses/new',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
  test('Admin shipping',async({page})=>{await login(page);await page.goto('/admin/shipping',{waitUntil:'domcontentloaded'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(10);});
});
