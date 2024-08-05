package com.gyulaieric.ECommerceStore.service;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private TokenService tokenService;

    @Test
    public void testGenerateJwt() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Jwt mockJwt = Mockito.mock(Jwt.class);
        Mockito.when(authentication.getName()).thenReturn("testuser");
        Mockito.when(mockJwt.getClaimAsString("authorities")).thenReturn("ROLE_USER");

        Instant now = Instant.now();
        Mockito.when(mockJwt.getSubject()).thenReturn("testuser");
        Mockito.when(mockJwt.getClaimAsString("authorities")).thenReturn("ROLE_USER");
        Mockito.when(jwtEncoder.encode(Mockito.any())).thenReturn(mockJwt);

        String jwtToken = tokenService.generateJwt(authentication);

        Mockito.verify(jwtEncoder, Mockito.times(1)).encode(Mockito.any());

        assertEquals("testuser", mockJwt.getSubject());
        assertEquals("ROLE_USER", mockJwt.getClaimAsString("authorities"));
    }
}
