import type { Certificate, DigitalBadge, Achievement, CredentialWallet, AcademicTranscript, TranscriptCourseRecord, VerificationRecord, CertificateAnalytics, CertificateDashboard, CertificateType, CertificateStatus, AchievementType, BadgeType, VerificationStatus } from '../types';

const STUDENTS = Array.from({ length: 25 }, (_, i) => ({ id: `stu-${i + 1}`, name: ['Aarav Sharma','Priya Patel','Rahul Singh','Ananya Gupta','Vikram Joshi','Neha Kapoor','Arjun Mehta','Kavita Reddy','Rohan Desai','Ishita Verma','Amit Kumar','Sneha Agarwal','Deepak Tiwari','Pooja Nair','Karan Malhotra','Divya Bhat','Suresh Iyer','Meera Choudhury','Nitin Saxena','Lakshmi Rajan','Rajesh Kumar','Anjali Sinha','Vivek Mishra','Pallavi Rao','Aditya Khanna'][i] }));

const COURSES = [
  { id: 'course-1', name: 'Full Stack Web Development' },
  { id: 'course-2', name: 'Data Science & Analytics' },
  { id: 'course-3', name: 'Cloud Architecture' },
  { id: 'course-4', name: 'Mobile App Development' },
  { id: 'course-5', name: 'DevOps Engineering' },
  { id: 'course-6', name: 'UI/UX Design' },
];

const BATCHES = [
  { id: 'batch-1', name: 'Morning Batch' },
  { id: 'batch-2', name: 'Evening Batch' },
  { id: 'batch-3', name: 'Weekend Batch' },
];

function randomFrom<T>(arr: T[]): T { return arr[Math.floor(Math.random() * arr.length)]; }

function randomDate(daysAgo: number): string {
  const d = new Date(); d.setDate(d.getDate() - Math.floor(Math.random() * daysAgo));
  return d.toISOString().split('T')[0];
}

const CERT_TYPES: CertificateType[] = ['course-completion', 'professional', 'skill', 'workshop', 'training', 'internship', 'achievement', 'participation', 'merit'];

const CERT_STATUSES: CertificateStatus[] = ['draft', 'pending-approval', 'approved', 'generated', 'issued', 'shared', 'verified', 'expired', 'revoked', 'archived'];

const BADGE_TYPES: BadgeType[] = ['course', 'skill', 'competency', 'assessment', 'attendance', 'leadership', 'trainer', 'corporate'];

const ACH_TYPES: AchievementType[] = ['course-completed', 'perfect-attendance', 'top-performer', 'fast-learner', 'outstanding-project', 'highest-marks', 'innovation-award', 'research-excellence', 'industry-ready', 'leadership-award', 'community-contributor'];

function generateCertificates(): Certificate[] {
  return STUDENTS.flatMap((stu, si) => {
    const count = Math.floor(Math.random() * 3) + 1;
    return Array.from({ length: count }, (_, ci) => {
      const course = randomFrom(COURSES);
      const batch = randomFrom(BATCHES);
      const status = randomFrom(CERT_STATUSES);
      const certType = randomFrom(CERT_TYPES);
      const isVerified = status === 'verified' || (status === 'issued' && Math.random() > 0.7);
      return {
        id: `cert-${stu.id}-${ci}`,
        certificateNumber: `CRT-${String(si + 1).padStart(4, '0')}-${String(ci + 1).padStart(3, '0')}`,
        credentialId: `cred-${stu.id}-${ci}`,
        studentId: stu.id,
        studentName: stu.name,
        enrollmentId: `enr-${stu.id}-${course.id}`,
        courseId: course.id,
        courseName: course.name,
        batchId: batch.id,
        batchName: batch.name,
        certificateType: certType,
        title: `${course.name} - ${certType.split('-').map((w) => w.charAt(0).toUpperCase() + w.slice(1)).join(' ')}`,
        description: `Awarded to ${stu.name} for successful completion of ${course.name}`,
        issueDate: status === 'draft' || status === 'pending-approval' ? '' : randomDate(90),
        expiryDate: certType === 'government-skill' || certType === 'professional' ? randomDate(730) : null,
        status,
        verificationStatus: isVerified ? 'verified' : randomFrom(['unverified', 'pending']),
        credentialStatus: status === 'revoked' ? 'revoked' : status === 'expired' ? 'expired' : status === 'archived' ? 'archived' : 'active',
        achievementLevel: ['Outstanding', 'Excellent', 'Good', 'Satisfactory'][Math.floor(Math.random() * 4)],
        template: 'standard-certificate-template',
        issuer: 'SporeKart Academy',
        issuerLogo: '/assets/issuer-logo.png',
        verificationUrl: `https://verify.sporekart.com/${stu.id}/${ci}`,
        qrCode: `qr-${stu.id}-${ci}`,
        digitalSignature: `sig-${stu.id}-${ci}-${Date.now()}`,
        createdDate: randomDate(120),
        lastUpdated: randomDate(7),
      };
    });
  });
}

