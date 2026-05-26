package com.portafolio.zomtg.salesflow;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.auth.dto.AuthResponseDTO;
import com.portafolio.zomtg.salesflow.auth.dto.LoginDTO;
import com.portafolio.zomtg.salesflow.users.entity.User;
import com.portafolio.zomtg.salesflow.users.enums.Role;
import com.portafolio.zomtg.salesflow.auth.repository.AuthRepository;
import com.portafolio.zomtg.salesflow.auth.repository.AuthRepositoryClients;
import com.portafolio.zomtg.salesflow.security.service.JWTService;
import com.portafolio.zomtg.salesflow.auth.service.AuthService;
import com.portafolio.zomtg.salesflow.users.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {
    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String username;
    String password;
    AuthService authService;
    AuthRepository  authRepository;
    User user;
    UserMapper userMapper;
    JWTService jwtService;
    AuthRepositoryClients authRepositoryClients;

    @BeforeEach
    void setUp() {
        username="admin";
        password="admin123";
        password=passwordEncoder.encode(password);

        authRepository = mock(AuthRepository.class);
        jwtService = mock(JWTService.class);
        authRepositoryClients = mock(AuthRepositoryClients.class);
        user = mock(User.class);
        userMapper=new UserMapper();
        authService = new AuthService(authRepository,jwtService,authRepositoryClients,userMapper);

        UUID uuid = UUID.fromString("17ac43b7-16eb-4bb6-afc5-f7e2d56d3b4c");
        when(user.getOwnerId()).thenReturn(uuid);
        when(user.getUsername()).thenReturn(username);
        when(user.getPassword()).thenReturn(password);
        when(user.getRole()).thenReturn(Role.OWNER);

        when(authRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));




    }
    @Test
    public void shouldReturnTokenAndUser_whenCredentialsAreValid(){

        when(jwtService.getToken(user)).thenReturn("jwt-token");

        AuthResponseDTO response = authService.loginCredentials(new LoginDTO("admin","admin123"));

        assertEquals("jwt-token", response.getToken());
        assertEquals("admin", response.getUser().username());
    }
    @Test
    public void shouldThrowException_whenUserDoesNotExist(){

        when(authRepository.findByUsername("admin"))
                .thenReturn(Optional.empty());

        assertThrows(InvalidCredentials.class, () ->
                authService.loginCredentials(new LoginDTO("admin","admin123"))
        );
    }
    @Test
    public void shouldThrowException_whenPasswordIsInvalid(){

        when(authRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        assertThrows(InvalidCredentials.class, () ->
                authService.loginCredentials(new LoginDTO("admin","wrongpass"))
        );
    }
    @Test
    public void shouldExtractUsernameFromToken(){

        JWTService jwtService = new JWTService();

        String token = jwtService.getToken(user);

        String username = jwtService.extractUsername(token);

        assertEquals("admin", username);
    }
    @Test
    public void shouldCallJwtService_whenLoginIsSuccessful(){
        when(jwtService.getToken(user)).thenReturn("jwt-token");
        authService.loginCredentials(new LoginDTO("admin","admin123"));
        verify(jwtService).getToken(user);
    }

//    @Test
//    public void shouldReturnUser_whenCredentialsAreCorrect() {
//        Optional<User> result=authService.loginCredentials(new LoginDTO("admin","admin123"));
//        verify(authRepository).findByUsername("admin");
//
//        assertTrue(result.isPresent());
//        assertEquals(username, result.get().getUsername());
//    }
//    @Test
//    public void shouldReturnUser_whenCredentialsAreIncorrect() {
//        Optional<User> result=authService.loginCredentials(new LoginDTO("admin2","admin123"));
//
//
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    public void shouldReturnUser_whenPasswordIsCorrect() {
//        Optional<User> result=authService.loginCredentials(new LoginDTO("admin","admin123"));
//        verify(authRepository).findByUsername("admin");
//        assertTrue(result.isPresent());
//        assertEquals(password, result.get().getPassword());
//    }
//    @Test
//    public void shouldReturnUser_whenPasswordIsIncorrect() {
//        Optional<User> result=authService.loginCredentials(new LoginDTO("admin2","admin121"));
//        assertTrue(result.isEmpty());
//
//    }
//    @Test
//    public void shouldGenerateJwtToken_whenCredentialsAreValid(){
//        Optional<User> result=authService.loginCredentials(new LoginDTO("admin","admin123"));
//        verify(authRepository).findByUsername("admin");
//            String token=jwtService.getToken(user);
//
//
//
//    }



}
