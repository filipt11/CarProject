package services;

import dto.CarDto;
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

    public List<String> selectBrands(){
        return carRepository.findAllDistinctBrandsOrdered();
    }

    public List<Car> selectHighlighted(){
        return carRepository.findByHighlightedTrue();
    }

    public Page<Car> selectPaging(int page, int size, String sort, List<String> brands) {
        Sort sorting;
        if ("no".equals(sort)) {
            sorting = Sort.by("id").ascending();
        } else if ("asc".equals(sort)) {
            sorting = Sort.by("brand").ascending();
        } else {
            sorting = Sort.by("brand").descending();
        }
        Pageable paging = PageRequest.of(page, size, sorting);

        if (brands == null || brands.isEmpty()) {
            return carRepository.findAll(paging);
        }

        return carRepository.findByBrandIn(brands, paging);
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