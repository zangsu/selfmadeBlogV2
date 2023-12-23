package zangsu.selfmadeBlog.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import zangsu.selfmadeBlog.exception.SmbException;
import zangsu.selfmadeBlog.user.controller.model.LoginDTO;
import zangsu.selfmadeBlog.user.repository.model.DBUser;
import zangsu.selfmadeBlog.user.service.LoginService;
import zangsu.selfmadeBlog.user.service.UserService;
import zangsu.selfmadeBlog.user.service.model.ServiceUser;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    @Autowired
    private final LoginService loginService;
    @Autowired
    private final UserService userService;

    @GetMapping("/")
    public String loginForm(@ModelAttribute(name = "loginDTO") LoginDTO loginDTO) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO loginDTO,
                        BindingResult bindingResult,
                        @ModelAttribute(name = "serviceUser") ServiceUser user) {

        try {
            long loginIndex = loginService.login(loginDTO.getLonginId(), loginDTO.getPassword());
            ServiceUser result = userService.findUser(loginIndex);
            user.setUserName(result.getUserName());

        } catch (SmbException e){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 잘못되었습니다");
            return "login/loginForm";
        }
        return "login/loginHome";
    }
}
