
package SklepInternetowy.Projekt.repository;

import SklepInternetowy.Projekt.entity.UserEnt;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRep extends JpaRepository<UserEnt,Long>{
    
    Optional<UserEnt> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
