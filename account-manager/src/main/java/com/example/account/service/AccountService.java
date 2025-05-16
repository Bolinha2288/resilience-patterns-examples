package com.example.account.service;

import com.example.account.domain.model.Account;
import com.example.account.domain.repository.AccountRepository;
import com.example.account.dto.ResponseDTO;
import com.example.account.dto.UserDTO;
import com.example.account.external.EmailClient;
import com.example.account.utils.AccountNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountNumber accountNumber;
    private final EmailClient emailClient;

    public AccountService(AccountRepository accountRepository, AccountNumber accountNumber, EmailClient emailClient) {
        this.accountRepository = accountRepository;
        this.accountNumber = accountNumber;
        this.emailClient = emailClient;
    }

    public ResponseDTO createAccount(UserDTO userDTO) {
        log.info("User event received in account-manager service {}", userDTO);

        Account account = new Account();
        account.setUserReference(userDTO.getIdReference());
        account.setNumberAccount(accountNumber.generate(userDTO.getIdReference()));
        account.setName(userDTO.getName());

        accountRepository.save(account);

        emailClient.sendWelcomeEmail(userDTO);

        log.info("Account created successfully {}", account);
        return new ResponseDTO("Account created", List.of(account));
    }

}
