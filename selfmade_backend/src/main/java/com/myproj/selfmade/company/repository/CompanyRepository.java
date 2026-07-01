package com.myproj.selfmade.company.repository;

import com.myproj.selfmade.company.entity.Company;
import com.myproj.selfmade.company.entity.CompanyStatus;
import com.myproj.selfmade.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByUser(User user);
    List<Company> findAllByStatus(CompanyStatus status);
    boolean existsByBusinessNumber(String businessNumber);

}