function generateBadges(certificates: Certificate[]): DigitalBadge[] {
  const used = new Set<string>();
  return certificates.slice(0, 40).map((cert, i) => {
    const bType = randomFrom(BADGE_TYPES);
    const badgeId = `badge-${cert.studentId}-${i}`;
    used.add(badgeId);
    return {
      id: badgeId,
      badgeCode: `BDG-${String(i + 1).padStart(4, '0')}`,
      badgeType: bType,
      name: `${bType.charAt(0).toUpperCase() + bType.slice(1)} Badge`,
      description: `Earned for ${bType} proficiency in ${cert.courseName}`,
      imageUrl: `/assets/badges/${bType}.svg`,
      criteria: `Complete all ${bType} requirements for ${cert.courseName}`,
      issuer: 'SporeKart Academy',
      studentId: cert.studentId,
      studentName: cert.studentName,
      issueDate: randomDate(60),
      expiryDate: Math.random() > 0.7 ? randomDate(730) : null,
      status: randomFrom(['active', 'shared']),
      verificationStatus: Math.random() > 0.3 ? 'verified' : 'unverified',
      verificationUrl: `https://verify.sporekart.com/badge/${badgeId}`,
      shareCount: Math.floor(Math.random() * 15),
      isNft: Math.random() > 0.9,
      createdDate: randomDate(60),
    };
  });
}

function generateAchievements(certificates: Certificate[]): Achievement[] {
  return certificates.slice(0, 50).map((cert, i) => {
    const achType = randomFrom(ACH_TYPES);
    return {
      id: `ach-${cert.studentId}-${i}`,
      achievementCode: `ACH-${String(i + 1).padStart(4, '0')}`,
      achievementType: achType,
      name: achType.split('-').map((w) => w.charAt(0).toUpperCase() + w.slice(1)).join(' '),
      description: `${cert.studentName} earned ${achType.replace('-', ' ')} in ${cert.courseName}`,
      studentId: cert.studentId,
      studentName: cert.studentName,
      courseId: cert.courseId,
      courseName: cert.courseName,
      dateAchieved: randomDate(60),
      iconUrl: `/assets/achievements/${achType}.svg`,
      points: Math.floor(Math.random() * 500) + 50,
      badgeId: Math.random() > 0.5 ? `badge-${cert.studentId}-${i}` : null,
      certificateId: Math.random() > 0.6 ? cert.id : null,
      isPublic: Math.random() > 0.2,
      createdDate: randomDate(60),
    };
  });
}

function generateWallets(certificates: Certificate[], badges: DigitalBadge[], achievements: Achievement[]): CredentialWallet[] {
  return STUDENTS.map((stu) => {
    const stuCerts = certificates.filter((c) => c.studentId === stu.id);
    const stuBadges = badges.filter((b) => b.studentId === stu.id);
    const stuAch = achievements.filter((a) => a.studentId === stu.id);
    const active = stuCerts.filter((c) => c.credentialStatus === 'active').length;
    const shared = stuCerts.filter((c) => c.credentialStatus === 'shared').length;
    return {
      id: `wallet-${stu.id}`,
      studentId: stu.id,
      studentName: stu.name,
      certificates: stuCerts,
      badges: stuBadges,
      achievements: stuAch,
      totalCredentials: stuCerts.length + stuBadges.length + stuAch.length,
      activeCredentials: active,
      sharedCredentials: shared,
      lastActivityDate: randomDate(7),
      createdDate: randomDate(120),
    };
  });
}

