package cn.edu.zju.template.exception;

public class InnerException extends BaseException {

    private static final long serialVersionUID = 1L;

    public InnerException() {
        super(500, "服务器错误");
    }
}
