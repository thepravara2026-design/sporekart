package com.sporekart.ai.configregistry.infrastructure.persistence;

import com.sporekart.ai.configregistry.domain.ConfigType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, String> {

    java.util.Optional<ConfigurationEntity> findByConfigKey(String configKey);

    List<ConfigurationEntity> findByConfigType(ConfigType configType);

    List<ConfigurationEntity> findByModule(String module);

    List<ConfigurationEntity> findByValidTrue();
}
