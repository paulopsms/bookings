package com.paulopsms.domain.model;

import com.paulopsms.exception.UserRuntimeException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import static java.util.Objects.isNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class User implements Serializable {

    private Long id;

    private String name;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
        validaNome();
    }

    private void validaNome() {
        if (isNull(this.name) || this.name.isBlank())
            throw new UserRuntimeException("O nome do usuário é obrigatório.");
    }
}
