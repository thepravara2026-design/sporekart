package com.sporekart.copilot.persona;

import java.util.List;
import java.util.Optional;

public interface PersonaRepository {

    Optional<Persona> findByName(String name);

    List<Persona> findAll();

    void save(Persona persona);

    void deleteByName(String name);
}
