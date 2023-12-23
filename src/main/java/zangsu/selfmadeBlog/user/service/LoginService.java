package zangsu.selfmadeBlog.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import zangsu.selfmadeBlog.exception.smbexception.NoSuchUserException;
import zangsu.selfmadeBlog.exception.smbexception.WrongLoginData;
import zangsu.selfmadeBlog.user.repository.UserDAO;
import zangsu.selfmadeBlog.user.repository.model.DBUser;

@Service
@RequiredArgsConstructor
public class LoginService {

    @Autowired
    private final UserDAO userDAO;

    public long login(String loginId, String password) {
        DBUser findUser = userDAO.findDBUserByUserId(loginId)
                .orElseThrow(() -> new WrongLoginData());

        if (findUser.getPassword().equals(password)) {
            return findUser.getIdx();
        }
        throw new WrongLoginData();
    }

}
