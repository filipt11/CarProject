package com.example.CarProject.utils;

import com.example.CarProject.dto.MyUserDto;
import com.example.CarProject.entities.MyUser;
import org.springframework.stereotype.Component;

@Component
public class MyUserConverter {

    public MyUser toEntity(MyUserDto dto){
        if(dto==null){
            return null;
        }
        MyUser myUser = new MyUser();
        myUser.setUsername(dto.getUsername());
        myUser.setPassword(dto.getPassword());
        myUser.setEmail(dto.getEmail());
        myUser.setRole(dto.getRole());
        return myUser;
    }

    public MyUserDto toDto(MyUser myUser){
        if(myUser==null){
            return null;
        }
        MyUserDto dto = new MyUserDto();
        dto.setId(myUser.getId());
        dto.setUsername(myUser.getUsername());
        dto.setEmail(myUser.getEmail());
        dto.setRole(myUser.getRole());

        return dto;
    }


}
