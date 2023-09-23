package zangsu.selfmadeBlog.user.exception;

public class NoSuchUserException extends IllegalArgumentException{
    public NoSuchUserException(String s) {
        super(s);
    }
}
