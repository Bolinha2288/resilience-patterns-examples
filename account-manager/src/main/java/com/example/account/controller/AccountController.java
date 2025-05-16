package com.example.account.controller;

import com.example.account.dto.ResponseDTO;
import com.example.account.dto.UserDTO;
import com.example.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createAccount(@Valid @RequestBody UserDTO userDTO) {
        ResponseDTO responseDTO = this.accountService.createAccount(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
