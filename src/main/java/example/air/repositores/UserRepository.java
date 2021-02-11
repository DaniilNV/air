package example.air.repositores;

import example.air.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String name);

    Users findByEmail(String email);

    Users findByActivationCode(String code);

    List<Users> findAllByActiveIsTrue();
}
