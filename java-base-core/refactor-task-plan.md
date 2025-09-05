# 📝 Task Plan – Refactor `java-base-core`

## 🎯 Goal
Chuẩn hóa và refactor module **`java-base-core`** để cung cấp nền tảng tiện ích chung, dễ tái sử dụng cho tất cả dự án.  
Đảm bảo module **nhẹ, ổn định, có test coverage cao** và **dễ maintain lâu dài**.

---

## 📦 Scope
- Tổ chức lại cấu trúc `utils` theo domain rõ ràng.  
- Chuẩn hóa **exception handling**.  
- Thiết kế **common response model** thống nhất.  
- Bổ sung **documentation + test utilities**.  
- Áp dụng coding rules & engineering guidelines.  

---

## ✅ Deliverables
1. **Module structure** rõ ràng:
   - `io.github.base.core.utils`
   - `io.github.base.core.exception`
   - `io.github.base.core.model`
   - `io.github.base.core.logging`
   - `io.github.base.core.resilience`
2. Bộ **utility class** đã refactor, có unit test.  
3. **Exception hierarchy** chuẩn hóa + global error code.  
4. `CommonResponse<T>` model với metadata chuẩn.  
5. **Unit test coverage ≥ 85%**.  
6. Documentation (`docs/core.md`) với usage examples.  

---

## 🔨 Task Breakdown
- [ ] **CORE-001**: Restructure `utils` package  
  - StringUtils, DateTimeUtils, CollectionUtils, JsonUtils.  
  - Tách rõ domain, không mixed logic.  
- [ ] **CORE-002**: Exception handling  
  - BaseException (runtime).  
  - BusinessException, ValidationException, SystemException.  
  - Standard error code registry.  
- [ ] **CORE-003**: Common response model  
  - `CommonResponse<T>` gồm:  
    - `status`, `message`, `code`, `data`, `timestamp`.  
  - Static factory methods: `success(data)`, `error(code, message)`.  
- [ ] **CORE-004**: Collection & Map Utils  
  - Null-safe operation (`emptyIfNull`, `isEmpty`).  
  - Convert List ↔ Set ↔ Map.  
  - Grouping, partitioning, flatten nested collections.  
  - Merge maps với conflict resolver.  
  👉 Giúp code clean, tránh null check lặp lại.  
- [ ] **CORE-005**: IO & File Utils  
  - Read/write file UTF-8.  
  - Safe close resource (try-with-resources helper).  
  - Temp file creation.  
  👉 Support test cases, config loader.  
- [ ] **CORE-006**: Test utilities  
  - AssertUtils (custom matcher).  
  - FixtureUtils (generate dummy object).  
- [ ] **CORE-007**: Unit tests  
  - Coverage ≥ 85% line + branch.  
  - Naming convention: `*Test.java`.  
- [ ] **CORE-008**: Documentation  
  - Javadoc cho public API nếu có.  


---

## 📏 Acceptance Criteria
- Module **build & test pass**.  
- **SonarQube Quality Gate pass** (no blocker/critical issues, coverage ≥ 85%).  
- API contract của `CommonResponse` được approved.  
- Exception hierarchy được approved bởi team.  
- Documentation đầy đủ, dễ onboard.  

---

## ⚠️ Risks & Mitigation
- **Over-engineering utils** → Giới hạn scope, chỉ giữ generic/common functions.  
- **Inconsistent error codes** → Define `ErrorCodeRegistry` ngay từ đầu.  
- **Breaking changes cho dự án đang dùng** → Bump version **MAJOR** theo SemVer.  

---

## 🔮 Extension & Reusability
- Chuẩn bị **base test utilities** để các module khác (cache, persistence, messaging) reuse.  
- Standardize **error code & response model** → áp dụng cho toàn bộ API layer nếu có.  
- Tạo **Archetype/Starter** tự động import `java-base-core`.  
- Sau khi ổn định → publish doc site với code sample.  