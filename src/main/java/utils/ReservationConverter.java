package utils;

import dto.ReservationDto;
import entities.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import repositories.UsersRepository;

@Component
public class ReservationConverter {

    @Autowired
    private UsersRepository usersRepository;

    public Reservation toEntity(ReservationDto dto){
        if(dto==null){
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        reservation.setUser(usersRepository.findById(1L).orElse(null));
        reservation.setCar(dto.getCar());

        return reservation;
    }

}
