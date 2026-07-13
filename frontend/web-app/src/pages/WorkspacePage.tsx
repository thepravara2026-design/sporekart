import { useLocation } from 'react-router-dom';
import { useApp } from '../context';
import { resolveRoute, canView } from '../config/navigation';
import PlaceholderPanel from '../components/ui/PlaceholderPanel';

export default function WorkspacePage() {
  const location = useLocation();
  const { activeRole } = useApp();
  const resolved = resolveRoute(location.pathname);

  if (!resolved) {
    return (
      <div className="sk-content__header">
        <h1>Not found</h1>
        <PlaceholderPanel
          title="Page not found"
          hint="This route is not part of the current navigation blueprint."
        />
      </div>
    );
  }

  const { page, workspace, params } = resolved;

  if (!canView(page.roles, activeRole)) {
    return (
      <div className="sk-content__header">
        <h1>Access restricted</h1>
        <PlaceholderPanel
          title="Access restricted"
          hint={`Your current role (${activeRole}) cannot view this page. Use the header role switcher to preview other roles.`}
        />
      </div>
    );
  }

  const title = params.id ? `${workspace.label}: ${params.id}` : page.label;

  return (
    <div className="sk-content__header">
      <div className="sk-content__title-row">
        <div>
          <h1>{title}</h1>
          <p className="sk-content__subtitle">{page.description}</p>
        </div>
        {page.primaryAction && (
          <button className="sk-primary-action" type="button">
            {page.primaryAction}
          </button>
        )}
      </div>
      <PlaceholderPanel
        title={`${page.label} — empty panel`}
        hint="This is a navigation prototype. Production content, tables, forms, and charts arrive in later Sprint 19 parts."
        actionLabel={page.primaryAction}
      />
    </div>
  );
}
