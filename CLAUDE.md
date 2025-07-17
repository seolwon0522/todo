
## Implementation Best Practices

### 0 — Purpose  

이 규칙들은 유지보수성, 안전성, 개발자 생산성을 보장합니다.
**MUST** 규칙은 CI에 의해 강제되며, **SHOULD** 규칙은 강력히 권장됩니다.

---

### 1 — Before Coding

- **BP-1 (MUST)** 사용자에게 명확화 질문을 하세요.
- **BP-2 (SHOULD)** 복잡한 작업에 대해 접근 방식을 초안하고 확인하세요.  
- **BP-3 (SHOULD)** 2개 이상의 접근 방식이 있다면, 명확한 장단점을 나열하세요.
- **BP-4 (MUST)** 모든 응답은 한글로 하세요.

---

### 2 — While Coding

- **C-1 (MUST)** TDD를 따르세요: stub 스캐폴드 -> 실패하는 테스트 작성 -> 구현.
- **C-2 (MUST)** 일관성을 위해 기존 도메인 어휘로 함수 이름을 지으세요.  
- **C-3 (SHOULD NOT)** 작은 테스트 가능한 함수로 충분할 때 클래스를 도입하지 마세요.  
- **C-4 (SHOULD)** 간단하고, 구성 가능하고, 테스트 가능한 함수를 선호하세요.
- **C-5 (MUST)** ID에 대해 branded `type`을 선호하세요
  ```ts
  type UserId = Brand<string, 'UserId'>   // ✅ Good
  type UserId = string                    // ❌ Bad
  ```  
- **C-6 (MUST)** 타입 전용 import에 `import type { … }`를 사용하세요.
- **C-7 (SHOULD NOT)** 중요한 주의사항을 제외하고는 주석을 추가하지 마세요; 자명한 코드에 의존하세요.
- **C-8 (SHOULD)** 기본적으로 `type`을 사용하고; 더 읽기 쉽거나 인터페이스 병합이 필요할 때만 `interface`를 사용하세요. 
- **C-9 (SHOULD NOT)** 다른 곳에서 재사용되지 않고, 테스트할 수 없는 로직을 단위 테스트하는 유일한 방법이 아니며, 불투명한 블록의 가독성을 극적으로 향상시키지 않는다면 새 함수를 추출하지 마세요.

---

### 3 — Testing

- **T-1 (MUST)** 간단한 함수의 경우, 소스 파일과 같은 디렉토리의 `*.spec.ts`에 단위 테스트를 함께 배치하세요.
- **T-2 (MUST)** API 변경의 경우, `packages/api/test/*.spec.ts`에 통합 테스트를 추가/확장하세요.
- **T-3 (MUST)** 항상 순수 로직 단위 테스트와 DB를 건드리는 통합 테스트를 분리하세요.
- **T-4 (SHOULD)** 과도한 모킹보다 통합 테스트를 선호하세요.  
- **T-5 (SHOULD)** 복잡한 알고리즘을 철저히 단위 테스트하세요.
- **T-6 (SHOULD)** 가능하면 하나의 assertion으로 전체 구조를 테스트하세요
  ```ts
  expect(result).toBe([value]) // Good

  expect(result).toHaveLength(1); // Bad
  expect(result[0]).toBe(value); // Bad
  ```

---

### 4 — Database

- **D-1 (MUST)** DB 헬퍼를 `KyselyDatabase | Transaction<Database>`로 타입하여 트랜잭션과 DB 인스턴스 모두에서 작동하도록 하세요.  
- **D-2 (SHOULD)** `packages/shared/src/db-types.override.ts`에서 잘못된 생성된 타입을 오버라이드하세요. 예: 자동 생성된 타입이 잘못된 BigInt 값을 보여주므로 수동으로 `string`으로 오버라이드합니다.

---

### 5 — Code Organization

- **O-1 (MUST)** 2개 이상의 패키지에서 사용되는 경우에만 `packages/shared`에 코드를 배치하세요.

---

### 6 — Tooling Gates

- **G-1 (MUST)** `prettier --check`가 통과해야 합니다.  
- **G-2 (MUST)** `turbo typecheck lint`가 통과해야 합니다.  

---

### 7 - Git

- **GH-1 (MUST)** 커밋 메시지 작성 시 Conventional Commits 형식을 사용하세요: https://www.conventionalcommits.org/en/v1.0.0
- **GH-2 (SHOULD NOT)** 커밋 메시지에서 Claude 또는 Anthropic을 언급하지 마세요.

---

## Writing Functions Best Practices

구현한 함수가 좋은지 평가할 때 이 체크리스트를 사용하세요:

