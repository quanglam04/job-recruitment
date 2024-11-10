package vn.trinhlam.jobhunter.service;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.trinhlam.jobhunter.domain.Company;
import vn.trinhlam.jobhunter.domain.User;
import vn.trinhlam.jobhunter.domain.response.ResCreateUserDTO;
import vn.trinhlam.jobhunter.domain.response.ResUpdateDTO;
import vn.trinhlam.jobhunter.domain.response.ResUserDTO;
import vn.trinhlam.jobhunter.domain.response.ResultPaginationDTO;
import vn.trinhlam.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public User createUser(User user) {

        if (user.getCompany() != null) {
            Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        }
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
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

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
                item.getEmail(),
                new ResUserDTO.CompanyUser(
                        item.getCompany() != null ? item.getCompany().getId() : 0,
                        item.getCompany() != null ? item.getCompany().getName() : null)))
                .collect(Collectors.toList());
        rs.setResult(listUser);

        return rs;
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser com = new ResCreateUserDTO.CompanyUser();

        resCreateUserDTO.setAddress(user.getAddress());
        resCreateUserDTO.setAge(user.getAge());
        resCreateUserDTO.setCreateAt(user.getCreatedAt());
        resCreateUserDTO.setEmail(user.getEmail());
        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setGender(user.getGender());
        resCreateUserDTO.setName(user.getName());

        if (user.getCompany() != null) {
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            resCreateUserDTO.setCompany(com);
        }
        return resCreateUserDTO;
    }

    public void handleDelete(long id) {
        this.userRepository.deleteById(id);
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO resUserDTO = new ResUserDTO();
        ResUserDTO.CompanyUser companyUser = new ResUserDTO.CompanyUser();

        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            resUserDTO.setCompany(companyUser);
        }

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

            if (reqUser.getCompany() != null) {
                Optional<Company> companyOptional = this.companyService.findById(reqUser.getCompany().getId());
                reqUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
            }

            currentUser = this.userRepository.save(currentUser);
        }

        return currentUser;
    }

    public ResUpdateDTO convertToResUpdateDTO(User user) {
        ResUpdateDTO resUpdateDTO = new ResUpdateDTO();
        ResUpdateDTO.CompanyUser companyUser = new ResUpdateDTO.CompanyUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            resUpdateDTO.setCompany(companyUser);
        }

        resUpdateDTO.setId(user.getId());
        resUpdateDTO.setName(user.getName());
        resUpdateDTO.setAge(user.getAge());
        resUpdateDTO.setUpdateAt(user.getUpdatedAt());
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
