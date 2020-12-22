package cn.edu.zju.template.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import cn.edu.zju.template.exception.AuthenticationException;
import cn.edu.zju.template.util.ResponseUtil;
import cn.edu.zju.template.util.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class TokenInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);

    /**
     * 先判断方法或类上是否有注解，如果有返回true
     * 如果不存在token，返回false
     * 存在token，验证token是否正确，是返回true，否者返回false
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        if (!(handler instanceof HandlerMethod)) {
            log.debug("url : [" + request.getRequestURI() + "], 类型:   [" + request.getMethod() + "] 接口不存在");
            ResponseUtil rp = new ResponseUtil(400, "url : [" + request.getRequestURI() + "], 类型:   [" + request.getMethod() + "] 接口不存在");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(rp.toJsonStr());
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        log.debug("【TokenInterceptor】:[url:{}, type:{}]", request.getRequestURI(), request.getMethod());
        // 如果方法上有注解
        if (method.isAnnotationPresent(TokenNotRequire.class)) {
            return true;
        }
        // 如果类上有注解
        if (method.getDeclaringClass().isAnnotationPresent(TokenNotRequire.class)) {
            return true;
        }
        String token = request.getHeader("token");
        if (token == null || token.equals("null")) {
            throw new AuthenticationException();
        }
        int code = TokenUtil.isValid(token);
        if (code == TokenUtil.Token_Expire || code == TokenUtil.Other_Error) {
            throw new AuthenticationException();
        }
        return true;
    }
}