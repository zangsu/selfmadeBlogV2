package zangsu.selfmadeBlog.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import zangsu.selfmadeBlog.user.exception.CantModifyFieldException;
import zangsu.selfmadeBlog.user.exception.NoSuchUserException;
import zangsu.selfmadeBlog.user.service.model.ServiceUser;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    ServiceUser existingUser = new ServiceUser("eUserName", "eUserID", "eUserPW");
    long existUserIdx;
    @BeforeEach
    public void init(){
        existUserIdx = userService.saveUser(existingUser);
    }

    @Test
    @Transactional
    public void findTest() throws Exception{
        //given

        //when
        ServiceUser findUser = userService.findUser(existUserIdx);

        //then
        isSameUser(existingUser, findUser);
    }

    @Test
    @Transactional
    public void cantFindTest() throws Exception{
        //given

        //when

        //then
        assertThatThrownBy(() -> userService.findUser(existUserIdx + 1))
                .isInstanceOf(NoSuchUserException.class);
    }

    @Test
    @Transactional
    public void saveTest() throws Exception{
        //given
        ServiceUser user = new ServiceUser("userName", "userID", "userPW");
        Long savedIdx = userService.saveUser(user);

        //when
        ServiceUser findUser = userService.findUser(savedIdx);

        //then
        isSameUser(user, findUser);
    }

    @Test
    @Transactional
    public void modifyTest() throws Exception{
        //given
        existingUser.setUserName("modifyUserName");
        existingUser.setPassword("newPassword");

        //when
        userService.modify(existUserIdx, existingUser);
        ServiceUser findUser = userService.findUser(existUserIdx);

        //then
        isSameUser(existingUser, findUser);
    }

    @Test
    @Transactional
    public void cantModifyIdTest() throws Exception{
        //given
        existingUser.setId("newId");

        //when
        assertThatThrownBy(() -> userService.modify(existUserIdx, existingUser))
                .isInstanceOf(CantModifyFieldException.class);

        //then
    }

    @Test
    @Transactional
    public void deleteTest() throws Exception{
        //given
        userService.delete(existUserIdx);

        //when
        assertThatThrownBy(() -> userService.findUser(existUserIdx))
                .isInstanceOf(NoSuchUserException.class);
        //then
    }

    private void isSameUser(ServiceUser u1, ServiceUser u2) {
        assertThat(u1.getId()).isEqualTo(u2.getId());
        assertThat(u1.getUserName()).isEqualTo(u2.getUserName());
        assertThat(u1.getPassword()).isEqualTo(u2.getPassword());
    }

}