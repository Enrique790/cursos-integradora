package mx.edu.utez.integradora.category.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    @Query(value = "SELECT * FROM categories WHERE name LIKE ?1 AND id != ?2 LIMIT 1", nativeQuery = true)
    Optional<Category> searchByNameAndId(String name, Long id);

    List<Category> findAllByStatusOrderByName(boolean status);
}
