import React, { useState, useRef, useEffect, useId } from 'react';

export interface FormSectionProps {
  children: React.ReactNode;
  title?: string;
  description?: string;
  collapsible?: boolean;
  defaultOpen?: boolean;
  required?: boolean;
  icon?: React.ReactNode;
  className?: string;
  error?: string;
}

export const FormSection: React.FC<FormSectionProps> = ({
  children,
  title,
  description,
  collapsible = false,
  defaultOpen = true,
  required = false,
  icon,
  className = '',
  error,
}) => {
  const [isOpen, setIsOpen] = useState(defaultOpen);
  const contentRef = useRef<HTMLDivElement>(null);
  const [contentHeight, setContentHeight] = useState<number | undefined>(
    defaultOpen ? undefined : 0
  );
  const generatedId = useId();
  const sectionId = `sk-form-section-${generatedId}`;
  const contentId = `sk-form-section-content-${generatedId}`;

  useEffect(() => {
    if (contentRef.current) {
      setContentHeight(contentRef.current.scrollHeight);
    }
  }, [children]);

  const toggleOpen = () => {
    setIsOpen((prev) => !prev);
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      toggleOpen();
    }
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    width: '100%',
    cursor: collapsible ? 'pointer' : 'default',
    border: 'none',
    background: 'none',
    padding: 'var(--space-0)',
    font: 'inherit',
    textAlign: 'left',
    color: 'inherit',
  };

  const chevronStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    transition: 'transform var(--duration-fast) var(--easing-standard)',
    transform: isOpen ? 'rotate(180deg)' : 'rotate(0deg)',
    flexShrink: 0,
    width: 'var(--icon-sm)',
    height: 'var(--icon-sm)',
  };

  const titleWrapperStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-xs)',
    flex: 1,
  };

  const titleRowStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
  };

  const titleTextStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontWeight: 'var(--weight-semibold)',
    fontSize: 'var(--text-h6)',
    lineHeight: 'var(--leading-normal)',
    color: 'var(--color-text-primary)',
    margin: 0,
  };

  const requiredBadgeStyle: React.CSSProperties = {
    color: 'var(--color-text-danger)',
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-medium)',
    marginLeft: 'var(--space-inline-xs)',
  };

  const descriptionStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-body-sm)',
    lineHeight: 'var(--leading-normal)',
    color: 'var(--color-text-secondary)',
    margin: 0,
  };

  const contentWrapperStyle: React.CSSProperties = {
    overflow: 'hidden',
    transition: 'max-height var(--duration-slow) var(--easing-standard), opacity var(--duration-fast) var(--easing-standard)',
    maxHeight: isOpen ? `${contentHeight ?? 0}px` : '0px',
    opacity: isOpen ? 1 : 0,
  };

  const contentInnerStyle: React.CSSProperties = {
    paddingTop: 'var(--space-stack-md)',
  };

  const errorStyle: React.CSSProperties = {
    color: 'var(--color-text-danger)',
    fontSize: 'var(--text-caption)',
    marginTop: 'var(--space-stack-xs)',
  };

  const sectionStyle: React.CSSProperties = {
    marginBottom: 'var(--space-stack-lg)',
  };

  const legendStyle: React.CSSProperties = {
    padding: 0,
    margin: 0,
    border: 'none',
    float: 'none',
    width: 'auto',
  };

  const renderHeader = () => {
    if (!title) return null;

    const headerContent = (
      <div style={headerStyle}>
        {collapsible && (
          <span style={chevronStyle} aria-hidden="true">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
              <path d="M4 6L8 10L12 6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
            </svg>
          </span>
        )}
        {icon && (
          <span aria-hidden="true" style={{ flexShrink: 0 }}>
            {icon}
          </span>
        )}
        <div style={titleWrapperStyle}>
          <div style={titleRowStyle}>
            <h3 style={titleTextStyle}>{title}</h3>
            {required && <span style={requiredBadgeStyle} aria-label="Required">*</span>}
          </div>
          {description && (
            <p style={descriptionStyle}>{description}</p>
          )}
        </div>
      </div>
    );

    if (collapsible) {
      return (
        <button
          type="button"
          onClick={toggleOpen}
          onKeyDown={handleKeyDown}
          aria-expanded={isOpen}
          aria-controls={contentId}
          style={{ ...headerStyle, cursor: 'pointer', border: 'none', background: 'none', padding: 0, font: 'inherit', color: 'inherit', textAlign: 'left', width: '100%' }}
        >
          {headerContent}
        </button>
      );
    }

    return headerContent;
  };

  const Wrapper = title ? 'fieldset' : 'div';

  return (
    <Wrapper
      id={sectionId}
      className={`sk-form-section ${className}`}
      style={sectionStyle}
      role={title ? 'group' : undefined}
      aria-labelledby={title ? `${sectionId}-title` : undefined}
    >
      {title && (
        <legend style={legendStyle} id={`${sectionId}-title`}>
          {renderHeader()}
        </legend>
      )}
      <div
        id={contentId}
        ref={contentRef}
        role="region"
        aria-labelledby={title ? `${sectionId}-title` : undefined}
        style={collapsible ? contentWrapperStyle : undefined}
      >
        <div style={collapsible ? contentInnerStyle : undefined}>
          {children}
        </div>
      </div>
      {error && (
        <div style={errorStyle} role="alert">
          {error}
        </div>
      )}
    </Wrapper>
  );
};

FormSection.displayName = 'FormSection';

export default FormSection;
