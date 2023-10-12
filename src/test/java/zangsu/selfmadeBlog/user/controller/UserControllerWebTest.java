package zangsu.selfmadeBlog.user.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import zangsu.selfmadeBlog.user.service.UserService;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
class UserControllerWebTest {

    public static final String BINDING_RESULT_KEY = "org.springframework.validation.BindingResult.webUser";
    @Autowired
    MockMvc mvc;
    @MockBean
    UserService userService;

    @Test
    public void id는_숫자로_시작하면_안됨() throws Exception{
        //given
        MvcResult result = mvc.perform(post("/user/join")
                        .param("userName", "")
                        .param("userId", "123User")
                        .param("password", ""))
                //.andDo(print())
                .andReturn();

        //when
        ModelMap modelMap = result.getModelAndView().getModelMap();
        BindingResult bindingResult = (BindingResult)modelMap.get(BINDING_RESULT_KEY);
        FieldError id = bindingResult.getFieldError("userId");

        //then
        assertThat(id).isNotNull();
        assertThat(id.getCodes()).contains("Pattern.webUser.userId");
    }

    @Test
    public void id는_최소_5글자() throws Exception{
        //given
        MvcResult result = mvc.perform(post("/user/join")
                        .param("userName", "")
                        .param("userId", "aaaa")
                        .param("password", ""))
                //.andDo(print())
                .andReturn();

        //when
        ModelMap modelMap = result.getModelAndView().getModelMap();
        BindingResult bindingResult = (BindingResult)modelMap.get(BINDING_RESULT_KEY);
        FieldError id = bindingResult.getFieldError("userId");

        //then
        assertThat(id).isNotNull();
        assertThat(id.getCodes()).contains("Pattern.webUser.userId");
    }

    @Test
    public void id는_최대_10글자() throws Exception{
        //given
        MvcResult result = mvc.perform(post("/user/join")
                        .param("userName", "")
                        .param("userId", "aaaaaaaaaaa")
                        .param("password", ""))
                //.andDo(print())
                .andReturn();

        //when
        ModelMap modelMap = result.getModelAndView().getModelMap();
        BindingResult bindingResult = (BindingResult)modelMap.get(BINDING_RESULT_KEY);
        FieldError id = bindingResult.getFieldError("userId");

        //then
        assertThat(id).isNotNull();
        assertThat(id.getCodes()).contains("Pattern.webUser.userId");
    }

    @Test
    public void id에_특수문자_금지() throws Exception{
        //given
        MvcResult result = mvc.perform(post("/user/join")
                        .param("userName", "")
                        .param("userId", "aaaa!")
                        .param("password", ""))
                //.andDo(print())
                .andReturn();

        //when
        ModelMap modelMap = result.getModelAndView().getModelMap();
        BindingResult bindingResult = (BindingResult)modelMap.get(BINDING_RESULT_KEY);
        FieldError id = bindingResult.getFieldError("userId");

        //then
        assertThat(id).isNotNull();
        assertThat(id.getCodes()).contains("Pattern.webUser.userId");
    }

    @Test
    public void 비밀번호는_특수문자_필수() throws Exception{
        //given
        MvcResult result = mvc.perform(post("/user/join")
                        .param("userName", "")
                        .param("userId", "")
                        .param("password", "password12"))
                //.andDo(print())
                .andReturn();

        //when
        ModelMap modelMap = result.getModelAndView().getModelMap();
        BindingResult bindingResult = (BindingResult)modelMap.get(BINDING_RESULT_KEY);
        FieldError password = bindingResult.getFieldError("password");

        //then
        assertThat(password).isNotNull();
        assertThat(password.getCodes()).contains("Pattern.webUser.password");
    }

    @Test
    public void 비밀번호는_최소_8글자() throws Exception{
        //given
        MvcResult result = mvc.perform(post("/user/join")
                        .param("userName", "")
                        .param("userId", "")
                        .param("password", "pass12!"))
                //.andDo(print())
                .andReturn();

        //when
        ModelMap modelMap = result.getModelAndView().getModelMap();
        BindingResult bindingResult = (BindingResult)modelMap.get(BINDING_RESULT_KEY);
        FieldError password = bindingResult.getFieldError("password");

        //then
        assertThat(password).isNotNull();
        assertThat(password.getCodes()).contains("Pattern.webUser.password");
    }

    @Test
    public void 비밀번호는_최대_20글자() throws Exception{
        //given
        MvcResult result = mvc.perform(post("/user/join")
                        .param("userName", "")
                        .param("userId", "")
                        .param("password", "password12345678pass!"))
                //.andDo(print())
                .andReturn();

        //when
        ModelMap modelMap = result.getModelAndView().getModelMap();
        BindingResult bindingResult = (BindingResult)modelMap.get(BINDING_RESULT_KEY);
        FieldError password = bindingResult.getFieldError("password");

        //then
        assertThat(password).isNotNull();
        assertThat(password.getCodes()).contains("Pattern.webUser.password");
    }

    @Test
    public void 올바른_입력() throws Exception{
        //given
        MvcResult result = mvc.perform(post("/user/join")
                        .param("userName", "유저")
                        .param("userId", "User12")
                        .param("password", "password12!"))
                //.andDo(print())
                .andReturn();

        //when
        ModelMap modelMap = result.getModelAndView().getModelMap();
        BindingResult bindingResult = (BindingResult)modelMap.get(BINDING_RESULT_KEY);


        //then
        assertThat(bindingResult).isNull();
    }
}