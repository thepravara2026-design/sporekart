import { useLocation, Link } from 'react-router-dom';
import { buildBreadcrumb } from '../../config/navigation';

export default function BreadcrumbBar() {
  const location = useLocation();
  const { crumbs } = buildBreadcrumb(location.pathname);

  return (
    <nav className="sk-breadcrumb" aria-label="Breadcrumb">
      <ol>
        {crumbs.map((c, i) => {
          const isLast = i === crumbs.length - 1;
          return (
            <li key={i} aria-current={isLast ? 'page' : undefined}>
              {c.to && !isLast ? <Link to={c.to}>{c.label}</Link> : <span>{c.label}</span>}
              {!isLast && <span className="sk-breadcrumb__sep" aria-hidden="true">/</span>}
            </li>
          );
        })}
      </ol>
    </nav>
  );
}
