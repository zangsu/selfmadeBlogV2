package zangsu.selfmadeBlog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import zangsu.selfmadeBlog.user.exception.CantModifyFieldException;
import zangsu.selfmadeBlog.user.exception.DuplicatedUserIdException;
import zangsu.selfmadeBlog.user.repository.UserDAO;
import zangsu.selfmadeBlog.user.repository.model.DBUser;
import zangsu.selfmadeBlog.user.exception.NoSuchUserException;
import zangsu.selfmadeBlog.user.service.model.ServiceUser;
import zangsu.selfmadeBlog.user.service.model.ServiceUserMapper;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public Long saveUser(ServiceUser user) throws DuplicatedUserIdException {
        long userIdx;
        try{
            userIdx = userDAO.save(ServiceUserMapper.getDBUser(user));
        } catch (DataIntegrityViolationException e){
            throw new DuplicatedUserIdException(e);
        }
        return userIdx;
    }

    public ServiceUser findUser(long idx) throws NoSuchUserException {
        DBUser dbUser = userDAO.find(idx);
        return ServiceUserMapper.getServiceUser(dbUser);
    }

    public void modify(long idx, ServiceUser user) throws NoSuchUserException, CantModifyFieldException {
        userDAO.modify(idx, ServiceUserMapper.getDBUser(user));
    }

    public void delete(long idx) throws NoSuchUserException {
        userDAO.delete(idx);
    }
}
