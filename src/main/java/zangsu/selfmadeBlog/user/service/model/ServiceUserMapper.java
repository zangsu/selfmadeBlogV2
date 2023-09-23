package zangsu.selfmadeBlog.user.service.model;

import zangsu.selfmadeBlog.user.repository.model.DBUser;

public class ServiceUserMapper {

    public static DBUser getDBUser(ServiceUser user){
        DBUser dbUser = new DBUser(user.getUserName(), user.getId(), user.getPassword());
        return dbUser;
    }

    public static ServiceUser getServiceUser(DBUser dbUser){
        ServiceUser user = new ServiceUser(dbUser.getUserName(), dbUser.getId(), dbUser.getPassword());
        return user;
    }
}
