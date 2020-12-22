package cn.edu.zju.template.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import cn.edu.zju.template.exception.BaseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Component
public class EmailUtil {
    private static final Logger log = LoggerFactory.getLogger(EmailUtil.class);

    Map<String, String> emailCode = new HashMap<String, String>();

    private String From;
    @Autowired
    private JavaMailSender mailSender;
    private Random random = new Random();

    @Value("${spring.mail.username}")
    private void setFrom(String from) {
        this.From = from;
        log.info("初始化EmailUtil.From:{}", this.From);
    }

    private String generatorCode(int length) {
        String code = String.format("%08d", this.random.nextInt((int) Math.pow(10, length)));
        return code;
    }

    private void addRecord(String email, String code) {
        this.emailCode.put(email, code);
    }

    private void removeRecord(String email) {
        if (email != null) {
            this.emailCode.remove(email);
        }
    }

    /**
     * 检查验证码是否正确
     * 注意：因为内部实现的方式的问题，导致验证码只能被验证一次，即使验证码是正确的也无法被验证第二次
     *
     * @param email
     * @param code
     * @return
     */
    public boolean checkCode(String email, String code) {
        if (this.isValidCode(email, code)) {
            this.removeRecord(email);
            return true;
        }
        return false;
    }

    private boolean isValidCode(String email, String code) {
        String code_ = this.emailCode.get(email);
        if (code_ == null) {
            return false;
        }
        if (!code_.equals(code)) {
            return false;
        }
        return true;
    }

    /**
     * 发送验证码邮件
     *
     * @param sendTo 目标邮箱地址
     */
    public void sendCode(String sendTo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(From);
        message.setTo(sendTo);
        message.setSubject("药品溯源管理系统");
        String code = this.generatorCode(8);
        message.setText("您的验证码是:" + code + "");
        try {
            mailSender.send(message);
            addRecord(sendTo, code);
        } catch (Exception e) {
            log.debug("From:{}, To:{}, content:{}", this.From, sendTo, code);
            log.error(e.getMessage());
            throw new BaseException(-1, "邮件发送失败");
        }
    }

    /**
     * 发送随机密码邮件
     *
     * @param sendTo 目标邮箱地址
     */
    public String sendRandomPassword(String sendTo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(From);
        message.setTo(sendTo);
        message.setSubject("药品溯源管理系统");
        String code = this.generatorCode(8);
        message.setText("您的随机密码是:" + code + ", 为了您的账号安全，请尽快登录修改密码！");
        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.debug("From:{}, To:{}, content:{}", this.From, sendTo, code);
            log.error(e.getMessage());
            throw new BaseException(-1, "邮件发送失败");
        }
        return code;
    }

}