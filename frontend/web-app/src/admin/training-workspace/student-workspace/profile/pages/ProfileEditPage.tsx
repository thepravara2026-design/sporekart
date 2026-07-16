import { useState } from 'react';
import { useProfile } from '../state/ProfileContext';
import { ProfileHeader } from '../components/ProfileHeader';
import { ProfileHeaderSkeleton } from '../components/ProfileSkeleton';

export function ProfileEditPage() {
  const { currentProfile, isLoading, error, updateProfile } = useProfile();
  const [saved, setSaved] = useState(false);

  if (isLoading) return <div className="profile-edit-page"><ProfileHeaderSkeleton /></div>;
  if (error) return <div className="profile-edit-page profile-error"><p>{error}</p></div>;
  if (!currentProfile) return <div className="profile-edit-page profile-empty"><p>No student profile selected.</p></div>;

  const handleSave = () => {
    updateProfile(currentProfile);
    setSaved(true);
    setTimeout(() => setSaved(false), 3000);
  };

  return (
    <div className="profile-edit-page">
      <ProfileHeader profile={currentProfile} />

      {saved && (
        <div className="profile-edit-page__toast">Profile changes saved successfully.</div>
      )}

      <div className="profile-edit-page__grid">
        <div className="profile-edit-page__section">
          <h3 className="profile-edit-page__section-title">Personal Information</h3>
          <div className="profile-edit-page__form">
            <div className="profile-edit-page__field">
              <label>Preferred Name</label>
              <input type="text" defaultValue={currentProfile.preferredName} />
            </div>
            <div className="profile-edit-page__field">
              <label>Email</label>
              <input type="email" defaultValue={currentProfile.email} />
            </div>
            <div className="profile-edit-page__field">
              <label>Phone</label>
              <input type="tel" defaultValue={currentProfile.phone} />
            </div>
            <div className="profile-edit-page__field">
              <label>Alternate Phone</label>
              <input type="tel" defaultValue={currentProfile.alternatePhone ?? ''} />
            </div>
            <div className="profile-edit-page__field">
              <label>Blood Group</label>
              <select defaultValue={currentProfile.bloodGroup ?? ''}>
                <option value="">Not Specified</option>
                {['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'].map((bg) => (
                  <option key={bg} value={bg}>{bg}</option>
                ))}
              </select>
            </div>
            <div className="profile-edit-page__field">
              <label>Government ID</label>
              <input type="text" defaultValue={currentProfile.governmentId ?? ''} />
            </div>
          </div>
        </div>

        <div className="profile-edit-page__section">
          <h3 className="profile-edit-page__section-title">Academic Information</h3>
          <div className="profile-edit-page__form">
            <div className="profile-edit-page__field">
              <label>Highest Qualification</label>
              <input type="text" defaultValue={currentProfile.academicInfo.highestQualification} />
            </div>
            <div className="profile-edit-page__field">
              <label>Specialization</label>
              <input type="text" defaultValue={currentProfile.academicInfo.specialization} />
            </div>
            <div className="profile-edit-page__field">
              <label>Institution</label>
              <input type="text" defaultValue={currentProfile.academicInfo.institution} />
            </div>
            <div className="profile-edit-page__field">
              <label>Graduation Year</label>
              <input type="number" defaultValue={currentProfile.academicInfo.graduationYear ?? ''} />
            </div>
          </div>
        </div>

        <div className="profile-edit-page__section">
          <h3 className="profile-edit-page__section-title">Professional Information</h3>
          <div className="profile-edit-page__form">
            <div className="profile-edit-page__field">
              <label>Current Profession</label>
              <input type="text" defaultValue={currentProfile.professionalInfo.currentProfession} />
            </div>
            <div className="profile-edit-page__field">
              <label>Industry</label>
              <input type="text" defaultValue={currentProfile.professionalInfo.industry ?? ''} />
            </div>
            <div className="profile-edit-page__field">
              <label>Years of Experience</label>
              <input type="number" defaultValue={currentProfile.professionalInfo.yearsOfExperience} />
            </div>
          </div>
        </div>

        <div className="profile-edit-page__section">
          <h3 className="profile-edit-page__section-title">Learning Preferences</h3>
          <div className="profile-edit-page__form">
            <div className="profile-edit-page__field">
              <label>Preferred Language</label>
              <input type="text" defaultValue={currentProfile.learningPreferences.preferredLanguage} />
            </div>
            <div className="profile-edit-page__field">
              <label>Preferred Mode</label>
              <select defaultValue={currentProfile.learningPreferences.preferredMode}>
                <option value="online">Online</option>
                <option value="offline">Offline</option>
                <option value="hybrid">Hybrid</option>
              </select>
            </div>
          </div>
        </div>
      </div>

      <div className="profile-edit-page__actions">
        <button className="btn btn--secondary" onClick={() => window.history.back()}>Cancel</button>
        <button className="btn btn--primary" onClick={handleSave}>Save Changes</button>
      </div>
    </div>
  );
}
