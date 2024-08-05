package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Role;
import com.gyulaieric.ECommerceStore.model.User;
import com.gyulaieric.ECommerceStore.repository.RoleRepository;
import com.gyulaieric.ECommerceStore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        authenticationService = new AuthenticationService(userRepository, roleRepository, passwordEncoder, authenticationManager, tokenService);
    }

    @Test
    public void registerUser() {
        String username = "testuser";
        String email = "test@example.com";
        String password = "testpassword";

        Mockito.when(userRepository.existsByUsername(username)).thenReturn(false);
        Mockito.when(roleRepository.findByAuthority("USER")).thenReturn(Optional.of(new Role("USER")));

        authenticationService.registerUser(username, email, password);

        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    public void registerUser_ExistentUser_ShouldThrowIllegalStateException() {
        String username = "testuser";
        String email = "test@example.com";
        String password = "testpassword";

        Mockito.when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> authenticationService.registerUser(username, email, password));
    }

    @Test
    public void loginUser_ShouldReturnUserIdAndJwt() {
        String username = "testuser";
        String password = "testpassword";
        User user = new User(1L, "test@example.com", username, password, new HashSet<>());

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);

        String jwtToken = "mockJwtToken";
        Mockito.when(tokenService.generateJwt(Mockito.any())).thenReturn(jwtToken);

        var result = authenticationService.loginUser(username, password);

        assertEquals(2, result.size());
        assertEquals("1", result.get("id"));
        assertEquals(jwtToken, result.get("jwt"));
    }

    @Test
    public void loginUser_BadCredentials_ShouldThrowIllegalStateException() {
        String username = "testuser";
        String password = "wrongPassword";

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Bad credentials") {});

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> authenticationService.loginUser(username, password));

        assertEquals("Bad credentials", exception.getMessage());
    }
}
