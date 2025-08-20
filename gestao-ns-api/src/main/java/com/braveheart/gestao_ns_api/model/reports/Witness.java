package com.braveheart.gestao_ns_api.model.reports;

import com.braveheart.gestao_ns_api.model.AbstractIDModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true, exclude = "report")
@ToString(exclude = "report")
@Entity
@Table(name = "witnesses")
public class Witness extends AbstractIDModel {

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "nationality", length = 50)
    private String nationality;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "signature", columnDefinition = "TEXT")
    private String signature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private RescueReport report;
}

