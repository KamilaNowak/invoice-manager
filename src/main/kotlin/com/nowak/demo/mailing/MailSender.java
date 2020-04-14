package com.nowak.demo.mailing;

import javax.mail.PasswordAuthentication;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MailSender {
    private final static String SMTP_AUTH = "mail.smtp.auth";
    private final static String SMTP_TLS = "mail.smtp.starttls.enable";
    private final static String SMTP_HOST = "mail.smtp.host";
    private final static String SMTP_PORT = "mail.smtp.port";

    private final static String GMAIL_HOST = "smtp.gmail.com";
    private final static String GMAIL_PORT = "587";

    public static void sendEmail( String receiver, String subject, String msg)  {

        Properties credentials = new Properties();
        try (FileReader fileReader = new FileReader("app.properties")) {
            credentials.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String SENDER = credentials.getProperty("sender");
        String SENDER_AUTH_PWD = credentials.getProperty("pass");
        Properties props = new Properties();

        props.put(SMTP_AUTH, "true");
        props.put(SMTP_TLS, "true");
        props.put(SMTP_HOST, GMAIL_HOST);
        props.put(SMTP_PORT, GMAIL_PORT);

        Session session=Session.getInstance(props,new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication () {
                return new PasswordAuthentication(SENDER,SENDER_AUTH_PWD);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);

            message.addRecipient(Message.RecipientType.TO,new InternetAddress(receiver));
            message.setSubject(subject);
            message.setText(msg);
            Transport.send(message);
        } catch (MessagingException e) {throw new RuntimeException(e);}

    }
}
