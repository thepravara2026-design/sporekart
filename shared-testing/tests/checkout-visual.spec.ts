import { test, expect } from '@playwright/test';
test.describe('Checkout Visual Review', () => {
  test('Homepage renders at desktop',async({page})=>{await page.setViewportSize({width:1440,height:900});await page.goto('/');await page.waitForLoadState('networkidle');expect(await page.locator('section').count()).toBeGreaterThanOrEqual(3);});
  test('Homepage renders at mobile',async({page})=>{await page.setViewportSize({width:375,height:667});await page.goto('/');await page.waitForLoadState('networkidle');expect(await page.locator('body').innerText()).toBeTruthy();});
  test('404 page has content',async({page})=>{await page.goto('/this-does-not-exist',{waitUntil:'networkidle'});expect((await page.locator('body').innerText()).length).toBeGreaterThan(5);});
  test('Typography is consistent',async({page})=>{await page.goto('/');const h1=page.locator('h1').first();if(await h1.isVisible().catch(()=>false)){const fs=await h1.evaluate(el=>getComputedStyle(el).fontSize);expect(parseFloat(fs)).toBeGreaterThan(16);}});
  test('No horizontal scroll',async({page})=>{await page.goto('/');const hs=await page.evaluate(()=>document.documentElement.scrollWidth>document.documentElement.clientWidth);expect(hs).toBe(false);});
  test('Cross-browser: key pages render at desktop',async({page})=>{const routes=['/','/products','/about','/contact','/login'];for(const r of routes){await page.setViewportSize({width:1440,height:900});const res=await page.goto(r);expect(res?.status()).toBeLessThan(400);await page.waitForLoadState('networkidle');}});
  test('Cross-browser: key pages render at mobile',async({page})=>{const routes=['/','/login','/products'];for(const r of routes){await page.setViewportSize({width:375,height:667});const res=await page.goto(r);expect(res?.status()).toBeLessThan(400);await page.waitForLoadState('networkidle');}});
});
