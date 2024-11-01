package vn.trinhlam.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.trinhlam.jobhunter.domain.Company;
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
}
