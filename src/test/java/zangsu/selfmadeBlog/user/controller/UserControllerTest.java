package zangsu.selfmadeBlog.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import zangsu.selfmadeBlog.exception.SmbException;
import zangsu.selfmadeBlog.exception.smbexception.CantModifyFieldException;
import zangsu.selfmadeBlog.exception.smbexception.NoSuchUserException;
import zangsu.selfmadeBlog.model.web.Warning;
import zangsu.selfmadeBlog.user.controller.model.WebUser;

import java.util.Map;
import zangsu.selfmadeBlog.user.service.model.ServiceUser;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    public static final String WEB_USER_KEY = "webUser";
    final String joinUrl = "/user/join";

    long extIdx;
    final static WebUser extUser =
            new WebUser("extUser", "extUserID", "extUserPassword1!");

    final static ServiceUser loginUser =
            new ServiceUser("extUser", "extUserID", "extUserPassword1!");
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void existingUser() throws Exception {
        MvcResult result = joinUserRequest(extUser)
                .andReturn();

        extIdx = Integer.parseInt(result.getResponse()
                .getRedirectedUrl()
                .replace("/user/", ""));

        System.out.println(extIdx);
    }

    @Test
    @Transactional
    public void saveUser() throws Exception {

        WebUser user = new WebUser("newUser", "userID", "userPassword1!");

        //given
        MvcResult result = joinUserRequest(user)
                .andExpect(status().is3xxRedirection())
                .andReturn();

        //when
        MockHttpServletResponse response = result.getResponse();

        //then
        assertThat(response.getRedirectedUrl()).matches("/user/[0-9]+");
    }

    @Test
    @Transactional
    public void duplicatedUserSave() throws Exception {
        //given
        joinUserRequest(extUser)
                .andExpect(view().name("user/join"))
                .andExpect(model()
                        .attributeHasFieldErrorCode(WEB_USER_KEY, "userId", "Duplicate"));
    }

    @Test
    @Transactional
    public void findUserSuccess() throws Exception{
        //given
        mockMvc.perform(get("/user/" + extIdx)
                        .sessionAttr(ControllerConst.SESSION_KEY, loginUser))
                .andExpect(status().isOk())
                .andExpect(model().attribute(WEB_USER_KEY, extUser));
    }

   @Test
    @Transactional
    public void findUserFail() throws Exception{
        //given
        MvcResult result = mockMvc.perform(get("/user/" + (extIdx + 1)))
                .andExpect(status().isOk())
                .andExpect(model().attribute(SmbException.WarningKey,
                        new Warning(NoSuchUserException.exceptionMessage)))
                .andReturn();

        //when

        //then
    }

    @Test
    @Transactional
    public void modifyUserSuccess() throws Exception{
        //given
        MvcResult result = mockMvc.perform(post("/user/" + extIdx)
                        .param("userName", "newUserName")
                        .param("userId", extUser.getUserId())
                        .param("password", "newPassword1!")
                        .sessionAttr(ControllerConst.SESSION_KEY, loginUser)
                )
                .andExpect(status().isOk())
                .andReturn();

        //when
        Map<String, Object> models = result.getModelAndView().getModel();

        //then
        assertThat(models.containsKey(WEB_USER_KEY)).isTrue();
        WebUser user = (WebUser) models.get(WEB_USER_KEY);
        assertThat(user.getUserName()).isEqualTo("newUserName");
        assertThat(user.getPassword()).isEqualTo("newPassword1!");
    }

    @Test
    @Transactional
    public void modifyUserFail() throws Exception{
        //given
        MvcResult result = mockMvc.perform(post("/user/" + extIdx)
                        .param("userName", "newUserName")
                        .param("userId","newUserId")
                        .param("password", "newPassword1!")
                        .sessionAttr(ControllerConst.SESSION_KEY, loginUser))
                .andExpect(status().isOk())
                .andExpect(model().attribute(SmbException.WarningKey, new Warning(CantModifyFieldException.exceptionMessage)))
                .andReturn();

        //when
    }

    @Test
    @Transactional
    public void deleteUserSuccess() throws Exception{
        //given
        MvcResult result = mockMvc.perform(delete("/user/" + extIdx))
                .andExpect(status().isOk())
                .andReturn();


        //when

        //then
    }


    private ResultActions joinUserRequest(WebUser user) throws Exception {
        return mockMvc.perform(post(joinUrl)
                    .param("userName", user.getUserName())
                    .param("userId", user.getUserId())
                    .param("password", user.getPassword()))
                .andDo(print());
    }
}