package cn.edu.zju.template.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zyl
 * controller层日志
 */
@Component
@Aspect
public class ControllerLogAspect {
    public static final Logger log = LoggerFactory.getLogger(ControllerLogAspect.class);

    /**
     * 切面点
     */
    private final String POINT_CUT = "execution(* cn.edu.zju.train.controller.*.*(..))";

    @Pointcut(POINT_CUT)
    private void pointcut() {
    }

    /**
     * 前置通知，方法调用前被调用
     *
     * @param joinPoint
     */
    @Before(value = POINT_CUT)
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames(); // 参数名
        String methodName = joinPoint.getSignature().getName();
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        for (int i = 0; i < args.length; i++) {
            sb.append(argNames[i]).append(':').append(args[i]).append(", ");
        }
        sb.append(']');
        log.debug("controller call < {} > : {}", methodName, sb);
    }

    @AfterReturning(value = POINT_CUT, returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.debug("controller return < {} > : {}", methodName, result);
    }
}
