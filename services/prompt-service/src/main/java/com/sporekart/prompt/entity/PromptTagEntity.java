package com.sporekart.prompt.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "prompt_tags")
public class PromptTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    public PromptTagEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
