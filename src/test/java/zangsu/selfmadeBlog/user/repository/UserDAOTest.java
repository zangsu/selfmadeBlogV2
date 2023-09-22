package zangsu.selfmadeBlog.user.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import zangsu.selfmadeBlog.user.model.User;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserDAOTest {

    @Autowired
    UserDAO userDAO;

    User existingUser = new User("existing User", "existing userID", "existing userPW");
    long existingId;

    @BeforeEach
    public void prepareBasicUser(){
        existingId = userDAO.save(existingUser);
    }

    @Test
    @Transactional
    public void saveTest() throws Exception{
        //given
        User user = new User("new User", "userID", "userPW");

        //when
        long savedId = userDAO.save(user);

        //then
        User findUser = userDAO.find(savedId);
        checkUserSame(user, findUser);
    }

    @Test
    @Transactional
    public void findTest() throws Exception{
        //given

        //when
        User findUser = userDAO.find(existingId);

        //then
        checkUserSame(existingUser, findUser);
    }

    @Test
    @Transactional
    public void deleteTeset() throws Exception{
        //given
        userDAO.delete(existingId);

        //when
        User findUser = userDAO.find(existingId);

        //then
        assertThat(findUser).isNull();
    }

    private void checkUserSame(User u1, User u2) {
        assertThat(u1.getUserName()).isEqualTo(u2.getUserName());
        assertThat(u1.getId()).isEqualTo(u2.getId());
        assertThat(u1.getPassword()).isEqualTo(u2.getPassword());
    }
}