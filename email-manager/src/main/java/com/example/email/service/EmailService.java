package com.example.email.service;

import com.example.email.dto.ResponseDTO;
import com.example.email.dto.UserDTO;
import com.example.email.utils.DispatchMail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmailService {

    private final DispatchMail dispatchMail;

    public EmailService(DispatchMail dispatchMail) {
        this.dispatchMail = dispatchMail;
    }


    public ResponseDTO sendWelcomeEMail(UserDTO userDTO){
        log.info("User event received in email-manager service {}", userDTO);

        sendMessageEmail(userDTO);

        return new ResponseDTO("Email sent", List.of(userDTO));

    }

    private void sendMessageEmail(UserDTO userDTO) {
        String subject = "Welcome!";
        String templateName = "welcome-user";

        Map<String, Object> model = Map.of(
                "userName", userDTO.getName(),
                "greeting", "Hellow, how are you?",
                "subject", subject
        );

        dispatchMail.sendHtmlEmail(userDTO.getEmail(), subject, templateName, model);
    }

}
