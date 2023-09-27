package zangsu.selfmadeBlog.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import zangsu.selfmadeBlog.model.web.WarningFactory;
import zangsu.selfmadeBlog.user.controller.model.WebUser;
import zangsu.selfmadeBlog.user.controller.model.WebUserMapper;
import zangsu.selfmadeBlog.user.exception.DuplicatedUserIdException;
import zangsu.selfmadeBlog.user.service.UserService;
import zangsu.selfmadeBlog.user.service.model.ServiceUser;

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
    final static WebUser extUser = new WebUser("extUser", "extUserID", "extUserPassword");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void existingUser() throws Exception {
        MvcResult result = mockMvc.perform(post(joinUrl)
                        .param("userName", extUser.getUserName())
                        .param("id", extUser.getId())
                        .param("password", extUser.getPassword())
                        .content(objectMapper.writeValueAsString(extUser)))
                .andReturn();

        extIdx = Integer.parseInt(result.getResponse()
                .getRedirectedUrl()
                .replace("/user/", ""));

        System.out.println(extIdx);
    }

    @Test
    @Transactional
    public void saveUser() throws Exception {

        //given
        MvcResult result = mockMvc.perform(post(joinUrl)
                        .param("userName", "new User")
                        .param("id", "userID")
                        .param("password", "userPW"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        //when

        MockHttpServletResponse response = result.getResponse();
        assertThat(response.getRedirectedUrl()).matches("/user/[0-9]+");
        //then

        //assertThat("1243").matches("[0-9]"); //false
        //assertThat("1243").matches("[0-9]+"); //true
        //assertThat("1243").matches("\\d");//false
        //assertThat("1243").matches("\\d+"); //true
    }

    @Test
    @Transactional
    public void duplicatedUserSave() throws Exception {
        //given

        MvcResult result = mockMvc.perform(post(joinUrl)
                        .param("userName", extUser.getUserName())
                        .param("id", extUser.getId())
                        .param("password", extUser.getPassword()))
                .andReturn();

        //when
        ModelAndView mv = result.getModelAndView();

        //then
        assertThat(mv.getViewName()).isEqualTo("user/join");
        Map<String, Object> modelAttributes = mv.getModel();
        assertThat(modelAttributes.get("warnings"))
                .isEqualTo(WarningFactory.duplicatedUserIdWarnings);
    }
}