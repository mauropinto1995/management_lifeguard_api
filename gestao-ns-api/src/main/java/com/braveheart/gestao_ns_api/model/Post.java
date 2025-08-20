package com.braveheart.gestao_ns_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true, exclude = "beach") // Excluir 'beach' para evitar loop infinito no toString/hashCode
@Entity
@Table(name = "post")
public class Post extends AbstractIDModel{

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beach_id", nullable = false) //
    private Beach beach;
}
