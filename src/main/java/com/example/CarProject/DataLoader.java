package com.example.CarProject;

import com.example.CarProject.entities.Car;
import com.example.CarProject.entities.MyUser;
import com.example.CarProject.entities.Reservation;
import com.example.CarProject.repositories.CarRepository;
import com.example.CarProject.repositories.MyUserRepository;
import com.example.CarProject.repositories.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {
    private final MyUserRepository myUserRepository;
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(MyUserRepository myUserRepository, CarRepository carRepository, ReservationRepository reservationRepository, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.carRepository = carRepository;
        this.reservationRepository = reservationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        LocalDate starting = LocalDate.now().plusDays(2);
        LocalDate ending = LocalDate.now().plusDays(7);

        MyUser admin = createUserIfNotExists("admin", "admin@example.com", "123", "ROLE_ADMIN");
        MyUser user = createUserIfNotExists("testuser", "test.email@example.com", "123", "ROLE_USER");

        Car bmw = createCarIfNotExists("Bmw","M3",4.2,420,2008,"m3.png",true, admin,"relatively fast car");

        Car porsche =  createCarIfNotExists("Porsche","911",5.0,534,2016,"porshe911.jpg",true, admin,"relatively even faster car");

        Car honda = createCarIfNotExists("Honda","Civic",1.6,180,2014,"civic.jpg",true, admin,"red civic");

        Car fiat = createCarIfNotExists("Fiat","Panda",1.2,60,2005,"panda.jpg",true, admin,"relatively the fastest car");

        Car opel = createCarIfNotExists("Opel","Astra",1.8,210,2017,"astra.jpg",true, admin,"blue astra opc");

        Car audi = createCarIfNotExists("Audi","R8",5.2,640,2020,null,false, admin,"fast audi");

        Car ford = createCarIfNotExists("Ford","Focus",2.0,190,2016,null,false, user,"American car");

        Reservation reservation = createReservationIfNotExists(starting,ending,admin,bmw);

        Reservation reservation1 = createReservationIfNotExists(starting.plusDays(4),ending.plusDays(7),admin,audi);

        Reservation reservation2 = createReservationIfNotExists(starting.plusDays(31),ending.plusDays(31),user,opel);


    }

    private MyUser createUserIfNotExists(String username, String email, String password, String role) {

        MyUser user = myUserRepository.findByUsername(username).orElse(null);

        if (user == null) {
            user = new MyUser();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            user = myUserRepository.save(user);
            System.out.println("Created user: " + username);
        } else {
            System.out.println("User already exists: " + username);
        }

        return user;
    }

    private Car createCarIfNotExists(String brand,String model,double engineSize, int hp, int prodYear, String image, boolean highlighted, MyUser myUser,String description){

        Car car = carRepository.findByBrandAndModelAndProdYear(brand,model,prodYear).orElse(null);

        if(car == null){
            car = new Car();
            car.setBrand(brand);
            car.setModel(model);
            car.setEngineSize(engineSize);
            car.setHp(hp);
            car.setProdYear(prodYear);
            car.setImage(image);
            car.setHighlighted(highlighted);
            car.setUser(myUser);
            car.setDescription(description);

            carRepository.save(car);
            System.out.println("Created Car : " + brand + " " + model);
        }
        else{
            System.out.println("Car already exists: " + brand + " " + model);
        }
        return car;
    }

    private Reservation createReservationIfNotExists(LocalDate startDate,LocalDate endDate,MyUser myUser, Car car){
        Reservation reservation = reservationRepository.findByStartDateAndEndDateAndCarId(startDate,endDate,car.getId()).orElse(null);

        if(reservation==null){
            reservation = new Reservation();
            reservation.setStartDate(startDate);
            reservation.setEndDate(endDate);
            reservation.setUser(myUser);
            reservation.setCar(car);

            reservationRepository.save(reservation);
            System.out.println("Reservation saved");
        }
        else{
            System.out.println("Reservation already exists");
        }
        return reservation;
    }

}
