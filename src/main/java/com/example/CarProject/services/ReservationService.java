package com.example.CarProject.services;

import com.example.CarProject.dto.ReservationDto;
import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.Reservation;
import com.example.CarProject.exceptions.CarNotFoundException;
import com.example.CarProject.repositories.CarRepository;
import com.example.CarProject.utils.ReservationConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import com.example.CarProject.repositories.ReservationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    private final CarRepository carRepository;

    public final ReservationConverter reservationConverter;

    public ReservationService(ReservationRepository reservationRepository, CarRepository carRepository, ReservationConverter reservationConverter) {
        this.reservationRepository = reservationRepository;
        this.carRepository = carRepository;
        this.reservationConverter = reservationConverter;
    }
    public List<Reservation> selectAll(){
        return reservationRepository.findAll();
    }
    public void saveReservation(ReservationDto dto){
        if (dto.getCar() == null){
            throw new CarNotFoundException(); }
        Long carId = dto.getCar().getId();
        Car car = carRepository.findById(carId) .orElseThrow(() -> new CarNotFoundException()); dto.setCar(car);
        Reservation reservation = reservationConverter.toEntity(dto);
        reservationRepository.save(reservation);
    }

    public Page<Reservation> selectPaging(int page, int size, String sort) {
        Sort sorting;
        if ("no".equals(sort)) {
            sorting = Sort.by("id").ascending();
        }
        else if ("start-asc".equals(sort)) {
            sorting = Sort.by("startDate").ascending();
        }
        else if ("start-desc".equals(sort)) {
            sorting = Sort.by("startDate").descending();
        }
        else if ("end-asc".equals(sort)) {
            sorting = Sort.by("endDate").ascending();
        }
        else {
            sorting = Sort.by("endDate").descending();
        }
        Pageable paging = PageRequest.of(page, size, sorting);

        return reservationRepository.findAll(paging);
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
}
