package com.braveheart.gestao_ns_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "address")
public class Address extends AbstractIDModel {

    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @Column(name = "number")
    private String number;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "zip_code", length = 10)
    private String zipCode;
}
