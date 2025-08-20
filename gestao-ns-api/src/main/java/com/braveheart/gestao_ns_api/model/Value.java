package com.braveheart.gestao_ns_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "value")
public class Value extends AbstractIDModel{

    @Column(name = "lifeguard_rate", precision = 10, scale = 2)
    private BigDecimal lifeguardRate;

    @Column(name = "coordinator_rate", precision = 10, scale = 2)
    private BigDecimal coordinatorRate;

}
