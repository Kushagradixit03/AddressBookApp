package com.example.addressBook;

import com.example.addressBook.dto.AuthUserDTO;
import com.example.addressBook.dto.LoginDTO;
import com.example.addressBook.model.AuthUser;
import com.example.addressBook.repository.AuthUserRepository;
import com.example.addressBook.service.AuthenticationService;
import com.example.addressBook.service.EmailService;
import com.example.addressBook.service.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private EmailService emailService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private AuthenticationService authenticationService;

    private AuthUser user;
    private AuthUserDTO userDTO;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        user = new AuthUser();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhone("1234567890");
        user.setHashPass("encodedPassword");

        userDTO = new AuthUserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setPhone("1234567890");
        userDTO.setPassword("password123");

        loginDTO = new LoginDTO();
        loginDTO.setEmail("john@example.com");
        loginDTO.setPassword("password123");
    }

    @Test
    void testRegisterUserSuccess() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(AuthUser.class))).thenReturn(user);

        String result = authenticationService.register(userDTO);

        assertEquals("User registered successfully!", result);
        verify(userRepository, times(1)).save(any(AuthUser.class));
    }

    @Test
    void testRegisterUserThrowsExceptionForDuplicateEmail() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authenticationService.register(userDTO));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("400 BAD_REQUEST \"Email is already in use\"", exception.getMessage());
    }

    @Test
    void testLoginSuccess() {
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getHashPass())).thenReturn(true);
        when(jwtTokenService.createToken(user.getId())).thenReturn("mockJwtToken");

        var response = authenticationService.login(loginDTO);

        assertEquals("Login successful", response.get("message"));
        assertTrue(response.containsKey("token"));
        verify(userRepository, times(1)).findByEmail(loginDTO.getEmail());
    }

    @Test
    void testLoginThrowsExceptionForInvalidCredentials() {
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getHashPass())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authenticationService.login(loginDTO));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("401 UNAUTHORIZED \"Invalid credentials\"", exception.getMessage());
    }

    @Test
    void testProcessForgotPasswordSuccess() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        authenticationService.processForgotPassword(user.getEmail());

        verify(emailService, times(1)).sendPasswordResetEmail(eq(user.getEmail()), anyString());
    }

    @Test
    void testProcessForgotPasswordThrowsExceptionForNonexistentUser() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authenticationService.processForgotPassword(user.getEmail()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"User not found\"", exception.getMessage());
    }

    @Test
    void testResetPasswordSuccess() {
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

        when(userRepository.findByResetToken(resetToken)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword");

        authenticationService.resetPassword(resetToken, "newPassword123");

        assertNull(user.getResetToken());
        assertNull(user.getResetTokenExpiry());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testResetPasswordThrowsExceptionForInvalidToken() {
        when(userRepository.findByResetToken("invalidToken")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authenticationService.resetPassword("invalidToken", "newPassword123"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Invalid or expired token\"", exception.getMessage());
    }

    @Test
    void testResetPasswordThrowsExceptionForExpiredToken() {
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().minusMinutes(5)); // Token expired

        when(userRepository.findByResetToken(resetToken)).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authenticationService.resetPassword(resetToken, "newPassword123"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("400 BAD_REQUEST \"Token expired\"", exception.getMessage());
    }
}
