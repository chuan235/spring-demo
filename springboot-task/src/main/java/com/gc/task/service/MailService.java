package com.gc.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author gouchuan
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.fromMail.addr}")
    private String from;

    /**
     * 发送文本邮件
     * @param to
     * @param subject
     * @param content
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送html的邮件
     * @param to
     * @param subject
     * @param content
     */
    public void sendHtmlMail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            preSend(message, to, subject, content);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param content
     * @param filePath
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath){
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = preSend(message, to, subject, content);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            // helper.addAttachment("test"+fileName, file);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送正文中有静态资源（图片）的邮件
     * @param to
     * @param subject
     * @param content
     * @param rscPath
     * @param rscId
     */
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId){
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = preSend(message, to, subject, content);
            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private MimeMessageHelper preSend(MimeMessage message,String to, String subject, String content) throws MessagingException{
        //true表示需要创建一个multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        return helper;
    }
}
