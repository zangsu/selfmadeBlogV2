package zangsu.selfmadeBlog.exception.smbexception;

import zangsu.selfmadeBlog.exception.SmbException;

public class DuplicatedUserIdException extends SmbException {
    private static final String exceptionMessage = "중복된 ID 입니다.";
    public DuplicatedUserIdException() {
        super(exceptionMessage);
    }
}
