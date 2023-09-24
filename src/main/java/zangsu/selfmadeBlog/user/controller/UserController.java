package zangsu.selfmadeBlog.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zangsu.selfmadeBlog.model.web.Warning;
import zangsu.selfmadeBlog.user.controller.model.WebUser;
import zangsu.selfmadeBlog.user.controller.model.WebUserMapper;
import zangsu.selfmadeBlog.user.exception.DuplicatedUserIdException;
import zangsu.selfmadeBlog.user.exception.NoSuchUserException;
import zangsu.selfmadeBlog.user.service.UserService;
import zangsu.selfmadeBlog.user.service.model.ServiceUser;

@Controller
@RequestMapping("/user")
public class UserController {

    final static String userViewPath = "user";


    @Autowired
    private UserService userService;

  /*  private static WebUser webUser = new WebUser();

    @ModelAttribute("userClass")
    public WebUser webUser(){
        return webUser;
    }*/

    //유저 홈으로 이동
    @GetMapping
    public String userHome(){
        return userViewPath + "/home";
    }

    //회원 가입 폼으로 이동
    @GetMapping("/join")
    public String joinForm(Model model){
        model.addAttribute("userClass", new WebUser());
        return userViewPath + "/join";
    }

    //회원 가입 후 회원 정보 페이지로
    @PostMapping("/join")
    public String saveUser(@ModelAttribute WebUser user, Model model){
        try {
            long savedId = userService.saveUser(WebUserMapper.getServiceUser(user));
            return "redirect:/user/" + savedId;
        } catch (DuplicatedUserIdException e) {
            model.addAttribute("userClass", new WebUser());
            model.addAttribute("warnings", new Warning("중복된 회원 ID 입니다."));
            return userViewPath + "/join";
        }
    }

    //회원 조회시 회원 정보 페이지로
    @GetMapping("{userIdx}")
    public String findUser(@PathVariable long userIdx, Model model){
        try {
            ServiceUser findUser = userService.findUser(userIdx);
            model.addAttribute("user", findUser);
            return userViewPath + "/user";
        } catch (NoSuchUserException e) {
            model.addAttribute("warnings", new Warning("해당 회원이 존재하지 않습니다"));
            return userViewPath + "/home";
        }
    }

    //회원 수정 이후 회원 정보 페이지로
    @PutMapping("{userIdx}")
    public String modifyUser(Model model){
        return userViewPath + "/user";
    }

    //회원 탈퇴 이후 탈퇴 성공 페이지로
    @DeleteMapping("{userIdx}")
    public String deleteUser(Model model) {
        return userViewPath + "/delete";
    }

}
