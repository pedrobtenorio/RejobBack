package com.efjpr.rejob.service;

import com.efjpr.rejob.domain.Collaborator;
import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.Dto.CompanyRegisterRequest;
import com.efjpr.rejob.domain.User;
import com.efjpr.rejob.repository.CollaboratorRepository;
import com.efjpr.rejob.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    public Company create(CompanyRegisterRequest request, User user) {

        Company company = Company.builder()
                .companyType(request.getCompanyType())
                .cnpj(request.getCnpj())
                .businessActivity(request.getBusinessActivity())
                .phone(request.getPhone())
                .email(request.getEmail())
                .institutionalDescription(request.getInstitutionalDescription())
                .numberOfEmployees(request.getNumberOfEmployees())
                .user(user)
                .headquarters(request.getHeadquarters())
                .name(request.getName())
                .build();

        return companyRepository.save(company);
    }

    public Company getCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company with ID " + companyId + " not found"));
    }

    public Company findByUser(User user) {
        return companyRepository.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("Collaborator not found"));
    }

    public void setUserToCompany(Collaborator collaborator, Company company) {
        List<Collaborator> collaboratorsList = new ArrayList<>();
        collaboratorsList.add(collaborator);
        company.setCollaborators(collaboratorsList);
        companyRepository.save(company);
    }

    public Company updateCompany(Long companyId, Company updatedCompany) {
        Company existingCompany = getCompanyById(companyId);
        updateCompanyFields(existingCompany, updatedCompany);
        return companyRepository.save(existingCompany);
    }

    public void deleteCompany(Long companyId) {
        Company companyToDelete = getCompanyById(companyId);
        companyRepository.delete(companyToDelete);
    }

    public Company getCompanyByCollaboratorId(Long id) {
        Optional<Collaborator> collaboratorOpt = collaboratorRepository.findById(id);
        if(collaboratorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Collaborator does not exist");
        }
        Collaborator collaborator = collaboratorOpt.get();
        Company company = collaborator.getCompany();

        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Collaborator does not have a company");
        }

        return company;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public void removeCollaborator(List<Company> companies) {
        for (Company company: companies) {
            company.removeCollaborators();
        }
    }

    private void updateCompanyFields(Company existingCompany, Company updatedCompany) {
        existingCompany.setCnpj(updatedCompany.getCnpj());
        existingCompany.setName(updatedCompany.getName());
        existingCompany.setBusinessActivity(updatedCompany.getBusinessActivity());
        existingCompany.setNumberOfEmployees(updatedCompany.getNumberOfEmployees());
        existingCompany.setHeadquarters(updatedCompany.getHeadquarters());
        existingCompany.setPhone(updatedCompany.getPhone());
        existingCompany.setInstitutionalDescription(updatedCompany.getInstitutionalDescription());
        existingCompany.setEmail(updatedCompany.getEmail());
        existingCompany.setCompanyType(updatedCompany.getCompanyType());
    }
}