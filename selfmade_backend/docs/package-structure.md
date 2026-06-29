# 패키지 구조 설계

> 작성일: 2026-06-29
> 프로젝트: selfmade (식품 이커머스)

---

## 구조 원칙

- **도메인별 패키지** 구조 사용
- 각 도메인은 `controller / service / repository / entity / dto` 로 구성
- 공통 관심사 (`config`, `exception`, `jwt`) 는 `global/` 패키지에서 관리
- 예외 클래스는 도메인별로 나누지 않고 `global/exception/` 에 전부 모아서 관리

---

## 전체 패키지 구조

```
com.myproj.selfmade
│
├── SelfmadeApplication.java
├── ApiResponse.java                       ← 공통 응답 포맷
│
├── global/
│   ├── config/
│   │   ├── SecurityConfig.java            ← 기존
│   │   ├── RedisConfig.java               ← 기존
│   │   └── CorsConfig.java                ← 추가 (React 연동)
│   ├── exception/
│   │   ├── BusinessException.java         ← 기존
│   │   ├── GlobalExceptionHandler.java    ← 기존
│   │   ├── DuplicateEmailException.java   ← 기존
│   │   ├── InvalidPasswordException.java  ← 기존
│   │   ├── InvalidTokenException.java     ← 기존
│   │   ├── UserNotFoundException.java     ← 기존
│   │   ├── CompanyNotFoundException.java  ← 추가 (Phase 2)
│   │   ├── ProductNotFoundException.java  ← 추가 (Phase 3)
│   │   ├── OrderNotFoundException.java    ← 추가 (Phase 5)
│   │   └── ...
│   └── jwt/
│       ├── JwtProvider.java               ← 기존
│       ├── JwtAuthFilter.java             ← 기존
│       └── RefreshTokenRepository.java    ← 기존
│
├── user/                                  ← Phase 1 (기존)
│   ├── controller/
│   │   ├── UserController.java
│   │   └── TokenController.java
│   ├── service/
│   │   ├── UserService.java
│   │   └── TokenService.java
│   ├── repository/
│   │   └── UserRepository.java
│   ├── entity/
│   │   └── User.java
│   └── dto/
│       ├── request/
│       │   ├── SignUpRequestDto.java
│       │   └── LoginRequestDto.java
│       └── response/
│           ├── UserResponseDto.java
│           └── TokenResponseDto.java
│
├── company/                               ← Phase 2
│   ├── controller/
│   │   └── CompanyController.java
│   ├── service/
│   │   └── CompanyService.java
│   ├── repository/
│   │   └── CompanyRepository.java
│   ├── entity/
│   │   └── Company.java
│   └── dto/
│       ├── request/
│       │   └── CompanyRegisterRequestDto.java
│       └── response/
│           └── CompanyResponseDto.java
│
├── category/                              ← Phase 3
│   ├── controller/
│   │   └── CategoryController.java
│   ├── service/
│   │   └── CategoryService.java
│   ├── repository/
│   │   └── CategoryRepository.java
│   ├── entity/
│   │   └── Category.java
│   └── dto/
│       ├── request/
│       │   └── CategoryRequestDto.java
│       └── response/
│           └── CategoryResponseDto.java
│
├── product/                               ← Phase 3
│   ├── controller/
│   │   └── ProductController.java
│   ├── service/
│   │   └── ProductService.java
│   ├── repository/
│   │   └── ProductRepository.java
│   ├── entity/
│   │   └── Product.java
│   └── dto/
│       ├── request/
│       │   └── ProductRequestDto.java
│       └── response/
│           └── ProductResponseDto.java
│
├── cart/                                  ← Phase 4
│   ├── controller/
│   │   └── CartController.java
│   ├── service/
│   │   └── CartService.java
│   ├── repository/
│   │   ├── CartRepository.java
│   │   └── CartItemRepository.java
│   ├── entity/
│   │   ├── Cart.java
│   │   └── CartItem.java
│   └── dto/
│       ├── request/
│       │   └── CartItemRequestDto.java
│       └── response/
│           └── CartResponseDto.java
│
├── order/                                 ← Phase 5
│   ├── controller/
│   │   └── OrderController.java
│   ├── service/
│   │   └── OrderService.java
│   ├── repository/
│   │   ├── OrderRepository.java
│   │   └── OrderItemRepository.java
│   ├── entity/
│   │   ├── Order.java
│   │   └── OrderItem.java
│   └── dto/
│       ├── request/
│       │   └── OrderRequestDto.java
│       └── response/
│           ├── OrderResponseDto.java
│           └── OrderItemResponseDto.java
│
├── payment/                               ← Phase 6
│   ├── controller/
│   │   └── PaymentController.java
│   ├── service/
│   │   └── PaymentService.java
│   ├── repository/
│   │   └── PaymentRepository.java
│   ├── entity/
│   │   └── Payment.java
│   └── dto/
│       ├── request/
│       │   └── PaymentRequestDto.java
│       └── response/
│           └── PaymentResponseDto.java
│
└── review/                                ← Phase 7
    ├── controller/
    │   └── ReviewController.java
    ├── service/
    │   └── ReviewService.java
    ├── repository/
    │   └── ReviewRepository.java
    ├── entity/
    │   └── Review.java
    └── dto/
        ├── request/
        │   └── ReviewRequestDto.java
        └── response/
            └── ReviewResponseDto.java
```

---

## 설계 결정 사항

### ApiResponse.java 위치
현재 루트에 위치. 모든 도메인에서 공통으로 사용하므로 그대로 유지.
`global/common/` 으로 옮기면 import 경로를 전부 변경해야 하므로 현 상태 유지.

### 예외 클래스 위치
도메인별로 나누지 않고 `global/exception/` 에 전부 모아서 관리.
예외는 어느 도메인에서도 던질 수 있으므로 한 곳에서 관리하는 것이 유지보수에 유리.

### Cart / CartItem, Order / OrderItem 같은 패키지
`CartItem` 은 `Cart` 없이 독립적으로 존재할 수 없으므로 같은 `cart/` 패키지에 배치.
`OrderItem` 도 동일한 이유로 `order/` 패키지에 배치.

### CorsConfig 추가
React 프론트엔드와 연동 시 CORS 문제가 발생하므로 Phase 0 에서 미리 추가.
