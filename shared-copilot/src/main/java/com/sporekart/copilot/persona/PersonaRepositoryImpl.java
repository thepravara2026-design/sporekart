package com.sporekart.copilot.persona;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PersonaRepositoryImpl implements PersonaRepository {

    private final ConcurrentMap<String, Persona> personas = new ConcurrentHashMap<>();

    public PersonaRepositoryImpl() {
        for (Persona persona : DefaultPersonas.all()) {
            personas.put(persona.name(), persona);
        }
    }

    @Override
    public Optional<Persona> findByName(String name) {
        return Optional.ofNullable(personas.get(name));
    }

    @Override
    public List<Persona> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(personas.values()));
    }

    @Override
    public void save(Persona persona) {
        personas.put(persona.name(), persona);
    }

    @Override
    public void deleteByName(String name) {
        personas.remove(name);
    }
}
