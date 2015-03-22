package org.savvas.shafted.service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import org.savvas.shafted.controller.request.RegistrationRequest;
import org.savvas.shafted.domain.User;
import org.savvas.shafted.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public void sendEmail() {
        try {
            String host = "smtp.gmail.com";
            String from = "savvas.a.michael@gmail.com";
            String pass = "pass";
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.user", from);
            props.put("mail.smtp.password", pass);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true");

            Session session = Session.getInstance(props, new GMailAuthenticator("savvas.a.michael@gmail.com", "cstrike54321"));
            MimeMessage message = new MimeMessage(session);
            Address fromAddress = new InternetAddress(from);
            Address toAddress = new InternetAddress("savvas.a.michael@gmail.com");

            message.setFrom(fromAddress);
            message.setRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject("Testing JavaMail");
            message.setText("Welcome to JavaMail");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            message.saveChanges();
            Transport.send(message);
            transport.close();

        } catch (Exception ex) {
        }
    }

    public Long createUser(RegistrationRequest registrationRequest) {
        System.out.println("Registering User: " + registrationRequest);
        User user = new User(registrationRequest.getEmail(),registrationRequest.getName(),registrationRequest.getPassword());
        User savedUser = repository.save(user);
        return savedUser.getId();
    }

    public User getUser(Long id) {
        return repository.findOne(id);
    }
}
