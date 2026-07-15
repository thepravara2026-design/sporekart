import React from 'react';
import { useNavigate } from 'react-router-dom';
import { ProductLayout } from '../layout/ProductLayout';
import { ProductWorkspace } from '../workspace/ProductWorkspace';
import { Button } from '../../../../design-system/components/core/Button';
import { Icon } from '../../../../design-system/icons/Icon';

import { PermissionGate } from '../../../permissions/PermissionGate';
import { useWizardState } from './useWizardState';
import WizardStepper from './WizardStepper';
import WizardActionBar from './WizardActionBar';
import ProductPreviewPanel from './ProductPreviewPanel';
import ReviewStep from './ReviewStep';
import ConfirmationStep from './ConfirmationStep';
import { PermissionDeniedState } from './CreationStates';
import WizardLoadingSkeleton from './LoadingStates';
import { validateAll } from './validation';
import './creation.css';

import BasicInfoStep from './steps/BasicInfoStep';
import ClassificationStep from './steps/ClassificationStep';
import PackagingStep from './steps/PackagingStep';
import PricingStep from './steps/PricingStep';
import SeoStep from './steps/SeoStep';

import type { SubmittedProduct, WizardStepId } from './types';

export interface ProductCreationWizardProps {
  initialStep?: WizardStepId;
  demoConfirmation?: boolean;
  embeddedPreview?: boolean;
}

function mockSubmittedProduct(): SubmittedProduct {
  return {
    id: 'SK-PROD-DEMO',
    name: 'Sample Mushroom Grow Kit',
    sku: 'SKU-DEMO-001',
    status: 'draft',
    createdAt: new Date().toISOString(),
  };
}

const ProductCreationWizard: React.FC<ProductCreationWizardProps> = ({
  initialStep = 'basic',
  demoConfirmation = false,
  embeddedPreview = false,
}) => {
  const navigate = useNavigate();
  const state = useWizardState({
    initialStep: demoConfirmation ? 'confirmation' : initialStep,
    submittedProduct: demoConfirmation ? mockSubmittedProduct() : null,
  });

  const [previewOpen, setPreviewOpen] = React.useState(embeddedPreview);

  const openPreview = () => setPreviewOpen(true);
  const togglePreview = () => setPreviewOpen((p) => !p);

  const goToCatalog = () => navigate('/products');

  const canSubmit = Object.keys(validateAll(state.data)).length === 0;

  React.useEffect(() => {
    const handler = (e: KeyboardEvent) => {
      if ((e.metaKey || e.ctrlKey) && e.key === 'Enter') {
        e.preventDefault();
        if (state.step === 'review') {
          if (canSubmit) state.submit();
        } else if (state.step !== 'confirmation') {
          state.next();
        }
      } else if ((e.metaKey || e.ctrlKey) && e.key.toLowerCase() === 's') {
        e.preventDefault();
        state.saveDraft();
      }
    };
    window.addEventListener('keydown', handler);
    return () => window.removeEventListener('keydown', handler);
  }, [state.step, canSubmit, state.next, state.submit, state.saveDraft]);

  const renderStep = () => {
    switch (state.step) {
      case 'basic':
        return <BasicInfoStep data={state.data} errors={state.errors} setField={state.setField} />;
      case 'classification':
        return <ClassificationStep data={state.data} errors={state.errors} setField={state.setField} />;
      case 'packaging':
        return <PackagingStep data={state.data} errors={state.errors} setField={state.setField} />;
      case 'pricing':
        return <PricingStep data={state.data} errors={state.errors} setField={state.setField} />;
      case 'seo':
        return <SeoStep data={state.data} errors={state.errors} setField={state.setField} />;
      case 'review':
        return <ReviewStep data={state.data} errors={state.errors} onJump={state.goToStep} />;
      case 'confirmation':
        return (
          <ConfirmationStep
            product={state.submittedProduct}
            onCreateAnother={state.reset}
            onGoToCatalog={goToCatalog}
            onPreview={openPreview}
          />
        );
      default:
        return null;
    }
  };

  const actions = (
    <>
      {state.step !== 'confirmation' && (
        <Button
          variant="secondary"
          size="sm"
          onClick={state.saveDraft}
          leftIcon={<Icon name="Save" size={16} />}
        >
          {state.isDraft ? 'Update Draft' : 'Save Draft'}
        </Button>
      )}
      <Button
        variant={previewOpen ? 'primary' : 'outline'}
        size="sm"
        onClick={togglePreview}
        leftIcon={<Icon name={previewOpen ? 'Eye' : 'Eye'} size={16} />}
      >
        {previewOpen ? 'Hide Preview' : 'Preview'}
      </Button>
    </>
  );

  const content = (
    <ProductWorkspace activeSection="products">
      <ProductLayout
        title="Create Product"
        breadcrumbs={[
          { label: 'Products', href: '/products' },
          { label: 'Create' },
        ]}
        actions={actions}
      >
        <div className={`sk-wizard-shell ${previewOpen ? 'sk-wizard-shell--with-preview' : ''}`}>
          <div className="sk-wizard-main">
            {state.initialLoading ? (
              <WizardLoadingSkeleton />
            ) : (
              <>
                {state.step !== 'confirmation' && (
                  <div className="sk-wizard-stepper-wrap">
                    <WizardStepper current={state.step} visited={state.visited} onStepClick={state.goToStep} />
                  </div>
                )}
                {renderStep()}
                <WizardActionBar
                  step={state.step}
                  unsaved={state.unsaved}
                  isDraft={state.isDraft}
                  canSubmit={canSubmit}
                  submitting={state.submitting}
                  onBack={state.prev}
                  onNext={state.next}
                  onSaveDraft={state.saveDraft}
                  onDiscard={state.discardDraft}
                  onCancel={goToCatalog}
                  onSubmit={state.submit}
                />
              </>
            )}
          </div>
          {previewOpen && (
            <aside className="sk-wizard-preview" aria-label="Product preview">
              <ProductPreviewPanel data={state.data} />
            </aside>
          )}
        </div>
      </ProductLayout>
    </ProductWorkspace>
  );

  return (
    <PermissionGate action="create" resource="products" fallback={<PermissionDeniedState onBack={goToCatalog} />}>
      {content}
    </PermissionGate>
  );
};

export default ProductCreationWizard;
