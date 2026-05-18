package com.ResellLK.demo.model.interaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Abstract base entity for all buyer-initiated interactions.
 * Demonstrates ENCAPSULATION via private fields and Lombok getters/setters.
 * Provides POLYMORPHIC abstract methods implemented by each subclass.
 *
 * JPA Strategy: SINGLE_TABLE — all subtypes stored in one "inquiries" table
 * distinguished by the "inquiry_type" discriminator column.
 */
@Entity
@Table(name = "inquiries")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "inquiry_type", discriminatorType = DiscriminatorType.STRING)
// Jackson polymorphic serialization — lets the REST API return List<Inquiry> correctly
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = GeneralInquiry.class, name = "general"),
    @JsonSubTypes.Type(value = VisitRequest.class,   name = "visit")
})
@Getter
@Setter
@NoArgsConstructor
public abstract class Inquiry {

    // ── Encapsulation: all fields private, exposed only via Lombok getters/setters ──

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long propertyId;

    @Column(nullable = false)
    private Long buyerId;

    private String contactNumber;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // ── Polymorphism: each subclass must provide its own implementation ──

    /** Returns a human-readable summary of this interaction's details. */
    public abstract String getDetails();

    /** Returns a context-aware response message based on the subclass type and state. */
    public abstract String getResponse();
}
