package com.messenger.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserUpdateProfile {

    private String name;
    private String phoneNumber;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
