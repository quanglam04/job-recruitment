package vn.trinhlam.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.trinhlam.jobhunter.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
