import { useState } from 'react';

const Section = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
    <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>{label}</h2>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(400px, 1fr))', gap: '16px' }}>{children}</div>
  </section>
);

const PreviewBox = ({ label, children }: { label: string; children: React.ReactNode }) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
    {children}
  </div>
);

const CheckIcon = () => (
  <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
    <path d="M2 6l3 3 5-5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

interface Step {
  label: string;
  description?: string;
  optional?: boolean;
}

const steps: Step[] = [
  { label: 'Cart', description: 'Review items' },
  { label: 'Shipping', description: 'Delivery address' },
  { label: 'Payment', description: 'Payment method' },
  { label: 'Confirm', description: 'Review order' },
];

const verticalSteps: Step[] = [
  { label: 'Account info', description: 'Name, email, password' },
  { label: 'Company details', description: 'Business info', optional: true },
  { label: 'Verification', description: 'Verify identity' },
  { label: 'Complete', description: 'Finish setup' },
];

const stepCircle = (state: 'completed' | 'active' | 'pending', size: number = 28): React.CSSProperties => {
  const base: React.CSSProperties = {
    width: size,
    height: size,
    borderRadius: '50%',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: size < 28 ? 'var(--text-xs)' : 'var(--text-sm)',
    fontWeight: 'var(--weight-bold)',
    flexShrink: 0,
  };
  if (state === 'completed') return { ...base, background: 'var(--color-bg-primary-default)', color: '#fff' };
  if (state === 'active') return { ...base, background: 'var(--color-bg-primary-default)', color: '#fff', boxShadow: '0 0 0 3px var(--color-bg-surface-default), 0 0 0 5px var(--color-bg-primary-default)' };
  return { ...base, background: 'var(--color-bg-subtle)', color: 'var(--color-text-tertiary)' };
};

function HorizontalStepper() {
  const [current, setCurrent] = useState(2);
  return (
    <div style={{ fontFamily: 'var(--font-family)', width: '100%' }}>
      <div style={{ display: 'flex', alignItems: 'flex-start' }}>
        {steps.map((step, i) => {
          const state = i < current ? 'completed' : i === current ? 'active' : 'pending';
          return (
            <div key={step.label} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', position: 'relative' }}>
              {i > 0 && (
                <div style={{
                  position: 'absolute',
                  top: '14px',
                  right: 'calc(50% + 18px)',
                  left: 'calc(-50% + 18px)',
                  height: '2px',
                  background: i <= current ? 'var(--color-bg-primary-default)' : 'var(--color-bg-subtle)',
                }} />
              )}
              <button
                onClick={() => i <= current && setCurrent(i)}
                style={{
                  ...stepCircle(state),
                  cursor: i <= current ? 'pointer' : 'default',
                  position: 'relative',
                  zIndex: 1,
                  border: 'none',
                }}
              >
                {state === 'completed' ? <CheckIcon /> : i + 1}
              </button>
              <span style={{ fontSize: 'var(--text-xs)', marginTop: '6px', fontWeight: state === 'active' ? 'var(--weight-semibold)' : 'var(--weight-medium)', color: state === 'pending' ? 'var(--color-text-tertiary)' : 'var(--color-text-primary)', textAlign: 'center' }}>{step.label}</span>
            </div>
          );
        })}
      </div>
      <button
        onClick={() => setCurrent(Math.min(current + 1, steps.length - 1))}
        style={{ marginTop: '16px', padding: '6px 16px', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', fontSize: 'var(--text-sm)' }}
      >
        Next Step
      </button>
    </div>
  );
}

function VerticalStepper() {
  const [current, setCurrent] = useState(1);
  return (
    <div style={{ fontFamily: 'var(--font-family)', display: 'flex', flexDirection: 'column', gap: '0' }}>
      {verticalSteps.map((step, i) => {
        const state = i < current ? 'completed' : i === current ? 'active' : 'pending';
        return (
          <div key={step.label} style={{ display: 'flex', gap: '12px', position: 'relative', paddingBottom: i < verticalSteps.length - 1 ? '24px' : '0' }}>
            {i < verticalSteps.length - 1 && (
              <div style={{
                position: 'absolute',
                left: '13px',
                top: '32px',
                bottom: '0',
                width: '2px',
                background: i < current ? 'var(--color-bg-primary-default)' : 'var(--color-bg-subtle)',
              }} />
            )}
            <button
              onClick={() => i <= current && setCurrent(i)}
              style={{
                ...stepCircle(state),
                cursor: i <= current ? 'pointer' : 'default',
                border: 'none',
                position: 'relative',
                zIndex: 1,
              }}
            >
              {state === 'completed' ? <CheckIcon /> : i + 1}
            </button>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '2px', paddingTop: '4px' }}>
              <span style={{ fontSize: 'var(--text-sm)', fontWeight: state === 'active' ? 'var(--weight-semibold)' : 'var(--weight-medium)', color: state === 'pending' ? 'var(--color-text-tertiary)' : 'var(--color-text-primary)' }}>
                {step.label}
                {step.optional && <span style={{ fontSize: 'var(--text-xs)', color: 'var(--color-text-tertiary)', marginLeft: '6px', fontWeight: 'var(--weight-normal)' }}>(optional)</span>}
              </span>
              <span style={{ fontSize: 'var(--text-xs)', color: 'var(--color-text-tertiary)' }}>{step.description}</span>
            </div>
          </div>
        );
      })}
      <button
        onClick={() => setCurrent(Math.min(current + 1, verticalSteps.length - 1))}
        style={{ alignSelf: 'flex-start', marginTop: '8px', padding: '6px 16px', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', fontSize: 'var(--text-sm)' }}
      >
        Next Step
      </button>
    </div>
  );
}

function ProgressStepper() {
  const [progress, setProgress] = useState(45);
  return (
    <div style={{ fontFamily: 'var(--font-family)', display: 'flex', flexDirection: 'column', gap: '12px' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
        <div style={{ flex: 1, height: '8px', borderRadius: 'var(--radius-full)', background: 'var(--color-bg-subtle)', overflow: 'hidden' }}>
          <div style={{ width: `${progress}%`, height: '100%', borderRadius: 'var(--radius-full)', background: 'var(--color-bg-primary-default)', transition: 'width 0.3s' }} />
        </div>
        <span style={{ fontSize: 'var(--text-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{progress}%</span>
      </div>
      <div style={{ display: 'flex', gap: '8px' }}>
        <button onClick={() => setProgress(Math.max(0, progress - 10))} style={{ padding: '4px 12px', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', fontSize: 'var(--text-xs)' }}>-10%</button>
        <button onClick={() => setProgress(Math.min(100, progress + 10))} style={{ padding: '4px 12px', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', fontSize: 'var(--text-xs)' }}>+10%</button>
      </div>
    </div>
  );
}

function ClickableSteps() {
  const [current, setCurrent] = useState(0);
  const items = ['Personal', 'Address', 'Payment', 'Review'];
  return (
    <div style={{ fontFamily: 'var(--font-family)', display: 'flex', gap: '4px' }}>
      {items.map((item, i) => {
        const state = i < current ? 'completed' : i === current ? 'active' : 'pending';
        return (
          <button
            key={item}
            onClick={() => setCurrent(i)}
            style={{
              flex: 1,
              padding: '10px 8px',
              border: `1px solid ${i === current ? 'var(--color-bg-primary-default)' : 'var(--color-border-default)'}`,
              borderRadius: 'var(--radius-sm)',
              background: i === current ? 'var(--color-bg-primary-default)' : 'var(--color-bg-surface-default)',
              color: i === current ? '#fff' : 'var(--color-text-primary)',
              cursor: 'pointer',
              fontSize: 'var(--text-sm)',
              fontWeight: 'var(--weight-medium)',
              textAlign: 'center',
              transition: 'all 0.15s',
            }}
          >
            {state === 'completed' ? '✓ ' : ''}{item}
          </button>
        );
      })}
    </div>
  );
}

function WithOptionalLabels() {
  const [current, setCurrent] = useState(1);
  const items: { label: string; optional?: boolean }[] = [
    { label: 'Sign in' },
    { label: 'Shipping' },
    { label: 'Payment' },
    { label: 'Review' },
  ];
  return (
    <div style={{ fontFamily: 'var(--font-family)' }}>
      <div style={{ display: 'flex', alignItems: 'flex-start' }}>
        {items.map((item, i) => {
          const state = i < current ? 'completed' : i === current ? 'active' : 'pending';
          return (
            <div key={item.label} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', position: 'relative' }}>
              {i > 0 && (
                <div style={{
                  position: 'absolute',
                  top: '14px',
                  right: 'calc(50% + 18px)',
                  left: 'calc(-50% + 18px)',
                  height: '2px',
                  background: i <= current ? 'var(--color-bg-primary-default)' : 'var(--color-bg-subtle)',
                  zIndex: 0,
                }} />
              )}
              <button
                onClick={() => i <= current && setCurrent(i)}
                style={{
                  ...stepCircle(state),
                  cursor: i <= current ? 'pointer' : 'default',
                  border: 'none',
                  position: 'relative',
                  zIndex: 1,
                }}
              >
                {state === 'completed' ? <CheckIcon /> : i + 1}
              </button>
              <span style={{ fontSize: 'var(--text-xs)', marginTop: '6px', fontWeight: state === 'active' ? 'var(--weight-semibold)' : 'var(--weight-medium)', color: state === 'pending' ? 'var(--color-text-tertiary)' : 'var(--color-text-primary)', textAlign: 'center' }}>{item.label}</span>
              {item.optional && <span style={{ fontSize: 'var(--text-xs)', color: 'var(--color-text-tertiary)' }}>(optional)</span>}
            </div>
          );
        })}
      </div>
    </div>
  );
}

function StatesDemo() {
  return (
    <div style={{ fontFamily: 'var(--font-family)', display: 'flex', flexDirection: 'column', gap: '12px' }}>
      <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
        <div style={stepCircle('completed')}><CheckIcon /></div>
        <span style={{ fontSize: 'var(--text-sm)' }}>Completed step</span>
      </div>
      <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
        <div style={stepCircle('active')}>3</div>
        <span style={{ fontSize: 'var(--text-sm)', fontWeight: 'var(--weight-semibold)' }}>Active step</span>
      </div>
      <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
        <div style={stepCircle('pending')}>4</div>
        <span style={{ fontSize: 'var(--text-sm)', color: 'var(--color-text-tertiary)' }}>Pending step</span>
      </div>
    </div>
  );
}

function SizeVariants() {
  const sizes = [
    { label: 'sm', circleSize: 24, textSize: 'var(--text-xs)' },
    { label: 'md', circleSize: 28, textSize: 'var(--text-sm)' },
    { label: 'lg', circleSize: 36, textSize: 'var(--text-base)' },
  ] as const;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
      {sizes.map((size) => (
        <div key={size.label} style={{ fontFamily: 'var(--font-family)', display: 'flex', gap: '8px', alignItems: 'center' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', minWidth: '28px' }}>{size.label}</span>
          <div style={stepCircle('completed', size.circleSize)}><CheckIcon /></div>
          <div style={stepCircle('active', size.circleSize)}>1</div>
          <div style={stepCircle('pending', size.circleSize)}>2</div>
          <span style={{ fontSize: size.textSize, marginLeft: '4px' }}>Step label</span>
        </div>
      ))}
    </div>
  );
}

export default function StepperPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Stepper Preview</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>All stepper variants</p>
      </div>

      <Section label="Horizontal Stepper">
        <PreviewBox label="4 steps, current=2, clickable">
          <HorizontalStepper />
        </PreviewBox>
      </Section>

      <Section label="Vertical Stepper">
        <PreviewBox label="With optional labels and descriptions">
          <VerticalStepper />
        </PreviewBox>
      </Section>

      <Section label="Progress Stepper">
        <PreviewBox label="Bar with percentage">
          <ProgressStepper />
        </PreviewBox>
      </Section>

      <Section label="Clickable Steps">
        <PreviewBox label="Button-style clickable steps">
          <ClickableSteps />
        </PreviewBox>
      </Section>

      <Section label="With Optional Labels">
        <PreviewBox label="Optional step indicator">
          <WithOptionalLabels />
        </PreviewBox>
      </Section>

      <Section label="States">
        <PreviewBox label="Completed / Active / Pending">
          <StatesDemo />
        </PreviewBox>
      </Section>

      <Section label="All Sizes">
        <PreviewBox label="sm, md, lg">
          <SizeVariants />
        </PreviewBox>
      </Section>
    </div>
  );
}
