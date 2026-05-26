
package SklepInternetowy.Projekt.repository;

import SklepInternetowy.Projekt.entity.UserEnt;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRep extends JpaRepository<UserEnt,Long>{
    
}
