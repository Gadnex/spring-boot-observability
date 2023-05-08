package net.binarypaper.productservice.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import io.micrometer.observation.annotation.Observed;

// Methods from extended interfaces are not traced and it is required to @Override them to work
@Observed
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    List<Product> findAll(Sort sort);

    @Override
    Optional<Product> findById(Long id);

    @Override
    <S extends Product> S save(S entity);

}