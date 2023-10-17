package zangsu.selfmadeBlog.user.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zangsu.selfmadeBlog.user.repository.model.DBUser;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class tempUserDaoTest {

    @Autowired
    tempUserDao dao;

    @Test
    @Transactional
    public void 중복_저장() throws Exception{
        //given
        DBUser user1 = new DBUser("user1", "userId", "password1");
        dao.save(user1);

        DBUser user2 = new DBUser("user2", "userId", "password2");
        dao.save(user2);


        //when

        //then
    }
}