package ru.hawoline.cloudfilestorage.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hawoline.cloudfilestorage.util.UserEntityValidator;

/**
 * @author Neil Alishev
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

	private final RegistrationService registrationService;
	private final UserEntityValidator personValidator;

	@Autowired
	public AuthController(RegistrationService registrationService, UserEntityValidator personValidator) {
		this.registrationService = registrationService;
		this.personValidator = personValidator;
	}

	@GetMapping("/login")
	public String loginPage() {
		return "auth/login";
	}

	@GetMapping("/registration")
	public String registrationPage(@ModelAttribute("person") UserEntity person) {
		return "auth/registration";
	}

	@PostMapping("/registration")
	public String performRegistration(@ModelAttribute("person") @Validated UserEntity person,
									  BindingResult bindingResult) {
		personValidator.validate(person, bindingResult);

		if (bindingResult.hasErrors())
			return "/auth/registration";

		registrationService.register(person);

		return "redirect:/auth/login";
	}
}