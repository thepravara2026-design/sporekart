# Enterprise Marketing Copilot — AI CMO

## Overview
The Enterprise Marketing Copilot (AI CMO) is SporeKart's marketing intelligence platform that orchestrates the end-to-end marketing lifecycle — from strategy and content creation to campaign execution, SEO/AEO/GEO optimization, social media management, email/WhatsApp marketing, analytics, brand governance, and growth recommendations.

## Architecture
The Marketing Copilot follows the same microservice architecture as the BI Copilot and integrates with the existing SporeKart platform:
- **AI Gateway**: Intent routing, rate limiting, RBAC
- **Prompt Platform**: Standardized prompt templates
- **Knowledge Platform**: RAG, brand guidelines, domain knowledge
- **Conversation Engine**: Multi-turn dialog management
- **Memory Engine**: Context persistence
- **Analytics Platform**: Campaign performance tracking
- **Gateway** (port 8100): API routing to marketing-copilot-service (port 8107)

## Capabilities

| Capability | Engine | Description |
|---|---|---|
| Strategy & Briefing | StrategyEngine | Generate marketing briefs, audience personas, competitor analysis |
| Content Creation | ContentEngine | Blog articles, product descriptions, landing pages, ad copy |
| Campaign Planning | CampaignEngine | Multi-channel campaign planning, budget optimization, scheduling |
| SEO Optimization | SEOEngine | Keyword research, on-page audit, competitor analysis, traffic estimation |
| AEO/GEO Optimization | AEOGEOEngine | Answer engine optimization, generative snippet optimization, schema markup |
| Social Media Management | SocialMediaEngine | Platform-specific content, hashtag generation, posting calendar, best times |
| Email Marketing | EmailEngine | Campaign generation, subject line optimization, audience segmentation |
| WhatsApp Marketing | WhatsAppEngine | Template-based messaging, personalization, campaign management |
| Marketing Analytics | AnalyticsEngine | Campaign performance, channel attribution, ROAS/CAC calculations |
| Brand Governance | BrandGovernanceEngine | Brand consistency checks, guideline enforcement, violation detection |
| Growth Recommendations | GrowthEngine | Data-driven growth strategies, experiment suggestions, ROI projections |

## Engines Detail

### StrategyEngine
- Generates comprehensive marketing briefs from objectives
- Creates detailed audience personas by segment
- Analyzes competitive landscape
- Suggests success metrics and KPIs

### ContentEngine
- Multi-format content generation (blog, product, landing page, social, email, WhatsApp)
- SEO optimization with keyword integration
- Topic suggestion based on audience segments
- Campaign-specific content creation

### CampaignEngine
- End-to-end campaign planning across channels
- Intelligent budget allocation recommendations
- Performance simulation and forecasting
- Channel-specific strategies (launch, seasonal, awareness, lead-gen)

### SEOEngine
- Comprehensive on-page SEO analysis
- Keyword research with volume and difficulty estimation
- Competitor analysis and gap identification
- Actionable SEO checklist generation

### AEOGEOEngine
- Answer appearance score calculation for voice/AI search
- Generative snippet optimization for AI search engines
- FAQ and structured data recommendations
- People-also-ask opportunity identification

### SocialMediaEngine
- Platform-specific content generation (Instagram, Facebook, LinkedIn, YouTube, etc.)
- Smart hashtag generation
- Best posting time analysis
- Multi-day content calendar creation

### EmailEngine
- Multi-type email generation (promotional, transactional, educational, etc.)
- Subject line A/B testing suggestions
- Audience segmentation by behavior
- HTML and text format generation

### WhatsAppEngine
- Template-based message generation
- Multi-type support (text, image, video, interactive)
- Message personalization
- Template suggestion by campaign type

### AnalyticsEngine
- Campaign performance analysis with metrics
- Channel attribution and breakdown
- ROAS, CAC, CTR, conversion rate calculations
- Dashboard data generation with trends

### BrandGovernanceEngine
- Brand consistency scoring
- Banned term detection
- Brand keyword density analysis
- Channel-specific compliance checks

### GrowthEngine
- Growth area identification (acquisition, retention, expansion, etc.)
- Impact-prioritized recommendations
- Experiment suggestions with measurable outcomes
- ROI projection calculations

## Integration Points
- **Gateway**: Routes `/api/v1/copilot/marketing/**` to port 8107
- **AI Platform**: Prompt templates for marketing content generation
- **Knowledge Platform**: Brand guidelines, marketing playbooks, audience data
- **RAG**: Marketing knowledge retrieval for context-aware generation
- **Monitoring**: Prometheus metrics via MarketingMetricsService
- **Analytics**: Campaign performance data integration

## Getting Started

1. **Build**: `mvn clean compile -pl marketing-copilot-service -am`
2. **Test**: `mvn test -pl marketing-copilot-service`
3. **Run**: `mvn spring-boot:run -pl marketing-copilot-service`
4. **API Docs**: http://localhost:8107/swagger-ui.html
5. **Health**: http://localhost:8107/actuator/health

## Configuration
Key application.yml properties under `sporekart.marketing.copilot`:
- `streaming-timeout-seconds`: 300
- `default-audience`: home-growers,commercial-growers,enthusiasts
- `brand-voice`: professional,creative,data-driven,persuasive,business-oriented
- `seo-default-locale`: en_IN
- `social-platforms`: instagram,facebook,linkedin,youtube,threads,pinterest,twitter
