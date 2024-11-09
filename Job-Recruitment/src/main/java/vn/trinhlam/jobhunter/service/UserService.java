package vn.trinhlam.jobhunter.service;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.trinhlam.jobhunter.domain.User;
import vn.trinhlam.jobhunter.domain.dto.Meta;
import vn.trinhlam.jobhunter.domain.dto.ResCreateUserDTO;
import vn.trinhlam.jobhunter.domain.dto.ResUpdateDTO;
import vn.trinhlam.jobhunter.domain.dto.ResUserDTO;
import vn.trinhlam.jobhunter.domain.dto.ResultPaginationDTO;
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

    public User getUserByUsername(String userName) {
        return this.userRepository.findByEmail(userName);
    }

    public User getUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> specification, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        List<ResUserDTO> listUser = pageUser.getContent().stream().map(item -> new ResUserDTO(
                item.getId(),
                item.getName(),
                item.getAge(),
                item.getGender(),
                item.getAddress(),
                item.getCreatedAt(),
                item.getUpdatedAt(),
                item.getEmail())).collect(Collectors.toList());
        rs.setResult(listUser);

        return rs;
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();

        resCreateUserDTO.setAddress(user.getAddress());
        resCreateUserDTO.setAge(user.getAge());
        resCreateUserDTO.setCreateAt(user.getCreatedAt());
        resCreateUserDTO.setEmail(user.getEmail());
        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setGender(user.getGender());
        resCreateUserDTO.setName(user.getName());

        return resCreateUserDTO;
    }

    public void handleDelete(long id) {
        this.userRepository.deleteById(id);
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO resUserDTO = new ResUserDTO();
        resUserDTO.setId(user.getId());
        resUserDTO.setAddress(user.getAddress());
        resUserDTO.setAge(user.getAge());
        resUserDTO.setCreatedAt(user.getCreatedAt());
        resUserDTO.setGender(user.getGender());
        resUserDTO.setEmail(user.getEmail());
        resUserDTO.setName(user.getName());
        resUserDTO.setUpdatedAt(user.getUpdatedAt());
        return resUserDTO;

    }

    public User handleUpdateUser(User reqUser) {
        User currentUser = this.getUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setAddress(reqUser.getAddress());
            currentUser.setGender(reqUser.getGender());
            currentUser.setAge(reqUser.getAge());
            currentUser.setName(reqUser.getName());

            currentUser = this.userRepository.save(currentUser);
        }

        return currentUser;
    }

    public ResUpdateDTO convertToResUpdateDTO(User user) {
        ResUpdateDTO resUpdateDTO = new ResUpdateDTO();
        resUpdateDTO.setId(user.getId());
        resUpdateDTO.setName(user.getName());
        resUpdateDTO.setAge(user.getAge());
        resUpdateDTO.setUpdateInstant(user.getUpdatedAt());
        resUpdateDTO.setGenderEnum(user.getGender());
        resUpdateDTO.setAddress(user.getAddress());

        return resUpdateDTO;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.getUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        User currentUser = this.userRepository.findByRefreshTokenAndEmail(token, email);
        return currentUser;
    }
}
