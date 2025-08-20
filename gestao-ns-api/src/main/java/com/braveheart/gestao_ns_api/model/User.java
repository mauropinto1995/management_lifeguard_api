package com.braveheart.gestao_ns_api.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name ="\"user\"")
public class User extends AbstractUUIDModel {

    @Column(name = "first_name", nullable = false, length = 50) // Increased length for safety
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50) // Increased length for safety
    private String lastName;

    @Column(nullable = false, length = 50)
    private String nationality;

    @Column(nullable = false, length = 10)
    private String gender;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "is_editor")
    private boolean isEditor;

    @Column(name = "lifeguard_number", nullable = false)
    private int lifeguardNumber;

    @Column(name = "signature_image_url", length = 255)
    private String signatureImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address; // This will now correctly refer to your Address entity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id")
    private Association association;
}
