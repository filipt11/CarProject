package services;

import entities.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import repositories.CarRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void saveCar(Car car) {
        carRepository.save(car);
    }

    public List<Car> selectAll() {
        return carRepository.findAll();
    }

    public Page<Car> selectPaging(int page, int size, String sort) {
        Sort sorting;
        if ("no".equals(sort)) {
            sorting = Sort.by("id").ascending();
        } else if ("asc".equals(sort)) {
            sorting = Sort.by("brand").ascending();
        } else {
            sorting = Sort.by("brand").descending();
        }
        Pageable paging = PageRequest.of(page, size, sorting);

        return carRepository.findAll(paging);
    }

//    public List<Integer> createPageNumbers(int page, int totalPages) {
//        List<Integer> pageNumbers = new ArrayList<>();
//        pageNumbers.add(1);
//
//        if (page > 2) {
//            pageNumbers.add(-1); // -1 = "..."
//        }
//        if (page > 1 && page < totalPages) {
//            pageNumbers.add(page);
//        }
//        if (page == 2 && totalPages > 2) {
//            pageNumbers.add(-1);
//        }
//        if (page == totalPages - 1 && totalPages > 2) {
//            pageNumbers.add(page);
//        }
//        if (totalPages > 1) {
//            pageNumbers.add(totalPages);
//        }
//        return pageNumbers;
//    }

//    public List<Integer> createPageNumbers(int currentPageZeroBased, int totalPages) {
//        List<Integer> pageNumbers = new ArrayList<>();
//        if (totalPages <= 0) return pageNumbers;
//
//        // zawsze pierwsza
//        pageNumbers.add(0);
//
//        // jeżeli mało stron pokaż wszystkie
////        if (totalPages <= 7) {
////            pageNumbers.clear();
////            for (int i = 0; i < totalPages; i++) pageNumbers.add(i);
////            return pageNumbers;
////        }
//
//        // jeśli aktualna jest blisko początku pokaż kilka pierwszych, potem ..., last
//        if (currentPageZeroBased <= 3) {
//            for (int i = 1; i <= 3; i++) pageNumbers.add(i);
//            pageNumbers.add(-1);              // "..."
//            pageNumbers.add(totalPages - 1);  // ostatnia
//            return pageNumbers;
//        }
//
//        // jeśli aktualna jest blisko końca pokaż first, ..., kilka ostatnich
//        if (currentPageZeroBased >= totalPages - 4) {
//            pageNumbers.add(-1);
//            for (int i = totalPages - 4; i < totalPages; i++) pageNumbers.add(i);
//            return pageNumbers;
//        }
//
//        // w środku: first, ..., current-1, current, current+1, ..., last
//        pageNumbers.add(-1);
//        //pageNumbers.add(currentPageZeroBased - 1);
//        pageNumbers.add(currentPageZeroBased);
//        //pageNumbers.add(currentPageZeroBased + 1);
//        pageNumbers.add(-1);
//        pageNumbers.add(totalPages - 1);
//
//        return pageNumbers;
//    }

public List<Integer> createPageNumbers(int current, int totalPages) {
    List<Integer> pageNumbers = new ArrayList<>();
    if (totalPages <= 0) return pageNumbers;

    // zawsze pierwsza strona (0-based)
    pageNumbers.add(0);

    // jeśli jesteśmy blisko początku: pokaż pierwsze kilka (0,1,2), potem "..." i last
    if (current <= 2) {
        pageNumbers.add(1);
        if (current == 2) {
            pageNumbers.add(2); // możesz to zakomentować, jeśli nie chcesz pokazywać 2 gdy current==2
        }
        pageNumbers.add(-1);
        pageNumbers.add(totalPages - 1);
        return pageNumbers;
    }

    // jeśli jesteśmy blisko końca: first, "..." i ostatnie trzy
    if (current >= totalPages - 3) {
        pageNumbers.add(-1);
        pageNumbers.add(totalPages - 3);
        pageNumbers.add(totalPages - 2);
        pageNumbers.add(totalPages - 1);
        return pageNumbers;
    }

    // przypadek środkowy: first, ..., current, ..., last
    pageNumbers.add(-1);

    // jeżeli chcesz kiedyś pokazać sąsiadów, odkomentuj poniższe linie:
    // pageNumbers.add(current - 1);
    pageNumbers.add(current);       // pokazujemy tylko aktualną stronę
    // pageNumbers.add(current + 1);

    pageNumbers.add(-1);
    pageNumbers.add(totalPages - 1);

    return pageNumbers;
}

}