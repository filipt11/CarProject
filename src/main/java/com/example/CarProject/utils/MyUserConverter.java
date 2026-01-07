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

}