1. 함수를 읽고 정말로 쉽게 무엇을 하는지 따라갈 수 있나요? 그렇다면 여기서 멈추세요.
2. 함수가 매우 높은 순환 복잡도를 가지고 있나요? (독립적인 경로의 수, 또는 많은 경우 if-else 중첩의 수를 대리로). 그렇다면 아마 문제가 있을 것입니다.
3. 이 함수를 훨씬 따라가기 쉽고 더 견고하게 만들 수 있는 공통 데이터 구조와 알고리즘이 있나요? 파서, 트리, 스택/큐 등.
4. 함수에 사용되지 않는 매개변수가 있나요?
5. 함수 인수로 이동할 수 있는 불필요한 타입 캐스트가 있나요?
6. 핵심 기능(예: sql 쿼리, redis 등)을 모킹하지 않고 함수를 쉽게 테스트할 수 있나요? 그렇지 않다면, 이 함수를 통합 테스트의 일부로 테스트할 수 있나요?
7. 숨겨진 테스트되지 않은 종속성이나 대신 인수로 분해할 수 있는 값이 있나요? 실제로 변경되거나 함수에 영향을 줄 수 있는 중요한 종속성만 신경 쓰세요.
8. 3개의 더 나은 함수 이름을 브레인스토밍하고 현재 이름이 최선인지, 나머지 코드베이스와 일치하는지 확인하세요.

중요: 다음과 같은 강력한 필요가 없는 한 별도의 함수로 리팩토링해서는 안 됩니다:
  - 리팩토링된 함수가 한 곳 이상에서 사용됨
  - 리팩토링된 함수는 쉽게 단위 테스트할 수 있지만 원래 함수는 그렇지 않고 다른 방법으로 테스트할 수 없음
  - 원래 함수가 따라가기 극도로 어렵고 설명하기 위해 주석을 여기저기 넣어야 함

## Writing Tests Best Practices

구현한 테스트가 좋은지 평가할 때 이 체크리스트를 사용하세요:

1. 입력을 매개변수화해야 합니다; 42나 "foo"와 같은 설명되지 않은 리터럴을 테스트에 직접 포함하지 마세요.
2. 실제 결함으로 실패할 수 있는 경우가 아니라면 테스트를 추가해서는 안 됩니다. 사소한 어설션(예: expect(2).toBe(2))은 금지됩니다.
3. 테스트 설명이 최종 expect가 검증하는 것을 정확히 명시해야 합니다. 문구와 어설션이 일치하지 않으면 이름을 바꾸거나 다시 작성하세요.
4. 함수의 출력을 오라클로 재사용하는 것이 아니라 독립적이고 미리 계산된 기대치나 도메인의 속성과 결과를 비교해야 합니다.
5. 프로덕션 코드와 동일한 린트, 타입 안전성, 스타일 규칙을 따라야 합니다(prettier, ESLint, 엄격한 타입).
6. 실용적일 때마다 단일 하드코딩된 케이스보다는 불변량이나 공리(예: 교환법칙, 멱등성, 라운드트립)를 표현해야 합니다. `fast-check` 라이브러리 사용 예:
```
import fc from 'fast-check';
import { describe, expect, test } from 'vitest';
import { getCharacterCount } from './string';

describe('properties', () => {
  test('concatenation functoriality', () => {
    fc.assert(
      fc.property(
        fc.string(),
        fc.string(),
        (a, b) =>
          getCharacterCount(a + b) ===
          getCharacterCount(a) + getCharacterCount(b)
      )
    );
  });
});
```

7. 함수에 대한 단위 테스트는 `describe(functionName, () => ...` 아래에 그룹화되어야 합니다.
8. 무엇이든 될 수 있는 매개변수를 테스트할 때 `expect.any(...)`를 사용하세요(예: 변수 ID).
9. 항상 약한 어설션보다 강한 어설션을 사용하세요. 예: `expect(x).toBeGreaterThanOrEqual(1)` 대신 `expect(x).toEqual(1)`.
10. 엣지 케이스, 현실적인 입력, 예상치 못한 입력, 값 경계를 테스트해야 합니다.
11. 타입 체커에 의해 잡히는 조건은 테스트하지 마세요.

## Code Organization

- `packages/api` - Fastify API 서버
  - `packages/api/src/publisher/*.ts` - 소셜 미디어 플랫폼에 게시하는 특정 구현
- `packages/web` - App Router를 사용한 Next.js 15 앱
- `packages/shared` - 공유 타입 및 유틸리티
  - `packages/shared/social.ts` - 소셜 미디어 플랫폼에 대한 문자 크기 및 미디어 검증
