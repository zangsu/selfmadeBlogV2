package zangsu.selfmadeBlog.exception.smbexception;

import zangsu.selfmadeBlog.exception.SmbException;

public class CantAccessException extends SmbException {
    private static final String exceptionMessage = "접근 권한이 존재하지 않습니다.";
    public CantAccessException() {
        super(exceptionMessage);
    }
}
