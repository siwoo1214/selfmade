package com.myproj.selfmade.company.service;

import com.myproj.selfmade.company.dto.response.CompanyResponseDto;
import com.myproj.selfmade.company.entity.Company;
import com.myproj.selfmade.company.entity.CompanyStatus;
import com.myproj.selfmade.company.repository.CompanyRepository;
import com.myproj.selfmade.global.exception.CompanyNotFoundException;
import com.myproj.selfmade.global.exception.UserNotFoundException;
import com.myproj.selfmade.user.entity.User;
import com.myproj.selfmade.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    // ADMIN - 전체 산청 목록 조회
    @Transactional(readOnly = true)
    public List<CompanyResponseDto> findAllByStatus(CompanyStatus status) {
        return companyRepository.findAllByStatus(status)
                .stream()
                .map(CompanyResponseDto::from)
                .collect(Collectors.toList());
    }

    // ADMIN - 단건 조회
    @Transactional(readOnly = true)
    public CompanyResponseDto findById(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(CompanyNotFoundException::new);
        return CompanyResponseDto.from(company);
    }

    // ADMIN - 회사 수락
    @Transactional
    public void approve(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(CompanyNotFoundException::new);
        company.approve();
    }

    // ADMIN — 회사 거절
    @Transactional
    public void reject(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(CompanyNotFoundException::new);
        company.reject();
    }

    // SELLER - 내 회사 정보 조회
    @Transactional(readOnly = true)
    public CompanyResponseDto findByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        Company company = companyRepository.findByUser(user)
                .orElseThrow(CompanyNotFoundException::new);
        return CompanyResponseDto.from(company);
    }

}
