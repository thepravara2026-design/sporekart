import React, { useId } from 'react';
import { useFormContext } from '../../forms/form-context';

export interface FormFieldProps {
  children: React.ReactNode;
  name?: string;
  label?: string;
  description?: string;
  helperText?: string;
  error?: string;
  warning?: string;
  success?: boolean;
  required?: boolean;
  disabled?: boolean;
  readOnly?: boolean;
  characterLimit?: number;
  currentLength?: number;
  className?: string;
  layout?: 'vertical' | 'horizontal';
  id?: string;
}

export const FormField: React.FC<FormFieldProps> = ({
  children,
  name,
  label,
  description,
  helperText,
  error: errorProp,
  warning,
  success = false,
  required = false,
  disabled = false,
  readOnly = false,
  characterLimit,
  currentLength,
  className = '',
  layout = 'vertical',
  id: idProp,
}) => {
  const generatedId = useId();
  const fieldId = idProp ?? `sk-field-${generatedId}`;
  const helperId = `sk-field-helper-${generatedId}`;
  const errorId = `sk-field-error-${generatedId}`;
  const descId = `sk-field-desc-${generatedId}`;

  let formCtx: ReturnType<typeof useFormContext> | null = null;
  try {
    formCtx = useFormContext();
  } catch {
    formCtx = null;
  }
  const fieldError = name && formCtx?.errors?.[name] ? formCtx.errors[name] : undefined;
  const error = errorProp ?? fieldError;

  const describedBy = [
    error ? errorId : null,
    helperText ? helperId : null,
    description ? descId : null,
  ].filter(Boolean).join(' ') || undefined;

  const fieldStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: layout === 'horizontal' ? 'row' : 'column',
    gap: layout === 'horizontal' ? 'var(--space-inline-md)' : 'var(--space-stack-xs)',
    alignItems: layout === 'horizontal' ? 'flex-start' : 'stretch',
    opacity: disabled ? 'var(--opacity-disabled)' : 1,
    pointerEvents: disabled ? 'none' : 'auto',
  };

  const labelStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-xs)',
    fontFamily: 'var(--font-family-sans)',
    fontWeight: 'var(--weight-medium)',
    fontSize: 'var(--text-label)',
    lineHeight: 'var(--leading-normal)',
    color: 'var(--color-text-primary)',
    cursor: disabled ? 'not-allowed' : 'pointer',
    minWidth: layout === 'horizontal' ? '120px' : undefined,
    paddingTop: layout === 'horizontal' ? 'var(--space-2)' : undefined,
    userSelect: 'none',
    WebkitFontSmoothing: 'antialiased',
    MozOsxFontSmoothing: 'grayscale',
  };

  const requiredIndicatorStyle: React.CSSProperties = {
    color: 'var(--color-text-danger)',
    marginLeft: 'var(--space-inline-xs)',
  };

  const optionalIndicatorStyle: React.CSSProperties = {
    color: 'var(--color-text-secondary)',
    fontWeight: 'var(--weight-normal)',
    fontSize: 'var(--text-caption)',
  };

  const descriptionStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    lineHeight: 'var(--leading-normal)',
    color: 'var(--color-text-secondary)',
    margin: 0,
    marginTop: 'var(--space-stack-xs)',
  };

  const helperStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    lineHeight: 'var(--leading-normal)',
    color: 'var(--color-text-secondary)',
    margin: 0,
    marginTop: 'var(--space-stack-xs)',
  };

  const errorStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    lineHeight: 'var(--leading-normal)',
    color: 'var(--color-text-danger)',
    margin: 0,
    marginTop: 'var(--space-stack-xs)',
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-xs)',
  };

  const warningStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    lineHeight: 'var(--leading-normal)',
    color: 'var(--color-text-warning)',
    margin: 0,
    marginTop: 'var(--space-stack-xs)',
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-xs)',
  };

  const successStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    lineHeight: 'var(--leading-normal)',
    color: 'var(--color-text-success)',
    margin: 0,
    marginTop: 'var(--space-stack-xs)',
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-xs)',
  };

  const charCounterStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    lineHeight: 'var(--leading-normal)',
    color: characterLimit && currentLength && currentLength > characterLimit
      ? 'var(--color-text-danger)'
      : 'var(--color-text-secondary)',
    textAlign: 'right',
    marginTop: 'var(--space-stack-xs)',
  };

  const contentStyle: React.CSSProperties = {
    flex: 1,
    width: '100%',
  };

  const renderChildren = () => {
    return React.Children.map(children, (child) => {
      if (React.isValidElement(child)) {
        return React.cloneElement(child as React.ReactElement<Record<string, unknown>>, {
          id: fieldId,
          'aria-describedby': describedBy,
          'aria-invalid': error ? true : undefined,
          'aria-required': required ? true : undefined,
          disabled: disabled || (child.props as Record<string, unknown>).disabled,
          readOnly: readOnly || (child.props as Record<string, unknown>).readOnly,
        });
      }
      return child;
    });
  };

  return (
    <div
      className={`sk-form-field sk-form-field--${layout} ${className}`}
      style={fieldStyle}
    >
      {label && (
        <label style={labelStyle} htmlFor={fieldId}>
          {label}
          {required ? (
            <span style={requiredIndicatorStyle} aria-hidden="true">*</span>
          ) : (
            <span style={optionalIndicatorStyle}>(optional)</span>
          )}
        </label>
      )}
      <div style={contentStyle}>
        {renderChildren()}
        {description && !error && !warning && !helperText && (
          <p style={descriptionStyle} id={descId}>{description}</p>
        )}
        {helperText && !error && (
          <p style={helperStyle} id={helperId}>{helperText}</p>
        )}
        {warning && !error && (
          <p style={warningStyle} role="alert">
            <span aria-hidden="true">⚠</span>
            {warning}
          </p>
        )}
        {success && !error && !warning && (
          <p style={successStyle}>
            <span aria-hidden="true">✓</span>
            Success
          </p>
        )}
        {error && (
          <p style={errorStyle} id={errorId} role="alert">
            <span aria-hidden="true">!</span>
            {error}
          </p>
        )}
        {characterLimit !== undefined && (
          <div style={charCounterStyle}>
            {currentLength ?? 0}/{characterLimit}
          </div>
        )}
      </div>
    </div>
  );
};

FormField.displayName = 'FormField';

export default FormField;
