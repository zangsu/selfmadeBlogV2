package zangsu.selfmadeBlog.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zangsu.selfmadeBlog.user.controller.model.WebUser;
import zangsu.selfmadeBlog.user.controller.model.WebUserMapper;
import zangsu.selfmadeBlog.user.exception.CantModifyFieldException;
import zangsu.selfmadeBlog.user.exception.DuplicatedUserIdException;
import zangsu.selfmadeBlog.user.exception.NoSuchUserException;
import zangsu.selfmadeBlog.user.service.UserService;
import zangsu.selfmadeBlog.user.service.model.ServiceUser;

import static zangsu.selfmadeBlog.model.web.WarningFactory.addWarnings;

@Controller
@RequestMapping("/user")
public class UserController {

    final static String userViewPath = "user";


    @Autowired
    private UserService userService;

    //유저 홈으로 이동
    @GetMapping

    public String userHome() {
        return userViewPath + "/home";
    }

    //회원 가입 폼으로 이동
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("userClass", new WebUser());
        return userViewPath + "/join";
    }

    //회원 가입 후 회원 정보 페이지로
    @PostMapping("/join")
    public String saveUser(@ModelAttribute WebUser user, Model model) {
        try {
            long savedId = userService.saveUser(WebUserMapper.getServiceUser(user));
            return "redirect:/user/" + savedId;
        } catch (DuplicatedUserIdException e) {
            model.addAttribute("userClass", new WebUser());
            addWarnings(model, e);
            return userViewPath + "/join";
        }
    }

    //회원 조회시 회원 정보 페이지로
    @GetMapping("{userIdx}")
    public String findUser(@PathVariable long userIdx, Model model) {
        try {
            return userInfo(userIdx, model);
        } catch (NoSuchUserException e) {
            addWarnings(model, e);
            return userViewPath + "/home";
        }
    }

    private String userInfo(long userIdx, Model model) throws NoSuchUserException {
        ServiceUser findUser = userService.findUser(userIdx);
        model.addAttribute("user", WebUserMapper.getWebUser(findUser));
        model.addAttribute("index", userIdx);
        return userViewPath + "/user";
    }

    //회원 수정 이후 회원 정보 페이지로
    @PostMapping("{userIdx}")
    public String modifyUser(@PathVariable long userIdx,
                             @ModelAttribute WebUser modifiedUser,
                             Model model) {
        try {
            userService.modify(userIdx, WebUserMapper.getServiceUser(modifiedUser));
            return userInfo(userIdx, model);
        } catch (NoSuchUserException e) {
            addWarnings(model, e);
            return userViewPath + "/home";
        } catch (CantModifyFieldException e) {
            addWarnings(model, e);
            return findUser(userIdx, model);
        }
    }

    //회원 탈퇴 이후 탈퇴 성공 페이지로
    @DeleteMapping("{userIdx}")
    public String deleteUser(@PathVariable int userIdx, Model model) {
        try {
            userService.delete(userIdx);
            return userViewPath + "/delete";
        } catch (NoSuchUserException e) {
            addWarnings(model, e);
            return userViewPath + "/home";
        }
    }
}
