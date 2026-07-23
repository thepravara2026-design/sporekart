package com.sporekart.copilot.persona;

import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.domain.CopilotType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class PersonaEngineImpl implements PersonaEngine {

    private static final Logger log = LoggerFactory.getLogger(PersonaEngineImpl.class);

    private final PersonaRepository repository;
    private final Map<CopilotType, Persona> builtInPersonas;

    public PersonaEngineImpl(PersonaRepository repository) {
        this.repository = repository;
        this.builtInPersonas = new EnumMap<>(CopilotType.class);
        initializeDefaultPersonas();
    }

    private void initializeDefaultPersonas() {
        builtInPersonas.put(CopilotType.CUSTOMER, DefaultPersonas.customerPersona());
        builtInPersonas.put(CopilotType.GROWER, DefaultPersonas.growerPersona());
        builtInPersonas.put(CopilotType.TRAINER, DefaultPersonas.trainerPersona());
        builtInPersonas.put(CopilotType.ADMIN, DefaultPersonas.adminPersona());
        builtInPersonas.put(CopilotType.BUSINESS_INTELLIGENCE, DefaultPersonas.businessIntelligencePersona());
        builtInPersonas.put(CopilotType.MARKETING, DefaultPersonas.marketingPersona());
        builtInPersonas.put(CopilotType.OPERATIONS, DefaultPersonas.operationsPersona());
        builtInPersonas.put(CopilotType.EXECUTIVE, DefaultPersonas.executivePersona());
        builtInPersonas.put(CopilotType.DEVELOPER, DefaultPersonas.developerPersona());
        log.info("Initialized {} built-in personas", builtInPersonas.size());
    }

    @Override
    public Persona resolvePersona(CopilotType copilotType, UserContext userContext) {
        log.debug("Resolving persona for copilotType={}, userId={}", copilotType, userContext.userId());

        String personaName = copilotType.name().toLowerCase() + "-" + copilotType.name().toLowerCase();
        Optional<Persona> customPersona = repository.findByName(personaName);

        if (customPersona.isPresent()) {
            log.debug("Found custom persona '{}' for copilotType={}", personaName, copilotType);
            return customPersona.get();
        }

        Persona defaultPersona = getDefaultPersona(copilotType);
        log.debug("Using default persona '{}' for copilotType={}", defaultPersona.name(), copilotType);
        return defaultPersona;
    }

    @Override
    public Persona getDefaultPersona(CopilotType copilotType) {
        Persona persona = builtInPersonas.get(copilotType);
        if (persona == null) {
            log.warn("No default persona for copilotType={}, falling back to customer persona", copilotType);
            return builtInPersonas.get(CopilotType.CUSTOMER);
        }
        return persona;
    }
}
