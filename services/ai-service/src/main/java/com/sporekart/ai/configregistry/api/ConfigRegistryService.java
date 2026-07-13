package com.sporekart.ai.configregistry.api;

import com.sporekart.ai.configregistry.domain.ConfigurationEntry;
import com.sporekart.ai.configregistry.domain.ConfigType;
import java.util.List;

public interface ConfigRegistryService {

    ConfigurationEntry getConfig(String key);

    void setConfig(ConfigurationEntry entry);

    void deleteConfig(String key);

    List<ConfigurationEntry> listByType(ConfigType type);

    List<ConfigurationEntry> listByModule(String module);

    List<ConfigurationEntry> searchConfig(String query);
}
