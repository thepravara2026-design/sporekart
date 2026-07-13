import type { ValidationRule } from './types';

// Built-in validators
export const required = (message?: string): ValidationRule => ({
  type: 'required',
  message: message || 'This field is required',
  validator: (value: any) => {
    if (value === null || value === undefined) return false;
    if (typeof value === 'string') return value.trim().length > 0;
    if (Array.isArray(value)) return value.length > 0;
    return true;
  }
});

export const minLength = (min: number, message?: string): ValidationRule => ({
  type: 'minLength',
  value: min,
  message: message || `Must be at least ${min} characters`,
  validator: (value: any) => {
    if (!value) return true;
    return String(value).length >= min;
  }
});

export const maxLength = (max: number, message?: string): ValidationRule => ({
  type: 'maxLength',
  value: max,
  message: message || `Must be no more than ${max} characters`,
  validator: (value: any) => {
    if (!value) return true;
    return String(value).length <= max;
  }
});

export const min = (min: number, message?: string): ValidationRule => ({
  type: 'min',
  value: min,
  message: message || `Must be at least ${min}`,
  validator: (value: any) => {
    if (value === null || value === undefined || value === '') return true;
    return Number(value) >= min;
  }
});

export const max = (max: number, message?: string): ValidationRule => ({
  type: 'max',
  value: max,
  message: message || `Must be no more than ${max}`,
  validator: (value: any) => {
    if (value === null || value === undefined || value === '') return true;
    return Number(value) <= max;
  }
});

export const pattern = (regex: RegExp, message?: string): ValidationRule => ({
  type: 'pattern',
  value: regex,
  message: message || 'Invalid format',
  validator: (value: any) => {
    if (!value) return true;
    return regex.test(String(value));
  }
});

export const email = (message?: string): ValidationRule => ({
  type: 'email',
  message: message || 'Please enter a valid email address',
  validator: (value: any) => {
    if (!value) return true;
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(String(value));
  }
});

export const phone = (message?: string): ValidationRule => ({
  type: 'phone',
  message: message || 'Please enter a valid phone number',
  validator: (value: any) => {
    if (!value) return true;
    return /^[+]?[\d\s()-]{7,15}$/.test(String(value));
  }
});

export const url = (message?: string): ValidationRule => ({
  type: 'url',
  message: message || 'Please enter a valid URL',
  validator: (value: any) => {
    if (!value) return true;
    try { new URL(String(value)); return true; }
    catch { return false; }
  }
});

export const password = (message?: string): ValidationRule => ({
  type: 'password',
  message: message || 'Password must contain uppercase, lowercase, number, and special character (min 8 chars)',
  validator: (value: any) => {
    if (!value) return true;
    const s = String(value);
    return /[A-Z]/.test(s) && /[a-z]/.test(s) && /[0-9]/.test(s) && /[^A-Za-z0-9]/.test(s) && s.length >= 8;
  }
});

export const numeric = (message?: string): ValidationRule => ({
  type: 'numeric',
  message: message || 'Must be a number',
  validator: (value: any) => {
    if (value === null || value === undefined || value === '') return true;
    return !isNaN(Number(value));
  }
});

export const custom = (validatorFn: (value: any, formValues?: Record<string, any>) => boolean | string, message?: string): ValidationRule => ({
  type: 'custom',
  message: message || 'Invalid value',
  validator: validatorFn
});

export const crossField = (validatorFn: (values: Record<string, any>) => Record<string, string | undefined>, message?: string): ValidationRule => ({
  type: 'crossField',
  message: message || 'Fields do not match',
  validator: (_value: any, formValues?: Record<string, any>) => {
    if (!formValues) return true;
    const errors = validatorFn(formValues);
    return Object.keys(errors).length === 0;
  }
});

// Validation engine: runs rules against a value
export async function validateValue(value: any, rules: ValidationRule[], formValues?: Record<string, any>): Promise<string | null> {
  for (const rule of rules) {
    if (rule.validator) {
      const result = rule.validator(value, formValues);
      if (result instanceof Promise) {
        const resolved = await result;
        if (resolved === false || typeof resolved === 'string') {
          return typeof resolved === 'string' ? resolved : rule.message;
        }
      } else if (result === false) {
        return rule.message;
      } else if (typeof result === 'string') {
        return result;
      }
    }
  }
  return null;
}

// Validate all fields and return errors map
export async function validateAllFields(values: Record<string, any>, fieldConfigs: Record<string, { rules?: ValidationRule[] }>): Promise<Record<string, string | undefined>> {
  const errors: Record<string, string | undefined> = {};
  const entries = Object.entries(fieldConfigs);
  const results = await Promise.all(
    entries.map(async ([name, config]) => {
      if (config.rules && config.rules.length > 0) {
        const error = await validateValue(values[name], config.rules, values);
        return [name, error] as [string, string | null];
      }
      return [name, null] as [string, string | null];
    })
  );
  results.forEach(([name, error]) => {
    if (error) errors[name] = error;
  });
  return errors;
}

export function hasErrors(errors: Record<string, string | undefined>): boolean {
  return Object.values(errors).some(e => e !== undefined && e !== null);
}
