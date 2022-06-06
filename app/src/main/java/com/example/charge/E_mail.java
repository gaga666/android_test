package com.example.charge;

import java.util.Properties;
import java.util.regex.Pattern;

public class E_mail {

    private String PROTOCOL = "smtp";
    private String HOST = "smtp.qq.com";
    private String PORT = "587";
    private String IS_AUTH = "true";
    private String IS_ENABLED_DEBUG_MOD = "true";

    // 发件人
    private static String from = "1634919209@qq.com";
    //授权码
    private static String authenticCode = "gpvzocwfijswbghi";
    // 初始化连接邮件服务器的会话信息
    private static Properties props;
//    public e_mail(String toEmail) {
////        props = new Properties();
////        props.setProperty("mail.transport.protocol", PROTOCOL);   // 邮件发送协议
////        props.setProperty("mail.smtp.host", HOST);   // SMTP邮件服务器
////        props.setProperty("mail.smtp.port", PORT);   // SMTP邮件服务器默认端口
////        props.setProperty("mail.smtp.auth", IS_AUTH);   // 是否要求身份认证
////        props.setProperty("mail.debug", IS_ENABLED_DEBUG_MOD);   // 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
//    }
    /**
     * 发送验证码
     */


    public  static boolean isValidEmail(String email) {
        if ((email != null) && (!email.isEmpty())) {
            return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
        }
        return false;
    }
}
