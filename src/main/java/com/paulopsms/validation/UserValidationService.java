package com.paulopsms.validation;

import com.paulopsms.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserValidationService {

    public void validateUser(User user) {
        this.createUserNameValidator()
                .validate(user)
                .isValidOrElseThrow();
    }

    private Validator<User> createUserNameValidator() {
        return usr -> Optional.ofNullable(usr)
                .map(User::getName)
                .filter(name -> !name.isBlank())
                .map(name -> new ValidationResult(true, ""))
                .orElseGet(() -> new ValidationResult(false, "O nome do usuário é obrigatório."));
    }
}
