package com.example.CarProject.services;


import com.example.CarProject.dto.MyUserDto;
import com.example.CarProject.entities.MyUser;
import com.example.CarProject.exceptions.PasswordsNotMatchingException;
import com.example.CarProject.exceptions.TakenEmailException;
import com.example.CarProject.exceptions.TakenUsernameException;
import com.example.CarProject.repositories.MyUserRepository;
import com.example.CarProject.utils.MyUserConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private MyUserRepository myUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MyUserConverter myUserConverter;


    private MyUserDto myUserDto;

    @BeforeEach
    public void init(){
        myUserDto = new MyUserDto();
        myUserDto.setUsername("test123");
        myUserDto.setEmail("test123@example.com");
        myUserDto.setPassword("1238");
    }

    @Test
    void shouldThrowExceptionWhenUsernameTaken(){
        when(myUserRepository.existsByUsername(myUserDto.getUsername())).thenReturn(true);

        assertThatThrownBy(() -> registrationService.saveUser(myUserDto)).isInstanceOf(TakenUsernameException.class);
    }

    @Test
    void shouldThrowExceptionWhenEmailTaken(){
        when(myUserRepository.existsByEmail(myUserDto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> registrationService.saveUser(myUserDto)).isInstanceOf(TakenEmailException.class);
    }

    @Test
    void shouldThrowExceptionWhenPasswordsNotMatching(){
        myUserDto.setPassword2("12348");

        assertThatThrownBy(() -> registrationService.saveUser(myUserDto)).isInstanceOf(PasswordsNotMatchingException.class);
    }

    @Test
    void shouldCreateNewUser(){
        MyUser myUser = new MyUser();
        myUserDto.setPassword2("1238");

        when(myUserRepository.existsByUsername("test123")).thenReturn(false);
        when(myUserRepository.existsByEmail("test123@example.com")).thenReturn(false);
        when(passwordEncoder.encode("1238")).thenReturn("encodedPass");
        when(myUserConverter.toEntity(myUserDto)).thenReturn(myUser);

        registrationService.saveUser(myUserDto);

        Assertions.assertEquals("encodedPass",myUserDto.getPassword());
        verify(myUserRepository).save(myUser);

    }


}
