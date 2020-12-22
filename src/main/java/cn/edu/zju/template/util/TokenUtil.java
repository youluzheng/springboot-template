package cn.edu.zju.template.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;

import cn.edu.zju.template.exception.AuthenticationException;
import cn.edu.zju.template.exception.BaseException;
import cn.edu.zju.template.exception.InnerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class TokenUtil {
    // 错误类型
    public static final int OK = 0;
    public static final int Token_Expire = 1;
    public static final int Algorithm_Mismatch = 2;
    public static final int Invalid_Claim = 3;
    public static final int Other_Error = 10;
    private static final Logger log = LoggerFactory.getLogger(TokenUtil.class);
    //私钥
    private static String TOKEN_SECRET;
    //过期时间
    private static long EXPIRE_TIME;

    public static String generatorToken(String name, Object value) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, value);
        return TokenUtil.generatorToken(map);
    }

    public static String generatorToken(Map<String, Object> map) {
        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "jwt");
            // 返回token字符串
            JWTCreator.Builder builder = JWT.create()
                    .withHeader(header)
                    .withIssuedAt(new Date()) //发证时间
                    .withExpiresAt(date);  //过期时间
            //   .sign(algorithm);  //密钥
//              map.entrySet().forEach(entry -> builder.withClaim(entry.getKey(), entry.getValue()));
            map.entrySet().forEach(entry -> {
                if (entry.getValue() instanceof Integer) {
                    builder.withClaim(entry.getKey(), (Integer) entry.getValue());
                } else if (entry.getValue() instanceof Long) {
                    builder.withClaim(entry.getKey(), (Long) entry.getValue());
                } else if (entry.getValue() instanceof Boolean) {
                    builder.withClaim(entry.getKey(), (Boolean) entry.getValue());
                } else if (entry.getValue() instanceof String) {
                    builder.withClaim(entry.getKey(), String.valueOf(entry.getValue()));
                } else if (entry.getValue() instanceof Double) {
                    builder.withClaim(entry.getKey(), (Double) entry.getValue());
                } else if (entry.getValue() instanceof Date) {
                    builder.withClaim(entry.getKey(), (Date) entry.getValue());
                }
            });
            return builder.sign(algorithm);
        } catch (Exception e) {
            log.debug("map:{}", map);
            log.error("token生成失败" + e.getMessage());
            throw new InnerException();
        }
    }

    /**
     * 检验token是否正确
     *
     * @param **token**
     * @return
     * @throws AlgorithmMismatchException     if the algorithm stated in the token's header it's not equal to the one defined in the {@link JWTVerifier}.
     * @throws SignatureVerificationException if the signature is invalid.
     * @throws TokenExpiredException          if the token has expired.
     * @throws InvalidClaimException          if a claim contained a different value than the expected one.
     */
    public static int isValid(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWT.require(algorithm).build().verify(token);
            return OK;
        } catch (TokenExpiredException e) {
            log.debug("token检查" + e.getMessage());
            return Token_Expire;
        } catch (Exception e1) {
            log.debug("token检查" + e1.getMessage());
            return Other_Error;
        }
    }

    public static int getIntItem(String token, String key) throws BaseException {
        return TokenUtil.getItem(token, key, Integer.class);
    }

    public static String getStringItem(String token, String key) throws BaseException {
        return TokenUtil.getItem(token, key, String.class);
    }

    public static <T> T getItem(String token, String key, Class<T> clazz) {
        if (TokenUtil.isValid(token) != 0) {
            log.debug("token:{}, key:{}, clazz:{}", token, key, clazz);
            log.error("token错误");
            throw new AuthenticationException();
        }
        Map<String, Claim> claims = TokenUtil.getClaims(token);
        if (StringUtils.isEmpty(key)) {
            log.debug("token:{}, key:{}, clazz:{}", token, key, clazz);
            log.error("key错误");
            throw new InnerException();
        }
        if (!claims.containsKey(key)) {
            log.debug("token:{}, key:{}, clazz:{}", token, key, clazz);
            log.error("key不存在");
            throw new InnerException();
        }
        T item = claims.get(key).as(clazz);
        return item;
    }

    /**
     * 获取用户自定义Claim集合
     *
     * @param token
     * @return
     */
    public static Map<String, Claim> getClaims(String token) {
        if (TokenUtil.isValid(token) != 0) {
            log.debug("token:{}", token);
            log.error("token错误");
            throw new BaseException(-1, "token错误");
        }
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        Map<String, Claim> jwt = verifier.verify(token).getClaims();
        return jwt;
    }

    /**
     * 获取过期时间
     *
     * @param token
     * @return
     */
    public static Date getExpiresAt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        return JWT.require(algorithm).build().verify(token).getExpiresAt();
    }

    /**
     * 获取jwt发布时间
     */
    public static Date getIssuedAt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        return JWT.require(algorithm).build().verify(token).getIssuedAt();
    }

    @Value("${spring.token.secretKey}")
    private void setKey(String key) {
        log.info("初始化Token.key:******");
        TokenUtil.TOKEN_SECRET = key;
    }

    @Value("${spring.token.expireTime}")
    private void setExpireTime(int min) {
        log.info("初始化Token.expireTime:{}min", min);
        TokenUtil.EXPIRE_TIME = min * 60 * 1000;
    }
}
