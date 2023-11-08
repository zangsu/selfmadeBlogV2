package zangsu.selfmadeBlog.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import zangsu.selfmadeBlog.user.controller.model.WebUser;
import zangsu.selfmadeBlog.user.controller.validator.WebUserValidator;
import zangsu.selfmadeBlog.user.service.UserService;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class UserControllerWebTest {

    public static final String BINDING_RESULT_KEY = "org.springframework.validation.BindingResult.webUser";
    @Autowired
    MockMvc mvc;
    @MockBean
    UserService userService;

    @Autowired
    UserController userController;

    @Test
    public void 빈값은_허용하지_않음() throws Exception{
        //given
        WebUser blankUser = new WebUser("", "", "");
        makeRequest(blankUser)
                .andExpect(model().attributeHasFieldErrorCode("webUser", "userId", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("webUser", "userName", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("webUser", "password", "NotBlank"));
        //when

        //then
    }
    @Test
    public void 공백은_허용하지_않음() throws Exception{
        //given
        WebUser blankUser = new WebUser(" ", " ", " ");
        makeRequest(blankUser)
                .andExpect(model().attributeHasFieldErrorCode("webUser", "userId", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("webUser", "userName", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("webUser", "password", "NotBlank"));
        //when

        //then
    }

    @Test
    public void id는_숫자로_시작하면_안됨() throws Exception{
        WebUser user = new WebUser("", "123User", "");
        makeRequest(user)
                .andExpect(model().attributeHasFieldErrorCode("webUser", "userId", "Pattern"));
    }


    @Test
    public void id는_최소_5글자() throws Exception{
        //given

        WebUser user = new WebUser("","aaaa","");
        makeRequest(user)
                .andExpect(model().attributeHasFieldErrorCode("webUser", "userId", "Pattern"));
    }

    @Test
    public void id는_최대_10글자() throws Exception{
        WebUser user = new WebUser("","aaaaaaaaaaa","");
        makeRequest(user)
                .andExpect(model().attributeHasFieldErrorCode("webUser", "userId", "Pattern"));

    }

    @Test
    public void id에_특수문자_금지() throws Exception{
        //given
        WebUser user = new WebUser("","aaaa!","");
        makeRequest(user)
                .andExpect(model().attributeHasFieldErrorCode("webUser", "userId", "Pattern"));

    }

    @Test
    public void 비밀번호는_특수문자_필수() throws Exception{
        WebUser user = new WebUser("","aaaaaaaaaaa","password12");
        makeRequest(user)
                .andExpect(model().attributeHasFieldErrorCode("webUser", "password", "Pattern"));

    }

    @Test
    public void 비밀번호는_최소_8글자() throws Exception{
        WebUser user = new WebUser("","aaaaaaaaaaa","pass12!");
        makeRequest(user)
                .andExpect(model().attributeHasFieldErrorCode("webUser", "password", "Pattern"));

    }

    @Test
    public void 비밀번호는_최대_20글자() throws Exception{
        WebUser user = new WebUser("","aaaaaaaaaaa","password12345678pass!");
        makeRequest(user)
                .andExpect(model().attributeHasFieldErrorCode("webUser", "password", "Pattern"));

    }

    @Test
    public void 올바른_입력() throws Exception{

        WebUser user = new WebUser("유저","User12","password12!");
        makeRequest(user)
                .andExpect(model().hasNoErrors());
    }

    private ResultActions makeRequest(WebUser webUser) throws Exception {
        return mvc.perform(post("/user/join")
                        .param("userName", webUser.getUserName())
                        .param("userId", webUser.getUserId())
                        .param("password", webUser.getPassword()));
    }
}