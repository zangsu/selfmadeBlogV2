package zangsu.selfmadeBlog.model.web;

import org.springframework.ui.Model;
import zangsu.selfmadeBlog.user.exception.CantModifyFieldException;
import zangsu.selfmadeBlog.user.exception.DuplicatedUserIdException;
import zangsu.selfmadeBlog.user.exception.NoSuchUserException;

public class WarningFactory {
    public static Warning duplicatedUserIdWarnings = new Warning("중복된 회원 ID 입니다.");
    public static Warning noUserFindWarnings = new Warning("해당 회원이 존재하지 않습니다");
    public static Warning cantModifyWarnings = new Warning("ID는 수정할 수 없습니다.");

    public static void addWarnings(Model model, Exception e){
        Warning warning = null;
        Class<? extends Exception> exceptionClass = e.getClass();

        if(exceptionClass.equals(NoSuchUserException.class)) {
            warning = noUserFindWarnings;
        }
        else if(exceptionClass.equals(DuplicatedUserIdException.class)) {
            warning = duplicatedUserIdWarnings;
        }
        else if(exceptionClass.equals(CantModifyFieldException.class)) {
            warning = cantModifyWarnings;
        }
        if(warning ==null) {
            return;
        }

        model.addAttribute("warnings", warning);
    }
}
