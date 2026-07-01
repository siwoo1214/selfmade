package com.myproj.selfmade.company.controller;

import com.myproj.selfmade.ApiResponse;
import com.myproj.selfmade.company.dto.response.CompanyResponseDto;
import com.myproj.selfmade.company.entity.CompanyStatus;
import com.myproj.selfmade.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CompanyController {

    private final CompanyService companyService;

    // ADMIN - 판매자 신청 목록 조회
    @GetMapping("/admin/companies")
    public ResponseEntity<ApiResponse<List<CompanyResponseDto>>> getCompanies(
            @RequestParam(defaultValue="PENDING") CompanyStatus status
            ) {
        List<CompanyResponseDto> data = companyService.findAllByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    // ADMIN - 판매자 신청 상세 조회
    @GetMapping("/admin/companies/{id}")
    public ResponseEntity<ApiResponse<CompanyResponseDto>> getCompany(
            @PathVariable Long id
    ){
        CompanyResponseDto data = companyService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    // ADMIN — 판매자 승인
    @PatchMapping("/admin/companies/{id}/approve")
    public ResponseEntity<ApiResponse<Void>> approveCompany(
            @PathVariable Long id
    ) {
        companyService.approve(id);
        return ResponseEntity.ok(ApiResponse.success(null, "판매자 승인이 완료되었습니다."));
    }

    // ADMIN — 판매자 거절
    @PatchMapping("/admin/companies/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectCompany(
            @PathVariable Long id
    ) {
        companyService.reject(id);
        return ResponseEntity.ok(ApiResponse.success(null, "판매자 신청이 거절되었습니다."));
    }

    // SELLER - 내 회사 정보 조회
    @GetMapping("/seller/company")
    public ResponseEntity<ApiResponse<CompanyResponseDto>> getSellerCompany(
            @AuthenticationPrincipal String email
    ){
        CompanyResponseDto data = companyService.findByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

}
