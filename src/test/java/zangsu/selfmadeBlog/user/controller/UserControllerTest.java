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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import zangsu.selfmadeBlog.model.web.WarningFactory;
import zangsu.selfmadeBlog.user.controller.model.WebUser;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    final String joinUrl = "/user/join";

    long extIdx;
    final static WebUser extUser =
            new WebUser("extUser", "extUserID", "extUserPassword");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void existingUser() throws Exception {
        MvcResult result = mockMvc.perform(post(joinUrl)
                        .param("userName", extUser.getUserName())
                        .param("id", extUser.getUserId())
                        .param("password", extUser.getPassword())
                )
                .andReturn();

        extIdx = Integer.parseInt(result.getResponse()
                .getRedirectedUrl()
                .replace("/user/", ""));

        System.out.println(extIdx);
    }

    @Test
    @Transactional
    public void saveUser() throws Exception {

        WebUser user = new WebUser("new User", "userID", "userPW");

        //given
        MvcResult result = mockMvc.perform(post(joinUrl)
                        .param("userName", user.getUserName())
                        .param("id", user.getUserId())
                        .param("password", user.getPassword()))
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

        MvcResult result = mockMvc.perform(post(joinUrl)
                        .param("userName", extUser.getUserName())
                        .param("id", extUser.getUserId())
                        .param("password", extUser.getPassword()))
                .andReturn();

        //when
        ModelAndView mv = result.getModelAndView();

        //then
        assertThat(mv.getViewName()).isEqualTo("user/join");
        Map<String, Object> modelAttributes = mv.getModel();
        assertThat(modelAttributes.get(WarningFactory.WarningKey))
                .isEqualTo(WarningFactory.duplicatedUserIdWarnings);
    }

    @Test
    @Transactional
    public void findUserSuccess() throws Exception{
        //given
        MvcResult result = mockMvc.perform(get("/user/" + extIdx))
                .andExpect(status().isOk())
                .andReturn();

        //when
        ModelAndView mv = result.getModelAndView();
        ModelMap modelMap = mv.getModelMap();

        //then
        assertThat(modelMap.containsKey("user")).isTrue();
        WebUser user = (WebUser) modelMap.get("user");
        assertThat(user.getUserName()).isEqualTo(extUser.getUserName());
        assertThat(user.getUserId()).isEqualTo(extUser.getUserId());
        assertThat(user.getPassword()).isEqualTo(extUser.getPassword());
    }

    @Test
    @Transactional
    public void findUserFail() throws Exception{
        //given
        MvcResult result = mockMvc.perform(get("/user/" + (extIdx + 1)))
                .andExpect(status().isOk())
                .andReturn();

        //when
        ModelAndView mv = result.getModelAndView();
        Map<String, Object> models = mv.getModel();

        //then
        assertThat(models.containsKey(WarningFactory.WarningKey)).isTrue();
        assertThat(models.get("warnings")).isEqualTo(WarningFactory.noUserFindWarnings);
    }

    @Test
    @Transactional
    public void modifyUserSuccess() throws Exception{
        //given
        MvcResult result = mockMvc.perform(post("/user/" + extIdx)
                        .param("userName", "newUserName")
                        .param("id", extUser.getUserId())
                        .param("password", "newPassword"))
                .andExpect(status().isOk())
                .andReturn();

        //when
        Map<String, Object> models = result.getModelAndView().getModel();

        //then
        assertThat(models.containsKey("user")).isTrue();
        WebUser user = (WebUser) models.get("user");
        assertThat(user.getUserName()).isEqualTo("newUserName");
        assertThat(user.getPassword()).isEqualTo("newPassword");
    }
    
    @Test
    @Transactional
    public void modifyUserFail() throws Exception{
        //given
        MvcResult result = mockMvc.perform(post("/user/" + extIdx)
                        .param("id", "newUserId"))
                .andExpect(status().isOk())
                .andReturn();

        //when
        ModelAndView mv = result.getModelAndView();
        Map<String, Object> models = mv.getModel();
        //then
        assertThat(models.containsKey(WarningFactory.WarningKey)).isTrue();
        assertThat(models.get(WarningFactory.WarningKey)).isEqualTo(WarningFactory.cantModifyWarnings);
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
}