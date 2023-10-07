package zangsu.selfmadeBlog.user.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import zangsu.selfmadeBlog.user.controller.model.WebUser;

import java.util.regex.Pattern;

@Component
public class WebUserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(WebUser.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        WebUser webUser = (WebUser) target;
        BindingResult bindingResult = (BindingResult) errors;

        validateId(webUser.getUserId(), bindingResult);
    }

    private void validateId(String userId, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors("userId"))
            return;
        if(!Pattern.matches("^[a-zA-Z][a-zA-Z0-9]*.{4,9}$", userId))
            bindingResult.rejectValue("userId", "Pattern");
    }
}
