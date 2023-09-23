package zangsu.selfmadeBlog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zangsu.selfmadeBlog.user.repository.UserDAO;
import zangsu.selfmadeBlog.user.repository.model.DBUser;
import zangsu.selfmadeBlog.user.exception.NoSuchUserException;
import zangsu.selfmadeBlog.user.service.model.ServiceUser;
import zangsu.selfmadeBlog.user.service.model.ServiceUserMapper;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    public Long saveUser(ServiceUser user){
        long userIdx = userDAO.save(ServiceUserMapper.getDBUser(user));
        return userIdx;
    }

    public ServiceUser findUser(long idx){
        DBUser dbUser = userDAO.find(idx);
        return ServiceUserMapper.getServiceUser(dbUser);
    }

    public void modify(long idx, ServiceUser user){
        userDAO.modify(idx, ServiceUserMapper.getDBUser(user));
    }

    public void delete(long idx){
        userDAO.delete(idx);
    }
}
