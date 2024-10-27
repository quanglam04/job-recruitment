package vn.trinhlam.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.trinhlam.jobhunter.domain.User;
import vn.trinhlam.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    public void delete(long id) {
        this.userRepository.deleteById(id);
    }
}
