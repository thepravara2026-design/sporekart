import { useState } from 'react';
import { type CopilotContext } from '../types';
import { CopilotMemoryTimeline } from './CopilotMemoryTimeline';
import { CopilotKnowledgeReferences } from './CopilotKnowledgeReferences';
import type { MemoryEntry, KnowledgeReference } from '../types';

interface CopilotContextPanelProps {
  context: CopilotContext;
  memories?: MemoryEntry[];
  knowledgeRefs?: KnowledgeReference[];
}

export function CopilotContextPanel({ context, memories, knowledgeRefs }: CopilotContextPanelProps) {
  const [expandedSections, setExpandedSections] = useState<Set<string>>(new Set(['context']));

  const toggleSection = (id: string) => {
    setExpandedSections((prev) => {
      const next = new Set(prev);
      if (next.has(id)) next.delete(id);
      else next.add(id);
      return next;
    });
  };

  const renderContextRow = (label: string, value?: string) => {
    if (!value) return null;
    return (
      <div className="cp-context__row">
        <span className="cp-context__label">{label}</span>
        <span className="cp-context__value">{value}</span>
      </div>
    );
  };

  return (
    <div className="cp-context" role="region" aria-label="Copilot context">
      <div className="cp-context__section">
        <button
          type="button"
          className="cp-context__section-header"
          onClick={() => toggleSection('context')}
          aria-expanded={expandedSections.has('context')}
        >
          <span>Current Context</span>
          <span className="cp-context__chevron" aria-hidden="true">
            {expandedSections.has('context') ? '\u25BC' : '\u25B6'}
          </span>
        </button>
        {expandedSections.has('context') && (
          <div className="cp-context__body">
            {renderContextRow('Page', context.pageTitle)}
            {renderContextRow('Section', context.section)}
            {renderContextRow('Entity', context.entityType ? `${context.entityType}: ${context.entityId}` : undefined)}
            {renderContextRow('Workspace', context.workspaceId)}
            {context.roles && context.roles.length > 0 && (
              <div className="cp-context__row">
                <span className="cp-context__label">Roles</span>
                <span className="cp-context__value">{context.roles.join(', ')}</span>
              </div>
            )}
          </div>
        )}
      </div>

      {memories && memories.length > 0 && (
        <div className="cp-context__section">
          <button
            type="button"
            className="cp-context__section-header"
            onClick={() => toggleSection('memory')}
            aria-expanded={expandedSections.has('memory')}
          >
            <span>Memory Timeline</span>
            <span className="cp-context__chevron" aria-hidden="true">
              {expandedSections.has('memory') ? '\u25BC' : '\u25B6'}
            </span>
          </button>
          {expandedSections.has('memory') && (
            <div className="cp-context__body">
              <CopilotMemoryTimeline memories={memories} />
            </div>
          )}
        </div>
      )}

      {knowledgeRefs && knowledgeRefs.length > 0 && (
        <div className="cp-context__section">
          <button
            type="button"
            className="cp-context__section-header"
            onClick={() => toggleSection('knowledge')}
            aria-expanded={expandedSections.has('knowledge')}
          >
            <span>Knowledge References</span>
            <span className="cp-context__chevron" aria-hidden="true">
              {expandedSections.has('knowledge') ? '\u25BC' : '\u25B6'}
            </span>
          </button>
          {expandedSections.has('knowledge') && (
            <div className="cp-context__body">
              <CopilotKnowledgeReferences references={knowledgeRefs} />
            </div>
          )}
        </div>
      )}
    </div>
  );
}
