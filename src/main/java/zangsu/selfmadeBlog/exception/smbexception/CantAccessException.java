package zangsu.selfmadeBlog.exception.smbexception;

import lombok.Getter;
import zangsu.selfmadeBlog.exception.SmbException;


public class CantAccessException extends SmbException {
    public static final String exceptionMessage = "접근 권한이 존재하지 않습니다.";
    public CantAccessException() {
        super(exceptionMessage);
    }

}
