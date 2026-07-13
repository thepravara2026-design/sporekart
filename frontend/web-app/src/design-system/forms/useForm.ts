import { useState, useCallback, useRef } from 'react';
import type { FormConfig, FormContextValue, FormState, FieldConfig, FieldState, FormErrors, FieldWarnings, FormTouched, FormDirty } from './types';
import { validateValue, validateAllFields, hasErrors } from './validation';

export function useForm(config: FormConfig): FormContextValue {
  const {
    fields: fieldConfigs,
    onSubmit,
    validateOnChange = true,
    validateOnBlur = true,
    initialValues = {},
  } = config;

  const [values, setValues] = useState<Record<string, any>>(() => {
    const vals: Record<string, any> = { ...initialValues };
    fieldConfigs.forEach(f => { if (!(f.name in vals)) vals[f.name] = f.initialValue ?? ''; });
    return vals;
  });

  const [errors, setErrors] = useState<FormErrors>({});
  const [warnings, setWarnings] = useState<FieldWarnings>({});
  const [touched, setTouched] = useState<FormTouched>({});
  const [dirty, setDirty] = useState<FormDirty>({});
  const [formState, setFormState] = useState<FormState>('idle');
  const [fieldStates, setFieldStates] = useState<Record<string, FieldState>>({});

  const fieldDefs = useRef<Record<string, FieldConfig>>({});
  
  // Register/unregister fields
  const registerField = useCallback((config: FieldConfig) => {
    fieldDefs.current[config.name] = config;
    setValues(prev => {
      if (!(config.name in prev)) return { ...prev, [config.name]: config.initialValue ?? '' };
      return prev;
    });
  }, []);

  const unregisterField = useCallback((name: string) => {
    delete fieldDefs.current[name];
    setValues(prev => { const { [name]: _, ...rest } = prev; return rest; });
    setErrors(prev => { const { [name]: _, ...rest } = prev; return rest; });
  }, []);

  // Set field value
  const setFieldValue = useCallback((name: string, value: any) => {
    setValues(prev => {
      const next = { ...prev, [name]: value };
      const isDirty = value !== (fieldDefs.current[name]?.initialValue ?? '');
      setDirty(prevDirty => ({ ...prevDirty, [name]: isDirty }));
      return next;
    });
    setFieldStates(prev => ({ ...prev, [name]: value ? 'typing' : 'idle' }));
    
    if (validateOnChange && fieldDefs.current[name]?.rules) {
      validateValue(value, fieldDefs.current[name].rules!, values).then(error => {
        setErrors(prev => error ? { ...prev, [name]: error } : { ...prev, [name]: undefined });
      });
    }
  }, [validateOnChange, values]);

  // Set field touched
  const setFieldTouched = useCallback((name: string, touchedVal = true) => {
    setTouched(prev => ({ ...prev, [name]: touchedVal }));
    if (validateOnBlur && fieldDefs.current[name]?.rules) {
      const value = values[name];
      validateValue(value, fieldDefs.current[name].rules!, values).then(error => {
        setErrors(prev => error ? { ...prev, [name]: error } : { ...prev, [name]: undefined });
      });
    }
  }, [validateOnBlur, values]);

  // Set field error manually
  const setFieldError = useCallback((name: string, error?: string) => {
    setErrors(prev => error ? { ...prev, [name]: error } : { ...prev, [name]: undefined });
  }, []);

  // Set field warning
  const setFieldWarning = useCallback((name: string, warning?: string) => {
    setWarnings(prev => warning ? { ...prev, [name]: warning } : { ...prev, [name]: undefined });
  }, []);

  // Validate single field
  const validateField = useCallback(async (name: string): Promise<boolean> => {
    const config = fieldDefs.current[name];
    if (!config?.rules || config.rules.length === 0) return true;
    const error = await validateValue(values[name], config.rules, values);
    setErrors(prev => error ? { ...prev, [name]: error } : { ...prev, [name]: undefined });
    setFieldStates(prev => ({ ...prev, [name]: error ? 'invalid' : 'valid' }));
    return !error;
  }, [values]);

  // Validate entire form
  const validateForm = useCallback(async (): Promise<boolean> => {
    const allErrors = await validateAllFields(values, fieldDefs.current);
    setErrors(allErrors);
    const hasErr = hasErrors(allErrors);
    setFormState(hasErr ? 'invalid' : 'valid');
    return !hasErr;
  }, [values]);

  // Submit form
  const submitForm = useCallback(async () => {
    setFormState('submitting');
    const valid = await validateForm();
    if (!valid) {
      setFormState('invalid');
      // Focus first invalid field
      const firstError = Object.entries(errors).find(([_, e]) => e);
      if (firstError) {
        const el = document.querySelector(`[name="${firstError[0]}"]`) as HTMLElement;
        el?.focus();
      }
      return;
    }
    try {
      await onSubmit(values);
      setFormState('success');
    } catch {
      setFormState('failure');
    }
  }, [validateForm, errors, onSubmit, values]);

  // Reset form
  const resetForm = useCallback((newValues?: Record<string, any>) => {
    const vals = newValues || { ...initialValues };
    Object.keys(fieldDefs.current).forEach(name => {
      if (!(name in vals)) vals[name] = fieldDefs.current[name].initialValue ?? '';
    });
    setValues(vals);
    setErrors({});
    setWarnings({});
    setTouched({});
    setDirty({});
    setFormState('idle');
    setFieldStates({});
  }, [initialValues]);

  // Get field state
  const getFieldState = useCallback((name: string): FieldState => {
    if (fieldDefs.current[name]?.disabled) return 'disabled';
    if (fieldDefs.current[name]?.readOnly) return 'readOnly';
    if (errors[name]) return 'invalid';
    if (warnings[name]) return 'warning';
    return fieldStates[name] || 'idle';
  }, [errors, warnings, fieldStates]);

  const isSubmitting = formState === 'submitting';
  const isDirty = Object.values(dirty).some(Boolean);
  const isValid = formState !== 'invalid';

  return {
    values, errors, warnings, touched, dirty,
    formState, fieldStates,
    registerField, unregisterField,
    setFieldValue, setFieldTouched,
    setFieldError, setFieldWarning,
    validateField, validateForm,
    submitForm, resetForm, setFormState,
    getFieldState, isSubmitting, isDirty, isValid,
  };
}
