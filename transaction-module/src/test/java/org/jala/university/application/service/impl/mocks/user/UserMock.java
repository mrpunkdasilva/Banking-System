package org.jala.university.application.service.impl.mocks.user;

import org.jala.university.commons.domain.Role;
import org.jala.university.domain.entity.User;

public class UserMock {
    public static User createAValidUser() {
        return User.builder()
                .id(1L)
                .email("test@test.com")
                .name("Test User")
                .roles(Role.USER)
                .build();
    }
}
