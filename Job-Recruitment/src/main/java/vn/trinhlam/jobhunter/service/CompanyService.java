package vn.trinhlam.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.trinhlam.jobhunter.domain.Company;
import vn.trinhlam.jobhunter.domain.User;
import vn.trinhlam.jobhunter.domain.response.ResultPaginationDTO;
import vn.trinhlam.jobhunter.repository.CompanyRepository;
import vn.trinhlam.jobhunter.repository.UserRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handleCreateCompany(Company c) {
        return this.companyRepository.save(c);
    }

    public ResultPaginationDTO handleGetCompany(Specification<Company> specification, Pageable pageable) {
        Page<Company> pCompany = this.companyRepository.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pCompany.getNumber() + 1);
        mt.setPageSize(pCompany.getSize());
        System.out.println(pCompany.getSize() + ">>>>>>>>>>>>>>>>>>>>>>>");

        mt.setPages(pCompany.getTotalPages());
        mt.setTotal(pCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pCompany.getContent());
        return rs;
    }

    public Company handleUpdateCompany(Company C) {
        Optional<Company> comOptional = this.companyRepository.findById(C.getId());
        if (comOptional.isPresent()) {
            Company currentCompany = comOptional.get();

            currentCompany.setLogo(C.getLogo());
            currentCompany.setName(C.getName());
            currentCompany.setDescription(C.getDescription());
            currentCompany.setAddress(C.getAddress());
            return this.companyRepository.save(currentCompany);
        }
        return null;
    }

    public void handleDeleteCompany(long id) {
        Optional<Company> cOptional = this.companyRepository.findById(id);
        if (cOptional.isPresent()) {
            Company company = cOptional.get();
            List<User> users = this.userRepository.findByCompany(company);
            this.userRepository.deleteAll(users);

        }
        this.companyRepository.deleteById(id);

        return;
    }

    public Optional findById(long id) {
        return this.companyRepository.findById(id);
    }

}
