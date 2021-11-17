/*
 * EmailService.java
 * Author : 박찬형
 * Created Date : 2021-08-04
 */
package com.codrest.teriser.developers;

import com.codrest.teriser.developers.accounts.AccountMode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${email.subject.register}")
    private String registerSubject;
    @Value("${email.subject.login}")
    private String loginSubject;
    @Value("${email.subject.deactivate}")
    private String deactivateSubject;
    @Value("${email.from}")
    private String from;
    @Value("${email.url}")
    private String url;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async("threadPoolTaskExecutor")
    public void sendAccountVerifyEmail(Email email, String token, AccountMode mode){
        String subject = mode == AccountMode.REGISTER ? registerSubject: deactivateSubject;
        Map<String, String> contents = new HashMap<>();
        contents.put("mode", mode.mailText());
        contents.put("url", url + token);
        send(email, subject, "mail/verify_account", contents);
    }

    @Async("threadPoolTaskExecutor")
    public void sendLoginToken(Email email, String loginToken) {
        Map<String, String> contents = new HashMap<>();
        contents.put("token", loginToken);

        send(email, loginSubject, "mail/login", contents);
    }

    public void send(Email to, String subject, String templateName, Map<String, String> contents) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            Context context = new Context();
            contents.forEach(context::setVariable);
            String html = templateEngine.process(templateName, context);
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(from);
            messageHelper.setTo("<" + to.getAddress() + ">");
            messageHelper.setSubject(subject);
            messageHelper.setText(html, true);
        };
        javaMailSender.send(messagePreparator);
    }
}
