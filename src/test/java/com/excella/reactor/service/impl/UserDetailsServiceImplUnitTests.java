package com.excella.reactor.service.impl;

import com.excella.reactor.domain.User;
import com.excella.reactor.repositories.EmployeeRepository;
import com.excella.reactor.repositories.UserRepository;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;


public class UserDetailsServiceImplUnitTests {
    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeClass
    private void beforeAll() {
        user = new User();
        user.setUsername("user");
        user.setPassword("123");
    }
    @BeforeMethod
    private void beforeEach() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test(description = "Should successfully load username")
    public void loadUsernameSuccess() {
        Mockito.when(userRepository.findByUsername("user"))
            .thenReturn(Optional.of(user));
        var testUser = userDetailsService.loadUserByUsername("user");
        assert testUser.equals(user);
    }

    @Test(description = "Exception should be thrown if username cannot be found",
        expectedExceptions = UsernameNotFoundException.class)
    public void loadUsernameFailure() {
        Mockito.when(userRepository.findByUsername("user"))
            .thenReturn(Optional.empty());
        userDetailsService.loadUserByUsername("user");
    }

}
