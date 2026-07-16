import { useAssignments } from '../state/AssignmentContext';
import { ProjectCard } from '../components/ProjectCard';
import { EmptyState } from '../components/EmptyStates';

export function AssignmentProjectsPage() {
  const { projects } = useAssignments();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Project Management</h2>

      {projects.length === 0 ? (
        <EmptyState type="noProjects" />
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
          {projects.map((p) => (
            <ProjectCard key={p.id} project={p} />
          ))}
        </div>
      )}
    </div>
  );
}
