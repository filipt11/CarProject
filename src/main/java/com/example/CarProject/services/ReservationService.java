package com.example.CarProject.services;

import com.example.CarProject.dto.ReservationDto;
import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.MyUser;
import com.example.CarProject.entities.Reservation;
import com.example.CarProject.exceptions.CarNotFoundException;
import com.example.CarProject.exceptions.InvalidReservationDateException;
import com.example.CarProject.exceptions.ReservationNotFoundException;
import com.example.CarProject.exceptions.UserNotFoundException;
import com.example.CarProject.repositories.CarRepository;
import com.example.CarProject.repositories.MyUserRepository;
import com.example.CarProject.security.SignedUserDetails;
import com.example.CarProject.utils.ReservationConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import com.example.CarProject.repositories.ReservationRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;
    private final ReservationConverter reservationConverter;
    private final MyUserRepository myUserRepository;

    public ReservationService(ReservationRepository reservationRepository, CarRepository carRepository, ReservationConverter reservationConverter, MyUserRepository myUserRepository) {
        this.reservationRepository = reservationRepository;
        this.carRepository = carRepository;
        this.reservationConverter = reservationConverter;
        this.myUserRepository = myUserRepository;
    }

    public List<Reservation> selectAll(){
        return reservationRepository.findAll();
    }

    public void saveReservation(ReservationDto dto){
        if (dto.getCarId() == null){
            throw new CarNotFoundException();
        }
        if(dto.getStartDate().isAfter(dto.getEndDate())){
            throw new InvalidReservationDateException();
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SignedUserDetails signedUserDetails = (SignedUserDetails) auth.getPrincipal();
        Long userId = signedUserDetails.getId();
        Long carId = dto.getCarId();

        Car car = carRepository.findById(carId) .orElseThrow(() -> new CarNotFoundException());
        MyUser user = myUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Reservation reservation = reservationConverter.toEntity(dto);
        reservation.setCar(car);
        reservation.setUser(user);

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

    public String generateCsv() {
        List<Reservation> reservations = selectAll();
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Car ID,Car Brand,Car Model,Start Date,End Date\n");
        for (Reservation reservation : reservations) {
            sb.append(reservation.getId()).append(",");
            sb.append(reservation.getCar().getId()).append(",");
            sb.append(reservation.getCar().getBrand()).append(",");
            sb.append(reservation.getCar().getModel()).append(",");
            sb.append(reservation.getStartDate()).append(",");
            sb.append(reservation.getEndDate()).append("\n");
        }

        return sb.toString();
    }

    public Optional<Reservation> findById(Long id){
        return reservationRepository.findById(id);
    }

    @Transactional
    public void updateReservation(ReservationDto dto){
        Reservation reservation = reservationRepository.findById(dto.getId()).orElseThrow(() -> new ReservationNotFoundException());
        if(dto.getStartDate().isAfter(dto.getEndDate())){
            throw new InvalidReservationDateException();
        }

        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());

    }

    @Transactional
    public void deleteReservation(Long id){
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException());
        reservationRepository.delete(reservation);
    }

}
