package com.sumscope.cdh.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by Roidder on 2016/11/11.
 */

public class EmailUtil {

    @Autowired
    private JavaMailSenderImpl mailSender;

    private String toEmail;

    private String[] toEmails;

    public void sendEmail(String toEmail, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("请点击以下链接找回密码-->http://" + AddressUtil.getIp() + ":9900/index.html#/resetPassword?userName=" + userName);
        message.setTo(toEmail);
        message.setSubject("Admin-web找回密码!");
        String diyName = AddressUtil.diyTitle(mailSender.getUsername(), "Mailsenderservice-No-Reply");
        diyName = diyName != null ? diyName : mailSender.getUsername();
        message.setFrom(diyName);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendNoticeEmail(String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(text);
        message.setTo(getToEmails());
        message.setSubject(text);
        String diyName = AddressUtil.diyTitle(mailSender.getUsername(), "Mailsenderservice-No-Reply");
        diyName = diyName != null ? diyName : mailSender.getUsername();
        message.setFrom(diyName);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JavaMailSenderImpl getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }
    
    public String getToEmail() {
        return toEmail;
    }
    
    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String[] getToEmails() {
        if(toEmail.contains(",")){
            toEmails = toEmail.split(",");
        }else {
            toEmails = new String[1];
            toEmails[0] = toEmail;
        }
        return toEmails;
    }

    public void setToEmails(String[] toEmails) {
        this.toEmails = toEmails;
    }
}
