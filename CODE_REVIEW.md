# 🔍 투두 리스트 애플리케이션 코드 리뷰

## 📋 **개요**
Spring Boot + Next.js 풀스택 투두 애플리케이션에 대한 종합적인 코드 리뷰입니다.

## ✅ **강점**

### 백엔드 (Spring Boot)
- **잘 구성된 레이어 아키텍처**: Entity, Repository, Service, Controller로 명확하게 분리
- **Lombok 활용**: 보일러플레이트 코드 감소로 가독성 향상
- **JPA 활용**: 데이터베이스 추상화와 객체지향적 데이터 처리
- **DTO 패턴**: 요청/응답 데이터와 엔티티 분리로 캡슐화 달성
- **일관된 API 응답**: `ApiResponse<T>` 래퍼 클래스로 통일된 응답 구조

### 프론트엔드 (Next.js)
- **TypeScript 활용**: 타입 안정성 확보
- **컴포넌트 분리**: 재사용 가능한 UI 컴포넌트들로 잘 구성
- **Custom Hooks**: 로직과 UI 분리로 관심사 분리 (`useAuth`, `useTodos`)
- **서비스 레이어**: API 호출 로직을 별도 서비스로 추상화

## ⚠️ **보안 취약점**

### 1. 데이터베이스 자격증명 노출
```properties
# src/main/resources/application.properties
spring.datasource.password=1q2w3e4r!  # ❌ 평문으로 노출됨
```
**개선방안**: 환경변수 사용
```properties
spring.datasource.password=${DB_PASSWORD:default}
```

### 2. 입력 검증 부족
```java
// UserController.java - 서버 측 입력 검증 필요
@PostMapping("/register")
public ResponseEntity<ApiResponse<UserResponseDto>> register(@RequestBody UserRequestDto userRequestDto)
```
**개선방안**: `@Valid` 어노테이션과 Bean Validation 적용
```java
public ResponseEntity<ApiResponse<UserResponseDto>> register(@Valid @RequestBody UserRequestDto userRequestDto)
```

### 3. 에러 정보 과다 노출
```java
// UserService.java:33
throw new RuntimeException("이미 사용중인 아이디입니다");
```
**개선방안**: 커스텀 예외와 GlobalExceptionHandler 구현

## 🐛 **잠재적 버그**

### 1. API 엔드포인트 불일치
```java
// SecurityConfig.java:36 - 경로 불일치
.requestMatchers("/api/users/register", "/api/users/login", "/api/users/logout").permitAll()
// 실제 UserController는 "/api/user" 사용
```

### 2. 타이머 상태 동기화 문제
```typescript
// frontend/todolist/src/components/Stopwatch.tsx:15
const savedTime = localStorage.getItem(`stopwatch-${todoId}`);
// 브라우저 새로고침 시 서버와 클라이언트 시간 불일치 가능
```

### 3. 포인트 계산 로직 취약성
```java
// TodoService.java:84 - Integer 오버플로우 가능성
Long totalSeconds = leftoverSeconds + seconds;
Integer newLeftoverSeconds = (int) (totalSeconds % 60);
```

## 🚀 **성능 개선점**

### 1. 데이터베이스 인덱스 부족
```java
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_id", columnList = "userId", unique = true)
})
public class User { ... }
```

### 2. N+1 쿼리 최적화
```java
@Query("SELECT t FROM Todo t JOIN FETCH t.user WHERE t.user = :user")
List<Todo> findByUserWithFetch(@Param("user") User user);
```

### 3. 페이징 구현 필요
```java
// TodoRepository.java - 페이징 추가
Page<Todo> findByUser(User user, Pageable pageable);
```

## 🔧 **우선 개선사항**

### 1. 환경별 설정 분리
```yaml
# application-prod.yml 생성
spring:
  datasource:
    password: ${DB_PASSWORD}
  jpa:
    show-sql: false
```

### 2. 입력 검증 강화
```java
public class UserRequestDto {
    @NotBlank(message = "아이디는 필수입니다")
    @Size(min = 4, max = 20, message = "아이디는 4-20자여야 합니다")
    private String userId;
    
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
    private String password;
}
```

### 3. 예외 처리 체계화
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }
}
```

### 4. 테스트 코드 작성
```java
@SpringBootTest
@Transactional
class TodoServiceTest {
    @Test
    void shouldCreateTodoSuccessfully() {
        // Given, When, Then 패턴으로 테스트 작성
    }
}
```

## 📊 **종합 평가**

**현재 상태**: 중급 수준의 잘 구조화된 풀스택 애플리케이션
**강점**: 명확한 아키텍처, 모던 기술 스택, 타입 안정성
**개선점**: 보안 강화, 예외 처리, 테스트 코드, 성능 최적화

전체적으로 **견고한 기반 위에 구축된 좋은 프로젝트**이며, 제안된 개선사항들을 순차적으로 적용하면 프로덕션 레벨의 애플리케이션으로 발전시킬 수 있습니다.

---
📅 **리뷰 일자**: 2025-07-15  
🔍 **리뷰어**: Claude Code  
📝 **리뷰 범위**: 전체 코드베이스 (백엔드 + 프론트엔드)