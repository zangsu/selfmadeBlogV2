package zangsu.selfmadeBlog.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zangsu.selfmadeBlog.user.controller.model.LoginDTO;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/")
    public String loginForm(@ModelAttribute(name = "loginDTO") LoginDTO loginDTO) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO loginDTO,
                        BindingResult bindingResult) {
        System.out.println(loginDTO.getLonginId());
        System.out.println(loginDTO.getPassword());
        return "login/loginForm";
    }
}
