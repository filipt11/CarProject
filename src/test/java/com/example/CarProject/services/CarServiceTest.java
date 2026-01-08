package com.example.CarProject.services;

import com.example.CarProject.dto.CarDto;
import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.MyUser;
import com.example.CarProject.exceptions.CarNotFoundException;
import com.example.CarProject.exceptions.UserNotFoundException;
import com.example.CarProject.repositories.CarRepository;
import com.example.CarProject.repositories.MyUserRepository;
import com.example.CarProject.security.SignedUserDetails;
import com.example.CarProject.converters.CarConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private MyUserRepository myUserRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private CarConverter carConverter;

    @Mock
    private CarRepository carRepository;

    @Mock
    private SignedUserDetails signedUserDetails;

    @Mock
    private SecurityContext securityContext;


    @Test
    void shouldSaveCarOwnedByCurrentlyLoginUser(){
        CarDto carDto = new CarDto();
        Car car = new Car();
        car.setEngineSize(1.5);

        MyUser myUser = new MyUser();

        myUser.setId(1L);
        when(carConverter.toEntity(carDto)).thenReturn(car);
        when(myUserRepository.findById(1L)).thenReturn(Optional.of(myUser));
        when(signedUserDetails.getId()).thenReturn(1L);
        when(authentication.getPrincipal()).thenReturn(signedUserDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        carService.saveCar(carDto);

        assertEquals(myUser,car.getUser());

        verify(carRepository).save(car);

    }

    @Test
    void shouldUseFindAllWhenBrandsListIsEmpty(){
        when(carRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        carService.selectPaging(0, 10, "no", List.of());

        verify(carRepository).findAll(any(Pageable.class));
    }

    @Test
    void shouldUseFindAllWhenNoBrands(){
        when(carRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        carService.selectPaging(0, 10, "no", null);

        verify(carRepository).findAll(any(Pageable.class));
    }

    @Test
    void shouldUseFindByBrandInWhenBrandsProvided(){
        List<String> brands = List.of("Bmw", "Audi");
        when(carRepository.findByBrandIn(anyList(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        carService.selectPaging(0, 10, "no", brands);

        verify(carRepository).findByBrandIn(anyList(), any(Pageable.class));
    }

    @Test
    void shouldReturnAllCarsWhenNoBrands() {
        Page<Car> page = new PageImpl<>(List.of(new Car()));
        when(carRepository.findAll(any(Pageable.class))).thenReturn(page);

        carService.selectedBrands(null);

        verify(carRepository).findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnFilteredCarsWhenBrandsProvided() {
        List<String> brands = List.of("Bmw","Audi");
        Page<Car> page = new PageImpl<>(List.of(new Car()));
        when(carRepository.findByBrandIn(anyList(), any(Pageable.class))).thenReturn(page);

        carService.selectedBrands(brands);

        verify(carRepository).findByBrandIn(anyList(), any(Pageable.class));
    }

    @Test
    void shouldReturnAllCarsWhenBrandsListIsEmpty() {
        Page<Car> page = new PageImpl<>(List.of());
        when(carRepository.findAll(any(Pageable.class))).thenReturn(page);

        carService.selectedBrands(List.of());

        verify(carRepository).findAll(any(Pageable.class));
    }

    @Test
    void testListNumbersReturn0From0() {
        List<Integer> numbers = carService.createPageNumbers(0,0);
        assertThat(numbers).isEmpty();
    }

    @Test
    void testListNumbersReturn0From3() {
        List<Integer> numbers = carService.createPageNumbers(0,3);
        List<Integer> expected = List.of(0,1,2);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn1From1() {
        List<Integer> numbers = carService.createPageNumbers(1,1);
        List<Integer> expected = List.of(0);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn0From4() {
        List<Integer> numbers = carService.createPageNumbers(0,4);
        List<Integer> expected = List.of(0,1,2,3);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn0From19() {
        List<Integer> numbers = carService.createPageNumbers(0,19);
        List<Integer> expected = List.of(0,1,-1,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn1From19() {
        List<Integer> numbers = carService.createPageNumbers(1,19);
        List<Integer> expected = List.of(0,1,-1,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn18From19() {
        List<Integer> numbers = carService.createPageNumbers(18,19);
        List<Integer> expected = List.of(0,-1,16,17,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn6From19() {
        List<Integer> numbers = carService.createPageNumbers(6,19);
        List<Integer> expected = List.of(0,-1,6,-1,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn2From19() {
        List<Integer> numbers = carService.createPageNumbers(2,19);
        List<Integer> expected = List.of(0,1,2,-1,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void testListNumbersReturn16From19() {
        List<Integer> numbers = carService.createPageNumbers(16,19);
        List<Integer> expected = List.of(0,-1,16,17,18);
        Assertions.assertEquals(expected,numbers);
    }

    @Test
    void shouldGenerateCsvHeader() {
        Page<Car> emptyPage = new PageImpl<>(List.of());
        when(carRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

        String csv = carService.generateCsv(null);

        assertThat(csv).startsWith("ID,Brand,Model,Year,EngineSize,HP\n");
    }

    @Test
    void shouldGenerateCsvWithCarData() {
        Car car = new Car(1,"Bmw", "E36", 2020, 2.4, 140);
        Page<Car> page = new PageImpl<>(List.of(car));
        when(carRepository.findAll(any(Pageable.class))).thenReturn(page);

        String csv = carService.generateCsv(null);

        assertThat(csv).contains("1,Bmw,E36,2020,2.4,140");
    }

    @Test
    void shouldGenerateCsvForMultipleCars() {
        Car car1 = new Car(1,"Bmw", "E36", 2020, 2, 140);
        Car car2 = new Car(2,"Audi", "RS7", 2025, 4, 520);
        Page<Car> page = new PageImpl<>(List.of(car1, car2));
        when(carRepository.findAll(any(Pageable.class))).thenReturn(page);

        String csv = carService.generateCsv(null);

        assertThat(csv.split("\n")).hasSize(3);
    }

    @Test
    void shouldUpdateCar(){
        Long carId = 1L;
        Long myUserId = 20L;

        Car car = new Car();
        car.setId(carId);
        car.setBrand("Bmw");
        car.setModel("E92");
        car.setProdYear(2008);
        car.setEngineSize(1.4);
        car.setHp(299);
        car.setDescription("old test description");
        car.setImage("example.png");
        car.setHighlighted(true);


        MyUser myUser = new MyUser();
        myUser.setId(myUserId);

        CarDto carDto = new CarDto();

        carDto.setId(carId);
        carDto.setBrand("Audi");
        carDto.setModel("Rs7");
        carDto.setProdYear(2017);
        carDto.setEngineSize(4.2);
        carDto.setHp(380);
        carDto.setDescription("new test description");
        carDto.setImage("example.png");
        carDto.setHighlighted(false);
        carDto.setUser(myUserId);

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(myUserRepository.findById(myUserId)).thenReturn(Optional.of(myUser));

        carService.updateEntity(carDto);

        Assertions.assertEquals("Audi",car.getBrand());
        Assertions.assertEquals("Rs7",car.getModel());
        Assertions.assertEquals(2017,car.getProdYear());
        Assertions.assertEquals(4.2,car.getEngineSize());
        Assertions.assertEquals(380,car.getHp());
        Assertions.assertEquals("new test description",car.getDescription());
        Assertions.assertEquals("example.png",car.getImage());
        Assertions.assertFalse(car.isHighlighted());
        Assertions.assertEquals(myUser,car.getUser());

    }

    @Test
    void shouldCapitalizeBrandAndModel(){
        Long carId = 1L;
        Long myUserId = 20L;

        Car car = new Car();
        car.setId(carId);
        car.setBrand("Bmw");
        car.setModel("M3");

        MyUser myUser = new MyUser();
        myUser.setId(myUserId);

        CarDto carDto = new CarDto();

        carDto.setId(carId);
        carDto.setBrand("ToyOtA");
        carDto.setModel("ceLiCa");
        carDto.setUser(myUserId);

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(myUserRepository.findById(myUserId)).thenReturn(Optional.of(myUser));

        carService.updateEntity(carDto);

        Assertions.assertEquals("Toyota",car.getBrand());
        Assertions.assertEquals("Celica",car.getModel());
        Assertions.assertEquals(myUser,car.getUser());

    }

    @Test
    void shouldThrowExceptionWhenCarNotFoundDuringUpdate(){
        CarDto carDto = new CarDto();
        carDto.setId(99999L);
        when(carRepository.findById(carDto.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(()-> carService.updateEntity(carDto)).isInstanceOf(CarNotFoundException.class);

    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundDuringUpgrade(){
        Long carId = 1L;
        Long myUserId = 99999L;

        Car car = new Car();
        car.setId(carId);
        car.setBrand("Bmw");
        car.setModel("M3");

        MyUser myUser = new MyUser();
        myUser.setId(myUserId);

        CarDto carDto = new CarDto();

        carDto.setId(carId);
        carDto.setBrand("Audi");
        carDto.setModel("Rs7");
        carDto.setUser(myUserId);

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(myUserRepository.findById(myUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> carService.updateEntity(carDto)).isInstanceOf(UserNotFoundException.class);

    }

    @Test
    void shouldDeleteCar(){
        Car car = new Car();
        Long id = 10L;
        car.setId(id);

        when(carRepository.findById(10L)).thenReturn(Optional.of(car));
        carService.deleteEntity(id);

        verify(carRepository).delete(car);

    }

    @Test
    void shouldThrowExceptionWhenCarNotFoundDuringDelete(){
        when(carRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carService.deleteEntity(999L)).isInstanceOf(CarNotFoundException.class);

    }

}
