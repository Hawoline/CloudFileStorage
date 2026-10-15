package ru.hawoline.cloudfilestorage.data;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.hawoline.cloudfilestorage.data.entity.SignInRequest;
import ru.hawoline.cloudfilestorage.data.entity.SignUpRequest;
import ru.hawoline.cloudfilestorage.data.entity.UserResponse;
import ru.hawoline.cloudfilestorage.util.UserEntityValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Neil Alishev
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final RegistrationService registrationService;
	private final UserEntityValidator personValidator;
	private final SecurityContextRepository securityContextRepository;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, RegistrationService registrationService, UserEntityValidator personValidator, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.registrationService = registrationService;
		this.personValidator = personValidator;
        this.securityContextRepository = securityContextRepository;
    }

	@GetMapping("/csrf")
	public Map<String, String> getCsrfToken(HttpServletRequest request) {
		// Извлекаем CSRF-токен, который Spring Security привязал к текущему запросу
		CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

		Map<String, String> response = new HashMap<>();
		if (token != null) {
			response.put("parameterName", token.getParameterName()); // Обычно "_csrf"
			response.put("headerName", token.getHeaderName());       // Обычно "X-XSRF-TOKEN"
			response.put("token", token.getToken());                 // Сам случайный токен
		}
		return response;
	}

	@PostMapping("/login")
	public @ResponseBody UserResponse login(@RequestBody SignInRequest loginRequest,
											HttpServletRequest request,
											HttpServletResponse response) {
		Authentication authenticationRequest =
				UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
		Authentication authenticationResponse =
				this.authenticationManager.authenticate(authenticationRequest);
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authenticationResponse);
		SecurityContextHolder.setContext(context);

		// 4. ОБЯЗАТЕЛЬНО ДЛЯ SPRING SECURITY 6+: Сохраняем контекст в репозиторий сессий
		this.securityContextRepository.saveContext(context, request, response);

		return new UserResponse(loginRequest.getUsername());
	}

	@GetMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest servletRequest) {
        try {
            servletRequest.logout();
			return ResponseEntity.ok().build();
		} catch (ServletException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	@PostMapping("/sign-up")
	public @ResponseBody UserResponse performRegistration(@RequestBody SignUpRequest signUpRequest,
									  BindingResult bindingResult) {
		UserEntity userEntity = new UserEntity(signUpRequest.getUsername(), signUpRequest.getPassword(), "ROLE_USER");
		personValidator.validate(userEntity, bindingResult);

		if (bindingResult.hasErrors())
			throw new IllegalArgumentException();

		registrationService.register(userEntity);

		return new UserResponse(userEntity.getName());
	}
}