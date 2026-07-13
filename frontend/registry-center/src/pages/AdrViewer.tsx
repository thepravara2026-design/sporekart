interface Adr {
  id: string;
  title: string;
}

const ADRS: Adr[] = [
  { id: 'adr-001', title: 'Adopt Java 21 LTS and Spring Boot 3.x baseline' },
  { id: 'adr-002', title: 'Spring Modulith structured modules' },
  { id: 'adr-003', title: 'Apache Kafka event-driven architecture' },
  { id: 'adr-004', title: 'Redis caching and coordination' },
  { id: 'adr-005', title: 'AI Gateway central abstraction' },
  { id: 'adr-006', title: 'Contract-first development for APIs and events' },
  { id: 'adr-007', title: 'Registry Center API design' },
  { id: 'adr-008', title: 'Provider Registry pattern' },
  { id: 'adr-009', title: 'Prompt versioning strategy' },
  { id: 'adr-010', title: 'Knowledge source sync model' },
  { id: 'adr-011', title: 'Usage and cost tracking' },
  { id: 'adr-012', title: 'Global Configuration Center' },
  { id: 'adr-013', title: 'Event Catalog standard' },
  { id: 'adr-014', title: 'API Registry metadata' },
  { id: 'adr-015', title: 'Capability Discovery protocol' }
];

export default function AdrViewer() {
  return (
    <div>
      <h1 className="page-title">Architecture Decision Records</h1>
      <p className="page-subtitle">Read-only index of ADRs (sources in docs/adr).</p>
      <div className="card">
        <ul className="adr-list">
          {ADRS.map((a) => (
            <li key={a.id}>
              <a href={`/docs/adr/${a.id}.md`} target="_blank" rel="noreferrer">
                {a.id.toUpperCase()}: {a.title}
              </a>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
