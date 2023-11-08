package zangsu.selfmadeBlog.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import zangsu.selfmadeBlog.exception.smbexception.CantAccessException;
import zangsu.selfmadeBlog.user.controller.model.WebUser;
import zangsu.selfmadeBlog.user.controller.model.WebUserMapper;
import zangsu.selfmadeBlog.user.controller.validator.WebUserValidator;
import zangsu.selfmadeBlog.exception.smbexception.CantModifyFieldException;
import zangsu.selfmadeBlog.exception.smbexception.DuplicatedUserIdException;
import zangsu.selfmadeBlog.exception.smbexception.NoSuchUserException;
import zangsu.selfmadeBlog.user.service.UserService;
import zangsu.selfmadeBlog.user.service.model.ServiceUser;


@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    final static String userViewPath = "user";
    @Autowired
    private UserService userService;

    //@Autowired
    private WebUserValidator validator = new WebUserValidator();

    @InitBinder("webUser")
    public void initValidator(DataBinder dataBinder){
        dataBinder.addValidators(validator);
    }

    @ModelAttribute("webUser")
    public WebUser addUserForm(){
        return new WebUser();
    }

    //유저 홈으로 이동
    @GetMapping
    public String userHome() {
        return userViewPath + "/home";
    }

    //회원 가입 폼으로 이동
    @GetMapping("/join")
    public String joinForm(@ModelAttribute WebUser webUser) {

        return userViewPath + "/join";
    }

    //회원 가입 후 회원 정보 페이지로
    @PostMapping("/join")
    public String saveUser(@Validated @ModelAttribute WebUser user, BindingResult bindingResult, HttpServletRequest request) {

        if(bindingResult.hasErrors()){
            return userViewPath + "/join";
        }

        try {
            long savedId = userService.saveUser(WebUserMapper.getServiceUser(user));
            HttpSession session = request.getSession();
            session.setAttribute("userIdx", savedId);
            return "redirect:/user/" + savedId;
        } catch (DuplicatedUserIdException e) {
            bindingResult.rejectValue("userId", "Duplicate", "");
            return userViewPath + "/join";
        }
    }

    //회원 조회시 회원 정보 페이지로
    @GetMapping("{userIdx}")
    public String findUser(@SessionAttribute(name = "userIdx", required = false) Long sessionIdx, @PathVariable long userIdx, Model model) {
        if(sessionIdx == null || sessionIdx != userIdx){
            new CantAccessException().addWarnings(model);
            return "user/home";
        }
        return userInfo(userIdx, model);
    }

    private String userInfo(long userIdx, Model model) {
        try {
            ServiceUser findUser = userService.findUser(userIdx);
            model.addAttribute("webUser", WebUserMapper.getWebUser(findUser));
            model.addAttribute("index", userIdx);
            return userViewPath + "/user";
        }catch (NoSuchUserException e){
            e.addWarnings(model);
            return userViewPath + "/home";
        }
    }

    //회원 수정 이후 회원 정보 페이지로
    @PostMapping("{userIdx}")
    public String modifyUser(@SessionAttribute(name = "userIdx", required = false) Long sessionIdx,
                             @PathVariable long userIdx,
                             @ModelAttribute WebUser modifiedUser,
                             Model model) {
        if(sessionIdx == null || sessionIdx != userIdx){
            new CantAccessException().addWarnings(model);
            return "user/home";
        }

        try {
            userService.modify(userIdx, WebUserMapper.getServiceUser(modifiedUser));
            return userInfo(userIdx, model);
        } catch (NoSuchUserException e) {
            e.addWarnings(model);
            return userViewPath + "/home";
        } catch (CantModifyFieldException e) {
            e.addWarnings(model);
            return userInfo(userIdx, model);
        }
    }

    //회원 탈퇴 이후 탈퇴 성공 페이지로
    @DeleteMapping("{userIdx}")
    public String deleteUser(@SessionAttribute(name = "userIdx", required = false) Long sessionIdx,
                             @PathVariable int userIdx, Model model) {
        if(sessionIdx == null || sessionIdx != userIdx){
            new CantAccessException().addWarnings(model);
            return "user/home";
        }

        try {
            userService.delete(userIdx);
            return userViewPath + "/delete";
        } catch (NoSuchUserException e) {
            e.addWarnings(model);
            return userViewPath + "/home";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }

        return "user/home";
    }
}
