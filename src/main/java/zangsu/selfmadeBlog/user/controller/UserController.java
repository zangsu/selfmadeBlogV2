package zangsu.selfmadeBlog.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import zangsu.selfmadeBlog.exception.smbexception.CantAccessException;
import zangsu.selfmadeBlog.exception.smbexception.CantModifyFieldException;
import zangsu.selfmadeBlog.exception.smbexception.DuplicatedUserIdException;
import zangsu.selfmadeBlog.exception.smbexception.NoSuchUserException;
import zangsu.selfmadeBlog.user.controller.model.UserNameDTO;
import zangsu.selfmadeBlog.user.controller.model.WebUser;
import zangsu.selfmadeBlog.user.controller.model.WebUserMapper;
import zangsu.selfmadeBlog.user.controller.validator.WebUserValidator;
import zangsu.selfmadeBlog.user.service.UserService;
import zangsu.selfmadeBlog.user.service.model.ServiceUser;


@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private static final String SESSION_KEY = ControllerConst.SESSION_KEY;
    //@Autowired
    private final WebUserValidator validator = new WebUserValidator();

    @Autowired
    private UserService userService;

    @InitBinder("webUser")
    public void initValidator(DataBinder dataBinder) {
        dataBinder.addValidators(validator);
    }

    @ModelAttribute("webUser")
    public WebUser addUserForm() {
        return new WebUser();
    }

    //유저 홈으로 이동
    @GetMapping
    public String userHome() {
        return "user/home";
    }

    //회원 가입 폼으로 이동
    @GetMapping("/join")
    public String joinForm(@SessionAttribute(name = SESSION_KEY, required = false) WebUser loginUser,
                           @ModelAttribute UserNameDTO userNameDTO,
                           @ModelAttribute WebUser webUser) {

        if (loginUser != null) {
            userNameDTO.setName(loginUser.getUserName());
            return "login/loginHome";
        }
        return "user/join";
    }

    //회원 가입 후 회원 정보 페이지로
    @PostMapping("/join")
    public String saveUser(@Validated @ModelAttribute WebUser user,
                           BindingResult bindingResult,
                           HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "user/join";
        }

        try {
            long savedId = userService.saveUser(WebUserMapper.getServiceUser(user));
            ServiceUser savedUser = userService.findUser(savedId);
            request.getSession().setAttribute(SESSION_KEY, savedUser);
            return "redirect:/user/" + savedId;
        } catch (DuplicatedUserIdException e) {
            bindingResult.rejectValue("userId", "Duplicate", "");
            return "user/join";
        }
    }

    //회원 조회시 회원 정보 페이지로
    @GetMapping("{userIdx}")
    public String findUser(
            @SessionAttribute(name = SESSION_KEY, required = false)
            ServiceUser loginUser,
            @PathVariable long userIdx, Model model) {

        try {
            ServiceUser findUser = userService.findUser(userIdx);
            if (findUser.getId().equals(loginUser.getId())) {
                model.addAttribute("webUser", WebUserMapper.getWebUser(findUser));
                model.addAttribute("index", userIdx);
                return "user/user";
            }
            new CantAccessException().addWarnings(model);
            return "user/home";
        } catch (NoSuchUserException e) {
            e.addWarnings(model);
            return "user/home";
        }
    }

    //todo 바로 윗 메서드처럼 변경해야 할 수도.
    private String userInfo(long userIdx, Model model) {
        try {
            ServiceUser findUser = userService.findUser(userIdx);
            model.addAttribute("webUser", WebUserMapper.getWebUser(findUser));
            model.addAttribute("index", userIdx);
            return "user/user";
        } catch (NoSuchUserException e) {
            e.addWarnings(model);
            return "user/home";
        }
    }

    //회원 수정 이후 회원 정보 페이지로
    @PostMapping("{userIdx}")
    public String modifyUser(
            @SessionAttribute(name = SESSION_KEY, required = false) ServiceUser loginUser,
            @PathVariable long userIdx,
            @Validated @ModelAttribute WebUser modifiedUser,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            //TODO 에러 메시지 추가해야됨
            bindingResult.getAllErrors()
                    .forEach(System.out::println);
            return "redirect:/user/" + userIdx;
        }

        try {
            if (!isIdxForServiceUser(loginUser, userIdx)) {
                new CantAccessException().addWarnings(model);
                return "user/home";
            }
            userService.modify(userIdx, WebUserMapper.getServiceUser(modifiedUser));
            return userInfo(userIdx, model);
        } catch (NoSuchUserException e) {
            e.addWarnings(model);
            return "user/home";
        } catch (CantModifyFieldException e) {
            e.addWarnings(model);
            return userInfo(userIdx, model);
        }
    }

    //회원 탈퇴 이후 탈퇴 성공 페이지로
    @DeleteMapping("{userIdx}")
    public String deleteUser(
            @SessionAttribute(name = SESSION_KEY, required = false) ServiceUser loginUser,
            @PathVariable int userIdx, Model model) {

        try {
            if (!isIdxForServiceUser(loginUser, userIdx)) {
                new CantAccessException().addWarnings(model);
                return "user/home";
            }
            userService.delete(userIdx);
            return "user/delete";
        } catch (NoSuchUserException e) {
            e.addWarnings(model);
            return "user/home";
        }
    }

    private boolean isIdxForServiceUser(ServiceUser loginUser, long accessIdx) {
        ServiceUser findUser = userService.findUser(accessIdx);
        return findUser.getId().equals(loginUser.getId());
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "user/home";
    }
}
