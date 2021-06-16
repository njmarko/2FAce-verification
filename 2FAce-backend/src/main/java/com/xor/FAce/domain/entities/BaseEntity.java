package com.xor.FAce.domain.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@MappedSuperclass
@Getter
@Setter
@Where(clause = "active = true")
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public BaseEntity() {
        this.setActive(true);
    }
}
