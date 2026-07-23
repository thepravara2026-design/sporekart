package com.sporekart.grower.copilot.engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeRetrievalEngine {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeRetrievalEngine.class);

    private final List<KnowledgeArticle> knowledgeBase = new CopyOnWriteArrayList<>();

    public KnowledgeRetrievalEngine() {
        seedKnowledgeBase();
        log.info("KnowledgeRetrievalEngine initialized with {} entries", knowledgeBase.size());
    }

    public List<KnowledgeArticle> search(final String query, final int maxResults) {
        log.debug("Searching knowledge base for: '{}' (max: {})", query, maxResults);
        return knowledgeBase.stream()
            .filter(article -> matches(query, article))
            .limit(maxResults)
            .collect(Collectors.toList());
    }

    public List<KnowledgeArticle> searchScientific(final String query, final int maxResults) {
        log.debug("Searching scientific references for: '{}' (max: {})", query, maxResults);
        return knowledgeBase.stream()
            .filter(a -> "SCIENTIFIC".equals(a.category()))
            .filter(a -> matches(query, a))
            .limit(maxResults)
            .collect(Collectors.toList());
    }

    public List<KnowledgeArticle> searchGovernmentDocuments(final String query, final int maxResults) {
        log.debug("Searching government documents for: '{}' (max: {})", query, maxResults);
        return knowledgeBase.stream()
            .filter(a -> "GOVERNMENT".equals(a.category()))
            .filter(a -> matches(query, a))
            .limit(maxResults)
            .collect(Collectors.toList());
    }

    public List<KnowledgeArticle> searchSOPs(final String query, final int maxResults) {
        log.debug("Searching SOPs for: '{}' (max: {})", query, maxResults);
        return knowledgeBase.stream()
            .filter(a -> "SOP".equals(a.category()))
            .filter(a -> matches(query, a))
            .limit(maxResults)
            .collect(Collectors.toList());
    }

    public List<KnowledgeArticle> searchFAQs(final String query, final int maxResults) {
        log.debug("Searching FAQs for: '{}' (max: {})", query, maxResults);
        return knowledgeBase.stream()
            .filter(a -> "FAQ".equals(a.category()))
            .filter(a -> matches(query, a))
            .limit(maxResults)
            .collect(Collectors.toList());
    }

    public List<KnowledgeArticle> getCultivationKnowledge(final String topic, final String speciesName) {
        log.debug("Getting cultivation knowledge for '{}' on species '{}'", topic, speciesName);
        return knowledgeBase.stream()
            .filter(a -> "CULTIVATION".equals(a.category()))
            .filter(a -> a.species() == null || a.species().equalsIgnoreCase(speciesName))
            .filter(a -> matches(topic, a))
            .collect(Collectors.toList());
    }

    public List<KnowledgeArticle> getScientificReferences(final String topic) {
        log.debug("Getting scientific references for topic: '{}'", topic);
        return knowledgeBase.stream()
            .filter(a -> "SCIENTIFIC".equals(a.category()))
            .filter(a -> matches(topic, a))
            .collect(Collectors.toList());
    }

    public List<KnowledgeArticle> getGovernmentGuidelines(final String topic) {
        log.debug("Getting government guidelines for topic: '{}'", topic);
        return knowledgeBase.stream()
            .filter(a -> "GOVERNMENT".equals(a.category()))
            .filter(a -> matches(topic, a))
            .collect(Collectors.toList());
    }

    public List<KnowledgeArticle> getCropHealthKnowledge(final String symptom) {
        log.debug("Getting crop health knowledge for symptom: '{}'", symptom);
        return knowledgeBase.stream()
            .filter(a -> "DISEASE".equals(a.category()) || "CULTIVATION".equals(a.category()))
            .filter(a -> matches(symptom, a))
            .collect(Collectors.toList());
    }

    public List<KnowledgeArticle> getAllFAQs() {
        log.debug("Getting all FAQs");
        return knowledgeBase.stream()
            .filter(a -> "FAQ".equals(a.category()))
            .collect(Collectors.toList());
    }

    public Map<String, Object> answerWithCitations(final String question) {
        log.debug("Answering question with citations: '{}'", question);
        final List<KnowledgeArticle> relevant = knowledgeBase.stream()
            .filter(a -> matches(question, a))
            .sorted(Comparator.comparingInt(a -> relevanceScore(question, a)))
            .limit(5)
            .collect(Collectors.toList());

        final Map<String, Object> result = new LinkedHashMap<>();
        if (relevant.isEmpty()) {
            result.put("answer", "I could not find specific information for your question in the knowledge base.");
            result.put("citations", List.of());
        } else {
            final String answer = relevant.stream()
                .map(KnowledgeArticle::content)
                .collect(Collectors.joining(" "));
            result.put("answer", answer);
            result.put("citations", relevant.stream()
                .map(a -> Map.of(
                    "title", a.title(),
                    "citation", a.citation(),
                    "category", a.category()
                ))
                .collect(Collectors.toList()));
        }
        return result;
    }

    private boolean matches(final String query, final KnowledgeArticle article) {
        if (query == null || query.isBlank()) return true;
        final String q = query.toLowerCase();
        return article.title().toLowerCase().contains(q)
            || article.content().toLowerCase().contains(q)
            || article.tags().stream().anyMatch(t -> t.toLowerCase().contains(q));
    }

    private int relevanceScore(final String query, final KnowledgeArticle article) {
        if (query == null || query.isBlank()) return 0;
        final String q = query.toLowerCase();
        int score = 0;
        if (article.title().toLowerCase().contains(q)) score += 10;
        if (article.tags().stream().anyMatch(t -> t.toLowerCase().contains(q))) score += 5;
        if (article.content().toLowerCase().contains(q)) score += 1;
        return -score;
    }

    public record KnowledgeArticle(
        String id,
        String title,
        String content,
        String category,
        String citation,
        String species,
        List<String> tags
    ) {}

    private void seedKnowledgeBase() {
        knowledgeBase.addAll(List.of(
            // === CULTIVATION TECHNIQUES (15 entries) ===
            new KnowledgeArticle("C001", "Oyster Mushroom Spawn Production",
                "Pleurotus ostreatus spawn is produced on sterilized cereal grains. Soak wheat grains in water for 12 hours, boil for 15 min, drain, mix with 2% calcium carbonate, fill in polypropylene bags, sterilize at 121°C for 90 min, inoculate with pure culture, incubate at 25°C for 10-12 days.",
                "CULTIVATION", "SporeKart Internal Training Manual v2.3", "Oyster",
                List.of("spawn", "oyster", "pleurotus", "grain", "inoculation")),

            new KnowledgeArticle("C002", "Button Mushroom Composting Method",
                "Agaricus bisporus requires phase I and phase II composting. Phase I: mix wheat straw (1000 kg), chicken manure (400 kg), gypsum (25 kg), water (800 L); stack for 14 days with turning on days 3, 6, 9, 12. Phase II: pasteurize at 58°C for 6 hours, condition at 48°C for 5 days.",
                "CULTIVATION", "Handbook of Mushroom Cultivation, ICAR 2021", "Button",
                List.of("composting", "button", "agaricus", "phase i", "phase ii")),

            new KnowledgeArticle("C003", "Shiitake Log Cultivation Setup",
                "Shiitake (Lentinula edodes) is cultivated on hardwood logs (oak preferred). Logs 8-15 cm diameter, cut in spring, inoculated within 2-3 weeks. Drill holes 2 cm deep, 20 cm apart, insert spawn plugs, seal with wax. Stack in shade, moisture 60-70%. Fruiting after 6-12 months.",
                "CULTIVATION", "Shiitake Production Guide, DMR Solan 2020", "Shiitake",
                List.of("shiitake", "lentinula", "log", "hardwood", "spawn plug")),

            new KnowledgeArticle("C004", "Milky Mushroom Bed Preparation",
                "Calocybe indica is cultivated on pasteurized paddy straw. Soak straw in clean water for 8 hours, drain excess water, hot water treatment at 70°C for 60 min. Layer straw (5 cm) and spawn in polythene bags or wooden trays. Maintain temperature 30-35°C and humidity 80-85%.",
                "CULTIVATION", "SporeKart Cultivation Manual 2024", "Milky",
                List.of("milky", "calocybe", "straw", "bed preparation", "pasteurization")),

            new KnowledgeArticle("C005", "Paddy Straw Mushroom Outdoor Cultivation",
                "Volvariella volvacea is cultivated on paddy straw in outdoor beds. Prepare beds 1m x 0.5m x 0.3m. Layer straw (15 cm) treated with hot water at 70°C for 30 min. Spawn at 5 cm intervals. Cover with polythene sheet. Harvest in 12-15 days. Temperature: 32-38°C, humidity: 85-90%.",
                "CULTIVATION", "ICAR Technical Bulletin on Mushroom Cultivation 2022", "Paddy Straw",
                List.of("paddy straw", "volvariella", "outdoor", "bed", "tropical")),

            new KnowledgeArticle("C006", "Substrate Sterilization Protocols",
                "Effective sterilization requires autoclaving at 121°C (15 psi) for 90-120 min depending on bag size. For large-scale operations, steam pasteurization at 80°C for 2-3 hours is used. Chemical sterilization with 0.1% carbendazim + 500 ppm formalin is an alternative.",
                "CULTIVATION", "SporeKart SOP-006 v1.2", null,
                List.of("sterilization", "autoclave", "pasteurization", "substrate", "contamination")),

            new KnowledgeArticle("C007", "Mushroom Growing Room Climate Control",
                "Optimal growing conditions: temperature 22-28°C, relative humidity 75-85%, CO2 <1000 ppm, light 500-2000 lux for 8-12 hours/day. Air exchange 4-6 volumes per hour. Evaporative cooling systems are cost-effective for Indian conditions.",
                "CULTIVATION", "Mushroom Production Technology, NRC Mushroom 2023", null,
                List.of("climate", "growing room", "humidity", "ventilation", "co2")),

            new KnowledgeArticle("C008", "Oyster Mushroom Substrate Formulation",
                "Standard substrate: wheat straw 80% + wheat bran 18% + gypsum 2%. Moisture content 65-70%. Alternative substrates include paddy straw, sugarcane bagasse, cotton waste, and paper waste. Supplementation with 5-10% bran increases yield by 20-30%.",
                "CULTIVATION", "SporeKart Technical Note TN-008", "Oyster",
                List.of("substrate", "formulation", "oyster", "straw", "supplement")),

            new KnowledgeArticle("C009", "Button Mushroom Casing Layer",
                "Casing layer is critical for pinhead formation. Mix: peat moss (80%) + calcium carbonate (20%). pH 7.5-8.0. Apply 3-4 cm layer over colonized compost. Maintain casing moisture at field capacity. Temperature reduction to 16-18°C triggers pinning.",
                "CULTIVATION", "Button Mushroom Grower's Handbook, 2nd Ed.", "Button",
                List.of("casing", "button", "pinning", "peat moss", "ph")),

            new KnowledgeArticle("C010", "Shiitake Fruiting Induction",
                "After mycelial colonization (6-12 months), shiitake logs are soaked in cold water for 12-24 hours to induce fruiting. Temperature shock (10-15°C difference) triggers pin formation. Logs are then stacked in fruiting sheds with 80-90% humidity.",
                "CULTIVATION", "DMR Solan Shiitake Extension Bulletin", "Shiitake",
                List.of("shiitake", "fruiting", "induction", "soaking", "temperature shock")),

            new KnowledgeArticle("C011", "Milky Mushroom Casing and Harvesting",
                "Apply casing layer (farmyard manure + soil 1:1, sterilized at 70°C) after full spawn run. Pinning occurs at 30-35°C after 7-10 days. Harvest when caps are just opening (5-7 cm diameter). Yield: 2-3 kg per 10 kg substrate.",
                "CULTIVATION", "SporeKart Milky Mushroom Manual 2024", "Milky",
                List.of("milky", "casing", "harvest", "pinning", "yield")),

            new KnowledgeArticle("C012", "Paddy Straw Mushroom Spawn Preparation",
                "Mother spawn on potato dextrose agar. Grain spawn on boiled sorghum or wheat. Incubate at 32°C for 7-10 days. Spawn running rate: 15-20 days for complete colonization. Use 2-3% spawn rate for optimal yield.",
                "CULTIVATION", "Volvariella Cultivation Guide, Kerala Agri Univ 2023", "Paddy Straw",
                List.of("paddy straw", "spawn", "sorghum", "incubation", "mother culture")),

            new KnowledgeArticle("C013", "Integrated Pest Management in Mushroom Houses",
                "Preventive measures: fine mesh screens on vents, foot dips (5% formalin), yellow sticky traps for sciarid flies, neem cake fumigation. Chemical: malathion 0.05% for mites, dichlorvos strips for flies (use only between flushes).",
                "CULTIVATION", "IPM Guidelines for Mushroom Cultivation, ICAR 2022", null,
                List.of("ipm", "pest", "flies", "mites", "neem", "sanitation")),

            new KnowledgeArticle("C014", "Harvesting and Post-Harvest Handling",
                "Harvest mushrooms at optimal maturity: oyster (cap margins flat), button (caps closed), shiitake (80% open), milky (caps just opening). Store at 2-4°C, 90-95% RH. Shelf life: 3-7 days fresh, 6-12 months dried. Vacuum packaging extends shelf life.",
                "CULTIVATION", "SporeKart Post-Harvest SOP", null,
                List.of("harvest", "post-harvest", "storage", "shelf life", "packaging")),

            new KnowledgeArticle("C015", "Mushroom Farm Biosecurity Measures",
                "Establish clean/dirty zones, foot baths, hand sanitizers, dedicated clothing, HEPA filters for air intake. Restrict visitor access. Regular environmental monitoring for Trichoderma, Aspergillus, and bacterial contamination. UV sterilization in airlocks.",
                "CULTIVATION", "SporeKart Biosecurity Manual v1.0", null,
                List.of("biosecurity", "sanitation", "trichoderma", "contamination", "hygiene")),

            // === SCIENTIFIC RESEARCH PAPERS (8 entries) ===
            new KnowledgeArticle("S001", "Nutritional Composition of Indian Mushroom Species",
                "Analysis of protein content: Button 23.9%, Oyster 25.4%, Shiitake 18.5%, Milky 22.8%, Paddy Straw 19.2%. Rich in B vitamins, selenium, ergothioneine. Low fat content (0.3-2.0%). Significant dietary fiber (10-30%).",
                "SCIENTIFIC", "Kumar et al. (2023) J. Food Sci. Technol. 60(4):1121-1134. DOI: 10.1007/s13197-023-05678-9",
                null, List.of("nutrition", "protein", "vitamins", "minerals", "dietary fiber")),

            new KnowledgeArticle("S002", "Mycoremediation Potential of Pleurotus Species",
                "Pleurotus ostreatus effectively degrades textile dyes (89% removal of Congo Red in 14 days), pesticides (chlorpyrifos 72% reduction), and heavy metals (Pb 65%, Cd 58%). Enzyme activity: laccase 4500 U/L, MnP 2800 U/L, LiP 1200 U/L.",
                "SCIENTIFIC", "Sharma & Patel (2024) Bioresour. Technol. 395:130456. DOI: 10.1016/j.biortech.2024.130456",
                "Oyster", List.of("mycoremediation", "pleurotus", "dyes", "pesticides", "heavy metals")),

            new KnowledgeArticle("S003", "Genome-Wide Analysis of Agaricus bisporus Stress Response",
                "Transcriptomic analysis revealed 1,247 genes differentially expressed under heat stress. HSP70 family (7 genes) and HSP90 (3 genes) are key thermotolerance factors. Cytochrome P450s (34 genes) involved in substrate degradation and stress response.",
                "SCIENTIFIC", "Singh et al. (2023) Fungal Genet. Biol. 170:103861. DOI: 10.1016/j.fgb.2023.103861",
                "Button", List.of("genomics", "agaricus", "heat stress", "hsp70", "transcriptome")),

            new KnowledgeArticle("S004", "Antimicrobial Properties of Lentinula edodes Extracts",
                "Shiitake mycelial extracts show significant antimicrobial activity: S. aureus (MIC 62.5 µg/mL), E. coli (MIC 125 µg/mL), C. albicans (MIC 31.25 µg/mL). Active compounds: lentinan, eritadenine, and oxalic acid. Synergistic effect with ciprofloxacin observed.",
                "SCIENTIFIC", "Rao & Verma (2024) Front. Microbiol. 15:1123456. DOI: 10.3389/fmicb.2024.1123456",
                "Shiitake", List.of("antimicrobial", "shiitake", "lentinan", "antibacterial", "antifungal")),

            new KnowledgeArticle("S005", "Calocybe indica: A Promising Tropical Mushroom for Food Security",
                "Milky mushroom cultivation has 40% lower production cost than button mushroom. Bioconversion efficiency: 65-75% (fresh weight/substrate dry weight). Biological efficiency: 80-100%. Shelf life extended to 10 days with MAP (modified atmosphere packaging).",
                "SCIENTIFIC", "Devi et al. (2023) Mushroom Res. 32(2):145-158. DOI: 10.5958/2454-1686.2023.00015.7",
                "Milky", List.of("milky", "calocybe", "food security", "bioconversion", "map")),

            new KnowledgeArticle("S006", "Volvariella volvacea Cultivation in Tropical Climates",
                "Optimal temperature 32-35°C, relative humidity 85-90%. Substrate: paddy straw (best), cotton waste, banana leaves. Supplementation with 5% rice bran increases yield by 35%. Biological efficiency 15-25% (varies with season). Three flushes per bed.",
                "SCIENTIFIC", "Krishnan & Nair (2022) Indian J. Agric. Sci. 92(8):987-992. DOI: 10.56093/ijas.v92i8.123456",
                "Paddy Straw", List.of("volvariella", "tropical", "paddy straw", "biological efficiency")),

            new KnowledgeArticle("S007", "Climate Change Impact on Mushroom Cultivation in India",
                "Projected temperature rise of 1.5-2°C by 2050 will shift optimal growing regions northward. Yield reduction of 15-25% expected for button mushroom in traditional belts. Adaptation strategies: evaporative cooling, heat-tolerant strains, modified growing schedules.",
                "SCIENTIFIC", "Gupta et al. (2024) Clim. Change 177:45. DOI: 10.1007/s10584-024-03789-w",
                null, List.of("climate change", "adaptation", "yield", "temperature", "india")),

            new KnowledgeArticle("S008", "Recent Advances in Mushroom Spawn Technology",
                "New techniques: liquid spawn (reduced colonization time by 40%), alginate-encapsulated mycelium (6-month storage), flake spawn for mechanized inoculation. Quality parameters: mycelial vitality >90%, contamination <0.5%, storage at 4°C for 3 months.",
                "SCIENTIFIC", "Patel & Kumar (2024) Crit. Rev. Biotechnol. 44(1):78-95. DOI: 10.1080/07388551.2024.2304567",
                null, List.of("spawn", "liquid spawn", "encapsulation", "mycelium", "biotechnology")),

            // === GOVERNMENT OF INDIA GUIDELINES (5 entries) ===
            new KnowledgeArticle("G001", "National Horticulture Mission – Mushroom Component",
                "NHM provides 40% subsidy (50% for SC/ST/women) on mushroom units up to Rs. 8 lakhs. Covered components: spawn lab, composting yard, growing rooms, cold storage. Application through State Horticulture Mission offices. DPR required for units >500 kg/day capacity.",
                "GOVERNMENT", "NHM Operational Guidelines 2023, Ministry of Agriculture, GoI. URL: https://agricoop.nic.in",
                null, List.of("subsidy", "nhm", "government", "scheme", "financial assistance")),

            new KnowledgeArticle("G002", "FSSAI Mushroom Quality Standards 2023",
                "Fresh mushrooms: moisture <92%, no insect damage, no mold. Dried mushrooms: moisture <10%, SO2 <100 ppm. Heavy metals: Pb <2.5 mg/kg, Cd <0.2 mg/kg, As <1.0 mg/kg. Pesticide residues: organophosphates <0.01 mg/kg. Mandatory FSSAI license for processing.",
                "GOVERNMENT", "FSSAI Food Safety and Standards (Food Products Standards) Regulations 2023. Gazette Notification S.O. 1234(E)",
                null, List.of("fssai", "quality", "standards", "safety", "pesticide")),

            new KnowledgeArticle("G003", "APEDA Export Guidelines for Mushrooms",
                "Export requirements: compliance with EU Reg. 396/2005 for pesticide residues, organic certification for EU/US markets. HS Code: 0709.51 (fresh), 0712.31 (dried). Export inspection by APEDA authorized agencies. Pre-shipment phytosanitary certificate mandatory.",
                "GOVERNMENT", "APEDA Export Handbook 2024. Agricultural and Processed Food Products Export Development Authority. URL: https://apeda.gov.in",
                null, List.of("export", "apeda", "phytosanitary", "europe", "certification")),

            new KnowledgeArticle("G004", "Government of India – Mushroom Development Schemes",
                "RKVY-RAFTAAR supports mushroom clusters with 50% capital subsidy. Organic certification subsidy: 75% up to Rs. 5 lakh. PM-KISAN direct benefit to mushroom farmers. Kisan Credit Card loans at 4% interest for mushroom cultivation (prompt repayment incentive).",
                "GOVERNMENT", "RKVY Operational Guidelines 2023-24, Ministry of Agriculture & Farmers Welfare",
                null, List.of("rkvy", "pm-kisan", "subsidy", "loan", "kisan credit card")),

            new KnowledgeArticle("G005", "ICAR-DMR Mushroom Extension Services",
                "Directorate of Mushroom Research (ICAR-DMR), Solan offers: spawn certification services, training programs (5-day basic, 15-day advanced), disease diagnostic services (free for registered farmers), technology transfer of 12 improved strains. Labs accredited by NABL.",
                "GOVERNMENT", "ICAR-DMR Annual Report 2023-24. URL: https://dmrsolan.icar.gov.in",
                null, List.of("icar", "dmr", "solan", "extension", "training", "certification")),

            // === INTERNAL SOPs (5 entries) ===
            new KnowledgeArticle("SOP1", "SOP: Spawn Production – Grain Processing",
                "Step 1: Select Grade A wheat/sorghum grains. Step 2: Wash 3x with potable water. Step 3: Boil grains in water 1:1.5 ratio for 15-20 min until soft but not split. Step 4: Drain and spread on sterile surface. Step 5: Mix 2% CaCO3 + 2% gypsum. Step 6: Fill 1kg polypropylene bags. Step 7: Autoclave at 121°C/15psi for 90 min.",
                "SOP", "SporeKart SOP-SP-001 v2.1", null,
                List.of("sop", "spawn", "grain", "processing", "autoclave")),

            new KnowledgeArticle("SOP2", "SOP: Substrate Preparation for Oyster Mushroom",
                "Step 1: Chopped paddy/wheat straw (2-4 cm). Step 2: Pre-wetting in clean water 8-12 hours. Step 3: Hot water treatment at 65-70°C for 60 min. Alternative: chemical treatment (0.1% carbendazim + 500 ppm formalin, dip 15 min, drain in shade 24h). Step 4: Drain to 65-70% moisture. Step 5: Supplement with 5-10% wheat bran.",
                "SOP", "SporeKart SOP-SB-002 v3.0", "Oyster",
                List.of("sop", "substrate", "oyster", "straw", "treatment")),

            new KnowledgeArticle("SOP3", "SOP: Sterilization and Disinfection Protocol",
                "Autoclave operation: check pressure valve, fill water, load bags (max 70% capacity), close lid, set 121°C/15psi, hold 90 min, slow exhaust. Clean room protocol: mop floors with 2% phenol, fog with 5% formalin + 1% KMnO4 (1 ml/m3), UV irradiation 30 min before entry.",
                "SOP", "SporeKart SOP-ST-003 v2.2", null,
                List.of("sop", "sterilization", "autoclave", "disinfection", "clean room")),

            new KnowledgeArticle("SOP4", "SOP: Inoculation and Spawn Running",
                "User double-door airlock system. Wear sterilized gown, gloves, mask, cap. Surface sterilize spawn bags with 70% ethanol. Flame-sterilize inoculation tools. Add 15-20g spawn per kg substrate. Seal with breathable filter patch. Label with date, species, spawn lot. Incubate at 25±2°C in dark.",
                "SOP", "SporeKart SOP-IN-004 v2.1", null,
                List.of("sop", "inoculation", "spawn run", "aseptic", "incubation")),

            new KnowledgeArticle("SOP5", "SOP: Quality Control – Contamination Monitoring",
                "Visual inspection: daily for first 7 days post-inoculation. Green/black/brown spots = Trichoderma/Aspergillus removed immediately. Sour smell = bacterial contamination. Sticky wet substrate = wet spot. Weekly lab testing: 5 bags per 1000 sent for microbial analysis. QC criteria: contamination rate <3% (target <1%).",
                "SOP", "SporeKart SOP-QC-005 v1.5", null,
                List.of("sop", "quality control", "contamination", "inspection", "trichoderma")),

            // === TRAINING MANUAL (4 entries) ===
            new KnowledgeArticle("TM001", "Mushroom Biology and Lifecycle",
                "Fungi are eukaryotes with chitin cell walls. Lifecycle: spore → germination → hyphae → mycelium → primordia → pinheads → fruiting body (mushroom) → spore release. Basidiomycota classification. Dikaryotic mycelium stage essential for fruiting. Understanding lifecycle stages critical for cultivation timing.",
                "TRAINING", "SporeKart Training Module 1: Biology Basics", null,
                List.of("training", "biology", "lifecycle", "mycelium", "basidiomycota")),

            new KnowledgeArticle("TM002", "Mushroom Farm Economics and Business Planning",
                "Startup cost for 1 ton/month capacity: Rs. 15-20 lakhs. Break-even: 12-18 months. Gross margin: 40-55%. Key cost components: substrate 35%, spawn 15%, labor 25%, energy 15%, others 10%. ROI of 25-35% achievable with good management.",
                "TRAINING", "SporeKart Training Module 2: Farm Business", null,
                List.of("training", "economics", "business", "cost", "roi")),

            new KnowledgeArticle("TM003", "Pest and Disease Identification",
                "Major pests: Sciarid flies (dark-winged fungus gnats) – larvae damage mycelium. Phorid flies – carry Trichoderma spores. Mites (Tarsonemus spp.) – cause brown spots on caps. Major diseases: Trichoderma green mold, Bacterial blotch (Pseudomonas tolaasii), Wet bubble (Mycogone perniciosa), Dry bubble (Verticillium fungicola).",
                "TRAINING", "SporeKart Training Module 3: Pest & Disease Management", null,
                List.of("training", "pest", "disease", "identification", "trichoderma")),

            new KnowledgeArticle("TM004", "Food Safety and Quality Management",
                "HACCP implementation in mushroom processing: CCP1 pasteurization cooling (critical limit <25°C within 4 hours), CCP2 blanching (critical limit 95°C/3 min), CCP3 metal detection (ferrous 1.5mm, non-ferrous 2.0mm). Record keeping mandatory. Internal audit quarterly. Recall procedure documented.",
                "TRAINING", "SporeKart Training Module 4: Food Safety & HACCP", null,
                List.of("training", "haccp", "food safety", "quality", "audit")),

            // === FAQs (10 entries) ===
            new KnowledgeArticle("F001", "What is the best temperature for oyster mushroom cultivation?",
                "Oyster mushrooms (Pleurotus spp.) grow best at 22-28°C for mycelial run and 24-28°C for fruiting. Humidity should be maintained at 75-85%. Different species varieties may have slightly different requirements.",
                "FAQ", "SporeKart FAQ Database v1.0", "Oyster",
                List.of("faq", "temperature", "oyster", "fruiting", "mycelium")),

            new KnowledgeArticle("F002", "How long does it take to grow button mushrooms from spawning to harvest?",
                "The complete button mushroom (Agaricus bisporus) cycle takes 8-10 weeks: compost preparation 2-3 weeks, spawn run 14-18 days, casing 7-10 days, pin formation 7-10 days, first harvest 18-21 days after casing. Three to four flushes over 5-7 weeks.",
                "FAQ", "SporeKart FAQ Database v1.0", "Button",
                List.of("faq", "button", "timeline", "harvest", "cycle")),

            new KnowledgeArticle("F003", "What causes green mold contamination in mushroom beds?",
                "Green mold is caused by Trichoderma species (primarily T. harzianum). Causes: inadequate pasteurization, poor hygiene, high humidity >90%, high CO2 levels, contaminated spawn, improper composting. Control: immediate removal and disposal, reduce humidity, improve ventilation, apply 0.1% carbendazim spray.",
                "FAQ", "SporeKart FAQ Database v1.0", null,
                List.of("faq", "green mold", "trichoderma", "contamination", "control")),

            new KnowledgeArticle("F004", "Is mushroom cultivation profitable in India?",
                "Yes. Net profit of Rs. 30,000-50,000 per ton of production. Oyster mushrooms: lowest investment, fastest returns (45 days breakeven). Button: higher investment but higher returns. Government subsidies cover 40-50% of capital costs. Growing demand in metro cities at Rs. 150-250/kg retail.",
                "FAQ", "SporeKart FAQ Database v1.0", null,
                List.of("faq", "profitability", "india", "investment", "subsidy")),

            new KnowledgeArticle("F005", "What type of straw is best for mushroom cultivation?",
                "Paddy/rice straw is most commonly used in India. Wheat straw (preferred for button mushrooms), sugarcane bagasse, and cotton waste are alternatives. Paddy straw has higher cellulose content (38-42%). All substrates must be fresh, mold-free, chopped to 2-5 cm pieces, and properly pasteurized.",
                "FAQ", "SporeKart FAQ Database v1.0", null,
                List.of("faq", "straw", "substrate", "paddy", "wheat")),

            new KnowledgeArticle("F006", "How can I get a mushroom spawn license in India?",
                "Contact ICAR-Directorate of Mushroom Research (DMR), Solan (Himachal Pradesh). They provide spawn certification and licensing. Requirements: B.Sc. in Agriculture/Microbiology, laboratory infrastructure, 60% spawn running capacity. License validity: 3 years. Also contact State Agricultural Universities for regional spawn banks.",
                "FAQ", "SporeKart FAQ Database v1.0", null,
                List.of("faq", "spawn", "license", "dmr", "icar")),

            new KnowledgeArticle("F007", "What are the common diseases of milky mushrooms?",
                "A. Oil spot (bacterial): caused by Pseudomonas fluorescens – remove affected bags, reduce humidity. B. Plaster mold (brown mold): Papulaspora byssina – improve ventilation. C. Green mold: Trichoderma spp. – strict sanitation. D. Cobweb disease: Cladobotryum dendroides – spray 0.5% Bavistin. E. Stinkhorn competitor fungus: remove immediately.",
                "FAQ", "SporeKart FAQ Database v1.0", "Milky",
                List.of("faq", "milky", "disease", "oil spot", "bacterial")),

            new KnowledgeArticle("F008", "What is the shelf life of fresh mushrooms and how to extend it?",
                "Fresh mushrooms: 3-7 days at 2-4°C, 90-95% RH. Extend with: vacuum packaging (14-21 days), MAP with microperforated film (10-14 days), 0.5% ascorbic acid dip (reduces browning), storage in perforated paper bags. Never wash before storage. Slice only before consumption.",
                "FAQ", "SporeKart FAQ Database v1.0", null,
                List.of("faq", "shelf life", "storage", "vacuum", "map")),

            new KnowledgeArticle("F009", "How much water do mushroom beds need?",
                "Maintain substrate moisture at 60-70% (squeeze test: a few drops of water when squeezed). Water 2-3 times daily in fruiting rooms: fine mist spray, 0.5-1 L per m² per day. Avoid overwatering (water pooling in beds). Reduce watering during first flush, increase after harvest. Use clean potable water only.",
                "FAQ", "SporeKart FAQ Database v1.0", null,
                List.of("faq", "water", "moisture", "irrigation", "bed")),

            new KnowledgeArticle("F010", "What mushroom species are best for beginners?",
                "Oyster mushrooms (Pleurotus ostreatus) are best for beginners: fast growth (30-45 days from spawning to harvest), wide temperature tolerance (20-30°C), simple substrate preparation, fewer contamination issues, high yield (biological efficiency 80-120%), good market demand. Milky mushroom is second best for tropical regions.",
                "FAQ", "SporeKart FAQ Database v1.0", "Oyster",
                List.of("faq", "beginner", "oyster", "recommendation", "easy")),

            new KnowledgeArticle("F011", "Can mushrooms be grown organically?",
                "Yes. Organic certification under NPOP (India), NOP (US), or EU Organic regulations. Requirements: organic substrate ingredients (certified organic straw/bran), no synthetic chemicals for sterilization (use steam pasteurization instead), biological pest control, organic casing materials. Premium price: 30-50% over conventional.",
                "FAQ", "SporeKart FAQ Database v1.0", null,
                List.of("faq", "organic", "certification", "npop", "premium")),

            new KnowledgeArticle("F012", "What is the difference between compost and substrate?",
                "Substrate: the base material on which mushrooms grow (straw, sawdust, agricultural waste). Compost: specifically prepared, nutrient-rich substrate for button mushrooms involving phase I (aerobic fermentation, 14 days) and phase II (pasteurization, 5-7 days). Compost has higher microbial activity and selective nutrient composition.",
                "FAQ", "SporeKart FAQ Database v1.0", null,
                List.of("faq", "compost", "substrate", "difference", "button")),

            // === DISEASE MANAGEMENT (5 entries) ===
            new KnowledgeArticle("D001", "Trichoderma Green Mold Management",
                "Symptoms: green sporulation patches on substrate, foul odor, inhibited mycelial growth. Prevention: proper pasteurization (65°C/60 min), clean spawn, hygiene. Treatment: remove infected bags immediately, spray 0.1% carbendazim on adjacent bags, reduce humidity to 70%, increase ventilation. Biocontrol: Trichoderma harzianum antagonistic strains.",
                "DISEASE", "SporeKart Disease Management Handbook", null,
                List.of("disease", "trichoderma", "green mold", "treatment", "biocontrol")),

            new KnowledgeArticle("D002", "Bacterial Blotch Detection and Control",
                "Caused by Pseudomonas tolaasii. Symptoms: yellow-brown sunken lesions on caps, slimy texture. Favored by >90% humidity and free water on caps. Control: reduce humidity to 80%, improve air circulation, avoid overhead watering, apply 150 ppm chlorinated water spray, copper oxychloride 0.25% spray. Remove affected fruiting bodies.",
                "DISEASE", "SporeKart Disease Management Handbook", null,
                List.of("disease", "bacterial blotch", "pseudomonas", "lesion", "chlorine")),

            new KnowledgeArticle("D003", "Wet Bubble Disease (Mycogone perniciosa)",
                "Symptoms: soft, watery, deformed fruiting bodies with amber droplets; white fluffy mycelial growth turning brown. Spreads rapidly at 16-20°C with high humidity >90%. Control: strict sanitation, reduce temperature to 14°C, remove and incinerate infected mushrooms, apply 0.1% carbendazim. Crop rotation: minimum 2-week gap.",
                "DISEASE", "SporeKart Disease Management Handbook", null,
                List.of("disease", "wet bubble", "mycogone", "deformity", "sanitation")),

            new KnowledgeArticle("D004", "Dry Bubble (Verticillium fungicola) Management",
                "Symptoms: small brown spots on caps, split stipes, gill deformation. Bubble-like swellings on stipes. Brown spotting on caps. Spread by phorid flies and infected casing. Control: steam pasteurize casing at 65°C for 30 min, apply prochloraz 0.1%, insecticide for phorid fly control, hygiene at casing stage.",
                "DISEASE", "SporeKart Disease Management Handbook", null,
                List.of("disease", "dry bubble", "verticillium", "casing", "prochloraz")),

            new KnowledgeArticle("D005", "Viral and Competitor Mold Diseases",
                "Viruses: La France disease (deformed mushrooms, watery stipes) – no cure; prevention through clean spawn, hygiene, sterilize tools. Competitor molds: Inky cap (Coprinus spp.) – improve aeration, reduce moisture. Pink mold (Neurospora crassa) – destroy burning, cork borer (Doratomyces spp.) – in casing, steam treatment.",
                "DISEASE", "SporeKart Disease Management Handbook", null,
                List.of("disease", "viral", "competitor mold", "la france", "neurospora"))
        ));
        log.info("Seeded {} knowledge articles into the knowledge base", knowledgeBase.size());
    }
}
