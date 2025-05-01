package com.paulopsms.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "AppUser")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
        validaNome();
    }

    private void validaNome() {
        if (this.name.isBlank()) throw new RuntimeException("O nome do usuário é obrigatório.");
    }
}
