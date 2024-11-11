package vn.trinhlam.jobhunter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.trinhlam.jobhunter.domain.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findAll(Specification specification, Pageable pageable);
}
