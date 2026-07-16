import { memo } from 'react';
import type { Project } from '../types';
import { AssignmentStatusBadge } from './AssignmentStatusBadge';
import { PROJECT_TYPE_LABELS } from '../types';

interface ProjectCardProps {
  project: Project;
  onSelect?: (id: string) => void;
}

export const ProjectCard = memo(function ProjectCard({ project, onSelect }: ProjectCardProps) {
  return (
    <div
      onClick={() => onSelect?.(project.id)}
      onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onSelect) { e.preventDefault(); onSelect(project.id); } }}
      tabIndex={onSelect ? 0 : undefined}
      role={onSelect ? 'button' : undefined}
      aria-label={`Project ${project.projectCode}`}
      style={{
        padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
        border: '1px solid var(--color-border-default)',
        background: 'var(--color-bg-surface-default)',
        cursor: onSelect ? 'pointer' : 'default',
        display: 'flex', flexDirection: 'column', gap: 8,
      }}
    >
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{project.title}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontFamily: 'monospace' }}>{project.projectCode}</div>
        </div>
        <AssignmentStatusBadge status={project.status} />
      </div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{project.courseName} &middot; {project.batchName}</div>
      <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>{PROJECT_TYPE_LABELS[project.projectType]}</span>
        {project.teamSize && <><span>&middot;</span><span>Team: {project.teamSize}</span></>}
        <span>&middot;</span>
        <span>Due: {project.dueDate}</span>
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        Marks: {project.maxMarks} | Passing: {project.passingMarks}
      </div>
    </div>
  );
});
