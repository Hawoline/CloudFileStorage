package ru.hawoline.cloudfilestorage.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.hawoline.cloudfilestorage.data.PersonDetailsService;
import ru.hawoline.cloudfilestorage.data.UserEntity;

@Component
public class UserEntityValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public UserEntityValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserEntity person = (UserEntity) o;

        try {
            personDetailsService.loadUserByUsername(person.getName());
        } catch (UsernameNotFoundException ignored) {
            return;
        }

        errors.rejectValue("username", "", "Человек с таким именем пользователя уже существует");
    }
}
