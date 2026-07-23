package com.sporekart.marketplace.loader;

import com.sporekart.marketplace.registry.CapabilityRegistry;
import com.sporekart.marketplace.sdk.PluginCapability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
public class KnowledgePackIndexer {

    private static final Logger log = LoggerFactory.getLogger(KnowledgePackIndexer.class);

    private final CapabilityRegistry capabilityRegistry;
    private final Map<String, KnowledgePack> indexedPacks = new HashMap<>();

    public KnowledgePackIndexer(CapabilityRegistry capabilityRegistry) {
        this.capabilityRegistry = capabilityRegistry;
    }

    public void indexPack(String pluginId, String pluginName, KnowledgePack pack) {
        indexedPacks.put(pack.packId(), pack);
        capabilityRegistry.register(pluginId, pluginName, PluginCapability.KNOWLEDGE);
        log.info("Knowledge pack indexed: {} by plugin {}", pack.packId(), pluginId);
    }

    public Optional<KnowledgePack> getPack(String packId) {
        return Optional.ofNullable(indexedPacks.get(packId));
    }

    public List<KnowledgePack> getPacksByPlugin(String pluginId) {
        return indexedPacks.values().stream()
            .filter(p -> p.pluginId().equals(pluginId))
            .toList();
    }

    public List<KnowledgePack> getAllPacks() {
        return List.copyOf(indexedPacks.values());
    }

    public void removePluginPacks(String pluginId) {
        indexedPacks.values().removeIf(p -> p.pluginId().equals(pluginId));
        log.info("Knowledge packs removed for plugin {}", pluginId);
    }

    public int count() { return indexedPacks.size(); }

    public record KnowledgePack(
        String packId,
        String pluginId,
        String name,
        String description,
        KnowledgePackType type,
        List<String> documents,
        Instant indexedAt
    ) {
        public enum KnowledgePackType {
            FAQ, SOP, DOMAIN_DOCS, TRAINING_MATERIAL, REFERENCE
        }
    }
}
