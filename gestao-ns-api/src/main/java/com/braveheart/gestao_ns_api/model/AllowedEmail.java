package com.braveheart.gestao_ns_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "allowed_emails")
public class AllowedEmail extends AbstractIDModel {

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "is_registered")
    private boolean isRegistered = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by_user_id")
    private User invitedByUser;
}
