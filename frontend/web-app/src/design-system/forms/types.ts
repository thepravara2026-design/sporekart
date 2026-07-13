export type FormState = 'idle' | 'focused' | 'typing' | 'valid' | 'invalid' | 'warning' | 'loading' | 'submitting' | 'success' | 'failure' | 'disabled' | 'readOnly';
export type FieldState = 'idle' | 'focused' | 'typing' | 'valid' | 'invalid' | 'warning' | 'loading' | 'disabled' | 'readOnly';
export type ValidationStatus = 'idle' | 'validating' | 'valid' | 'invalid' | 'warning';

export interface FieldError {
  field: string;
  message: string;
  type: string;
}

export interface FormErrors {
  [fieldName: string]: string | undefined;
}

export interface FieldWarnings {
  [fieldName: string]: string | undefined;
}

export interface FormTouched {
  [fieldName: string]: boolean;
}

export interface FormDirty {
  [fieldName: string]: boolean;
}

export type ValidationRule = {
  type: string;
  value?: any;
  message: string;
  validator?: (value: any, formValues?: Record<string, any>) => boolean | string | Promise<boolean | string>;
};

export interface FieldConfig {
  name: string;
  initialValue?: any;
  rules?: ValidationRule[];
  disabled?: boolean;
  readOnly?: boolean;
  validateOnChange?: boolean;
  validateOnBlur?: boolean;
}

export interface FormConfig {
  fields: FieldConfig[];
  onSubmit: (values: Record<string, any>) => Promise<void> | void;
  validateOnChange?: boolean;
  validateOnBlur?: boolean;
  initialValues?: Record<string, any>;
}

export interface FormContextValue {
  values: Record<string, any>;
  errors: FormErrors;
  warnings: FieldWarnings;
  touched: FormTouched;
  dirty: FormDirty;
  formState: FormState;
  fieldStates: Record<string, FieldState>;
  registerField: (config: FieldConfig) => void;
  unregisterField: (name: string) => void;
  setFieldValue: (name: string, value: any) => void;
  setFieldTouched: (name: string, touched?: boolean) => void;
  setFieldError: (name: string, error?: string) => void;
  setFieldWarning: (name: string, warning?: string) => void;
  validateField: (name: string) => Promise<boolean>;
  validateForm: () => Promise<boolean>;
  submitForm: () => Promise<void>;
  resetForm: (values?: Record<string, any>) => void;
  setFormState: (state: FormState) => void;
  getFieldState: (name: string) => FieldState;
  isSubmitting: boolean;
  isDirty: boolean;
  isValid: boolean;
}
