package com.example.CarProject.services;


import com.example.CarProject.dto.MyUserUpdateDto;
import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.MyUser;
import com.example.CarProject.exceptions.UserNotFoundException;
import com.example.CarProject.repositories.MyUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyUserServiceTest {

    @InjectMocks
    MyUserService myUserService;

    @Mock
    MyUserRepository myUserRepository;

    @Test
    void testListNumbersReturn0From0() {
        List<Integer> numbers = myUserService.createPageNumbers(0,0);
        assertThat(numbers).isEmpty();
    }

    @Test
    void testListNumbersReturn0From3() {
        List<Integer> numbers = myUserService.createPageNumbers(0,3);
        List<Integer> expected = List.of(0,1,2);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn1From1() {
        List<Integer> numbers = myUserService.createPageNumbers(1,1);
        List<Integer> expected = List.of(0);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn0From4() {
        List<Integer> numbers = myUserService.createPageNumbers(0,4);
        List<Integer> expected = List.of(0,1,2,3);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn0From19() {
        List<Integer> numbers = myUserService.createPageNumbers(0,19);
        List<Integer> expected = List.of(0,1,-1,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn1From19() {
        List<Integer> numbers = myUserService.createPageNumbers(1,19);
        List<Integer> expected = List.of(0,1,-1,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn18From19() {
        List<Integer> numbers = myUserService.createPageNumbers(18,19);
        List<Integer> expected = List.of(0,-1,16,17,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn6From19() {
        List<Integer> numbers = myUserService.createPageNumbers(6,19);
        List<Integer> expected = List.of(0,-1,6,-1,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn2From19() {
        List<Integer> numbers = myUserService.createPageNumbers(2,19);
        List<Integer> expected = List.of(0,1,2,-1,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn16From19() {
        List<Integer> numbers = myUserService.createPageNumbers(16,19);
        List<Integer> expected = List.of(0,-1,16,17,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void shouldUpdateUserEmail(){
        MyUserUpdateDto dto = new MyUserUpdateDto();
        MyUser myUser = new MyUser();
        myUser.setEmail("old.email@example.com");
        dto.setId(10L);
        dto.setEmail("new.email@example.com");

        when(myUserRepository.findById(dto.getId())).thenReturn(Optional.of(myUser));
        myUserService.updateUser(dto);

        Assertions.assertEquals("new.email@example.com",myUser.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundDuringUpdate(){
        MyUserUpdateDto dto = new MyUserUpdateDto();
        dto.setId(10L);

        when(myUserRepository.findById(dto.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> myUserService.updateUser(dto)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldDeleteUser(){
        MyUser myUser = new MyUser();
        Long id = 10L;
        myUser.setId(id);

        when(myUserRepository.findById(id)).thenReturn(Optional.of(myUser));
        myUserService.deleteUser(id);

        verify(myUserRepository).delete(myUser);

    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundDuringDelete(){
        when(myUserRepository.findById(99999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> myUserService.deleteUser(99999L)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldPromoteUser(){
        MyUser myUser = new MyUser();
        Long id = 10L;
        myUser.setId(id);
        myUser.setRole("ROLE_USER");

        when(myUserRepository.findById(id)).thenReturn(Optional.of(myUser));
        myUserService.promoteUser(id);

        Assertions.assertEquals("ROLE_ADMIN",myUser.getRole());
    }

    @Test
    void shouldThrowExceptionWhereUserNotFoundDuringPromotion(){
        when(myUserRepository.findById(99999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->myUserService.promoteUser(99999L)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldBanUser(){
        MyUser myUser = new MyUser();
        Long id = 10L;
        myUser.setId(id);
        myUser.setBanned(false);

        when(myUserRepository.findById(id)).thenReturn(Optional.of(myUser));
        myUserService.banUser(id);

        Assertions.assertTrue(myUser.isBanned());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundDuringBan(){
        when(myUserRepository.findById(99999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> myUserService.banUser(99999L)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldUnbanUser(){
        MyUser myUser = new MyUser();
        Long id = 10L;
        myUser.setId(id);
        myUser.setBanned(true);

        when(myUserRepository.findById(id)).thenReturn(Optional.of(myUser));
        myUserService.unBanUser(id);

        Assertions.assertFalse(myUser.isBanned());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundDuringUnBan(){
        when(myUserRepository.findById(99999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> myUserService.unBanUser(99999L)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldGenerateCsvHeader(){
        String csv = myUserService.generateCsv();

        assertThat(csv).startsWith("ID,Username,Email,Role\n");
    }

    @Test
    void shouldGenerateCsvWithUserData(){
        MyUser myUser = new MyUser();
        myUser.setId(1L);
        myUser.setUsername("admin");
        myUser.setEmail("email@example.com");
        myUser.setRole("ROLE_ADMIN");

        List<MyUser> users = new ArrayList<>();
        users.add(myUser);
        when(myUserService.selectAll()).thenReturn(users);

        String csv = myUserService.generateCsv();

        assertThat(csv).contains("1,admin,email@example.com,ROLE_ADMIN");
    }

    @Test
    void shouldGenerateCsvWithMultipleUsers(){
        MyUser myUser = new MyUser();
        myUser.setId(1L);
        myUser.setUsername("admin");
        myUser.setEmail("email@example.com");
        myUser.setRole("ROLE_ADMIN");

        MyUser myUser2 = new MyUser();
        myUser2.setId(1L);
        myUser2.setUsername("test");
        myUser2.setEmail("test@example.com");
        myUser2.setRole("ROLE_USER");

        List<MyUser> users = new ArrayList<>();
        users.add(myUser);
        users.add(myUser2);
        when(myUserService.selectAll()).thenReturn(users);

        String csv = myUserService.generateCsv();

        assertThat(csv.split("\n")).hasSize(3);
    }

}
