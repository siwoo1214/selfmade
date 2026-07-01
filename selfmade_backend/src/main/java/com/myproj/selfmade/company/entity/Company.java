package com.myproj.selfmade.company.entity;

import com.myproj.selfmade.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="companies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    @Column(unique = true)
    private String businessNumber;

    private String representative;
    private String address;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status;

    private LocalDateTime createdAt;

    @Builder
    public Company(User user, String name, String businessNumber,
                   String representative, String address, CompanyStatus status) {
        this.user = user;
        this.name = name;
        this.businessNumber = businessNumber;
        this.representative = representative;
        this.address = address;
        this.status = CompanyStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    // 회사 승인
    public void approve(){
        this.status = CompanyStatus.APPROVED;
    }

    // 회사 거부
    public void reject(){
        this.status = CompanyStatus.REJECTED;
    }
}
