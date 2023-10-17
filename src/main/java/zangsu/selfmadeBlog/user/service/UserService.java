package zangsu.selfmadeBlog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import zangsu.selfmadeBlog.user.exception.CantModifyFieldException;
import zangsu.selfmadeBlog.user.exception.DuplicatedUserIdException;
import zangsu.selfmadeBlog.user.exception.NoSuchUserException;
import zangsu.selfmadeBlog.user.repository.UserDAO;
import zangsu.selfmadeBlog.user.repository.model.DBUser;
import zangsu.selfmadeBlog.user.service.model.ServiceUser;
import zangsu.selfmadeBlog.user.service.model.ServiceUserMapper;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public Long saveUser(ServiceUser user) throws DuplicatedUserIdException {
        if(userDAO.existsByUserId(user.getId()))
            throw new DuplicatedUserIdException(new DataIntegrityViolationException(""));

        DBUser dbUser = userDAO.save(ServiceUserMapper.getDBUser(user));
        return dbUser.getIdx();
    }

    public ServiceUser findUser(long idx) throws NoSuchUserException {
        DBUser dbUser = findDbUser(idx);
        return ServiceUserMapper.getServiceUser(dbUser);
    }

    public void modify(long idx, ServiceUser user) throws NoSuchUserException, CantModifyFieldException {
        DBUser dbUser = findDbUser(idx);
        if(!dbUser.getUserId().equals(user.getId()))
            throw new CantModifyFieldException("");
        dbUser.setUserName(user.getUserName());
        dbUser.setPassword(user.getPassword());
    }

    public void delete(long idx) throws NoSuchUserException {
        DBUser dbUser = findDbUser(idx);
        userDAO.delete(dbUser);
    }

    public boolean checkId(String userId){
        return userDAO.existsByUserId(userId);
    }

    private DBUser findDbUser(long idx) throws NoSuchUserException {
        Optional<DBUser> dbUser = userDAO.findById(idx);
        if(dbUser.isEmpty())
            throw new NoSuchUserException("");
        return dbUser.get();
    }


}