package vn.trinhlam.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.trinhlam.jobhunter.domain.Company;
import vn.trinhlam.jobhunter.domain.dto.Meta;
import vn.trinhlam.jobhunter.domain.dto.ResultPaginationDTO;
import vn.trinhlam.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company c) {
        return this.companyRepository.save(c);
    }

    public ResultPaginationDTO handleGetCompany(Specification<Company> specification, Pageable pageable) {
        Page<Company> pCompany = this.companyRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pCompany.getNumber() + 1);
        mt.setPageSize(pCompany.getSize());

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
            this.companyRepository.delete(cOptional.get());
        }
        return;
    }

}
