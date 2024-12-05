package mx.edu.utez.integradora.user.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM user WHERE email = ?1", nativeQuery = true)
    Optional<User> findByMail(@Param("email") String email);

    @Query(value = "SELECT * FROM user WHERE phone = ?1", nativeQuery = true)
    Optional<User> findByPhone(@Param("phone") String phone);

    @Query(value = "SELECT * FROM user WHERE code = ?1", nativeQuery = true)
    Optional<User> findByCode(@Param("code") String code);
}
