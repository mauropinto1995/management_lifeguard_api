package com.braveheart.gestao_ns_api.model.reports;

import com.braveheart.gestao_ns_api.model.AbstractIDModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "equipment")
public class Equipment extends AbstractIDModel {

    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;
}
