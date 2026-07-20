import { Page } from '@playwright/test';
import { getBaseUrl } from '../utils/env';
import { personas, Persona } from '../mock-data/personas';

export async function loginAs(page: Page, role: string): Promise<void> {
  const persona = Object.values(personas).find((p: Persona) => p.role === role);
  if (!persona) throw new Error(`No persona found for role: ${role}`);
  await page.goto(`${getBaseUrl()}/login`);
  await page.waitForLoadState('networkidle');
  if (persona.phone) {
    await page.fill('input[type="tel"], input[name="phone"], input[placeholder*="phone" i]', persona.phone);
  } else {
    return;
  }
  await page.click('button:has-text("Send secure code")');
  await page.waitForTimeout(500);
  const otpInputs = page.locator('input[type="tel"][maxlength="1"], .otp-input, input[aria-label*="OTP" i]');
  const otpCount = await otpInputs.count();
  if (otpCount > 0) {
    for (let i = 0; i < otpCount && i < 6; i++) {
      await otpInputs.nth(i).fill(String(Math.floor(Math.random() * 9) + 1));
    }
    await page.click('button:has-text("Verify")');
  }
  await page.waitForTimeout(1000);
}

export async function setRole(page: Page, role: string): Promise<void> {
  const roleSelect = page.locator('select[aria-label*="role" i], #role-select, .role-switcher select');
  if (await roleSelect.isVisible()) {
    await roleSelect.selectOption(role);
    await page.waitForTimeout(500);
  }
}

export async function logout(page: Page): Promise<void> {
  const logoutBtn = page.locator('button:has-text("Sign out"), a:has-text("Sign out"), button:has-text("Logout")');
  if (await logoutBtn.isVisible()) {
    await logoutBtn.click();
    await page.waitForTimeout(500);
  }
}
