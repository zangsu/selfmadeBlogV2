package zangsu.selfmadeBlog.exception.smbexception;

import lombok.Getter;
import zangsu.selfmadeBlog.exception.SmbException;

public class NoSuchUserException extends SmbException {

    public static final String exceptionMessage = "해당 회원이 존재하지 않습니다.";
    public NoSuchUserException() {
        super(exceptionMessage);
    }
}