function generateTranscriptRecords(): TranscriptCourseRecord[] {
  return COURSES.map((course) => {
    const status = randomFrom(['in-progress', 'completed', 'certified', 'failed'] as const);
    return {
      courseId: course.id,
      courseName: course.name,
      enrollmentDate: randomDate(180),
      completionDate: status !== 'in-progress' ? randomDate(30) : null,
      attendancePercent: Math.floor(Math.random() * 30) + 70,
      assignmentScore: Math.floor(Math.random() * 30) + 70,
      assessmentScore: Math.floor(Math.random() * 30) + 70,
      competenciesCount: Math.floor(Math.random() * 5) + 1,
      learningHours: Math.floor(Math.random() * 100) + 20,
      grade: randomFrom(['A+', 'A', 'B+', 'B', 'C+', 'C', 'D']),
      gradePoint: Math.floor(Math.random() * 4) + 1,
      status,
    };
  });
}

function generateTranscripts(wallets: CredentialWallet[]): AcademicTranscript[] {
  return wallets.slice(0, 20).map((wallet) => {
    const records = generateTranscriptRecords();
    const completed = records.filter((r) => r.status === 'completed' || r.status === 'certified').length;
    const avgScore = Math.round(records.reduce((s, r) => s + r.assessmentScore, 0) / records.length);
    const avgAtt = Math.round(records.reduce((s, r) => s + r.attendancePercent, 0) / records.length);
    const totalHours = records.reduce((s, r) => s + r.learningHours, 0);
    const gpa = +(Math.random() * 3 + 1).toFixed(2);
    return {
      id: `transcript-${wallet.studentId}`,
      studentId: wallet.studentId,
      studentName: wallet.studentName,
      studentEmail: `${wallet.studentName.toLowerCase().replace(/\s+/g, '.')}@sporekart.com`,
      studentPhoto: `/assets/students/${wallet.studentId}.jpg`,
      enrollmentDate: randomDate(365),
      completionDate: Math.random() > 0.3 ? randomDate(30) : null,
      totalCourses: records.length,
      completedCourses: completed,
      totalAttendancePercent: avgAtt,
      overallAssignmentScore: Math.floor(Math.random() * 15) + 80,
      overallAssessmentScore: avgScore,
      competenciesAchieved: Math.floor(Math.random() * 10) + 5,
      totalCompetencies: 15,
      achievementsCount: wallet.achievements.length,
      certificatesCount: wallet.certificates.length,
      totalLearningHours: totalHours,
      overallPerformance: Math.round((avgScore + avgAtt) / 2),
      gpa,
      credits: completed * 4,
      courseRecords: records,
      status: randomFrom(['active', 'completed', 'graduated']),
    };
  });
}

function generateVerificationRecords(certificates: Certificate[]): VerificationRecord[] {
  return certificates.slice(0, 30).map((cert, i) => {
    const status = randomFrom(['unverified', 'pending', 'verified', 'failed'] as VerificationStatus[]);
    return {
      id: `ver-${cert.id}`,
      verificationCode: `VER-${String(i + 1).padStart(6, '0')}`,
      certificateId: cert.id,
      certificateNumber: cert.certificateNumber,
      studentId: cert.studentId,
      studentName: cert.studentName,
      courseName: cert.courseName,
      certificateType: cert.certificateType,
      issueDate: cert.issueDate,
      verificationDate: status === 'verified' ? randomDate(15) : '',
      status,
      verifiedBy: status === 'verified' ? randomFrom(['SporeKart Verify', 'Employer Portal', 'Govt Skill Registry']) : '',
      verificationMethod: randomFrom(['manual', 'qr', 'employer']),
      notes: status === 'failed' ? 'Invalid credential ID' : status === 'verified' ? 'Credential verified successfully' : 'Awaiting verification',
      createdDate: randomDate(30),
    };
  });
}

