package com.myproj.selfmade.user.dto.request;

import com.myproj.selfmade.user.entity.Role;
import com.myproj.selfmade.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SellerSignUpRequestDto {

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String password;

    @NotBlank(message = "전화번호는 필수입니다")
    private String phone;

    @NotBlank(message = "회사명은 필수입니다")
    private String companyName;

    @NotBlank(message = "사업자번호는 필수입니다")
    private String businessNumber;

    @NotBlank(message = "대표자명은 필수입니다")
    private String representative;

    @NotBlank(message = "주소는 필수입니다")
    private String address;

    public User toUserEntity(String encodedPassword) {
        return User.builder()
                .name(this.name)
                .email(this.email)
                .password(encodedPassword)
                .phone(this.phone)
                .role(Role.SELLER)
                .build();
    }
}