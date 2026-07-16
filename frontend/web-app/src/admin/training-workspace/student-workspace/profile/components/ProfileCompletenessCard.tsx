import type { ProfileCompleteness } from '../types';

interface ProfileCompletenessCardProps {
  completeness: ProfileCompleteness;
}

const sectionLabels: Record<string, string> = {
  personal: 'Personal Info',
  academic: 'Academic Info',
  professional: 'Professional Info',
  learningPreferences: 'Learning Prefs',
  guardian: 'Guardian',
  emergencyContact: 'Emergency',
  documents: 'Documents',
};

export function ProfileCompletenessCard({ completeness }: ProfileCompletenessCardProps) {
  const sectionsList = Object.entries(completeness.sections).map(([key, val]) => ({
    key,
    label: sectionLabels[key as keyof typeof sectionLabels] || key,
    ...val,
  }));

  return (
    <div className="profile-completeness">
      <div className="profile-completeness__header">
        <h3 className="profile-completeness__title">Profile Completeness</h3>
        <span className="profile-completeness__score">{completeness.overall}%</span>
      </div>

      <div className="profile-completeness__bar-track">
        <div
          className="profile-completeness__bar-fill"
          style={{ width: `${completeness.overall}%` }}
        />
      </div>

      <div className="profile-completeness__sections">
        {sectionsList.map((section) => (
          <div key={section.key} className="profile-completeness__section">
            <div className="profile-completeness__section-header">
              <span className="profile-completeness__section-label">{section.label}</span>
              <span className="profile-completeness__section-count">
                {section.completed}/{section.total}
              </span>
            </div>
            <div className="profile-completeness__section-bar">
              <div
                className="profile-completeness__section-fill"
                style={{ width: `${section.percent}%` }}
              />
            </div>
          </div>
        ))}
      </div>

      {completeness.suggestions.length > 0 && (
        <div className="profile-completeness__suggestions">
          <p className="profile-completeness__suggestions-title">Suggestions</p>
          <ul className="profile-completeness__suggestions-list">
            {completeness.suggestions.map((s, i) => (
              <li key={i} className="profile-completeness__suggestion">{s}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
