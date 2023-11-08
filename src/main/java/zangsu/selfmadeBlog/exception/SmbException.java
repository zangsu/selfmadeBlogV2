package zangsu.selfmadeBlog.exception;

import org.springframework.ui.Model;
import zangsu.selfmadeBlog.model.web.Warning;

public class SmbException extends RuntimeException{
    public static String WarningKey = "warnings";

    public SmbException(String message){
        super(message);
    }
    public void addWarnings(Model model){
        Warning warning = new Warning(this.getMessage());
        System.out.println("[message] " + warning.getMessage());

        model.addAttribute(WarningKey, warning);
    }
}
