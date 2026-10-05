package ru.hawoline.cloudfilestorage.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	@GetMapping("/hello")
	public String login() {
		return "haw";
	}

}