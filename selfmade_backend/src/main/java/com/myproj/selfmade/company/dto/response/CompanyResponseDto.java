package com.myproj.selfmade.company.dto.response;

import com.myproj.selfmade.company.entity.Company;
import com.myproj.selfmade.company.entity.CompanyStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CompanyResponseDto {

    private Long id;
    private Long userId;
    private String name;
    private String businessNumber;
    private String representative;
    private String address;
    private CompanyStatus status;
    private LocalDateTime createdAt;

    public static CompanyResponseDto from(Company company) {
        return CompanyResponseDto.builder()
                .id(company.getId())
                .userId(company.getUser().getId())
                .name(company.getName())
                .businessNumber(company.getBusinessNumber())
                .representative(company.getRepresentative())
                .address(company.getAddress())
                .status(company.getStatus())
                .createdAt(company.getCreatedAt())
                .build();
    }
}