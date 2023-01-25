package com.messenger.service;

import com.messenger.config.JwtUtil;
import com.messenger.config.SecureUser;
import com.messenger.exp.BadRequestException;
import com.messenger.model.dto.auth.LoginRequestDTO;
import com.messenger.model.dto.auth.RegisterRequestDTO;
import com.messenger.model.entity.RoleEntity;
import com.messenger.model.entity.UserEntity;
import com.messenger.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    /**
     * register the user and save it
     * do log in with user and generate jwt token
     */


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    @Transactional
    public String doRegister(RegisterRequestDTO registerDTO) {
        Optional<UserEntity> find = userRepository.findByUsername(registerDTO.getUsername());

        if (find.isPresent()) {
            throw new BadRequestException("User already registered");
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        user.setName(registerDTO.getName());

        RoleEntity basicRole = new RoleEntity();
        basicRole.setRole("ROLE_USER");
        basicRole.setUserEntity(user);

        user.getUserRoles().add(basicRole);
        userRepository.save(user);

        return "registered";
    }


    public String doLogin(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SecureUser userDetails = (SecureUser) authentication.getPrincipal();
        return JwtUtil.generateJwtToken(userDetails.getUsername());
    }


}
