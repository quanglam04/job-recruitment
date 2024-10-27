package vn.trinhlam.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.trinhlam.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);

}
