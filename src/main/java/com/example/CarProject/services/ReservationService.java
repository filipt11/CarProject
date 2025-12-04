package com.example.CarProject.services;

import com.example.CarProject.entities.Reservation;
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


    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    public List<Reservation> selectAll(){
        return reservationRepository.findAll();
    }
    public void saveReservation(Reservation reservation){
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
