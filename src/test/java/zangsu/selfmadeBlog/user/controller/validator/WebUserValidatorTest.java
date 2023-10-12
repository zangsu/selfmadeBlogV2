package zangsu.selfmadeBlog.user.controller.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class WebUserValidatorTest {
    WebUserValidator validator = new WebUserValidator();

    @Test
    public void id는_숫자로_시작하면_안됨() throws Exception{
        //given
        BindingResult bindingResult = Mockito.mock(BindingResult.class);

        //when
        validator.validateId("123User", bindingResult);

        //then
        assertThat(Pattern.matches(validator.USER_ID_PATTERN, "123User"))
                .isFalse();
        assertThat(bindingResult.hasFieldErrors("userId")).isTrue();
        assertThat(bindingResult.getFieldError("userId").getCodes())
                .contains("Pattern.WebUser.userId");
    }

}