package repositories;

import entities.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long> {
    List<Car> findByHighlightedTrue();
    @Query("select distinct c.brand from Car c order by c.brand")
    List<String> findAllDistinctBrandsOrdered();

    Page<Car> findByBrandIn(List<String> brands, Pageable pageable);
}
