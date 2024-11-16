package vn.trinhlam.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.trinhlam.jobhunter.domain.Subscriber;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    boolean existsByEmail(String email);
}