function generateAnalytics(certificates: Certificate[], badges: DigitalBadge[], achievements: Achievement[], wallets: CredentialWallet[], transcripts: AcademicTranscript[]): CertificateAnalytics {
  const issued = certificates.filter((c) => c.status === 'issued' || c.status === 'verified' || c.status === 'shared').length;
  const pending = certificates.filter((c) => c.status === 'pending-approval' || c.status === 'approved' || c.status === 'generated').length;
  const verified = certificates.filter((c) => c.verificationStatus === 'verified').length;
  const revoked = certificates.filter((c) => c.status === 'revoked').length;
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];
  return {
    totalCertificates: certificates.length,
    issuedCertificates: issued,
    pendingCertificates: pending,
    verifiedCertificates: verified,
    revokedCertificates: revoked,
    totalBadges: badges.length,
    totalAchievements: achievements.length,
    totalWallets: wallets.length,
    totalTranscripts: transcripts.length,
    verificationRequests: Math.floor(Math.random() * 50) + 10,
    certificatesByCourse: COURSES.map((c) => ({
      courseName: c.name,
      count: certificates.filter((cert) => cert.courseId === c.id).length,
    })),
    certificatesByMonth: months.map((m) => ({
      month: m,
      count: Math.floor(Math.random() * 15) + 3,
    })),
    achievementDistribution: achievements.reduce<{ type: string; count: number }[]>((acc, ach) => {
      const existing = acc.find((a) => a.type === ach.achievementType);
      if (existing) existing.count++;
      else acc.push({ type: ach.achievementType, count: 1 });
      return acc;
    }, []),
    badgeDistribution: badges.reduce<{ type: string; count: number }[]>((acc, bdg) => {
      const existing = acc.find((a) => a.type === bdg.badgeType);
      if (existing) existing.count++;
      else acc.push({ type: bdg.badgeType, count: 1 });
      return acc;
    }, []),
    verificationRate: Math.round((verified / Math.max(certificates.length, 1)) * 100),
    completionRate: Math.round((issued / Math.max(certificates.length, 1)) * 100),
  };
}

function generateDashboard(certificates: Certificate[], badges: DigitalBadge[], achievements: Achievement[], wallets: CredentialWallet[], transcripts: AcademicTranscript[]): CertificateDashboard {
  return {
    certificatesIssued: certificates.filter((c) => c.status === 'issued' || c.status === 'verified').length,
    pendingCertificates: certificates.filter((c) => c.status === 'pending-approval' || c.status === 'approved').length,
    verifiedCertificates: certificates.filter((c) => c.verificationStatus === 'verified').length,
    revokedCertificates: certificates.filter((c) => c.status === 'revoked').length,
    digitalBadges: badges.length,
    achievements: achievements.length,
    credentialWallets: wallets.length,
    transcriptCount: transcripts.length,
    verificationRequests: Math.floor(Math.random() * 30) + 5,
    recentCertificates: certificates.slice(0, 5),
    recentAchievements: achievements.slice(0, 5),
  };
}

const certificates = generateCertificates();
const badges = generateBadges(certificates);
const achievements = generateAchievements(certificates);
const wallets = generateWallets(certificates, badges, achievements);
const transcripts = generateTranscripts(wallets);
const verificationRecords = generateVerificationRecords(certificates);
const analytics = generateAnalytics(certificates, badges, achievements, wallets, transcripts);
const dashboard = generateDashboard(certificates, badges, achievements, wallets, transcripts);

export function getCertificates(): Certificate[] { return certificates; }
export function getBadges(): DigitalBadge[] { return badges; }
export function getAchievements(): Achievement[] { return achievements; }
export function getWallets(): CredentialWallet[] { return wallets; }
export function getTranscripts(): AcademicTranscript[] { return transcripts; }
export function getVerificationRecords(): VerificationRecord[] { return verificationRecords; }
export function getAnalytics(): CertificateAnalytics { return analytics; }
export function getDashboard(): CertificateDashboard { return dashboard; }
export function getStudentWallet(studentId: string): CredentialWallet | undefined { return wallets.find((w) => w.studentId === studentId); }
