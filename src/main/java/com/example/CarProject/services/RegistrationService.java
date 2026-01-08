package com.example.CarProject.services;


import com.example.CarProject.dto.MyUserDto;
import com.example.CarProject.entities.MyUser;
import com.example.CarProject.exceptions.PasswordsNotMatchingException;
import com.example.CarProject.exceptions.TakenEmailException;
import com.example.CarProject.exceptions.TakenUsernameException;
import com.example.CarProject.repositories.MyUserRepository;
import com.example.CarProject.converters.MyUserConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final MyUserRepository myUserRepository;
    private final MyUserConverter myUserConverter;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(MyUserRepository myUserRepository, MyUserConverter myUserConverter, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.myUserConverter = myUserConverter;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(MyUserDto dto){

        if(myUserRepository.existsByUsername(dto.getUsername())){
            throw new TakenUsernameException();
        }

        if(myUserRepository.existsByEmail(dto.getEmail())){
            throw new TakenEmailException();
        }

        String pass1 = dto.getPassword();
        String pass2 = dto.getPassword2();

        if(!pass1.equals(pass2)){
            throw new PasswordsNotMatchingException();
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        dto.setRole("ROLE_USER");
        MyUser user = myUserConverter.toEntity(dto);

        myUserRepository.save(user);
    }

}
