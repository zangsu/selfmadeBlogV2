package zangsu.selfmadeBlog.user.controller;

import java.net.http.HttpRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import zangsu.selfmadeBlog.exception.SmbException;
import zangsu.selfmadeBlog.user.controller.model.LoginDTO;
import zangsu.selfmadeBlog.user.controller.model.UserNameDTO;
import zangsu.selfmadeBlog.user.repository.model.DBUser;
import zangsu.selfmadeBlog.user.service.LoginService;
import zangsu.selfmadeBlog.user.service.UserService;
import zangsu.selfmadeBlog.user.service.model.ServiceUser;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private static final String SESSION_KEY = ControllerConst.SESSION_KEY;

    @Autowired
    private final LoginService loginService;
    @Autowired
    private final UserService userService;

    @GetMapping("/")
    public String loginForm(@ModelAttribute(name = "loginDTO") LoginDTO loginDTO,
                            @ModelAttribute(name = "userNameDTO") UserNameDTO nameDTO,
                            HttpServletRequest request) {

        HttpSession loginSession = request.getSession(false);
        if (loginSession != null && loginSession.getAttribute(SESSION_KEY) != null) {
            ServiceUser loginUser = (ServiceUser) loginSession.getAttribute(SESSION_KEY);
            nameDTO.setName(loginUser.getUserName());
            return "login/loginHome";
        }
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO loginDTO,
                        BindingResult bindingResult,
                        @ModelAttribute(name = "userNameDTO") UserNameDTO nameDTO,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {
        try {
            long loginIndex = loginService.login(loginDTO.getLonginId(), loginDTO.getPassword());
            ServiceUser result = userService.findUser(loginIndex);
            nameDTO.setName(result.getUserName());
            HttpSession session = request.getSession();
            session.setAttribute(SESSION_KEY, result);
            return "redirect:" + redirectURL;
        } catch (SmbException e){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 잘못되었습니다");
            return "login/loginForm";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request,
                         @RequestParam(defaultValue = "/login/") String redirectURL){
        HttpSession session = request.getSession(false);
        if(session == null){
            //todo 세션이 null인 경우 접근 불가능??
            throw new IllegalStateException();
        }
        session.invalidate();
        return "redirect:" + redirectURL;
    }
}
