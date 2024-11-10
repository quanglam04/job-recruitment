package vn.trinhlam.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.trinhlam.jobhunter.domain.Company;
import vn.trinhlam.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User save(User user);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    User findByRefreshTokenAndEmail(String token, String email);

    List<User> findByCompany(Company company);

}
