package com.example.CarProject.utils;


import com.example.CarProject.dto.MyUserDto;
import com.example.CarProject.entities.MyUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MyUserConverterTest {

    @InjectMocks
    private MyUserConverter myUserConverter;

    private MyUserDto myUserDto;

    @Test
    void shouldReturnNullWhenDtoIsNull(){
        Assertions.assertNull(myUserConverter.toEntity(null));
    }

    @Test
    void shouldReturnUser(){

        myUserDto = new MyUserDto();
        myUserDto.setUsername("test");
        myUserDto.setEmail("email@example.com");
        myUserDto.setPassword("1234");
        myUserDto.setRole("ROLE_USER");

        MyUser myUser = myUserConverter.toEntity(myUserDto);

        Assertions.assertEquals("test",myUser.getUsername());
        Assertions.assertEquals("email@example.com",myUser.getEmail());
        Assertions.assertEquals("1234",myUser.getPassword());
        Assertions.assertEquals("ROLE_USER",myUser.getRole());
    }

}
