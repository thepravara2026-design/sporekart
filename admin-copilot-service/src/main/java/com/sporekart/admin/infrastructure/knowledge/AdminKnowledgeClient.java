package com.sporekart.admin.infrastructure.knowledge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminKnowledgeClient {

    private static final Logger log = LoggerFactory.getLogger(AdminKnowledgeClient.class);

    private final List<Map<String, Object>> knowledgeBase;

    public AdminKnowledgeClient() {
        this.knowledgeBase = buildKnowledgeBase();
        log.info("Initialized knowledge client with {} articles", knowledgeBase.size());
    }

    private List<Map<String, Object>> buildKnowledgeBase() {
        List<Map<String, Object>> articles = new ArrayList<>();

        articles.add(kbArticle("How to process refunds for cancelled orders",
                "operations", "Step-by-step refund processing guide for admin operators. " +
                        "Refunds can be processed via Admin > Orders > Refund. Processing time: 3-5 business days.",
                "Standard refund process applies to all cancelled orders within 30 days of purchase."));

        articles.add(kbArticle("Inventory reorder approval workflow",
                "operations", "Low stock alerts trigger auto-notification to procurement team. " +
                        "Orders above ₹50,000 require manager approval. Standard reorder lead time: 5-7 days.",
                "Emergency reorder process available for critical stockouts with 24h approval."));

        articles.add(kbArticle("Customer dispute resolution policy",
                "customer-service", "Disputes are escalated to Level 1 (CS agent), Level 2 (supervisor), " +
                        "Level 3 (manager). Resolution SLA: 24h for Level 1, 48h for Level 2, 72h for Level 3.",
                "All disputes must be logged with supporting evidence within 48 hours of customer contact."));

        articles.add(kbArticle("Mushroom quality standards and grading",
                "quality", "Grade A: Perfect caps, no blemishes. Grade B: Minor imperfections. " +
                        "Grade C: Significant blemishes (processed products only). Reject: Mold or decay present.",
                "Quality inspection must be performed within 2 hours of harvest for fresh mushrooms."));

        articles.add(kbArticle("Seasonal inventory planning guidelines",
                "inventory", "Monsoon (June-Aug): Increase spawn inventory by 30%. " +
                        "Festival (Oct-Nov): Increase kit inventory by 50%. Summer (Mar-May): Increase equipment stock.",
                "Historical demand patterns show 40% higher sales during festival season."));

        articles.add(kbArticle("Admin user roles and permissions",
                "security", "Super Admin: Full access. Operations Manager: Inventory, orders, reports. " +
                        "CS Agent: Customer management, refunds. Viewer: Read-only dashboard access.",
                "Role changes require approval from Super Admin. Audit logs maintained for all changes."));

        articles.add(kbArticle("Training course approval process",
                "training", "Course proposals reviewed by curriculum committee. " +
                        "Approval stages: Content review -> Pilot run -> Feedback -> Full launch. Timeline: 4-6 weeks.",
                "Instructors must have minimum 3 years of industry experience."));

        articles.add(kbArticle("Platform maintenance schedule",
                "operations", "Scheduled maintenance: Every Sunday 2AM-4AM IST. " +
                        "Critical patches: Rolling deployment with zero downtime. " +
                        "Major updates: Quarter-end with 2-hour maintenance window.",
                "Emergency maintenance can be triggered with 30-minute notice for security issues."));

        articles.add(kbArticle("Pricing and discount approval matrix",
                "pricing", "Discounts up to 10%: Auto-approve. 10-25%: Manager approval. " +
                        "25-40%: Director approval. Above 40%: CEO approval required.",
                "Promotional pricing must be reviewed for margin impact before approval."));

        articles.add(kbArticle("B2B bulk order processing",
                "operations", "B2B orders above ₹1,00,000 get priority processing. " +
                        "Dedicated account manager assigned. Payment terms: 15/30/45 days based on credit assessment.",
                "New B2B customers require credit check and account setup (2-3 business days)."));

        articles.add(kbArticle("Return and exchange policy",
                "customer-service", "Return window: 7 days for perishables, 15 days for non-perishables. " +
                        "Exchange only for defective products. Refunds processed after quality check.",
                "Customers must provide order ID and reason for return. Photographs required for quality claims."));

        articles.add(kbArticle("Regulatory compliance for mushroom exports",
                "compliance", "Export requires FSSAI certification, phytosanitary certificate, " +
                        "and country-specific import permits. Lead time for documentation: 2-3 weeks.",
                "Consult trade advisory team for destination country-specific requirements."));

        articles.add(kbArticle("Cash flow management guidelines",
                "finance", "Monitor daily cash position. Minimum operating balance: ₹5,00,000. " +
                        "Vendor payments: Net 30. Urgent payments can be processed via CFO approval.",
                "Weekly cash flow forecast submitted to finance team every Friday."));

        articles.add(kbArticle("Data backup and recovery procedures",
                "it", "Automated backups: Every 6 hours. Full backup: Daily at 2AM. " +
                        "Retention: 30 days daily, 12 months monthly. DR drill: Quarterly.",
                "RPO: 6 hours. RTO: 2 hours for critical systems. DR tested every quarter."));

        articles.add(kbArticle("Vendor onboarding checklist",
                "procurement", "Vendor registration -> Document verification -> Background check -> " +
                        "Sample quality assessment -> Contract signing -> System onboarding. Timeline: 2-4 weeks.",
                "All vendors must agree to SporeKart's supplier code of conduct."));

        return articles;
    }

    private Map<String, Object> kbArticle(String title, String category, String content, String summary) {
        Map<String, Object> article = new LinkedHashMap<>();
        article.put("id", "KB-" + UUID.randomUUID().toString().substring(0, 8));
        article.put("title", title);
        article.put("category", category);
        article.put("content", content);
        article.put("summary", summary);
        article.put("createdAt", "2025-07-01");
        article.put("updatedAt", "2026-06-15");
        return article;
    }

    public List<Map<String, Object>> searchKnowledge(String query, String category) {
        log.info("Knowledge search: query='{}', category='{}'", query, category);
        return knowledgeBase.stream()
                .filter(a -> category == null || category.isEmpty() || category.equals(a.get("category")))
                .filter(a -> query == null || query.isEmpty() ||
                        ((String) a.get("title")).toLowerCase().contains(query.toLowerCase()) ||
                        ((String) a.get("content")).toLowerCase().contains(query.toLowerCase()))
                .map(a -> {
                    Map<String, Object> result = new LinkedHashMap<>(a);
                    double relevance = 0.5 + new Random(query != null ? query.hashCode() : 0).nextDouble() * 0.5;
                    result.put("relevanceScore", Math.round(relevance * 100.0) / 100.0);
                    return result;
                })
                .sorted((a, b) -> Double.compare((double) b.get("relevanceScore"), (double) a.get("relevanceScore")))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getFAQs(String category) {
        log.info("Fetching FAQs for category: {}", category);
        String[] faqData = {
                "How long does delivery take?: Standard delivery 3-5 business days. Express 1-2 days.",
                "What is the return policy?: 7 days for perishables, 15 days for non-perishables.",
                "How do I track my order?: Use order ID on track-order page or contact support.",
                "Do you offer training?: Yes, 10 online courses from beginner to advanced.",
                "What payment methods are accepted?: UPI, Credit Card, Net Banking, Wallet, COD.",
                "Can I get bulk discounts?: Yes, contact sales for B2B pricing and volume discounts.",
                "How are mushroom kits shipped?: In breathable packaging with moisture control.",
                "What is the shelf life of processed products?: 6-12 months depending on product."
        };

        List<Map<String, Object>> faqs = new ArrayList<>();
        for (int i = 0; i < faqData.length; i++) {
            String[] parts = faqData[i].split(":", 2);
            Map<String, Object> faq = new LinkedHashMap<>();
            faq.put("id", "FAQ-" + (i + 1));
            faq.put("question", parts[0]);
            faq.put("answer", parts.length > 1 ? parts[1].trim() : "");
            faq.put("category", "general");
            if (category == null || category.isEmpty() || category.equals(faq.get("category"))) {
                faqs.add(faq);
            }
        }
        return faqs;
    }

    public List<Map<String, Object>> getPolicyDocs(String category) {
        log.info("Fetching policy docs for category: {}", category);
        String[][] policies = {
                {"Privacy Policy", "governance", "Data collection, storage, and processing policies."},
                {"Terms of Service", "governance", "User agreement and service terms."},
                {"Refund Policy", "customer-service", "Refund eligibility and processing guidelines."},
                {"Shipping Policy", "operations", "Shipping charges, timelines, and coverage areas."},
                {"Cookie Policy", "governance", "Cookie usage and consent management."},
                {"Cancellation Policy", "customer-service", "Order cancellation terms and timelines."},
                {"Quality Assurance Policy", "quality", "Product quality standards and testing procedures."}
        };

        List<Map<String, Object>> docs = new ArrayList<>();
        for (int i = 0; i < policies.length; i++) {
            if (category == null || category.isEmpty() || category.equals(policies[i][1])) {
                Map<String, Object> doc = new LinkedHashMap<>();
                doc.put("id", "POL-" + (i + 1));
                doc.put("title", policies[i][0]);
                doc.put("category", policies[i][1]);
                doc.put("summary", policies[i][2]);
                doc.put("version", "2." + (i + 1) + ".0");
                doc.put("effectiveDate", "2026-01-01");
                docs.add(doc);
            }
        }
        return docs;
    }

    public List<Map<String, Object>> getOperationalManuals() {
        log.info("Fetching operational manuals");
        String[][] manuals = {
                {"Admin Dashboard Guide", "How to use the admin dashboard effectively."},
                {"Order Management Manual", "Complete order lifecycle management."},
                {"Inventory Management Guide", "Stock tracking and reordering procedures."},
                {"Customer Support Handbook", "Handling customer queries and complaints."},
                {"Incident Response Playbook", "Security and system incident response."},
                {"Vendor Management Guide", "Vendor onboarding and relationship management."},
                {"Financial Reporting Manual", "Monthly financial reporting procedures."}
        };

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < manuals.length; i++) {
            Map<String, Object> manual = new LinkedHashMap<>();
            manual.put("id", "MAN-" + (i + 1));
            manual.put("title", manuals[i][0]);
            manual.put("description", manuals[i][1]);
            manual.put("pages", 20 + new Random(i).nextInt(60));
            manual.put("version", "1." + (i + 1) + ".0");
            result.add(manual);
        }
        return result;
    }
}
