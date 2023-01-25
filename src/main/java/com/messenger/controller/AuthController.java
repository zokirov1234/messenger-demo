package com.messenger.controller;

import com.messenger.model.dto.auth.LoginRequestDTO;
import com.messenger.model.dto.auth.RegisterRequestDTO;
import com.messenger.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> doRegister(@RequestBody RegisterRequestDTO registerRequestDTO) {
        String response = authService.doRegister(registerRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody LoginRequestDTO loginRequestDTO) {
        String token = authService.doLogin(loginRequestDTO);
        return ResponseEntity.ok(token);
    }
}
