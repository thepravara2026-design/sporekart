import { useEffect, useCallback } from 'react';
import { useFormContext } from './form-context';
import type { ValidationRule } from './types';

export interface UseFieldOptions {
  name: string;
  initialValue?: any;
  rules?: ValidationRule[];
  disabled?: boolean;
  readOnly?: boolean;
  validateOnChange?: boolean;
  validateOnBlur?: boolean;
}

export function useField(options: UseFieldOptions) {
  const { name, rules, disabled, readOnly } = options;
  const ctx = useFormContext();

  useEffect(() => {
    ctx.registerField({ name, initialValue: options.initialValue, rules, disabled, readOnly });
    return () => ctx.unregisterField(name);
  }, [name]);

  const value = ctx.values[name];
  const error = ctx.errors[name];
  const warning = ctx.warnings[name];
  const touched = ctx.touched[name];
  const dirty = ctx.dirty[name];
  const fieldState = ctx.getFieldState(name);

  const onChange = useCallback((e: any) => {
    const val = e?.target?.value !== undefined ? e.target.value : e;
    ctx.setFieldValue(name, val);
  }, [name, ctx]);

  const onBlur = useCallback(() => {
    ctx.setFieldTouched(name, true);
  }, [name, ctx]);

  const setValue = useCallback((val: any) => {
    ctx.setFieldValue(name, val);
  }, [name, ctx]);

  return {
    value,
    error,
    warning,
    touched,
    dirty,
    fieldState,
    onChange,
    onBlur,
    setValue,
    setError: (err?: string) => ctx.setFieldError(name, err),
    setWarning: (warn?: string) => ctx.setFieldWarning(name, warn),
    validate: () => ctx.validateField(name),
    name,
  };
}
