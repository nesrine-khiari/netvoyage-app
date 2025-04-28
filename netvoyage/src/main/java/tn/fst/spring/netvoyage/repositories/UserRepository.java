package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.enums.Role;

import java.time.LocalDateTime;
import java.util.Optional;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByEntrepriseId(Long entrepriseId);
    User findByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT FUNCTION('MONTH', u.createdAt) AS month, COUNT(u) FROM User u GROUP BY FUNCTION('MONTH', u.createdAt)")
    List<Object[]> countUsersByMonth();


    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> countUsersByRole();

    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate AND u.role = :role ORDER BY u.createdAt ASC")
    List<User> findUsersByDateRangeAndRole(LocalDateTime startDate, LocalDateTime endDate, Role role);


}
