package com.example.CarProject.services;


import com.example.CarProject.dto.MyUserUpdateDto;
import com.example.CarProject.entities.MyUser;
import com.example.CarProject.exceptions.UserNotFoundException;
import com.example.CarProject.repositories.MyUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserService {
    private final MyUserRepository myUserRepository;

    public MyUserService(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    public List<MyUser> selectAll(){
        return myUserRepository.findAll();
    }

    public Page<MyUser> selectPaging(int page, int size) {

        Pageable paging = PageRequest.of(page, size);

        return myUserRepository.findAll(paging);
    }

    public List<Integer> createPageNumbers(int current, int totalPages) {
        List<Integer> pageNumbers = new ArrayList<>();
        if (totalPages <= 0) return pageNumbers;

        if (totalPages <= 4) {
            for (int i = 0; i < totalPages; i++) pageNumbers.add(i);
            return pageNumbers;
        }

        pageNumbers.add(0);

        if (current <= 2) {
            pageNumbers.add(1);
            if (current == 2) {
                pageNumbers.add(2);
            }
            pageNumbers.add(-1);
            pageNumbers.add(totalPages - 1);
            return pageNumbers;
        }

        if (current >= totalPages - 3) {
            pageNumbers.add(-1);
            pageNumbers.add(totalPages - 3);
            pageNumbers.add(totalPages - 2);
            pageNumbers.add(totalPages - 1);
            return pageNumbers;
        }

        pageNumbers.add(-1);
        pageNumbers.add(current);
        pageNumbers.add(-1);
        pageNumbers.add(totalPages - 1);

        return pageNumbers;
    }

    @Transactional
    public void updateUser(MyUserUpdateDto dto){
        Long id = dto.getId();
        MyUser myUser = myUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        myUser.setEmail(dto.getEmail());
    }

    @Transactional
    public void deleteUser(Long id){
        MyUser myUser = myUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        myUserRepository.delete(myUser);
    }

    @Transactional
    public void promoteUser(Long id){
        MyUser myUser = myUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        myUser.setRole("ROLE_ADMIN");
    }

    @Transactional
    public void banUser(long id){
        MyUser myUser = myUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        myUser.setBanned(true);
    }

    @Transactional
    public void unBanUser(long id){
        MyUser myUser = myUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        myUser.setBanned(false);
    }

    public String generateCsv() {
        List<MyUser> users = selectAll();
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Username,Email,Role\n");
        for (MyUser user : users) {
            sb.append(user.getId()).append(",");
            sb.append(user.getUsername()).append(",");
            sb.append(user.getEmail()).append(",");
            sb.append(user.getRole()).append("\n");
        }

        return sb.toString();
    }

}
