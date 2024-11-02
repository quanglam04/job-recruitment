package vn.trinhlam.jobhunter.service;

import java.util.List;
import java.util.Optional;

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

    public List<Company> handleGetCompany() {
        return this.companyRepository.findAll();
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
