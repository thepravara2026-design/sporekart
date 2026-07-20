import { Page, expect } from '@playwright/test';

export async function expectVisible(page: Page, selector: string): Promise<void> {
  await expect(page.locator(selector).first()).toBeVisible();
}

export async function expectNotVisible(page: Page, selector: string): Promise<void> {
  await expect(page.locator(selector).first()).not.toBeVisible();
}

export async function expectText(page: Page, text: string): Promise<void> {
  await expect(page.locator(`text="${text}"`).first()).toBeVisible();
}

export async function expectNoText(page: Page, text: string): Promise<void> {
  await expect(page.locator(`text="${text}"`).first()).not.toBeVisible();
}

export async function expectInputValue(page: Page, selector: string, value: string): Promise<void> {
  await expect(page.locator(selector)).toHaveValue(value);
}

export async function expectCount(page: Page, selector: string, count: number): Promise<void> {
  await expect(page.locator(selector)).toHaveCount(count);
}

export async function expectTitle(page: Page, title: string): Promise<void> {
  await expect(page).toHaveTitle(title);
}
