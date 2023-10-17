package zangsu.selfmadeBlog.user.controller.model;

import zangsu.selfmadeBlog.user.service.model.ServiceUser;

public class WebUserMapper {
    public static ServiceUser getServiceUser(WebUser webUser){
        return new ServiceUser(
                webUser.getUserName(),
                webUser.getUserId(),
                webUser.getPassword());
    }

    public static WebUser getWebUser(ServiceUser serviceUser){
        return new WebUser(
                serviceUser.getUserName(),
                serviceUser.getId(),
                serviceUser.getPassword()
        );
    }
}
