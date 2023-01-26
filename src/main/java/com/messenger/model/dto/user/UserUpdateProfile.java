package com.messenger.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserUpdateProfile {

    private String name;
    private String phoneNumber;
    private String username;
    private String password;
}
