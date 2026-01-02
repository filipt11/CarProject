package com.example.CarProject.controllers;


import com.example.CarProject.dto.MyUserDto;
import com.example.CarProject.dto.MyUserUpdateDto;
import com.example.CarProject.entities.MyUser;
import com.example.CarProject.exceptions.UserNotFoundException;
import com.example.CarProject.repositories.MyUserRepository;
import com.example.CarProject.services.MyUserService;
import com.example.CarProject.utils.MyUserConverter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class MyUserController {

    private final MyUserService myUserService;
    private final MyUserRepository myUserRepository;

    public MyUserController(MyUserService myUserService, MyUserRepository myUserRepository) {
        this.myUserService = myUserService;
        this.myUserRepository = myUserRepository;
    }

    @GetMapping("/administration/userList")
    public String userList(@RequestParam(defaultValue = "0") int page, @RequestParam (defaultValue = "10") int size, Model model){

        Page<MyUser> result = myUserService.selectPaging(page,size);
        long totalElements = result.getTotalElements();
        int startItem = (page * size) + 1;
        int endItem = Math.min(startItem + size - 1, (int) totalElements);

        model.addAttribute("list", result);
        model.addAttribute("numbers", myUserService.createPageNumbers(page, result.getTotalPages()));
        model.addAttribute("size",size);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("startItem", startItem);
        model.addAttribute("endItem", endItem);

        return "userList";
    }

    @GetMapping("/administration/userUpdate/{id}")
    public String userUpdate(@PathVariable Long id, Model model){
        MyUser user = myUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        MyUserUpdateDto dto = new MyUserUpdateDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setBanned(user.isBanned());

        model.addAttribute("userDto",dto);

        return "userUpdate";
    }

    @PostMapping("/administration/userUpdate/process")
    public String userUpdateProcess(@Valid @ModelAttribute("userDto") MyUserUpdateDto myUserUpdateDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){

            return "userUpdate";
        }
        myUserService.updateUser(myUserUpdateDto);

        return "redirect:/administration/userList";
    }

    @GetMapping("/administration/userDelete/{id}")
    public String userDelete(@PathVariable Long id){
        myUserService.deleteUser(id);

        return "redirect:/administration/userList";
    }

    @GetMapping("/administration/userPromote/{id}")
    public String userPromote(@PathVariable Long id){
        myUserService.promoteUser(id);

        return "redirect:/administration/userList";
    }

    @GetMapping("/administration/userBan/{id}")
    public String userBan(@PathVariable Long id){
        myUserService.banUser(id);

        return "redirect:/administration/userList";
    }

    @GetMapping("/administration/userUnBan/{id}")
    public String userUnBan(@PathVariable Long id){
        myUserService.unBanUser(id);

        return "redirect:/administration/userList";
    }



    @GetMapping("/userExport")
    public void userExport(HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=users.csv");

        String csv = myUserService.generateCsv();

        try (PrintWriter writer = response.getWriter()) {
            writer.write(csv);
        }
    }

}
