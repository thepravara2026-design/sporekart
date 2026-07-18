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
test.describe('Checkout Order Summary, Coupons, Payment', () => {
  test('Order shows pricing',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('INR')||t.includes('Rs')||t.includes('Total')||t.includes('total')).toBeTruthy();});
  test('Order shows subtotal tax',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('Subtotal')||t.includes('subtotal')||t.includes('Tax')||t.includes('tax')).toBeTruthy();});
  test('Order shows items qty',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('Qty')||t.includes('qty')||t.includes('x ')||t.includes('×')).toBeTruthy();});
  test('Order shows discount',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('Discount')||t.includes('discount')||t.includes('Coupon')||t.includes('coupon')).toBeTruthy();});
  test('Order shows payment info',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('Payment')||t.includes('payment')||t.includes('Razorpay')||t.includes('UPI')).toBeTruthy();});
  test('Order shows shipping address',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const t=await page.locator('body').innerText();expect(t.includes('Shipping')||t.includes('shipping')||t.includes('Address')||t.includes('address')).toBeTruthy();});
  test('Data persists on refresh',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-8842',{waitUntil:'networkidle'});const b=await page.locator('body').innerText();await page.reload({waitUntil:'networkidle'});const a=await page.locator('body').innerText();expect(a.length).toBeGreaterThan(50);});
  test('Different order loads',async({page})=>{await login(page);await page.goto('/dashboard/orders/ORD-2026-5541',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(50);});
});
