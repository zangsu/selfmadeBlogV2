package zangsu.selfmadeBlog.user.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import zangsu.selfmadeBlog.user.controller.model.WebUser;

import java.util.regex.Pattern;

@Component
public class WebUserValidator implements Validator {

    public static final String USER_ID_PATTERN = "^[a-zA-Z][a-zA-Z0-9]{4,9}$";
    public static final String USER_PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W])[a-zA-Z\\d\\W]{8,20}$";

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(WebUser.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        WebUser webUser = (WebUser) target;
        BindingResult bindingResult = (BindingResult) errors;

        validateId(webUser.getUserId(), bindingResult);
        validatePassword(webUser.getPassword(), bindingResult);
    }

    public void validatePassword(String password, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors("password"))
            return;
        if(!Pattern.matches(USER_PASSWORD_PATTERN, password))
            bindingResult.rejectValue("password", "Pattern", "");
    }

    public void validateId(String userId, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors("userId"))
            return;
        if(!Pattern.matches(USER_ID_PATTERN, userId))
            bindingResult.rejectValue("userId", "Pattern", "");
    }
}
