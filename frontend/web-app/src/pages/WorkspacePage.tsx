import { useLocation, Navigate } from 'react-router-dom';
import { useApp } from '../context';
import { resolveRoute, canView } from '../config/navigation';
import PlaceholderPanel from '../components/ui/PlaceholderPanel';

export default function WorkspacePage() {
  const location = useLocation();
  const { auth } = useApp();
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

  if (!canView(page.roles, auth.userRole)) {
    /**
     * BUG-RT-007: the workspace shell previously rendered a static
     * "Access restricted" placeholder instead of enforcing the restriction.
     * Redirect unauthorised roles to the dedicated Access Denied screen so the
     * restriction is consistent with the route guard behaviour.
     */
    return <Navigate to="/access-denied" replace />;
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
