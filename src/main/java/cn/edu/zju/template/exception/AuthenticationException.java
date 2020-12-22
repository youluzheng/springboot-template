package cn.edu.zju.template.exception;

public class AuthenticationException extends BaseException {

    private static final long serialVersionUID = 1L;

    public AuthenticationException() {
        super(401, "请求要求用户的身份认证");
    }

}
