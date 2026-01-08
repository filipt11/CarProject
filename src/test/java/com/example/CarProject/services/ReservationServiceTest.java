package com.example.CarProject.services;


import com.example.CarProject.dto.ReservationDto;
import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.MyUser;
import com.example.CarProject.entities.Reservation;
import com.example.CarProject.exceptions.CarNotFoundException;
import com.example.CarProject.exceptions.InvalidReservationDateException;
import com.example.CarProject.exceptions.ReservationNotFoundException;
import com.example.CarProject.repositories.CarRepository;
import com.example.CarProject.repositories.MyUserRepository;
import com.example.CarProject.repositories.ReservationRepository;
import com.example.CarProject.security.SignedUserDetails;
import com.example.CarProject.converters.ReservationConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private MyUserRepository myUserRepository;

    @Mock
    private ReservationConverter reservationConverter;

    @Mock
    private SignedUserDetails signedUserDetails;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;


    private ReservationDto reservationDto;
    private Reservation reservation;
    private Car car;
    private MyUser myUser;

    @BeforeEach
    void init(){
        reservation = new Reservation();
        reservationDto = new ReservationDto();
        car = new Car();
        myUser = new MyUser();
        reservationDto.setId(10L);
        reservationDto.setCarId(10L);
        reservationDto.setStartDate(LocalDate.now());
        reservationDto.setEndDate(LocalDate.now().plusDays(1));

        reservation.setId(10L);
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusDays(1));
        reservation.setCar(car);

        car.setId(10L);
        car.setBrand("Bmw");
        car.setModel("E92");

        myUser.setId(10L);
    }

    @Test
    void shouldThrowExceptionWhenDateIsInvalid(){
        reservationDto.setStartDate(LocalDate.now().plusDays(1));
        reservationDto.setEndDate(LocalDate.now());

        assertThatThrownBy(()->reservationService.saveReservation(reservationDto)).isInstanceOf(InvalidReservationDateException.class);
    }

    @Test
    void shouldThrowExceptionWhenCarNotFoundDuringSave(){
        when(signedUserDetails.getId()).thenReturn(1L);
        when(authentication.getPrincipal()).thenReturn(signedUserDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(carRepository.findById(reservationDto.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(()-> reservationService.saveReservation(reservationDto)).isInstanceOf(CarNotFoundException.class);
    }

    @Test
    void shouldSaveReservation(){
        when(signedUserDetails.getId()).thenReturn(10L);
        when(authentication.getPrincipal()).thenReturn(signedUserDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        when(myUserRepository.findById(myUser.getId())).thenReturn(Optional.of(myUser));
        when(reservationConverter.toEntity(reservationDto)).thenReturn(reservation);

        reservationService.saveReservation(reservationDto);

        verify(reservationRepository).save(reservation);
        Assertions.assertEquals(car, reservation.getCar());
        Assertions.assertEquals(myUser, reservation.getUser());

    }

    @Test void shouldUseStartDateAscendingSort() {
        Pageable expected = PageRequest.of(0, 10, Sort.by("startDate").ascending());
        reservationService.selectPaging(0, 10, "start-asc");

        verify(reservationRepository).findAll(expected);
    }

    @Test void shouldUseStartDateDescSort() {
        Pageable expected = PageRequest.of(0, 10, Sort.by("startDate").descending());
        reservationService.selectPaging(0, 10, "start-desc");

        verify(reservationRepository).findAll(expected);
    }

    @Test void shouldUseEndDateAscSort() {
        Pageable expected = PageRequest.of(0, 10, Sort.by("endDate").ascending());
        reservationService.selectPaging(0, 10, "end-asc");

        verify(reservationRepository).findAll(expected);
    }

    @Test void shouldUseEndDateDescSort() {
        Pageable expected = PageRequest.of(0, 10, Sort.by("endDate").descending());
        reservationService.selectPaging(0, 10, "end-desc");

        verify(reservationRepository).findAll(expected);
    }

    @Test
    void testListNumbersReturn0From0() {
        List<Integer> numbers = reservationService.createPageNumbers(0,0);
        assertThat(numbers).isEmpty();
    }

    @Test
    void testListNumbersReturn0From3() {
        List<Integer> numbers = reservationService.createPageNumbers(0,3);
        List<Integer> expected = List.of(0,1,2);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn1From1() {
        List<Integer> numbers = reservationService.createPageNumbers(1,1);
        List<Integer> expected = List.of(0);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn0From4() {
        List<Integer> numbers = reservationService.createPageNumbers(0,4);
        List<Integer> expected = List.of(0,1,2,3);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn0From19() {
        List<Integer> numbers = reservationService.createPageNumbers(0,19);
        List<Integer> expected = List.of(0,1,-1,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn1From19() {
        List<Integer> numbers = reservationService.createPageNumbers(1,19);
        List<Integer> expected = List.of(0,1,-1,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn18From19() {
        List<Integer> numbers = reservationService.createPageNumbers(18,19);
        List<Integer> expected = List.of(0,-1,16,17,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn6From19() {
        List<Integer> numbers = reservationService.createPageNumbers(6,19);
        List<Integer> expected = List.of(0,-1,6,-1,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn2From19() {
        List<Integer> numbers = reservationService.createPageNumbers(2,19);
        List<Integer> expected = List.of(0,1,2,-1,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn16From19() {
        List<Integer> numbers = reservationService.createPageNumbers(16,19);
        List<Integer> expected = List.of(0,-1,16,17,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void shouldGenerateCsvHeader(){
        String csv = reservationService.generateCsv();

        assertThat(csv).startsWith("ID,Car ID,Car Brand,Car Model,Start Date,End Date\n");
    }

    @Test
    void shouldGenerateCsvWithUserData(){

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        when(reservationService.selectAll()).thenReturn(reservations);

        String csv = reservationService.generateCsv();
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);


        assertThat(csv).contains("10,10," + car.getBrand() + "," + car.getModel() + ","+start + "," + end + "\n");
    }

    @Test
    void shouldGenerateCsvWithMultipleUsers(){
        Reservation reservation1 = new Reservation(20L,LocalDate.now().plusDays(20),LocalDate.now().plusDays(30),myUser,car);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        reservations.add(reservation1);
        when(reservationService.selectAll()).thenReturn(reservations);

        String csv = reservationService.generateCsv();

        assertThat(csv.split("\n")).hasSize(3);
    }

    @Test
    void shouldThrowExceptionWhenReservationNotFoundDuringUpdate(){
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(()-> reservationService.updateReservation(reservationDto)).isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenInvalidDateProvidedDuringUpdate(){
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        reservationDto.setStartDate(LocalDate.now().plusDays(1));
        reservationDto.setEndDate(LocalDate.now());

        assertThatThrownBy(()-> reservationService.updateReservation(reservationDto)).isInstanceOf(InvalidReservationDateException.class);
    }

    @Test
    void shouldUpdateReservation(){
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        reservationDto.setStartDate(LocalDate.now().plusDays(2));
        reservationDto.setEndDate(LocalDate.now().plusDays(3));

        reservationService.updateReservation(reservationDto);

        Assertions.assertEquals(LocalDate.now().plusDays(2),reservation.getStartDate());
        Assertions.assertEquals(LocalDate.now().plusDays(3),reservation.getEndDate());
    }

    @Test
    void shouldThrowExceptionWhenReservationNotFoundDuringDelete(){
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(()-> reservationService.deleteReservation(reservation.getId())).isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void shouldDeleteReservation(){
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        reservationService.deleteReservation(reservation.getId());

        verify(reservationRepository).delete(reservation);

    }

}