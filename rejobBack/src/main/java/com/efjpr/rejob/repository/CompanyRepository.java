package com.efjpr.rejob.repository;


import com.efjpr.rejob.domain.Company;
import com.efjpr.rejob.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByUser(User user);
}
