package org.savvas.milked.service;

import org.savvas.milked.controller.request.RegistrationRequest;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkedUserRepository;
import org.savvas.milked.domain.MilkingGroup;
import org.savvas.milked.domain.MilkingGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
public class UserService {
    private MilkedUserRepository milkedUserRepository;
    private MilkingGroupRepository milkingGroupRepository;

    @Autowired
    public UserService(MilkedUserRepository repository, MilkingGroupRepository milkingGroupRepository) {
        this.milkedUserRepository = repository;
        this.milkingGroupRepository = milkingGroupRepository;
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
            Address toAddress = new InternetAddress(getUser(1l).getEmail());
            message.setFrom(fromAddress);
            message.setRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject("Welcome to milked!");
            message.setText("In order to activate your account please follow localhost:8080/activation/" + getUser(1l).getUuid());
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            message.saveChanges();
            Transport.send(message);
            transport.close();
        } catch (Exception ex) {
        }
    }

    public MilkedUser createUser(RegistrationRequest registrationRequest) {
        String uuid = UUID.randomUUID().toString();
        MilkedUser milkedUser = new MilkedUser(registrationRequest.getEmail(), registrationRequest.getName(), registrationRequest.getPassword(), uuid);
        MilkedUser savedMilkedUser = milkedUserRepository.save(milkedUser);
        return savedMilkedUser;
    }
    public MilkedUser getUser(Long id) {
        return milkedUserRepository.findOne(id);
    }

    public boolean userExists(String email) {
        return milkedUserRepository.findByEmail(email) != null;
    }

    public List<MilkingGroup> getUserGroups(Long userId) {
        MilkedUser user = milkedUserRepository.findOne(userId);
        List<MilkingGroup> groupsContianingUserId = milkingGroupRepository.findGroupsContianingUserId(user);
        return groupsContianingUserId;
    }
}
