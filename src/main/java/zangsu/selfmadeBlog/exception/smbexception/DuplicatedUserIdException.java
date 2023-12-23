package zangsu.selfmadeBlog.exception.smbexception;

import lombok.Getter;
import zangsu.selfmadeBlog.exception.SmbException;


public class DuplicatedUserIdException extends SmbException {
    public static final String exceptionMessage = "중복된 ID 입니다.";
    public DuplicatedUserIdException() {
        super(exceptionMessage);
    }
}
