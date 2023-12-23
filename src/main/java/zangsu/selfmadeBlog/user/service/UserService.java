package zangsu.selfmadeBlog.user.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import zangsu.selfmadeBlog.exception.smbexception.CantModifyFieldException;
import zangsu.selfmadeBlog.exception.smbexception.DuplicatedUserIdException;
import zangsu.selfmadeBlog.exception.smbexception.NoSuchUserException;
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
            throw new DuplicatedUserIdException();

        DBUser dbUser = userDAO.save(ServiceUserMapper.getDBUser(user));
        return dbUser.getIdx();
    }

    public ServiceUser findUser(long idx) throws NoSuchUserException {
        DBUser dbUser = findDbUser(idx);
        return ServiceUserMapper.getServiceUser(dbUser);
    }

    @Transactional
    public void modify(long idx, ServiceUser user) throws NoSuchUserException, CantModifyFieldException {
        DBUser dbUser = findDbUser(idx);
        if(!dbUser.getUserId().equals(user.getId())) {
            throw new CantModifyFieldException();
        }
        System.out.println("try modify in service");
        dbUser.setUserName(user.getUserName());
        dbUser.setPassword(user.getPassword());
        System.out.println("end modify");
    }

    public void delete(long idx) throws NoSuchUserException {
        DBUser dbUser = findDbUser(idx);
        userDAO.delete(dbUser);
    }

    public boolean checkId(String userId){
        return userDAO.existsByUserId(userId);
    }

    private DBUser findDbUser(long idx) throws NoSuchUserException {
        return userDAO.findById(idx)
                .orElseThrow(() -> new NoSuchUserException());
    }


}