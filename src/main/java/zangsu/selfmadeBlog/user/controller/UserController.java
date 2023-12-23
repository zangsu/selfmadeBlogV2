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

    @Autowired
    private UserService userService;

    //@Autowired
    private final WebUserValidator validator = new WebUserValidator();

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
        return "user/home";
    }

    //회원 가입 폼으로 이동
    @GetMapping("/join")
    public String joinForm(@ModelAttribute WebUser webUser) {
        return "user/join";
    }

    //회원 가입 후 회원 정보 페이지로
    @PostMapping("/join")
    public String saveUser(@Validated @ModelAttribute WebUser user,
                           BindingResult bindingResult,
                           HttpServletRequest request) {

        if(bindingResult.hasErrors()){
            return "user/join";
        }

        try {
            long savedId = userService.saveUser(WebUserMapper.getServiceUser(user));
            HttpSession session = request.getSession();
            session.setAttribute("userIdx", savedId);
            return "redirect:/user/" + savedId;
        } catch (DuplicatedUserIdException e) {
            bindingResult.rejectValue("userId", "Duplicate", "");
            return "user/join";
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
            return "user/user";
        }catch (NoSuchUserException e){
            e.addWarnings(model);
            return "user/home";
        }
    }

    //회원 수정 이후 회원 정보 페이지로
    @PostMapping("{userIdx}")
    public String modifyUser(@SessionAttribute(name = "userIdx", required = false)
                                 Long sessionIdx,
                             @PathVariable long userIdx,
                             @Validated @ModelAttribute WebUser modifiedUser,
                             BindingResult bindingResult,
                             Model model) {

        if(bindingResult.hasErrors()){
            //TODO 에러 메시지 추가해야됨
            bindingResult.getAllErrors()
                    .forEach(System.out::println);
            return "redirect:/user/" + userIdx;
        }
        if(sessionIdx == null || sessionIdx != userIdx){
            new CantAccessException().addWarnings(model);
            return "user/home";
        }

        try {
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
    public String deleteUser(@SessionAttribute(name = "userIdx", required = false) Long sessionIdx,
                             @PathVariable int userIdx, Model model) {
        if(sessionIdx == null || sessionIdx != userIdx){
            new CantAccessException().addWarnings(model);
            return "user/home";
        }

        try {
            userService.delete(userIdx);
            return "user/delete";
        } catch (NoSuchUserException e) {
            e.addWarnings(model);
            return "user/home";
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
