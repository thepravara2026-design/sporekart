import { memo, useMemo, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card } from '../../../../design-system/components/composite/Card';
import { Badge } from '../../../../design-system/components/display/Badge';
import { Icon } from '../../../../design-system/icons/Icon';
import { getCourseById, MOCK_COURSES } from '../data/courseMockData';
import {
  LIFECYCLE_LABELS, LIFECYCLE_VARIANTS,
  CATEGORY_LABELS, CATEGORY_COLORS,
  LEVEL_LABELS, DELIVERY_LABELS,
} from '../data/courseMockData';

const CourseDetailPage = memo(function CourseDetailPage() {
  const { courseId } = useParams<{ courseId: string }>();
  const navigate = useNavigate();

  const course = useMemo(() => {
    if (courseId) return getCourseById(courseId);
    return MOCK_COURSES[0];
  }, [courseId]);

  const handleBack = useCallback(() => {
    navigate('/admin/training/courses');
  }, [navigate]);

  if (!course) {
    return (
      <div style={{
        display: 'flex', flexDirection: 'column', alignItems: 'center',
        justifyContent: 'center', padding: 'var(--space-stack-xl)',
        textAlign: 'center', minHeight: 300,
      }}>
        <Icon name="alert-circle" size={48} color="var(--color-danger)" />
        <h3 style={{ margin: 'var(--space-stack-md) 0 var(--space-stack-xs)' }}>
          Course not found
        </h3>
        <button
          type="button"
          onClick={handleBack}
          style={{
            padding: '6px 14px', borderRadius: 'var(--radius-input)',
            background: 'var(--color-bg-primary-default)',
            color: 'var(--color-text-on-primary)', border: 'none', cursor: 'pointer',
          }}
        >
          Back to Courses
        </button>
      </div>
    );
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{
        display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between',
        flexWrap: 'wrap', gap: 'var(--space-inline-md)',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-sm)' }}>
          <button
            type="button"
            onClick={handleBack}
            style={{
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              width: 32, height: 32, borderRadius: 'var(--radius-sm)',
              background: 'var(--color-bg-background)',
              border: '1px solid var(--color-border-default)',
              cursor: 'pointer', color: 'var(--color-text-secondary)',
            }}
            aria-label="Back to course registry"
          >
            <Icon name="arrow-left" size={16} color="currentColor" />
          </button>
          <div>
            <h2 style={{
              margin: 0, fontSize: 'var(--text-h2)',
              color: 'var(--color-text-primary)',
            }}>
              {course.name}
            </h2>
            <span style={{
              fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)',
              fontFamily: 'var(--font-family-mono)',
            }}>
              {course.code}
            </span>
          </div>
        </div>
        <div style={{ display: 'flex', gap: 'var(--space-inline-xs)', flexShrink: 0 }}>
          <button
            type="button"
            onClick={() => navigate(`/admin/training/courses/${course.id}/preview`)}
            style={{
              padding: '6px 12px', borderRadius: 'var(--radius-input)',
              background: 'transparent', border: '1px solid var(--color-border-default)',
              cursor: 'pointer', color: 'var(--color-text-secondary)',
              fontSize: 'var(--text-body-sm)',
            }}
          >
            <Icon name="eye" size={14} color="currentColor" /> Preview
          </button>
          <button
            type="button"
            style={{
              padding: '6px 12px', borderRadius: 'var(--radius-input)',
              background: 'var(--color-bg-primary-default)',
              color: 'var(--color-text-on-primary)', border: 'none',
              cursor: 'pointer', fontSize: 'var(--text-body-sm)',
            }}
          >
            <Icon name="edit" size={14} color="currentColor" /> Edit
          </button>
        </div>
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: '1fr 320px',
        gap: 'var(--space-section-gap)',
      }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
          <Card variant="default" padding="md" as="section">
            <div style={{
              display: 'flex', gap: 'var(--space-inline-md)', flexWrap: 'wrap',
            }}>
              <div style={{
                width: '100%', maxWidth: 320, height: 180,
                borderRadius: 'var(--radius-sm)',
                background: `linear-gradient(135deg, ${CATEGORY_COLORS[course.category]}40, ${CATEGORY_COLORS[course.category]}20)`,
                display: 'flex', alignItems: 'center', justifyContent: 'center',
              }}>
                <Icon name="book-open" size={48} color={CATEGORY_COLORS[course.category]} />
              </div>
              <div style={{ flex: 1, minWidth: 200 }}>
                <h3 style={{
                  margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h4)',
                  color: 'var(--color-text-primary)',
                }}>
                  {course.name}
                </h3>
                <p style={{
                  margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-body)',
                  color: 'var(--color-text-secondary)',
                  lineHeight: 'var(--leading-relaxed)',
                }}>
                  {course.shortDescription}
                </p>
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
                  <Badge variant={LIFECYCLE_VARIANTS[course.lifecycle]}>
                    {LIFECYCLE_LABELS[course.lifecycle]}
                  </Badge>
                  <Badge variant="default">{LEVEL_LABELS[course.level]}</Badge>
                  <Badge variant="default">{DELIVERY_LABELS[course.deliveryMode]}</Badge>
                  <Badge variant="default">{course.language}</Badge>
                </div>
              </div>
            </div>
          </Card>

          <Card variant="default" padding="md" as="section">
            <h4 style={{
              margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h4)',
              color: 'var(--color-text-primary)',
            }}>
              Learning Objectives
            </h4>
            <ul style={{
              margin: 0, padding: '0 0 0 var(--space-stack-md)',
              display: 'flex', flexDirection: 'column', gap: 4,
            }}>
              {course.learningObjectives.map((obj, i) => (
                <li key={i} style={{
                  fontSize: 'var(--text-body)',
                  color: 'var(--color-text-secondary)',
                  lineHeight: 'var(--leading-relaxed)',
                }}>
                  {obj}
                </li>
              ))}
            </ul>
          </Card>

          <Card variant="default" padding="md" as="section">
            <h4 style={{
              margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h4)',
              color: 'var(--color-text-primary)',
            }}>
              Target Audience
            </h4>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
              {course.targetAudience.map((aud, i) => (
                <Badge key={i} variant="neutral" size="sm">{aud}</Badge>
              ))}
            </div>
          </Card>

          <Card variant="default" padding="md" as="section">
            <h4 style={{
              margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h4)',
              color: 'var(--color-text-primary)',
            }}>
              Prerequisites
            </h4>
            {course.prerequisites.length === 0 ? (
              <p style={{
                margin: 0, fontSize: 'var(--text-body-sm)',
                color: 'var(--color-text-tertiary)',
              }}>
                No prerequisites required.
              </p>
            ) : (
              <ul style={{
                margin: 0, padding: '0 0 0 var(--space-stack-md)',
              }}>
                {course.prerequisites.map((pr, i) => (
                  <li key={i} style={{
                    fontSize: 'var(--text-body)',
                    color: 'var(--color-text-secondary)',
                  }}>
                    {pr}
                  </li>
                ))}
              </ul>
            )}
          </Card>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
          <Card variant="default" padding="md" as="section">
            <h4 style={{
              margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h4)',
              color: 'var(--color-text-primary)',
            }}>
              Course Info
            </h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
              <InfoRow label="Code" value={course.code} />
              <InfoRow label="Category" value={CATEGORY_LABELS[course.category]} />
              <InfoRow label="Level" value={LEVEL_LABELS[course.level]} />
              <InfoRow label="Delivery" value={DELIVERY_LABELS[course.deliveryMode]} />
              <InfoRow label="Language" value={course.language} />
              <InfoRow label="Duration" value={course.duration} />
              <InfoRow label="Status" value={LIFECYCLE_LABELS[course.lifecycle]} />
              <InfoRow label="Visibility" value={course.visibility} />
            </div>
          </Card>

          <Card variant="default" padding="md" as="section">
            <h4 style={{
              margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h4)',
              color: 'var(--color-text-primary)',
            }}>
              Statistics
            </h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
              <InfoRow label="Enrollments" value={String(course.enrollmentCount)} />
              <InfoRow label="Batches" value={String(course.batchCount)} />
              <InfoRow label="Modules" value={String(course.moduleCount)} />
              <InfoRow label="Instructors" value={String(course.instructorCount)} />
              <InfoRow label="Rating" value={course.rating > 0 ? `${course.rating}/5` : 'N/A'} />
              <InfoRow label="Reviews" value={String(course.reviewCount)} />
            </div>
          </Card>

          <Card variant="default" padding="md" as="section">
            <h4 style={{
              margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h4)',
              color: 'var(--color-text-primary)',
            }}>
              Timeline
            </h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
              <InfoRow label="Created" value={course.createdAt} />
              <InfoRow label="Updated" value={course.updatedAt} />
              {course.publishedAt && <InfoRow label="Published" value={course.publishedAt} />}
            </div>
          </Card>

          <Card variant="default" padding="md" as="section">
            <h4 style={{
              margin: '0 0 var(--space-stack-sm)', fontSize: 'var(--text-h4)',
              color: 'var(--color-text-primary)',
            }}>
              Actions
            </h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
              {[
                { label: 'Manage Curriculum', icon: 'layers' },
                { label: 'Manage Resources', icon: 'folder' },
                { label: 'Manage Batches', icon: 'calendar' },
                { label: 'Manage Pricing', icon: 'credit-card' },
                { label: 'Duplicate Course', icon: 'copy' },
                { label: 'Archive Course', icon: 'archive' },
                { label: 'Export Course', icon: 'download' },
              ].map((action) => (
                <button
                  key={action.label}
                  type="button"
                  style={{
                    display: 'flex', alignItems: 'center', gap: 8,
                    padding: '6px 8px', borderRadius: 'var(--radius-xs)',
                    background: 'none', border: 'none', cursor: 'pointer',
                    color: 'var(--color-text-secondary)',
                    fontSize: 'var(--text-body-sm)', textAlign: 'left',
                    width: '100%',
                  }}
                  aria-label={action.label}
                >
                  <Icon name={action.icon} size={14} color="currentColor" />
                  {action.label}
                </button>
              ))}
            </div>
          </Card>
        </div>
      </div>
    </div>
  );
});

function InfoRow({ label, value }: { label: string; value: string }) {
  return (
    <div style={{
      display: 'flex', justifyContent: 'space-between', alignItems: 'center',
      gap: 'var(--space-inline-sm)',
    }}>
      <span style={{
        fontSize: 'var(--text-caption)',
        color: 'var(--color-text-tertiary)',
        textTransform: 'uppercase',
        letterSpacing: 'var(--tracking-wide)',
      }}>
        {label}
      </span>
      <span style={{
        fontSize: 'var(--text-body-sm)',
        color: 'var(--color-text-primary)',
        fontWeight: 'var(--weight-medium)',
        textAlign: 'right',
      }}>
        {value}
      </span>
    </div>
  );
}

export default CourseDetailPage;
