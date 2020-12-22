package cn.edu.zju.template.exception;

public class KeyNotFoundException extends BaseException {

    private static final long serialVersionUID = 1L;

    public KeyNotFoundException(int code, String msg) {
        super(code, msg);
    }

}
