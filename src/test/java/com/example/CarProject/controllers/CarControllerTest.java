//package com.example.CarProject.controllers;
//
//import com.example.CarProject.converters.CarConverter;
//import com.example.CarProject.entities.Car;
//import com.example.CarProject.services.CarService;
//import jakarta.persistence.EntityManagerFactory;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//@WebMvcTest(CarController.class)
//public class CarControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockitoBean
//    private CarService carService;
//
//    @MockitoBean
//    private CarConverter carConverter;
//
//    @MockitoBean
//    private EntityManagerFactory entityManagerFactory;
//
//    @Test
//    void shouldReturnCarListViewWithHighlightedCars() throws Exception {
//        List<Car> highlighted = List.of( new Car(1L, "BMW", "M3", 2008, 4.0, 420, "desc", "img.png", true, null, null) );
//        when(carService.selectHighlighted()).thenReturn(highlighted);
//        mockMvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("carList"))
//                .andExpect(model().attributeExists("list"))
//                .andExpect(model().attribute("list", highlighted));
//
//        verify(carService).selectHighlighted();
//    }
//
//}