- `packages/api-schema` - TypeBox를 사용한 API 계약 스키마

## Remember Shortcuts

사용자가 언제든지 호출할 수 있는 다음 단축키를 기억하세요.

### QNEW

"qnew"를 입력하면 다음을 의미합니다:

```
CLAUDE.md에 나열된 모든 BEST PRACTICES를 이해하세요.
당신의 코드는 항상 이러한 베스트 프랙티스를 따라야 합니다.
```

### QPLAN
"qplan"을 입력하면 다음을 의미합니다:
```
코드베이스의 유사한 부분을 분석하고 당신의 계획이:
- 나머지 코드베이스와 일치하는지
- 최소한의 변경을 도입하는지
- 기존 코드를 재사용하는지
를 결정하세요.
```

## QCODE

"qcode"를 입력하면 다음을 의미합니다:

```
계획을 구현하고 새 테스트가 통과하는지 확인하세요.
다른 것을 깨뜨리지 않았는지 확인하기 위해 항상 테스트를 실행하세요.
표준 형식을 보장하기 위해 새로 생성된 파일에 항상 `prettier`를 실행하세요.
타입 체킹과 린팅이 통과하는지 확인하기 위해 항상 `turbo typecheck lint`를 실행하세요.
```

### QCHECK

"qcheck"를 입력하면 다음을 의미합니다:

```
당신은 회의적인 시니어 소프트웨어 엔지니어입니다.
도입한 모든 주요 코드 변경에 대해 이 분석을 수행하세요(사소한 변경은 제외):

1. CLAUDE.md 체크리스트 Writing Functions Best Practices.
2. CLAUDE.md 체크리스트 Writing Tests Best Practices.
3. CLAUDE.md 체크리스트 Implementation Best Practices.
```

### QCHECKF

"qcheckf"를 입력하면 다음을 의미합니다:

```
당신은 회의적인 시니어 소프트웨어 엔지니어입니다.
추가하거나 편집한 모든 주요 함수에 대해 이 분석을 수행하세요(사소한 변경은 제외):

1. CLAUDE.md 체크리스트 Writing Functions Best Practices.
```

### QCHECKT

"qcheckt"를 입력하면 다음을 의미합니다:

```
당신은 회의적인 시니어 소프트웨어 엔지니어입니다.
추가하거나 편집한 모든 주요 테스트에 대해 이 분석을 수행하세요(사소한 변경은 제외):

1. CLAUDE.md 체크리스트 Writing Tests Best Practices.
```

### QUX

"qux"를 입력하면 다음을 의미합니다:

```
구현한 기능의 인간 UX 테스터라고 상상해보세요. 
테스트할 포괄적인 시나리오 목록을 우선순위가 높은 순으로 정렬하여 출력하세요.
```

### QGIT

"qgit"를 입력하면 다음을 의미합니다:

```
모든 변경 사항을 스테이징에 추가하고, 커밋을 생성하고, 원격으로 푸시하세요.

커밋 메시지 작성을 위한 이 체크리스트를 따르세요:
- Conventional Commits 형식을 사용해야 합니다: https://www.conventionalcommits.org/en/v1.0.0
- 커밋 메시지에서 Claude 또는 Anthropic을 언급해서는 안 됩니다.
- 커밋 메시지를 다음과 같이 구조화해야 합니다:
<type>[optional scope]: <description>
[optional body]
[optional footer(s)]
- 커밋은 의도를 전달하기 위해 다음 구조적 요소를 포함해야 합니다: 
fix: fix 타입의 커밋은 코드베이스의 버그를 패치합니다(이는 Semantic Versioning의 PATCH와 상관관계가 있습니다).
feat: feat 타입의 커밋은 코드베이스에 새로운 기능을 도입합니다(이는 Semantic Versioning의 MINOR와 상관관계가 있습니다).
BREAKING CHANGE: 푸터에 BREAKING CHANGE:가 있거나 타입/범위 뒤에 !를 추가한 커밋은 중단적 API 변경을 도입합니다(Semantic Versioning의 MAJOR와 상관관계가 있습니다). BREAKING CHANGE는 모든 타입의 커밋의 일부가 될 수 있습니다.
fix:와 feat: 이외의 타입도 허용됩니다. 예를 들어 @commitlint/config-conventional(Angular 규칙 기반)은 build:, chore:, ci:, docs:, style:, refactor:, perf:, test: 등을 권장합니다.
BREAKING CHANGE: <description> 이외의 푸터도 제공될 수 있으며 git trailer 형식과 유사한 규칙을 따릅니다.
```