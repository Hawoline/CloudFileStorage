package ru.hawoline.cloudfilestorage.data;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.hawoline.cloudfilestorage.data.entity.UserResponse;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/me")
    public @ResponseBody UserResponse me(Principal principal) {
        return new UserResponse(principal.getName());
    }
}
