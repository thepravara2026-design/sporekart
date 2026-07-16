import { useState } from 'react';
import { ProfileProvider } from '../state/ProfileContext';
import { ProfileNavigation } from '../components/ProfileNavigation';
import { ProfileMainPage } from './ProfileMainPage';
import { ProfileEditPage } from './ProfileEditPage';
import { ProfileTimelinePage } from './ProfileTimelinePage';
import { ProfileAcademicPage } from './ProfileAcademicPage';
import { ProfileProfessionalPage } from './ProfileProfessionalPage';
import { ProfileLearningPage } from './ProfileLearningPage';
import { ProfileGuardianPage } from './ProfileGuardianPage';
import { ProfileDocumentsPage } from './ProfileDocumentsPage';

export function ProfileIndex() {
  const [activeSection, setActiveSection] = useState('profile');

  const renderSection = () => {
    switch (activeSection) {
      case 'profile/edit':
        return <ProfileEditPage />;
      case 'profile/timeline':
        return <ProfileTimelinePage />;
      case 'profile/academic':
        return <ProfileAcademicPage />;
      case 'profile/professional':
        return <ProfileProfessionalPage />;
      case 'profile/learning':
        return <ProfileLearningPage />;
      case 'profile/guardian':
        return <ProfileGuardianPage />;
      case 'profile/documents':
        return <ProfileDocumentsPage />;
      default:
        return <ProfileMainPage />;
    }
  };

  return (
    <ProfileProvider>
      <div className="profile-index">
        <ProfileNavigation activeId={activeSection} onNavigate={setActiveSection} />
        <div className="profile-index__content">
          {renderSection()}
        </div>
      </div>
    </ProfileProvider>
  );
}

export default ProfileIndex;
